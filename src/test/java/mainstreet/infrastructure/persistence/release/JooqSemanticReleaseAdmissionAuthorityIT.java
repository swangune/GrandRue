package mainstreet.infrastructure.persistence.release;

import mainstreet.semantic.release.AdvanceOrdinarySemanticReleaseCommand;
import mainstreet.semantic.release.OrdinaryNewConfigurationSemanticReleaseReference;
import mainstreet.semantic.release.RecordSemanticReleasePurposeAdmissionDecisionCommand;
import mainstreet.semantic.release.SemanticReleaseAdmissionDisposition;
import mainstreet.semantic.release.SemanticReleaseAdmissionFailureCategory;
import mainstreet.semantic.release.SemanticReleaseAdmissionPersistenceException;
import mainstreet.semantic.release.SemanticReleasePurpose;
import mainstreet.semantic.release.SemanticReleasePurposeAdmissionDecision;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqSemanticReleaseAdmissionAuthorityIT {

    private static final Instant DECIDED_AT =
            Instant.parse("2026-08-30T03:40:00Z");

    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure().dataSource(dataSource)
                .locations("classpath:db/migration").load().migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        dsl.execute("truncate table ordinary_new_configuration_semantic_release_pointer, ordinary_new_configuration_semantic_release_reference_revision, semantic_release_purpose_current_admission, semantic_release_purpose_admission_decision cascade");
    }

    @Test
    void retains_append_only_current_decisions_separately_per_release_and_purpose() {
        JooqSemanticReleaseAdmissionAuthority authority = authority();
        SemanticReleasePurposeAdmissionDecision validation = authority.recordDecision(
                decision("decision-validation", SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION,
                        SemanticReleaseAdmissionDisposition.ADMITTED)
        );
        SemanticReleasePurposeAdmissionDecision activity = authority.recordDecision(
                decision("decision-activity", SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY,
                        SemanticReleaseAdmissionDisposition.ADMITTED)
        );

        assertEquals(validation, authority.currentDecision(
                "semantic-release-21",
                SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION
        ).orElseThrow());
        assertEquals(activity, authority.currentDecision(
                "semantic-release-21",
                SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY
        ).orElseThrow());
        assertEquals(2, dsl.fetchCount(DSL.table(
                DSL.name("semantic_release_purpose_admission_decision")
        )));
    }

    @Test
    void ordinary_reference_advance_atomically_requires_both_current_purposes_admitted() {
        JooqSemanticReleaseAdmissionAuthority authority = authority();
        authority.recordDecision(decision(
                "decision-validation",
                SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION,
                SemanticReleaseAdmissionDisposition.ADMITTED
        ));

        SemanticReleaseAdmissionPersistenceException missing = assertThrows(
                SemanticReleaseAdmissionPersistenceException.class,
                () -> authority.advanceOrdinaryReference(reference("reference-1", 0))
        );
        assertEquals(
                SemanticReleaseAdmissionFailureCategory.RELEASE_NOT_ADMITTED_FOR_BOTH_PURPOSES,
                missing.category()
        );
        assertEquals(0, dsl.fetchCount(DSL.table(
                DSL.name("ordinary_new_configuration_semantic_release_reference_revision")
        )));

        authority.recordDecision(decision(
                "decision-activity",
                SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY,
                SemanticReleaseAdmissionDisposition.ADMITTED
        ));
        OrdinaryNewConfigurationSemanticReleaseReference reference =
                authority.advanceOrdinaryReference(reference("reference-1", 0));

        assertEquals("semantic-release-21", reference.semanticRegistryReleaseIdentifier());
        assertEquals("decision-validation", reference.validationAdmissionDecisionIdentifier());
        assertEquals("decision-activity", reference.businessActivityAdmissionDecisionIdentifier());
        assertEquals(1, reference.referenceEpoch());
        assertEquals(reference, authority.currentOrdinaryReference().orElseThrow());
    }

    @Test
    void withdrawn_current_decision_blocks_ordinary_reference_even_when_old_admission_exists() {
        JooqSemanticReleaseAdmissionAuthority authority = authority();
        authority.recordDecision(decision(
                "decision-validation-admit",
                SemanticReleasePurpose.NEW_CONFIGURATION_VALIDATION,
                SemanticReleaseAdmissionDisposition.ADMITTED
        ));
        authority.recordDecision(decision(
                "decision-activity-admit",
                SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY,
                SemanticReleaseAdmissionDisposition.ADMITTED
        ));
        authority.recordDecision(decision(
                "decision-activity-withdraw",
                SemanticReleasePurpose.NEW_BUSINESS_ACTIVITY,
                SemanticReleaseAdmissionDisposition.WITHDRAWN
        ));

        SemanticReleaseAdmissionPersistenceException failure = assertThrows(
                SemanticReleaseAdmissionPersistenceException.class,
                () -> authority.advanceOrdinaryReference(reference("reference-2", 0))
        );
        assertEquals(
                SemanticReleaseAdmissionFailureCategory.RELEASE_NOT_ADMITTED_FOR_BOTH_PURPOSES,
                failure.category()
        );
        assertEquals(3, dsl.fetchCount(DSL.table(
                DSL.name("semantic_release_purpose_admission_decision")
        )));
    }

    private JooqSemanticReleaseAdmissionAuthority authority() {
        return new JooqSemanticReleaseAdmissionAuthority(dsl, transactionManager);
    }

    private static RecordSemanticReleasePurposeAdmissionDecisionCommand decision(
            String identifier,
            SemanticReleasePurpose purpose,
            SemanticReleaseAdmissionDisposition disposition
    ) {
        return new RecordSemanticReleasePurposeAdmissionDecisionCommand(
                identifier,
                "semantic-release-21",
                purpose,
                disposition,
                "platform-release-controller",
                "change-ticket-21",
                DECIDED_AT
        );
    }

    private static AdvanceOrdinarySemanticReleaseCommand reference(
            String identifier,
            long expectedEpoch
    ) {
        return new AdvanceOrdinarySemanticReleaseCommand(
                identifier,
                "semantic-release-21",
                expectedEpoch,
                "platform-release-controller",
                "change-ticket-21",
                DECIDED_AT.plusSeconds(60)
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
