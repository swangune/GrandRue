package mainstreet.onboarding;

/**
 * MS-PROT-039 / MS-PROT-052 merchant interaction shape for an onboarding
 * answer.
 *
 * <p>An answer form describes presentation/collection shape only. It does not
 * create or widen the semantic value types accepted by configuration-owning
 * contracts.</p>
 */
public enum OnboardingAnswerForm {
    SINGLE_SELECT,
    MULTI_SELECT,
    BOOLEAN,
    QUANTITY,
    DURATION,
    TEXT,
    STRUCTURED_VALUE
}
