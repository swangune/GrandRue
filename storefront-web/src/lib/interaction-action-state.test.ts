import { describe, expect, it } from "vitest";

import {
  completedInteractionState,
  initialInteractionState,
  rejectedInteractionState,
  uncertainInteractionState,
} from "./interaction-action-state";

describe("interaction action state", () => {
  it("preserves logical command identity when outcome is uncertain", () => {
    const initial = initialInteractionState(
      "intent-1",
      "appointment-1",
    );

    expect(uncertainInteractionState(initial)).toMatchObject({
      status: "uncertain",
      commandIdentifier: "intent-1",
      objectIdentifier: "appointment-1",
    });
  });

  it("preserves identity after acknowledged completion", () => {
    const initial = initialInteractionState("intent-2", "booking-1");

    expect(completedInteractionState(initial)).toMatchObject({
      status: "completed",
      commandIdentifier: "intent-2",
      objectIdentifier: "booking-1",
    });
  });

  it("uses fresh identity only after definitive rejection", () => {
    const initial = initialInteractionState(
      "intent-3",
      "order-1",
      "portion-1",
    );

    expect(
      rejectedInteractionState(initial, {
        commandIdentifier: "intent-4",
        objectIdentifier: "order-2",
        portionIdentifier: "portion-2",
      }),
    ).toMatchObject({
      status: "rejected",
      commandIdentifier: "intent-4",
      objectIdentifier: "order-2",
      portionIdentifier: "portion-2",
    });
  });
});
