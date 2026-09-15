import Link from "next/link";

const EXAMPLES = [
  {
    merchantIdentifier: "prototype-gardener-showcase",
    label: "Showcase gardener",
    detail: "Published work only in the current executable surface",
  },
  {
    merchantIdentifier: "prototype-gardener-bookable",
    label: "Bookable gardener",
    detail: "The same trade with an additional Appointment interaction",
  },
  {
    merchantIdentifier: "prototype-motel",
    label: "Motel",
    detail: "Booking contribution",
  },
  {
    merchantIdentifier: "prototype-retailer",
    label: "Retailer",
    detail: "Ordering contribution",
  },
  {
    merchantIdentifier: "prototype-publisher",
    label: "Information publisher",
    detail: "Publication content without commerce",
  },
] as const;

export default function PrototypeIndex() {
  return (
    <main>
      <section className="hero">
        <p className="eyebrow">Storefront composition prototype</p>
        <h1>One storefront runtime. Different merchant choices.</h1>
        <p className="hero-copy">
          These examples use the same Next.js application. The visible surface
          changes because the backend returns different registered surface
          contributions, not because the frontend selects a business template.
        </p>
      </section>

      <section className="surface-section">
        <h2>Reference merchants</h2>
        <div className="example-grid">
          {EXAMPLES.map((example) => (
            <Link
              className="example-link"
              href={`/m/${example.merchantIdentifier}`}
              key={example.merchantIdentifier}
            >
              <strong>{example.label}</strong>
              <span>{example.detail}</span>
            </Link>
          ))}
        </div>
      </section>
    </main>
  );
}
