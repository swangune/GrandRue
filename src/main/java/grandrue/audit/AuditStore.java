package grandrue.audit;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Audit-owned append/query boundary. No ordinary update or delete operation is
 * exposed for historical evidence.
 */
public interface AuditStore {

    AuditRecord append(AuditRecord record);

    Optional<AuditRecord> record(String auditIdentity);

    List<AuditRecord> merchantRecords(
            MerchantScope merchantScope,
            Instant fromInclusive,
            Instant untilExclusive,
            int limit
    );
}
