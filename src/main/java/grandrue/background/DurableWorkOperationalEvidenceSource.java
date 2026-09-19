package grandrue.background;

import java.util.List;

/**
 * Read-only operational evidence over durable work progression.
 *
 * <p>Observation through this port cannot claim, retry, finalise or otherwise
 * mutate work. It exposes bounded progression evidence only.</p>
 */
public interface DurableWorkOperationalEvidenceSource {

    List<DurableWorkProgressEvidence> outstanding(
            BackgroundWorkContractAffinity contractAffinity,
            int limit
    );
}
