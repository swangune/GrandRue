"use client";

import { type FormEvent, useActionState, useRef, useState } from "react";

import {
  initialInteractionState,
  type InteractionActionState,
} from "@/src/lib/interaction-action-state";

type ServerAction = (
  previousState: InteractionActionState,
  formData: FormData,
) => Promise<InteractionActionState>;

type SharedIntentProps = {
  action: ServerAction;
  commandIdentifier: string;
  objectIdentifier: string;
  subjectReference: string;
  subjectLabel: string;
};

export function TimeWindowInteractionForm({
  action,
  commandIdentifier,
  objectIdentifier,
  subjectReference,
  subjectLabel,
  submitLabel,
}: SharedIntentProps & { submitLabel: string }) {
  const [state, formAction, pending] = useActionState(
    action,
    initialInteractionState(commandIdentifier, objectIdentifier),
  );
  const startsAtLocal = useRef<HTMLInputElement>(null);
  const endsAtLocal = useRef<HTMLInputElement>(null);
  const startsAt = useRef<HTMLInputElement>(null);
  const endsAt = useRef<HTMLInputElement>(null);
  const [validationError, setValidationError] = useState<string | null>(null);

  function prepareIntent(event: FormEvent<HTMLFormElement>) {
    const startValue = startsAtLocal.current?.value;
    const endValue = endsAtLocal.current?.value;
    if (!startValue || !endValue || !startsAt.current || !endsAt.current) {
      event.preventDefault();
      setValidationError("Choose a start and end time.");
      return;
    }

    const start = new Date(startValue);
    const end = new Date(endValue);
    if (
      Number.isNaN(start.getTime()) ||
      Number.isNaN(end.getTime()) ||
      end.getTime() <= start.getTime()
    ) {
      event.preventDefault();
      setValidationError("End time must be after start time.");
      return;
    }

    startsAt.current.value = start.toISOString();
    endsAt.current.value = end.toISOString();
    setValidationError(null);
  }

  return (
    <form action={formAction} className="interaction-form" onSubmit={prepareIntent}>
      <IntentIdentityFields
        commandIdentifier={state.commandIdentifier}
        objectIdentifier={state.objectIdentifier}
        subjectReference={subjectReference}
      />
      <input ref={startsAt} name="startsAt" type="hidden" />
      <input ref={endsAt} name="endsAt" type="hidden" />

      <strong>{subjectLabel}</strong>
      <label>
        <span>Start</span>
        <input
          disabled={state.status === "completed"}
          ref={startsAtLocal}
          type="datetime-local"
          required
        />
      </label>
      <label>
        <span>End</span>
        <input
          disabled={state.status === "completed"}
          ref={endsAtLocal}
          type="datetime-local"
          required
        />
      </label>
      {validationError ? (
        <span className="form-error" role="alert">
          {validationError}
        </span>
      ) : null}
      <ActionStateMessage state={state} />
      <button disabled={pending || state.status === "completed"} type="submit">
        {pending
          ? "Submitting…"
          : state.status === "uncertain"
            ? "Retry safely"
            : state.status === "completed"
              ? "Completed"
              : submitLabel}
      </button>
    </form>
  );
}

export function OrderInteractionForm({
  action,
  commandIdentifier,
  objectIdentifier,
  subjectReference,
  subjectLabel,
  portionIdentifier,
}: SharedIntentProps & { portionIdentifier: string }) {
  const [state, formAction, pending] = useActionState(
    action,
    initialInteractionState(
      commandIdentifier,
      objectIdentifier,
      portionIdentifier,
    ),
  );

  return (
    <form action={formAction} className="interaction-form">
      <IntentIdentityFields
        commandIdentifier={state.commandIdentifier}
        objectIdentifier={state.objectIdentifier}
        subjectReference={subjectReference}
      />
      <input
        name="portionIdentifier"
        type="hidden"
        value={state.portionIdentifier ?? portionIdentifier}
      />

      <strong>{subjectLabel}</strong>
      <label>
        <span>Quantity</span>
        <input
          disabled={state.status === "completed"}
          min="1"
          name="quantity"
          required
          step="1"
          type="number"
        />
      </label>
      <ActionStateMessage state={state} />
      <button disabled={pending || state.status === "completed"} type="submit">
        {pending
          ? "Submitting…"
          : state.status === "uncertain"
            ? "Retry safely"
            : state.status === "completed"
              ? "Completed"
              : "Place order"}
      </button>
    </form>
  );
}

function IntentIdentityFields({
  commandIdentifier,
  objectIdentifier,
  subjectReference,
}: Pick<
  SharedIntentProps,
  "commandIdentifier" | "objectIdentifier" | "subjectReference"
>) {
  return (
    <>
      <input name="commandIdentifier" type="hidden" value={commandIdentifier} />
      <input name="objectIdentifier" type="hidden" value={objectIdentifier} />
      <input name="subjectReference" type="hidden" value={subjectReference} />
    </>
  );
}

function ActionStateMessage({ state }: { state: InteractionActionState }) {
  if (!state.message) {
    return null;
  }

  const className =
    state.status === "uncertain"
      ? "interaction-result interaction-result-uncertain"
      : state.status === "rejected"
        ? "interaction-result interaction-result-error"
        : "interaction-result";

  return (
    <span className={className} role="status">
      {state.message}
    </span>
  );
}
