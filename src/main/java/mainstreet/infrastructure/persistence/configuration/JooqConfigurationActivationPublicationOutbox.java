package mainstreet.infrastructure.persistence.configuration;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL/jOOQ access to pending publication intent for committed
 * ConfigurationRevisionActivated facts.
 *
 * <p>Publication completion is technical delivery evidence. Marking an intent
 * published does not alter the underlying Configuration activation fact.</p>
 */
public final class JooqConfigurationActivationPublicationOutbox {

    private static final Table<?> OUTBOX =
            DSL.table(DSL.name("configuration_activation_publication_intent"));

    private static final Field<String> PUBLICATION_INTENT_IDENTIFIER =
            DSL.field(DSL.name("publication_intent_identifier"), String.class);
    private static final Field<String> ACTIVATION_REQUEST_IDENTIFIER =
            DSL.field(DSL.name("activation_request_identifier"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> CONFIGURATION_REVISION_IDENTIFIER =
            DSL.field(DSL.name("configuration_revision_identifier"), String.class);
    private static final Field<String> RELEASE_IDENTIFIER =
            DSL.field(DSL.name("release_identifier"), String.class);
    private static final Field<Instant> OCCURRED_AT =
            DSL.field(DSL.name("occurred_at"), Instant.class);
    private static final Field<Instant> PUBLISHED_AT =
            DSL.field(DSL.name("published_at"), Instant.class);

    private final DSLContext dsl;

    public JooqConfigurationActivationPublicationOutbox(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    public List<ConfigurationActivationPublicationIntent> pending(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be positive");
        }

        return dsl
                .select(
                        PUBLICATION_INTENT_IDENTIFIER,
                        ACTIVATION_REQUEST_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        CONFIGURATION_REVISION_IDENTIFIER,
                        RELEASE_IDENTIFIER,
                        OCCURRED_AT,
                        PUBLISHED_AT
                )
                .from(OUTBOX)
                .where(PUBLISHED_AT.isNull())
                .orderBy(OCCURRED_AT, PUBLICATION_INTENT_IDENTIFIER)
                .limit(limit)
                .fetch(this::toIntent);
    }

    public Optional<ConfigurationActivationPublicationIntent> intentForActivation(
            String activationRequestIdentifier
    ) {
        requireIdentifier(
                activationRequestIdentifier,
                "Activation request identifier"
        );

        Record record = dsl
                .select(
                        PUBLICATION_INTENT_IDENTIFIER,
                        ACTIVATION_REQUEST_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        CONFIGURATION_REVISION_IDENTIFIER,
                        RELEASE_IDENTIFIER,
                        OCCURRED_AT,
                        PUBLISHED_AT
                )
                .from(OUTBOX)
                .where(ACTIVATION_REQUEST_IDENTIFIER.eq(activationRequestIdentifier))
                .fetchOne();

        return Optional.ofNullable(record).map(this::toIntent);
    }

    public boolean markPublished(
            String publicationIntentIdentifier,
            Instant publishedAt
    ) {
        requireIdentifier(
                publicationIntentIdentifier,
                "Publication intent identifier"
        );
        Objects.requireNonNull(publishedAt, "publishedAt");

        return dsl.update(OUTBOX)
                .set(PUBLISHED_AT, publishedAt)
                .where(PUBLICATION_INTENT_IDENTIFIER.eq(publicationIntentIdentifier))
                .and(PUBLISHED_AT.isNull())
                .execute() == 1;
    }

    private ConfigurationActivationPublicationIntent toIntent(Record record) {
        return new ConfigurationActivationPublicationIntent(
                record.get(PUBLICATION_INTENT_IDENTIFIER),
                record.get(ACTIVATION_REQUEST_IDENTIFIER),
                record.get(MERCHANT_IDENTIFIER),
                record.get(CONFIGURATION_REVISION_IDENTIFIER),
                record.get(RELEASE_IDENTIFIER),
                record.get(OCCURRED_AT),
                Optional.ofNullable(record.get(PUBLISHED_AT))
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
