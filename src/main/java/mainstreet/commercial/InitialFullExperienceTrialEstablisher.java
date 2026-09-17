package mainstreet.commercial;

import grandrue.commercial.FirstConfigurationActivationAuthority;
import grandrue.commercial.InvalidInitialFullExperienceTrialOriginException;

import java.util.Objects;

/**
 * Commercial authority for idempotently establishing the one automatic
 * initial full-experience trial permitted for a Merchant Account.
 *
 * <p>Configuration remains authoritative for whether the supplied origin is
 * the merchant's first committed configuration activation. The store remains
 * responsible for the atomic at-most-one persistence invariant.</p>
 *
 * <p>Governed by MS-PROT-056 v1.4.</p>
 */
public final class InitialFullExperienceTrialEstablisher {

    private final FirstConfigurationActivationAuthority activationAuthority;
    private final InitialFullExperienceTrialStore trialStore;

    public InitialFullExperienceTrialEstablisher(
            FirstConfigurationActivationAuthority activationAuthority,
            InitialFullExperienceTrialStore trialStore
    ) {
        this.activationAuthority = Objects.requireNonNull(
                activationAuthority,
                "activationAuthority"
        );
        this.trialStore = Objects.requireNonNull(trialStore, "trialStore");
    }

    public InitialFullExperienceTrial establish(
            InitialFullExperienceTrial candidate
    ) {
        Objects.requireNonNull(candidate, "candidate");

        boolean validOrigin = activationAuthority.isFirstCommittedActivation(
                candidate.merchantScope(),
                candidate.originConfigurationRevisionIdentifier(),
                candidate.originatingFirstActivationIdentity(),
                candidate.startsAt()
        );
        if (!validOrigin) {
            throw new InvalidInitialFullExperienceTrialOriginException();
        }

        return Objects.requireNonNull(
                trialStore.establishIfAbsent(candidate),
                "trialStore result"
        );
    }
}
