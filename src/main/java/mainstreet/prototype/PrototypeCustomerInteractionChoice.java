package mainstreet.prototype;

/**
 * Prototype representation of the stable customer-interaction discovery
 * option identities accepted by MS-PROT-052 v1.1.
 *
 * <p>These are onboarding evidence identities, not capability identities.</p>
 */
public enum PrototypeCustomerInteractionChoice {
    PUBLISH_INFORMATION,
    SEND_ENQUIRY,
    ARRANGE_APPOINTMENT,
    RESERVE_SUBJECT,
    PLACE_ORDER,
    SUBSCRIBE_UPDATES,
    NOTHING_ELSE_FOR_NOW,
    OTHER
}
