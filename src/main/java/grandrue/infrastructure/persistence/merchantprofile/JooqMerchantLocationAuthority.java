package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import mainstreet.merchantprofile.AcceptedLocationCoordinates;
import mainstreet.merchantprofile.CorrectMerchantLocationCommand;
import mainstreet.merchantprofile.CreateMerchantLocationCommand;
import mainstreet.merchantprofile.LocationCoordinateSourceKind;
import mainstreet.merchantprofile.MerchantLocationAuthority;
import mainstreet.merchantprofile.MerchantLocationLifecycle;
import mainstreet.merchantprofile.MerchantLocationRevision;
import mainstreet.merchantprofile.MerchantProfileFailureCategory;
import mainstreet.merchantprofile.MerchantProfileMutationException;
import mainstreet.merchantprofile.PostalAddressEvidence;
import mainstreet.merchantprofile.PostalAddressInput;
import mainstreet.merchantprofile.PostalAddressV1;
import mainstreet.merchantprofile.RetireMerchantLocationCommand;
import mainstreet.runtime.TrustedExecutionContext;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** PostgreSQL authority for immutable merchant-scoped Location revisions. */
public final class JooqMerchantLocationAuthority
        implements MerchantLocationAuthority {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantLocationAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantLocationRevision create(
            CreateMerchantLocationCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.CREATE,
                command.merchantScope(),
                command.locationIdentity(),
                Optional.empty(),
                command.publicLabel(),
                Optional.of(PostalAddressEvidence.accept(
                        command.postalAddress()
                )),
                command.acceptedCoordinates(),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantLocationRevision correct(
            CorrectMerchantLocationCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.CORRECT,
                command.merchantScope(),
                command.locationIdentity(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                command.publicLabel(),
                Optional.of(PostalAddressEvidence.accept(
                        command.postalAddress()
                )),
                command.acceptedCoordinates(),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantLocationRevision retire(
            RetireMerchantLocationCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.RETIRE,
                command.merchantScope(),
                command.locationIdentity(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public Optional<MerchantLocationRevision> current(
            MerchantScope merchantScope,
            String locationIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(locationIdentity, "locationIdentity");
        Record pointer = dsl.fetchOne(
                "select revision_identifier from current_merchant_location "
                        + "where merchant_identifier = ? and location_identifier = ?",
                merchantScope.merchantIdentifier(),
                locationIdentity
        );
        return pointer == null
                ? Optional.empty()
                : revision(pointer.get("revision_identifier", String.class));
    }

    @Override
    public Optional<MerchantLocationRevision> revision(String revisionIdentity) {
        requireIdentifier(revisionIdentity, "revisionIdentity");
        Record row = dsl.fetchOne(
                "select * from merchant_location_revision "
                        + "where revision_identifier = ?",
                revisionIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private MerchantLocationRevision mutate(MutationIntent intent) {
        MerchantLocationRevision result = transactions.execute(status -> {
            lock(
                    "merchant-location-request|" + intent.requestIdentity(),
                    513
            );
            Optional<MerchantLocationRevision> replay = byRequest(
                    intent.requestIdentity()
            );
            if (replay.isPresent()) {
                return requireSameIntent(intent, replay.orElseThrow());
            }

            String merchant = intent.merchantScope().merchantIdentifier();
            lock(merchant, 76);
            requireOpenUnsuspendedAccount(intent.merchantScope());
            String controllerRelationship = requireCurrentController(
                    intent.merchantScope(),
                    intent.actorIdentity()
            );
            lock(
                    merchant + "|" + intent.locationIdentity(),
                    512
            );

            Record pointer = currentPointerForUpdate(intent);
            Optional<MerchantLocationRevision> current = pointer == null
                    ? Optional.empty()
                    : revision(pointer.get(
                            "revision_identifier",
                            String.class
                    ));

            RevisionMaterial material = resolveMaterial(intent, current);
            String revisionIdentity = "merchant-location-revision-"
                    + UUID.randomUUID();
            try {
                insertRevision(
                        revisionIdentity,
                        intent,
                        material,
                        controllerRelationship
                );
                insertAddressLines(revisionIdentity, material.addressEvidence());
                advancePointer(
                        pointer,
                        revisionIdentity,
                        intent,
                        material
                );
            } catch (DataAccessException failure) {
                throw new MerchantProfileMutationException(
                        MerchantProfileFailureCategory
                                .TECHNICAL_FAILURE_BEFORE_COMMIT,
                        "Merchant Location revision could not commit",
                        failure
                );
            }
            return revision(revisionIdentity).orElseThrow(() ->
                    new IllegalStateException(
                            "Committed Merchant Location revision is missing"
                    )
            );
        });
        return Objects.requireNonNull(
                result,
                "Merchant Location mutation returned no revision"
        );
    }

    private RevisionMaterial resolveMaterial(
            MutationIntent intent,
            Optional<MerchantLocationRevision> current
    ) {
        if (intent.operation() == OperationKind.CREATE) {
            if (current.isPresent()) {
                throw failure(
                        MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                        "Merchant Location identity is already established"
                );
            }
            return new RevisionMaterial(
                    1L,
                    Optional.empty(),
                    MerchantLocationLifecycle.ACTIVE,
                    intent.publicLabel(),
                    intent.addressEvidence().orElseThrow(),
                    intent.coordinates()
            );
        }

        MerchantLocationRevision authoritative = current.orElseThrow(() ->
                failure(
                        MerchantProfileFailureCategory.PROFILE_FACT_NOT_FOUND,
                        "Merchant Location is not established"
                )
        );
        if (authoritative.lifecycle() == MerchantLocationLifecycle.RETIRED) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                    "Merchant Location identity is retired"
            );
        }
        if (!intent.expectedRevision().orElseThrow().equals(
                authoritative.revisionIdentity()
        )) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Merchant Location current revision changed"
            );
        }
        long next = Math.addExact(authoritative.revisionNumber(), 1L);
        if (intent.operation() == OperationKind.RETIRE) {
            return new RevisionMaterial(
                    next,
                    Optional.of(authoritative.revisionIdentity()),
                    MerchantLocationLifecycle.RETIRED,
                    authoritative.publicLabel(),
                    authoritative.addressEvidence(),
                    authoritative.acceptedCoordinates()
            );
        }
        return new RevisionMaterial(
                next,
                Optional.of(authoritative.revisionIdentity()),
                MerchantLocationLifecycle.ACTIVE,
                intent.publicLabel(),
                intent.addressEvidence().orElseThrow(),
                intent.coordinates()
        );
    }

    private Optional<MerchantLocationRevision> byRequest(String request) {
        Record row = dsl.fetchOne(
                "select * from merchant_location_revision "
                        + "where logical_request_identifier = ?",
                request
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private Record currentPointerForUpdate(MutationIntent intent) {
        return dsl.fetchOne(
                "select * from current_merchant_location "
                        + "where merchant_identifier = ? and location_identifier = ? "
                        + "for update",
                intent.merchantScope().merchantIdentifier(),
                intent.locationIdentity()
        );
    }

    private void requireOpenUnsuspendedAccount(MerchantScope merchantScope) {
        Record account = dsl.fetchOne(
                "select lifecycle from merchant_account "
                        + "where merchant_identifier = ? for share",
                merchantScope.merchantIdentifier()
        );
        if (account == null
                || !"OPEN".equals(account.get("lifecycle", String.class))
                || dsl.fetchOne(
                        "select 1 from merchant_account_suspension "
                                + "where merchant_identifier = ? "
                                + "and released_at is null limit 1",
                        merchantScope.merchantIdentifier()
                ) != null) {
            throw failure(
                    MerchantProfileFailureCategory
                            .MERCHANT_ACCOUNT_OPERATION_RESTRICTED,
                    "Merchant Account does not permit ordinary mutation"
            );
        }
    }

    private String requireCurrentController(
            MerchantScope merchantScope,
            String actorIdentity
    ) {
        Record controller = dsl.fetchOne(
                "select controller_relationship_identifier, identity_identifier "
                        + "from merchant_controller_relationship "
                        + "where merchant_identifier = ? "
                        + "and lifecycle = 'ACTIVE' for share",
                merchantScope.merchantIdentifier()
        );
        if (controller == null
                || !actorIdentity.equals(controller.get(
                        "identity_identifier",
                        String.class
                ))) {
            throw failure(
                    MerchantProfileFailureCategory.AUTHORISATION_REJECTION,
                    "Current Merchant Controller authority is required"
            );
        }
        return controller.get(
                "controller_relationship_identifier",
                String.class
        );
    }

    private void insertRevision(
            String revisionIdentity,
            MutationIntent intent,
            RevisionMaterial material,
            String controllerRelationship
    ) {
        PostalAddressInput original = material.addressEvidence().originalInput();
        PostalAddressV1 normalized = material.addressEvidence().normalizedAddress();
        AcceptedLocationCoordinates coordinates = material.coordinates()
                .orElse(null);
        dsl.execute(
                "insert into merchant_location_revision ("
                        + "revision_identifier, merchant_identifier, location_identifier, "
                        + "revision_number, predecessor_revision_identifier, operation_kind, "
                        + "lifecycle, public_label, address_schema_identifier, "
                        + "normalization_profile_identifier, country_registry_identifier, "
                        + "original_country_code, normalized_country_code, "
                        + "original_dependent_locality, normalized_dependent_locality, "
                        + "original_locality, normalized_locality, "
                        + "original_administrative_area, normalized_administrative_area, "
                        + "original_postal_code, normalized_postal_code, "
                        + "original_sorting_code, normalized_sorting_code, latitude, "
                        + "longitude, coordinate_source_kind, coordinate_source_reference, "
                        + "coordinates_accepted_by_actor_identifier, coordinates_accepted_at, "
                        + "logical_request_identifier, provenance_reference, "
                        + "acting_principal_identifier, controller_relationship_identifier, "
                        + "committed_at) values ("
                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?, ?, ?, cast(? as timestamptz), ?, ?, ?, ?, "
                        + "cast(? as timestamptz))",
                revisionIdentity,
                intent.merchantScope().merchantIdentifier(),
                intent.locationIdentity(),
                material.revisionNumber(),
                material.predecessorRevision().orElse(null),
                intent.operation().name(),
                material.lifecycle().name(),
                material.publicLabel().orElse(null),
                normalized.schemaIdentity(),
                normalized.normalizationProfileIdentity(),
                normalized.countryRegistryIdentity(),
                original.countryCode(),
                normalized.countryCode(),
                original.dependentLocality().orElse(null),
                normalized.dependentLocality().orElse(null),
                original.locality().orElse(null),
                normalized.locality().orElse(null),
                original.administrativeArea().orElse(null),
                normalized.administrativeArea().orElse(null),
                original.postalCode().orElse(null),
                normalized.postalCode().orElse(null),
                original.sortingCode().orElse(null),
                normalized.sortingCode().orElse(null),
                coordinates == null ? null : coordinates.latitude(),
                coordinates == null ? null : coordinates.longitude(),
                coordinates == null ? null : coordinates.sourceKind().name(),
                coordinates == null
                        ? null
                        : coordinates.sourceReference().orElse(null),
                coordinates == null
                        ? null
                        : coordinates.acceptedByActorIdentity(),
                coordinates == null ? null : coordinates.acceptedAt().toString(),
                intent.requestIdentity(),
                intent.provenanceReference(),
                intent.actorIdentity(),
                controllerRelationship,
                intent.committedAt().toString()
        );
    }

    private void insertAddressLines(
            String revisionIdentity,
            PostalAddressEvidence evidence
    ) {
        insertLines(
                "merchant_location_original_address_line",
                revisionIdentity,
                evidence.originalInput().addressLines()
        );
        insertLines(
                "merchant_location_normalized_address_line",
                revisionIdentity,
                evidence.normalizedAddress().addressLines()
        );
    }

    private void insertLines(
            String table,
            String revisionIdentity,
            List<String> lines
    ) {
        for (int index = 0; index < lines.size(); index++) {
            dsl.execute(
                    "insert into " + table
                            + " (revision_identifier, line_sequence, line_value) "
                            + "values (?, ?, ?)",
                    revisionIdentity,
                    index,
                    lines.get(index)
            );
        }
    }

    private void advancePointer(
            Record pointer,
            String revisionIdentity,
            MutationIntent intent,
            RevisionMaterial material
    ) {
        if (pointer == null) {
            dsl.execute(
                    "insert into current_merchant_location ("
                            + "current_pointer_identifier, merchant_identifier, "
                            + "location_identifier, revision_identifier, revision_number, "
                            + "lifecycle) values (?, ?, ?, ?, ?, ?)",
                    "merchant-location-current-" + UUID.randomUUID(),
                    intent.merchantScope().merchantIdentifier(),
                    intent.locationIdentity(),
                    revisionIdentity,
                    material.revisionNumber(),
                    material.lifecycle().name()
            );
            return;
        }
        int updated = dsl.execute(
                "update current_merchant_location set revision_identifier = ?, "
                        + "revision_number = ?, lifecycle = ? "
                        + "where current_pointer_identifier = ?",
                revisionIdentity,
                material.revisionNumber(),
                material.lifecycle().name(),
                pointer.get("current_pointer_identifier", String.class)
        );
        if (updated != 1) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Merchant Location current pointer changed"
            );
        }
    }

    private MerchantLocationRevision toRevision(Record row) {
        String revisionIdentity = row.get(
                "revision_identifier",
                String.class
        );
        PostalAddressInput original = new PostalAddressInput(
                row.get("original_country_code", String.class),
                addressLines(
                        "merchant_location_original_address_line",
                        revisionIdentity
                ),
                optional(row, "original_dependent_locality"),
                optional(row, "original_locality"),
                optional(row, "original_administrative_area"),
                optional(row, "original_postal_code"),
                optional(row, "original_sorting_code")
        );
        PostalAddressV1 normalized = new PostalAddressV1(
                row.get("address_schema_identifier", String.class),
                row.get("normalization_profile_identifier", String.class),
                row.get("country_registry_identifier", String.class),
                row.get("normalized_country_code", String.class),
                addressLines(
                        "merchant_location_normalized_address_line",
                        revisionIdentity
                ),
                optional(row, "normalized_dependent_locality"),
                optional(row, "normalized_locality"),
                optional(row, "normalized_administrative_area"),
                optional(row, "normalized_postal_code"),
                optional(row, "normalized_sorting_code")
        );
        return new MerchantLocationRevision(
                revisionIdentity,
                new MerchantScope(row.get("merchant_identifier", String.class)),
                row.get("location_identifier", String.class),
                row.get("revision_number", Long.class),
                optional(row, "predecessor_revision_identifier"),
                MerchantLocationLifecycle.valueOf(
                        row.get("lifecycle", String.class)
                ),
                optional(row, "public_label"),
                new PostalAddressEvidence(original, normalized),
                coordinates(row),
                row.get("logical_request_identifier", String.class),
                row.get("provenance_reference", String.class),
                row.get("acting_principal_identifier", String.class),
                row.get("controller_relationship_identifier", String.class),
                row.get("committed_at", Instant.class)
        );
    }

    private Optional<AcceptedLocationCoordinates> coordinates(Record row) {
        BigDecimal latitude = row.get("latitude", BigDecimal.class);
        if (latitude == null) {
            return Optional.empty();
        }
        return Optional.of(new AcceptedLocationCoordinates(
                latitude,
                row.get("longitude", BigDecimal.class),
                LocationCoordinateSourceKind.valueOf(
                        row.get("coordinate_source_kind", String.class)
                ),
                optional(row, "coordinate_source_reference"),
                row.get(
                        "coordinates_accepted_by_actor_identifier",
                        String.class
                ),
                row.get("coordinates_accepted_at", Instant.class)
        ));
    }

    private List<String> addressLines(String table, String revisionIdentity) {
        List<String> result = new ArrayList<>();
        dsl.fetch(
                "select line_value from " + table
                        + " where revision_identifier = ? order by line_sequence",
                revisionIdentity
        ).forEach(row -> result.add(row.get("line_value", String.class)));
        return List.copyOf(result);
    }

    private static Optional<String> optional(Record row, String field) {
        return Optional.ofNullable(row.get(field, String.class));
    }

    private static MerchantLocationRevision requireSameIntent(
            MutationIntent intent,
            MerchantLocationRevision replay
    ) {
        OperationKind committedOperation = replay.revisionNumber() == 1
                ? OperationKind.CREATE
                : replay.lifecycle() == MerchantLocationLifecycle.RETIRED
                        ? OperationKind.RETIRE
                        : OperationKind.CORRECT;
        boolean same = committedOperation == intent.operation()
                && replay.merchantScope().equals(intent.merchantScope())
                && replay.locationIdentity().equals(intent.locationIdentity())
                && replay.predecessorRevisionIdentity().equals(
                        intent.expectedRevision()
                )
                && replay.logicalRequestIdentity().equals(
                        intent.requestIdentity()
                )
                && replay.provenanceReference().equals(
                        intent.provenanceReference()
                )
                && replay.actingPrincipalIdentity().equals(
                        intent.actorIdentity()
                )
                && replay.committedAt().equals(intent.committedAt());
        if (intent.operation() != OperationKind.RETIRE) {
            same = same
                    && replay.publicLabel().equals(intent.publicLabel())
                    && Optional.of(replay.addressEvidence()).equals(
                            intent.addressEvidence()
                    )
                    && replay.acceptedCoordinates().equals(intent.coordinates());
        }
        if (!same) {
            throw failure(
                    MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                    "Merchant Location request identity has different intent"
            );
        }
        return replay;
    }

    private static void requireAuthenticated(
            MerchantScope merchantScope,
            String actorIdentity,
            TrustedExecutionContext context
    ) {
        if (context == null
                || context.authentication().isEmpty()
                || !context.merchantScope().equals(merchantScope)
                || !context.principal().identifier().equals(actorIdentity)
                || !context.authentication().orElseThrow()
                        .identityIdentifier().equals(actorIdentity)) {
            throw failure(
                    MerchantProfileFailureCategory.AUTHENTICATION_REQUIRED,
                    "Matching authenticated trusted principal is required"
            );
        }
    }

    private void lock(String key, int namespace) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), ?))",
                key,
                namespace
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private static MerchantProfileMutationException failure(
            MerchantProfileFailureCategory category,
            String message
    ) {
        return new MerchantProfileMutationException(category, message);
    }

    private enum OperationKind {
        CREATE,
        CORRECT,
        RETIRE
    }

    private record MutationIntent(
            OperationKind operation,
            MerchantScope merchantScope,
            String locationIdentity,
            Optional<String> expectedRevision,
            Optional<String> publicLabel,
            Optional<PostalAddressEvidence> addressEvidence,
            Optional<AcceptedLocationCoordinates> coordinates,
            String requestIdentity,
            String provenanceReference,
            String actorIdentity,
            Instant committedAt
    ) {
    }

    private record RevisionMaterial(
            long revisionNumber,
            Optional<String> predecessorRevision,
            MerchantLocationLifecycle lifecycle,
            Optional<String> publicLabel,
            PostalAddressEvidence addressEvidence,
            Optional<AcceptedLocationCoordinates> coordinates
    ) {
    }
}
