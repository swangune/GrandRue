# Conversation Engine — Historical Draft Evidence

> **Module:** Platform Services
> **Status:** Historical Draft evidence — non-authoritative
> **Current governing authority:** composite MS-PROT-043 through v1.4, MS-PROT-059, composite MS-PROT-075, MS-PROT-085 and MS-PROT-086

This document's former central Conversation Engine proposal is retained through Git history as early product/architecture evidence. It does not govern current implementation.

MS-PROT-086 now establishes the bounded Customer Communication ownership required for durable Conversation creation/reuse, participant/guest/channel bindings, immutable Message acceptance and customer-service handoff within the modular monolith.

The former assumptions that one central engine owns a universal Conversation lifecycle, that AI confidence determines resolution, that every communication enters merchant work handling, or that provider/channel state defines Conversation truth are superseded within overlapping scope.

Current implementation must read the accepted authorities through `designs/AUTHORITY-INDEX.md` and may proceed only through `designs/IMPLEMENTATION-RULES.md` and the accepted implementation programme. Production channel, automated-response, guest-access and attachment portfolios remain gated by `MS-PROT-086-DQ-001` through `MS-PROT-086-DQ-004`.
