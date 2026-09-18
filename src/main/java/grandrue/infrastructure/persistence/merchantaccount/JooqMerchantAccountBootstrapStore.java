package grandrue.infrastructure.persistence.merchantaccount;

import grandrue.application.MerchantScope;
import grandrue.application.TrustedPlatformHumanPrincipal;
import grandrue.merchantaccount.MerchantAccount;
import grandrue.merchantaccount.MerchantAccountBootstrapStore;
import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * PostgreSQL/jOOQ persistence adapter for the accepted ordinary Merchant Account
 * bootstrap invariant.
 *
 * <p>The adapter owns no business policy. It atomically persists the Merchant
 * Account existence fact, the initiating identity's initial ACTIVE Merchant
 * Controller relationship, logical-request replay evidence, immutable temporal
 * establishment evidence and durable publication intent for the committed
 * MerchantAccountEstablished fact.</p>
 */
public final class JooqMerchantAccountBootstrapStore
        implements MerchantAccountBootstrapStore {

    private static final Table<?> MERCHANT_ACCOUNT =
            DSL.table(DSL.name("merchant_account"));
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);

    private static final Table<?> CONTROLLER_RELATIONSHIP =
            DSL.table(DSL.name("merchant_controller_relationship"));
    private static final Field<String> CONTROLLER_RELATIONSHIP_IDENTIFIER =
            DSL.field(DSL.name("controller_relationship_identifier"), String.class);
    private static final Field<String> IDENTITY_IDENTIFIER =
            DSL.field(DSL.name("identity_identifier"), String.class);
    private static final Field<String> CONTROLLER_LIFECYCLE =
            DSL.field(DSL.name("lifecycle"), String.class);

    private static final Table<?> ESTABLISHMENT_REQUEST =
            DSL.table(DSL.name("merchant_account_establishment_request"));
    private static final Field<String> LOGICAL_REQUEST_IDENTITY =
            DSL.field(DSL.name("logical_establishment_request_identity"), String.class);
    private static final Field<String> INITIAL_CONTROLLER_RELATIONSHIP_IDENTIFIER =
            DSL.field(DSL.name("initial_controller_relationship_identifier"), String.class);

    private static final Table<?> ESTABLISHMENT =
            DSL.table(DSL.name("merchant_account_establishment"));
    private static final Field<String> ESTABLISHMENT_IDENTITY =
            DSL.field(DSL.name("establishment_identity"), String.class);
    private static final Field<Instant> ESTABLISHED_AT =
            DSL.field(DSL.name("established_at"), Instant.class);

    private static final Table<?> ESTABLISHMENT_PUBLICATION_INTENT =
            DSL.table(DSL.name("merchant_account_establishment_publication_intent"));
    private static final Field<String> PUBLICATION_INTENT_IDENTIFIER =
            DSL.field(DSL.name("publication_intent_identifier"), String.class);
    private static final Field<Instant> OCCURRED_AT =
            DSL.field(DSL.name("occurred_at"), Instant.class);

    private static final Field<String> EVENT_OWNER_IDENTIFIER =
            DSL.field(DSL.name("event_owner_identifier"), String.class);
    private static final Field<String> EVENT_CONTRACT_IDENTIFIER =
            DSL.field(DSL.name("event_contract_identifier"), String.class);
    private static final Field<String> EVENT_SEMANTIC_RELEASE =
            DSL.field(DSL.name("event_semantic_release"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final Clock clock;
    private final Supplier<String> establishmentIdentityFactory;
    private final Supplier<String> publicationIntentIdentifierFactory;

    public JooqMerchantAccountBootstrapStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager) {
        this(
                dsl,
                transactionManager,
                Clock.systemUTC(),
                () -> "establishment-" + UUID.randomUUID(),
                () -> "publication-" + UUID.randomUUID()
        );
    }

    public JooqMerchantAccountBootstrapStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Clock clock,
            Supplier<String> establishmentIdentityFactory,
            Supplier<String> publicationIntentIdentifierFactory) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.clock = Objects.requireNonNull(clock, "clock");
        this.establishmentIdentityFactory = Objects.requireNonNull(
                establishmentIdentityFactory,
                "establishmentIdentityFactory"
        );
        this.publicationIntentIdentifierFactory = Objects.requireNonNull(
                publicationIntentIdentifierFactory,
                "publicationIntentIdentifierFactory"
        );
    }

    @Override
    public BootstrapOutcome establishIfAbsent(
            String logicalEstablishmentRequestIdentity,
            TrustedPlatformHumanPrincipal initialController) {
        if (logicalEstablishmentRequestIdentity == null
                || logicalEstablishmentRequestIdentity.isBlank()) {
            throw new IllegalArgumentException(
                    "logicalEstablishmentRequestIdentity must not be blank"
            );
        }
        Objects.requireNonNull(initialController, "initialController");

        BootstrapOutcome outcome = transactionTemplate.execute(status -> {
            lockLogicalRequest(logicalEstablishmentRequestIdentity);

            String existingMerchantIdentifier = dsl
                    .select(MERCHANT_IDENTIFIER)
                    .from(ESTABLISHMENT_REQUEST)
                    .where(LOGICAL_REQUEST_IDENTITY.eq(logicalEstablishmentRequestIdentity))
                    .fetchOne(MERCHANT_IDENTIFIER);

            if (existingMerchantIdentifier != null) {
                requireTemporalEvidence(
                        existingMerchantIdentifier,
                        logicalEstablishmentRequestIdentity
                );
                return new BootstrapOutcome(
                        merchantAccount(existingMerchantIdentifier),
                        true
                );
            }

            String merchantIdentifier = newMerchantIdentifier();
            String controllerRelationshipIdentifier =
                    newControllerRelationshipIdentifier();
            String establishmentIdentity = requireGeneratedIdentifier(
                    establishmentIdentityFactory.get(),
                    "establishment identity"
            );
            String publicationIntentIdentifier = requireGeneratedIdentifier(
                    publicationIntentIdentifierFactory.get(),
                    "publication intent identifier"
            );
            Instant establishedAt = clock.instant();

            dsl.insertInto(MERCHANT_ACCOUNT)
                    .columns(MERCHANT_IDENTIFIER)
                    .values(merchantIdentifier)
                    .execute();

            dsl.insertInto(CONTROLLER_RELATIONSHIP)
                    .columns(
                            CONTROLLER_RELATIONSHIP_IDENTIFIER,
                            MERCHANT_IDENTIFIER,
                            IDENTITY_IDENTIFIER,
                            CONTROLLER_LIFECYCLE
                    )
                    .values(
                            controllerRelationshipIdentifier,
                            merchantIdentifier,
                            initialController.identityIdentifier(),
                            "ACTIVE"
                    )
                    .execute();

            dsl.insertInto(ESTABLISHMENT_REQUEST)
                    .columns(
                            LOGICAL_REQUEST_IDENTITY,
                            MERCHANT_IDENTIFIER,
                            INITIAL_CONTROLLER_RELATIONSHIP_IDENTIFIER
                    )
                    .values(
                            logicalEstablishmentRequestIdentity,
                            merchantIdentifier,
                            controllerRelationshipIdentifier
                    )
                    .execute();

            dsl.insertInto(ESTABLISHMENT)
                    .columns(
                            ESTABLISHMENT_IDENTITY,
                            MERCHANT_IDENTIFIER,
                            LOGICAL_REQUEST_IDENTITY,
                            ESTABLISHED_AT
                    )
                    .values(
                            establishmentIdentity,
                            merchantIdentifier,
                            logicalEstablishmentRequestIdentity,
                            establishedAt
                    )
                    .execute();

            dsl.insertInto(ESTABLISHMENT_PUBLICATION_INTENT)
                    .columns(
                            PUBLICATION_INTENT_IDENTIFIER,
                            ESTABLISHMENT_IDENTITY,
                            MERCHANT_IDENTIFIER,
                            LOGICAL_REQUEST_IDENTITY,
                            OCCURRED_AT,
                            EVENT_OWNER_IDENTIFIER,
                            EVENT_CONTRACT_IDENTIFIER,
                            EVENT_SEMANTIC_RELEASE
                    )
                    .values(
                            publicationIntentIdentifier,
                            establishmentIdentity,
                            merchantIdentifier,
                            logicalEstablishmentRequestIdentity,
                            establishedAt,
                            MerchantAccountEstablishedEventContract.AFFINITY.contractIdentity().ownerIdentifier(),
                            MerchantAccountEstablishedEventContract.AFFINITY.contractIdentity().contractIdentifier(),
                            MerchantAccountEstablishedEventContract.AFFINITY.semanticRegistryReleaseIdentifier()
                    )
                    .execute();

            return new BootstrapOutcome(
                    merchantAccount(merchantIdentifier),
                    false
            );
        });

        return Objects.requireNonNull(
                outcome,
                "Merchant Account bootstrap transaction returned no outcome"
        );
    }

    private void requireTemporalEvidence(
            String merchantIdentifier,
            String logicalEstablishmentRequestIdentity) {
        Integer count = dsl
                .selectCount()
                .from(ESTABLISHMENT)
                .where(MERCHANT_IDENTIFIER.eq(merchantIdentifier))
                .and(LOGICAL_REQUEST_IDENTITY.eq(logicalEstablishmentRequestIdentity))
                .fetchOne(0, Integer.class);
        if (count == null || count != 1) {
            throw new IllegalStateException(
                    "Committed Merchant Account bootstrap lacks authoritative establishment evidence"
            );
        }
    }

    private void lockLogicalRequest(String logicalEstablishmentRequestIdentity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                logicalEstablishmentRequestIdentity
        );
    }

    private static MerchantAccount merchantAccount(String merchantIdentifier) {
        return new MerchantAccount(new MerchantScope(merchantIdentifier));
    }

    private static String newMerchantIdentifier() {
        return "merchant-" + UUID.randomUUID();
    }

    private static String newControllerRelationshipIdentifier() {
        return "controller-" + UUID.randomUUID();
    }

    private static String requireGeneratedIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(label + " factory returned blank identifier");
        }
        return value;
    }
}
