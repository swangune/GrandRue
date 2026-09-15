package mainstreet.background;

/** Infrastructure scope carried by durable work; never a source of authority. */
public enum BackgroundExecutionScope {
    PLATFORM,
    MERCHANT
}
