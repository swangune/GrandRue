package mainstreet.application;

import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import mainstreet.semantic.event.EventReactionContractAffinity;
import mainstreet.semantic.event.EventReactionContractDefinition;
import mainstreet.semantic.event.EventReactionContractIdentity;
import mainstreet.semantic.event.EventReactionContractRegistrySnapshot;
import mainstreet.semantic.event.EventReactionTargetReference;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The delayed consequence retains the original establishment time and one baseline per merchant.
 * MS-PROT-056 v1.6, designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md,
 * §3 — Standing Free baseline fact; §5 — Idempotency and duplicate delivery.
 */
public final class StandingFreeEventReactionContract {
    private static final String DOWNSTREAM_INTENT_PREFIX = "standing-free-baseline/";

    public static final EventReactionContractIdentity IDENTITY =
            new EventReactionContractIdentity("commercial", "standing-free-from-merchant-establishment");

    public static final EventReactionContractAffinity AFFINITY =
            new EventReactionContractAffinity(
                    IDENTITY,
                    "standing-free-event-reactions@1"
            );

    public static final EventReactionContractDefinition DEFINITION =
            new EventReactionContractDefinition(
                    IDENTITY,
                    MerchantAccountEstablishedEventContract.AFFINITY,
                    "MS-PROT-056-v1.6/1:required-standing-free-consequence",
                    "MS-PROT-056-v1.6/3:merchant-scope",
                    "MS-PROT-056-v1.6/1:durable-post-commit-reaction",
                    new EventReactionTargetReference(
                            "commercial",
                            "standing-free.establish-from-merchant-establishment"
                    ),
                    "MS-PROT-026-v1.1/47:own-bounded-scheduled-principal",
                    "MS-PROT-026-v1.1/23:event-occurrence-plus-reaction-contract",
                    "MS-PROT-056-v1.6/3:original-establishment-time-not-arrival-order",
                    "MS-PROT-056-v1.6/5:duplicates-converge-without-rewriting-history",
                    Set.of(
                            "MS-PROT-056-v1.6/3:merchant-scope",
                            "MS-PROT-056-v1.6/3:originating-establishment-identity",
                            "MS-PROT-056-v1.6/3:original-establishment-time"
                    ),
                    "MS-PROT-056-v1.6/5:resolve-committed-baseline-after-lost-acknowledgement",
                    "MS-PROT-056-v1.6/5:one-baseline-per-merchant-with-exact-origin"
            );

    private StandingFreeEventReactionContract() {
    }

    public static EventReactionContractRegistrySnapshot registry() {
        return new EventReactionContractRegistrySnapshot(
                AFFINITY.semanticRegistryReleaseIdentifier(),
                List.of(DEFINITION)
        );
    }

    public static String downstreamLogicalIntentReference(
            MerchantAccountEstablishedOccurrence occurrence
    ) {
        Objects.requireNonNull(occurrence, "occurrence");
        return DOWNSTREAM_INTENT_PREFIX
                + occurrence.fact().establishmentIdentity();
    }
}