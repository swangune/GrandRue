package grandrue.onboarding;

/** MS-PROT-052 v1.2 initial Onboarding Case lifecycle. */
public enum OnboardingCaseLifecycle {
    IN_PROGRESS(false),
    SUBMITTED(false),
    COMPLETED(true),
    ABANDONED(true);

    private final boolean terminal;

    OnboardingCaseLifecycle(boolean terminal) {
        this.terminal = terminal;
    }

    public boolean isTerminal() {
        return terminal;
    }
}
