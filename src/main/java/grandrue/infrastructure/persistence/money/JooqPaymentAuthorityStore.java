package grandrue.infrastructure.persistence.money;

import grandrue.application.MerchantScope;
import grandrue.money.CurrencyIdentity;
import grandrue.money.MonetaryAmount;
import grandrue.money.PaymentApplication;
import grandrue.money.PaymentAuthorityStore;
import grandrue.money.PaymentObligation;
import grandrue.money.ProviderPaymentEvidence;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL/jOOQ authority for provider-neutral payment obligations and evidence. */
public final class JooqPaymentAuthorityStore implements PaymentAuthorityStore {

    private static final Table<?> OBLIGATION = DSL.table(DSL.name("payment_obligation"));
    private static final Table<?> EVIDENCE = DSL.table(DSL.name("provider_payment_evidence"));
    private static final Table<?> APPLICATION = DSL.table(DSL.name("payment_application"));

    private static final Field<String> OBLIGATION_IDENTIFIER =
            DSL.field(DSL.name("obligation_identifier"), String.class);
    private static final Field<String> EVIDENCE_IDENTIFIER =
            DSL.field(DSL.name("evidence_identifier"), String.class);
    private static final Field<String> APPLICATION_IDENTIFIER =
            DSL.field(DSL.name("application_identifier"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> COMMERCIAL_SUBJECT_REFERENCE =
            DSL.field(DSL.name("commercial_subject_reference"), String.class);
    private static final Field<String> CURRENCY_IDENTIFIER =
            DSL.field(DSL.name("currency_identifier"), String.class);
    private static final Field<BigDecimal> MINOR_UNIT_AMOUNT =
            DSL.field(DSL.name("minor_unit_amount"), BigDecimal.class);
    private static final Field<String> SOURCE_COMMITMENT_REFERENCE =
            DSL.field(DSL.name("source_commercial_commitment_reference"), String.class);
    private static final Field<String> DUE_CONDITION_IDENTIFIER =
            DSL.field(DSL.name("due_condition_identifier"), String.class);
    private static final Field<String> PROVENANCE_REFERENCE =
            DSL.field(DSL.name("provenance_reference"), String.class);
    private static final Field<Instant> ESTABLISHED_AT =
            DSL.field(DSL.name("established_at"), Instant.class);

    private static final Field<String> PROVIDER_IDENTIFIER =
            DSL.field(DSL.name("provider_identifier"), String.class);
    private static final Field<String> PROVIDER_TRANSACTION_REFERENCE =
            DSL.field(DSL.name("provider_transaction_reference"), String.class);
    private static final Field<String> MAINSTREET_CORRELATION_IDENTIFIER =
            DSL.field(DSL.name("mainstreet_correlation_identifier"), String.class);
    private static final Field<String> PAYMENT_METHOD_CATEGORY =
            DSL.field(DSL.name("payment_method_category"), String.class);
    private static final Field<String> PROVIDER_RESULT_CATEGORY =
            DSL.field(DSL.name("provider_result_category"), String.class);
    private static final Field<Instant> OBSERVED_AT =
            DSL.field(DSL.name("observed_at"), Instant.class);
    private static final Field<String> RECEIPT_EVIDENCE_REFERENCE =
            DSL.field(DSL.name("receipt_evidence_reference"), String.class);

    private static final Field<String> PAYMENT_EVIDENCE_IDENTIFIER =
            DSL.field(DSL.name("payment_evidence_identifier"), String.class);
    private static final Field<Instant> APPLIED_AT =
            DSL.field(DSL.name("applied_at"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqPaymentAuthorityStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public PaymentObligation registerObligation(PaymentObligation candidate) {
        Objects.requireNonNull(candidate, "candidate");
        PaymentObligation result = transactionTemplate.execute(status -> {
            lockIdentity("obligation", candidate.obligationIdentity());
            Optional<PaymentObligation> existing = obligation(
                    candidate.merchantScope(),
                    candidate.obligationIdentity()
            );
            if (existing.isPresent()) {
                PaymentObligation committed = existing.orElseThrow();
                if (!committed.equals(candidate)) {
                    throw new IllegalStateException(
                            "Payment obligation identity already exists with different intent"
                    );
                }
                return committed;
            }

            dsl.insertInto(OBLIGATION)
                    .columns(
                            OBLIGATION_IDENTIFIER,
                            MERCHANT_IDENTIFIER,
                            COMMERCIAL_SUBJECT_REFERENCE,
                            CURRENCY_IDENTIFIER,
                            MINOR_UNIT_AMOUNT,
                            SOURCE_COMMITMENT_REFERENCE,
                            DUE_CONDITION_IDENTIFIER,
                            PROVENANCE_REFERENCE,
                            ESTABLISHED_AT
                    )
                    .values(
                            candidate.obligationIdentity(),
                            candidate.merchantScope().merchantIdentifier(),
                            candidate.commercialSubjectReference(),
                            candidate.obligationAmount().currencyIdentity().identifier(),
                            decimal(candidate.obligationAmount().minorUnitAmount()),
                            candidate.sourceCommercialCommitmentReference(),
                            candidate.dueConditionIdentifier(),
                            candidate.provenanceReference(),
                            candidate.establishedAt()
                    )
                    .execute();
            return candidate;
        });
        return Objects.requireNonNull(result, "Payment obligation transaction returned no result");
    }

    @Override
    public ProviderPaymentEvidence recordProviderEvidence(ProviderPaymentEvidence candidate) {
        Objects.requireNonNull(candidate, "candidate");
        ProviderPaymentEvidence result = transactionTemplate.execute(status -> {
            lockIdentity("evidence", candidate.evidenceIdentity());
            Optional<ProviderPaymentEvidence> existing = providerEvidence(
                    candidate.merchantScope(),
                    candidate.evidenceIdentity()
            );
            if (existing.isPresent()) {
                ProviderPaymentEvidence committed = existing.orElseThrow();
                if (!committed.equals(candidate)) {
                    throw new IllegalStateException(
                            "Provider payment evidence identity already exists with different evidence"
                    );
                }
                return committed;
            }

            dsl.insertInto(EVIDENCE)
                    .columns(
                            EVIDENCE_IDENTIFIER,
                            MERCHANT_IDENTIFIER,
                            PROVIDER_IDENTIFIER,
                            PROVIDER_TRANSACTION_REFERENCE,
                            MAINSTREET_CORRELATION_IDENTIFIER,
                            CURRENCY_IDENTIFIER,
                            MINOR_UNIT_AMOUNT,
                            PAYMENT_METHOD_CATEGORY,
                            PROVIDER_RESULT_CATEGORY,
                            OBSERVED_AT,
                            RECEIPT_EVIDENCE_REFERENCE
                    )
                    .values(
                            candidate.evidenceIdentity(),
                            candidate.merchantScope().merchantIdentifier(),
                            candidate.providerIdentifier(),
                            candidate.providerTransactionReference(),
                            candidate.mainStreetCorrelationIdentity(),
                            candidate.amount().currencyIdentity().identifier(),
                            decimal(candidate.amount().minorUnitAmount()),
                            candidate.paymentMethodCategory(),
                            candidate.providerResultCategory(),
                            candidate.observedAt(),
                            candidate.receiptEvidenceReference().orElse(null)
                    )
                    .execute();
            return candidate;
        });
        return Objects.requireNonNull(result, "Provider evidence transaction returned no result");
    }

    @Override
    public PaymentApplication applyPayment(PaymentApplication candidate) {
        Objects.requireNonNull(candidate, "candidate");
        PaymentApplication result = transactionTemplate.execute(status -> {
            lockIdentity("application", candidate.applicationIdentity());
            Optional<PaymentApplication> replay = application(candidate.applicationIdentity());
            if (replay.isPresent()) {
                PaymentApplication committed = replay.orElseThrow();
                if (!committed.equals(candidate)) {
                    throw new IllegalStateException(
                            "Payment application identity already exists with different intent"
                    );
                }
                return committed;
            }

            Record obligationRow = lockObligation(candidate);
            Record evidenceRow = lockEvidence(candidate);
            MonetaryAmount obligationAmount = amountFrom(obligationRow);
            MonetaryAmount evidenceAmount = amountFrom(evidenceRow);
            requireSameCurrency(candidate.appliedAmount(), obligationAmount, "obligation");
            requireSameCurrency(candidate.appliedAmount(), evidenceAmount, "provider evidence");

            Instant obligationEstablishedAt = obligationRow.get(ESTABLISHED_AT);
            Instant evidenceObservedAt = evidenceRow.get(OBSERVED_AT);
            if (candidate.appliedAt().isBefore(obligationEstablishedAt)
                    || candidate.appliedAt().isBefore(evidenceObservedAt)) {
                throw new IllegalArgumentException(
                        "Payment application cannot predate its obligation or provider evidence"
                );
            }

            BigInteger alreadyAppliedToObligation = sumAppliedToObligation(
                    candidate.merchantScope(),
                    candidate.obligationIdentity()
            );
            BigInteger alreadyAppliedFromEvidence = sumAppliedFromEvidence(
                    candidate.merchantScope(),
                    candidate.paymentEvidenceIdentity()
            );
            BigInteger requested = candidate.appliedAmount().minorUnitAmount();
            BigInteger obligationRemaining = obligationAmount.minorUnitAmount()
                    .subtract(alreadyAppliedToObligation);
            BigInteger evidenceRemaining = evidenceAmount.minorUnitAmount()
                    .subtract(alreadyAppliedFromEvidence);

            if (requested.compareTo(obligationRemaining) > 0) {
                throw new IllegalStateException(
                        "Payment application exceeds remaining payment obligation"
                );
            }
            if (requested.compareTo(evidenceRemaining) > 0) {
                throw new IllegalStateException(
                        "Payment application exceeds unapplied provider evidence amount"
                );
            }

            dsl.insertInto(APPLICATION)
                    .columns(
                            APPLICATION_IDENTIFIER,
                            MERCHANT_IDENTIFIER,
                            OBLIGATION_IDENTIFIER,
                            PAYMENT_EVIDENCE_IDENTIFIER,
                            CURRENCY_IDENTIFIER,
                            MINOR_UNIT_AMOUNT,
                            APPLIED_AT,
                            PROVENANCE_REFERENCE
                    )
                    .values(
                            candidate.applicationIdentity(),
                            candidate.merchantScope().merchantIdentifier(),
                            candidate.obligationIdentity(),
                            candidate.paymentEvidenceIdentity(),
                            candidate.appliedAmount().currencyIdentity().identifier(),
                            decimal(candidate.appliedAmount().minorUnitAmount()),
                            candidate.appliedAt(),
                            candidate.provenanceReference()
                    )
                    .execute();
            return candidate;
        });
        return Objects.requireNonNull(result, "Payment application transaction returned no result");
    }

    @Override
    public Optional<PaymentObligation> obligation(
            MerchantScope merchantScope,
            String obligationIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(obligationIdentity, "obligationIdentity");
        Record row = dsl.select(
                        OBLIGATION_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        COMMERCIAL_SUBJECT_REFERENCE,
                        CURRENCY_IDENTIFIER,
                        MINOR_UNIT_AMOUNT,
                        SOURCE_COMMITMENT_REFERENCE,
                        DUE_CONDITION_IDENTIFIER,
                        PROVENANCE_REFERENCE,
                        ESTABLISHED_AT
                )
                .from(OBLIGATION)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OBLIGATION_IDENTIFIER.eq(obligationIdentity))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toObligation);
    }

    @Override
    public Optional<ProviderPaymentEvidence> providerEvidence(
            MerchantScope merchantScope,
            String evidenceIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(evidenceIdentity, "evidenceIdentity");
        Record row = dsl.select(
                        EVIDENCE_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        PROVIDER_IDENTIFIER,
                        PROVIDER_TRANSACTION_REFERENCE,
                        MAINSTREET_CORRELATION_IDENTIFIER,
                        CURRENCY_IDENTIFIER,
                        MINOR_UNIT_AMOUNT,
                        PAYMENT_METHOD_CATEGORY,
                        PROVIDER_RESULT_CATEGORY,
                        OBSERVED_AT,
                        RECEIPT_EVIDENCE_REFERENCE
                )
                .from(EVIDENCE)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(EVIDENCE_IDENTIFIER.eq(evidenceIdentity))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toEvidence);
    }

    @Override
    public List<PaymentApplication> applications(
            MerchantScope merchantScope,
            String obligationIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(obligationIdentity, "obligationIdentity");
        return dsl.select(
                        APPLICATION_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        OBLIGATION_IDENTIFIER,
                        PAYMENT_EVIDENCE_IDENTIFIER,
                        CURRENCY_IDENTIFIER,
                        MINOR_UNIT_AMOUNT,
                        APPLIED_AT,
                        PROVENANCE_REFERENCE
                )
                .from(APPLICATION)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OBLIGATION_IDENTIFIER.eq(obligationIdentity))
                .orderBy(APPLIED_AT, APPLICATION_IDENTIFIER)
                .fetch(this::toApplication);
    }

    @Override
    public MonetaryAmount currentAmountDue(
            MerchantScope merchantScope,
            String obligationIdentity
    ) {
        PaymentObligation obligation = obligation(merchantScope, obligationIdentity)
                .orElseThrow(() -> new IllegalStateException(
                        "Unknown payment obligation: " + obligationIdentity
                ));
        BigInteger applied = sumAppliedToObligation(merchantScope, obligationIdentity);
        return new MonetaryAmount(
                obligation.obligationAmount().currencyIdentity(),
                obligation.obligationAmount().minorUnitAmount().subtract(applied)
        );
    }

    private Optional<PaymentApplication> application(String applicationIdentity) {
        Record row = dsl.select(
                        APPLICATION_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        OBLIGATION_IDENTIFIER,
                        PAYMENT_EVIDENCE_IDENTIFIER,
                        CURRENCY_IDENTIFIER,
                        MINOR_UNIT_AMOUNT,
                        APPLIED_AT,
                        PROVENANCE_REFERENCE
                )
                .from(APPLICATION)
                .where(APPLICATION_IDENTIFIER.eq(applicationIdentity))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toApplication);
    }

    private Record lockObligation(PaymentApplication candidate) {
        Record row = dsl.select(
                        OBLIGATION_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        CURRENCY_IDENTIFIER,
                        MINOR_UNIT_AMOUNT,
                        ESTABLISHED_AT
                )
                .from(OBLIGATION)
                .where(MERCHANT_IDENTIFIER.eq(candidate.merchantScope().merchantIdentifier()))
                .and(OBLIGATION_IDENTIFIER.eq(candidate.obligationIdentity()))
                .forUpdate()
                .fetchOne();
        if (row == null) {
            throw new IllegalStateException(
                    "Unknown payment obligation in Merchant Scope"
            );
        }
        return row;
    }

    private Record lockEvidence(PaymentApplication candidate) {
        Record row = dsl.select(
                        EVIDENCE_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        CURRENCY_IDENTIFIER,
                        MINOR_UNIT_AMOUNT,
                        OBSERVED_AT
                )
                .from(EVIDENCE)
                .where(MERCHANT_IDENTIFIER.eq(candidate.merchantScope().merchantIdentifier()))
                .and(EVIDENCE_IDENTIFIER.eq(candidate.paymentEvidenceIdentity()))
                .forUpdate()
                .fetchOne();
        if (row == null) {
            throw new IllegalStateException(
                    "Unknown provider payment evidence in Merchant Scope"
            );
        }
        return row;
    }

    private BigInteger sumAppliedToObligation(
            MerchantScope merchantScope,
            String obligationIdentity
    ) {
        BigDecimal total = dsl.select(
                        DSL.coalesce(DSL.sum(MINOR_UNIT_AMOUNT), BigDecimal.ZERO)
                )
                .from(APPLICATION)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OBLIGATION_IDENTIFIER.eq(obligationIdentity))
                .fetchOne(0, BigDecimal.class);
        return Objects.requireNonNull(total).toBigIntegerExact();
    }

    private BigInteger sumAppliedFromEvidence(
            MerchantScope merchantScope,
            String evidenceIdentity
    ) {
        BigDecimal total = dsl.select(
                        DSL.coalesce(DSL.sum(MINOR_UNIT_AMOUNT), BigDecimal.ZERO)
                )
                .from(APPLICATION)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(PAYMENT_EVIDENCE_IDENTIFIER.eq(evidenceIdentity))
                .fetchOne(0, BigDecimal.class);
        return Objects.requireNonNull(total).toBigIntegerExact();
    }

    private PaymentObligation toObligation(Record row) {
        return new PaymentObligation(
                row.get(OBLIGATION_IDENTIFIER),
                new MerchantScope(row.get(MERCHANT_IDENTIFIER)),
                row.get(COMMERCIAL_SUBJECT_REFERENCE),
                amountFrom(row),
                row.get(SOURCE_COMMITMENT_REFERENCE),
                row.get(DUE_CONDITION_IDENTIFIER),
                row.get(PROVENANCE_REFERENCE),
                row.get(ESTABLISHED_AT)
        );
    }

    private ProviderPaymentEvidence toEvidence(Record row) {
        return new ProviderPaymentEvidence(
                row.get(EVIDENCE_IDENTIFIER),
                new MerchantScope(row.get(MERCHANT_IDENTIFIER)),
                row.get(PROVIDER_IDENTIFIER),
                row.get(PROVIDER_TRANSACTION_REFERENCE),
                row.get(MAINSTREET_CORRELATION_IDENTIFIER),
                amountFrom(row),
                row.get(PAYMENT_METHOD_CATEGORY),
                row.get(PROVIDER_RESULT_CATEGORY),
                row.get(OBSERVED_AT),
                Optional.ofNullable(row.get(RECEIPT_EVIDENCE_REFERENCE))
        );
    }

    private PaymentApplication toApplication(Record row) {
        return new PaymentApplication(
                row.get(APPLICATION_IDENTIFIER),
                new MerchantScope(row.get(MERCHANT_IDENTIFIER)),
                row.get(OBLIGATION_IDENTIFIER),
                row.get(PAYMENT_EVIDENCE_IDENTIFIER),
                amountFrom(row),
                row.get(APPLIED_AT),
                row.get(PROVENANCE_REFERENCE)
        );
    }

    private MonetaryAmount amountFrom(Record row) {
        return new MonetaryAmount(
                new CurrencyIdentity(row.get(CURRENCY_IDENTIFIER)),
                row.get(MINOR_UNIT_AMOUNT).toBigIntegerExact()
        );
    }

    private static void requireSameCurrency(
            MonetaryAmount applied,
            MonetaryAmount source,
            String sourceLabel
    ) {
        if (!applied.currencyIdentity().equals(source.currencyIdentity())) {
            throw new IllegalArgumentException(
                    "Payment application currency does not match " + sourceLabel
            );
        }
    }

    private void lockIdentity(String kind, String identity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                "money|" + kind + "|" + identity
        );
    }

    private static BigDecimal decimal(BigInteger value) {
        return new BigDecimal(value);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
