package mainstreet.booking;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.ConfigurationImpactAssessment;
import mainstreet.semantic.configuration.ConfigurationImpactClassification;
import mainstreet.semantic.configuration.ConfigurationImpactContext;
import mainstreet.semantic.configuration.ConfigurationImpactContribution;
import mainstreet.semantic.configuration.ConfigurationImpactFinding;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Assesses Booking activation membership and residual management only.
 * Policy, scheduling and other capability consequences require their own assessments.
 *
 * Authority: designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §§20–24 impact and protected commitments; §26 Capability deactivation;
 * §56 Booking capability deactivation validation;
 * designs/MS-PROT-042 v1.4 — Booking Residual Obligation & Discharge Amendment.md,
 * §§1–5 Booking-owned obligations and discharge.
 */
public final class BookingAvailabilityImpactAssessment implements ConfigurationImpactAssessment {
    private final ConfigurationCompiler compiler;
    private final BookingResidualObligationAuthority obligations;

    public BookingAvailabilityImpactAssessment(ConfigurationCompiler compiler,
                                               BookingResidualObligationAuthority obligations) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
        this.obligations = Objects.requireNonNull(obligations, "obligations");
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        // Historical dependency closure can differ even when direct selections are unchanged.
        boolean previouslyEnabled = context.base().map(compiler::compile)
                .map(model -> model.capabilityIdentifiers().contains("booking")).orElse(false);
        boolean proposedEnabled = context.resolvedPackage().executableSemanticModel()
                .capabilityIdentifiers().contains("booking");
        var effects = new ArrayList<String>();
        var findings = new ArrayList<ConfigurationImpactFinding>();
        if (previouslyEnabled != proposedEnabled) {
            String effect = proposedEnabled
                    ? "New bookings will be enabled, subject to the configured booking policies."
                    : "New bookings will no longer be enabled.";
            effects.add(effect);
            findings.add(new ConfigurationImpactFinding(ConfigurationImpactClassification.CONSEQUENTIAL, effect));
        }
        if (!proposedEnabled && obligations.hasOutstandingBookingObligation(
                new MerchantScope(context.candidate().merchantIdentifier()))) {
            findings.add(new ConfigurationImpactFinding(ConfigurationImpactClassification.INFORMATIONAL,
                    "Some existing bookings still require management; disabling new bookings does not cancel or discharge them."));
        }
        return new ConfigurationImpactContribution(effects, findings);
    }
}
