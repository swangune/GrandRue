import { describe, expect, it } from "vitest";

import {
  composeStorefront,
  type StorefrontSurfaceResponse,
} from "./storefront-composition";

function surface(
  contributions: Array<{
    ownerCapabilityIdentifier: string;
    contributionIdentifier: string;
    supportedOperationReferences?: string[];
    bindings?: Array<{
      subjectReference: string;
      label: string;
    }>;
  }>,
): StorefrontSurfaceResponse {
  return {
    merchantIdentifier: "merchant-1",
    configurationIdentifier: "config-1",
    releaseIdentifier: "release-1",
    groups: [
      {
        compositionTargetReference: "public/test",
        contributions: contributions.map((contribution) => ({
          kind: "PUBLIC_INTERACTION",
          supportedOperationReferences:
            contribution.supportedOperationReferences ?? [],
          bindings: contribution.bindings ?? [],
          ...contribution,
        })),
      },
    ],
  };
}

describe("composeStorefront", () => {
  it("renders registered publication content without inventing commerce actions", () => {
    const result = composeStorefront(
      surface([
        {
          ownerCapabilityIdentifier: "publication",
          contributionIdentifier: "browse-published-content",
        },
      ]),
    );

    expect(result.contentEnabled).toBe(true);
    expect(result.actions).toEqual([]);
    expect(result.unsupportedContributionKeys).toEqual([]);
  });

  it("adds appointment presentation only with backend-projected subject bindings", () => {
    const result = composeStorefront(
      surface([
        {
          ownerCapabilityIdentifier: "publication",
          contributionIdentifier: "browse-published-content",
        },
        {
          ownerCapabilityIdentifier: "appointment",
          contributionIdentifier: "arrange-appointment",
          supportedOperationReferences: ["appointment.confirm"],
          bindings: [
            {
              subjectReference: "garden-maintenance",
              label: "Garden maintenance visit",
            },
          ],
        },
      ]),
    );

    expect(result.contentEnabled).toBe(true);
    expect(result.actions).toEqual([
      {
        contributionKey: "appointment/arrange-appointment",
        label: "Arrange a time",
        interactionKind: "appointment",
        bindings: [
          {
            subjectReference: "garden-maintenance",
            label: "Garden maintenance visit",
          },
        ],
      },
    ]);
  });

  it("registers distinct form kinds for booking and ordering", () => {
    expect(
      composeStorefront(
        surface([
          {
            ownerCapabilityIdentifier: "booking",
            contributionIdentifier: "reserve-subject",
            supportedOperationReferences: ["booking.confirm"],
            bindings: [
              { subjectReference: "standard-room", label: "Standard room" },
            ],
          },
        ]),
      ).actions[0],
    ).toEqual({
      contributionKey: "booking/reserve-subject",
      label: "Reserve",
      interactionKind: "booking",
      bindings: [{ subjectReference: "standard-room", label: "Standard room" }],
    });

    expect(
      composeStorefront(
        surface([
          {
            ownerCapabilityIdentifier: "ordering",
            contributionIdentifier: "place-order",
            supportedOperationReferences: ["ordering.commit"],
            bindings: [{ subjectReference: "milk-2l", label: "Milk 2L" }],
          },
        ]),
      ).actions[0],
    ).toEqual({
      contributionKey: "ordering/place-order",
      label: "Order",
      interactionKind: "order",
      bindings: [{ subjectReference: "milk-2l", label: "Milk 2L" }],
    });
  });

  it("keeps a registered interaction with no projected subjects non-actionable", () => {
    const result = composeStorefront(
      surface([
        {
          ownerCapabilityIdentifier: "appointment",
          contributionIdentifier: "arrange-appointment",
          supportedOperationReferences: ["appointment.confirm"],
          bindings: [],
        },
      ]),
    );

    expect(result.actions).toEqual([
      {
        contributionKey: "appointment/arrange-appointment",
        label: "Arrange a time",
        interactionKind: "appointment",
        bindings: [],
      },
    ]);
  });

  it("fails closed for an unregistered presentation contribution", () => {
    const result = composeStorefront(
      surface([
        {
          ownerCapabilityIdentifier: "future-capability",
          contributionIdentifier: "future-interaction",
        },
      ]),
    );

    expect(result.contentEnabled).toBe(false);
    expect(result.actions).toEqual([]);
    expect(result.unsupportedContributionKeys).toEqual([
      "future-capability/future-interaction",
    ]);
  });
});
