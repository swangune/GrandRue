import { afterEach, describe, expect, it, vi } from "vitest";

import {
  GrandRueBackendResponseError,
  commitPublicOrder,
  confirmPublicAppointment,
  confirmPublicBooking,
} from "./grandrue-api";

afterEach(() => {
  vi.unstubAllGlobals();
});

describe("public interaction API client", () => {
  it("submits appointment intent using only the public subject reference", async () => {
    const fetchMock = vi.fn().mockResolvedValue(
      new Response(JSON.stringify({ identifier: "appointment-1" }), {
        status: 201,
        headers: { "Content-Type": "application/json" },
      }),
    );
    vi.stubGlobal("fetch", fetchMock);

    await confirmPublicAppointment("prototype-gardener-bookable", {
      commandIdentifier: "intent-1",
      appointmentIdentifier: "appointment-1",
      subjectReference: "garden-maintenance",
      startsAt: "2026-08-27T13:00:00Z",
      endsAt: "2026-08-27T15:00:00Z",
    });

    expect(fetchMock).toHaveBeenCalledOnce();
    const [url, init] = fetchMock.mock.calls[0] as [string, RequestInit];
    expect(url).toContain(
      "/prototype/public/merchants/prototype-gardener-bookable/appointments",
    );
    expect(init.headers).toMatchObject({ "Idempotency-Key": "intent-1" });
    expect(init.body).toContain('"subjectReference":"garden-maintenance"');
    expect(init.body).not.toContain("gardening.perform");
    expect(init.body).not.toContain("customer-1");
  });

  it("submits booking intent without allocation identity", async () => {
    const fetchMock = vi.fn().mockResolvedValue(
      new Response(JSON.stringify({ identifier: "booking-1" }), {
        status: 201,
        headers: { "Content-Type": "application/json" },
      }),
    );
    vi.stubGlobal("fetch", fetchMock);

    await confirmPublicBooking("prototype-motel", {
      commandIdentifier: "intent-2",
      bookingIdentifier: "booking-1",
      subjectReference: "standard-room",
      startsAt: "2026-08-27T14:00:00Z",
      endsAt: "2026-08-29T10:00:00Z",
    });

    const [, init] = fetchMock.mock.calls[0] as [string, RequestInit];
    expect(init.body).toContain('"subjectReference":"standard-room"');
    expect(init.body).not.toContain("capacity");
  });

  it("submits order intent without internal sku or configured quantity unit", async () => {
    const fetchMock = vi.fn().mockResolvedValue(
      new Response(JSON.stringify({ identifier: "order-1" }), {
        status: 201,
        headers: { "Content-Type": "application/json" },
      }),
    );
    vi.stubGlobal("fetch", fetchMock);

    await commitPublicOrder("prototype-retailer", {
      commandIdentifier: "intent-3",
      orderIdentifier: "order-1",
      portions: [
        {
          portionIdentifier: "portion-1",
          subjectReference: "milk-2l",
          quantity: 2,
        },
      ],
    });

    const [, init] = fetchMock.mock.calls[0] as [string, RequestInit];
    expect(init.body).toContain('"subjectReference":"milk-2l"');
    expect(init.body).not.toContain("sku-1");
    expect(init.body).not.toContain("unitIdentifier");
    expect(init.body).not.toContain("EACH");
  });

  it("classifies an HTTP conflict as a definitive backend response", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue(
        new Response(JSON.stringify({ error: "conflict" }), {
          status: 409,
          headers: { "Content-Type": "application/json" },
        }),
      ),
    );

    await expect(
      confirmPublicBooking("prototype-motel", {
        commandIdentifier: "intent-conflict",
        bookingIdentifier: "booking-conflict",
        subjectReference: "standard-room",
        startsAt: "2026-08-27T14:00:00Z",
        endsAt: "2026-08-29T10:00:00Z",
      }),
    ).rejects.toMatchObject({
      name: "GrandRueBackendResponseError",
      status: 409,
    });
  });

  it("does not reinterpret a network exception as backend rejection", async () => {
    const networkFailure = new TypeError("fetch failed");
    vi.stubGlobal("fetch", vi.fn().mockRejectedValue(networkFailure));

    try {
      await confirmPublicAppointment("prototype-gardener-bookable", {
        commandIdentifier: "intent-uncertain",
        appointmentIdentifier: "appointment-uncertain",
        subjectReference: "garden-maintenance",
        startsAt: "2026-08-27T13:00:00Z",
        endsAt: "2026-08-27T15:00:00Z",
      });
      throw new Error("Expected network failure");
    } catch (error) {
      expect(error).toBe(networkFailure);
      expect(error).not.toBeInstanceOf(GrandRueBackendResponseError);
    }
  });
});
