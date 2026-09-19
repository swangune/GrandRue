import type { StorefrontSurfaceResponse } from "./storefront-composition";

export class GrandRueBackendResponseError extends Error {
  constructor(
    readonly status: number,
    path: string,
  ) {
    super(`GrandRue backend request failed (${status}) for ${path}`);
    this.name = "GrandRueBackendResponseError";
  }
}

export type Publication = {
  publicationIdentifier: string;
  title: string;
  summary: string;
  publicationState: string;
  revision: number;
  publishFrom: string | null;
  publishUntil: string | null;
};

export type PublicAppointmentIntent = {
  commandIdentifier: string;
  appointmentIdentifier: string;
  subjectReference: string;
  startsAt: string;
  endsAt: string;
};

export type PublicBookingIntent = {
  commandIdentifier: string;
  bookingIdentifier: string;
  subjectReference: string;
  startsAt: string;
  endsAt: string;
};

export type PublicOrderIntent = {
  commandIdentifier: string;
  orderIdentifier: string;
  portions: Array<{
    portionIdentifier: string;
    subjectReference: string;
    quantity: number;
  }>;
};

const BACKEND_URL =
  process.env.GRANDRUE_BACKEND_URL ??
  process.env.MAINSTREET_BACKEND_URL ??
  "http://localhost:8080";

export async function getStorefrontSurface(
  merchantIdentifier: string,
): Promise<StorefrontSurfaceResponse> {
  return getJson<StorefrontSurfaceResponse>(
    `/prototype/merchants/${encodeURIComponent(merchantIdentifier)}/storefront-surface`,
  );
}

export async function getPublications(
  merchantIdentifier: string,
): Promise<Publication[]> {
  return getJson<Publication[]>(
    `/prototype/merchants/${encodeURIComponent(merchantIdentifier)}/publications`,
  );
}

export async function confirmPublicAppointment(
  merchantIdentifier: string,
  intent: PublicAppointmentIntent,
): Promise<unknown> {
  const { commandIdentifier, ...body } = intent;
  return postJson(
    `/prototype/public/merchants/${encodeURIComponent(merchantIdentifier)}/appointments`,
    commandIdentifier,
    body,
  );
}

export async function confirmPublicBooking(
  merchantIdentifier: string,
  intent: PublicBookingIntent,
): Promise<unknown> {
  const { commandIdentifier, ...body } = intent;
  return postJson(
    `/prototype/public/merchants/${encodeURIComponent(merchantIdentifier)}/bookings`,
    commandIdentifier,
    body,
  );
}

export async function commitPublicOrder(
  merchantIdentifier: string,
  intent: PublicOrderIntent,
): Promise<unknown> {
  const { commandIdentifier, ...body } = intent;
  return postJson(
    `/prototype/public/merchants/${encodeURIComponent(merchantIdentifier)}/orders`,
    commandIdentifier,
    body,
  );
}

async function getJson<T>(path: string): Promise<T> {
  const response = await fetch(`${BACKEND_URL}${path}`, {
    cache: "no-store",
    headers: {
      Accept: "application/json",
    },
  });

  return readJson<T>(response, path);
}

async function postJson<T>(
  path: string,
  commandIdentifier: string,
  body: unknown,
): Promise<T> {
  const response = await fetch(`${BACKEND_URL}${path}`, {
    method: "POST",
    cache: "no-store",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      "Idempotency-Key": commandIdentifier,
    },
    body: JSON.stringify(body),
  });

  return readJson<T>(response, path);
}

async function readJson<T>(response: Response, path: string): Promise<T> {
  if (!response.ok) {
    throw new GrandRueBackendResponseError(response.status, path);
  }

  return (await response.json()) as T;
}
