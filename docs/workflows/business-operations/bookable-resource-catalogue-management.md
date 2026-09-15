# BO-12 — Hybrid & Bookable-Resource Catalogue Management

> **Workflow ID:** BO-12
> **Module:** Business Operations
> **Capability:** Catalogue Management
> **Applies To:** Hybrid and Bookable-Resource / Hybrid businesses
> **Status:** Beta Design

---

# 1. Purpose

This workflow defines how Main Street enables hybrid and bookable-resource/hybrid businesses to create, organise, manage and publish everything they offer to customers.

The workflow must support businesses that sell:

* Products
* Services
* Bookable resources
* Combinations of products, services and bookable resources

The merchant should manage all offerings through one simple catalogue interface.

Main Street shall determine the operational complexity required by each catalogue item without requiring the merchant to understand the underlying technical model.

---

# 2. Business Models Covered

## 2.1 Hybrid

A hybrid business sells both products and services.

Examples:

* Mechanic
* Beauty business selling products and treatments
* Retailer providing installation services
* Bakery providing catering services

Example:

```text
Mechanic
│
├── Products
│   ├── Engine Oil
│   ├── Tyres
│   └── Brake Pads
│
└── Services
    ├── Vehicle Service
    ├── Diagnostics
    └── Brake Replacement
```

---

# 2.2 Bookable-Resource / Hybrid

A Bookable-Resource / Hybrid business sells products or services while also selling access to resources whose availability is constrained by time, capacity or occupancy.

Examples:

* Motel
* Inn
* Hotel
* Guesthouse
* Restaurant with table reservations
* Salon with treatment rooms
* Clinic
* Vehicle rental business
* Event venue

Example:

```text
Motel
│
├── Bookable Resources
│   ├── Double Room
│   └── Family Room
│
├── Products
│   ├── Breakfast
│   ├── Drinks
│   └── Snacks
│
└── Services
    ├── Room Service
    └── Late Checkout
```

---

# 3. Core Objective

The merchant should be able to answer one simple question:

> **What do you offer your customers?**

Main Street then determines what operational structure is required.

The merchant should not need to understand:

* inventory architecture
* resource allocation
* availability engines
* booking logic
* service scheduling
* catalogue data structures
* website publishing mechanisms
* POS integration
* payment orchestration

Main Street absorbs that complexity.

---

# 4. Catalogue Model

Every customer-facing catalogue item shall have an **Offering**.

An Offering may represent:

```text
Offering
│
├── Product
│
├── Service
│
├── Bookable Resource
│
└── Composite Offering
```

A composite offering may combine multiple underlying items.

For example:

```text
Weekend Stay
│
├── Room
├── Breakfast
└── Late Checkout
```

The customer sees one offering.

Main Street manages the underlying components.

---

# 5. Merchant Catalogue Structure

The merchant catalogue shall be organised around what customers can purchase or book.

```text
Merchant Catalogue
│
├── Products
├── Services
├── Bookable Resources
└── Packages / Composite Offerings
```

The merchant may organise these into customer-facing categories.

Examples:

```text
Motel
│
├── Accommodation
│   ├── Double Room
│   └── Family Room
│
├── Dining
│   ├── Breakfast
│   ├── Lunch
│   └── Drinks
│
└── Services
    ├── Room Service
    └── Late Checkout
```

The internal architecture may be more complex, but the merchant-facing catalogue remains simple.

---

# 6. Workflow Overview

```text
Merchant
   │
   ▼
Open Catalogue
   │
   ▼
Add Offering
   │
   ▼
Select Offering Type
   │
   ├──────────────┬──────────────┬─────────────────┐
   ▼              ▼              ▼                 ▼
Product        Service       Bookable          Composite
                              Resource           Offering
   │              │              │                 │
   └──────────────┴──────────────┴─────────────────┘
                              │
                              ▼
                     Main Street configures
                     required capabilities
                              │
                              ▼
                       Merchant reviews
                              │
                              ▼
                           Publish
                              │
                 ┌────────────┴────────────┐
                 ▼                         ▼
            Merchant POS            Merchant Website
```

---

# 7. Step 1 — Merchant Opens Catalogue

The merchant opens the Catalogue section from the merchant dashboard.

The interface should immediately show:

* Existing offerings
* Offering status
* Price
* Availability where applicable
* Stock where applicable
* Published/unpublished status
* Relevant operational information

The merchant should not be presented with technical configuration.

---

# 8. Step 2 — Add Offering

The merchant selects:

> **Add Offering**

Main Street asks:

> **What are you offering customers?**

The merchant selects:

* Product
* Service
* Bookable resource
* Package / combination

---

# 9. Product Workflow

If the merchant selects **Product**, Main Street requests only the information necessary to sell the product.

Typical information includes:

* Product name
* Description
* Price
* Images
* Category
* Stock information where applicable
* Tax information where required
* Product options or variants where applicable

Main Street determines whether inventory management is required.

For example:

```text
Breakfast
£12
```

may be treated as a product without requiring the merchant to configure a complex inventory system.

If the business later requires ingredient or stock management, that capability can be activated independently.

---

# 10. Service Workflow

If the merchant selects **Service**, Main Street requests information relevant to the service.

Examples:

* Service name
* Description
* Price
* Duration where applicable
* Category
* Staff requirement where applicable
* Resource requirement where applicable
* Customer-facing information

Example:

```text
Room Service
£15
```

If staff participation is required, Main Street can associate the service with authorised staff.

If staff scheduling is not used by the business, the merchant should not be forced to configure a schedule.

---

# 11. Bookable Resource Workflow

If the merchant selects **Bookable Resource**, Main Street asks only the questions necessary to establish the resource.

For example:

> **What is being booked?**

The merchant selects or enters:

> Room

Main Street then requests relevant information such as:

* Resource name/type
* Capacity
* Quantity
* Price or pricing rule
* Availability requirements
* Booking duration
* Booking rules
* Customer-facing description
* Images
* Relevant amenities or attributes

Example:

```text
Double Room

Capacity: 2
Quantity: 4
Price: £85/night
Booking unit: Night
```

Main Street then creates the underlying resource structure.

The merchant does not need to understand resource allocation or availability calculations.

---

# 12. Resource Types

Bookable resources should be generic.

Examples include:

```text
Accommodation
├── Room
├── Apartment
└── Cabin

Facilities
├── Meeting Room
├── Treatment Room
└── Event Space

Equipment
├── Vehicle
├── Machinery
└── Rental Equipment

Operational Resources
├── Table
├── Service Bay
└── Other capacity-constrained resources
```

Main Street should not create separate catalogue architecture for every industry.

---

# 13. Resource Quantity

Where multiple equivalent resources exist, the merchant should be able to define a quantity rather than manually configure every resource when unnecessary.

Example:

```text
Double Room
Quantity: 6
Price: £85/night
```

Main Street can represent the six available units internally.

If the merchant needs individual resource management, Main Street may expose individual resources.

For example:

```text
Double Room
├── Room 1
├── Room 2
├── Room 3
├── Room 4
├── Room 5
└── Room 6
```

The merchant should only see this level of detail when it provides operational value.

---

# 14. Composite Offering Workflow

The merchant may create an offering that combines multiple catalogue components.

Example:

```text
Bed & Breakfast Package
│
├── Double Room
├── Breakfast
└── Late Checkout
```

Main Street shall associate the underlying components and manage their operational requirements.

A composite offering may therefore require:

* Product allocation
* Service fulfilment
* Resource reservation
* Staff allocation
* Availability validation
* Payment

The customer should experience this as one offering.

---

# 15. Example — Small Motel

A six-room motel begins onboarding.

The merchant selects:

> **Bookable-Resource / Hybrid**

Main Street recommends:

* Accommodation
* Products
* Services
* Bookings
* Customers
* Payments
* Website
* POS where required

The merchant creates:

### Accommodation

```text
Double Room
£85/night
6 rooms
```

### Product

```text
Breakfast
£12/person
```

### Product

```text
Soft Drink
£2.50
```

### Service

```text
Late Checkout
£20
```

The merchant does not need to configure separate hotel, restaurant, POS, inventory and booking systems.

Main Street composes the necessary capabilities.

---

# 16. Customer Website Publishing

Every published offering may appear on the merchant's Main Street website.

However, the customer interaction depends on the offering type.

### Product

```text
Breakfast
£12
[Add to Order]
```

### Service

```text
Late Checkout
£20
[Add]
```

### Bookable Resource

```text
Double Room
£85/night
[Check Availability]
```

### Composite Offering

```text
Bed & Breakfast
£105/night
[Book]
```

The customer sees one coherent merchant website.

The underlying operational differences remain inside Main Street.

---

# 17. POS Integration

Catalogue items must be usable by the merchant's operational interface.

Products may be:

* Added to orders
* Sold through POS
* Associated with customers
* Paid for

Services may be:

* Added to orders
* Scheduled where required
* Assigned to staff where required
* Paid for

Bookable resources may be:

* Reserved
* Associated with customers
* Included in bookings
* Paid for

Composite offerings may trigger multiple underlying operations while appearing as one commercial item.

---

# 18. Availability Integration

Bookable resources must integrate with the Availability capability.

Availability may consider:

* Resource quantity
* Existing bookings
* Booking duration
* Business rules
* Resource status
* Staff availability where required
* Other operational constraints

A catalogue item must never be shown as available merely because its catalogue record exists.

Availability must be determined by the relevant operational capability.

---

# 19. Staff Integration

Staff participation is conditional.

A merchant may define:

> This service requires staff.

Or:

> This service does not require staff.

A bookable resource may also require staff participation.

Example:

```text
Treatment Room
+
Qualified Therapist
```

Main Street must be capable of evaluating both resource and staff requirements.

However, businesses that do not use staff scheduling must not be forced to create staff schedules.

---

# 20. Catalogue Changes

Merchants may modify:

* Name
* Description
* Price
* Images
* Categories
* Availability rules
* Stock information
* Resource quantity
* Service duration
* Booking rules
* Customer-facing information

Changes must respect existing operational records.

For example, changing a room's description must not alter historical bookings.

Changing a price must not retroactively alter completed transactions.

---

# 21. Deactivation

An offering may be:

```text
Draft
Active
Paused
Archived
```

Pausing an offering prevents new customer transactions where appropriate.

Existing bookings or orders remain valid unless the merchant explicitly initiates a cancellation or modification workflow.

Archiving removes the offering from normal customer-facing catalogue views while preserving historical records.

---

# 22. Business Growth

The catalogue must support progressive complexity.

A small motel may initially use:

```text
Rooms
Breakfast
Payments
Website
```

Later it may add:

```text
Staff
Restaurant
Inventory
Room Service
Packages
Advanced Availability
```

The merchant should not need to migrate to another system.

Main Street activates additional capabilities as required.

---

# 23. Merchant Experience Principle

The catalogue interface shall follow:

> **Simple input → intelligent configuration → operational result**

The merchant provides business information.

Main Street determines:

* Required data structures
* Required workflows
* Required integrations
* Availability mechanisms
* Order mechanisms
* Booking mechanisms
* Website presentation
* POS behaviour

The merchant should not be required to configure the underlying architecture.

---

# 24. Business Rules

* Every customer-facing catalogue item is represented as an Offering.
* An Offering may represent a Product, Service, Bookable Resource or Composite Offering.
* Hybrid businesses may use Products and Services simultaneously.
* Bookable-Resource / Hybrid businesses may use Products, Services and Bookable Resources simultaneously.
* A Bookable Resource must have an availability model.
* A catalogue entry does not itself determine availability.
* Resource availability is determined by the appropriate operational capability.
* Staff requirements are conditional.
* Staff scheduling is conditional.
* Product inventory is conditional.
* Merchants control their catalogue.
* Main Street handles underlying operational complexity.
* Catalogue changes must preserve historical transaction integrity.
* Deactivated offerings must not unnecessarily affect historical records.
* Businesses must be able to start with minimal configuration.
* Additional capabilities may be activated as business requirements increase.
* The same catalogue architecture must support different industries without requiring industry-specific catalogue systems.

---

# 25. Success Criteria

The workflow is successful when:

* A hybrid merchant can manage products and services from one catalogue.
* A bookable-resource/hybrid merchant can manage resources, products and services from one catalogue.
* A small motel can manage rooms and breakfast without configuring unnecessary systems.
* Rooms can appear on the merchant website as customer-facing offerings.
* Customers can check room availability and book.
* Products can be sold through the merchant's operational interface.
* Services can be purchased or booked where applicable.
* Composite offerings can combine multiple operational components.
* Resource availability is correctly enforced.
* Staff involvement is only required when the business needs it.
* The merchant does not need to understand the underlying technical architecture.
* The same infrastructure can support multiple business types.
* Additional capabilities can be introduced without redesigning the merchant's catalogue.

---

# 26. Architectural Principle

Main Street's catalogue is not simply a list of products.

It is the **commercial representation of what a business offers its customers**.

The catalogue must therefore support different forms of commerce:

```text
Product
   ↓
Sell

Service
   ↓
Perform / Sell / Schedule

Bookable Resource
   ↓
Reserve / Allocate

Composite Offering
   ↓
Coordinate multiple commercial and operational components
```

The merchant experiences these as one catalogue.

Main Street handles the underlying complexity.

> **The merchant defines what the business offers. Main Street determines how that offering must be operated.**

---

# 27. Relationship to Main Street Architecture

This workflow establishes an important architectural boundary:

```text
Merchant
   │
   ▼
Catalogue
   │
   ▼
Offering
   │
   ├── Product ──────────► Order / POS
   │
   ├── Service ──────────► Service Fulfilment
   │
   ├── Resource ─────────► Availability / Booking
   │
   └── Composite ────────► Orchestration
                              │
                              ▼
                     Multiple capabilities
```

This allows Main Street to serve small and complex businesses through the same infrastructure without forcing either group into the other's operating model.

---

# 28. Design Statement

Main Street shall provide a unified catalogue infrastructure for businesses whose operations combine products, services and bookable resources.

The merchant should experience the catalogue as a simple representation of the business.

Main Street shall absorb the complexity required to:

* Sell products
* Deliver services
* Reserve resources
* Manage availability
* Coordinate staff where necessary
* Combine offerings
* Publish offerings online
* Process operational transactions
* Maintain historical integrity

The objective is not to build separate catalogue systems for motels, restaurants, mechanics, salons or other industries.

The objective is to provide a **composable catalogue infrastructure** that adapts to the actual business.

This enables a small motel, for example, to manage accommodation, breakfast, services, customers, bookings, payments and its merchant website through the same Main Street infrastructure without requiring the merchant to understand or configure the complexity underneath.
