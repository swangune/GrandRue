"use server";

import { randomUUID } from "node:crypto";

import {
  commitPublicOrder,
  confirmPublicAppointment,
  confirmPublicBooking,
} from "@/src/lib/grandrue-api";
import { classifyInteractionAttemptFailure } from "@/src/lib/interaction-attempt-classification";
import {
  completedInteractionState,
  initialInteractionState,
  rejectedInteractionState,
  type InteractionActionState,
} from "@/src/lib/interaction-action-state";
import {
  appointmentIntentFromFormData,
  bookingIntentFromFormData,
  orderIntentFromFormData,
} from "@/src/lib/storefront-interaction-intents";

export async function submitPublicAppointment(
  merchantIdentifier: string,
  previousState: InteractionActionState,
  formData: FormData,
): Promise<InteractionActionState> {
  let intent;
  try {
    intent = appointmentIntentFromFormData(formData);
  } catch {
    return rejectedInteractionState(
      previousState,
      freshIdentity("appointment"),
      "The request details were invalid. Correct them and try again.",
    );
  }

  const current = initialInteractionState(
    intent.commandIdentifier,
    intent.appointmentIdentifier,
  );
  try {
    await confirmPublicAppointment(merchantIdentifier, intent);
    return completedInteractionState(current);
  } catch (error) {
    return classifyInteractionAttemptFailure(
      error,
      current,
      freshIdentity("appointment"),
    );
  }
}

export async function submitPublicBooking(
  merchantIdentifier: string,
  previousState: InteractionActionState,
  formData: FormData,
): Promise<InteractionActionState> {
  let intent;
  try {
    intent = bookingIntentFromFormData(formData);
  } catch {
    return rejectedInteractionState(
      previousState,
      freshIdentity("booking"),
      "The request details were invalid. Correct them and try again.",
    );
  }

  const current = initialInteractionState(
    intent.commandIdentifier,
    intent.bookingIdentifier,
  );
  try {
    await confirmPublicBooking(merchantIdentifier, intent);
    return completedInteractionState(current);
  } catch (error) {
    return classifyInteractionAttemptFailure(
      error,
      current,
      freshIdentity("booking"),
    );
  }
}

export async function submitPublicOrder(
  merchantIdentifier: string,
  previousState: InteractionActionState,
  formData: FormData,
): Promise<InteractionActionState> {
  let intent;
  try {
    intent = orderIntentFromFormData(formData);
  } catch {
    return rejectedInteractionState(
      previousState,
      freshIdentity("order", true),
      "The request details were invalid. Correct them and try again.",
    );
  }

  const current = initialInteractionState(
    intent.commandIdentifier,
    intent.orderIdentifier,
    intent.portions[0]?.portionIdentifier,
  );
  try {
    await commitPublicOrder(merchantIdentifier, intent);
    return completedInteractionState(current);
  } catch (error) {
    return classifyInteractionAttemptFailure(
      error,
      current,
      freshIdentity("order", true),
    );
  }
}

function freshIdentity(
  interaction: "appointment" | "booking" | "order",
  includePortion = false,
): {
  commandIdentifier: string;
  objectIdentifier: string;
  portionIdentifier?: string;
} {
  const seed = randomUUID();
  return {
    commandIdentifier: `storefront-${interaction}-intent-${seed}`,
    objectIdentifier: `${interaction}-${seed}`,
    ...(includePortion ? { portionIdentifier: `portion-${seed}` } : {}),
  };
}
