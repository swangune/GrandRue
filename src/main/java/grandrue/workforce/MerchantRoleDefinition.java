package grandrue.workforce;

import grandrue.application.MerchantScope;
import mainstreet.semantic.Privilege;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;

import java.util.Objects;
import java.util.Set;

/**
 * Merchant-scoped reusable access profile composed only from privileges already
 * registered by Main Street semantic authority.
 */
public final class MerchantRoleDefinition {

    private final String roleIdentifier;
    private final MerchantScope merchantScope;
    private final String displayName;
    private final Set<Privilege> privileges;

    private MerchantRoleDefinition(
            String roleIdentifier,
            MerchantScope merchantScope,
            String displayName,
            Set<Privilege> privileges
    ) {
        if (roleIdentifier == null || roleIdentifier.isBlank()) {
            throw new IllegalArgumentException("Role identifier must not be blank");
        }
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Role display name must not be blank");
        }
        this.roleIdentifier = roleIdentifier;
        this.displayName = displayName;
        this.privileges = Set.copyOf(Objects.requireNonNull(privileges, "privileges"));
    }

    public static MerchantRoleDefinition define(
            String roleIdentifier,
            MerchantScope merchantScope,
            String displayName,
            Set<Privilege> privileges,
            SemanticRegistrySnapshot registry
    ) {
        Objects.requireNonNull(registry, "registry");
        Set<Privilege> copied = Set.copyOf(
                Objects.requireNonNull(privileges, "privileges")
        );
        for (Privilege privilege : copied) {
            boolean registered = registry.capabilities().stream()
                    .flatMap(capability -> capability.operations().stream())
                    .map(operation -> operation.requiredPrivilegeIdentifier())
                    .anyMatch(privilege.identifier()::equals);
            if (!registered) {
                throw new IllegalArgumentException(
                        "Role references an unregistered privilege: "
                                + privilege.identifier()
                );
            }
        }
        return new MerchantRoleDefinition(
                roleIdentifier,
                merchantScope,
                displayName,
                copied
        );
    }

    public String roleIdentifier() {
        return roleIdentifier;
    }

    public MerchantScope merchantScope() {
        return merchantScope;
    }

    public String displayName() {
        return displayName;
    }

    public Set<Privilege> privileges() {
        return privileges;
    }
}
