package grandrue.infrastructure.persistence.enquiry;

import grandrue.application.MerchantScope;
import mainstreet.enquiry.EnquiryIdentityConflictException;
import mainstreet.enquiry.EnquiryRevisionProvenance;
import mainstreet.enquiry.EnquirySemanticContext;
import mainstreet.enquiry.EnquirySubmission;
import mainstreet.enquiry.EnquirySubmissionStore;
import mainstreet.enquiry.EnquirySubmittedContact;
import mainstreet.semantic.registry.OwnedOperationalObjectTypeReference;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * SQL-explicit Enquiry persistence. A single insert atomically preserves all initial evidence.
 * Supply a transaction-aware DSLContext to participate in the application transaction.
 * No writes or foreign keys into subject/customer owners and no public read authority.
 */
public final class JooqEnquirySubmissionStore implements EnquirySubmissionStore {
    private static final Table<?> SUBMISSIONS = DSL.table(DSL.name("enquiry_submission"));
    private static final Field<String> MERCHANT = DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> IDENTITY = DSL.field(DSL.name("enquiry_identity"), String.class);
    private static final Field<Long> SECONDS = DSL.field(DSL.name("submitted_epoch_second"), Long.class);
    private static final Field<Integer> NANOS = DSL.field(DSL.name("submitted_nano"), Integer.class);
    private static final Field<String> QUESTION = DSL.field(DSL.name("question"), String.class);
    private static final Field<String> NAME = DSL.field(DSL.name("submitted_name"), String.class);
    private static final Field<String> EMAIL = DSL.field(DSL.name("submitted_email"), String.class);
    private static final Field<String> TELEPHONE = DSL.field(DSL.name("submitted_telephone"), String.class);
    private static final Field<String> RELEASE = DSL.field(DSL.name("semantic_registry_release_identifier"), String.class);
    private static final Field<String> MODEL = DSL.field(DSL.name("resolved_model_identifier"), String.class);
    private static final Field<Long> VERSION = DSL.field(DSL.name("resolved_model_version"), Long.class);
    private static final Field<String> OWNER = DSL.field(DSL.name("subject_owner_capability"), String.class);
    private static final Field<String> TYPE = DSL.field(DSL.name("subject_object_type"), String.class);
    private static final Field<String> SUBJECT = DSL.field(DSL.name("subject_identity"), String.class);
    private static final Field<String> REVISION = DSL.field(DSL.name("subject_revision_identity"), String.class);
    private static final Field<String> CUSTOMER = DSL.field(DSL.name("customer_context_identity"), String.class);

    private final DSLContext dsl;

    public JooqEnquirySubmissionStore(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    @Override
    public void append(EnquirySubmission submission) {
        Objects.requireNonNull(submission, "submission");
        Optional<EnquiryRevisionProvenance> subject = submission.subjectRevision();
        int inserted = dsl.insertInto(SUBMISSIONS)
                .set(MERCHANT, submission.merchantScope().merchantIdentifier())
                .set(IDENTITY, submission.enquiryIdentity())
                .set(SECONDS, submission.submittedAt().getEpochSecond())
                .set(NANOS, submission.submittedAt().getNano())
                .set(QUESTION, submission.question())
                .set(NAME, submission.contact().name().orElse(null))
                .set(EMAIL, submission.contact().email().orElse(null))
                .set(TELEPHONE, submission.contact().telephone().orElse(null))
                .set(RELEASE, submission.semanticContext().semanticRegistryReleaseIdentifier())
                .set(MODEL, submission.semanticContext().resolvedModelIdentifier())
                .set(VERSION, submission.semanticContext().resolvedModelVersion())
                .set(OWNER, subject.map(value -> value.subjectType().ownerCapabilityIdentifier()).orElse(null))
                .set(TYPE, subject.map(value -> value.subjectType().objectIdentifier()).orElse(null))
                .set(SUBJECT, subject.map(EnquiryRevisionProvenance::subjectIdentity).orElse(null))
                .set(REVISION, subject.map(EnquiryRevisionProvenance::revisionIdentity).orElse(null))
                .set(CUSTOMER, submission.customerContextIdentity().orElse(null))
                .onConflict(MERCHANT, IDENTITY).doNothing()
                .execute();
        if (inserted != 1) throw new EnquiryIdentityConflictException();
    }

    @Override
    public Optional<EnquirySubmission> submission(MerchantScope merchantScope, String enquiryIdentity) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        if (enquiryIdentity == null || enquiryIdentity.isBlank()) {
            throw new IllegalArgumentException("enquiryIdentity must not be blank");
        }
        Record row = dsl.select(MERCHANT, IDENTITY, SECONDS, NANOS, QUESTION, NAME, EMAIL, TELEPHONE, RELEASE, MODEL, VERSION, OWNER, TYPE, SUBJECT, REVISION, CUSTOMER)
                .from(SUBMISSIONS)
                .where(MERCHANT.eq(merchantScope.merchantIdentifier()).and(IDENTITY.eq(enquiryIdentity)))
                .fetchOne();
        return Optional.ofNullable(row).map(this::decode);
    }

    private EnquirySubmission decode(Record row) {
        Optional<EnquiryRevisionProvenance> subject = Optional.ofNullable(row.get(SUBJECT))
                .map(identity -> new EnquiryRevisionProvenance(
                        new OwnedOperationalObjectTypeReference(row.get(OWNER), row.get(TYPE)),
                        identity, row.get(REVISION)));
        return new EnquirySubmission(new MerchantScope(row.get(MERCHANT)), row.get(IDENTITY),
                Instant.ofEpochSecond(row.get(SECONDS), row.get(NANOS)), row.get(QUESTION),
                new EnquirySubmittedContact(Optional.ofNullable(row.get(NAME)),
                        Optional.ofNullable(row.get(EMAIL)), Optional.ofNullable(row.get(TELEPHONE))),
                new EnquirySemanticContext(row.get(RELEASE), row.get(MODEL), row.get(VERSION)),
                subject, Optional.ofNullable(row.get(CUSTOMER)));
    }
}
