import { describe, expect, it } from "vitest";

import {
  appointmentIntentFromFormData,
  bookingIntentFromFormData,
  orderIntentFromFormData,
} from "./storefront-interaction-intents";

function form(values: Record<string, string>): FormData {
  const data = new FormData();
  for (const [key, value] of Object.entries(values)) {
    data.set(key, value);
  }
  return data;
}

describe("storefront interaction intent parsing", () => {
  it("preserves stable appointment intent identity and public subject only", () => {
    expect(
      appointmentIntentFromFormData(
        form({
          commandIdentifier: "intent-appointment-1",
          objectIdentifier: "appointment-1",
          subjectReference: "garden-maintenance",
          startsAt: "2026-08-27T12:00:00.000Z",
          endsAt: "2026-08-27T14:00:00.000Z",
        }),
      ),
    ).toEqual({
      commandIdentifier: "intent-appointment-1",
      appointmentIdentifier: "appointment-1",
      subjectReference: "garden-maintenance",
      startsAt: "2026-08-27T12:00:00.000Z",
      endsAt: "2026-08-27T14:00:00.000Z",
    });
  });

  it("parses booking intent without allocation or operation context", () => {
    const intent = bookingIntentFromFormData(
      form({
        commandIdentifier: "intent-booking-1",
        objectIdentifier: "booking-1",
        subjectReference: "standard-room",
        startsAt: "2026-08-27T13:00:00.000Z",
        endsAt: "2026-08-29T09:00:00.000Z",
      }),
    );

    expect(intent.subjectReference).toBe("standard-room");
    expect(JSON.stringify(intent)).not.toContain("capacity");
    expect(JSON.stringify(intent)).not.toContain("booking.confirm");
  });

  it("parses an order with public subject and quantity only", () => {
    expect(
      orderIntentFromFormData(
        form({
          commandIdentifier: "intent-order-1",
          objectIdentifier: "order-1",
          portionIdentifier: "portion-1",
          subjectReference: "milk-2l",
          quantity: "2",
        }),
      ),
    ).toEqual({
      commandIdentifier: "intent-order-1",
      orderIdentifier: "order-1",
      portions: [
        {
          portionIdentifier: "portion-1",
          subjectReference: "milk-2l",
          quantity: 2,
        },
      ],
    });
  });

  it("rejects non-ascending time windows", () => {
    expect(() =>
      appointmentIntentFromFormData(
        form({
          commandIdentifier: "intent-appointment-2",
          objectIdentifier: "appointment-2",
          subjectReference: "garden-maintenance",
          startsAt: "2026-08-27T14:00:00.000Z",
          endsAt: "2026-08-27T12:00:00.000Z",
        }),
      ),
    ).toThrow("End time must be after start time");
  });

  it("rejects zero or negative order quantity", () => {
    expect(() =>
      orderIntentFromFormData(
        form({
          commandIdentifier: "intent-order-2",
          objectIdentifier: "order-2",
          portionIdentifier: "portion-2",
          subjectReference: "milk-2l",
          quantity: "0",
        }),
      ),
    ).toThrow("Quantity must be greater than zero");
  });
});
