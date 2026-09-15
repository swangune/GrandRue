# IMP-06 E4 Post-Approval Governance Conformance — 2 September 2026

**Record type:** post-approval design-governance / implementation-readiness evidence  
**Branch:** `development`  
**Governing rules:** `designs/DESIGN-RULES.md`, `designs/DESIGN-CORPUS-CONFORMANCE.md`, `designs/IMPLEMENTATION-RULES.md`, `designs/MS-IMP-001.md`

This record is evidence only. Substantive authority remains in the accepted design authorities.

## Accepted authorities

Explicit manual approval on 2 September 2026 authorised formalisation of:

- `MS-PROT-051 v1.5 — Merchant Location Public Exposure Choice Revision & Persistence Amendment`;
- `MS-PROT-027 v1.11 — Instance-Aware Exposure Membership & Owner-Filtered Deterministic Resolution Amendment`.

Formalisation commits:

- `f36c0e1ef662fe09a223f599dc90bfe4e19395c6` — MS-PROT-051 v1.5;
- `3dfa22df3ce48707a044fd786ad8467cad4acaac` — MS-PROT-027 v1.11.

## Governance navigation

Post-approval navigation was reconciled:

- `designs/AUTHORITY-INDEX.md` v3.53 includes composite MS-PROT-027 through v1.11 and composite MS-PROT-051 through v1.5;
- `designs/DEFERRED-DECISION-REGISTER.md` v4.34 resolves `IMP-06-E3-DG-002`, `IMP-06-E4-DG-001`, `MS-PROT-027-V15-DQ-002` and the design decision `MS-PROT-027-V15-DQ-003`, while retaining DQ-004 and other narrower deferred scope;
- `docs/development/implementation-status.md` identifies LEC1 Merchant Location Exposure-choice persistence as the smallest READY executable node;
- `docs/development/imp-06-graph-refresh-2026-09-02.md` records the accepted v1.11 implementation dependency chain.

## Structural conformance review

| Check | Result |
|---|---|
| Exactly one canonical Design Rules file | PASS |
| Exactly one canonical Authority Index | PASS |
| Exactly one canonical DDR | PASS |
| Exactly one canonical Semantic Lexicon | PASS |
| Exactly one canonical Implementation Rules file | PASS |
| New accepted authorities expose ID/version/status/amends/depends/closes/purpose metadata | PASS |
| Accepted MS-PROT-027 v1.11 represented in Authority Index | PASS |
| Accepted MS-PROT-051 v1.5 represented in Authority Index | PASS |
| Amendment targets resolve to existing accepted authority chains | PASS |
| DQ/gate statuses in current DDR no longer contradict the accepted authorities | PASS |
| No duplicate accepted file claims MS-PROT-027 v1.11 | PASS |
| No duplicate accepted file claims MS-PROT-051 v1.5 | PASS |
| No competing current governance companion file introduced | PASS |

## Semantic/terminology review

No amendment to `designs/CANONICAL-SEMANTIC-LEXICON.md` is required for this formalisation.

Reason: the existing lexicon already distinguishes semantic Exposure from presentation visibility and other runtime decision classes. The new terms `ExposureMemberIdentitySpecification`, `ExposureCandidateInstanceReference` and `ExposedElementMembership` are bounded MS-PROT-027 contract terms whose normative meaning is complete in v1.11; adding them to the cross-domain canonical lexicon would not resolve an ambiguity outside that authority.

No amendment to `designs/IMPLEMENTATION-RULES.md` is required. The current rules already require exactly the manual-gate lifecycle followed here and already govern the subsequent RED → GREEN → verification → evidence sequence.

## Preserved boundaries

The accepted correction does not introduce:

- a platform-wide identity framework;
- a generic Exposure policy database;
- a rules DSL;
- a generic business-value payload inside E4;
- business-capability switches in the generic resolver;
- a new deployment boundary; or
- a change to Merchant Location fact/revision semantics.

The downstream S2/T1b4 bounded-read representation-affinity question remains deliberately separate. It is not silently resolved by this evidence record.

## Readiness conclusion

```text
IMP-06-E4-DG-001
    DESIGN-RESOLVED

post-approval governance formalisation
    CONFORMING

current smallest READY executable node
    LEC1 — Merchant Location Exposure-choice persistence
```

Implementation may now resume automatically under `MS-IMPLEMENTATION-RULES-001` beginning with LEC1 RED tests.
