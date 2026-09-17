package grandrue.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable registry of owner-qualified safe API problem mappings.
 *
 * <p>A delivery adapter selects the governed safe-representation rule declared
 * by its API contract. The registry then enforces exact owner and evidence
 * affinity. It does not translate arbitrary Java exceptions or assign an HTTP
 * status.</p>
 */
public final class ApiProblemMappingRegistrySnapshot {

    private final Map<String, ApiProblemMappingAuthority> byRule;

    public ApiProblemMappingRegistrySnapshot(
            List<ApiProblemMappingAuthority> authorities
    ) {
        Objects.requireNonNull(authorities, "authorities");
        Map<String, ApiProblemMappingAuthority> indexed = new LinkedHashMap<>();
        for (ApiProblemMappingAuthority authority : authorities) {
            Objects.requireNonNull(authority, "authority");
            String ruleReference = requireRuleReference(
                    authority.mappingRuleReference()
            );
            Objects.requireNonNull(
                    authority.ownerContractReference(),
                    "authority.ownerContractReference"
            );
            Class<? extends ApiProblemEvidence> evidenceType =
                    Objects.requireNonNull(
                            authority.evidenceType(),
                            "authority.evidenceType"
                    );
            if (Throwable.class.isAssignableFrom(evidenceType)) {
                throw new IllegalArgumentException(
                        "Java exceptions are not API problem evidence contracts"
                );
            }
            if (indexed.putIfAbsent(ruleReference, authority) != null) {
                throw new IllegalArgumentException(
                        "Duplicate API problem mapping rule: " + ruleReference
                );
            }
        }
        byRule = Map.copyOf(indexed);
    }

    public ApiProblem map(
            ApiContractRegistration registration,
            String mappingRuleReference,
            ApiProblemEvidence evidence
    ) {
        Objects.requireNonNull(registration, "registration");
        String ruleReference = requireRuleReference(mappingRuleReference);
        Objects.requireNonNull(evidence, "evidence");
        if (evidence instanceof Throwable) {
            throw new IllegalArgumentException(
                    "Java exceptions are not API problem evidence contracts"
            );
        }

        ApiProblemMappingAuthority authority = byRule.get(ruleReference);
        if (authority == null) {
            throw new IllegalStateException(
                    "No API problem mapping authority for rule " + ruleReference
            );
        }
        if (!authority.ownerContractReference().equals(
                registration.ownerContractReference()
        )) {
            throw new IllegalStateException(
                    "API problem mapping owner does not match registration"
            );
        }
        if (!authority.evidenceType().equals(evidence.getClass())) {
            throw new IllegalStateException(
                    "API problem evidence does not match mapping authority"
            );
        }

        ApiProblem problem = authority.map(evidence);
        if (problem == null) {
            throw new IllegalStateException(
                    "API problem mapping authority did not return a problem"
            );
        }
        problem.ownerDetail().ifPresent(detail -> {
            if (!detail.ownerContractReference().equals(
                    authority.ownerContractReference()
            )) {
                throw new IllegalStateException(
                        "API problem detail does not match mapping owner"
                );
            }
        });
        return problem;
    }

    private static String requireRuleReference(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "API problem mapping rule reference must not be blank"
            );
        }
        return value;
    }
}
