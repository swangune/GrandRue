# IMP-08C C4B Selection — Merchant Account Event Mapping
Date: 6 September 2026. Implementation navigation under MS-IMP-001 and IMPLEMENTATION-RULES v1.6.
C4A final head `b2ffc0b3ee1d7ef4ec84722a4befdbde23548f2d`, run `33998151474`, passed 1095 unit/governance + 377 PostgreSQL integration tests.

Select C4B1, READY: preserve explicit MerchantAccountEstablished contract affinity in the existing atomic bootstrap/publication path. Authority: MS-PROT-071 §21 and v1.2 §§3–6; MS-PROT-026 v1.1 §§4–10. Exact encoding is implementation detail.
The owner contract references the accepted fact, merchant scope, subject, temporal/request provenance and bounded payload. Its initial immutable event registry release is implementation-pinned. This does not alter the accepted event meaning or create a new consumer.
A stable event occurrence identity is derived by a namespaced encoding of the already durable unique publication identity; work, publication, fact and logical-request identities remain distinguishable.
Legacy rows preserve absent registration; no historical backfill inference. New bootstrap commits affinity with existing publication evidence. Registered mapping fails closed for absent/unknown/changed contract meaning. Existing event()/technical publication APIs retain compatibility and are not upgraded into reaction admission.
C4B remains IN_PROGRESS until concrete mapping proof closes; C4C reaction registration is next independent prerequisite. C2B/C3/C5 retain their dependencies.
