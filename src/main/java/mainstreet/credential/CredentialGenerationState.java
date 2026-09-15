package mainstreet.credential;

/** Operational security state of one physical credential generation. */
public enum CredentialGenerationState {
    USABLE,
    RETIRING,
    EXPIRED,
    REVOKED,
    COMPROMISED;

    public boolean terminal() {
        return this == EXPIRED || this == REVOKED || this == COMPROMISED;
    }
}
