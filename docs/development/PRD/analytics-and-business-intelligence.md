# **35. Analytics & Business Intelligence**

> **Authority note — 14 September 2026:** This PRD is lower-authority product evidence and MUST be read under composite accepted `MS-PROT-083` through v1.4. Business Intelligence is a derived analytical and decision-support capability over capability-owned business evidence, not a second source of business truth and not necessarily a standalone merchant-facing software module. Metrics require governed Analytical Measure Definitions; missing evidence is not zero; whole-business coverage must be established rather than assumed; Business Health has no canonical universal 0–100 score; probabilistic forecasts and quantitative impact estimates require currently qualified analytical methods; Business Recommendations are non-authoritative and require a new merchant instruction before normal capability execution. Composite MS-PROT-083 through v1.4 now also governs the bounded Merchant Analytical Surface, request-scoped analytical presentation projection, progressive-disclosure and presentation-fidelity requirements. Complete Financial Health remains separately governed by the accepted Financial Operations/Financial Health composition. Where wording below is less precise or conflicts, current accepted authority governs.

## **35.1 Purpose**

Analytics & Business Intelligence transforms governed business activity into meaningful insights that help merchants make better decisions.

Rather than overwhelming merchants with complex charts and technical metrics, Main Street should present simple, actionable information that answers one fundamental question:

> **"How is my business performing, what needs my attention, and what should I consider doing next?"**

Analytics should support business growth without requiring merchants to become data analysts.

---

# **35.2 Design Philosophy**

Analytics & Business Intelligence is founded on one principle:

> **Insights should lead to better-informed action without becoming action authority.**

Every surfaced metric or analytical claim should help a merchant understand performance, identify opportunities, monitor risks or solve problems, while preserving its evidence, coverage and uncertainty semantics under MS-PROT-083.

If a metric cannot be defined truthfully or influence a legitimate business decision, it should not be displayed merely because it is conventional BI vocabulary.

---

# **35.3 Objectives**

Analytics should:

* Measure supported business performance using governed Analytical Measure Definitions.
* Track supported customer behaviour under applicable data-use authority.
* Analyse sales, bookings and other capability-owned evidence without re-owning those facts.
* Evaluate marketing effectiveness where the underlying evidence and definitions are governed.
* Identify material risks and growth opportunities.
* Provide evidence-grounded Business Recommendations.
* Present information in a clear, non-technical manner.
* Preserve whether material claims are observed, derived, inferred or unknown.
* State evidence coverage rather than silently treating missing activity as zero.

---

# **35.4 Merchant Analytical Overview**

The merchant experience should provide an analytical overview appropriate to the merchant's active capabilities and available governed evidence.

Potential information may include:

* Website visitors.
* Orders.
* Bookings.
* Defined monetary measures where authoritative semantics exist.
* Returning customers where the relationship and metric are governed.
* Product performance.
* Service performance.
* Customer enquiries.
* Marketing performance.
* Business announcements.
* Applicable Business Health assessments.

The experience should prioritise material business conditions and decisions rather than require merchants to navigate a large analytics application.

Composite `MS-PROT-083` through v1.4 governs the Merchant Analytical Surface: one bounded Analytics workspace contribution, the request-scoped `business-intelligence/merchant-analytics` projection, progressive-disclosure and mandatory presentation-fidelity/honesty requirements for natural-language and visual analytical presentation across first-party merchant clients. Exact route names, pixel layout, component/chart libraries and native navigation mechanics remain implementation/presentation decisions. Exact report/export formats remain deferred under `MS-PROT-083-DQ-010`.

---

# **35.5 Website Analytics**

Where collection, data-use and metric semantics are governed, the platform may analyse website performance including:

* Total visitors.
* Unique visitors.
* Returning visitors.
* Page views.
* Popular pages.
* Device types (mobile, tablet, desktop).
* Traffic sources.
* Average session duration.

The goal is to help merchants understand how customers discover and use their website without manufacturing unsupported attribution or conversion claims.

---

# **35.6 Customer Analytics**

Where applicable data-use authority and metric definitions exist, customer insights may include:

* New customers.
* Returning customers.
* Repeat purchase rate.
* Booking frequency.
* Customer continuity/retention trends.
* Customer value measures where their financial semantics are governed.

Customer analytics should support long-term relationship understanding rather than surveillance-style individual scoring. Aggregated, cohort-based or non-identifying evidence should be preferred where individual identity is unnecessary.

---

# **35.7 Sales & Booking Analytics**

Merchants may be able to view governed observations such as:

* Orders received.
* Completed orders where completion semantics are defined by the owning authority.
* Order releases/cancellations where appropriately defined.
* Bookings made.
* Completed appointments.
* Booking cancellations.
* Defined monetary measures by period where authoritative semantics exist.
* Average order or booking value where a Measure Definition establishes the calculation.

Reports may support daily, weekly, monthly and annual views where comparison periods are materially comparable under the governing Measure Definition.

---

# **35.8 Product & Service Performance**

Where governed evidence permits, the platform may identify:

* Best-selling products.
* Most booked services.
* Products or services with materially weakening performance.
* Frequently viewed items.
* Frequently purchased items.

These insights should help merchants understand offerings without turning presentation labels such as `low-performing` into undefined analytical truth.

---

# **35.9 Announcement Performance**

Where governed evidence exists, merchants may understand the impact of announcements through measures such as:

* Number of announcement views.
* Shares to social media.
* Customer engagement.
* Website traffic associated with announcements.
* Click-through rates.
* Conversion measures where attribution semantics are accepted.

Association shall not be presented as causation unless a qualified analytical method supports the stronger claim.

---

# **35.10 Marketing Analytics**

For eligible subscription tiers and where notification/marketing-law/data-use authority permits, marketing reports may include:

* Email campaigns sent.
* Provider delivery evidence.
* Open rates where supported and lawful.
* Click rates.
* Unsubscribe rates.
* Campaign conversion measures under governed attribution semantics.

Marketing analytics should help merchants improve future campaigns without redefining Notification provider evidence or consent authority.

---

# **35.11 Search Visibility**

Where supported by accepted external/provider evidence, Main Street may provide insights into online discoverability.

Examples include:

* Search impressions.
* Website clicks from search.
* Google Business Profile interactions where available.
* Direction requests.
* Phone call clicks.
* Website visits originating from supported search sources.

External provider metrics remain evidence and must not become merchant business truth merely because they are imported.

---

# **35.12 Business Insights and AI Explanation**

Main Street may generate Business Insights and Business Recommendations from governed Analytical Observations and Claims.

Examples of merchant-facing explanations might include:

* "Bookings have been higher on Saturdays over the comparable periods we can currently observe."
* "This product is now the highest-selling product under the selected sales measure."
* "Customer enquiries increased over the defined comparison period."
* "Friday announcements have been associated with higher engagement in the available history."
* "Website traffic increased by 18% over this defined comparable period."

AI may translate governed analytical evidence into intuitive language but must not invent metrics, diagnoses, causal explanations, forecasts, quantitative impacts or recommendations from raw data.

A probabilistic or causal claim requires the applicable currently qualified Analytical Method under MS-PROT-083.

---

# **35.13 Benchmarking**

Cross-merchant benchmarking is **not authorised by the current accepted Business Intelligence authority**.

`MS-PROT-083-DQ-013` deliberately defers any future benchmarking capability until separate material governance establishes at least:

* purpose;
* cohort construction;
* business comparability;
* minimum cohort size;
* re-identification risk;
* industry/location semantics;
* data-protection and participation rules; and
* benchmark methodology.

A shared business category or anonymisation claim alone is insufficient.

---

# **35.14 Reports**

Merchants may be able to generate reports from governed Analytical Observations and other analytical artifacts.

Potential report areas include:

* Defined monetary measures.
* Orders.
* Bookings.
* Customers.
* Announcements.
* Marketing.
* Products.
* Services.
* Business Health indicators where applicable.

Report code must consume governed Measure Definitions rather than define independent formulas.

Exact report/export formats remain deferred under `MS-PROT-083-DQ-010`.

---

# **35.15 Merchant Attention**

Material Business Insights or Business Recommendations may contribute to the existing Merchant Attention model only through the accepted MS-PROT-085 integration boundary.

Examples may include:

* Significant demand change.
* Material drop in bookings.
* Capacity constraint.
* Inventory concern where Inventory semantics support it.
* Material customer-continuity change.

Business Health and Business Recommendations do not own Merchant Attention. Composite MS-PROT-085 remains authoritative for handling semantics; the Merchant Analytical Surface must not recreate an analytical inbox, task queue or handling lifecycle.

---

# **35.16 Data Visualisation**

Governed analytics may be presented using:

* Summary cards.
* Charts.
* Trend lines.
* Comparison tables.
* Business Health indicators.

Visualisations should remain simple and understandable for non-technical users and should expose sufficient context to avoid false precision. Under MS-PROT-083 v1.4, missing evidence must remain visibly missing rather than zero; materially non-comparable periods must not be presented as comparable; incompatible Measure Definition versions must not be joined into an apparently continuous series without accepted comparability semantics; different currencies remain partitioned absent accepted normalisation; and visual trend/forecast/statistical interpretation must resolve to governed analytical meaning rather than being manufactured by presentation code.

---

# **35.17 Historical Trends**

Merchants should be able to compare performance over time where the applicable Measure Definitions establish valid comparison semantics.

Potential examples include:

* Month-on-month.
* Quarter-on-quarter.
* Year-on-year.
* Equivalent trading-period comparisons.

A label such as `down 12%` must have a truthful comparison basis. Different Measure Definition versions are not automatically comparable.

---

# **35.18 Future Analytics Capabilities**

The accepted architecture may support future enhancements including:

* Sales or demand forecasting under qualified Analytical Methods.
* Inventory forecasting under Inventory-owned source semantics and qualified methods.
* Customer churn/continuity inference where data-use and method qualification permit it.
* Business Health indicator portfolios.
* Hypothetical Scenario Evaluations.
* Quantified Impact Estimates where governed evidence supports them.
* Operational staffing recommendations that do not become consequential individual-worker scoring or employment decisions.

The following are **not currently authorised merely by this PRD**:

* a universal AI-generated Business Health score;
* cross-merchant benchmarking;
* autonomous price, staffing, opening-hour, campaign, inventory-purchase or money-movement changes;
* complete Financial Health, profit, cash-flow or cash-runway semantics without separate financial authority.

---

# **35.19 Analytics & Business Intelligence Statement**

Analytics & Business Intelligence enables merchants to understand and improve their business through governed measurements, Business Health interpretation and evidence-grounded operational decision support.

Main Street should combine capability-owned operational evidence with trustworthy analytical semantics and intuitive explanation so merchants can understand what is happening, why it matters, what remains uncertain and what actions are worth considering.

The goal is not to produce more dashboards or force local-business owners to become analysts. It is to absorb analytical complexity while preserving source ownership, uncertainty and merchant decision authority.

---

### End of Section 35 – Analytics & Business Intelligence