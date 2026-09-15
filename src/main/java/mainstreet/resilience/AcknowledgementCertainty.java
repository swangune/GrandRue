package mainstreet.resilience;

/** Whether the initiating caller is known to have received the resolved outcome. */
public enum AcknowledgementCertainty {
    PENDING,
    ACKNOWLEDGED,
    LOST,
    NOT_APPLICABLE
}
