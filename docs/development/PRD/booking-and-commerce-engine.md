# **20. Booking & Commerce Engine**

## **20.1 Purpose**

The Booking & Commerce Engine is the operational core of Main Street.

It enables merchants to accept appointments, sell products and manage customer interactions through a single, unified transaction system.

Rather than maintaining separate systems for bookings and sales, Main Street treats every customer action as a business interaction with specialised behaviour depending on the business type.

This unified approach simplifies implementation while providing a consistent experience for merchants and customers.

---

# **20.2 Design Philosophy**

The Booking & Commerce Engine is built upon a single principle:

> **Every customer interaction is a business transaction.**

Whether a customer:

* books a haircut,
* orders flowers,
* purchases vehicle parts,
* reserves a table,
* books a consultation,
* schedules a repair,

the underlying workflow remains fundamentally the same.

The differences exist only in business rules—not in the system architecture.

---

# **20.3 Objectives**

The Booking & Commerce Engine shall:

* Support product sales.
* Support appointment bookings.
* Support hybrid businesses.
* Minimise customer friction.
* Maintain transaction history.
* Feed CRM automatically.
* Integrate with analytics.
* Enable AI recommendations.
* Support future expansion.

---

# **20.4 Business Types**

The engine supports three operational modes.

## Product Businesses

Examples:

* Retail shop
* Florist
* Bakery
* Electronics shop
* Pharmacy

Primary interaction:

> Purchase

---

## Service Businesses

Examples:

* Hairdresser
* Barber
* Accountant
* Solicitor
* Dentist
* Electrician

Primary interaction:

> Booking

---

## Hybrid Businesses

Examples:

* Salon selling products
* Bicycle repair shop
* Garage
* Spa
* Veterinary practice

Primary interactions:

* Purchases
* Bookings

Both workflows coexist seamlessly.

---

# **20.5 Unified Interaction Model**

Every transaction creates an **Interaction**.

Interactions become the foundation of:

* CRM.
* Analytics.
* Marketing.
* Customer history.
* AI recommendations.
* Reporting.

Regardless of interaction type, common information includes:

* Customer
* Merchant
* Date
* Status
* Payment
* Communication history
* Notes

---

# **20.6 Customer Journey**

Every interaction follows the same lifecycle.

1. Customer discovers business.
2. Customer selects product or service.
3. Customer provides required information.
4. Customer confirms interaction.
5. Merchant receives notification.
6. Customer receives confirmation.
7. Interaction is completed.
8. Follow-up communications begin.

This consistency simplifies customer expectations.

---

# **20.7 Product Purchase Flow**

Typical purchase workflow:

Browse products →

View product →

Add to basket →

Checkout →

Payment →

Confirmation →

Fulfilment →

Completion

Optional fulfilment methods:

* Collection
* Local delivery
* Shipping (merchant dependent)

---

# **20.8 Booking Flow**

Typical booking workflow:

Browse services →

Choose service →

Select staff (optional) →

Choose date →

Choose time →

Confirm →

Receive confirmation →

Attend appointment →

Completion

---

# **20.9 Hybrid Workflow**

Hybrid businesses may combine both experiences.

Example:

A customer books:

* Hair colouring

while simultaneously purchasing:

* Shampoo
* Conditioner
* Hair treatment

The customer completes everything during one checkout.

---

# **20.10 Guest Customers**

Main Street encourages low-friction interactions.

Customers should be able to continue as guests wherever practical.

Guest customers provide only information necessary to complete the interaction.

Examples:

* Name
* Email
* Phone

No Main Street account is required.

---

# **20.11 Registered Customers**

Merchants may offer optional customer accounts.

Benefits include:

* Faster checkout.
* Booking history.
* Order history.
* Saved addresses.
* Saved preferences.
* Repeat purchasing.

Accounts belong to the merchant.

Not Main Street.

---

# **20.12 Merchant Notifications**

Merchants should receive immediate notifications when interactions occur.

Notification channels may include:

* Dashboard
* Email
* Push notification
* SMS (future)

Notifications should include only relevant information.

---

# **20.13 Customer Notifications**

Customers receive confirmations directly from the merchant.

Examples include:

* Booking confirmation.
* Order confirmation.
* Appointment reminder.
* Collection ready.
* Delivery update.
* Cancellation notice.

Main Street operates invisibly.

Customers perceive communications as originating from the business.

---

# **20.14 Status Management**

Every interaction includes a lifecycle status.

Examples:

Product Orders

* Pending
* Confirmed
* Preparing
* Ready
* Completed
* Cancelled
* Refunded

Appointments

* Requested
* Confirmed
* Rescheduled
* In Progress
* Completed
* Cancelled
* No Show

Status updates should trigger appropriate customer notifications.

---

# **20.15 Payments**

Where enabled, payments are processed through Stripe Connect.

Main Street facilitates the transaction.

The merchant remains the Merchant of Record.

Supported payment models include:

* Immediate payment
* Deposit
* Pay on collection
* Pay after service
* Free booking

Payment options depend on merchant configuration.

---

# **20.16 Calendar Integration**

Appointment businesses require scheduling capabilities.

Supported features include:

* Working hours.
* Staff availability.
* Holiday closures.
* Appointment duration.
* Buffer time.
* Break periods.
* Maximum daily bookings.

The system should prevent double bookings automatically.

---

# **20.17 Inventory Integration**

For product businesses, inventory may optionally track:

* Quantity available.
* Reserved stock.
* Low stock warnings.
* Out-of-stock status.

Inventory management remains intentionally lightweight to avoid unnecessary complexity for small businesses.

---

# **20.18 CRM Integration**

Every completed interaction automatically enriches the merchant's CRM.

Captured information may include:

* Customer identity.
* Purchase history.
* Booking history.
* Preferred services.
* Preferred products.
* Visit frequency.
* Total lifetime value.

No duplicate data entry is required.

---

# **20.19 Analytics Integration**

Interaction data powers business insights such as:

* Revenue.
* Booking rates.
* Conversion rates.
* Repeat customers.
* Average order value.
* Peak booking periods.
* Popular products.
* Popular services.

Insights focus on helping merchants make better business decisions.

---

# **20.20 AI Integration**

AI may assist by:

* Predicting busy periods.
* Suggesting discounts during quiet periods.
* Identifying repeat customers.
* Recommending products.
* Recommending services.
* Drafting follow-up campaigns.

AI provides recommendations rather than autonomous decision-making unless explicitly enabled by the merchant.

---

# **20.21 Security**

Customer information must be handled securely.

The platform shall:

* Encrypt sensitive information.
* Validate inputs.
* Protect payment information.
* Respect privacy regulations.
* Maintain complete audit trails.

---

# **20.22 Commerce Statement**

The Booking & Commerce Engine unifies appointments, product sales and customer interactions into a single operational model.

Rather than forcing merchants to manage separate systems for bookings, orders, CRM and customer communications, Main Street provides one integrated engine that adapts to the needs of product, service and hybrid businesses.

This unified architecture reduces complexity while providing the flexibility required to support a diverse range of local businesses.

---

### End of Section 20 – Booking & Commerce Engine
