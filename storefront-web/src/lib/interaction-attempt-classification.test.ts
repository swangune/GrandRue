import { describe, expect, it } from "vitest";

import { GrandRueBackendResponseError } from "./grandrue-api";
import { classifyInteractionAttemptFailure } from "./interaction-attempt-classification";
import { initialInteractionState } from "./interaction-action-state";

describe("interaction attempt failure classification", () => {
  const current = initialInteractionState("intent-1", "booking-1");
  const fresh = {
    commandIdentifier: "intent-2",
    objectIdentifier: "booking-2",
  };

  it("treats a 409 response as definitive rejection with fresh identity", () => {
    expect(
      classifyInteractionAttemptFailure(
        new GrandRueBackendResponseError(409, "/bookings"),
        current,
        fresh,
      ),
    ).toMatchObject({
      status: "rejected",
      commandIdentifier: "intent-2",
      objectIdentifier: "booking-2",
    });
  });

  it("treats a 500 response as uncertain and preserves logical identity", () => {
    expect(
      classifyInteractionAttemptFailure(
        new GrandRueBackendResponseError(500, "/bookings"),
        current,
        fresh,
      ),
    ).toMatchObject({
      status: "uncertain",
      commandIdentifier: "intent-1",
      objectIdentifier: "booking-1",
    });
  });

  it("treats network failure as uncertain and preserves logical identity", () => {
    expect(
      classifyInteractionAttemptFailure(
        new TypeError("fetch failed"),
        current,
        fresh,
      ),
    ).toMatchObject({
      status: "uncertain",
      commandIdentifier: "intent-1",
      objectIdentifier: "booking-1",
    });
  });
});
