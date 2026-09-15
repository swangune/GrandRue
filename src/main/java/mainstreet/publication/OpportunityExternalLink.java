package mainstreet.publication;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/** Role-qualified external navigation fact within one Opportunity material revision. */
public record OpportunityExternalLink(
        OpportunityExternalLinkRole role,
        URI uri,
        Optional<String> label
) {
    public OpportunityExternalLink {
        Objects.requireNonNull(role, "role");
        Objects.requireNonNull(uri, "uri");
        Objects.requireNonNull(label, "label");
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("Opportunity external link URI must be absolute");
        }
    }
}
