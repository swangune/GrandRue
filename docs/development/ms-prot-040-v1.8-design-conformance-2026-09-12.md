# MS-PROT-040 v1.8 Design-Cycle Conformance — 12 September 2026

**Authority:** MS-PROT-040 v1.8 — Configuration Reinstatement Decision Affinity Amendment  
**Branch:** development  
**Formalisation baseline:** ba169c2f25c96aa295babf89b9e3ba6789c899e3  
**Manual approval:** granted in ChatGPT on 12 September 2026  
**Implementation effect:** design gate resolved; IMP-05-R3B remains IN_PROGRESS

## Result

The implementation-discovered gap was `UNDERSPECIFIED_SEMANTICS / MISSING_CONSISTENCY_RULE`.

Composite MS-PROT-040 through v1.7 required a new current reinstatement approval but did not identify the causal datum distinguishing it from an older approval when the same revision or Controller Relationship recurred.

MS-PROT-040 v1.8 resolves the gap with the exact current Reinstatement Basis Activation.

## Fundamental Vision Conformance

**VISION-CONFORMING.**

The amendment increases internal correctness without adding merchant-facing configuration concepts or administration.

## Architecture Review

**PASS.**

Configuration retains Configuration decision/activation ownership. Merchant Account and Controller truth remain owner-separated. Existing first-activation and forward-replacement authorities are not broadened.

## Falsification

**PASS.**

The accepted authority covers historical first approval, fresh superseded-initial reinstatement, same-revision cycle alias, historical later revision, Controller transfer, basis movement during and after approval, evidence-basis mismatch, never-activated candidate, historical activation replay and unsupported serving admission.

## Ambiguity Review

**PASS.**

Revision identity is distinct from exact current activation identity; historical approval is distinct from reinstatement approval; timestamp recency is not causal affinity; activation replay is not reinstatement.

## Implementation-Rules Impact

No amendment to `designs/IMPLEMENTATION-RULES.md` is required.

## Navigation / Corpus Updates

```text
designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision Affinity Amendment.md
designs/AUTHORITY-INDEX.md
designs/DEFERRED-DECISION-REGISTER.md
designs/CANONICAL-SEMANTIC-LEXICON.md
SEQUENCE.md
docs/development/ms-prot-040-v1.8-design-conformance-2026-09-12.md
```

No implementation-programme node is marked complete by this design formalisation.

`IMP-05-R3B` remains `IN_PROGRESS`.

`IMP-05` remains `PARTIALLY_CONFORMING`.

`IMP-06` remains blocked by the accepted hard dependency on IMP-05.

## Next Governed Action

Resume `IMP-05-R3B` tests first under composite MS-PROT-040 through v1.8.

The first proof must exercise the superseded-initial-revision path and prove that historical first approval is insufficient while fresh exact basis-affined reinstatement approval can authorise a new Configuration Activation fact.
