package mainstreet.credential;

/** Credential context scope; platform credentials never require a fake merchant. */
public enum CredentialBindingScope {
    PLATFORM,
    MERCHANT
}
