package mainstreet.enquiry.delivery;

import grandrue.application.MerchantScope;
import mainstreet.enquiry.EnquiryRevisionProvenance;
import mainstreet.semantic.registry.OwnedOperationalObjectTypeReference;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

/**
 * Opaque, purpose-bound transport locator for exact selected material. It grants no authority.
 * Deployment supplies persistent AES-256 keys and retains old decoding keys for supported retries.
 * There is no invented expiry, default key, current-revision lookup or receipt encoded here.
 */
public final class OpportunityEnquiryBindingCodec {
    private final String active;
    private final Map<String, SecretKeySpec> keys;
    private final SecureRandom random = new SecureRandom();

    public OpportunityEnquiryBindingCodec(String active, Map<String, byte[]> keys) {
        var copy = new HashMap<String, SecretKeySpec>();
        keys.forEach((id, key) -> {
            if (id == null || !id.matches("[A-Za-z0-9_-]{1,40}") || key == null || key.length != 32)
                throw new IllegalArgumentException("A named AES-256 binding key is required");
            copy.put(id, new SecretKeySpec(key.clone(), "AES"));
        });
        if (!copy.containsKey(active)) throw new IllegalArgumentException("Active binding key is absent");
        this.active = active;
        this.keys = Map.copyOf(copy);
    }

    String encode(MerchantScope scope, EnquiryRevisionProvenance revision) {
        try {
            var bytes = new ByteArrayOutputStream();
            try (var out = new DataOutputStream(bytes)) {
                write(out, scope.merchantIdentifier());
                write(out, revision.subjectIdentity());
                write(out, revision.revisionIdentity());
            }
            byte[] nonce = new byte[12];
            random.nextBytes(nonce);
            var cipher = cipher(Cipher.ENCRYPT_MODE, active, nonce);
            var encrypted = cipher.doFinal(bytes.toByteArray());
            var value = "v1." + active + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(
                    java.nio.ByteBuffer.allocate(nonce.length + encrypted.length).put(nonce).put(encrypted).array());
            if (value.length() > 32768) throw new IllegalArgumentException("Binding identities exceed transport bounds");
            return value;
        } catch (IOException | GeneralSecurityException failure) {
            throw new IllegalStateException("Binding locator encoding unavailable", failure);
        }
    }

    EnquiryRevisionProvenance decode(MerchantScope scope, String value) {
        try {
            if (value == null || value.length() > 32768) throw new IllegalArgumentException();
            var parts = value.split("\\.", -1);
            if (parts.length != 3 || !parts[0].equals("v1") || !keys.containsKey(parts[1]))
                throw new IllegalArgumentException();
            var bytes = Base64.getUrlDecoder().decode(parts[2]);
            if (bytes.length < 29) throw new IllegalArgumentException();
            var cipher = cipher(Cipher.DECRYPT_MODE, parts[1], Arrays.copyOf(bytes, 12));
            try (var in = new DataInputStream(new ByteArrayInputStream(cipher.doFinal(bytes, 12, bytes.length - 12)))) {
                var merchant = in.readUTF();
                var subject = in.readUTF();
                var revision = in.readUTF();
                if (!scope.merchantIdentifier().equals(merchant) || in.available() != 0)
                    throw new IllegalArgumentException();
                return new EnquiryRevisionProvenance(new OwnedOperationalObjectTypeReference(
                        "publication", "opportunity"), subject, revision);
            }
        } catch (IOException | GeneralSecurityException | IllegalArgumentException invalid) {
            throw new IllegalArgumentException("Invalid Opportunity enquiry binding");
        }
    }

    private Cipher cipher(int mode, String id, byte[] nonce) throws GeneralSecurityException {
        var cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(mode, keys.get(id), new GCMParameterSpec(128, nonce));
        cipher.updateAAD(("mainstreet/enquiry/opportunity-binding/v1/" + id).getBytes(StandardCharsets.UTF_8));
        return cipher;
    }

    private static void write(DataOutputStream out, String value) throws IOException {
        if (value.isBlank() || value.length() > 4096) throw new IllegalArgumentException("Binding identity too long");
        out.writeUTF(value);
    }
}
