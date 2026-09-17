package grandrue.businesshours;

import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/** Owner boundary for stable weekly Public Business Hours revisions. */
public interface StandardBusinessHoursAuthority {
    StandardBusinessHoursRevision configure(
            ConfigureStandardBusinessHoursCommand command,
            TrustedExecutionContext trustedContext
    );

    StandardBusinessHoursRevision withdraw(
            WithdrawStandardBusinessHoursCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<StandardBusinessHoursRevision> current(BusinessHoursScope scope);

    Optional<StandardBusinessHoursRevision> revision(String revisionIdentity);
}
