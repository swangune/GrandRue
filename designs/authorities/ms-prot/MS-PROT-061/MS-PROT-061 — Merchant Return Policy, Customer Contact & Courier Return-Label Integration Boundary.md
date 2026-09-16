# MS-PROT-061 — Merchant Return Policy, Customer Contact & Courier Return-Label Integration Boundary

**Document ID:** MS-PROT-061  
**Version:** 1.0  
**Status:** ACCEPTED after proposal, review, graph falsification, recommendation and approval  
**Purpose:** Define the minimum merchant-controlled return boundary for physical-product commerce without turning Main Street into a marketplace, return adjudicator or returns-management workflow engine.

---

# 1. Governing principle

> **Main Street is merchant operating infrastructure, not a marketplace. Merchants control their customer-return decisions and commercial return policy. Main Street may publish that policy, preserve relevant Order context, support merchant/customer communication, execute separately authorised existing capability operations, and provide optional courier-integrated return-label generation where the merchant has chosen to fund/free-return that case.**

Main Street shall not process, approve, reject, adjudicate or manage customer returns as an independent platform-owned business workflow.

---

# 2. No Return workflow engine

The following are not introduced at current scope:

```text
ReturnRequest
ReturnCase
ReturnApproval
ReturnWorkflow
ReturnStatus
CustomerSelfServiceReturn
AutomatedReturnApproval
```

A customer wishing to return goods contacts the merchant through the merchant's supported communication channels.

Main Street assists the interaction but does not become the commercial decision-maker.

---

# 3. Merchant-owned return policy

The merchant owns its return terms, subject to applicable law and platform-wide non-overridable constraints.

Merchant policy may include supported semantics such as:

- whether discretionary returns are offered;
- return window;
- required item condition;
- whether sale items are eligible;
- whether customer contact is required before sending goods back;
- whether the merchant pays return carriage;
- whether the customer pays return carriage;
- return instructions;
- applicable contact channels;
- other registered merchant-controlled return terms.

Main Street shall not substitute a platform-standard return policy for the merchant's chosen policy.

---

# 4. AI-assisted policy configuration

The merchant AI concierge/specialist may infer return-policy intent from natural language only against registered semantics.

Example:

> Customers can return unused items within 14 days, but they must contact us first. We pay the return postage.

Candidate mapping may include:

```text
returnWindow = 14 days
conditionRequirement = UNUSED
merchantContactRequired = true
returnCarriageResponsibility = MERCHANT
```

The candidate shall pass the mandatory merchant validation gate and deterministic semantic validation before becoming authoritative.

AI shall not invent return-policy semantics or determine whether a specific customer is commercially entitled to a return.

---

# 5. Customer-facing return information

Registered and guest customers may be shown the merchant's applicable return policy within an authorised Order context.

The customer-facing interaction should generally direct the customer to contact the merchant, for example:

```text
Returns

Contact us within 14 days if you wish to return an item.

[Contact merchant]
```

Main Street shall not imply that selecting a customer-side control automatically initiates, approves or processes a return.

---

# 6. Customer contact remains communication

A customer statement such as:

> I want to return this handbag.

may be represented through existing Conversation/Enquiry semantics with the relevant Order carried structurally as context where known.

Main Street shall not require a dedicated ReturnRequest Operational Object merely because a customer raises the issue.

---

# 7. Merchant decision remains authoritative

Following customer contact, the merchant decides how to handle the matter.

Potential merchant-directed outcomes may include:

```text
decline / explain
refund
replacement
ask customer to return the item
provide return instructions
generate a courier return label where eligible
record inventory disposition after goods are received
```

Each actual business effect executes through the capability that already owns that authority.

---

# 8. Existing capability ownership

Merchant return handling does not transfer authority between capabilities.

```text
Refund
    → Payment / Money authority

Replacement
    → Fulfilment authority

Stock adjustment / returned-item disposition
    → Inventory authority

Customer communication
    → Conversation / Notification authority

Courier return-label generation
    → provider integration utility
```

No return-specific coordinator may directly mutate these authorities outside their registered contracts.

---

# 9. Return-label generation is conditional infrastructure

Main Street may provide a merchant-invoked courier return-label generation utility only where all applicable conditions are satisfied.

At minimum:

1. the merchant has an applicable integrated courier/provider;
2. the merchant has chosen to support merchant-funded/free return carriage for the applicable case, or has otherwise explicitly authorised the label cost under supported semantics;
3. the merchant has explicitly instructed/approved the label generation operation;
4. the relevant Order/customer/return-destination context is valid;
5. the courier/provider accepts the request.

Hard rule:

> **Main Street shall not generate a merchant-funded return label merely because a customer wishes to return an item. Label generation is available only when the merchant's approved policy and explicit merchant action make merchant-funded/free return carriage applicable.**

---

# 10. Free return is merchant policy

Whether a return is free to the customer is a merchant-controlled commercial policy.

Examples:

```text
FREE RETURNS
    merchant pays eligible return carriage

CUSTOMER-PAID RETURNS
    customer arranges/pays carriage

CASE-BY-CASE
    merchant decides for the specific customer interaction
```

Main Street may help the merchant configure these choices but shall not select one on the merchant's behalf.

A merchant offering customer-paid returns shall not automatically receive a Main Street-generated merchant-funded label.

---

# 11. Label generation does not imply business approval

A generated return label is infrastructure/provider output only.

```text
GenerateReturnLabel
    ≠ approve return
    ≠ adjudicate entitlement
    ≠ issue refund
    ≠ restock item
    ≠ cancel Order
    ≠ create replacement fulfilment
```

The label may be represented as provider evidence/artifact associated with the relevant Order/Shipment context rather than a mandatory standalone domain aggregate.

---

# 12. Courier integration boundary

Where a supported courier is integrated:

```text
Merchant instruction
        ↓
resolve relevant Order/customer/destination context
        ↓
merchant review/approval where required
        ↓
courier port
        ↓
provider adapter
        ↓
label/reference/artifact
```

Carrier-specific implementation details remain behind ports/adapters.

Main Street shall not inherit provider workflow vocabulary as business semantics.

---

# 13. Courier return movement evidence

If a courier subsequently reports that a return parcel has moved or reached the merchant, that information is provider/transport evidence.

It shall not automatically:

- restore Inventory;
- issue Refund;
- cancel or modify Order;
- create replacement Fulfilment;
- mark a merchant-customer dispute as resolved.

The merchant remains responsible for the next business decision.

---

# 14. Inventory after physical receipt

When returned goods physically arrive, the merchant decides the applicable Inventory operation.

Examples:

```text
restock
mark damaged
quarantine
repair
write off / dispose
```

Main Street shall not infer saleable condition from courier delivery evidence alone.

Inventory remains authoritative for stock truth and disposition.

---

# 15. Refund without physical return

A merchant may decide:

> Keep the item; I will refund you.

This requires no Return object or return movement.

The merchant invokes the applicable Refund operation through Payment/Money authority.

This is valid because refund and physical return are independent business facts.

---

# 16. Replacement without physical return

A merchant may decide to send replacement goods while allowing the customer to retain the original item.

This is represented through the applicable Fulfilment/Inventory/Payment operations without fabricating a physical Return.

---

# 17. Historical policy provenance

Where materially relevant, Main Street shall retain sufficient provenance to show the merchant what return policy/terms applied to an Order at commitment time.

If a merchant later changes current return policy, the historical Order context shall not silently be rewritten.

This provenance supports merchant decision-making; it does not turn Main Street into a return adjudicator.

---

# 18. Registered and guest customer access

A CustomerAccount is not required merely to contact a merchant about an Order.

Registered customers may contact the merchant from authenticated Order history.

Guest customers may do so through transaction-specific contextual access where permitted.

In both cases, Main Street shall preserve Order context structurally and minimise repeated customer input.

---

# 19. AI concierge operations

The merchant AI concierge may infer merchant operational instructions such as:

> Refund Sarah's last handbag order.

> Send Sarah a replacement.

> Generate a free-return label for Sarah's handbag order.

The agent must resolve the applicable structured Order/customer context and route the approved instruction to the existing capability/provider operation.

AI shall not independently decide that a return is permitted, that the merchant should pay the return postage, or that the customer is entitled to a refund.

---

# 20. Customer self-service return processing is future scope

The following remain outside the accepted current scope:

```text
customer-started Return workflow
customer self-generated courier labels
automatic merchant return approval
automatic refund based on return policy
automatic inventory restoration from return tracking
platform adjudication of return disputes
marketplace-style return centre
```

If Main Street later chooses to support any of these, they require a new proposal, graph review, falsification, recommendation and explicit approval.

---

# 21. Graph model

```text
                 MERCHANT RETURN POLICY
          terms + carriage responsibility
                         │
                         ▼
                 CUSTOMER PROJECTION
                         │
                         ▼
                  CONTACT MERCHANT
                         │
                         ▼
                       MERCHANT
                  decides resolution
                         │
         ┌───────────────┼─────────────────┐
         ▼               ▼                 ▼
      PAYMENT         FULFILMENT         INVENTORY
      refund          replacement        disposition
                         │
                         ▼
                  COURIER INTEGRATION
             return-label utility only
             when merchant-funded/free
             return is applicable
```

---

# 22. Hard invariants

1. Main Street is not a marketplace or return adjudicator.
2. Merchant owns return policy and customer-return decisions.
3. Customer desire to return an item does not create a platform-owned Return workflow.
4. Customer contact is handled through existing communication semantics.
5. Refund remains owned by Payment/Money.
6. Replacement remains owned by Fulfilment.
7. Returned-item disposition remains owned by Inventory.
8. Courier label generation is infrastructure, not commercial approval.
9. Merchant-funded/free return-label generation is available only where merchant policy and explicit merchant action make it applicable.
10. Customer-paid return policy shall not silently cause Main Street to fund/generate a merchant-paid label.
11. Courier return evidence shall not automatically mutate Inventory, Payment, Order or Fulfilment.
12. Historical return-policy provenance may inform merchant decisions without transferring adjudication authority to Main Street.
13. AI may infer merchant instructions but shall not decide merchant commercial policy or customer entitlement.
14. Guest and registered customers may contact merchants from authorised Order context without a dedicated Return object.
15. Customer self-service returns remain future scope unless separately approved.

---

# 23. Rejected designs

The following are rejected at current scope:

```text
ReturnRequest aggregate
ReturnCase aggregate
ReturnApproval workflow
ReturnStatus state machine
platform automatic return adjudication
platform automatic refund
platform automatic inventory restoration
platform-generated merchant-paid return label without merchant policy/approval
carrier delivery evidence treated as merchant decision
customer self-service marketplace-style return centre
```

---

# 24. Acceptance statement

MS-PROT-061 accepts a deliberately narrow merchant-return boundary.

Main Street may help merchants define and publish return terms, retain relevant historical Order context, facilitate communication, invoke existing capability operations at merchant instruction, and generate courier return labels when an integrated courier is available and the merchant has chosen merchant-funded/free returns for the applicable case.

The canonical rule is:

> **The customer contacts the merchant; the merchant decides; Main Street provides the infrastructure the merchant chooses to use.**
