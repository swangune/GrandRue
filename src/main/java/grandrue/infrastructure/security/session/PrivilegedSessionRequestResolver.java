package grandrue.infrastructure.security.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import mainstreet.runtime.AuthenticationException;
import mainstreet.runtime.AuthenticationFailureCategory;
import grandrue.runtime.AuthenticationSession;
import grandrue.runtime.SessionCredentialResolver;

import java.util.Objects;

/**
 * ADR-014 request adapter from the host-bound privileged browser cookie to the
 * Main Street-owned server-authoritative Session resolver.
 *
 * <p>This adapter establishes no Merchant Scope, relationship, role,
 * privilege, entitlement or capability authority. Headers, request parameters
 * and Spring authority state are not alternative privileged browser bearer
 * sources at this boundary.</p>
 */
public final class PrivilegedSessionRequestResolver {

    private final SessionCredentialResolver sessionCredentialResolver;

    public PrivilegedSessionRequestResolver(
            SessionCredentialResolver sessionCredentialResolver
    ) {
        this.sessionCredentialResolver = Objects.requireNonNull(
                sessionCredentialResolver,
                "sessionCredentialResolver"
        );
    }

    public AuthenticationSession resolve(HttpServletRequest request) {
        Objects.requireNonNull(request, "request");

        String presentedCredential = null;
        int matchingCookies = 0;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (!PrivilegedSessionCookieFactory.COOKIE_NAME.equals(cookie.getName())) {
                    continue;
                }

                matchingCookies++;
                if (matchingCookies > 1) {
                    throw authenticationFailed(
                            "Privileged Session Credential is ambiguous"
                    );
                }

                String candidate = cookie.getValue();
                if (candidate == null || candidate.isBlank()) {
                    throw authenticationFailed(
                            "Privileged Session Credential is missing"
                    );
                }
                presentedCredential = candidate;
            }
        }

        if (presentedCredential == null) {
            throw authenticationFailed("Privileged Session Credential is missing");
        }

        return sessionCredentialResolver.resolve(presentedCredential);
    }

    private static AuthenticationException authenticationFailed(String message) {
        return new AuthenticationException(
                AuthenticationFailureCategory.AUTHENTICATION_FAILED,
                message
        );
    }
}
