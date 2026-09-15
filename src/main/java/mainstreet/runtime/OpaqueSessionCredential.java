package mainstreet.runtime;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/**
 * ADR-014 opaque bearer credential for human browser sessions.
 *
 * <p>The raw value is intentionally excluded from {@link #toString()}. Durable
 * persistence should retain only {@link #verifier()}.</p>
 */
public final class OpaqueSessionCredential {

    private static final int ENTROPY_BYTES = 32;
    private static final Base64.Encoder ENCODER =
            Base64.getUrlEncoder().withoutPadding();

    private final String value;
    private final String verifier;

    private OpaqueSessionCredential(String value) {
        this.value = require(value, "value");
        this.verifier = verifierForPresentedCredential(value);
    }

    /**
     * Issues a fresh credential from the platform cryptographic random source.
     */
    public static OpaqueSessionCredential generate() {
        return generate(new SecureRandom());
    }

    /**
     * Injection overload for deterministic/security-focused tests.
     */
    public static OpaqueSessionCredential generate(SecureRandom secureRandom) {
        Objects.requireNonNull(secureRandom, "secureRandom");
        byte[] entropy = new byte[ENTROPY_BYTES];
        secureRandom.nextBytes(entropy);
        return new OpaqueSessionCredential(ENCODER.encodeToString(entropy));
    }

    /**
     * Derives the persistence lookup verifier from a bearer credential
     * presented by a caller. This does not establish authentication by itself;
     * the verifier must resolve to a current authoritative Session Record.
     */
    public static String verifierForPresentedCredential(String presentedCredential) {
        String value = require(presentedCredential, "presentedCredential");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.US_ASCII));
            return ENCODER.encodeToString(hashed);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 is unavailable for session credential verification",
                    exception
            );
        }
    }

    /**
     * The bearer secret. Callers must not persist or log this value.
     */
    public String value() {
        return value;
    }

    /**
     * Stable one-way verifier suitable for authoritative session lookup.
     */
    public String verifier() {
        return verifier;
    }

    public boolean matchesVerifier(String candidateVerifier) {
        require(candidateVerifier, "candidateVerifier");
        return MessageDigest.isEqual(
                verifier.getBytes(StandardCharsets.US_ASCII),
                candidateVerifier.getBytes(StandardCharsets.US_ASCII)
        );
    }

    @Override
    public String toString() {
        return "OpaqueSessionCredential[REDACTED]";
    }

    private static String require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }
}
