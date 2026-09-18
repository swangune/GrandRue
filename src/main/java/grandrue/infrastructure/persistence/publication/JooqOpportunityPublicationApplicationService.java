package grandrue.infrastructure.persistence.publication;

import grandrue.application.ApplicationRequestIdentity;
import grandrue.application.MerchantScope;
import grandrue.publication.OpportunityPublicationApplicationService;
import grandrue.publication.OpportunityPublicationMaterialRevision;
import grandrue.publication.OpportunityPublicationState;
import grandrue.publication.OpportunityPublicationStateAuthority;
import grandrue.publication.PublicationApplicationRequestConflictException;
import grandrue.publication.PublicationLifecycle;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * PostgreSQL application orchestration for Opportunity Publication mutation and logical retry
 * reconciliation.
 *
 * <p>The outer REQUIRED transaction composes durable application-request evidence with the P2B
 * Publication state/material/history authority. A retry is reconciled before current state is
 * re-read, so it returns the original committed result rather than re-executing against later
 * authoritative state.</p>
 */
public final class JooqOpportunityPublicationApplicationService
        implements OpportunityPublicationApplicationService {

    private final DSLContext dsl;
    private final TransactionTemplate transactions;
    private final OpportunityPublicationStateAuthority publicationAuthority;

    public JooqOpportunityPublicationApplicationService(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        PlatformTransactionManager requiredTransactionManager = Objects.requireNonNull(
                transactionManager,
                "transactionManager"
        );
        this.transactions = new TransactionTemplate(requiredTransactionManager);
        this.publicationAuthority = new JooqOpportunityPublicationStateAuthority(
                dsl,
                requiredTransactionManager
        );
    }

    @Override
    public OpportunityPublicationState establishDraft(
            ApplicationRequestIdentity requestIdentity,
            OpportunityPublicationMaterialRevision initialRevision
    ) {
        Objects.requireNonNull(initialRevision, "initialRevision");
        Intent intent = new Intent(
                OperationKind.ESTABLISH_DRAFT,
                initialRevision.merchantScope(),
                initialRevision.opportunityIdentity(),
                Optional.empty(),
                Optional.of(initialRevision)
        );
        return execute(requestIdentity, intent, () -> publicationAuthority.establish(
                OpportunityPublicationState.draft(
                        initialRevision.merchantScope(),
                        initialRevision.opportunityIdentity(),
                        initialRevision.revisionIdentity()
                ),
                initialRevision
        ));
    }

    @Override
    public OpportunityPublicationState revise(
            ApplicationRequestIdentity requestIdentity,
            String expectedCurrentRevisionIdentity,
            OpportunityPublicationMaterialRevision newRevision
    ) {
        requireText(expectedCurrentRevisionIdentity, "expectedCurrentRevisionIdentity");
        Objects.requireNonNull(newRevision, "newRevision");
        Intent intent = new Intent(
                OperationKind.REVISE,
                newRevision.merchantScope(),
                newRevision.opportunityIdentity(),
                Optional.of(expectedCurrentRevisionIdentity),
                Optional.of(newRevision)
        );
        return execute(requestIdentity, intent, () -> {
            OpportunityPublicationState current = requireCurrent(
                    newRevision.merchantScope(),
                    newRevision.opportunityIdentity()
            );
            OpportunityPublicationState next = current.revise(
                    expectedCurrentRevisionIdentity,
                    newRevision.revisionIdentity()
            );
            return publicationAuthority.compareAndSet(
                    current,
                    next,
                    Optional.of(newRevision)
            );
        });
    }

    @Override
    public OpportunityPublicationState publish(
            ApplicationRequestIdentity requestIdentity,
            MerchantScope merchantScope,
            String opportunityIdentity,
            String expectedCurrentRevisionIdentity
    ) {
        return lifecycleMutation(
                requestIdentity,
                OperationKind.PUBLISH,
                merchantScope,
                opportunityIdentity,
                expectedCurrentRevisionIdentity
        );
    }

    @Override
    public OpportunityPublicationState withdraw(
            ApplicationRequestIdentity requestIdentity,
            MerchantScope merchantScope,
            String opportunityIdentity,
            String expectedCurrentRevisionIdentity
    ) {
        return lifecycleMutation(
                requestIdentity,
                OperationKind.WITHDRAW,
                merchantScope,
                opportunityIdentity,
                expectedCurrentRevisionIdentity
        );
    }

    @Override
    public OpportunityPublicationState republish(
            ApplicationRequestIdentity requestIdentity,
            MerchantScope merchantScope,
            String opportunityIdentity,
            String expectedCurrentRevisionIdentity
    ) {
        return lifecycleMutation(
                requestIdentity,
                OperationKind.REPUBLISH,
                merchantScope,
                opportunityIdentity,
                expectedCurrentRevisionIdentity
        );
    }

    private OpportunityPublicationState lifecycleMutation(
            ApplicationRequestIdentity requestIdentity,
            OperationKind operationKind,
            MerchantScope merchantScope,
            String opportunityIdentity,
            String expectedCurrentRevisionIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        requireText(expectedCurrentRevisionIdentity, "expectedCurrentRevisionIdentity");
        Intent intent = new Intent(
                operationKind,
                merchantScope,
                opportunityIdentity,
                Optional.of(expectedCurrentRevisionIdentity),
                Optional.empty()
        );
        return execute(requestIdentity, intent, () -> {
            OpportunityPublicationState current = requireCurrent(
                    merchantScope,
                    opportunityIdentity
            );
            OpportunityPublicationState next = switch (operationKind) {
                case PUBLISH -> current.publish(expectedCurrentRevisionIdentity);
                case WITHDRAW -> current.withdraw(expectedCurrentRevisionIdentity);
                case REPUBLISH -> current.republish(expectedCurrentRevisionIdentity);
                case ESTABLISH_DRAFT, REVISE -> throw new IllegalStateException(
                        "Material operation cannot use lifecycle mutation path"
                );
            };
            return publicationAuthority.compareAndSet(
                    current,
                    next,
                    Optional.empty()
            );
        });
    }

    private OpportunityPublicationState execute(
            ApplicationRequestIdentity requestIdentity,
            Intent intent,
            Supplier<OpportunityPublicationState> mutation
    ) {
        Objects.requireNonNull(requestIdentity, "requestIdentity");
        Objects.requireNonNull(intent, "intent");
        Objects.requireNonNull(mutation, "mutation");

        OpportunityPublicationState result = transactions.execute(status -> {
            lockRequest(requestIdentity);
            Optional<CommittedRequest> committed = byRequest(requestIdentity);
            if (committed.isPresent()) {
                CommittedRequest existing = committed.orElseThrow();
                requireSameIntent(requestIdentity, intent, existing);
                return existing.result();
            }

            OpportunityPublicationState committedState = mutation.get();
            insertRequest(requestIdentity, intent, committedState);
            return committedState;
        });
        return Objects.requireNonNull(
                result,
                "Publication application operation returned no result"
        );
    }

    private OpportunityPublicationState requireCurrent(
            MerchantScope merchantScope,
            String opportunityIdentity
    ) {
        return publicationAuthority.current(merchantScope, opportunityIdentity)
                .orElseThrow(() -> new IllegalStateException(
                        "Opportunity Publication state is not established"
                ));
    }

    private Optional<CommittedRequest> byRequest(
            ApplicationRequestIdentity requestIdentity
    ) {
        Record row = dsl.fetchOne(
                "select * from opportunity_publication_application_request "
                        + "where application_request_identity = ?",
                requestIdentity.value()
        );
        if (row == null) {
            return Optional.empty();
        }
        MerchantScope merchantScope = new MerchantScope(
                row.get("merchant_identifier", String.class)
        );
        String opportunityIdentity = row.get(
                "opportunity_identity",
                String.class
        );
        OpportunityPublicationState result = new OpportunityPublicationState(
                merchantScope,
                opportunityIdentity,
                row.get("result_current_revision_identity", String.class),
                PublicationLifecycle.valueOf(row.get("result_lifecycle", String.class)),
                Optional.ofNullable(row.get(
                        "result_published_revision_identity",
                        String.class
                ))
        );
        return Optional.of(new CommittedRequest(
                OperationKind.valueOf(row.get("operation_kind", String.class)),
                merchantScope,
                opportunityIdentity,
                Optional.ofNullable(row.get(
                        "expected_revision_identity",
                        String.class
                )),
                Optional.ofNullable(row.get(
                        "material_revision_identity",
                        String.class
                )),
                result
        ));
    }

    private void requireSameIntent(
            ApplicationRequestIdentity requestIdentity,
            Intent intent,
            CommittedRequest committed
    ) {
        boolean sameShape = committed.operationKind() == intent.operationKind()
                && committed.merchantScope().equals(intent.merchantScope())
                && committed.opportunityIdentity().equals(intent.opportunityIdentity())
                && committed.expectedRevisionIdentity().equals(
                        intent.expectedRevisionIdentity()
                )
                && committed.materialRevisionIdentity().equals(
                        intent.materialRevision().map(
                                OpportunityPublicationMaterialRevision::revisionIdentity
                        )
                );
        if (!sameShape) {
            throw new PublicationApplicationRequestConflictException(
                    requestIdentity.value()
            );
        }
        if (intent.materialRevision().isPresent()) {
            OpportunityPublicationMaterialRevision incoming =
                    intent.materialRevision().orElseThrow();
            OpportunityPublicationMaterialRevision stored = publicationAuthority.revision(
                    intent.merchantScope(),
                    intent.opportunityIdentity(),
                    incoming.revisionIdentity()
            ).orElseThrow(() -> new IllegalStateException(
                    "Committed Publication request references missing material revision"
            ));
            if (!stored.equals(incoming)) {
                throw new PublicationApplicationRequestConflictException(
                        requestIdentity.value()
                );
            }
        }
    }

    private void insertRequest(
            ApplicationRequestIdentity requestIdentity,
            Intent intent,
            OpportunityPublicationState result
    ) {
        dsl.execute(
                "insert into opportunity_publication_application_request "
                        + "(application_request_identity, operation_kind, merchant_identifier, "
                        + "opportunity_identity, expected_revision_identity, "
                        + "material_revision_identity, result_current_revision_identity, "
                        + "result_lifecycle, result_published_revision_identity) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                requestIdentity.value(),
                intent.operationKind().name(),
                intent.merchantScope().merchantIdentifier(),
                intent.opportunityIdentity(),
                intent.expectedRevisionIdentity().orElse(null),
                intent.materialRevision()
                        .map(OpportunityPublicationMaterialRevision::revisionIdentity)
                        .orElse(null),
                result.currentRevisionIdentity(),
                result.lifecycle().name(),
                result.publishedRevisionIdentity().orElse(null)
        );
    }

    private void lockRequest(ApplicationRequestIdentity requestIdentity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 7301))",
                "opportunity-publication-application|" + requestIdentity.value()
        );
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }

    private enum OperationKind {
        ESTABLISH_DRAFT,
        REVISE,
        PUBLISH,
        WITHDRAW,
        REPUBLISH
    }

    private record Intent(
            OperationKind operationKind,
            MerchantScope merchantScope,
            String opportunityIdentity,
            Optional<String> expectedRevisionIdentity,
            Optional<OpportunityPublicationMaterialRevision> materialRevision
    ) {
        private Intent {
            Objects.requireNonNull(operationKind, "operationKind");
            Objects.requireNonNull(merchantScope, "merchantScope");
            requireText(opportunityIdentity, "opportunityIdentity");
            Objects.requireNonNull(expectedRevisionIdentity, "expectedRevisionIdentity");
            expectedRevisionIdentity.ifPresent(value -> requireText(
                    value,
                    "expectedRevisionIdentity"
            ));
            Objects.requireNonNull(materialRevision, "materialRevision");
        }
    }

    private record CommittedRequest(
            OperationKind operationKind,
            MerchantScope merchantScope,
            String opportunityIdentity,
            Optional<String> expectedRevisionIdentity,
            Optional<String> materialRevisionIdentity,
            OpportunityPublicationState result
    ) {
    }
}
