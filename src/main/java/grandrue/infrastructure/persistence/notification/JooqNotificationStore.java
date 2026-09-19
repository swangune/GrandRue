package grandrue.infrastructure.persistence.notification;

import grandrue.notification.DeliveryAttempt;
import grandrue.notification.DeliveryAttemptOutcome;
import grandrue.notification.DeliveryEvidence;
import grandrue.notification.NotificationChannel;
import grandrue.notification.NotificationDispatch;
import grandrue.notification.NotificationIntent;
import grandrue.notification.NotificationOwnerScope;
import grandrue.notification.NotificationRecipient;
import grandrue.notification.NotificationRecipientResolutionBasis;
import grandrue.notification.NotificationStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL/jOOQ durable Notification Intent/Dispatch/Attempt/Evidence store. */
public final class JooqNotificationStore implements NotificationStore {

    private static final Table<?> INTENT = DSL.table(DSL.name("notification_intent"));
    private static final Table<?> DISPATCH = DSL.table(DSL.name("notification_dispatch"));
    private static final Table<?> ATTEMPT = DSL.table(DSL.name("notification_delivery_attempt"));
    private static final Table<?> EVIDENCE = DSL.table(DSL.name("notification_delivery_evidence"));

    private static final Field<String> INTENT_ID = text("intent_identity");
    private static final Field<String> NOTIFICATION_TYPE_ID = text("notification_type_identifier");
    private static final Field<String> OWNER_SCOPE_KIND = text("owner_scope_kind_identifier");
    private static final Field<String> OWNER_SCOPE_ID = text("owner_scope_identifier");
    private static final Field<String> SOURCE_REFERENCE = text("source_fact_or_process_reference");
    private static final Field<String> RECIPIENT_BASIS_REFERENCE = text("recipient_resolution_basis_reference");
    private static final Field<String> CORRELATION_ID = text("correlation_identifier");
    private static final Field<String> CAUSATION_ID = text("causation_identifier");
    private static final Field<Instant> CREATED_AT = instant("created_at");

    private static final Field<String> DISPATCH_ID = text("dispatch_identity");
    private static final Field<String> RECIPIENT_ID = text("recipient_identity");
    private static final Field<String> RECIPIENT_KIND_ID = text("recipient_kind_identifier");
    private static final Field<String> RECIPIENT_SEMANTIC_REF = text("recipient_semantic_reference");
    private static final Field<String> RECIPIENT_RESOLUTION_BASIS = text("recipient_resolution_basis");
    private static final Field<String> CHANNEL = text("channel");
    private static final Field<String> ENDPOINT_REFERENCE = text("endpoint_reference");
    private static final Field<String> CONTENT_PROJECTION_REFERENCE = text("content_projection_reference");

    private static final Field<String> ATTEMPT_ID = text("attempt_identity");
    private static final Field<Instant> ATTEMPTED_AT = instant("attempted_at");
    private static final Field<String> PROVIDER_CONTEXT_REFERENCE = text("channel_provider_context_reference");
    private static final Field<String> PROVIDER_IDEMPOTENCY_REFERENCE = text("provider_idempotency_reference");
    private static final Field<String> OUTCOME = text("outcome");
    private static final Field<String> FAILURE_REFERENCE = text("failure_reference");

    private static final Field<String> EVIDENCE_ID = text("evidence_identity");
    private static final Field<String> EVIDENCE_TYPE_ID = text("evidence_type_identifier");
    private static final Field<Instant> OBSERVED_AT = instant("observed_at");
    private static final Field<String> PROVIDER_REFERENCE = text("provider_reference");
    private static final Field<String> PROVENANCE_ID = text("provenance_identifier");

    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqNotificationStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public NotificationIntent establishIntent(NotificationIntent intent) {
        Objects.requireNonNull(intent, "intent");
        NotificationIntent result = transactions.execute(status -> {
            lockIdentity("intent", intent.intentIdentity(), 7501);
            NotificationIntent existing = findIntent(intent.intentIdentity()).orElse(null);
            if (existing != null) {
                requireEqual(existing, intent, "Notification Intent");
                return existing;
            }
            dsl.insertInto(INTENT)
                    .columns(INTENT_ID, NOTIFICATION_TYPE_ID, OWNER_SCOPE_KIND,
                            OWNER_SCOPE_ID, SOURCE_REFERENCE, RECIPIENT_BASIS_REFERENCE,
                            CORRELATION_ID, CAUSATION_ID, CREATED_AT)
                    .values(intent.intentIdentity(), intent.notificationTypeIdentifier(),
                            intent.ownerScope().scopeKindIdentifier(),
                            intent.ownerScope().scopeIdentifier(),
                            intent.sourceFactOrProcessReference(),
                            intent.recipientResolutionBasisReference(),
                            intent.correlationIdentifier().orElse(null),
                            intent.causationIdentifier().orElse(null), intent.createdAt())
                    .execute();
            return intent;
        });
        return Objects.requireNonNull(result, "Intent transaction returned no result");
    }

    @Override
    public NotificationDispatch establishDispatch(NotificationDispatch dispatch) {
        Objects.requireNonNull(dispatch, "dispatch");
        NotificationDispatch result = transactions.execute(status -> {
            lockIdentity("dispatch", dispatch.dispatchIdentity(), 7502);
            NotificationDispatch existing = findDispatch(dispatch.dispatchIdentity()).orElse(null);
            if (existing != null) {
                requireEqual(existing, dispatch, "Notification Dispatch");
                return existing;
            }
            NotificationIntent intent = findIntent(dispatch.intentIdentity())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown Notification Intent: " + dispatch.intentIdentity()));
            requireOwnerScope(intent.ownerScope(), dispatch.ownerScope());
            dsl.insertInto(DISPATCH)
                    .columns(DISPATCH_ID, INTENT_ID, OWNER_SCOPE_KIND, OWNER_SCOPE_ID,
                            RECIPIENT_ID, RECIPIENT_KIND_ID, RECIPIENT_SEMANTIC_REF,
                            RECIPIENT_RESOLUTION_BASIS, CHANNEL, ENDPOINT_REFERENCE,
                            CONTENT_PROJECTION_REFERENCE, CREATED_AT)
                    .values(dispatch.dispatchIdentity(), dispatch.intentIdentity(),
                            dispatch.ownerScope().scopeKindIdentifier(),
                            dispatch.ownerScope().scopeIdentifier(),
                            dispatch.recipient().recipientIdentity(),
                            dispatch.recipient().recipientKindIdentifier(),
                            dispatch.recipient().semanticReference(),
                            dispatch.recipient().resolutionBasis().name(),
                            dispatch.channel().name(), dispatch.endpointReference(),
                            dispatch.contentProjectionReference(), dispatch.createdAt())
                    .execute();
            return dispatch;
        });
        return Objects.requireNonNull(result, "Dispatch transaction returned no result");
    }

    @Override
    public DeliveryAttempt startAttempt(DeliveryAttempt attempt) {
        Objects.requireNonNull(attempt, "attempt");
        if (attempt.outcome() != DeliveryAttemptOutcome.STARTED) {
            throw new IllegalArgumentException("A new Delivery Attempt must begin as STARTED");
        }
        DeliveryAttempt result = transactions.execute(status -> {
            lockIdentity("attempt", attempt.attemptIdentity(), 7503);
            DeliveryAttempt existing = findAttempt(attempt.attemptIdentity()).orElse(null);
            if (existing != null) {
                requireEqual(existing, attempt, "Delivery Attempt");
                return existing;
            }
            NotificationDispatch dispatch = findDispatch(attempt.dispatchIdentity())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown Notification Dispatch: " + attempt.dispatchIdentity()));
            requireOwnerScope(dispatch.ownerScope(), attempt.ownerScope());
            dsl.insertInto(ATTEMPT)
                    .columns(ATTEMPT_ID, DISPATCH_ID, OWNER_SCOPE_KIND, OWNER_SCOPE_ID,
                            ATTEMPTED_AT, PROVIDER_CONTEXT_REFERENCE,
                            PROVIDER_IDEMPOTENCY_REFERENCE, OUTCOME, FAILURE_REFERENCE)
                    .values(attempt.attemptIdentity(), attempt.dispatchIdentity(),
                            attempt.ownerScope().scopeKindIdentifier(),
                            attempt.ownerScope().scopeIdentifier(), attempt.attemptedAt(),
                            attempt.channelProviderContextReference(),
                            attempt.providerIdempotencyReference(), attempt.outcome().name(),
                            attempt.failureReference().orElse(null))
                    .execute();
            return attempt;
        });
        return Objects.requireNonNull(result, "Attempt-start transaction returned no result");
    }

    @Override
    public DeliveryAttempt completeAttempt(DeliveryAttempt attempt) {
        Objects.requireNonNull(attempt, "attempt");
        if (!attempt.terminal()) {
            throw new IllegalArgumentException(
                    "Delivery Attempt completion requires a terminal outcome");
        }
        DeliveryAttempt result = transactions.execute(status -> {
            lockIdentity("attempt", attempt.attemptIdentity(), 7503);
            DeliveryAttempt current = requireAttemptForUpdate(attempt.attemptIdentity());
            requireAttemptAffinity(current, attempt);
            if (current.terminal()) {
                requireEqual(current, attempt, "Delivery Attempt terminal outcome");
                return current;
            }
            int changed = dsl.update(ATTEMPT)
                    .set(OUTCOME, attempt.outcome().name())
                    .set(FAILURE_REFERENCE, attempt.failureReference().orElse(null))
                    .where(ATTEMPT_ID.eq(attempt.attemptIdentity()))
                    .and(OUTCOME.eq(DeliveryAttemptOutcome.STARTED.name()))
                    .execute();
            if (changed != 1) {
                throw new IllegalStateException(
                        "Delivery Attempt terminal transition lost its current-state predicate");
            }
            return attempt;
        });
        return Objects.requireNonNull(result, "Attempt completion returned no result");
    }

    @Override
    public DeliveryEvidence recordEvidence(DeliveryEvidence evidence) {
        Objects.requireNonNull(evidence, "evidence");
        DeliveryEvidence result = transactions.execute(status -> {
            lockIdentity("evidence", evidence.evidenceIdentity(), 7504);
            DeliveryEvidence existing = findEvidence(evidence.evidenceIdentity()).orElse(null);
            if (existing != null) {
                requireEqual(existing, evidence, "Delivery Evidence");
                return existing;
            }
            NotificationDispatch dispatch = findDispatch(evidence.dispatchIdentity())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown Notification Dispatch: " + evidence.dispatchIdentity()));
            requireOwnerScope(dispatch.ownerScope(), evidence.ownerScope());
            evidence.attemptIdentity().ifPresent(attemptIdentity -> {
                DeliveryAttempt attempt = findAttempt(attemptIdentity)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Unknown Delivery Attempt: " + attemptIdentity));
                if (!attempt.dispatchIdentity().equals(evidence.dispatchIdentity())) {
                    throw new IllegalArgumentException(
                            "Delivery Evidence Attempt belongs to another Dispatch");
                }
                requireOwnerScope(attempt.ownerScope(), evidence.ownerScope());
            });
            dsl.insertInto(EVIDENCE)
                    .columns(EVIDENCE_ID, DISPATCH_ID, OWNER_SCOPE_KIND, OWNER_SCOPE_ID,
                            ATTEMPT_ID, EVIDENCE_TYPE_ID, OBSERVED_AT,
                            PROVIDER_REFERENCE, PROVENANCE_ID)
                    .values(evidence.evidenceIdentity(), evidence.dispatchIdentity(),
                            evidence.ownerScope().scopeKindIdentifier(),
                            evidence.ownerScope().scopeIdentifier(),
                            evidence.attemptIdentity().orElse(null),
                            evidence.evidenceTypeIdentifier(), evidence.observedAt(),
                            evidence.providerReference().orElse(null),
                            evidence.provenanceIdentifier())
                    .execute();
            return evidence;
        });
        return Objects.requireNonNull(result, "Evidence transaction returned no result");
    }

    @Override
    public Optional<NotificationIntent> findIntent(String identity) {
        requireIdentifier(identity, "Notification Intent identity");
        Record record = dsl.select(INTENT_ID, NOTIFICATION_TYPE_ID, OWNER_SCOPE_KIND,
                        OWNER_SCOPE_ID, SOURCE_REFERENCE, RECIPIENT_BASIS_REFERENCE,
                        CORRELATION_ID, CAUSATION_ID, CREATED_AT)
                .from(INTENT).where(INTENT_ID.eq(identity)).fetchOne();
        return Optional.ofNullable(record).map(this::toIntent);
    }

    @Override
    public Optional<NotificationDispatch> findDispatch(String identity) {
        requireIdentifier(identity, "Notification Dispatch identity");
        Record record = dsl.select(DISPATCH_ID, INTENT_ID, OWNER_SCOPE_KIND,
                        OWNER_SCOPE_ID, RECIPIENT_ID, RECIPIENT_KIND_ID,
                        RECIPIENT_SEMANTIC_REF, RECIPIENT_RESOLUTION_BASIS, CHANNEL,
                        ENDPOINT_REFERENCE, CONTENT_PROJECTION_REFERENCE, CREATED_AT)
                .from(DISPATCH).where(DISPATCH_ID.eq(identity)).fetchOne();
        return Optional.ofNullable(record).map(this::toDispatch);
    }

    @Override
    public Optional<DeliveryAttempt> findAttempt(String identity) {
        requireIdentifier(identity, "Delivery Attempt identity");
        Record record = dsl.select(ATTEMPT_ID, DISPATCH_ID, OWNER_SCOPE_KIND,
                        OWNER_SCOPE_ID, ATTEMPTED_AT, PROVIDER_CONTEXT_REFERENCE,
                        PROVIDER_IDEMPOTENCY_REFERENCE, OUTCOME, FAILURE_REFERENCE)
                .from(ATTEMPT).where(ATTEMPT_ID.eq(identity)).fetchOne();
        return Optional.ofNullable(record).map(this::toAttempt);
    }

    @Override
    public List<DeliveryAttempt> attemptsForDispatch(String dispatchIdentity) {
        requireIdentifier(dispatchIdentity, "Notification Dispatch identity");
        Result<? extends Record> records = dsl.select(ATTEMPT_ID, DISPATCH_ID,
                        OWNER_SCOPE_KIND, OWNER_SCOPE_ID, ATTEMPTED_AT,
                        PROVIDER_CONTEXT_REFERENCE, PROVIDER_IDEMPOTENCY_REFERENCE,
                        OUTCOME, FAILURE_REFERENCE)
                .from(ATTEMPT)
                .where(DISPATCH_ID.eq(dispatchIdentity))
                .orderBy(ATTEMPTED_AT, ATTEMPT_ID)
                .fetch();
        List<DeliveryAttempt> attempts = new ArrayList<>();
        records.forEach(record -> attempts.add(toAttempt(record)));
        return List.copyOf(attempts);
    }

    private Optional<DeliveryEvidence> findEvidence(String identity) {
        Record record = dsl.select(EVIDENCE_ID, DISPATCH_ID, OWNER_SCOPE_KIND,
                        OWNER_SCOPE_ID, ATTEMPT_ID, EVIDENCE_TYPE_ID, OBSERVED_AT,
                        PROVIDER_REFERENCE, PROVENANCE_ID)
                .from(EVIDENCE).where(EVIDENCE_ID.eq(identity)).fetchOne();
        return Optional.ofNullable(record).map(this::toEvidence);
    }

    private DeliveryAttempt requireAttemptForUpdate(String identity) {
        Record record = dsl.select(ATTEMPT_ID, DISPATCH_ID, OWNER_SCOPE_KIND,
                        OWNER_SCOPE_ID, ATTEMPTED_AT, PROVIDER_CONTEXT_REFERENCE,
                        PROVIDER_IDEMPOTENCY_REFERENCE, OUTCOME, FAILURE_REFERENCE)
                .from(ATTEMPT).where(ATTEMPT_ID.eq(identity)).forUpdate().fetchOne();
        if (record == null) {
            throw new IllegalArgumentException("Unknown Delivery Attempt: " + identity);
        }
        return toAttempt(record);
    }

    private NotificationIntent toIntent(Record r) {
        return new NotificationIntent(r.get(INTENT_ID), r.get(NOTIFICATION_TYPE_ID),
                scope(r), r.get(SOURCE_REFERENCE), r.get(RECIPIENT_BASIS_REFERENCE),
                Optional.ofNullable(r.get(CORRELATION_ID)),
                Optional.ofNullable(r.get(CAUSATION_ID)), r.get(CREATED_AT));
    }

    private NotificationDispatch toDispatch(Record r) {
        return new NotificationDispatch(r.get(DISPATCH_ID), r.get(INTENT_ID), scope(r),
                new NotificationRecipient(r.get(RECIPIENT_ID), r.get(RECIPIENT_KIND_ID),
                        r.get(RECIPIENT_SEMANTIC_REF),
                        NotificationRecipientResolutionBasis.valueOf(
                                r.get(RECIPIENT_RESOLUTION_BASIS))),
                NotificationChannel.valueOf(r.get(CHANNEL)), r.get(ENDPOINT_REFERENCE),
                r.get(CONTENT_PROJECTION_REFERENCE), r.get(CREATED_AT));
    }

    private DeliveryAttempt toAttempt(Record r) {
        return new DeliveryAttempt(r.get(ATTEMPT_ID), r.get(DISPATCH_ID), scope(r),
                r.get(ATTEMPTED_AT), r.get(PROVIDER_CONTEXT_REFERENCE),
                r.get(PROVIDER_IDEMPOTENCY_REFERENCE),
                DeliveryAttemptOutcome.valueOf(r.get(OUTCOME)),
                Optional.ofNullable(r.get(FAILURE_REFERENCE)));
    }

    private DeliveryEvidence toEvidence(Record r) {
        return new DeliveryEvidence(r.get(EVIDENCE_ID), r.get(DISPATCH_ID), scope(r),
                Optional.ofNullable(r.get(ATTEMPT_ID)), r.get(EVIDENCE_TYPE_ID),
                r.get(OBSERVED_AT), Optional.ofNullable(r.get(PROVIDER_REFERENCE)),
                r.get(PROVENANCE_ID));
    }

    private NotificationOwnerScope scope(Record r) {
        return new NotificationOwnerScope(r.get(OWNER_SCOPE_KIND), r.get(OWNER_SCOPE_ID));
    }

    private static void requireAttemptAffinity(DeliveryAttempt current, DeliveryAttempt candidate) {
        if (!current.attemptIdentity().equals(candidate.attemptIdentity())
                || !current.dispatchIdentity().equals(candidate.dispatchIdentity())
                || !current.ownerScope().equals(candidate.ownerScope())
                || !current.attemptedAt().equals(candidate.attemptedAt())
                || !current.channelProviderContextReference().equals(
                        candidate.channelProviderContextReference())
                || !current.providerIdempotencyReference().equals(
                        candidate.providerIdempotencyReference())) {
            throw new IllegalArgumentException(
                    "Delivery Attempt completion changed immutable attempt intent");
        }
    }

    private static void requireOwnerScope(NotificationOwnerScope expected,
                                          NotificationOwnerScope actual) {
        if (!expected.equals(actual)) {
            throw new IllegalArgumentException(
                    "Notification fact belongs to another owner scope");
        }
    }

    private static void requireEqual(Object existing, Object candidate, String label) {
        if (!Objects.equals(existing, candidate)) {
            throw new IllegalArgumentException(
                    label + " identity was reused for different intent/evidence");
        }
    }

    private void lockIdentity(String kind, String identity, int namespace) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 75))",
                namespace + "\u001f" + kind + "\u001f" + identity
        );
    }

    private static Field<String> text(String name) {
        return DSL.field(DSL.name(name), String.class);
    }

    private static Field<Instant> instant(String name) {
        return DSL.field(DSL.name(name), Instant.class);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
