# Conversation Lifecycle Model — Historical Draft Evidence

> **Module:** Data
> **Status:** Historical Draft evidence — non-authoritative
> **Current governing authority:** MS-PROT-086 v1.0 composed with composite MS-PROT-043, MS-PROT-059, composite MS-PROT-075 and MS-PROT-085

The former `Created / AI Processing / Waiting for Customer / Waiting for Merchant / Active / Resolved / Closed / Reopened` lifecycle is preserved only in Git history as an earlier hypothesis.

MS-PROT-086 explicitly rejects a universal Conversation lifecycle. A Conversation provides durable communication continuity; it is not an Enquiry, ticket, source business aggregate, provider thread, Notification or Merchant Attention occurrence. Message acceptance, delivery/read evidence, human handling and source-business resolution remain independently owned facts.

Implementations must model only the accepted contract-governed creation/reuse, participant/guest/channel binding, immutable Message and response/handoff semantics. A source capability may define its own lifecycle without transferring that lifecycle to Conversation.
