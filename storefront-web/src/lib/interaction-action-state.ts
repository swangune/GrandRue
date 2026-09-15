export type InteractionActionStatus =
  | "idle"
  | "completed"
  | "rejected"
  | "uncertain";

export type InteractionActionState = {
  status: InteractionActionStatus;
  message: string | null;
  commandIdentifier: string;
  objectIdentifier: string;
  portionIdentifier?: string;
};

export function initialInteractionState(
  commandIdentifier: string,
  objectIdentifier: string,
  portionIdentifier?: string,
): InteractionActionState {
  return {
    status: "idle",
    message: null,
    commandIdentifier,
    objectIdentifier,
    ...(portionIdentifier ? { portionIdentifier } : {}),
  };
}

export function completedInteractionState(
  current: InteractionActionState,
): InteractionActionState {
  return {
    ...current,
    status: "completed",
    message: "Completed successfully.",
  };
}

export function uncertainInteractionState(
  current: InteractionActionState,
): InteractionActionState {
  return {
    ...current,
    status: "uncertain",
    message:
      "We could not confirm the authoritative outcome. Retry this same request to resolve it safely.",
  };
}

export function rejectedInteractionState(
  current: InteractionActionState,
  fresh: {
    commandIdentifier: string;
    objectIdentifier: string;
    portionIdentifier?: string;
  },
  message =
    "The authoritative backend rejected this request. Change the details and try again.",
): InteractionActionState {
  return {
    ...current,
    ...fresh,
    status: "rejected",
    message,
  };
}
