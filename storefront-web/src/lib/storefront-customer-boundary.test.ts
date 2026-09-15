import { readFileSync } from "node:fs";
import { fileURLToPath } from "node:url";

import { describe, expect, it } from "vitest";

const merchantPageSource = readFileSync(
  fileURLToPath(
    new URL("../../app/m/[merchantIdentifier]/page.tsx", import.meta.url),
  ),
  "utf8",
);

describe("customer-facing storefront boundary", () => {
  it("does not render semantic registry or configuration diagnostics", () => {
    expect(merchantPageSource).not.toContain("surface.releaseIdentifier");
    expect(merchantPageSource).not.toContain("configurationIdentifier");
    expect(merchantPageSource).not.toContain("action-meta");
    expect(merchantPageSource).not.toContain(
      "registered backend contribution has no storefront presentation component",
    );
    expect(merchantPageSource).not.toContain("active semantics");
    expect(merchantPageSource).not.toContain("published-content contribution");
    expect(merchantPageSource).not.toContain("active storefront surface");
  });
});
