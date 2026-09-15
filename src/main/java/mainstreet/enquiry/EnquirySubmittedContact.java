package mainstreet.enquiry;

import java.util.Objects;
import java.util.Optional;

/** Original customer-supplied values, never authentication or identity-reconciliation evidence. */
public record EnquirySubmittedContact(
        Optional<String> name,
        Optional<String> email,
        Optional<String> telephone
) {
    public EnquirySubmittedContact {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(telephone, "telephone");
    }
}
