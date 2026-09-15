# IMP-08C C4C Selection — Reaction Registration and Identity
Date: 6 September 2026. Navigation under MS-IMP-001 and IMPLEMENTATION-RULES v1.6.
C4B1 closing head `48c991424a48f6793bede6a5336a882d250503d9`, run `33999150773`, passed 1099 unit/governance + 380 PostgreSQL integration tests.

Select C4C: READY after C4B1 closing gate. MS-PROT-026 v1.1 §§13–18, 22–24, 26, 29–32 and 36 require registered owner reactions, per-event/per-contract identity, explicit current/historical source meaning and downstream intent affinity.
Implement static reaction declarations and exact release/source affinity resolution. EventReactionIdentity is the source event occurrence plus owner-qualified reaction contract identity, excluding attempt, delivery and registry release. A code/release change alone must not manufacture a second logical reaction.
Register the concrete Commercial Standing Free declaration from MS-PROT-056 v1.6 and the source contract from C4B1. Its execution is not enabled by registration; existing StandingFreeFromMerchantAccountEstablishedHandler remains the owning application path.
C4D then supplies trusted source/worker composition and independent durable acknowledgement; C2/C3/C5 require later execution/recovery evidence. No universal listener engine, global acknowledgement, provider call or merchant-configurable job.
