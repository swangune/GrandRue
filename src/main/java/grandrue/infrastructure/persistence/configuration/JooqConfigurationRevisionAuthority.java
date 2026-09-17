package grandrue.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import grandrue.fulfilment.FulfilmentBindingSetRevisionReference;
import mainstreet.onboarding.AcknowledgeInitialConfigurationIntentCommand;
import mainstreet.onboarding.InitialConfigurationIntent;
import mainstreet.onboarding.InitialConfigurationIntentIdentity;
import mainstreet.onboarding.OnboardingCaseEvidenceStore;
import mainstreet.onboarding.OnboardingCaseIdentity;
import mainstreet.onboarding.OnboardingCaseRevision;
import mainstreet.onboarding.OnboardingSemanticSeed;
import mainstreet.semantic.configuration.ConfigurationRevisionAuthority;
import mainstreet.semantic.configuration.ConfigurationRevisionFailureCategory;
import mainstreet.semantic.configuration.ConfigurationRevisionPersistenceException;
import mainstreet.semantic.configuration.MaterialiseInitialConfigurationRevisionCommand;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.configuration.MerchantConfigurationRevision;
import mainstreet.semantic.configuration.OrdinaryNewConfigurationSemanticReleaseAuthority;
import mainstreet.semantic.configuration.PolicySelection;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PostgreSQL/jOOQ immutable Merchant Configuration Revision authority.
 *
 * <p>The ordinary semantic release is resolved from a platform-owned port only
 * after the exact source intent is locked and confirmed not to have been
 * consumed. Retry therefore returns the original release-affined revision even
 * after the ordinary platform reference advances.</p>
 */
public final class JooqConfigurationRevisionAuthority
        implements ConfigurationRevisionAuthority {

    private static final String CAPABILITY_SEED_NAMESPACE =
            "mainstreet.semantic";

    private static final Table<?> REVISION =
            DSL.table(DSL.name("merchant_configuration_revision"));
    private static final Table<?> CAPABILITY = DSL.table(
            DSL.name("merchant_configuration_revision_capability")
    );
    private static final Table<?> POLICY = DSL.table(
            DSL.name("merchant_configuration_revision_policy")
    );

    private static final Field<String> MERCHANT_ID =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> CONFIGURATION_ID = DSL.field(
            DSL.name("configuration_revision_identifier"),
            String.class
    );
    private static final Field<Long> CONFIGURATION_VERSION = DSL.field(
            DSL.name("configuration_version"),
            Long.class
    );
    private static final Field<String> SEMANTIC_RELEASE = DSL.field(
            DSL.name("semantic_registry_release_identifier"),
            String.class
    );
    private static final Field<String> BASE_CONFIGURATION_ID = DSL.field(
            DSL.name("base_configuration_revision_identifier"),
            String.class
    );
    private static final Field<String> BINDING_SET_ID = DSL.field(
            DSL.name("fulfilment_binding_set_identifier"),
            String.class
    );
    private static final Field<Long> BINDING_SET_REVISION = DSL.field(
            DSL.name("fulfilment_binding_set_revision"),
            Long.class
    );
    private static final Field<String> SOURCE_INTENT_ID = DSL.field(
            DSL.name("source_initial_configuration_intent_identity"),
            String.class
    );
    private static final Field<String> SOURCE_CASE_ID = DSL.field(
            DSL.name("source_onboarding_case_identity"),
            String.class
    );
    private static final Field<String> SOURCE_CASE_REVISION = DSL.field(
            DSL.name("source_onboarding_case_revision"),
            String.class
    );
    private static final Field<String> ONBOARDING_COMPLETION_REVISION =
            DSL.field(
                    DSL.name("onboarding_completion_revision"),
                    String.class
            );
    private static final Field<String> MATERIALISED_BY =
            DSL.field(DSL.name("materialised_by"), String.class);
    private static final Field<String> ORIGIN_ID =
            DSL.field(DSL.name("origin_identifier"), String.class);
    private static final Field<Instant> MATERIALISED_AT =
            DSL.field(DSL.name("materialised_at"), Instant.class);
    private static final Field<String> CAPABILITY_ID =
            DSL.field(DSL.name("capability_identifier"), String.class);
    private static final Field<String> POLICY_OWNER_ID = DSL.field(
            DSL.name("owner_capability_identifier"),
            String.class
    );
    private static final Field<String> POLICY_ID =
            DSL.field(DSL.name("policy_identifier"), String.class);
    private static final Field<String> SELECTED_VALUE =
            DSL.field(DSL.name("selected_value"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final OnboardingCaseEvidenceStore onboardingStore;
    private final OrdinaryNewConfigurationSemanticReleaseAuthority
            ordinaryReleaseAuthority;

    public JooqConfigurationRevisionAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            OnboardingCaseEvidenceStore onboardingStore,
            OrdinaryNewConfigurationSemanticReleaseAuthority
                    ordinaryReleaseAuthority
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.onboardingStore = Objects.requireNonNull(
                onboardingStore,
                "onboardingStore"
        );
        this.ordinaryReleaseAuthority = Objects.requireNonNull(
                ordinaryReleaseAuthority,
                "ordinaryReleaseAuthority"
        );
    }

    @Override
    public MerchantConfigurationRevision materialiseInitial(
            MaterialiseInitialConfigurationRevisionCommand command
    ) {
        Objects.requireNonNull(command, "command");
        MerchantConfigurationRevision result = transactionTemplate.execute(
                status -> materialiseInsideTransaction(command)
        );
        return Objects.requireNonNull(
                result,
                "Configuration Revision materialisation returned no result"
        );
    }

    @Override
    public Optional<MerchantConfigurationRevision> revision(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        if (configurationRevisionIdentifier == null
                || configurationRevisionIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Configuration Revision identifier must not be blank"
            );
        }
        Record row = selectRevisionFields()
                .from(REVISION)
                .where(MERCHANT_ID.eq(
                        merchantScope.merchantIdentifier()
                ))
                .and(CONFIGURATION_ID.eq(configurationRevisionIdentifier))
                .and(SOURCE_INTENT_ID.isNotNull())
                .fetchOne();
        return Optional.ofNullable(row).map(this::toRevision);
    }

    @Override
    public Optional<MerchantConfiguration> configuration(MerchantScope scope, String revisionIdentifier) {
        Objects.requireNonNull(scope, "scope");
        if (revisionIdentifier == null || revisionIdentifier.isBlank()) {
            throw new IllegalArgumentException("Configuration Revision identifier must not be blank");
        }
        Record row = selectRevisionFields().from(REVISION)
                .where(MERCHANT_ID.eq(scope.merchantIdentifier()))
                .and(CONFIGURATION_ID.eq(revisionIdentifier)).fetchOne();
        return Optional.ofNullable(row).map(this::toConfiguration);
    }

    @Override
    public Optional<MerchantConfigurationRevision> revisionForInitialIntent(
            InitialConfigurationIntentIdentity intentIdentity
    ) {
        Objects.requireNonNull(intentIdentity, "intentIdentity");
        Record row = selectRevisionFields()
                .from(REVISION)
                .where(SOURCE_INTENT_ID.eq(intentIdentity.value()))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private MerchantConfigurationRevision materialiseInsideTransaction(
            MaterialiseInitialConfigurationRevisionCommand command
    ) {
        InitialConfigurationIntentIdentity intentIdentity =
                command.initialConfigurationIntentIdentity();
        lockInitialIntent(intentIdentity.value());

        Optional<MerchantConfigurationRevision> committed =
                revisionForInitialIntent(intentIdentity);
        if (committed.isPresent()) {
            MerchantConfigurationRevision existing = committed.orElseThrow();
            requireSameIntent(command, existing);
            return existing;
        }

        InitialConfigurationIntent intent = onboardingStore
                .initialConfigurationIntentByIdentity(intentIdentity)
                .orElseThrow(() -> failure(
                        ConfigurationRevisionFailureCategory
                                .INITIAL_CONFIGURATION_INTENT_NOT_FOUND,
                        "Initial Configuration Intent does not exist"
                ));
        Set<String> capabilityIdentifiers =
                resolveCapabilityIdentifiers(intent);

        String merchantIdentifier =
                intent.merchantScope().merchantIdentifier();
        lockMerchant(merchantIdentifier);
        if (configurationVersionExists(merchantIdentifier, 1L)) {
            throw failure(
                    ConfigurationRevisionFailureCategory
                            .CONFIGURATION_VERSION_CONFLICT,
                    "Initial Configuration Revision version already exists"
            );
        }
        lockConfigurationRevision(
                merchantIdentifier,
                command.requestedConfigurationRevisionIdentifier()
        );
        if (revision(
                intent.merchantScope(),
                command.requestedConfigurationRevisionIdentifier()
        ).isPresent()) {
            throw failure(
                    ConfigurationRevisionFailureCategory
                            .CONFIGURATION_REVISION_IDENTITY_CONFLICT,
                    "Configuration Revision identity already exists"
            );
        }

        String semanticRelease = ordinaryReleaseAuthority
                .currentSemanticRegistryReleaseIdentifier();
        if (semanticRelease == null || semanticRelease.isBlank()) {
            throw failure(
                    ConfigurationRevisionFailureCategory
                            .ORDINARY_SEMANTIC_RELEASE_UNAVAILABLE,
                    "Ordinary new-configuration semantic release is unavailable"
            );
        }

        MerchantConfiguration configuration = new MerchantConfiguration(
                merchantIdentifier,
                command.requestedConfigurationRevisionIdentifier(),
                1L,
                semanticRelease,
                capabilityIdentifiers,
                Set.of(),
                Optional.empty(),
                Optional.empty()
        );
        MerchantConfigurationRevision revision =
                new MerchantConfigurationRevision(
                        configuration,
                        intent.identity(),
                        intent.sourceOnboardingCaseIdentity(),
                        intent.sourceOnboardingCaseRevision(),
                        command.onboardingCompletionRevision(),
                        command.materialisedBy(),
                        command.originIdentifier(),
                        command.materialisedAt()
                );
        insert(revision);
        onboardingStore.acknowledgeInitialConfigurationIntent(
                new AcknowledgeInitialConfigurationIntentCommand(
                        intent.identity(),
                        command.onboardingCompletionRevision(),
                        acknowledgementRequestIdentifier(revision),
                        command.materialisedBy(),
                        command.originIdentifier(),
                        command.materialisedAt()
                )
        );
        return revisionForInitialIntent(intentIdentity)
                .orElseThrow(() -> new IllegalStateException(
                        "Configuration Revision was not committed"
                ));
    }

    private static Set<String> resolveCapabilityIdentifiers(
            InitialConfigurationIntent intent
    ) {
        if (intent.merchantReviewEvidence()
                .candidateRegisteredIntent()
                .stream()
                .map(OnboardingSemanticSeed::namespace)
                .anyMatch(namespace ->
                        !CAPABILITY_SEED_NAMESPACE.equals(namespace)
                )) {
            throw failure(
                    ConfigurationRevisionFailureCategory
                            .UNSUPPORTED_INITIAL_INTENT,
                    "Initial Configuration Intent contains an unsupported seed namespace"
            );
        }
        return intent.merchantReviewEvidence()
                .candidateRegisteredIntent()
                .stream()
                .map(OnboardingSemanticSeed::identifier)
                .collect(Collectors.toUnmodifiableSet());
    }

    private boolean configurationVersionExists(
            String merchantIdentifier,
            long configurationVersion
    ) {
        return dsl.fetchExists(
                DSL.selectOne()
                        .from(REVISION)
                        .where(MERCHANT_ID.eq(merchantIdentifier))
                        .and(CONFIGURATION_VERSION.eq(configurationVersion))
        );
    }

    private void insert(MerchantConfigurationRevision revision) {
        MerchantConfiguration configuration = revision.configuration();
        Optional<FulfilmentBindingSetRevisionReference> binding =
                configuration.fulfilmentBindingSetRevisionReference();
        dsl.insertInto(REVISION)
                .columns(
                        MERCHANT_ID,
                        CONFIGURATION_ID,
                        CONFIGURATION_VERSION,
                        SEMANTIC_RELEASE,
                        BASE_CONFIGURATION_ID,
                        BINDING_SET_ID,
                        BINDING_SET_REVISION,
                        SOURCE_INTENT_ID,
                        SOURCE_CASE_ID,
                        SOURCE_CASE_REVISION,
                        ONBOARDING_COMPLETION_REVISION,
                        MATERIALISED_BY,
                        ORIGIN_ID,
                        MATERIALISED_AT
                )
                .values(
                        configuration.merchantIdentifier(),
                        configuration.configurationIdentifier(),
                        configuration.version(),
                        configuration.semanticRegistryVersion(),
                        configuration.baseConfigurationIdentifier()
                                .orElse(null),
                        binding.map(
                                FulfilmentBindingSetRevisionReference
                                        ::bindingSetIdentifier
                        ).orElse(null),
                        binding.map(
                                FulfilmentBindingSetRevisionReference::revision
                        ).orElse(null),
                        revision.sourceInitialConfigurationIntentIdentity()
                                .value(),
                        revision.sourceOnboardingCaseIdentity().value(),
                        revision.sourceOnboardingCaseRevision().value(),
                        revision.onboardingCompletionRevision().value(),
                        revision.materialisedBy(),
                        revision.originIdentifier().orElse(null),
                        revision.materialisedAt()
                )
                .execute();

        configuration.capabilityIdentifiers().stream()
                .sorted()
                .forEach(capability -> dsl.insertInto(CAPABILITY)
                        .columns(MERCHANT_ID, CONFIGURATION_ID, CAPABILITY_ID)
                        .values(
                                configuration.merchantIdentifier(),
                                configuration.configurationIdentifier(),
                                capability
                        )
                        .execute());

        configuration.policySelections().stream()
                .sorted(Comparator
                        .comparing(PolicySelection
                                ::ownerCapabilityIdentifier)
                        .thenComparing(PolicySelection::policyIdentifier))
                .forEach(policy -> dsl.insertInto(POLICY)
                        .columns(
                                MERCHANT_ID,
                                CONFIGURATION_ID,
                                POLICY_OWNER_ID,
                                POLICY_ID,
                                SELECTED_VALUE
                        )
                        .values(
                                configuration.merchantIdentifier(),
                                configuration.configurationIdentifier(),
                                policy.ownerCapabilityIdentifier(),
                                policy.policyIdentifier(),
                                policy.selectedValue()
                        )
                        .execute());
    }

    private org.jooq.SelectSelectStep<? extends Record>
            selectRevisionFields() {
        return dsl.select(
                MERCHANT_ID,
                CONFIGURATION_ID,
                CONFIGURATION_VERSION,
                SEMANTIC_RELEASE,
                BASE_CONFIGURATION_ID,
                BINDING_SET_ID,
                BINDING_SET_REVISION,
                SOURCE_INTENT_ID,
                SOURCE_CASE_ID,
                SOURCE_CASE_REVISION,
                ONBOARDING_COMPLETION_REVISION,
                MATERIALISED_BY,
                ORIGIN_ID,
                MATERIALISED_AT
        );
    }

    private MerchantConfiguration toConfiguration(Record row) {
        String merchantIdentifier = row.get(MERCHANT_ID);
        String configurationIdentifier = row.get(CONFIGURATION_ID);
        Set<String> capabilities = dsl.select(CAPABILITY_ID)
                .from(CAPABILITY)
                .where(MERCHANT_ID.eq(merchantIdentifier))
                .and(CONFIGURATION_ID.eq(configurationIdentifier))
                .orderBy(CAPABILITY_ID.asc())
                .fetchSet(CAPABILITY_ID);
        Set<PolicySelection> policies = dsl.select(
                        POLICY_OWNER_ID,
                        POLICY_ID,
                        SELECTED_VALUE
                )
                .from(POLICY)
                .where(MERCHANT_ID.eq(merchantIdentifier))
                .and(CONFIGURATION_ID.eq(configurationIdentifier))
                .orderBy(POLICY_OWNER_ID.asc(), POLICY_ID.asc())
                .fetchSet(record -> new PolicySelection(
                        record.get(POLICY_OWNER_ID),
                        record.get(POLICY_ID),
                        record.get(SELECTED_VALUE)
                ));
        Optional<FulfilmentBindingSetRevisionReference> binding =
                Optional.ofNullable(row.get(BINDING_SET_ID))
                        .map(identifier ->
                                new FulfilmentBindingSetRevisionReference(
                                        identifier,
                                        row.get(BINDING_SET_REVISION)
                                )
                        );
        return new MerchantConfiguration(
                merchantIdentifier,
                configurationIdentifier,
                row.get(CONFIGURATION_VERSION),
                row.get(SEMANTIC_RELEASE),
                capabilities,
                policies,
                Optional.ofNullable(row.get(BASE_CONFIGURATION_ID)),
                binding
        );
    }

    private MerchantConfigurationRevision toRevision(Record row) {
        return new MerchantConfigurationRevision(
                toConfiguration(row),
                new InitialConfigurationIntentIdentity(
                        row.get(SOURCE_INTENT_ID)
                ),
                new OnboardingCaseIdentity(row.get(SOURCE_CASE_ID)),
                new OnboardingCaseRevision(row.get(SOURCE_CASE_REVISION)),
                new OnboardingCaseRevision(
                        row.get(ONBOARDING_COMPLETION_REVISION)
                ),
                row.get(MATERIALISED_BY),
                Optional.ofNullable(row.get(ORIGIN_ID)),
                row.get(MATERIALISED_AT)
        );
    }

    private static void requireSameIntent(
            MaterialiseInitialConfigurationRevisionCommand command,
            MerchantConfigurationRevision committed
    ) {
        boolean same = command.initialConfigurationIntentIdentity().equals(
                        committed.sourceInitialConfigurationIntentIdentity()
                )
                && command.requestedConfigurationRevisionIdentifier().equals(
                        committed.configuration().configurationIdentifier()
                )
                && command.onboardingCompletionRevision().equals(
                        committed.onboardingCompletionRevision()
                )
                && command.materialisedBy().equals(committed.materialisedBy())
                && command.originIdentifier().equals(
                        committed.originIdentifier()
                )
                && command.materialisedAt().equals(
                        committed.materialisedAt()
                );
        if (!same) {
            throw failure(
                    ConfigurationRevisionFailureCategory
                            .INITIAL_INTENT_IDENTITY_CONFLICT,
                    "Initial Configuration Intent is bound to a different revision intent"
            );
        }
    }

    private void lockInitialIntent(String intentIdentity) {
        advisoryLock(
                "configuration-initial-intent|" + intentIdentity,
                71
        );
    }

    private static String acknowledgementRequestIdentifier(
            MerchantConfigurationRevision revision
    ) {
        return "configuration-bootstrap|"
                + revision.configuration().merchantIdentifier()
                + "|"
                + revision.configuration().configurationIdentifier();
    }

    private void lockMerchant(String merchantIdentifier) {
        advisoryLock("configuration-merchant|" + merchantIdentifier, 72);
    }

    private void lockConfigurationRevision(
            String merchantIdentifier,
            String configurationIdentifier
    ) {
        advisoryLock(
                "configuration-revision|"
                        + merchantIdentifier
                        + "|"
                        + configurationIdentifier,
                73
        );
    }

    private void advisoryLock(String value, int seed) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), ?))",
                value,
                seed
        );
    }

    private static ConfigurationRevisionPersistenceException failure(
            ConfigurationRevisionFailureCategory category,
            String message
    ) {
        return new ConfigurationRevisionPersistenceException(category, message);
    }
}
