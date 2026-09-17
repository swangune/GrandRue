package grandrue.infrastructure.persistence.background;

import grandrue.background.BackgroundWorkContractIdentity;
import grandrue.background.ClaimedWork;
import grandrue.background.DurableWorkStore;
import grandrue.background.RegisteredDurableWorkClaimer;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PostgreSQL technical claim adapter for one registered Background Work Contract identity.
 * The contract filter prevents a bounded worker from leasing unrelated durable work.
 */
public final class JooqRegisteredDurableWorkClaimer implements RegisteredDurableWorkClaimer {

    private static final Table<?> WORK = DSL.table(DSL.name("durable_work_instruction"));
    private static final Field<String> WORK_IDENTIFIER =
            DSL.field(DSL.name("work_identifier"), String.class);
    private static final Field<Instant> NEXT_ATTEMPT_AT =
            DSL.field(DSL.name("next_attempt_at"), Instant.class);
    private static final Field<String> CONTRACT_OWNER_IDENTIFIER =
            DSL.field(DSL.name("contract_owner_identifier"), String.class);
    private static final Field<String> CONTRACT_IDENTIFIER =
            DSL.field(DSL.name("contract_identifier"), String.class);
    private static final Field<String> CLAIMED_BY =
            DSL.field(DSL.name("claimed_by"), String.class);
    private static final Field<Instant> CLAIM_EXPIRES_AT =
            DSL.field(DSL.name("claim_expires_at"), Instant.class);
    private static final Field<Instant> FINALISED_AT =
            DSL.field(DSL.name("finalised_at"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final DurableWorkStore durableWorkStore;

    public JooqRegisteredDurableWorkClaimer(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            DurableWorkStore durableWorkStore) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager"));
        this.durableWorkStore = Objects.requireNonNull(durableWorkStore, "durableWorkStore");
    }

    @Override
    public List<ClaimedWork> claimDue(
            BackgroundWorkContractIdentity contractIdentity,
            String workerIdentity,
            Instant now,
            Instant claimExpiresAt,
            int limit) {
        Objects.requireNonNull(contractIdentity, "contractIdentity");
        requireIdentifier(workerIdentity, "workerIdentity");
        Objects.requireNonNull(now, "now");
        Objects.requireNonNull(claimExpiresAt, "claimExpiresAt");
        if (!claimExpiresAt.isAfter(now)) {
            throw new IllegalArgumentException("Claim expiry must be after claim time");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Claim limit must be positive");
        }

        List<ClaimedWork> claimed = transactionTemplate.execute(status -> {
            var rows = dsl.select(WORK_IDENTIFIER)
                    .from(WORK)
                    .where(FINALISED_AT.isNull())
                    .and(NEXT_ATTEMPT_AT.le(now))
                    .and(CONTRACT_OWNER_IDENTIFIER.eq(contractIdentity.ownerIdentifier()))
                    .and(CONTRACT_IDENTIFIER.eq(contractIdentity.contractIdentifier()))
                    .and(CLAIMED_BY.isNull().or(CLAIM_EXPIRES_AT.le(now)))
                    .orderBy(NEXT_ATTEMPT_AT, WORK_IDENTIFIER)
                    .limit(limit)
                    .forUpdate()
                    .skipLocked()
                    .fetch();

            List<ClaimedWork> results = new ArrayList<>(rows.size());
            for (var row : rows) {
                String workIdentity = row.get(WORK_IDENTIFIER);
                int updated = dsl.update(WORK)
                        .set(CLAIMED_BY, workerIdentity)
                        .set(CLAIM_EXPIRES_AT, claimExpiresAt)
                        .where(WORK_IDENTIFIER.eq(workIdentity))
                        .and(FINALISED_AT.isNull())
                        .execute();
                if (updated != 1) {
                    throw new IllegalStateException(
                            "Could not establish technical claim for work: " + workIdentity);
                }
                var instruction = durableWorkStore.instruction(workIdentity)
                        .orElseThrow(() -> new IllegalStateException(
                                "Claimed durable work instruction disappeared: " + workIdentity));
                results.add(new ClaimedWork(instruction, workerIdentity, claimExpiresAt));
            }
            return List.copyOf(results);
        });

        return Objects.requireNonNull(claimed, "Registered work claim transaction returned no result");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
