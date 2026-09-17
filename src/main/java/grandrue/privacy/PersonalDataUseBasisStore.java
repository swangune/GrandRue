package grandrue.privacy;

import java.time.Instant;
import java.util.Optional;

/** Durable authority boundary for personal-data-use evidence. */
public interface PersonalDataUseBasisStore extends PersonalDataUseBasisAuthority {

    PersonalDataUseBasis establish(PersonalDataUseBasis basis);

    PersonalDataUseBasis cease(String basisIdentity, Instant effectiveUntilExclusive);

    Optional<PersonalDataUseBasis> basis(String basisIdentity);
}
