package mainstreet.fulfilment;

/** Exact immutable identity of one merchant fulfilment-binding-set revision. */
public record FulfilmentBindingSetRevisionReference(
        String bindingSetIdentifier,
        long revision
) {
    public FulfilmentBindingSetRevisionReference {
        if (bindingSetIdentifier == null || bindingSetIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Fulfilment binding-set identifier must not be blank"
            );
        }
        if (revision < 1) {
            throw new IllegalArgumentException(
                    "Fulfilment binding-set revision must be positive"
            );
        }
    }

    public String provenanceIdentifier() {
        return "fulfilment-binding-set:" + bindingSetIdentifier + "@" + revision;
    }
}
