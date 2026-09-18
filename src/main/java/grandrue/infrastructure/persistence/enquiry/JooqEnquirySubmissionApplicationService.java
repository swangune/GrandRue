package grandrue.infrastructure.persistence.enquiry;

import grandrue.application.ApplicationRequestIdentity;
import grandrue.application.MerchantScope;
import grandrue.enquiry.EnquiryApplicationRequestConflictException;
import grandrue.enquiry.EnquirySubmission;
import grandrue.enquiry.EnquirySubmissionApplicationService;
import grandrue.enquiry.EnquirySubmissionIntent;
import grandrue.enquiry.EnquirySubmissionPreparation;
import grandrue.enquiry.EnquirySubmissionStore;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

/**
 * PostgreSQL transaction/retry orchestration. Request identity is global within this bounded
 * application operation, following ApplicationRequestIdentity. Merchant remains part of intent.
 * A transaction-scoped request lock serializes retries; the full key, not its hash, identifies
 * the durable receipt. Hash collisions only serialize unrelated work.
 * The DSLContext must participate in the same Spring-managed transaction as the store.
 */
public final class JooqEnquirySubmissionApplicationService implements EnquirySubmissionApplicationService {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;
    private final EnquirySubmissionStore submissions;

    public JooqEnquirySubmissionApplicationService(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(Objects.requireNonNull(transactionManager, "transactionManager"));
        this.submissions = new JooqEnquirySubmissionStore(dsl);
    }

    @Override
    public EnquirySubmission submit(
            ApplicationRequestIdentity requestIdentity,
            EnquirySubmissionIntent intent,
            EnquirySubmissionPreparation preparation
    ) {
        Objects.requireNonNull(requestIdentity, "requestIdentity");
        Objects.requireNonNull(intent, "intent");
        Objects.requireNonNull(preparation, "preparation");
        EnquirySubmission result = transactions.execute(status -> {
            dsl.fetch("select pg_advisory_xact_lock(hashtextextended(cast(? as text), 7302))",
                    "enquiry-submission-application|" + requestIdentity.value());
            Record receipt = dsl.fetchOne("select merchant_identifier, enquiry_identity "
                    + "from enquiry_submission_application_request where application_request_identity = ?",
                    requestIdentity.value());
            if (receipt != null) {
                MerchantScope storedScope = new MerchantScope(receipt.get("merchant_identifier", String.class));
                if (!storedScope.equals(intent.merchantScope())) {
                    throw new EnquiryApplicationRequestConflictException();
                }
                EnquirySubmission original = submissions.submission(storedScope,
                        receipt.get("enquiry_identity", String.class)).orElseThrow(
                        () -> new IllegalStateException("Committed Enquiry request has no submission"));
                if (!EnquirySubmissionIntent.from(original).equals(intent)) {
                    throw new EnquiryApplicationRequestConflictException();
                }
                return original;
            }

            EnquirySubmission prepared = Objects.requireNonNull(preparation.prepare(intent), "prepared submission");
            if (!EnquirySubmissionIntent.from(prepared).equals(intent)) {
                throw new IllegalArgumentException("Prepared Enquiry must preserve supplied intent");
            }
            submissions.append(prepared);
            dsl.execute("insert into enquiry_submission_application_request "
                    + "(application_request_identity, merchant_identifier, enquiry_identity) values (?, ?, ?)",
                    requestIdentity.value(), prepared.merchantScope().merchantIdentifier(), prepared.enquiryIdentity());
            return prepared;
        });
        return Objects.requireNonNull(result, "Enquiry transaction returned no result");
    }
}
