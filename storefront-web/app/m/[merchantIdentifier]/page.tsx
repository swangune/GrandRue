import { randomUUID } from "node:crypto";
import Link from "next/link";

import {
  OrderInteractionForm,
  TimeWindowInteractionForm,
} from "@/src/components/public-interaction-forms";
import { getPublications, getStorefrontSurface } from "@/src/lib/grandrue-api";
import {
  composeStorefront,
  type StorefrontAction,
  type StorefrontInteractionBinding,
} from "@/src/lib/storefront-composition";

import {
  submitPublicAppointment,
  submitPublicBooking,
  submitPublicOrder,
} from "./actions";

type MerchantPageProps = {
  params: Promise<{ merchantIdentifier: string }>;
};

export default async function MerchantStorefront({ params }: MerchantPageProps) {
  const { merchantIdentifier } = await params;
  const surface = await getStorefrontSurface(merchantIdentifier);
  const composition = composeStorefront(surface);
  const publications = composition.contentEnabled
    ? await getPublications(merchantIdentifier)
    : [];

  return (
    <main>
      <nav aria-label="Storefront navigation" className="prototype-bar">
        <Link href="/">← Back</Link>
      </nav>

      <section className="hero">
        <p className="eyebrow">Merchant storefront</p>
        <h1>{publications[0]?.title ?? "Welcome"}</h1>
        <p className="hero-copy">
          {publications[0]?.summary ??
            "Explore the information and actions currently available from this merchant."}
        </p>
      </section>

      {composition.contentEnabled ? (
        <section className="surface-section">
          <h2>Latest information</h2>
          {publications.length > 0 ? (
            <div className="content-grid">
              {publications.map((publication) => (
                <article className="card" key={publication.publicationIdentifier}>
                  <h3>{publication.title}</h3>
                  <p>{publication.summary}</p>
                </article>
              ))}
            </div>
          ) : (
            <div className="empty-state">
              There is no published information to show right now.
            </div>
          )}
        </section>
      ) : null}

      {composition.actions.length > 0 ? (
        <section className="surface-section">
          <h2>What you can do</h2>
          <div className="surface-grid">
            {composition.actions.map((action) => (
              <article className="action-card" key={action.contributionKey}>
                <span className="action-label">{action.label}</span>
                {action.bindings.length > 0 ? (
                  <div className="binding-list">
                    {action.bindings.map((binding) => (
                      <div className="binding-item" key={binding.subjectReference}>
                        <InteractionForm
                          action={action}
                          binding={binding}
                          merchantIdentifier={merchantIdentifier}
                        />
                      </div>
                    ))}
                  </div>
                ) : (
                  <p>This option is not available right now.</p>
                )}
              </article>
            ))}
          </div>
        </section>
      ) : null}
    </main>
  );
}

function InteractionForm({
  action,
  binding,
  merchantIdentifier,
}: {
  action: StorefrontAction;
  binding: StorefrontInteractionBinding;
  merchantIdentifier: string;
}) {
  const seed = randomUUID();

  if (action.interactionKind === "appointment") {
    return (
      <TimeWindowInteractionForm
        action={submitPublicAppointment.bind(null, merchantIdentifier)}
        commandIdentifier={`storefront-appointment-intent-${seed}`}
        objectIdentifier={`appointment-${seed}`}
        subjectLabel={binding.label}
        subjectReference={binding.subjectReference}
        submitLabel="Arrange this time"
      />
    );
  }

  if (action.interactionKind === "booking") {
    return (
      <TimeWindowInteractionForm
        action={submitPublicBooking.bind(null, merchantIdentifier)}
        commandIdentifier={`storefront-booking-intent-${seed}`}
        objectIdentifier={`booking-${seed}`}
        subjectLabel={binding.label}
        subjectReference={binding.subjectReference}
        submitLabel="Reserve"
      />
    );
  }

  return (
    <OrderInteractionForm
      action={submitPublicOrder.bind(null, merchantIdentifier)}
      commandIdentifier={`storefront-order-intent-${seed}`}
      objectIdentifier={`order-${seed}`}
      portionIdentifier={`portion-${seed}`}
      subjectLabel={binding.label}
      subjectReference={binding.subjectReference}
    />
  );
}
