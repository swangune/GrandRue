import { GrandRueBackendResponseError } from "./grandrue-api";
import {
  rejectedInteractionState,
  uncertainInteractionState,
  type InteractionActionState,
} from "./interaction-action-state";

export type FreshInteractionIdentity = {
  commandIdentifier: string;
  objectIdentifier: string;
  portionIdentifier?: string;
};

export function classifyInteractionAttemptFailure(
  error: unknown,
  current: InteractionActionState,
  freshIdentity: FreshInteractionIdentity,
): InteractionActionState {
  if (
    error instanceof GrandRueBackendResponseError &&
    error.status >= 400 &&
    error.status < 500
  ) {
    return rejectedInteractionState(current, freshIdentity);
  }

  return uncertainInteractionState(current);
}
