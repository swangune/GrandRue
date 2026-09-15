# Part I – Product Foundation

**Revision:** 4 September 2026  
**Status:** Current product intent

## 1. Executive Summary

### 1.1 Purpose

Main Street is a **Digital Presence Operating System (DPOS) with an authoritative local-business operational core**. Its purpose is to remove the technical and administrative fragmentation that forces micro and small businesses to assemble websites, booking tools, commerce tools, customer communication, staff workflows and other digital systems themselves.

Main Street is not primarily a website-generation product. Website creation is increasingly commoditised by AI and other software. Main Street therefore creates durable value by modelling how a merchant operates, maintaining authoritative business state, executing supported operations reliably and exposing the resulting business through appropriate delivery surfaces.

The governing product hierarchy is:

```text
merchant business intent
        ↓
authoritative merchant operational model
        ↓
capability-owned operations and state
        ↓
governed runtime execution
        ↓
delivery surfaces
   ├── merchant dashboard
   ├── customer interactions
   ├── public website/storefront
   ├── POS/staff experiences
   ├── integrations/APIs
   └── AI-assisted interaction
```

The website is an important output of Main Street, but it is not the product's source of truth or architectural centre of gravity.

### 1.2 Vision Statement

To become the trusted digital operating infrastructure for local businesses by making both digital presence and day-to-day digital operations simple, coherent and increasingly automated.

Every legitimate local business, regardless of technical ability or financial resources, should be able to establish, operate and evolve its digital business without assembling or maintaining a collection of disconnected software systems.

### 1.3 Mission Statement

Main Street exists to simplify digital business ownership for local merchants.

Our mission is to replace fragmented tools, duplicated state, technical configuration and repetitive administration with a capability-driven operating platform that lets merchants focus on customers and business decisions.

Technology should become invisible without making business authority invisible.

The merchant remains responsible for the business. Main Street provides the reliable digital infrastructure through which supported operations are represented and executed.

### 1.4 Product Definition

Main Street is not:

- a website builder;
- a page builder or CMS;
- a bespoke application generator;
- an e-commerce product sold in isolation;
- a booking application sold in isolation;
- a CRM sold in isolation;
- an AI app generator; or
- a marketplace that owns the merchant-customer relationship.

Main Street is a capability-driven operating platform for local businesses. The market-facing DPOS description remains useful, but **digital presence is an output of the operating platform, not its authoritative core**.

The platform maintains and coordinates merchant information, configuration, operations and state such as:

- business identity and public information;
- offerings, products and services;
- customer interactions;
- bookings, appointments and schedules where enabled;
- orders, payments and fulfilment where enabled;
- inventory and availability where enabled;
- staff authority and operational access;
- workforce compensation and payroll where enabled;
- policies and merchant choices;
- operational history and audit evidence; and
- supported integrations and communications.

Those authoritative facts and operations may then be exposed through different surfaces. A website is one surface. A dashboard is another. A POS, customer flow, API or AI-assisted interaction may be another.

### 1.5 Target Market

Main Street is designed for micro and small businesses whose owners need professional digital capability without becoming software operators.

The platform must support materially different local-business operating models through registered capabilities and composition rather than business-specific application forks. Physical-premises businesses, mobile/service-area businesses, online consultants and information-oriented organisations may all be valid where their required behaviour is represented by supported semantics and product policy.

### 1.6 The Problem

The core problem is no longer merely that small businesses struggle to create websites.

AI and modern software are rapidly reducing the cost of generating pages and application code. The persistent problem is that merchants still have to operate a coherent business across disconnected systems whose data, permissions, workflows and state can diverge.

Typical fragmentation includes:

- website and public information;
- customer enquiries;
- appointments and schedules;
- ordering and payment;
- staff access;
- payroll and workforce administration;
- stock and availability;
- customer communication;
- external providers; and
- reporting and audit evidence.

Generating software does not by itself solve the need for one dependable operational model, consistent invariants, durable state and long-lived integration.

### 1.7 The Solution

Main Street establishes an authoritative merchant operational model from merchant intent and supported registered semantics.

The Business Profile remains an important bounded source of merchant identity and public-information facts, but it is **not the universal source of all business truth**. Capability-owned state remains authoritative for the business concepts each capability owns.

Main Street then:

1. resolves a merchant's supported operating configuration;
2. executes operations through deterministic application/domain contracts;
3. maintains authoritative state and evidence;
4. derives appropriate projections and interaction opportunities; and
5. renders those through shared delivery surfaces.

No surface may silently become an alternative business engine.

Payroll is treated the same way as other optional operational capabilities: it participates in the merchant operating model only where enabled, remains bounded from workforce identity/access and customer Payment semantics, and may use replaceable external providers for jurisdiction-specific calculation, filing and funds-movement responsibilities.

### 1.8 AI Position

AI is an assistance and interpretation layer over deterministic Main Street capabilities.

AI may help merchants describe their business, interpret natural language, draft content, detect gaps, explain information and propose supported actions. It may not invent business semantics, become authoritative state, bypass validation or generate a bespoke uncontrolled application for each merchant.

The preferred relationship is:

```text
merchant language
      ↓
AI interpretation / proposal
      ↓
registered Main Street semantics
      ↓
deterministic validation and authorisation
      ↓
capability-owned execution
      ↓
authoritative state
```

AI handles ambiguity where useful. Main Street supplies determinism and operational continuity.

### 1.9 Value Proposition

**For merchants:** describe and operate the business in business language while Main Street handles the digital infrastructure and coordination.

**For customers:** interact with accurate, current business information and supported operations regardless of which Main Street delivery surface they encounter.

**For local communities:** make independent businesses easier to discover and transact with without requiring those businesses to become technology companies.

### 1.10 Governing Product Principle

> **The merchant operational model and governed runtime are the product core. Websites and other interfaces are delivery surfaces over that core.**

A feature that can be trivially generated by an external AI system may still be useful, but it is not by itself a defensible Main Street capability. Durable product value should come from authoritative context, reliable execution, integration, continuity, evidence and reduction of merchant operational complexity.

### 1.11 Product Promise

> **Tell Main Street how your business operates. Main Street sets up and runs the supported digital infrastructure around it, while you remain in control of the business.**

This supersedes narrower interpretations of the product promise that centre website creation or a static business profile as the complete product.
