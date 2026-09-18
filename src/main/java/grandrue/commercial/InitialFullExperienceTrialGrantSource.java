package grandrue.commercial;

import mainstreet.commercial.CommercialEntitlementGrant;
import mainstreet.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.InitialFullExperienceTrial;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Resolves one authoritative initial full-experience trial into its commercial
 * entitlement grants.
 *
 * <p>The set of entitlement identities is supplied by commercial product
 * authority; this class does not infer merchant semantics, inspect capability
 * configuration or translate plan names into domain meaning.</p>
 *
 * <p>Governed by MS-PROT-056 v1.4 and v1.5.</p>
 */
public final class InitialFullExperienceTrialGrantSource {

    public List<CommercialEntitlementGrant> grantsFor(
            InitialFullExperienceTrial trial,
            Set<CommercialEntitlementIdentity> entitlementIdentities
    ) {
        Objects.requireNonNull(trial, "trial");
        Set<CommercialEntitlementIdentity> identities = Set.copyOf(
                Objects.requireNonNull(
                        entitlementIdentities,
                        "entitlementIdentities"
                )
        );
        return identities.stream()
                .sorted(java.util.Comparator.comparing(
                        CommercialEntitlementIdentity::identifier
                ))
                .map(identity -> new CommercialEntitlementGrant(
                        trial.merchantScope(),
                        identity,
                        trial.grantProvenance(),
                        trial.startsAt(),
                        Optional.of(trial.expiresAt())
                ))
                .toList();
    }
}
