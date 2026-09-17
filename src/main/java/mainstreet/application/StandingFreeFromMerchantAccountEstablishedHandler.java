package mainstreet.application;

import mainstreet.commercial.FreePlanRevisionAuthority;
import mainstreet.commercial.StandardPlanLevel;
import mainstreet.commercial.StandardPlanRevision;
import mainstreet.commercial.StandingFreeBaseline;
import mainstreet.commercial.StandingFreeBaselineStore;
import grandrue.merchantaccount.MerchantAccountEstablished;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Committed recovery must retain original history even when catalogue access is unavailable.
 * MS-PROT-056 v1.6,
 * designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md,
 * §3 — Standing Free baseline fact; §5 — Idempotency and duplicate delivery.
 */
public final class StandingFreeFromMerchantAccountEstablishedHandler {

    private final FreePlanRevisionAuthority freePlanRevisionAuthority;
    private final StandingFreeBaselineStore baselineStore;
    private final Function<MerchantAccountEstablished, String> baselineIdentityFactory;

    public StandingFreeFromMerchantAccountEstablishedHandler(
            FreePlanRevisionAuthority freePlanRevisionAuthority,
            StandingFreeBaselineStore baselineStore,
            Function<MerchantAccountEstablished, String> baselineIdentityFactory) {
        this.freePlanRevisionAuthority = Objects.requireNonNull(
                freePlanRevisionAuthority,
                "freePlanRevisionAuthority"
        );
        this.baselineStore = Objects.requireNonNull(baselineStore, "baselineStore");
        this.baselineIdentityFactory = Objects.requireNonNull(
                baselineIdentityFactory,
                "baselineIdentityFactory"
        );
    }

    /**
     * Resolves only already-committed Commercial owner evidence for restart recovery.
     * MS-PROT-056 v1.6 §5; MS-PROT-065 v1.1 §§25-27.
     */
    public Optional<StandingFreeBaseline> committedBaseline(
            MerchantAccountEstablished event) {
        Objects.requireNonNull(event, "event");
        return baselineStore.baselineFor(event.merchantScope())
                .map(baseline -> requireSameEstablishment(baseline, event));
    }

    public StandingFreeBaseline handle(MerchantAccountEstablished event) {
        Objects.requireNonNull(event, "event");
        var committed = committedBaseline(event);
        if (committed.isPresent()) {
            return committed.orElseThrow();
        }

        StandardPlanRevision freeRevision = Objects.requireNonNull(
                freePlanRevisionAuthority.effectiveFreePlanRevisionAt(event.occurredAt()),
                "FREE plan revision authority returned null"
        );
        if (freeRevision.level() != StandardPlanLevel.FREE) {
            throw new IllegalStateException(
                    "Standing Free requires the FREE plan revision effective at establishment"
            );
        }

        StandingFreeBaseline candidate = new StandingFreeBaseline(
                requireBaselineIdentity(baselineIdentityFactory.apply(event)),
                event.merchantScope(),
                event.establishmentIdentity(),
                event.occurredAt(),
                freeRevision.revisionIdentifier(),
                freeRevision.entitlements()
        );
        return requireSameEstablishment(baselineStore.establishIfAbsent(candidate), event);
    }

    private static StandingFreeBaseline requireSameEstablishment(
            StandingFreeBaseline baseline, MerchantAccountEstablished event) {
        Objects.requireNonNull(baseline, "baseline");
        if (!baseline.merchantScope().equals(event.merchantScope())
                || !baseline.originatingMerchantAccountEstablishmentIdentity().equals(event.establishmentIdentity())
                || !baseline.effectiveFrom().equals(event.occurredAt())) {
            throw new IllegalStateException("Committed Standing Free baseline belongs to a different establishment");
        }
        return baseline;
    }

    private static String requireBaselineIdentity(String identity) {
        if (identity == null || identity.isBlank()) {
            throw new IllegalArgumentException(
                    "Standing Free baseline identity factory must return a non-blank identity"
            );
        }
        return identity;
    }
}
