package mainstreet.infrastructure.persistence.credential;

import mainstreet.application.MerchantScope;
import mainstreet.credential.CredentialBinding;
import mainstreet.credential.CredentialBindingScope;
import mainstreet.credential.CredentialGeneration;
import mainstreet.credential.CredentialGenerationPolicy;
import mainstreet.credential.CredentialGenerationState;
import mainstreet.credential.CredentialSecurityStore;
import mainstreet.credential.CredentialTechnicalUse;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * PostgreSQL/jOOQ persistence for non-secret credential security metadata.
 * Raw credential values never enter this adapter or its tables.
 */
public final class JooqCredentialSecurityStore implements CredentialSecurityStore {

    private static final Table<?> BINDING = DSL.table(DSL.name("credential_binding"));
    private static final Table<?> GENERATION = DSL.table(DSL.name("credential_generation"));
    private static final Table<?> POLICY = DSL.table(DSL.name("credential_generation_policy"));

    private static final Field<String> BINDING_IDENTIFIER =
            DSL.field(DSL.name("binding_identifier"), String.class);
    private static final Field<String> BINDING_SCOPE =
            DSL.field(DSL.name("binding_scope"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> RESPONSIBILITY_REFERENCE =
            DSL.field(DSL.name("responsibility_reference"), String.class);
    private static final Field<String> EXTERNAL_CONTEXT_REFERENCE =
            DSL.field(DSL.name("external_context_reference"), String.class);
    private static final Field<String> TECHNICAL_PURPOSE_IDENTIFIER =
            DSL.field(DSL.name("technical_purpose_identifier"), String.class);
    private static final Field<Instant> ESTABLISHED_AT =
            DSL.field(DSL.name("established_at"), Instant.class);

    private static final Field<String> GENERATION_IDENTIFIER =
            DSL.field(DSL.name("generation_identifier"), String.class);
    private static final Field<String> PROTECTED_MATERIAL_REFERENCE =
            DSL.field(DSL.name("protected_material_reference"), String.class);
    private static final Field<Instant> REGISTERED_AT =
            DSL.field(DSL.name("registered_at"), Instant.class);

    private static final Field<String> POLICY_IDENTIFIER =
            DSL.field(DSL.name("policy_identifier"), String.class);
    private static final Field<Long> POLICY_SEQUENCE =
            DSL.field(DSL.name("policy_sequence"), Long.class);
    private static final Field<String> STATE =
            DSL.field(DSL.name("state"), String.class);
    private static final Field<Boolean> ALLOW_NEW_EXECUTION =
            DSL.field(DSL.name("allow_new_execution"), Boolean.class);
    private static final Field<Boolean> ALLOW_EXISTING_OBLIGATION =
            DSL.field(DSL.name("allow_existing_obligation"), Boolean.class);
    private static final Field<Boolean> ALLOW_VERIFICATION =
            DSL.field(DSL.name("allow_verification"), Boolean.class);
    private static final Field<Instant> EFFECTIVE_AT =
            DSL.field(DSL.name("effective_at"), Instant.class);
    private static final Field<String> EVIDENCE_REFERENCE =
            DSL.field(DSL.name("evidence_reference"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqCredentialSecurityStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public CredentialBinding registerBinding(CredentialBinding candidate) {
        Objects.requireNonNull(candidate, "candidate");
        CredentialBinding result = transactionTemplate.execute(status -> {
            lockIdentity("binding", candidate.bindingIdentity());
            Optional<CredentialBinding> existing = binding(candidate.bindingIdentity());
            if (existing.isPresent()) {
                CredentialBinding committed = existing.orElseThrow();
                if (!committed.equals(candidate)) {
                    throw new IllegalStateException(
                            "Credential binding identity already exists with different metadata"
                    );
                }
                return committed;
            }

            dsl.insertInto(BINDING)
                    .columns(
                            BINDING_IDENTIFIER,
                            BINDING_SCOPE,
                            MERCHANT_IDENTIFIER,
                            RESPONSIBILITY_REFERENCE,
                            EXTERNAL_CONTEXT_REFERENCE,
                            TECHNICAL_PURPOSE_IDENTIFIER,
                            ESTABLISHED_AT
                    )
                    .values(
                            candidate.bindingIdentity(),
                            candidate.scope().name(),
                            candidate.merchantScope()
                                    .map(MerchantScope::merchantIdentifier)
                                    .orElse(null),
                            candidate.responsibilityReference(),
                            candidate.externalContextReference(),
                            candidate.technicalPurposeIdentifier(),
                            candidate.establishedAt()
                    )
                    .execute();
            return candidate;
        });
        return Objects.requireNonNull(result, "Credential binding transaction returned no result");
    }

    @Override
    public CredentialGeneration registerGeneration(
            CredentialGeneration generation,
            CredentialGenerationPolicy initialPolicy
    ) {
        Objects.requireNonNull(generation, "generation");
        Objects.requireNonNull(initialPolicy, "initialPolicy");
        requireInitialPolicy(generation, initialPolicy);

        CredentialGeneration result = transactionTemplate.execute(status -> {
            lockIdentity("generation", generation.generationIdentity());
            Optional<CredentialGeneration> existing = generation(generation.generationIdentity());
            if (existing.isPresent()) {
                CredentialGeneration committed = existing.orElseThrow();
                if (!committed.equals(generation)) {
                    throw new IllegalStateException(
                            "Credential generation identity already exists with different metadata"
                    );
                }
                CredentialGenerationPolicy committedPolicy = policy(initialPolicy.policyIdentity())
                        .orElseThrow(() -> new IllegalStateException(
                                "Existing credential generation has no expected initial policy"
                        ));
                if (!committedPolicy.equals(initialPolicy)) {
                    throw new IllegalStateException(
                            "Initial credential policy replay does not match committed evidence"
                    );
                }
                return committed;
            }
            if (binding(generation.bindingIdentity()).isEmpty()) {
                throw new IllegalStateException(
                        "Credential generation references an unknown binding"
                );
            }

            dsl.insertInto(GENERATION)
                    .columns(
                            GENERATION_IDENTIFIER,
                            BINDING_IDENTIFIER,
                            PROTECTED_MATERIAL_REFERENCE,
                            REGISTERED_AT
                    )
                    .values(
                            generation.generationIdentity(),
                            generation.bindingIdentity(),
                            generation.protectedMaterialReference(),
                            generation.registeredAt()
                    )
                    .execute();
            insertPolicy(initialPolicy);
            return generation;
        });
        return Objects.requireNonNull(result, "Credential generation transaction returned no result");
    }

    @Override
    public CredentialGenerationPolicy transitionGeneration(
            String generationIdentity,
            CredentialGenerationState expectedCurrentState,
            CredentialGenerationPolicy nextPolicy
    ) {
        requireIdentifier(generationIdentity, "generationIdentity");
        Objects.requireNonNull(expectedCurrentState, "expectedCurrentState");
        Objects.requireNonNull(nextPolicy, "nextPolicy");
        if (!generationIdentity.equals(nextPolicy.generationIdentity())) {
            throw new IllegalArgumentException(
                    "Credential transition policy belongs to another generation"
            );
        }

        CredentialGenerationPolicy result = transactionTemplate.execute(status -> {
            lockIdentity("generation", generationIdentity);

            Optional<CredentialGenerationPolicy> replay = policy(nextPolicy.policyIdentity());
            if (replay.isPresent()) {
                CredentialGenerationPolicy committed = replay.orElseThrow();
                if (!committed.equals(nextPolicy)) {
                    throw new IllegalStateException(
                            "Credential policy identity already exists with different evidence"
                    );
                }
                return committed;
            }

            CredentialGenerationPolicy current = currentPolicy(generationIdentity)
                    .orElseThrow(() -> new IllegalStateException(
                            "Unknown credential generation or missing current policy"
                    ));
            if (current.state() != expectedCurrentState) {
                throw new IllegalStateException(
                        "Credential generation current state does not match expected state"
                );
            }
            requireAllowedTransition(current.state(), nextPolicy.state());
            if (nextPolicy.sequence() != current.sequence() + 1) {
                throw new IllegalArgumentException(
                        "Credential policy sequence must advance by exactly one"
                );
            }
            if (nextPolicy.effectiveAt().isBefore(current.effectiveAt())) {
                throw new IllegalArgumentException(
                        "Credential policy effective time must not move backwards"
                );
            }
            insertPolicy(nextPolicy);
            return nextPolicy;
        });
        return Objects.requireNonNull(result, "Credential transition transaction returned no result");
    }

    @Override
    public Optional<CredentialBinding> binding(String bindingIdentity) {
        requireIdentifier(bindingIdentity, "bindingIdentity");
        Record row = dsl.select(
                        BINDING_IDENTIFIER,
                        BINDING_SCOPE,
                        MERCHANT_IDENTIFIER,
                        RESPONSIBILITY_REFERENCE,
                        EXTERNAL_CONTEXT_REFERENCE,
                        TECHNICAL_PURPOSE_IDENTIFIER,
                        ESTABLISHED_AT
                )
                .from(BINDING)
                .where(BINDING_IDENTIFIER.eq(bindingIdentity))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toBinding);
    }

    @Override
    public Optional<CredentialGenerationPolicy> currentPolicy(String generationIdentity) {
        requireIdentifier(generationIdentity, "generationIdentity");
        Record row = dsl.select(
                        POLICY_IDENTIFIER,
                        GENERATION_IDENTIFIER,
                        POLICY_SEQUENCE,
                        STATE,
                        ALLOW_NEW_EXECUTION,
                        ALLOW_EXISTING_OBLIGATION,
                        ALLOW_VERIFICATION,
                        EFFECTIVE_AT,
                        EVIDENCE_REFERENCE
                )
                .from(POLICY)
                .where(GENERATION_IDENTIFIER.eq(generationIdentity))
                .orderBy(POLICY_SEQUENCE.desc())
                .limit(1)
                .fetchOne();
        return Optional.ofNullable(row).map(this::toPolicy);
    }

    @Override
    public List<CredentialGeneration> eligibleGenerations(
            String bindingIdentity,
            CredentialTechnicalUse technicalUse,
            Instant instant
    ) {
        requireIdentifier(bindingIdentity, "bindingIdentity");
        Objects.requireNonNull(technicalUse, "technicalUse");
        Objects.requireNonNull(instant, "instant");
        if (binding(bindingIdentity).isEmpty()) {
            throw new IllegalStateException("Unknown credential binding: " + bindingIdentity);
        }

        var rows = dsl.select(
                        GENERATION_IDENTIFIER,
                        BINDING_IDENTIFIER,
                        PROTECTED_MATERIAL_REFERENCE,
                        REGISTERED_AT
                )
                .from(GENERATION)
                .where(BINDING_IDENTIFIER.eq(bindingIdentity))
                .and(REGISTERED_AT.le(instant))
                .orderBy(REGISTERED_AT.desc(), GENERATION_IDENTIFIER.desc())
                .fetch();

        List<CredentialGeneration> eligible = new ArrayList<>();
        for (Record row : rows) {
            CredentialGeneration generation = toGeneration(row);
            Optional<CredentialGenerationPolicy> policy = policyAt(
                    generation.generationIdentity(),
                    instant
            );
            if (policy.isPresent() && policy.orElseThrow().permits(technicalUse)) {
                eligible.add(generation);
            }
        }
        return List.copyOf(eligible);
    }

    private Optional<CredentialGeneration> generation(String generationIdentity) {
        Record row = dsl.select(
                        GENERATION_IDENTIFIER,
                        BINDING_IDENTIFIER,
                        PROTECTED_MATERIAL_REFERENCE,
                        REGISTERED_AT
                )
                .from(GENERATION)
                .where(GENERATION_IDENTIFIER.eq(generationIdentity))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toGeneration);
    }

    private Optional<CredentialGenerationPolicy> policy(String policyIdentity) {
        Record row = dsl.select(
                        POLICY_IDENTIFIER,
                        GENERATION_IDENTIFIER,
                        POLICY_SEQUENCE,
                        STATE,
                        ALLOW_NEW_EXECUTION,
                        ALLOW_EXISTING_OBLIGATION,
                        ALLOW_VERIFICATION,
                        EFFECTIVE_AT,
                        EVIDENCE_REFERENCE
                )
                .from(POLICY)
                .where(POLICY_IDENTIFIER.eq(policyIdentity))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toPolicy);
    }

    private Optional<CredentialGenerationPolicy> policyAt(
            String generationIdentity,
            Instant instant
    ) {
        Record row = dsl.select(
                        POLICY_IDENTIFIER,
                        GENERATION_IDENTIFIER,
                        POLICY_SEQUENCE,
                        STATE,
                        ALLOW_NEW_EXECUTION,
                        ALLOW_EXISTING_OBLIGATION,
                        ALLOW_VERIFICATION,
                        EFFECTIVE_AT,
                        EVIDENCE_REFERENCE
                )
                .from(POLICY)
                .where(GENERATION_IDENTIFIER.eq(generationIdentity))
                .and(EFFECTIVE_AT.le(instant))
                .orderBy(POLICY_SEQUENCE.desc())
                .limit(1)
                .fetchOne();
        return Optional.ofNullable(row).map(this::toPolicy);
    }

    private void insertPolicy(CredentialGenerationPolicy policy) {
        dsl.insertInto(POLICY)
                .columns(
                        POLICY_IDENTIFIER,
                        GENERATION_IDENTIFIER,
                        POLICY_SEQUENCE,
                        STATE,
                        ALLOW_NEW_EXECUTION,
                        ALLOW_EXISTING_OBLIGATION,
                        ALLOW_VERIFICATION,
                        EFFECTIVE_AT,
                        EVIDENCE_REFERENCE
                )
                .values(
                        policy.policyIdentity(),
                        policy.generationIdentity(),
                        policy.sequence(),
                        policy.state().name(),
                        policy.permittedUses().contains(CredentialTechnicalUse.NEW_EXECUTION),
                        policy.permittedUses().contains(CredentialTechnicalUse.EXISTING_OBLIGATION),
                        policy.permittedUses().contains(CredentialTechnicalUse.VERIFICATION),
                        policy.effectiveAt(),
                        policy.evidenceReference().orElse(null)
                )
                .execute();
    }

    private CredentialBinding toBinding(Record row) {
        String merchantIdentifier = row.get(MERCHANT_IDENTIFIER);
        return new CredentialBinding(
                row.get(BINDING_IDENTIFIER),
                CredentialBindingScope.valueOf(row.get(BINDING_SCOPE)),
                Optional.ofNullable(merchantIdentifier).map(MerchantScope::new),
                row.get(RESPONSIBILITY_REFERENCE),
                row.get(EXTERNAL_CONTEXT_REFERENCE),
                row.get(TECHNICAL_PURPOSE_IDENTIFIER),
                row.get(ESTABLISHED_AT)
        );
    }

    private CredentialGeneration toGeneration(Record row) {
        return new CredentialGeneration(
                row.get(GENERATION_IDENTIFIER),
                row.get(BINDING_IDENTIFIER),
                row.get(PROTECTED_MATERIAL_REFERENCE),
                row.get(REGISTERED_AT)
        );
    }

    private CredentialGenerationPolicy toPolicy(Record row) {
        Set<CredentialTechnicalUse> uses = EnumSet.noneOf(CredentialTechnicalUse.class);
        if (Boolean.TRUE.equals(row.get(ALLOW_NEW_EXECUTION))) {
            uses.add(CredentialTechnicalUse.NEW_EXECUTION);
        }
        if (Boolean.TRUE.equals(row.get(ALLOW_EXISTING_OBLIGATION))) {
            uses.add(CredentialTechnicalUse.EXISTING_OBLIGATION);
        }
        if (Boolean.TRUE.equals(row.get(ALLOW_VERIFICATION))) {
            uses.add(CredentialTechnicalUse.VERIFICATION);
        }
        return new CredentialGenerationPolicy(
                row.get(POLICY_IDENTIFIER),
                row.get(GENERATION_IDENTIFIER),
                row.get(POLICY_SEQUENCE),
                CredentialGenerationState.valueOf(row.get(STATE)),
                uses,
                row.get(EFFECTIVE_AT),
                Optional.ofNullable(row.get(EVIDENCE_REFERENCE))
        );
    }

    private static void requireInitialPolicy(
            CredentialGeneration generation,
            CredentialGenerationPolicy policy
    ) {
        if (!generation.generationIdentity().equals(policy.generationIdentity())) {
            throw new IllegalArgumentException(
                    "Initial credential policy belongs to another generation"
            );
        }
        if (policy.sequence() != 1) {
            throw new IllegalArgumentException(
                    "Initial credential policy sequence must be one"
            );
        }
        if (policy.effectiveAt().isBefore(generation.registeredAt())) {
            throw new IllegalArgumentException(
                    "Initial credential policy cannot predate generation registration"
            );
        }
    }

    private static void requireAllowedTransition(
            CredentialGenerationState current,
            CredentialGenerationState next
    ) {
        if (current.terminal()) {
            throw new IllegalStateException(
                    "Terminal credential generation state cannot transition"
            );
        }
        if (current == CredentialGenerationState.RETIRING
                && next == CredentialGenerationState.USABLE) {
            throw new IllegalStateException(
                    "Retiring credential generation cannot silently return to USABLE"
            );
        }
    }

    private void lockIdentity(String kind, String identity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                "credential|" + kind + "|" + identity
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
