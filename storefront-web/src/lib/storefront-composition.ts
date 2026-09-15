export type StorefrontInteractionBinding = {
  subjectReference: string;
  label: string;
};

export type StorefrontInteractionKind = "appointment" | "booking" | "order";

export type StorefrontSurfaceContribution = {
  ownerCapabilityIdentifier: string;
  contributionIdentifier: string;
  kind: string;
  supportedOperationReferences: string[];
  bindings: StorefrontInteractionBinding[];
};

export type StorefrontSurfaceGroup = {
  compositionTargetReference: string;
  contributions: StorefrontSurfaceContribution[];
};

export type StorefrontSurfaceResponse = {
  merchantIdentifier: string;
  configurationIdentifier: string;
  releaseIdentifier: string;
  groups: StorefrontSurfaceGroup[];
};

export type StorefrontAction = {
  contributionKey: string;
  label: string;
  interactionKind: StorefrontInteractionKind;
  bindings: StorefrontInteractionBinding[];
};

export type ComposedStorefront = {
  contentEnabled: boolean;
  actions: StorefrontAction[];
  unsupportedContributionKeys: string[];
};

type RegisteredPresentation =
  | { type: "content" }
  | {
      type: "action";
      label: string;
      interactionKind: StorefrontInteractionKind;
    };

const PRESENTATION_REGISTRY: Readonly<Record<string, RegisteredPresentation>> = {
  "publication/browse-published-content": { type: "content" },
  "appointment/arrange-appointment": {
    type: "action",
    label: "Arrange a time",
    interactionKind: "appointment",
  },
  "booking/reserve-subject": {
    type: "action",
    label: "Reserve",
    interactionKind: "booking",
  },
  "ordering/place-order": {
    type: "action",
    label: "Order",
    interactionKind: "order",
  },
};

export function composeStorefront(
  surface: StorefrontSurfaceResponse,
): ComposedStorefront {
  let contentEnabled = false;
  const actions: StorefrontAction[] = [];
  const unsupportedContributionKeys: string[] = [];

  for (const group of surface.groups) {
    for (const contribution of group.contributions) {
      const contributionKey = `${contribution.ownerCapabilityIdentifier}/${contribution.contributionIdentifier}`;
      const presentation = PRESENTATION_REGISTRY[contributionKey];

      if (!presentation) {
        unsupportedContributionKeys.push(contributionKey);
        continue;
      }

      if (presentation.type === "content") {
        contentEnabled = true;
        continue;
      }

      actions.push({
        contributionKey,
        label: presentation.label,
        interactionKind: presentation.interactionKind,
        bindings: contribution.bindings.map((binding) => ({ ...binding })),
      });
    }
  }

  return {
    contentEnabled,
    actions,
    unsupportedContributionKeys,
  };
}
