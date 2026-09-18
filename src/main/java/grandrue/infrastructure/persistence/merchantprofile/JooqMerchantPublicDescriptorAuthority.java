package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import mainstreet.merchantprofile.MerchantProfileFailureCategory;
import mainstreet.merchantprofile.MerchantProfileMutationException;
import mainstreet.merchantprofile.MerchantPublicDescriptor;
import mainstreet.merchantprofile.MerchantPublicDescriptorAuthority;
import mainstreet.merchantprofile.MerchantPublicDescriptorMutationCommand;
import mainstreet.merchantprofile.MerchantPublicDescriptorRevision;
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
 * PostgreSQL/jOOQ Merchant Public Descriptor revision authority.
 *
 * <p>Every row is immutable historical fact evidence. Current state is the
 * highest committed revision for one Merchant Scope; there is no universal
 * Business Profile aggregate or global profile version.</p>
 */
public final class JooqMerchantPublicDescriptorAuthority
        implements MerchantPublicDescriptorAuthority {

    private static final Table<?> REVISION =
            DSL.table(DSL.name("merchant_public_descriptor_revision"));
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<Long> DESCRIPTOR_REVISION =
            DSL.field(DSL.name("descriptor_revision"), Long.class);
    private static final Field<String> DISPLAY_NAME =
            DSL.field(DSL.name("display_name"), String.class);
    private static final Field<String> TAGLINE =
            DSL.field(DSL.name("tagline"), String.class);
    private static final Field<String> SHORT_SUMMARY =
            DSL.field(DSL.name("short_summary"), String.class);
    private static final Field<String> APPROVED_DESCRIPTION =
            DSL.field(DSL.name("approved_description"), String.class);
    private static final Field<String> MUTATION_REQUEST_IDENTIFIER =
            DSL.field(DSL.name("mutation_request_identifier"), String.class);
    private static final Field<String> PROVENANCE_REFERENCE =
            DSL.field(DSL.name("provenance_reference"), String.class);
    private static final Field<String> PRINCIPAL_REFERENCE =
            DSL.field(DSL.name("principal_reference"), String.class);
    private static final Field<String> ORIGIN_IDENTIFIER =
            DSL.field(DSL.name("origin_identifier"), String.class);
    private static final Field<Instant> COMMITTED_AT =
            DSL.field(DSL.name("committed_at"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqMerchantPublicDescriptorAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantPublicDescriptorRevision establish(
            MerchantPublicDescriptorMutationCommand command
    ) {
        Objects.requireNonNull(command, "command");
        if (command.expectedRevision().isPresent()) {
            throw new IllegalArgumentException(
                    "Descriptor establishment must not carry an expected revision"
            );
        }
        return mutate(command, MutationKind.ESTABLISH);
    }

    @Override
    public MerchantPublicDescriptorRevision revise(
            MerchantPublicDescriptorMutationCommand command
    ) {
        Objects.requireNonNull(command, "command");
        if (command.expectedRevision().isEmpty()) {
            throw new IllegalArgumentException(
                    "Descriptor revision requires an expected current revision"
            );
        }
        return mutate(command, MutationKind.REVISE);
    }

    @Override
    public Optional<MerchantPublicDescriptorRevision> current(
            MerchantScope merchantScope
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Record row = selectFields()
                .from(REVISION)
                .where(MERCHANT_IDENTIFIER.eq(
                        merchantScope.merchantIdentifier()
                ))
                .orderBy(DESCRIPTOR_REVISION.desc())
                .limit(1)
                .fetchOne();
        return Optional.ofNullable(row).map(this::toRevision);
    }

    @Override
    public Optional<MerchantPublicDescriptorRevision> revision(
            MerchantScope merchantScope,
            long revision
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        if (revision < 1) {
            throw new IllegalArgumentException(
                    "Descriptor revision must be positive"
            );
        }
        Record row = selectFields()
                .from(REVISION)
                .where(MERCHANT_IDENTIFIER.eq(
                        merchantScope.merchantIdentifier()
                ))
                .and(DESCRIPTOR_REVISION.eq(revision))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private MerchantPublicDescriptorRevision mutate(
            MerchantPublicDescriptorMutationCommand command,
            MutationKind kind
    ) {
        MerchantPublicDescriptorRevision result =
                transactionTemplate.execute(status -> {
                    lockRequest(command.mutationRequestIdentifier());

                    Optional<MerchantPublicDescriptorRevision> committed =
                            byRequest(command.mutationRequestIdentifier());
                    if (committed.isPresent()) {
                        MerchantPublicDescriptorRevision existing =
                                committed.orElseThrow();
                        requireSameIntent(command, existing);
                        return existing;
                    }

                    MerchantScope scope = command.descriptor().merchantScope();
                    lockMerchant(scope.merchantIdentifier());
                    Optional<MerchantPublicDescriptorRevision> current =
                            current(scope);

                    long nextRevision;
                    if (kind == MutationKind.ESTABLISH) {
                        if (current.isPresent()) {
                            throw failure(
                                    MerchantProfileFailureCategory
                                            .PROFILE_REVISION_CONFLICT,
                                    "Merchant Public Descriptor is already established"
                            );
                        }
                        nextRevision = 1;
                    } else {
                        MerchantPublicDescriptorRevision authoritative =
                                current.orElseThrow(() -> failure(
                                        MerchantProfileFailureCategory
                                            .PROFILE_FACT_NOT_FOUND,
                                        "Merchant Public Descriptor is not established"
                                ));
                        long expected = command.expectedRevision()
                                .orElseThrow();
                        if (authoritative.revision() != expected) {
                            throw failure(
                                    MerchantProfileFailureCategory
                                            .PROFILE_REVISION_CONFLICT,
                                    "Merchant Public Descriptor changed concurrently"
                            );
                        }
                        nextRevision = Math.addExact(
                                authoritative.revision(),
                                1
                        );
                    }

                    insert(command, nextRevision);
                    return byRequest(command.mutationRequestIdentifier())
                            .orElseThrow(() -> new IllegalStateException(
                                    "Descriptor revision was not committed"
                            ));
                });
        return Objects.requireNonNull(
                result,
                "Descriptor mutation returned no revision"
        );
    }

    private org.jooq.SelectSelectStep<? extends Record> selectFields() {
        return dsl.select(
                MERCHANT_IDENTIFIER,
                DESCRIPTOR_REVISION,
                DISPLAY_NAME,
                TAGLINE,
                SHORT_SUMMARY,
                APPROVED_DESCRIPTION,
                MUTATION_REQUEST_IDENTIFIER,
                PROVENANCE_REFERENCE,
                PRINCIPAL_REFERENCE,
                ORIGIN_IDENTIFIER,
                COMMITTED_AT
        );
    }

    private Optional<MerchantPublicDescriptorRevision> byRequest(
            String requestIdentifier
    ) {
        Record row = selectFields()
                .from(REVISION)
                .where(MUTATION_REQUEST_IDENTIFIER.eq(requestIdentifier))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private void insert(
            MerchantPublicDescriptorMutationCommand command,
            long revision
    ) {
        MerchantPublicDescriptor descriptor = command.descriptor();
        dsl.insertInto(REVISION)
                .columns(
                        MERCHANT_IDENTIFIER,
                        DESCRIPTOR_REVISION,
                        DISPLAY_NAME,
                        TAGLINE,
                        SHORT_SUMMARY,
                        APPROVED_DESCRIPTION,
                        MUTATION_REQUEST_IDENTIFIER,
                        PROVENANCE_REFERENCE,
                        PRINCIPAL_REFERENCE,
                        ORIGIN_IDENTIFIER,
                        COMMITTED_AT
                )
                .values(
                        descriptor.merchantScope().merchantIdentifier(),
                        revision,
                        descriptor.displayName(),
                        descriptor.tagline().orElse(null),
                        descriptor.shortSummary().orElse(null),
                        descriptor.approvedDescription().orElse(null),
                        command.mutationRequestIdentifier(),
                        command.provenanceReference(),
                        command.principalReference(),
                        command.originIdentifier().orElse(null),
                        command.committedAt()
                )
                .execute();
    }

    private MerchantPublicDescriptorRevision toRevision(Record row) {
        MerchantScope scope = new MerchantScope(
                row.get(MERCHANT_IDENTIFIER)
        );
        MerchantPublicDescriptor descriptor = new MerchantPublicDescriptor(
                scope,
                row.get(DISPLAY_NAME),
                Optional.ofNullable(row.get(TAGLINE)),
                Optional.ofNullable(row.get(SHORT_SUMMARY)),
                Optional.ofNullable(row.get(APPROVED_DESCRIPTION))
        );
        return new MerchantPublicDescriptorRevision(
                descriptor,
                row.get(DESCRIPTOR_REVISION),
                row.get(MUTATION_REQUEST_IDENTIFIER),
                row.get(PROVENANCE_REFERENCE),
                row.get(PRINCIPAL_REFERENCE),
                Optional.ofNullable(row.get(ORIGIN_IDENTIFIER)),
                row.get(COMMITTED_AT)
        );
    }

    private static void requireSameIntent(
            MerchantPublicDescriptorMutationCommand command,
            MerchantPublicDescriptorRevision committed
    ) {
        Optional<Long> committedExpected = committed.revision() == 1
                ? Optional.empty()
                : Optional.of(committed.revision() - 1);
        boolean same = command.descriptor().equals(committed.descriptor())
                && command.expectedRevision().equals(committedExpected)
                && command.mutationRequestIdentifier().equals(
                        committed.mutationRequestIdentifier()
                )
                && command.provenanceReference().equals(
                        committed.provenanceReference()
                )
                && command.principalReference().equals(
                        committed.principalReference()
                )
                && command.originIdentifier().equals(
                        committed.originIdentifier()
                )
                && command.committedAt().equals(committed.committedAt());
        if (!same) {
            throw failure(
                    MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                    "Descriptor request identity is bound to different intent"
            );
        }
    }

    private void lockRequest(String requestIdentifier) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 51))",
                "merchant-public-descriptor-request|" + requestIdentifier
        );
    }

    private void lockMerchant(String merchantIdentifier) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 52))",
                "merchant-public-descriptor|" + merchantIdentifier
        );
    }

    private static MerchantProfileMutationException failure(
            MerchantProfileFailureCategory category,
            String message
    ) {
        return new MerchantProfileMutationException(category, message);
    }

    private enum MutationKind {
        ESTABLISH,
        REVISE
    }
}
