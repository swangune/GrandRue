package mainstreet.semantic.release;

import mainstreet.fulfilment.FulfilmentContractRegistrySnapshot;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.ExposureElementContractRegistrySnapshot;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SemanticReleaseAssemblyTest {

    @Test
    void assemblesMatchingReleaseAffinities() {
        SemanticRegistrySnapshot semantic = semantic("release-a");
        SurfaceContributionRegistrySnapshot surface = surface("release-a");
        FulfilmentContractRegistrySnapshot fulfilment = fulfilment("release-a");
        ExposureElementContractRegistrySnapshot exposure = exposure("release-a");

        SemanticReleaseAssembly assembly = new SemanticReleaseAssembly(
                semantic,
                surface,
                fulfilment,
                exposure
        );

        assertEquals("release-a", assembly.releaseIdentifier());
        assertSame(semantic, assembly.semanticRegistry());
        assertSame(surface, assembly.surfaceRegistry());
        assertSame(fulfilment, assembly.fulfilmentRegistry());
        assertSame(exposure, assembly.exposureRegistry());
    }

    @Test
    void rejectsSurfaceRegistryFromAnotherSemanticRelease() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SemanticReleaseAssembly(
                        semantic("release-a"),
                        surface("release-b"),
                        fulfilment("release-a"),
                        exposure("release-a")
                )
        );
    }

    @Test
    void rejectsFulfilmentRegistryFromAnotherSemanticRelease() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SemanticReleaseAssembly(
                        semantic("release-a"),
                        surface("release-a"),
                        fulfilment("release-b"),
                        exposure("release-a")
                )
        );
    }

    @Test
    void rejectsExposureRegistryFromAnotherSemanticRelease() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SemanticReleaseAssembly(
                        semantic("release-a"),
                        surface("release-a"),
                        fulfilment("release-a"),
                        exposure("release-b")
                )
        );
    }

    @Test
    void repositoryResolvesExactReleaseWithoutLatestSubstitution() {
        SemanticReleaseAssembly releaseA = assembly("release-a");
        SemanticReleaseAssembly releaseB = assembly("release-b");
        SemanticReleaseAssemblyRepository repository =
                new InMemorySemanticReleaseAssemblyRepository(
                        List.of(releaseA, releaseB)
                );

        assertSame(releaseA, repository.release("release-a").orElseThrow());
        assertSame(releaseB, repository.release("release-b").orElseThrow());
        assertTrue(repository.release("release-c").isEmpty());
    }

    @Test
    void repositoryRejectsAmbiguousDuplicateReleaseIdentifiers() {
        SemanticReleaseAssembly first = assembly("release-a");
        SemanticReleaseAssembly second = new SemanticReleaseAssembly(
                semantic("release-a"),
                surface("release-a"),
                fulfilment("release-a"),
                exposure("release-a")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new InMemorySemanticReleaseAssemblyRepository(
                        List.of(first, second)
                )
        );
    }

    @Test
    void repositoryRejectsBlankLookupIdentifiers() {
        SemanticReleaseAssemblyRepository repository =
                new InMemorySemanticReleaseAssemblyRepository(
                        List.of(assembly("release-a"))
                );

        assertThrows(IllegalArgumentException.class, () -> repository.release(" "));
        assertFalse(repository.release("release-missing").isPresent());
    }

    private static SemanticReleaseAssembly assembly(String releaseIdentifier) {
        return new SemanticReleaseAssembly(
                semantic(releaseIdentifier),
                surface(releaseIdentifier),
                fulfilment(releaseIdentifier),
                exposure(releaseIdentifier)
        );
    }

    private static SemanticRegistrySnapshot semantic(String releaseIdentifier) {
        return new SemanticRegistrySnapshot(releaseIdentifier, Set.of());
    }

    private static SurfaceContributionRegistrySnapshot surface(
            String releaseIdentifier
    ) {
        return new SurfaceContributionRegistrySnapshot(
                releaseIdentifier,
                Set.of()
        );
    }

    private static FulfilmentContractRegistrySnapshot fulfilment(
            String releaseIdentifier
    ) {
        return new FulfilmentContractRegistrySnapshot(
                releaseIdentifier,
                Set.of(),
                Set.of()
        );
    }

    private static ExposureElementContractRegistrySnapshot exposure(
            String releaseIdentifier
    ) {
        return new ExposureElementContractRegistrySnapshot(
                releaseIdentifier,
                Set.of()
        );
    }
}
