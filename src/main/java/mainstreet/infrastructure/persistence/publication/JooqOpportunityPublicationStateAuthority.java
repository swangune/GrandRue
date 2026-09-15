package mainstreet.infrastructure.persistence.publication;

import mainstreet.application.MerchantScope;
import mainstreet.publication.OpportunityCalendarDateBoundary;
import mainstreet.publication.OpportunityExactInstantBoundary;
import mainstreet.publication.OpportunityExternalLink;
import mainstreet.publication.OpportunityExternalLinkRole;
import mainstreet.publication.OpportunityPublicationHistoryEntry;
import mainstreet.publication.OpportunityPublicationMaterialRevision;
import mainstreet.publication.OpportunityPublicationState;
import mainstreet.publication.OpportunityPublicationStateAuthority;
import mainstreet.publication.OpportunityPublicationSubmissionLock;
import mainstreet.publication.OpportunityTemporalBoundary;
import mainstreet.publication.PublicationHistoryOperationKind;
import mainstreet.publication.PublicationLifecycle;
import mainstreet.publication.PublicationRevisionConflictException;
import mainstreet.publication.PublicationStateConflictException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * PostgreSQL/jOOQ persistence for Opportunity Publication currentness, immutable typed material
 * revisions and ordered Publication History.
 *
 * <p>V54's field rows remain a private physical encoding. The Publication boundary is closed to
 * the accepted {@code publication / opportunity@1} material contract: arbitrary schema versions or
 * field identifiers are never exposed as authoritative Opportunity material. Exposure, Enquiry,
 * application orchestration and logical-operation retry idempotency remain outside this boundary.</p>
 */
public final class JooqOpportunityPublicationStateAuthority
        implements OpportunityPublicationStateAuthority, OpportunityPublicationSubmissionLock {

    private static final String OPPORTUNITY_SCHEMA = "opportunity";
    private static final long OPPORTUNITY_SCHEMA_VERSION = 1L;

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ELIGIBILITY_INFORMATION = "eligibility-information";
    private static final String EXTERNAL_PROVIDER_NAME = "external-provider-name";
    private static final String SOURCE_NAME = "source-name";
    private static final String EXTERNAL_LINK = "external-link";
    private static final String APPLICATIONS_OPEN = "applications-open";
    private static final String APPLICATION_DEADLINE = "application-deadline";
    private static final String PUBLISH_FROM = "publish-from";
    private static final String PUBLISH_UNTIL = "publish-until";

    private static final Set<String> ALLOWED_MATERIAL_FIELDS = Set.of(
            TITLE,
            DESCRIPTION,
            ELIGIBILITY_INFORMATION,
            EXTERNAL_PROVIDER_NAME,
            SOURCE_NAME,
            EXTERNAL_LINK,
            APPLICATIONS_OPEN,
            APPLICATION_DEADLINE,
            PUBLISH_FROM,
            PUBLISH_UNTIL
    );

    private static final Table<?> CURRENT =
            DSL.table(DSL.name("opportunity_publication_current"));
    private static final Table<?> REVISION_IDENTITY =
            DSL.table(DSL.name("opportunity_publication_revision_identity"));
    private static final Table<?> REVISION_MATERIAL =
            DSL.table(DSL.name("opportunity_publication_revision_material"));
    private static final Table<?> REVISION_FIELD_VALUE =
            DSL.table(DSL.name("opportunity_publication_revision_field_value"));
    private static final Table<?> PUBLICATION_HISTORY =
            DSL.table(DSL.name("opportunity_publication_lifecycle_history"));

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> OPPORTUNITY_IDENTITY =
            DSL.field(DSL.name("opportunity_identity"), String.class);
    private static final Field<String> CURRENT_REVISION_IDENTITY =
            DSL.field(DSL.name("current_revision_identity"), String.class);
    private static final Field<String> REVISION_IDENTITY_VALUE =
            DSL.field(DSL.name("revision_identity"), String.class);
    private static final Field<String> LIFECYCLE =
            DSL.field(DSL.name("lifecycle"), String.class);
    private static final Field<String> PUBLISHED_REVISION_IDENTITY =
            DSL.field(DSL.name("published_revision_identity"), String.class);
    private static final Field<String> SCHEMA_IDENTIFIER =
            DSL.field(DSL.name("schema_identifier"), String.class);
    private static final Field<Long> SCHEMA_VERSION =
            DSL.field(DSL.name("schema_version"), Long.class);
    private static final Field<String> FIELD_IDENTIFIER =
            DSL.field(DSL.name("field_identifier"), String.class);
    private static final Field<String> CANONICAL_VALUE =
            DSL.field(DSL.name("canonical_value"), String.class);
    private static final Field<Long> TRANSITION_SEQUENCE =
            DSL.field(DSL.name("transition_sequence"), Long.class);
    private static final Field<String> TRANSITION_KIND =
            DSL.field(DSL.name("transition_kind"), String.class);
    private static final Field<String> PREVIOUS_LIFECYCLE =
            DSL.field(DSL.name("previous_lifecycle"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqOpportunityPublicationStateAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public OpportunityPublicationState establish(
            OpportunityPublicationState initialState,
            OpportunityPublicationMaterialRevision initialRevision
    ) {
        Objects.requireNonNull(initialState, "initialState");
        Objects.requireNonNull(initialRevision, "initialRevision");
        requireRevisionAffinity(initialState, initialRevision);

        OpportunityPublicationState result = transactionTemplate.execute(status -> {
            recordRevisionIdentity(initialState);
            recordMaterialRevision(initialRevision);
            int inserted = dsl.insertInto(CURRENT)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            OPPORTUNITY_IDENTITY,
                            CURRENT_REVISION_IDENTITY,
                            LIFECYCLE,
                            PUBLISHED_REVISION_IDENTITY
                    )
                    .values(
                            initialState.merchantScope().merchantIdentifier(),
                            initialState.opportunityIdentity(),
                            initialState.currentRevisionIdentity(),
                            initialState.lifecycle().name(),
                            initialState.publishedRevisionIdentity().orElse(null)
                    )
                    .onConflict(MERCHANT_IDENTIFIER, OPPORTUNITY_IDENTITY)
                    .doNothing()
                    .execute();
            if (inserted != 1) {
                throw new IllegalStateException(
                        "Opportunity Publication state is already established"
                );
            }
            // DRAFT establishment creates material revision evidence, not Publication History.
            return initialState;
        });
        return Objects.requireNonNull(
                result,
                "Opportunity Publication establishment returned no state"
        );
    }

    @Override
    public OpportunityPublicationState compareAndSet(
            OpportunityPublicationState expectedState,
            OpportunityPublicationState nextState,
            Optional<OpportunityPublicationMaterialRevision> newRevision
    ) {
        Objects.requireNonNull(expectedState, "expectedState");
        Objects.requireNonNull(nextState, "nextState");
        Objects.requireNonNull(newRevision, "newRevision");
        requireSamePublication(expectedState, nextState);
        requireMaterialBoundary(expectedState, nextState, newRevision);
        Optional<PublicationHistoryOperationKind> operation =
                PublicationHistoryOperationKind.between(expectedState, nextState);

        OpportunityPublicationState result = transactionTemplate.execute(status -> {
            Condition expectedPublishedRevision = expectedState.publishedRevisionIdentity()
                    .<Condition>map(PUBLISHED_REVISION_IDENTITY::eq)
                    .orElseGet(PUBLISHED_REVISION_IDENTITY::isNull);

            int updated = dsl.update(CURRENT)
                    .set(CURRENT_REVISION_IDENTITY, nextState.currentRevisionIdentity())
                    .set(LIFECYCLE, nextState.lifecycle().name())
                    .set(
                            PUBLISHED_REVISION_IDENTITY,
                            nextState.publishedRevisionIdentity().orElse(null)
                    )
                    .where(MERCHANT_IDENTIFIER.eq(
                            expectedState.merchantScope().merchantIdentifier()
                    ))
                    .and(OPPORTUNITY_IDENTITY.eq(expectedState.opportunityIdentity()))
                    .and(CURRENT_REVISION_IDENTITY.eq(
                            expectedState.currentRevisionIdentity()
                    ))
                    .and(LIFECYCLE.eq(expectedState.lifecycle().name()))
                    .and(expectedPublishedRevision)
                    .execute();

            if (updated != 1) {
                OpportunityPublicationState authoritative = current(
                        expectedState.merchantScope(),
                        expectedState.opportunityIdentity()
                ).orElseThrow(() -> new IllegalStateException(
                        "Opportunity Publication state is not established"
                ));
                if (!authoritative.currentRevisionIdentity().equals(
                        expectedState.currentRevisionIdentity()
                )) {
                    throw new PublicationRevisionConflictException(
                            expectedState.opportunityIdentity(),
                            expectedState.currentRevisionIdentity(),
                            authoritative.currentRevisionIdentity()
                    );
                }
                throw new PublicationStateConflictException(expectedState, authoritative);
            }

            if (!nextState.currentRevisionIdentity().equals(
                    expectedState.currentRevisionIdentity()
            )) {
                recordRevisionIdentity(nextState);
                recordMaterialRevision(newRevision.orElseThrow());
            }

            operation.ifPresent(kind -> recordPublicationHistory(
                    nextState,
                    kind,
                    expectedState.lifecycle()
            ));
            return nextState;
        });
        return Objects.requireNonNull(
                result,
                "Opportunity Publication compare-and-set returned no state"
        );
    }

    @Override
    public Optional<OpportunityPublicationState> current(
            MerchantScope merchantScope,
            String opportunityIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");

        Record row = dsl.select(
                        MERCHANT_IDENTIFIER,
                        OPPORTUNITY_IDENTITY,
                        CURRENT_REVISION_IDENTITY,
                        LIFECYCLE,
                        PUBLISHED_REVISION_IDENTITY
                )
                .from(CURRENT)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OPPORTUNITY_IDENTITY.eq(opportunityIdentity))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toState);
    }

    @Override
    public Optional<OpportunityPublicationState> lockCurrent(
            MerchantScope merchantScope, String opportunityIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Publication submission lock requires an active transaction");
        }
        Record row = dsl.select(MERCHANT_IDENTIFIER, OPPORTUNITY_IDENTITY,
                        CURRENT_REVISION_IDENTITY, LIFECYCLE, PUBLISHED_REVISION_IDENTITY)
                .from(CURRENT)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OPPORTUNITY_IDENTITY.eq(opportunityIdentity))
                .forShare()
                .fetchOne();
        return Optional.ofNullable(row).map(this::toState);
    }

    @Override
    public Optional<OpportunityPublicationMaterialRevision> revision(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String revisionIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        requireText(revisionIdentity, "revisionIdentity");

        Record material = dsl.select(SCHEMA_IDENTIFIER, SCHEMA_VERSION)
                .from(REVISION_MATERIAL)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OPPORTUNITY_IDENTITY.eq(opportunityIdentity))
                .and(REVISION_IDENTITY_VALUE.eq(revisionIdentity))
                .fetchOne();
        if (material == null) {
            return Optional.empty();
        }
        requireOpportunityV1Schema(material);

        Map<String, String> fields = new HashMap<>();
        dsl.select(FIELD_IDENTIFIER, CANONICAL_VALUE)
                .from(REVISION_FIELD_VALUE)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OPPORTUNITY_IDENTITY.eq(opportunityIdentity))
                .and(REVISION_IDENTITY_VALUE.eq(revisionIdentity))
                .fetch()
                .forEach(row -> {
                    String identifier = row.get(FIELD_IDENTIFIER);
                    if (!ALLOWED_MATERIAL_FIELDS.contains(identifier)) {
                        throw new IllegalStateException(
                                "Stored Opportunity material contains an unregistered v1 field: "
                                        + identifier
                        );
                    }
                    fields.put(identifier, row.get(CANONICAL_VALUE));
                });

        String title = Optional.ofNullable(fields.get(TITLE))
                .orElseThrow(() -> new IllegalStateException(
                        "Stored Opportunity v1 material is missing title"
                ));

        return Optional.of(new OpportunityPublicationMaterialRevision(
                merchantScope,
                opportunityIdentity,
                revisionIdentity,
                title,
                Optional.ofNullable(fields.get(DESCRIPTION)),
                Optional.ofNullable(fields.get(ELIGIBILITY_INFORMATION)),
                Optional.ofNullable(fields.get(EXTERNAL_PROVIDER_NAME)),
                Optional.ofNullable(fields.get(SOURCE_NAME)),
                decodeExternalLinks(fields.get(EXTERNAL_LINK)),
                decodeOptionalBoundary(fields.get(APPLICATIONS_OPEN)),
                decodeOptionalBoundary(fields.get(APPLICATION_DEADLINE)),
                decodeOptionalBoundary(fields.get(PUBLISH_FROM)),
                decodeOptionalBoundary(fields.get(PUBLISH_UNTIL))
        ));
    }

    @Override
    public List<OpportunityPublicationHistoryEntry> publicationHistory(
            MerchantScope merchantScope,
            String opportunityIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");

        return dsl.select(
                        MERCHANT_IDENTIFIER,
                        OPPORTUNITY_IDENTITY,
                        TRANSITION_SEQUENCE,
                        TRANSITION_KIND,
                        LIFECYCLE,
                        CURRENT_REVISION_IDENTITY,
                        PUBLISHED_REVISION_IDENTITY
                )
                .from(PUBLICATION_HISTORY)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OPPORTUNITY_IDENTITY.eq(opportunityIdentity))
                .and(TRANSITION_KIND.in(
                        PublicationHistoryOperationKind.PUBLISH.name(),
                        PublicationHistoryOperationKind.WITHDRAW.name(),
                        PublicationHistoryOperationKind.REPUBLISH.name()
                ))
                .orderBy(TRANSITION_SEQUENCE)
                .fetch(this::toPublicationHistoryEntry);
    }

    private void recordRevisionIdentity(OpportunityPublicationState state) {
        int inserted = dsl.insertInto(REVISION_IDENTITY)
                .columns(
                        MERCHANT_IDENTIFIER,
                        OPPORTUNITY_IDENTITY,
                        REVISION_IDENTITY_VALUE
                )
                .values(
                        state.merchantScope().merchantIdentifier(),
                        state.opportunityIdentity(),
                        state.currentRevisionIdentity()
                )
                .onConflict(
                        MERCHANT_IDENTIFIER,
                        OPPORTUNITY_IDENTITY,
                        REVISION_IDENTITY_VALUE
                )
                .doNothing()
                .execute();
        if (inserted != 1) {
            throw new IllegalStateException(
                    "Opportunity Publication revision identity is already recorded"
            );
        }
    }

    private void recordMaterialRevision(OpportunityPublicationMaterialRevision revision) {
        int inserted = dsl.insertInto(REVISION_MATERIAL)
                .columns(
                        MERCHANT_IDENTIFIER,
                        OPPORTUNITY_IDENTITY,
                        REVISION_IDENTITY_VALUE,
                        SCHEMA_IDENTIFIER,
                        SCHEMA_VERSION
                )
                .values(
                        revision.merchantScope().merchantIdentifier(),
                        revision.opportunityIdentity(),
                        revision.revisionIdentity(),
                        OPPORTUNITY_SCHEMA,
                        OPPORTUNITY_SCHEMA_VERSION
                )
                .onConflict(
                        MERCHANT_IDENTIFIER,
                        OPPORTUNITY_IDENTITY,
                        REVISION_IDENTITY_VALUE
                )
                .doNothing()
                .execute();
        if (inserted != 1) {
            throw new IllegalStateException(
                    "Opportunity Publication material revision is already recorded"
            );
        }

        insertMaterialField(revision, TITLE, revision.title());
        revision.description().ifPresent(value -> insertMaterialField(revision, DESCRIPTION, value));
        revision.eligibilityInformation().ifPresent(
                value -> insertMaterialField(revision, ELIGIBILITY_INFORMATION, value)
        );
        revision.externalProviderName().ifPresent(
                value -> insertMaterialField(revision, EXTERNAL_PROVIDER_NAME, value)
        );
        revision.sourceName().ifPresent(value -> insertMaterialField(revision, SOURCE_NAME, value));
        if (!revision.externalLinks().isEmpty()) {
            insertMaterialField(revision, EXTERNAL_LINK, encodeExternalLinks(revision.externalLinks()));
        }
        revision.applicationsOpen().ifPresent(
                value -> insertMaterialField(revision, APPLICATIONS_OPEN, encodeBoundary(value))
        );
        revision.applicationDeadline().ifPresent(
                value -> insertMaterialField(revision, APPLICATION_DEADLINE, encodeBoundary(value))
        );
        revision.publishFrom().ifPresent(
                value -> insertMaterialField(revision, PUBLISH_FROM, encodeBoundary(value))
        );
        revision.publishUntil().ifPresent(
                value -> insertMaterialField(revision, PUBLISH_UNTIL, encodeBoundary(value))
        );
    }

    private void insertMaterialField(
            OpportunityPublicationMaterialRevision revision,
            String fieldIdentifier,
            String canonicalValue
    ) {
        dsl.insertInto(REVISION_FIELD_VALUE)
                .columns(
                        MERCHANT_IDENTIFIER,
                        OPPORTUNITY_IDENTITY,
                        REVISION_IDENTITY_VALUE,
                        FIELD_IDENTIFIER,
                        CANONICAL_VALUE
                )
                .values(
                        revision.merchantScope().merchantIdentifier(),
                        revision.opportunityIdentity(),
                        revision.revisionIdentity(),
                        fieldIdentifier,
                        canonicalValue
                )
                .execute();
    }

    private void recordPublicationHistory(
            OpportunityPublicationState state,
            PublicationHistoryOperationKind operationKind,
            PublicationLifecycle previousLifecycle
    ) {
        long sequence = nextHistorySequence(state);
        dsl.insertInto(PUBLICATION_HISTORY)
                .columns(
                        MERCHANT_IDENTIFIER,
                        OPPORTUNITY_IDENTITY,
                        TRANSITION_SEQUENCE,
                        TRANSITION_KIND,
                        PREVIOUS_LIFECYCLE,
                        LIFECYCLE,
                        CURRENT_REVISION_IDENTITY,
                        PUBLISHED_REVISION_IDENTITY
                )
                .values(
                        state.merchantScope().merchantIdentifier(),
                        state.opportunityIdentity(),
                        sequence,
                        operationKind.name(),
                        previousLifecycle.name(),
                        state.lifecycle().name(),
                        state.currentRevisionIdentity(),
                        state.publishedRevisionIdentity().orElse(null)
                )
                .execute();
    }

    private long nextHistorySequence(OpportunityPublicationState state) {
        Long maximum = dsl.select(DSL.max(TRANSITION_SEQUENCE))
                .from(PUBLICATION_HISTORY)
                .where(MERCHANT_IDENTIFIER.eq(state.merchantScope().merchantIdentifier()))
                .and(OPPORTUNITY_IDENTITY.eq(state.opportunityIdentity()))
                .fetchOne(0, Long.class);
        return maximum == null ? 1L : maximum + 1L;
    }

    private OpportunityPublicationState toState(Record row) {
        return new OpportunityPublicationState(
                new MerchantScope(row.get(MERCHANT_IDENTIFIER)),
                row.get(OPPORTUNITY_IDENTITY),
                row.get(CURRENT_REVISION_IDENTITY),
                PublicationLifecycle.valueOf(row.get(LIFECYCLE)),
                Optional.ofNullable(row.get(PUBLISHED_REVISION_IDENTITY))
        );
    }

    private OpportunityPublicationHistoryEntry toPublicationHistoryEntry(Record row) {
        return new OpportunityPublicationHistoryEntry(
                new MerchantScope(row.get(MERCHANT_IDENTIFIER)),
                row.get(OPPORTUNITY_IDENTITY),
                row.get(TRANSITION_SEQUENCE),
                PublicationHistoryOperationKind.valueOf(row.get(TRANSITION_KIND)),
                row.get(CURRENT_REVISION_IDENTITY),
                Optional.ofNullable(row.get(PUBLISHED_REVISION_IDENTITY)),
                PublicationLifecycle.valueOf(row.get(LIFECYCLE))
        );
    }

    private static void requireMaterialBoundary(
            OpportunityPublicationState expectedState,
            OpportunityPublicationState nextState,
            Optional<OpportunityPublicationMaterialRevision> newRevision
    ) {
        boolean revisionChanged = !expectedState.currentRevisionIdentity().equals(
                nextState.currentRevisionIdentity()
        );
        if (revisionChanged) {
            OpportunityPublicationMaterialRevision material = newRevision.orElseThrow(
                    () -> new IllegalArgumentException(
                            "A new material revision is required when revision identity changes"
                    )
            );
            requireRevisionAffinity(nextState, material);
        } else if (newRevision.isPresent()) {
            throw new IllegalArgumentException(
                    "Material revision must be absent when revision identity does not change"
            );
        }
    }

    private static void requireRevisionAffinity(
            OpportunityPublicationState state,
            OpportunityPublicationMaterialRevision revision
    ) {
        if (!state.merchantScope().equals(revision.merchantScope())
                || !state.opportunityIdentity().equals(revision.opportunityIdentity())
                || !state.currentRevisionIdentity().equals(revision.revisionIdentity())) {
            throw new IllegalArgumentException(
                    "Material revision must match the exact merchant-owned Opportunity revision"
            );
        }
    }

    private static void requireSamePublication(
            OpportunityPublicationState expectedState,
            OpportunityPublicationState nextState
    ) {
        if (!expectedState.merchantScope().equals(nextState.merchantScope())
                || !expectedState.opportunityIdentity().equals(nextState.opportunityIdentity())) {
            throw new IllegalArgumentException(
                    "Expected and next Publication state must refer to the same merchant-owned Opportunity"
            );
        }
    }

    private static void requireOpportunityV1Schema(Record row) {
        if (!OPPORTUNITY_SCHEMA.equals(row.get(SCHEMA_IDENTIFIER))
                || !Long.valueOf(OPPORTUNITY_SCHEMA_VERSION).equals(row.get(SCHEMA_VERSION))) {
            throw new IllegalStateException(
                    "Stored Opportunity material does not have publication / opportunity@1 affinity"
            );
        }
    }

    private static String encodeBoundary(OpportunityTemporalBoundary boundary) {
        if (boundary instanceof OpportunityCalendarDateBoundary calendar) {
            return "CALENDAR_DATE|" + calendar.date() + "|" + calendar.zoneId().getId();
        }
        if (boundary instanceof OpportunityExactInstantBoundary exact) {
            return "EXACT_INSTANT|" + exact.instant();
        }
        throw new IllegalArgumentException("Unsupported Opportunity temporal boundary");
    }

    private static Optional<OpportunityTemporalBoundary> decodeOptionalBoundary(String encoded) {
        if (encoded == null) {
            return Optional.empty();
        }
        String[] parts = encoded.split("\\|", -1);
        try {
            if (parts.length == 3 && "CALENDAR_DATE".equals(parts[0])) {
                return Optional.of(new OpportunityCalendarDateBoundary(
                        LocalDate.parse(parts[1]),
                        ZoneId.of(parts[2])
                ));
            }
            if (parts.length == 2 && "EXACT_INSTANT".equals(parts[0])) {
                return Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse(parts[1])
                ));
            }
        } catch (RuntimeException invalid) {
            throw new IllegalStateException("Stored Opportunity temporal boundary is invalid", invalid);
        }
        throw new IllegalStateException("Stored Opportunity temporal boundary has unknown encoding");
    }

    private static String encodeExternalLinks(List<OpportunityExternalLink> links) {
        return links.stream()
                .map(link -> link.role().name()
                        + ";" + encodeComponent(link.uri().toString())
                        + ";" + link.label().map(JooqOpportunityPublicationStateAuthority::encodeComponent)
                                .orElse("-"))
                .reduce((left, right) -> left + "," + right)
                .orElseThrow();
    }

    private static List<OpportunityExternalLink> decodeExternalLinks(String encoded) {
        if (encoded == null) {
            return List.of();
        }
        List<OpportunityExternalLink> links = new ArrayList<>();
        for (String entry : encoded.split(",", -1)) {
            String[] parts = entry.split(";", -1);
            if (parts.length != 3) {
                throw new IllegalStateException("Stored Opportunity external link is invalid");
            }
            try {
                links.add(new OpportunityExternalLink(
                        OpportunityExternalLinkRole.valueOf(parts[0]),
                        URI.create(decodeComponent(parts[1])),
                        "-".equals(parts[2])
                                ? Optional.empty()
                                : Optional.of(decodeComponent(parts[2]))
                ));
            } catch (RuntimeException invalid) {
                throw new IllegalStateException("Stored Opportunity external link is invalid", invalid);
            }
        }
        return List.copyOf(links);
    }

    private static String encodeComponent(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
                value.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String decodeComponent(String value) {
        return new String(
                Base64.getUrlDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
