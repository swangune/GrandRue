package mainstreet.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import grandrue.fulfilment.FulfilmentBindingSetRevisionReference;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.configuration.ChangedConfigurationRevision;
import mainstreet.semantic.configuration.ConfigurationChangeAuthority;
import mainstreet.semantic.configuration.ConfigurationChangeAuthorizationAuthority;
import mainstreet.semantic.configuration.ConfigurationChangeSet;
import mainstreet.semantic.configuration.ConfigurationRevisionAuthority;
import mainstreet.semantic.configuration.ConfigurationRevisionFailureCategory;
import mainstreet.semantic.configuration.ConfigurationRevisionPersistenceException;
import mainstreet.semantic.configuration.MaterialiseConfigurationChangeCommand;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.configuration.OrdinaryNewConfigurationSemanticReleaseAuthority;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Stores a complete immutable change candidate without approving or activating it.
 * Authority: MS-PROT-040 v1.0,
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * §13 — Change set; §14 — Change-set provenance; §19 — Candidate generation; §49 — Configuration history.
 * Release pinning: MS-PROT-040 v1.5,
 * designs/MS-PROT-040 v1.5 — Release-Purpose Admission & Ordinary Release Reference Amendment.md,
 * §9 — Configuration-Revision Creation and Validation.
 */
public final class JooqConfigurationChangeAuthority implements ConfigurationChangeAuthority {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;
    private final ConfigurationRevisionAuthority revisions;
    private final OrdinaryNewConfigurationSemanticReleaseAuthority ordinaryRelease;
    private final ConfigurationChangeAuthorizationAuthority authorization;

    public JooqConfigurationChangeAuthority(DSLContext dsl, PlatformTransactionManager transactionManager,
            ConfigurationRevisionAuthority revisions, OrdinaryNewConfigurationSemanticReleaseAuthority ordinaryRelease,
            ConfigurationChangeAuthorizationAuthority authorization) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(Objects.requireNonNull(transactionManager, "transactionManager"));
        this.revisions = Objects.requireNonNull(revisions, "revisions");
        this.ordinaryRelease = Objects.requireNonNull(ordinaryRelease, "ordinaryRelease");
        this.authorization = Objects.requireNonNull(authorization, "authorization");
    }

    @Override
    public ChangedConfigurationRevision materialise(MaterialiseConfigurationChangeCommand command,
                                                     TrustedExecutionContext context) {
        Objects.requireNonNull(command, "command");
        var change = command.changeSet();
        if (context == null || !change.merchantScope().equals(context.merchantScope())
                || !change.provenance().proposingPrincipalIdentifier().equals(context.principal().identifier())) {
            throw failure(ConfigurationRevisionFailureCategory.CHANGE_AUTHORIZATION_REJECTED,
                    "Exact trusted merchant and proposing principal are required");
        }
        try {
            return Objects.requireNonNull(transactions.execute(status -> materialiseInside(command, context)),
                    "Configuration change transaction returned no result");
        } catch (DataAccessException failure) {
            throw new ConfigurationRevisionPersistenceException(ConfigurationRevisionFailureCategory.PERSISTENCE_FAILURE,
                    "Could not materialise Configuration change", failure);
        }
    }

    private ChangedConfigurationRevision materialiseInside(MaterialiseConfigurationChangeCommand command,
                                                            TrustedExecutionContext context) {
        var change = command.changeSet();
        String merchant = change.merchantScope().merchantIdentifier();
        // Share the initial writer's merchant lock so version allocation has one serialization boundary.
        dsl.fetch("select pg_advisory_xact_lock(hashtextextended(cast(? as text), 72))",
                "configuration-merchant|" + merchant);
        if (!authorization.mayMaterialise(change, context)) {
            throw failure(ConfigurationRevisionFailureCategory.CHANGE_AUTHORIZATION_REJECTED,
                    "Current source-qualified Configuration change authority is required");
        }
        var previous = revisionForChange(change.merchantScope(), change.changeSetIdentifier());
        if (previous.isPresent()) {
            var committed = previous.orElseThrow();
            if (!committed.changeSet().equals(change)
                    || !committed.configuration().configurationIdentifier().equals(command.requestedRevisionIdentifier())) {
                throw failure(ConfigurationRevisionFailureCategory.CHANGE_SET_IDENTITY_CONFLICT,
                        "Change-set identity was already used for different intent");
            }
            return committed;
        }
        var base = revisions.configuration(change.merchantScope(), change.baseConfigurationRevisionIdentifier())
                .orElseThrow(() -> failure(ConfigurationRevisionFailureCategory.BASE_CONFIGURATION_REVISION_NOT_FOUND,
                        "Exact merchant-owned base revision does not exist"));
        if (revisions.configuration(change.merchantScope(), command.requestedRevisionIdentifier()).isPresent()) {
            throw failure(ConfigurationRevisionFailureCategory.CONFIGURATION_REVISION_IDENTITY_CONFLICT,
                    "Requested Configuration Revision identity already exists");
        }
        String release = ordinaryRelease.currentSemanticRegistryReleaseIdentifier();
        if (release == null || release.isBlank()) {
            throw failure(ConfigurationRevisionFailureCategory.ORDINARY_SEMANTIC_RELEASE_UNAVAILABLE,
                    "Ordinary semantic release is unavailable");
        }
        Long lastVersion = dsl.fetchOne("select max(configuration_version) as last_version "
                + "from merchant_configuration_revision where merchant_identifier = ?", merchant)
                .get("last_version", Long.class);
        long version = Math.addExact(Objects.requireNonNull(lastVersion, "Stored base has no version"), 1);
        var candidate = change.candidateFrom(base, command.requestedRevisionIdentifier(), version, release);
        insert(command, candidate);
        return revisionForChange(change.merchantScope(), change.changeSetIdentifier()).orElseThrow();
    }

    @Override
    public Optional<ChangedConfigurationRevision> revisionForChange(MerchantScope scope, String changeSetIdentifier) {
        Objects.requireNonNull(scope, "scope");
        if (changeSetIdentifier == null || changeSetIdentifier.isBlank()) {
            throw new IllegalArgumentException("Change-set identifier must not be blank");
        }
        Record row = dsl.fetchOne("select configuration_revision_identifier, change_origin, change_source_identifier, "
                + "change_proposed_at, materialised_by, materialised_at from merchant_configuration_revision "
                + "where merchant_identifier = ? and source_change_set_identifier = ?",
                scope.merchantIdentifier(), changeSetIdentifier);
        if (row == null) return Optional.empty();
        var configuration = revisions.configuration(scope, row.get("configuration_revision_identifier", String.class))
                .orElseThrow(() -> new IllegalStateException("Stored change has no Configuration contents"));
        var change = new ConfigurationChangeSet(scope, changeSetIdentifier,
                configuration.baseConfigurationIdentifier().orElseThrow(), configuration.capabilityIdentifiers(),
                configuration.policySelections(), configuration.fulfilmentBindingSetRevisionReference(),
                new ConfigurationChangeSet.Provenance(ConfigurationChangeSet.Origin.valueOf(row.get("change_origin", String.class)),
                        row.get("change_source_identifier", String.class), row.get("materialised_by", String.class),
                        Instant.parse(row.get("change_proposed_at", String.class))));
        return Optional.of(new ChangedConfigurationRevision(configuration, change, row.get("materialised_at", Instant.class)));
    }

    private void insert(MaterialiseConfigurationChangeCommand command, MerchantConfiguration candidate) {
        var change = command.changeSet();
        var binding = candidate.fulfilmentBindingSetRevisionReference();
        dsl.execute("insert into merchant_configuration_revision (merchant_identifier, configuration_revision_identifier, "
                + "configuration_version, semantic_registry_release_identifier, base_configuration_revision_identifier, "
                + "fulfilment_binding_set_identifier, fulfilment_binding_set_revision, materialised_by, materialised_at, "
                + "source_change_set_identifier, change_origin, change_source_identifier, change_proposed_at) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamptz), ?, ?, ?, ?)",
                candidate.merchantIdentifier(), candidate.configurationIdentifier(), candidate.version(),
                candidate.semanticRegistryVersion(), change.baseConfigurationRevisionIdentifier(),
                binding.map(FulfilmentBindingSetRevisionReference::bindingSetIdentifier).orElse(null),
                binding.map(FulfilmentBindingSetRevisionReference::revision).orElse(null),
                change.provenance().proposingPrincipalIdentifier(), command.materialisedAt().toString(),
                change.changeSetIdentifier(), change.provenance().origin().name(), change.provenance().sourceIdentifier(),
                change.provenance().proposedAt().toString());
        for (String capability : candidate.capabilityIdentifiers()) {
            dsl.execute("insert into merchant_configuration_revision_capability "
                    + "(merchant_identifier, configuration_revision_identifier, capability_identifier) values (?, ?, ?)",
                    candidate.merchantIdentifier(), candidate.configurationIdentifier(), capability);
        }
        for (var policy : candidate.policySelections()) {
            dsl.execute("insert into merchant_configuration_revision_policy "
                    + "(merchant_identifier, configuration_revision_identifier, owner_capability_identifier, policy_identifier, selected_value) "
                    + "values (?, ?, ?, ?, ?)", candidate.merchantIdentifier(), candidate.configurationIdentifier(),
                    policy.ownerCapabilityIdentifier(), policy.policyIdentifier(), policy.selectedValue());
        }
    }

    private static ConfigurationRevisionPersistenceException failure(
            ConfigurationRevisionFailureCategory category,
            String message) {
        return new ConfigurationRevisionPersistenceException(category, message);
    }
}