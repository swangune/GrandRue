import type {
  PublicAppointmentIntent,
  PublicBookingIntent,
  PublicOrderIntent,
} from "./grandrue-api";

export function appointmentIntentFromFormData(
  formData: FormData,
): PublicAppointmentIntent {
  const startsAt = isoInstant(formData, "startsAt");
  const endsAt = isoInstant(formData, "endsAt");
  requireAscendingWindow(startsAt, endsAt);

  return {
    commandIdentifier: text(formData, "commandIdentifier"),
    appointmentIdentifier: text(formData, "objectIdentifier"),
    subjectReference: text(formData, "subjectReference"),
    startsAt,
    endsAt,
  };
}

export function bookingIntentFromFormData(
  formData: FormData,
): PublicBookingIntent {
  const startsAt = isoInstant(formData, "startsAt");
  const endsAt = isoInstant(formData, "endsAt");
  requireAscendingWindow(startsAt, endsAt);

  return {
    commandIdentifier: text(formData, "commandIdentifier"),
    bookingIdentifier: text(formData, "objectIdentifier"),
    subjectReference: text(formData, "subjectReference"),
    startsAt,
    endsAt,
  };
}

export function orderIntentFromFormData(formData: FormData): PublicOrderIntent {
  const quantity = Number(text(formData, "quantity"));
  if (!Number.isFinite(quantity) || quantity <= 0) {
    throw new Error("Quantity must be greater than zero");
  }

  return {
    commandIdentifier: text(formData, "commandIdentifier"),
    orderIdentifier: text(formData, "objectIdentifier"),
    portions: [
      {
        portionIdentifier: text(formData, "portionIdentifier"),
        subjectReference: text(formData, "subjectReference"),
        quantity,
      },
    ],
  };
}

function text(formData: FormData, key: string): string {
  const value = formData.get(key);
  if (typeof value !== "string" || value.trim().length === 0) {
    throw new Error(`${key} is required`);
  }
  return value.trim();
}

function isoInstant(formData: FormData, key: string): string {
  const value = text(formData, key);
  const instant = new Date(value);
  if (Number.isNaN(instant.getTime())) {
    throw new Error(`${key} must be an ISO instant`);
  }
  return instant.toISOString();
}

function requireAscendingWindow(startsAt: string, endsAt: string): void {
  if (Date.parse(endsAt) <= Date.parse(startsAt)) {
    throw new Error("End time must be after start time");
  }
}
