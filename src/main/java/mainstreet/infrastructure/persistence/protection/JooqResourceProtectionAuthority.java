package mainstreet.infrastructure.persistence.protection;

import grandrue.protection.ProtectionAdmissionDecision;
import grandrue.protection.ProtectionPolicy;
import grandrue.protection.ProtectionStateFailureBehaviour;
import grandrue.protection.ProtectionSubject;
import grandrue.protection.ProtectionTarget;
import grandrue.protection.ResourceProtectionAdmissionAuthority;
import grandrue.protection.TemporaryProtectiveRestriction;
import grandrue.protection.TemporaryProtectiveRestrictionStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PostgreSQL/jOOQ Resource Protection adapter for bounded-window consumption
 * accounting and bounded temporary restrictions.
 *
 * <p>Admission and consumption accounting are one transaction at the protected
 * subject/target boundary. Resource Protection remains an operational admission
 * authority only; it never mutates commercial, account or capability truth.</p>
 */
public final class JooqResourceProtectionAuthority
        implements ResourceProtectionAdmissionAuthority,
        TemporaryProtectiveRestrictionStore {

    private static final Table<?> WINDOW =
            DSL.table(DSL.name("resource_protection_consumption_window"));
    private static final Table<?> EVIDENCE =
            DSL.table(DSL.name("resource_protection_consumption_evidence"));
    private static final Table<?> RESTRICTION =
            DSL.table(DSL.name("temporary_protective_restriction"));
    private static final Table<?> RESTRICTION_TARGET =
            DSL.table(DSL.name("temporary_protective_restriction_target"));

    private static final Field<String> POLICY_ID =
            DSL.field(DSL.name("policy_identifier"), String.class);
    private static final Field<Integer> POLICY_VERSION =
            DSL.field(DSL.name("policy_version"), Integer.class);
    private static final Field<String> TARGET_ID =
            DSL.field(DSL.name("target_identifier"), String.class);
    private static final Field<String> SUBJECT_SCOPE_ID =
            DSL.field(DSL.name("subject_scope_identifier"), String.class);
    private static final Field<String> SUBJECT_ID =
            DSL.field(DSL.name("subject_identifier"), String.class);
    private static final Field<Instant> WINDOW_START =
            DSL.field(DSL.name("window_starts_at"), Instant.class);
    private static final Field<Instant> WINDOW_END =
            DSL.field(DSL.name("window_ends_at"), Instant.class);
    private static final Field<Long> CONSUMED_UNITS =
            DSL.field(DSL.name("consumed_units"), Long.class);
    private static final Field<String> MEASUREMENT_BASIS_ID =
            DSL.field(DSL.name("measurement_basis_identifier"), String.class);
    private static final Field<String> CONSUMPTION_ID =
            DSL.field(DSL.name("consumption_identity"), String.class);
    private static final Field<Instant> ADMITTED_AT =
            DSL.field(DSL.name("admitted_at"), Instant.class);
    private static final Field<Long> UNITS =
            DSL.field(DSL.name("units"), Long.class);

    private static final Field<String> RESTRICTION_ID =
            DSL.field(DSL.name("restriction_identity"), String.class);
    private static final Field<Instant> EFFECTIVE_FROM =
            DSL.field(DSL.name("effective_from"), Instant.class);
    private static final Field<Instant> EXPIRES_AT =
            DSL.field(DSL.name("expires_at"), Instant.class);
    private static final Field<String> REASON_CLASS_ID =
            DSL.field(DSL.name("reason_class_identifier"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactions;
    private final Clock clock;
    private final Set<ProtectionTarget> registeredTargets;

    public JooqResourceProtectionAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Clock clock,
            Set<ProtectionTarget> registeredTargets
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.clock = Objects.requireNonNull(clock, "clock");
        this.registeredTargets = Set.copyOf(
                Objects.requireNonNull(registeredTargets, "registeredTargets")
        );
    }

    @Override
    public ProtectionAdmissionDecision admit(
            ProtectionPolicy policy,
            ProtectionSubject subject,
            String consumptionIdentity,
            long units,
            boolean owningExecutionSupportsDeferral
    ) {
        Objects.requireNonNull(policy, "policy");
        Objects.requireNonNull(subject, "subject");
        requireIdentifier(consumptionIdentity, "Consumption identity");
        if (units < 1) {
            throw new IllegalArgumentException(
                    "Protection consumption units must be positive"
            );
        }
        requireRegistered(policy.target());
        if (!policy.appliesTo(subject)) {
            throw new IllegalArgumentException(
                    "Protection policy does not apply to subject scope: "
                            + subject.scopeIdentifier()
            );
        }

        Instant now = clock.instant();
        try {
            ProtectionAdmissionDecision result = transactions.execute(status -> {
                lockSubjectTarget(subject, policy.target());
                lockConsumptionIdentity(policy, subject, consumptionIdentity);

                if (hasActiveRestriction(subject, policy.target(), now)) {
                    return ProtectionAdmissionDecision.REJECT;
                }
                if (alreadyAdmitted(policy, subject, consumptionIdentity)) {
                    return ProtectionAdmissionDecision.ADMIT;
                }

                WindowBounds bounds = windowBounds(now, policy.window());
                ensureWindow(policy, subject, bounds);
                Record state = lockWindow(policy, subject, bounds.startsAt());
                long consumed = Objects.requireNonNull(state.get(CONSUMED_UNITS));

                if (units > policy.capacityUnits() - consumed) {
                    if (policy.exhaustionDecision() == ProtectionAdmissionDecision.DEFER
                            && owningExecutionSupportsDeferral) {
                        return ProtectionAdmissionDecision.DEFER;
                    }
                    return ProtectionAdmissionDecision.REJECT;
                }

                dsl.update(WINDOW)
                        .set(CONSUMED_UNITS, consumed + units)
                        .where(POLICY_ID.eq(policy.policyIdentifier()))
                        .and(POLICY_VERSION.eq(policy.version()))
                        .and(TARGET_ID.eq(policy.target().identifier()))
                        .and(SUBJECT_SCOPE_ID.eq(subject.scopeIdentifier()))
                        .and(SUBJECT_ID.eq(subject.subjectIdentifier()))
                        .and(WINDOW_START.eq(bounds.startsAt()))
                        .execute();

                dsl.insertInto(EVIDENCE)
                        .columns(
                                TARGET_ID,
                                SUBJECT_SCOPE_ID,
                                SUBJECT_ID,
                                MEASUREMENT_BASIS_ID,
                                CONSUMPTION_ID,
                                POLICY_ID,
                                POLICY_VERSION,
                                WINDOW_START,
                                ADMITTED_AT,
                                UNITS
                        )
                        .values(
                                policy.target().identifier(),
                                subject.scopeIdentifier(),
                                subject.subjectIdentifier(),
                                policy.measurementBasisIdentifier(),
                                consumptionIdentity,
                                policy.policyIdentifier(),
                                policy.version(),
                                bounds.startsAt(),
                                now,
                                units
                        )
                        .execute();
                return ProtectionAdmissionDecision.ADMIT;
            });
            return Objects.requireNonNull(
                    result,
                    "Protection transaction returned no decision"
            );
        } catch (org.jooq.exception.DataAccessException | TransactionException failure) {
            return policy.stateFailureBehaviour() == ProtectionStateFailureBehaviour.ADMIT
                    ? ProtectionAdmissionDecision.ADMIT
                    : ProtectionAdmissionDecision.REJECT;
        }
    }

    @Override
    public TemporaryProtectiveRestriction establish(
            TemporaryProtectiveRestriction restriction
    ) {
        Objects.requireNonNull(restriction, "restriction");
        restriction.targets().forEach(this::requireRegistered);

        TemporaryProtectiveRestriction result = transactions.execute(status -> {
            restriction.targets().stream()
                    .sorted(Comparator.comparing(ProtectionTarget::identifier))
                    .forEach(target -> lockSubjectTarget(restriction.subject(), target));
            lockRestrictionIdentity(restriction.restrictionIdentity());

            TemporaryProtectiveRestriction existing = existingRestriction(
                    restriction.restrictionIdentity()
            );
            if (existing != null) {
                if (!existing.equals(restriction)) {
                    throw new IllegalArgumentException(
                            "Restriction identity cannot be reused for different intent"
                    );
                }
                return existing;
            }

            dsl.insertInto(RESTRICTION)
                    .columns(
                            RESTRICTION_ID,
                            SUBJECT_SCOPE_ID,
                            SUBJECT_ID,
                            EFFECTIVE_FROM,
                            EXPIRES_AT,
                            REASON_CLASS_ID
                    )
                    .values(
                            restriction.restrictionIdentity(),
                            restriction.subject().scopeIdentifier(),
                            restriction.subject().subjectIdentifier(),
                            restriction.effectiveFrom(),
                            restriction.expiresAt(),
                            restriction.reasonClassIdentifier()
                    )
                    .execute();
            restriction.targets().stream()
                    .sorted(Comparator.comparing(ProtectionTarget::identifier))
                    .forEach(target -> dsl.insertInto(RESTRICTION_TARGET)
                            .columns(RESTRICTION_ID, TARGET_ID)
                            .values(
                                    restriction.restrictionIdentity(),
                                    target.identifier()
                            )
                            .execute());
            return restriction;
        });
        return Objects.requireNonNull(
                result,
                "Restriction transaction returned no result"
        );
    }

    private void ensureWindow(
            ProtectionPolicy policy,
            ProtectionSubject subject,
            WindowBounds bounds
    ) {
        dsl.insertInto(WINDOW)
                .columns(
                        POLICY_ID,
                        POLICY_VERSION,
                        TARGET_ID,
                        SUBJECT_SCOPE_ID,
                        SUBJECT_ID,
                        WINDOW_START,
                        WINDOW_END,
                        CONSUMED_UNITS
                )
                .values(
                        policy.policyIdentifier(),
                        policy.version(),
                        policy.target().identifier(),
                        subject.scopeIdentifier(),
                        subject.subjectIdentifier(),
                        bounds.startsAt(),
                        bounds.endsAt(),
                        0L
                )
                .onConflictDoNothing()
                .execute();
    }

    private Record lockWindow(
            ProtectionPolicy policy,
            ProtectionSubject subject,
            Instant startsAt
    ) {
        Record record = dsl.select(CONSUMED_UNITS)
                .from(WINDOW)
                .where(POLICY_ID.eq(policy.policyIdentifier()))
                .and(POLICY_VERSION.eq(policy.version()))
                .and(TARGET_ID.eq(policy.target().identifier()))
                .and(SUBJECT_SCOPE_ID.eq(subject.scopeIdentifier()))
                .and(SUBJECT_ID.eq(subject.subjectIdentifier()))
                .and(WINDOW_START.eq(startsAt))
                .forUpdate()
                .fetchOne();
        if (record == null) {
            throw new IllegalStateException(
                    "Protection consumption window disappeared"
            );
        }
        return record;
    }

    private boolean alreadyAdmitted(
            ProtectionPolicy policy,
            ProtectionSubject subject,
            String consumptionIdentity
    ) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(EVIDENCE)
                        .where(TARGET_ID.eq(policy.target().identifier()))
                        .and(SUBJECT_SCOPE_ID.eq(subject.scopeIdentifier()))
                        .and(SUBJECT_ID.eq(subject.subjectIdentifier()))
                        .and(MEASUREMENT_BASIS_ID.eq(
                                policy.measurementBasisIdentifier()
                        ))
                        .and(CONSUMPTION_ID.eq(consumptionIdentity))
        );
    }

    private boolean hasActiveRestriction(
            ProtectionSubject subject,
            ProtectionTarget target,
            Instant instant
    ) {
        Field<String> outerRestrictionId = DSL.field(
                DSL.name("temporary_protective_restriction", "restriction_identity"),
                String.class
        );
        Field<String> outerScope = DSL.field(
                DSL.name("temporary_protective_restriction", "subject_scope_identifier"),
                String.class
        );
        Field<String> outerSubject = DSL.field(
                DSL.name("temporary_protective_restriction", "subject_identifier"),
                String.class
        );
        Field<Instant> outerFrom = DSL.field(
                DSL.name("temporary_protective_restriction", "effective_from"),
                Instant.class
        );
        Field<Instant> outerExpiry = DSL.field(
                DSL.name("temporary_protective_restriction", "expires_at"),
                Instant.class
        );
        Field<String> innerRestrictionId = DSL.field(
                DSL.name(
                        "temporary_protective_restriction_target",
                        "restriction_identity"
                ),
                String.class
        );
        Field<String> innerTarget = DSL.field(
                DSL.name(
                        "temporary_protective_restriction_target",
                        "target_identifier"
                ),
                String.class
        );

        return dsl.fetchExists(
                dsl.selectOne()
                        .from(RESTRICTION)
                        .where(outerScope.eq(subject.scopeIdentifier()))
                        .and(outerSubject.eq(subject.subjectIdentifier()))
                        .and(outerFrom.le(instant))
                        .and(outerExpiry.gt(instant))
                        .and(DSL.exists(
                                dsl.selectOne()
                                        .from(RESTRICTION_TARGET)
                                        .where(innerRestrictionId.eq(outerRestrictionId))
                                        .and(innerTarget.eq(target.identifier()))
                        ))
        );
    }

    private TemporaryProtectiveRestriction existingRestriction(String identity) {
        Record record = dsl.select(
                        RESTRICTION_ID,
                        SUBJECT_SCOPE_ID,
                        SUBJECT_ID,
                        EFFECTIVE_FROM,
                        EXPIRES_AT,
                        REASON_CLASS_ID
                )
                .from(RESTRICTION)
                .where(RESTRICTION_ID.eq(identity))
                .fetchOne();
        if (record == null) {
            return null;
        }
        Set<ProtectionTarget> targets = dsl
                .select(TARGET_ID)
                .from(RESTRICTION_TARGET)
                .where(RESTRICTION_ID.eq(identity))
                .fetch(TARGET_ID)
                .stream()
                .map(ProtectionTarget::new)
                .collect(Collectors.toUnmodifiableSet());
        return new TemporaryProtectiveRestriction(
                record.get(RESTRICTION_ID),
                new ProtectionSubject(
                        record.get(SUBJECT_SCOPE_ID),
                        record.get(SUBJECT_ID)
                ),
                targets,
                record.get(EFFECTIVE_FROM),
                record.get(EXPIRES_AT),
                record.get(REASON_CLASS_ID)
        );
    }

    private void lockSubjectTarget(
            ProtectionSubject subject,
            ProtectionTarget target
    ) {
        String key = subject.scopeIdentifier()
                + "\u001f" + subject.subjectIdentifier()
                + "\u001f" + target.identifier();
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 75))",
                key
        );
    }

    private void lockConsumptionIdentity(
            ProtectionPolicy policy,
            ProtectionSubject subject,
            String consumptionIdentity
    ) {
        String key = policy.target().identifier()
                + "\u001f" + subject.scopeIdentifier()
                + "\u001f" + subject.subjectIdentifier()
                + "\u001f" + policy.measurementBasisIdentifier()
                + "\u001f" + consumptionIdentity;
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 73))",
                key
        );
    }

    private void lockRestrictionIdentity(String restrictionIdentity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 74))",
                restrictionIdentity
        );
    }

    private void requireRegistered(ProtectionTarget target) {
        if (!registeredTargets.contains(Objects.requireNonNull(target, "target"))) {
            throw new IllegalArgumentException(
                    "Protection target is not registered: " + target.identifier()
            );
        }
    }

    private static WindowBounds windowBounds(Instant instant, Duration duration) {
        long windowMillis;
        try {
            windowMillis = duration.toMillis();
        } catch (ArithmeticException overflow) {
            throw new IllegalArgumentException(
                    "Protection window is too large",
                    overflow
            );
        }
        if (windowMillis < 1) {
            throw new IllegalArgumentException(
                    "Bounded-window protection requires at least one millisecond"
            );
        }
        long epochMillis = instant.toEpochMilli();
        long startMillis = Math.floorDiv(epochMillis, windowMillis) * windowMillis;
        Instant start = Instant.ofEpochMilli(startMillis);
        return new WindowBounds(start, start.plusMillis(windowMillis));
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private record WindowBounds(Instant startsAt, Instant endsAt) {
    }
}
