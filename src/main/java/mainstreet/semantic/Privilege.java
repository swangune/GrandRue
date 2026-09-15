package mainstreet.semantic;

import java.util.Objects;

public final class Privilege {

    private final String identifier;

    public Privilege(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Privilege identifier must not be blank"
            );
        }

        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Privilege privilege)) {
            return false;
        }

        return identifier.equals(privilege.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}