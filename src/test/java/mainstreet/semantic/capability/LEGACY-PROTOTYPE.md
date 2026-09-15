# Legacy capability prototype — test-scope evidence only

The classes colocated with this note are retained only so historical prototype tests remain executable.

They are **not production semantic authority** and are deliberately excluded from `src/main`.

The accepted production compilation path is:

`MerchantConfiguration + SemanticRegistrySnapshot -> mainstreet.semantic.compiler.ConfigurationCompiler -> ExecutableMerchantModel`

The historical `CapabilityCompiler`, mutable `Capability` model, `OperationComposition` trigger-to-consequence model, and related configuration/registry helpers predate the accepted Operational Object / registered-semantics architecture. The accepted primitive review explicitly rejects universal trigger-to-consequence composition and requires the older compiler path to be removed or quarantined.

Do not add production dependencies on these test fixtures.
