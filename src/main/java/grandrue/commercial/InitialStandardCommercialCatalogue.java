package grandrue.commercial;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Exact approved initial standard Commercial Catalogue Manifest.
 *
 * <p>MS-PROT-056 v1.10 — Initial Standard Commercial Catalogue Manifest.
 * Construction is immutable policy representation only. It does not publish the
 * catalogue, activate a capability, establish a merchant agreement or grant
 * runtime access.</p>
 */
public final class InitialStandardCommercialCatalogue {
    private static final String CATALOGUE = "standard-commercial-catalogue@1";
    private static final String FREE_PLAN = "standard-plan/free@1";
    private static final String BUSINESS_PLAN = "standard-plan/business@1";
    private static final String GROWTH_PLAN = "standard-plan/growth@1";
    private static final String ENTITLEMENT_PREFIX = "commercial-entitlement/";
    private static final String APPROVAL =
            "MS-PROT-056 v1.10 accepted 2026-09-19; formalisation commit "
                    + "a1f4ad04c211b1d8098b2fb3f4e6bcc5871f3e94";

    private InitialStandardCommercialCatalogue() {
    }

    public static CommercialCatalogueManifest manifest() {
        var bindings = new LinkedHashSet<CommercialAccessBinding>();
        var taggedBindings = new LinkedHashSet<TaggedBinding>();

        add(taggedBindings, bindings, free(
                "profile/merchant-presence-authoring",
                "profile/merchant-presence-authoring-access@1",
                "MAINTAIN_MERCHANT_PRESENCE",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-051 v1.6"));
        add(taggedBindings, bindings, free(
                "business-hours/standard-authoring",
                "business-hours/standard-authoring-access@1",
                "MAINTAIN_MERCHANT_PRESENCE",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-050 v1.6"));
        add(taggedBindings, bindings, free(
                "business-hours/dated-override-maintenance",
                "business-hours/dated-override-maintenance-access@1",
                "MAINTAIN_MERCHANT_PRESENCE",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-050 v1.6"));
        add(taggedBindings, bindings, free(
                "storefront/composition-publication",
                "storefront/composition-publication-access@1",
                "PUBLISH_STOREFRONT_COMPOSITION",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-036 v1.4"));

        var websiteNamespaceSupport = new CommercialConditionalSupportingAccessRequirement(
                "MS-PROT-036 v1.3 + MS-PROT-088 v1.1 — actual Website Hostname Binding namespace family",
                Set.of(
                        alternative(
                                "PLATFORM_DELEGATED_NAMESPACE",
                                required(
                                        "merchant-brand-infrastructure/platform-website-namespace-use@1",
                                        "USE_PLATFORM_WEBSITE_NAMESPACE")),
                        alternative(
                                "MERCHANT_CONTROLLED_DOMAIN",
                                required(
                                        "merchant-brand-infrastructure/custom-website-domain-use@1",
                                        "USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN"))),
                "MS-PROT-056 v1.10 §12");
        add(taggedBindings, bindings, free(
                "storefront/website-delivery",
                "storefront/website-delivery-access@1",
                "SERVE_CUSTOMER_WEBSITE",
                CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS,
                "MS-PROT-036 v1.3",
                Set.of(websiteNamespaceSupport)));

        add(taggedBindings, bindings, free(
                "merchant-brand-infrastructure/platform-website-namespace-use",
                "merchant-brand-infrastructure/platform-website-namespace-use@1",
                "USE_PLATFORM_WEBSITE_NAMESPACE",
                CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS,
                "MS-PROT-088 v1.1"));
        add(taggedBindings, bindings, free(
                "offering/merchant-definition-authoring",
                "offering/merchant-definition-authoring-access@1",
                "MAINTAIN_MERCHANT_OFFERING_DEFINITION",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-044 v1.3"));
        add(taggedBindings, bindings, free(
                "product/merchant-definition-authoring",
                "product/merchant-definition-authoring-access@1",
                "MAINTAIN_MERCHANT_PRODUCT_DEFINITION",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-044 v1.3"));
        add(taggedBindings, bindings, free(
                "listing/merchant-definition-authoring",
                "listing/merchant-definition-authoring-access@1",
                "MAINTAIN_MERCHANT_LISTING_DEFINITION",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-044 v1.6"));
        add(taggedBindings, bindings, free(
                "publication/merchant-authoring",
                "publication/merchant-authoring-access@1",
                "AUTHOR_AND_PUBLISH_INFORMATION",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-046 v1.5"));
        add(taggedBindings, bindings, free(
                "enquiry/general-submission",
                "enquiry/general-submission-access@1",
                "ORIGINATE_ENQUIRY",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-043 v1.5"));
        add(taggedBindings, bindings, free(
                "enquiry/opportunity-submission",
                "enquiry/opportunity-submission-access@1",
                "ORIGINATE_ENQUIRY",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-043 v1.5"));
        add(taggedBindings, bindings, free(
                "enquiry/merchant-observation",
                "enquiry/merchant-observation-access@1",
                "OBSERVE_ENQUIRY",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-043 v1.5"));

        add(taggedBindings, bindings, business(
                "merchant-brand-infrastructure/custom-website-domain-use",
                "merchant-brand-infrastructure/custom-website-domain-use@1",
                "USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN",
                CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS,
                "MS-PROT-088 v1.1"));
        add(taggedBindings, bindings, business(
                "booking/reservation-commitment-establishment",
                "booking/reservation-commitment-establishment-access@1",
                "ESTABLISH_BOOKING_RESERVATION_COMMITMENT",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-042 v1.16"));
        add(taggedBindings, bindings, business(
                "appointment/time-commitment-establishment",
                "appointment/time-commitment-establishment-access@1",
                "ESTABLISH_APPOINTMENT_TIME_COMMITMENT",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-042 v1.16"));
        add(taggedBindings, bindings, business(
                "ordering/purchase-commitment-establishment",
                "ordering/purchase-commitment-establishment-access@1",
                "ESTABLISH_ORDER_PURCHASE_COMMITMENT",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-077 v1.2"));
        add(taggedBindings, bindings, business(
                "payment/customer-obligation-establishment",
                "payment/customer-obligation-establishment-access@1",
                "ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-055 v1.2"));
        add(taggedBindings, bindings, business(
                "inventory/stock-administration",
                "inventory/stock-administration-access@1",
                "ADMINISTER_INVENTORY_STOCK",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-058 v1.3"));
        add(taggedBindings, bindings, business(
                "workforce-scheduling/arrangement-authoring",
                "workforce-scheduling/arrangement-authoring-access@1",
                "MAINTAIN_WORKFORCE_SCHEDULING_TERMS",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-081 v1.4"));
        add(taggedBindings, bindings, business(
                "workforce-scheduling/work-planning-authoring",
                "workforce-scheduling/work-planning-authoring-access@1",
                "PLAN_WORKFORCE_SCHEDULE",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-081 v1.4"));
        add(taggedBindings, bindings, business(
                "workforce-scheduling/shift-offer-acceptance",
                "workforce-scheduling/shift-offer-acceptance-access@1",
                "COMMIT_OFFERED_WORK",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-081 v1.4"));
        add(taggedBindings, bindings, business(
                "workforce-scheduling/leave-request-origination",
                "workforce-scheduling/leave-request-origination-access@1",
                "REQUEST_WORKFORCE_LEAVE",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-081 v1.4"));
        add(taggedBindings, bindings, business(
                "workforce-compensation/terms-authoring",
                "workforce-compensation/terms-authoring-access@1",
                "MAINTAIN_WORKFORCE_COMPENSATION_TERMS",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-080 v1.4"));
        add(taggedBindings, bindings, business(
                "workforce-compensation/compensation-administration",
                "workforce-compensation/compensation-administration-access@1",
                "ADMINISTER_WORKFORCE_COMPENSATION",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-080 v1.4"));
        add(taggedBindings, bindings, business(
                "workforce-rota/authoring",
                "workforce-rota/authoring-access@1",
                "PLAN_WORKFORCE_SCHEDULE",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-091 v1.1"));
        add(taggedBindings, bindings, business(
                "workforce-rota/open-selection-participation",
                "workforce-rota/open-selection-participation-access@1",
                "PARTICIPATE_IN_OPEN_ROTA_SELECTION",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-091 v1.1"));
        add(taggedBindings, bindings, business(
                "workforce-rota/schedule-exclusion-origination",
                "workforce-rota/schedule-exclusion-origination-access@1",
                "DECLARE_OPERATIONAL_SCHEDULING_UNAVAILABILITY",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-091 v1.1"));
        add(taggedBindings, bindings, business(
                "financial-operations/record-establishment",
                "financial-operations/record-establishment-access@1",
                "ESTABLISH_FINANCIAL_OPERATIONS_RECORD",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-084 v1.2"));
        add(taggedBindings, bindings, business(
                "business-intelligence/business-analytics-evaluation",
                "business-intelligence/business-analytics-evaluation-access@1",
                "USE_BUSINESS_ANALYTICS",
                CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS,
                "MS-PROT-083 v1.7"));
        add(taggedBindings, bindings, business(
                "customer-communication/message-participation",
                "customer-communication/message-participation-access@1",
                "CONDUCT_CUSTOMER_COMMUNICATION",
                CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS,
                "MS-PROT-086 v1.4"));

        var automatedResponseSupport = new CommercialConditionalSupportingAccessRequirement(
                "MS-PROT-086 v1.4 §§11–12 — whether automated response becomes new ConversationMessage activity",
                Set.of(
                        alternative("NO_NEW_CONVERSATION_MESSAGE"),
                        alternative(
                                "NEW_CONVERSATION_MESSAGE",
                                required(
                                        "customer-communication/message-participation-access@1",
                                        "CONDUCT_CUSTOMER_COMMUNICATION"))),
                "MS-PROT-056 v1.10 §13");
        add(taggedBindings, bindings, business(
                "customer-communication/automated-response",
                "customer-communication/automated-response-access@1",
                "AUTOMATE_ROUTINE_CUSTOMER_SERVICE",
                CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS,
                "MS-PROT-086 v1.4",
                Set.of(automatedResponseSupport)));

        add(taggedBindings, bindings, business(
                "quotation/commercial-offer-issuance",
                "quotation/commercial-offer-issuance-access@1",
                "ISSUE_QUOTATION_COMMERCIAL_OFFER",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-095 v1.0"));

        var invoicePaymentSupport = new CommercialConditionalSupportingAccessRequirement(
                "MS-PROT-096 §§23–24 — Invoice payment-obligation relationship mode",
                Set.of(
                        alternative("EXISTING_OBLIGATION"),
                        alternative(
                                "INVOICE_ESTABLISHES_OBLIGATION",
                                required(
                                        "payment/customer-obligation-establishment-access@1",
                                        "ESTABLISH_CUSTOMER_PAYMENT_OBLIGATION"))),
                "MS-PROT-056 v1.10 §14");
        add(taggedBindings, bindings, business(
                "invoicing/invoice-issuance",
                "invoicing/invoice-issuance-access@1",
                "ISSUE_CUSTOMER_INVOICE",
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "MS-PROT-096 v1.0",
                Set.of(invoicePaymentSupport)));

        add(taggedBindings, bindings, growth(
                "business-intelligence/campaign-analytics-evaluation",
                "business-intelligence/campaign-analytics-evaluation-access@1",
                "USE_CAMPAIGN_ANALYTICS",
                CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS,
                "MS-PROT-083 v1.7"));

        var marketingPublicationSupport = new CommercialConditionalSupportingAccessRequirement(
                "MS-PROT-087 + MS-PROT-046 — accepted Campaign outreach family",
                Set.of(
                        alternative("DIRECT_EMAIL_MARKETING_V1"),
                        alternative(
                                "WEBSITE_ANNOUNCEMENT_V1",
                                required(
                                        "publication/merchant-authoring-access@1",
                                        "AUTHOR_AND_PUBLISH_INFORMATION"))),
                "MS-PROT-056 v1.10 §16");
        add(taggedBindings, bindings, growth(
                "marketing/campaign-service",
                "marketing/campaign-service-access@1",
                "CONDUCT_MARKETING_CAMPAIGNS",
                CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS,
                "MS-PROT-087 v1.4",
                Set.of(marketingPublicationSupport)));

        Set<CommercialEntitlementIdentity> free = identities(taggedBindings, Tier.FREE);
        Set<CommercialEntitlementIdentity> business = union(
                free,
                identities(taggedBindings, Tier.BUSINESS));
        Set<CommercialEntitlementIdentity> growth = union(
                business,
                identities(taggedBindings, Tier.GROWTH));

        var revision = new StandardPlanCatalogueRevision(
                CATALOGUE,
                new StandardPlanRevision(StandardPlanLevel.FREE, FREE_PLAN, free),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, BUSINESS_PLAN, business),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, GROWTH_PLAN, growth));

        return new CommercialCatalogueManifest(
                revision,
                Set.copyOf(bindings),
                Set.of("MS-PROT-056 v1.7", "MS-PROT-056 v1.8", "MS-PROT-056 v1.10"),
                APPROVAL);
    }

    private static TaggedBinding free(
            String entitlement,
            String contract,
            String purpose,
            CommercialEntitlementTargetKind kind,
            String authority
    ) {
        return tagged(Tier.FREE, entitlement, contract, purpose, kind, authority, Set.of());
    }

    private static TaggedBinding free(
            String entitlement,
            String contract,
            String purpose,
            CommercialEntitlementTargetKind kind,
            String authority,
            Set<CommercialConditionalSupportingAccessRequirement> conditional
    ) {
        return tagged(Tier.FREE, entitlement, contract, purpose, kind, authority, conditional);
    }

    private static TaggedBinding business(
            String entitlement,
            String contract,
            String purpose,
            CommercialEntitlementTargetKind kind,
            String authority
    ) {
        return tagged(Tier.BUSINESS, entitlement, contract, purpose, kind, authority, Set.of());
    }

    private static TaggedBinding business(
            String entitlement,
            String contract,
            String purpose,
            CommercialEntitlementTargetKind kind,
            String authority,
            Set<CommercialConditionalSupportingAccessRequirement> conditional
    ) {
        return tagged(Tier.BUSINESS, entitlement, contract, purpose, kind, authority, conditional);
    }

    private static TaggedBinding growth(
            String entitlement,
            String contract,
            String purpose,
            CommercialEntitlementTargetKind kind,
            String authority
    ) {
        return tagged(Tier.GROWTH, entitlement, contract, purpose, kind, authority, Set.of());
    }

    private static TaggedBinding growth(
            String entitlement,
            String contract,
            String purpose,
            CommercialEntitlementTargetKind kind,
            String authority,
            Set<CommercialConditionalSupportingAccessRequirement> conditional
    ) {
        return tagged(Tier.GROWTH, entitlement, contract, purpose, kind, authority, conditional);
    }

    private static TaggedBinding tagged(
            Tier tier,
            String entitlement,
            String contract,
            String purpose,
            CommercialEntitlementTargetKind kind,
            String authority,
            Set<CommercialConditionalSupportingAccessRequirement> conditional
    ) {
        var binding = new CommercialAccessBinding(
                new CommercialEntitlementIdentity(ENTITLEMENT_PREFIX + entitlement + "@1"),
                kind,
                target(contract),
                purpose,
                authority,
                Set.of(),
                conditional,
                authority);
        return new TaggedBinding(tier, binding);
    }

    private static void add(
            Set<TaggedBinding> taggedBindings,
            Set<CommercialAccessBinding> bindings,
            TaggedBinding tagged
    ) {
        if (!bindings.add(tagged.binding()) || !taggedBindings.add(tagged)) {
            throw new IllegalStateException(
                    "Duplicate initial catalogue binding: "
                            + tagged.binding().entitlementIdentity().identifier());
        }
    }

    private static Set<CommercialEntitlementIdentity> identities(
            Set<TaggedBinding> taggedBindings,
            Tier tier
    ) {
        Set<CommercialEntitlementIdentity> result = new HashSet<>();
        for (var tagged : taggedBindings) {
            if (tagged.tier() == tier) {
                result.add(tagged.binding().entitlementIdentity());
            }
        }
        return Set.copyOf(result);
    }

    private static Set<CommercialEntitlementIdentity> union(
            Set<CommercialEntitlementIdentity> left,
            Set<CommercialEntitlementIdentity> right
    ) {
        Set<CommercialEntitlementIdentity> result = new HashSet<>(left);
        result.addAll(right);
        return Set.copyOf(result);
    }

    private static CommercialConditionalSupportAlternative alternative(
            String conditionValue,
            CommercialRequiredPurpose... required
    ) {
        return new CommercialConditionalSupportAlternative(conditionValue, Set.of(required));
    }

    private static CommercialRequiredPurpose required(String contract, String purpose) {
        return new CommercialRequiredPurpose(target(contract), purpose);
    }

    private static CommercialAccessTarget target(String exactContract) {
        int slash = exactContract.indexOf('/');
        int at = exactContract.lastIndexOf('@');
        if (slash <= 0 || at <= slash + 1 || at == exactContract.length() - 1) {
            throw new IllegalArgumentException("Invalid owner-qualified access contract: " + exactContract);
        }
        return new CommercialAccessTarget(
                exactContract.substring(0, slash),
                exactContract.substring(slash + 1, at),
                exactContract.substring(at + 1));
    }

    private enum Tier {
        FREE,
        BUSINESS,
        GROWTH
    }

    private record TaggedBinding(Tier tier, CommercialAccessBinding binding) {
    }
}
