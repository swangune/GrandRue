package grandrue.commercial;

import mainstreet.commercial.InitialFullExperienceTrial;

/**
 * Persistence/consistency port for the one automatic initial trial allowed per
 * Merchant Account.
 *
 * <p>The implementation MUST atomically preserve at-most-one initial trial per
 * merchant. If a trial already exists, it returns that authoritative trial
 * rather than creating or extending another one.</p>
 */
@FunctionalInterface
public interface InitialFullExperienceTrialStore {

    InitialFullExperienceTrial establishIfAbsent(
            InitialFullExperienceTrial candidate
    );
}
