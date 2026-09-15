# Legacy Booking notification prototype — test-scope evidence only

The Booking-specific delivery gateway retained beside this note is historical test evidence only.

It is **not a production notification authority** and is deliberately excluded from `src/main`.

Production notification semantics are governed by MS-PROT-075 and the generic `mainstreet.notification` model:

`NotificationContract -> NotificationIntent -> NotificationDispatch -> DeliveryAttempt -> DeliveryEvidence`

Capabilities such as Booking own why a communication is required and which authoritative facts it may communicate. They do not own a parallel provider-delivery subsystem.

Do not add production dependencies on these test fixtures.
