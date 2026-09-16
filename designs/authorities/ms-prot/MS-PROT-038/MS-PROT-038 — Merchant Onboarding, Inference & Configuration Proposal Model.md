# MS-PROT-038 — Merchant Onboarding, Inference & Configuration Proposal Model

**Version:** 1.0
**Status:** **Accepted**
**Depends on:** MS-PROT-020, 021, 022, 028, 036, 037
**Purpose:** Define how Main Street learns enough about a merchant to propose a valid configuration with minimal onboarding friction, without allowing inference to become a semantic authority or business gatekeeper.

---

## 1. Governing principle

> **Onboarding gathers business intent; inference proposes supported semantics; registered relationships determine consequences; the merchant corrects or refines the proposal; the compiler determines validity.**

```text
Merchant evidence
      ↓
Inference
      ↓
Seed configuration nodes
      ↓
Registered semantic relationships
      ↓
Candidate configuration graph
      ↓
Adaptive clarification
      ↓
Merchant review/refinement
      ↓
Compiler validation
      ↓
Approved configuration
```

Onboarding does not define business architecture from a category such as `MECHANIC`, `CONSULTANT`, or `PUBLISHER`.

---

## 2. Onboarding evidence

Main Street may learn from:

* structured questions and answers;
* structured merchant selections;
* optional natural-language business description;
* merchant-provided offerings/content;
* explicitly connected external systems where supported.

These are **evidence**, not executable configuration.

Natural language is useful for interpretation, but important merchant choices should resolve to registered structures and bounded options.

---

## 3. Structured questions are primary

Main Street should prefer:

```text
business-facing question
        ↓
bounded structured options
        ↓
registered semantic meaning
```

Example:

> How do customers normally interact with you?

Possible options:

```text
Read/browse information
Send an enquiry
Book an appointment
Place an order
Visit/contact us directly
Several of these
```

The merchant should not be asked:

```text
Enable Capability: SCHEDULING?
Choose AllocationModel?
Select RequirementGraph?
```

Internal semantics remain hidden.

---

## 4. Natural language supplements structured choices

Free-text interpretation is valuable for discovering likely seed semantics.

Example:

> "I publish scholarships and funding opportunities and people can contact us with questions."

Inference may propose:

```text
Publication
Classification/Search
Enquiry
Contact
```

But free text must not silently activate arbitrary behaviour.

Where a material business decision remains ambiguous, Main Street asks a bounded follow-up question.

Therefore:

> **Natural language reduces questioning; it does not replace explicit resolution of material ambiguity.**

---

## 5. Seed configuration graph

Inference need not determine the complete merchant model in one step.

Its first responsibility is to identify high-confidence seed nodes.

Example:

```text
"We repair cars at our workshop."
        ↓
Service
Vehicle context
```

Registered semantics and subsequent answers may then expose unresolved questions such as:

```text
Customer booking?
Scheduling?
Technician/bay capacity?
Mobile service?
Payment?
```

The graph develops progressively.

---

## 6. Inference does not invent relationships

This remains a hard boundary.

Inference may propose:

```text
Mobile Service
```

but cannot invent:

```text
Mobile Service
    REQUIRES
Service Location
```

That relationship must already exist in the semantic registry.

Likewise:

```text
Booking
    REQUIRES
Scheduling
```

may only be resolved where registered semantics establish that dependency.

Therefore:

```text
Inference       → selects/ranks semantics
Registry        → owns semantic relationships
Compiler        → resolves/validates them
Runtime         → executes resolved semantics
```

---

## 7. Adaptive questioning

Main Street does not use one universal questionnaire.

After each meaningful answer:

```text
Current candidate graph
        +
Known merchant evidence
        ↓
Unresolved material choices
        ↓
Next relevant question
```

A question should normally be asked only when:

1. its answer can materially change the candidate configuration;
2. the answer cannot already be derived from known evidence and registered semantics; and
3. the merchant is the appropriate authority for the choice.

This prevents questionnaire expansion as Main Street gains capabilities.

---

## 8. Question sequencing

Questions should move from **high-information business decisions** toward narrower configuration details.

Conceptually:

```text
What do you provide?
        ↓
How do customers interact with it?
        ↓
What operational constraints apply?
        ↓
Which supported variations do you use?
        ↓
Only then ask detailed parameters
```

Example mechanic:

```text
Provide vehicle services?
        ↓ yes
Do customers make appointments?
        ↓ yes
Scheduling/Booking relevant
        ↓
Does capacity depend on technicians/bays?
        ↓ yes
Allocation/Capacity relevant
        ↓
Do you also visit customers?
        ↓ yes
Mobile-service context relevant
```

Do not ask delivery, inventory, publication, or accommodation questions unless the graph makes them relevant.

---

## 9. Repetition rule

Onboarding should not repeatedly ask for information Main Street already knows.

```text
Known answer
    ↓
reuse
```

Re-questioning is justified only where:

* prior evidence conflicts;
* the merchant explicitly changes an earlier answer;
* the question concerns a materially different context;
* previous information is no longer sufficiently reliable.

Similar concepts in different contexts must not be collapsed merely to avoid repetition.

---

## 10. Merchant correction and freedom

Inference is advisory.

If inference proposes:

```text
Service + Enquiry
```

and the merchant says:

> "Customers can also book appointments."

the merchant may add supported:

```text
Scheduling + Booking
```

The compiler then resolves and validates the resulting graph.

Conversely, the merchant may reject an optional inferred capability.

Therefore:

> **Inference confidence never determines what the merchant is permitted to be.**

Merchant choice remains bounded by Main Street's supported semantics and invariants.

---

## 11. Required semantics are not fake merchant choices

MS-PROT-021 already distinguishes:

```text
REQUIRED_BY_PLATFORM
REQUIRED_BY_SELECTED_SEMANTICS
MERCHANT_SELECTED
```

Onboarding must preserve this.

If selecting a capability necessarily requires another registered semantic dependency, Main Street should not misleadingly ask:

> Would you like the required dependency?

Instead it should explain the consequence where useful.

Merchant choices should represent **real choices**.

---

## 12. Compiler remains authoritative

Inference may produce an invalid candidate.

That is acceptable.

The compiler may reject:

```text
unsupported capability
invalid policy value
missing required dependency
conflicting selections
invalid owner/context
unresolved mandatory choice
```

Inference cannot override the compiler.

If a merchant's desired operation cannot currently be represented:

```text
CONFIGURATION GAP
```

is preferable to invented semantics.

---

## 13. Candidate provenance

Main Street should be able to explain why important candidate semantics appeared.

Conceptually:

```text
Scheduling proposed
because:
merchant said customers book appointments
```

or:

```text
ServiceLocation applicable
because:
MobileService selected
and registered semantics require it
```

This need not become a verbose merchant-facing graph, but inference and compiler consequences should remain traceable.

A universal numeric AI confidence score is **not required**.

---

## 14. Configuration review

Merchant review should use business-facing summaries.

Example:

```text
Customers can:
✓ browse your services
✓ send enquiries
✓ book appointments

Your business can:
✓ manage appointments
✓ manage service availability

Not enabled:
– online ordering
– inventory
– delivery
```

The merchant reviews operating intent, not raw semantic nodes.

---

## 15. Configuration evolution

Onboarding is not a one-time irreversible classification.

A merchant may later evolve:

```text
Publication + Enquiry
        ↓
Publication + Enquiry
+ Consultation
+ Scheduling
+ Booking
+ Payment
```

The same proposal → validation → approval mechanism applies to later changes.

There is no migration between separate "publisher", "consultant", or "commerce" Main Street products.

---

## 16. Identity verification is separate

Merchant configuration inference and merchant-controller identity verification are independent concerns.

```text
Business understanding/configuration
        ≠
Controller identity verification
        ≠
Business verification
```

MS-PROT-038 does not decide exactly when controller verification must complete relative to publication.

That remains an explicitly open product/security decision.

Physical-premises verification is not a universal onboarding step.

---

## 17. External providers

External providers must remain integration choices rather than business semantics.

Example:

```text
Merchant needs Scheduling
        ↓
Scheduling semantics exist
        ↓
Implementation/integration may use:
Main Street / Calendly / Google Calendar / other supported provider
```

Similarly, Google Business Profile is relevant where the merchant chooses Google's local-discovery ecosystem; it is not evidence that physical premises are required for Main Street eligibility.

---

# Falsification review

The following proposals were tested and rejected:

| Failed assumption                                    | Why it fails                                                             |
| ---------------------------------------------------- | ------------------------------------------------------------------------ |
| One universal questionnaire                          | Irrelevant questions increase friction as platform breadth grows         |
| Business category determines configuration           | Hybrid and evolving merchants break category-driven architecture         |
| Free text alone is sufficient                        | Material ambiguity can produce incorrect silent assumptions              |
| Every decision must be explicitly questioned         | Registered semantics and existing evidence can resolve many consequences |
| Inference should produce the whole graph in one pass | Complex merchants require progressive clarification                      |
| AI may invent missing relationships                  | Makes inference semantic authority                                       |
| AI confidence may gate capability selection          | Turns inference into business gatekeeper                                 |
| Every capability should appear as a toggle           | Required/internal semantics are not genuine merchant choices             |
| Compiler may repair unsupported semantics creatively | Would make validation another inference engine                           |
| Onboarding must verify physical premises             | Online consultants and publishers disprove it                            |
| Controller verification means business verified      | These are different trust claims                                         |
| Answers should never be revisited                    | Conflicts, changes and contextual distinctions sometimes require it      |

No material falsifier remains after these corrections.

---

# Cross-domain validation

### Information publisher

```text
"Publish scholarship opportunities"
        ↓
Publication
        ↓
Ask about:
classification/search
deadlines
external links
enquiries
subscriptions
```

No inventory/payment/scheduling questions unless later evidence makes them relevant.

**PASS**

### Online consultant

```text
Consultation
+ Scheduling
+ Booking
+ Enquiry
+ optional Payment
```

No premises requirement. External scheduling provider remains an integration option.

**PASS**

### Mechanic

Workshop service may progressively reveal Booking, Scheduling and capacity; mobile service may make ServiceLocation applicable through registered semantics.

**PASS**

### Grocery

Product/order answers activate commerce questions; Delivery makes address-related semantics relevant while Collection does not.

**PASS**

### Hybrid/evolving merchant

A publisher later adding paid consultations extends the same configuration graph rather than being reclassified into another product.

**PASS**

---

# Accepted invariants

1. Onboarding collects business intent rather than exposing platform architecture.
2. Structured bounded questions are the primary mechanism for material merchant decisions.
3. Natural language may reduce questioning but cannot silently create semantics.
4. Inference proposes seed/configuration nodes; it does not invent semantic relationships.
5. Registered semantics determine dependencies and consequences.
6. The compiler remains authoritative over structural validity.
7. Questioning is adaptive and driven by unresolved material configuration choices.
8. Known information should not be requested repeatedly without reason.
9. Merchants may correct inference and select among supported optional semantics.
10. Required/internal semantics are not presented as artificial merchant choices.
11. Business category may aid inference but does not determine architecture or eligibility.
12. Inference confidence does not gate merchant operating identity.
13. Candidate configuration and important derived consequences remain traceable.
14. Invalid or unsupported merchant intent becomes a configuration gap rather than invented behaviour.
15. The same mechanism supports initial onboarding and later configuration evolution.
16. Controller identity verification is distinct from configuration inference and business verification.
17. Physical presence is optional context, not universal onboarding eligibility.
18. External providers implement/integrate semantics; they do not define merchant business models.

---

## Deferred decisions

MS-PROT-038 does not yet define:

* the exact question catalogue and answer options;
* the inference model/provider;
* ranking/confidence implementation;
* onboarding screen layout;
* precise identity-verification completion point;
* automatic external-data imports;
* recovery/resume UX;
* analytics for question optimisation;
* exact merchant approval UI.

Those can now be designed without changing the semantic boundary established here.

---

## Governance verdict

```text
PROPOSE                    ✓
FALSIFICATION REVIEW       ✓
FAILED ASSUMPTIONS REMOVED ✓
CROSS-DOMAIN VALIDATION    ✓
ACCEPT                     ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street onboarding is an adaptive configuration-discovery process. It uses structured merchant choices and optional natural-language interpretation to propose registered semantics, asks only questions needed to resolve material uncertainty, allows merchants to correct the proposal, relies on registered relationships for semantic consequences, and leaves final validity to the deterministic compiler. Inference facilitates merchant choice; it neither invents platform behaviour nor determines which businesses are allowed to use Main Street.**

The natural next specification is **MS-PROT-039 — Adaptive Onboarding Question & Decision Model**, which can now define the actual question families, option structures, sequencing rules and unresolved-choice algorithm without mixing those concerns into semantic architecture.
