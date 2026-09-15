# **32. Identity & Authentication**

## **32.1 Purpose**

The Identity & Authentication module provides secure access to the Main Street platform while maintaining a simple and frictionless sign-in experience.

Its primary responsibility is to verify identities, protect merchant accounts, safeguard customer information and ensure that authorised users can access only the resources for which they have permission.

Security should protect merchants without making the platform difficult to use.

---

# **32.2 Design Philosophy**

The Identity & Authentication module is built upon one principle:

> **Security should be invisible until it is needed.**

Most merchants should be able to sign in quickly and securely.

Additional security measures should activate only when risk increases or sensitive operations are performed.

---

# **32.3 Objectives**

The Identity & Authentication module shall:

* Secure merchant accounts.
* Protect customer information.
* Prevent unauthorised access.
* Support role-based permissions.
* Enable secure integrations.
* Detect suspicious activity.
* Minimise authentication friction.

---

# **32.4 Identity Model**

Main Street manages three distinct identity types.

## Merchant Users

Individuals authorised to manage a business.

Examples include:

* Business Owner.
* Manager.
* Staff Member.

Merchant Users authenticate directly with Main Street.

---

## Platform Administrators

Authorised Main Street personnel responsible for operating the platform.

Administrative accounts remain completely separate from merchant accounts.

---

## Customers

Customers are **not** Main Street users.

Customers interact directly with a merchant's website.

They may:

* Browse anonymously.
* Place guest orders.
* Make guest bookings.
* Register with an individual merchant.

Customer accounts belong to the merchant and are not global Main Street identities.

---

# **32.5 Authentication Methods**

The platform should support secure authentication methods including:

* Email and password.
* One-Time Password (future).
* Passkeys (future).
* Social sign-in (future).

Authentication methods should evolve with industry best practices.

---

# **32.6 Password Security**

Where passwords are used, the platform shall:

* Store only securely hashed passwords.
* Never store plaintext passwords.
* Enforce minimum password requirements.
* Prevent commonly compromised passwords.
* Support password reset through secure verification.

Passwords shall never be recoverable.

---

# **32.7 Multi-Factor Authentication**

Multi-Factor Authentication (MFA) should be supported.

MFA may include:

* Authenticator applications.
* Security keys.
* Email verification.
* Future biometric authentication where supported by devices.

MFA should be mandatory for Platform Administrators and optional, but strongly encouraged, for merchants.

---

# **32.8 Session Management**

Authenticated sessions shall be securely managed.

Capabilities include:

* Secure session tokens.
* Automatic session expiration.
* Session renewal.
* Logout from all devices.
* Device management.
* Suspicious session detection.

Inactive sessions should expire automatically after an appropriate period.

---

# **32.9 Device Recognition**

The platform may recognise trusted devices.

New devices may trigger:

* Additional verification.
* Security notifications.
* Risk assessment.

Merchants should be informed whenever a new device accesses their account.

---

# **32.10 Account Recovery**

Merchants shall be able to recover access securely.

Recovery options may include:

* Email verification.
* Multi-factor verification.
* Recovery codes (future).

Recovery procedures should prioritise security while remaining straightforward for legitimate account holders.

---

# **32.11 Account Ownership**

Every business has a single Business Owner.

The Business Owner is responsible for:

* Staff invitations.
* Permission management.
* Subscription management.
* Ownership transfer.

Ownership transfer shall require strong verification to prevent unauthorised business takeover.

---

# **32.12 Staff Authentication**

Every staff member receives an individual account.

Shared accounts are prohibited.

Individual accounts provide:

* Accountability.
* Audit trails.
* Secure permission management.
* Activity tracking.

---

# **32.13 Login Notifications**

Merchants may receive notifications for significant authentication events.

Examples include:

* New device login.
* Password changed.
* MFA enabled.
* Account recovery.
* Multiple failed login attempts.

Notifications improve account security awareness.

---

# **32.14 Suspicious Activity Detection**

The platform shall monitor for suspicious authentication behaviour.

Examples include:

* Repeated failed logins.
* Impossible travel.
* Unusual device changes.
* Excessive password resets.
* Automated login attempts.

Detected risks may trigger additional verification or temporary account protection.

---

# **32.15 API Authentication**

Third-party integrations shall authenticate securely using industry-standard mechanisms.

The platform shall:

* Issue secure credentials.
* Support scoped permissions.
* Allow credential revocation.
* Log API access.
* Apply rate limits.

Integrations should have only the permissions required for their intended purpose.

---

# **32.16 Permission Enforcement**

Authentication confirms identity.

Authorisation determines access.

Every authenticated request shall verify permissions before granting access to:

* Business data.
* Customer information.
* Financial records.
* Administrative functions.
* Platform resources.

Permission checks shall be enforced consistently across all platform components.

---

# **32.17 Privacy Protection**

Identity information shall be handled securely.

The platform shall:

* Minimise stored personal data.
* Encrypt sensitive information.
* Comply with applicable privacy legislation.
* Restrict access according to role.

Identity management must balance usability with privacy.

---

# **32.18 Future Authentication Capabilities**

The architecture should support future enhancements including:

* Passwordless authentication.
* Biometric authentication.
* Hardware security keys.
* Enterprise Single Sign-On.
* Adaptive risk-based authentication.

Security capabilities should evolve without disrupting existing merchants.

---

# **32.19 Identity & Authentication Statement**

The Identity & Authentication module establishes trust across the Main Street platform.

By combining secure authentication, intelligent risk detection, role-based access control and strong identity protection, the platform enables merchants to operate confidently while safeguarding customer information and business assets.

Security should strengthen confidence without creating unnecessary complexity, allowing merchants to focus on running their businesses rather than managing technology.

---

### End of Section 32 – Identity & Authentication
