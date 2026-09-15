# IMP-07 T2 — Executable Decomposition

**Date:** 5 September 2026

Implementation navigation only under MS-IMP-001 and IMPLEMENTATION-RULES. Composite MS-PROT-035 and MS-PROT-043 distinguish explicitly registered public submission from stored-data access; Enquiry requirements remain owner-defined.

```text
T2   PUBLIC Enquiry submission adapter                  IN_PROGRESS
T2A  merchant-general public command delivery          READY — E2/E3 + G0
T2B  Opportunity binding carry-forward and submission  BLOCKED_DEPENDENCY — T2A; I2/E3 complete
```

E3 already supports merchant-general submission without subject participation. T2A will register and deliver that exact path, with trusted public route scope, current admission on every attempt, required owner preparation, E2 retry reconciliation and a content-free completion acknowledgement. It will reject subject/customer/merchant authority fields rather than downgrade them to general Enquiry.

The existing I2 PublicInteractionBinding is an internal value, and T1's public DTO deliberately omits revision/binding internals. T2B must complete that browser-to-submission handoff and preserve stable retry intent before the Opportunity-specific path can be claimed. This decomposition does not declare a design gap or invent a token format, contact requirement schema, CustomerContext association or stored-data access proof.

T2 and V1 remain incomplete until the binding-aware path and required integrated proof conform.
