package mainstreet.application;

import grandrue.background.BackgroundExecutionScope;
import grandrue.background.BackgroundWorkContractAffinity;
import grandrue.background.BackgroundWorkContractDefinition;
import grandrue.background.BackgroundWorkContractIdentity;
import grandrue.background.BackgroundWorkContractRegistrySnapshot;
import grandrue.background.BackgroundWorkTargetReference;
import grandrue.background.OverdueHandling;

import java.util.List;
import java.util.Set;

/**
 * Commercial-owned durable prompt-reconciliation contract for a missing or
 * unacknowledged Standing Free post-commit materialisation.
 */
public final class StandingFreeBackgroundWorkContract {
    public static final BackgroundWorkContractIdentity IDENTITY =
            new BackgroundWorkContractIdentity(
                    "commercial", "standing-free-baseline-reconciliation");
    public static final BackgroundWorkContractAffinity AFFINITY =
            new BackgroundWorkContractAffinity(
                    IDENTITY, "standing-free-background-work@1");
    public static final BackgroundWorkContractDefinition DEFINITION =
            new BackgroundWorkContractDefinition(
                    IDENTITY,
                    "MS-PROT-056-v1.6/7:prompt-reconciliation-materialisation",
                    BackgroundExecutionScope.MERCHANT,
                    "MS-PROT-056-v1.6/6:post-commit-propagation-gap",
                    new BackgroundWorkTargetReference(
                            "commercial",
                            "standing-free.establish-from-merchant-establishment"),
                    "MS-PROT-065-v1.1/54:bounded-scheduled-principal",
                    "MS-PROT-056-v1.6/7:standing-free-source-not-yet-materialised",
                    OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                    Set.of(
                            "MS-PROT-056-v1.6/3:authoritative-establishment",
                            "MS-PROT-056-v1.6/3:merchant-scope",
                            "MS-PROT-056-v1.6/5:committed-baseline-origin"),
                    "MS-PROT-056-v1.6/5:one-baseline-per-establishment",
                    "MS-PROT-056-v1.6/5:resolve-committed-baseline-before-retry",
                    "MS-PROT-065-v1.1/26:resolve-authoritative-outcome",
                    "MS-PROT-056-v1.6/5:committed-baseline-resolves-responsibility",
                    "MS-PROT-065-v1.1/57:establishment-reference-only",
                    "MS-PROT-065-v1.1/33-34:captured-contract-current-eligibility");

    private StandingFreeBackgroundWorkContract() { }

    public static BackgroundWorkContractRegistrySnapshot registry() {
        return new BackgroundWorkContractRegistrySnapshot(
                AFFINITY.semanticRegistryReleaseIdentifier(), List.of(DEFINITION));
    }
}
