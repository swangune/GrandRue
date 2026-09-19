package grandrue.surface;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectionServiceabilityBoundedReadAffinityTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final ProjectionContractIdentity CONTRACT =
            new ProjectionContractIdentity("platform", "merchant-presence");
    private static final ProjectionReadUseIdentity READ_USE =
            new ProjectionReadUseIdentity("platform", "public-merchant-presence");

    @Test
    void semantically_equal_bounded_reads_remain_distinct_p2_affinity_domains() {
        EstablishedObservationRequest established = request();
        BoundedProjectionRead first = new BoundedProjectionRead(
                established,
                CONTRACT,
                READ_USE,
                Set.of(),
                List.of()
        );
        BoundedProjectionRead second = new BoundedProjectionRead(
                established,
                CONTRACT,
                READ_USE,
                Set.of(),
                List.of()
        );

        ProjectionServiceabilityEvaluationRequest firstRequest =
                ProjectionServiceabilityEvaluationRequest.forBoundedRead(
                        first,
                        Instant.EPOCH
                );
        ProjectionServiceabilityEvaluationRequest secondRequest =
                ProjectionServiceabilityEvaluationRequest.forBoundedRead(
                        second,
                        Instant.EPOCH
                );

        assertNotSame(first.binding(), second.binding());
        assertNotSame(
                firstRequest.boundedReadBinding().orElseThrow(),
                secondRequest.boundedReadBinding().orElseThrow()
        );
    }

    @Test
    void fail_closed_p2_results_preserve_exact_affinity_and_reject_laundering() {
        EstablishedObservationRequest established = request();
        BoundedProjectionRead first = new BoundedProjectionRead(
                established,
                CONTRACT,
                READ_USE,
                Set.of(),
                List.of()
        );
        BoundedProjectionRead second = new BoundedProjectionRead(
                established,
                CONTRACT,
                READ_USE,
                Set.of(),
                List.of()
        );
        ProjectionServiceabilityEvaluationEngine engine =
                new ProjectionServiceabilityEvaluationEngine(
                        new ProjectionContractRegistrySnapshot(RELEASE, Set.of()),
                        new ProjectionPolicyEvaluatorRegistrySnapshot(RELEASE, Set.of())
                );

        ProjectionServiceabilityResult firstResult = engine.evaluate(
                ProjectionServiceabilityEvaluationRequest.forBoundedRead(
                        first,
                        Instant.EPOCH
                )
        );
        ProjectionServiceabilityResult secondResult = engine.evaluate(
                ProjectionServiceabilityEvaluationRequest.forBoundedRead(
                        second,
                        Instant.EPOCH
                )
        );
        ProjectionServiceabilityResult launderedResult = engine.evaluate(
                new ProjectionServiceabilityEvaluationRequest(
                        first.semanticRegistryReleaseIdentifier(),
                        first.contractIdentity(),
                        first.readUseIdentity(),
                        Instant.EPOCH,
                        first.sourceEvidence()
                )
        );

        assertSame(first.binding(), firstResult.boundedReadBinding().orElseThrow());
        assertSame(second.binding(), secondResult.boundedReadBinding().orElseThrow());
        assertNotSame(
                firstResult.boundedReadBinding().orElseThrow(),
                secondResult.boundedReadBinding().orElseThrow()
        );
        assertTrue(firstResult.hasExactBoundedReadAffinity(first));
        assertFalse(firstResult.hasExactBoundedReadAffinity(second));
        assertTrue(secondResult.hasExactBoundedReadAffinity(second));
        assertFalse(secondResult.hasExactBoundedReadAffinity(first));
        assertFalse(launderedResult.hasExactBoundedReadAffinity(first));
        assertFalse(launderedResult.hasExactBoundedReadAffinity(second));
    }

    private static EstablishedObservationRequest request() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        MERCHANT.merchantIdentifier(),
                        RELEASE
                ),
                MERCHANT,
                RELEASE,
                Optional.empty()
        );
    }
}
