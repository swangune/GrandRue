package mainstreet.infrastructure.persistence.privacy;

import mainstreet.application.MerchantScope;
import mainstreet.privacy.DataSubjectReference;
import mainstreet.privacy.DataUsePurpose;
import mainstreet.privacy.PersonalDataUseBasis;
import mainstreet.privacy.PersonalDataUseBasisStore;
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

/** PostgreSQL/jOOQ authority for purpose- and audience-bound personal-data use evidence. */
public final class JooqPersonalDataUseBasisStore
        implements PersonalDataUseBasisStore {

    private static final Table<?> BASIS =
            DSL.table(DSL.name("personal_data_use_basis"));
    private static final Field<String> BASIS_IDENTITY =
            DSL.field(DSL.name("basis_identity"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> SUBJECT_REFERENCE =
            DSL.field(DSL.name("subject_reference"), String.class);
    private static final Field<String> DATA_SCOPE_IDENTIFIER =
            DSL.field(DSL.name("data_scope_identifier"), String.class);
    private static final Field<String> PURPOSE =
            DSL.field(DSL.name("purpose"), String.class);
    private static final Field<String> AUDIENCE_SCOPE_IDENTIFIER =
            DSL.field(DSL.name("audience_scope_identifier"), String.class);
    private static final Field<String> AUTHORITY_SOURCE =
            DSL.field(DSL.name("authority_source"), String.class);
    private static final Field<String> EVIDENCE_REFERENCE =
            DSL.field(DSL.name("evidence_reference"), String.class);
    private static final Field<Instant> EFFECTIVE_FROM =
            DSL.field(DSL.name("effective_from"), Instant.class);
    private static final Field<Instant> EFFECTIVE_UNTIL_EXCLUSIVE =
            DSL.field(DSL.name("effective_until_exclusive"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqPersonalDataUseBasisStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public PersonalDataUseBasis establish(PersonalDataUseBasis candidate) {
        Objects.requireNonNull(candidate, "candidate");
        PersonalDataUseBasis result = transactionTemplate.execute(status -> {
            lockBasis(candidate.basisIdentity());
            Optional<PersonalDataUseBasis> existing = basis(candidate.basisIdentity());
            if (existing.isPresent()) {
                PersonalDataUseBasis committed = existing.orElseThrow();
                if (!committed.equals(candidate)) {
                    throw new IllegalStateException(
                            "Personal-data use basis identity already records different evidence"
                    );
                }
                return committed;
            }

            dsl.insertInto(BASIS)
                    .columns(
                            BASIS_IDENTITY,
                            MERCHANT_IDENTIFIER,
                            SUBJECT_REFERENCE,
                            DATA_SCOPE_IDENTIFIER,
                            PURPOSE,
                            AUDIENCE_SCOPE_IDENTIFIER,
                            AUTHORITY_SOURCE,
                            EVIDENCE_REFERENCE,
                            EFFECTIVE_FROM,
                            EFFECTIVE_UNTIL_EXCLUSIVE
                    )
                    .values(
                            candidate.basisIdentity(),
                            candidate.subject().merchantScope().merchantIdentifier(),
                            candidate.subject().subjectReference(),
                            candidate.dataScopeIdentifier(),
                            candidate.purpose().name(),
                            candidate.audienceScopeIdentifier(),
                            candidate.authoritySource(),
                            candidate.evidenceReference(),
                            candidate.effectiveFrom(),
                            candidate.effectiveUntilExclusive().orElse(null)
                    )
                    .execute();
            return candidate;
        });
        return Objects.requireNonNull(result, "Personal-data use basis transaction returned no result");
    }

    @Override
    public PersonalDataUseBasis cease(
            String basisIdentity,
            Instant effectiveUntilExclusive) {
        requireIdentifier(basisIdentity, "basisIdentity");
        Objects.requireNonNull(effectiveUntilExclusive, "effectiveUntilExclusive");

        PersonalDataUseBasis result = transactionTemplate.execute(status -> {
            lockBasis(basisIdentity);
            PersonalDataUseBasis current = basis(basisIdentity).orElseThrow(() ->
                    new IllegalArgumentException("Unknown personal-data use basis"));

            if (current.effectiveUntilExclusive().isPresent()) {
                Instant committedEnd = current.effectiveUntilExclusive().orElseThrow();
                if (!committedEnd.equals(effectiveUntilExclusive)) {
                    throw new IllegalStateException(
                            "Personal-data use basis cessation is already committed at another instant"
                    );
                }
                return current;
            }
            if (!effectiveUntilExclusive.isAfter(current.effectiveFrom())) {
                throw new IllegalArgumentException(
                        "Personal-data use basis cessation must be after its start"
                );
            }

            dsl.update(BASIS)
                    .set(EFFECTIVE_UNTIL_EXCLUSIVE, effectiveUntilExclusive)
                    .where(BASIS_IDENTITY.eq(basisIdentity))
                    .and(EFFECTIVE_UNTIL_EXCLUSIVE.isNull())
                    .execute();

            return new PersonalDataUseBasis(
                    current.basisIdentity(),
                    current.subject(),
                    current.dataScopeIdentifier(),
                    current.purpose(),
                    current.audienceScopeIdentifier(),
                    current.authoritySource(),
                    current.evidenceReference(),
                    current.effectiveFrom(),
                    Optional.of(effectiveUntilExclusive)
            );
        });
        return Objects.requireNonNull(result, "Personal-data use basis cessation returned no result");
    }

    @Override
    public Optional<PersonalDataUseBasis> basis(String basisIdentity) {
        requireIdentifier(basisIdentity, "basisIdentity");
        Record record = dsl.select(
                        BASIS_IDENTITY,
                        MERCHANT_IDENTIFIER,
                        SUBJECT_REFERENCE,
                        DATA_SCOPE_IDENTIFIER,
                        PURPOSE,
                        AUDIENCE_SCOPE_IDENTIFIER,
                        AUTHORITY_SOURCE,
                        EVIDENCE_REFERENCE,
                        EFFECTIVE_FROM,
                        EFFECTIVE_UNTIL_EXCLUSIVE
                )
                .from(BASIS)
                .where(BASIS_IDENTITY.eq(basisIdentity))
                .fetchOne();
        return Optional.ofNullable(record).map(this::toBasis);
    }

    @Override
    public boolean hasEffectiveBasis(
            DataSubjectReference subject,
            String dataScopeIdentifier,
            DataUsePurpose purpose,
            String audienceScopeIdentifier,
            Instant instant) {
        Objects.requireNonNull(subject, "subject");
        requireIdentifier(dataScopeIdentifier, "dataScopeIdentifier");
        Objects.requireNonNull(purpose, "purpose");
        requireIdentifier(audienceScopeIdentifier, "audienceScopeIdentifier");
        Objects.requireNonNull(instant, "instant");

        return dsl.fetchExists(
                dsl.selectOne()
                        .from(BASIS)
                        .where(MERCHANT_IDENTIFIER.eq(
                                subject.merchantScope().merchantIdentifier()))
                        .and(SUBJECT_REFERENCE.eq(subject.subjectReference()))
                        .and(DATA_SCOPE_IDENTIFIER.eq(dataScopeIdentifier))
                        .and(PURPOSE.eq(purpose.name()))
                        .and(AUDIENCE_SCOPE_IDENTIFIER.eq(audienceScopeIdentifier))
                        .and(EFFECTIVE_FROM.le(instant))
                        .and(
                                EFFECTIVE_UNTIL_EXCLUSIVE.isNull()
                                        .or(EFFECTIVE_UNTIL_EXCLUSIVE.gt(instant))
                        )
        );
    }

    private PersonalDataUseBasis toBasis(Record record) {
        return new PersonalDataUseBasis(
                record.get(BASIS_IDENTITY),
                new DataSubjectReference(
                        new MerchantScope(record.get(MERCHANT_IDENTIFIER)),
                        record.get(SUBJECT_REFERENCE)
                ),
                record.get(DATA_SCOPE_IDENTIFIER),
                DataUsePurpose.valueOf(record.get(PURPOSE)),
                record.get(AUDIENCE_SCOPE_IDENTIFIER),
                record.get(AUTHORITY_SOURCE),
                record.get(EVIDENCE_REFERENCE),
                record.get(EFFECTIVE_FROM),
                Optional.ofNullable(record.get(EFFECTIVE_UNTIL_EXCLUSIVE))
        );
    }

    private void lockBasis(String basisIdentity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 90))",
                basisIdentity
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
