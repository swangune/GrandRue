package grandrue.infrastructure.persistence.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.application.TrustedDeviceApplicationContext;
import mainstreet.runtime.AuthorizationException;
import mainstreet.runtime.ExecutionPrincipal;
import grandrue.workforce.MerchantOperationalDeviceAuthorisation;
import grandrue.workforce.MerchantOperationalDeviceAuthorisationLifecycle;
import grandrue.workforce.MerchantOperationalDeviceAuthorisationStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL/jOOQ adapter for Merchant Operational Device Authorisation.
 *
 * <p>Controller authority is revalidated from the authoritative Merchant
 * Controller relationship inside the same transaction that persists or revokes
 * the workforce device-authorisation fact. The adapter stores only a trusted
 * binding reference; it does not define the security mechanism that produced
 * that reference.</p>
 */
public final class JooqMerchantOperationalDeviceAuthorisationStore
        implements MerchantOperationalDeviceAuthorisationStore {

    private static final Table<?> AUTHORISATION = DSL.table(
            DSL.name("workforce_operational_device_authorisation")
    );
    private static final Table<?> REQUEST = DSL.table(
            DSL.name("workforce_operational_device_authorisation_request")
    );
    private static final Table<?> CONTROLLER = DSL.table(
            DSL.name("merchant_controller_relationship")
    );

    private static final Field<String> AUTHORISATION_IDENTIFIER =
            DSL.field(DSL.name("authorisation_identifier"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> BINDING_REFERENCE =
            DSL.field(DSL.name("binding_reference"), String.class);
    private static final Field<String> AUTHORISED_BY_IDENTITY_REFERENCE =
            DSL.field(DSL.name("authorised_by_identity_reference"), String.class);
    private static final Field<Instant> AUTHORISED_AT =
            DSL.field(DSL.name("authorised_at"), Instant.class);
    private static final Field<String> LIFECYCLE =
            DSL.field(DSL.name("lifecycle"), String.class);
    private static final Field<Instant> REVOKED_AT =
            DSL.field(DSL.name("revoked_at"), Instant.class);

    private static final Field<String> LOGICAL_REQUEST_IDENTITY =
            DSL.field(DSL.name("logical_request_identity"), String.class);
    private static final Field<String> CONTROLLER_IDENTITY_REFERENCE =
            DSL.field(DSL.name("controller_identity_reference"), String.class);

    private static final Field<String> CONTROLLER_RELATIONSHIP_IDENTIFIER =
            DSL.field(
                    DSL.name("controller_relationship_identifier"),
                    String.class
            );
    private static final Field<String> IDENTITY_IDENTIFIER =
            DSL.field(DSL.name("identity_identifier"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantOperationalDeviceAuthorisationStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantOperationalDeviceAuthorisation establishIfCurrentController(
            String logicalRequestIdentity,
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal,
            TrustedDeviceApplicationContext deviceContext,
            String authorisationIdentifier,
            Instant authorisedAt
    ) {
        requireIdentifier(logicalRequestIdentity, "Logical request identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(controllerPrincipal, "controllerPrincipal");
        Objects.requireNonNull(deviceContext, "deviceContext");
        requireIdentifier(
                authorisationIdentifier,
                "Operational device authorisation identifier"
        );
        Objects.requireNonNull(authorisedAt, "authorisedAt");

        MerchantOperationalDeviceAuthorisation result = transactions.execute(status -> {
            lockLogicalRequest(logicalRequestIdentity);

            Record committedRequest = requestRecord(logicalRequestIdentity);
            if (committedRequest != null) {
                requireSameRequestIntent(
                        committedRequest,
                        merchantScope,
                        controllerPrincipal,
                        deviceContext
                );
                return requireAuthorisation(
                        committedRequest.get(AUTHORISATION_IDENTIFIER)
                );
            }

            lockDeviceBinding(merchantScope, deviceContext);
            requireCurrentController(merchantScope, controllerPrincipal);

            MerchantOperationalDeviceAuthorisation active = activeByBinding(
                    merchantScope,
                    deviceContext
            );
            if (active != null) {
                insertRequestEvidence(
                        logicalRequestIdentity,
                        merchantScope,
                        controllerPrincipal,
                        deviceContext,
                        active.authorisationIdentifier()
                );
                return active;
            }

            MerchantOperationalDeviceAuthorisation candidate =
                    MerchantOperationalDeviceAuthorisation.authorised(
                            authorisationIdentifier,
                            merchantScope,
                            deviceContext.bindingReference(),
                            controllerPrincipal.identifier(),
                            authorisedAt
                    );

            dsl.insertInto(AUTHORISATION)
                    .columns(
                            AUTHORISATION_IDENTIFIER,
                            MERCHANT_IDENTIFIER,
                            BINDING_REFERENCE,
                            AUTHORISED_BY_IDENTITY_REFERENCE,
                            AUTHORISED_AT,
                            LIFECYCLE,
                            REVOKED_AT
                    )
                    .values(
                            candidate.authorisationIdentifier(),
                            merchantScope.merchantIdentifier(),
                            candidate.bindingReference(),
                            candidate.authorisedByIdentityReference(),
                            candidate.authorisedAt(),
                            MerchantOperationalDeviceAuthorisationLifecycle.ACTIVE.name(),
                            null
                    )
                    .execute();

            insertRequestEvidence(
                    logicalRequestIdentity,
                    merchantScope,
                    controllerPrincipal,
                    deviceContext,
                    candidate.authorisationIdentifier()
            );
            return candidate;
        });

        return Objects.requireNonNull(
                result,
                "Operational device authorisation transaction returned no result"
        );
    }

    @Override
    public boolean revokeIfCurrentController(
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal,
            String authorisationIdentifier,
            Instant revokedAt
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(controllerPrincipal, "controllerPrincipal");
        requireIdentifier(
                authorisationIdentifier,
                "Operational device authorisation identifier"
        );
        Objects.requireNonNull(revokedAt, "revokedAt");

        Boolean result = transactions.execute(status -> {
            requireCurrentController(merchantScope, controllerPrincipal);

            Record current = dsl.select(AUTHORISED_AT, LIFECYCLE)
                    .from(AUTHORISATION)
                    .where(AUTHORISATION_IDENTIFIER.eq(authorisationIdentifier))
                    .and(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                    .forUpdate()
                    .fetchOne();
            if (current == null) {
                return false;
            }
            if (!MerchantOperationalDeviceAuthorisationLifecycle.ACTIVE.name()
                    .equals(current.get(LIFECYCLE))) {
                return false;
            }
            Instant authorisedAt = current.get(AUTHORISED_AT);
            if (revokedAt.isBefore(authorisedAt)) {
                throw new IllegalArgumentException(
                        "Device authorisation revocation cannot precede authorisation"
                );
            }

            return dsl.update(AUTHORISATION)
                    .set(
                            LIFECYCLE,
                            MerchantOperationalDeviceAuthorisationLifecycle.REVOKED.name()
                    )
                    .set(REVOKED_AT, revokedAt)
                    .where(AUTHORISATION_IDENTIFIER.eq(authorisationIdentifier))
                    .and(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                    .and(LIFECYCLE.eq(
                            MerchantOperationalDeviceAuthorisationLifecycle.ACTIVE.name()
                    ))
                    .execute() == 1;
        });

        return Boolean.TRUE.equals(result);
    }

    @Override
    public Optional<MerchantOperationalDeviceAuthorisation> find(
            String authorisationIdentifier
    ) {
        requireIdentifier(
                authorisationIdentifier,
                "Operational device authorisation identifier"
        );
        return Optional.ofNullable(authorisationRecord(authorisationIdentifier))
                .map(this::toAuthorisation);
    }

    @Override
    public boolean isActive(
            MerchantScope merchantScope,
            TrustedDeviceApplicationContext deviceContext
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(deviceContext, "deviceContext");
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(AUTHORISATION)
                        .where(MERCHANT_IDENTIFIER.eq(
                                merchantScope.merchantIdentifier()
                        ))
                        .and(BINDING_REFERENCE.eq(deviceContext.bindingReference()))
                        .and(LIFECYCLE.eq(
                                MerchantOperationalDeviceAuthorisationLifecycle.ACTIVE.name()
                        ))
        );
    }

    private Record requestRecord(String logicalRequestIdentity) {
        return dsl.select(
                        MERCHANT_IDENTIFIER,
                        BINDING_REFERENCE,
                        CONTROLLER_IDENTITY_REFERENCE,
                        AUTHORISATION_IDENTIFIER
                )
                .from(REQUEST)
                .where(LOGICAL_REQUEST_IDENTITY.eq(logicalRequestIdentity))
                .fetchOne();
    }

    private void requireSameRequestIntent(
            Record committedRequest,
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal,
            TrustedDeviceApplicationContext deviceContext
    ) {
        boolean same = merchantScope.merchantIdentifier().equals(
                committedRequest.get(MERCHANT_IDENTIFIER)
        ) && controllerPrincipal.identifier().equals(
                committedRequest.get(CONTROLLER_IDENTITY_REFERENCE)
        ) && deviceContext.bindingReference().equals(
                committedRequest.get(BINDING_REFERENCE)
        );
        if (!same) {
            throw new IllegalArgumentException(
                    "Committed operational device request identity cannot be reused "
                            + "for different intent"
            );
        }
    }

    private void insertRequestEvidence(
            String logicalRequestIdentity,
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal,
            TrustedDeviceApplicationContext deviceContext,
            String authorisationIdentifier
    ) {
        dsl.insertInto(REQUEST)
                .columns(
                        LOGICAL_REQUEST_IDENTITY,
                        MERCHANT_IDENTIFIER,
                        BINDING_REFERENCE,
                        CONTROLLER_IDENTITY_REFERENCE,
                        AUTHORISATION_IDENTIFIER
                )
                .values(
                        logicalRequestIdentity,
                        merchantScope.merchantIdentifier(),
                        deviceContext.bindingReference(),
                        controllerPrincipal.identifier(),
                        authorisationIdentifier
                )
                .execute();
    }

    private void requireCurrentController(
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal
    ) {
        Record controller = dsl.select(CONTROLLER_RELATIONSHIP_IDENTIFIER)
                .from(CONTROLLER)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(IDENTITY_IDENTIFIER.eq(controllerPrincipal.identifier()))
                .and(LIFECYCLE.eq("ACTIVE"))
                .forShare()
                .fetchOne();
        if (controller == null) {
            throw new AuthorizationException(
                    "Current Merchant Controller authority is required for "
                            + "operational device authorisation"
            );
        }
    }

    private MerchantOperationalDeviceAuthorisation activeByBinding(
            MerchantScope merchantScope,
            TrustedDeviceApplicationContext deviceContext
    ) {
        Record record = dsl.select(
                        AUTHORISATION_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        BINDING_REFERENCE,
                        AUTHORISED_BY_IDENTITY_REFERENCE,
                        AUTHORISED_AT,
                        LIFECYCLE,
                        REVOKED_AT
                )
                .from(AUTHORISATION)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(BINDING_REFERENCE.eq(deviceContext.bindingReference()))
                .and(LIFECYCLE.eq(
                        MerchantOperationalDeviceAuthorisationLifecycle.ACTIVE.name()
                ))
                .fetchOne();
        return record == null ? null : toAuthorisation(record);
    }

    private MerchantOperationalDeviceAuthorisation requireAuthorisation(
            String authorisationIdentifier
    ) {
        return find(authorisationIdentifier).orElseThrow(() ->
                new IllegalStateException(
                        "Committed operational device request references missing "
                                + "authorisation: " + authorisationIdentifier
                )
        );
    }

    private Record authorisationRecord(String authorisationIdentifier) {
        return dsl.select(
                        AUTHORISATION_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        BINDING_REFERENCE,
                        AUTHORISED_BY_IDENTITY_REFERENCE,
                        AUTHORISED_AT,
                        LIFECYCLE,
                        REVOKED_AT
                )
                .from(AUTHORISATION)
                .where(AUTHORISATION_IDENTIFIER.eq(authorisationIdentifier))
                .fetchOne();
    }

    private MerchantOperationalDeviceAuthorisation toAuthorisation(Record record) {
        return MerchantOperationalDeviceAuthorisation.reconstitute(
                record.get(AUTHORISATION_IDENTIFIER),
                new MerchantScope(record.get(MERCHANT_IDENTIFIER)),
                record.get(BINDING_REFERENCE),
                record.get(AUTHORISED_BY_IDENTITY_REFERENCE),
                record.get(AUTHORISED_AT),
                Optional.ofNullable(record.get(REVOKED_AT))
        );
    }

    private void lockLogicalRequest(String logicalRequestIdentity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 74))",
                logicalRequestIdentity
        );
    }

    private void lockDeviceBinding(
            MerchantScope merchantScope,
            TrustedDeviceApplicationContext deviceContext
    ) {
        String merchantIdentifier = merchantScope.merchantIdentifier();
        String bindingReference = deviceContext.bindingReference();
        String lockIdentity = merchantIdentifier.length()
                + ":"
                + merchantIdentifier
                + bindingReference.length()
                + ":"
                + bindingReference;

        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 75))",
                lockIdentity
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
