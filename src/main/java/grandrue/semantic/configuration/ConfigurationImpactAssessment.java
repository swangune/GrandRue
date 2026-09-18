package grandrue.semantic.configuration;

/**
 * A required business-effect or commitment assessment of the supplied exact result.
 * Implementations must obtain applicable operational facts from their owning authority;
 * unavailable facts must fail assessment, not become an empty finding list.
 * The composition owner is responsible for complete applicable assessment coverage.
 *
 * Authority: designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §21 Impact analysis; §22 Impact classifications; §§23–24 protected commitments and affinity.
 */
@FunctionalInterface
public interface ConfigurationImpactAssessment {
    ConfigurationImpactContribution assess(ConfigurationImpactContext context);
}
