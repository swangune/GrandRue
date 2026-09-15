package mainstreet.infrastructure.persistence.inventory;

import mainstreet.application.MerchantScope;
import mainstreet.inventory.InsufficientQuantityException;
import mainstreet.semantic.AllocationAuthority;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.AllocationScope;
import mainstreet.semantic.QuantityAllocationScope;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL Inventory allocation authority for finite quantity stock.
 *
 * <p>The owning stock-position row is locked before available-to-promise is
 * evaluated, so concurrent Ordering transactions cannot both consume the same
 * final unit. Ordering participates in the surrounding transaction but does
 * not own this stock arithmetic.</p>
 */
public final class JooqQuantityAllocationAuthority
        implements AllocationAuthority {

    private static final Table<?> STOCK =
            DSL.table(DSL.name("inventory_stock_position"));
    private static final Table<?> CLAIM =
            DSL.table(DSL.name("inventory_quantity_claim"));

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> SUBJECT_IDENTIFIER =
            DSL.field(DSL.name("subject_identifier"), String.class);
    private static final Field<Long> STOCK_ON_HAND =
            DSL.field(DSL.name("stock_on_hand"), Long.class);
    private static final Field<String> CLAIM_IDENTIFIER =
            DSL.field(DSL.name("claim_identifier"), String.class);
    private static final Field<Long> QUANTITY =
            DSL.field(DSL.name("quantity"), Long.class);
    private static final Field<String> USE_IDENTIFIER =
            DSL.field(DSL.name("use_identifier"), String.class);
    private static final Field<Instant> CLAIMED_AT =
            DSL.field(DSL.name("claimed_at"), Instant.class);

    private final DSLContext dsl;
    private final MerchantScope merchantScope;

    public JooqQuantityAllocationAuthority(
            DSLContext dsl,
            MerchantScope merchantScope
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.merchantScope = Objects.requireNonNull(
                merchantScope,
                "merchantScope"
        );
    }

    @Override
    public AllocationClaim claim(
            String identifier,
            AllocationScope scope,
            String useIdentifier,
            Instant claimedAt
    ) {
        requireIdentifier(identifier, "Inventory claim identifier");
        requireIdentifier(useIdentifier, "Inventory claim use identifier");
        Objects.requireNonNull(claimedAt, "claimedAt");
        QuantityAllocationScope quantityScope = requireQuantityScope(scope);

        Long stockOnHand = dsl.select(STOCK_ON_HAND)
                .from(STOCK)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(SUBJECT_IDENTIFIER.eq(quantityScope.subjectIdentifier()))
                .forUpdate()
                .fetchOne(STOCK_ON_HAND);

        long available = stockOnHand == null
                ? 0L
                : stockOnHand - claimedQuantity(quantityScope.subjectIdentifier());
        if (quantityScope.quantity() > available) {
            throw new InsufficientQuantityException(
                    quantityScope.subjectIdentifier(),
                    quantityScope.quantity(),
                    Math.max(available, 0L)
            );
        }

        dsl.insertInto(CLAIM)
                .columns(
                        MERCHANT_IDENTIFIER,
                        CLAIM_IDENTIFIER,
                        SUBJECT_IDENTIFIER,
                        QUANTITY,
                        USE_IDENTIFIER,
                        CLAIMED_AT
                )
                .values(
                        merchantScope.merchantIdentifier(),
                        identifier,
                        quantityScope.subjectIdentifier(),
                        quantityScope.quantity(),
                        useIdentifier,
                        claimedAt
                )
                .execute();

        return new AllocationClaim(
                identifier,
                quantityScope,
                useIdentifier,
                claimedAt
        );
    }

    public long availableToPromise(String subjectIdentifier) {
        requireIdentifier(subjectIdentifier, "Inventory subject identifier");
        Long stockOnHand = dsl.select(STOCK_ON_HAND)
                .from(STOCK)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(SUBJECT_IDENTIFIER.eq(subjectIdentifier))
                .fetchOne(STOCK_ON_HAND);
        if (stockOnHand == null) {
            return 0L;
        }
        return Math.max(
                0L,
                stockOnHand - claimedQuantity(subjectIdentifier)
        );
    }

    public Optional<AllocationClaim> claim(String identifier) {
        requireIdentifier(identifier, "Inventory claim identifier");
        Record row = dsl.select(
                        CLAIM_IDENTIFIER,
                        SUBJECT_IDENTIFIER,
                        QUANTITY,
                        USE_IDENTIFIER,
                        CLAIMED_AT
                )
                .from(CLAIM)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(CLAIM_IDENTIFIER.eq(identifier))
                .fetchOne();
        return Optional.ofNullable(row).map(this::claim);
    }

    private long claimedQuantity(String subjectIdentifier) {
        Long value = dsl.select(
                        DSL.field(
                                "coalesce(sum({0}), 0)::bigint",
                                Long.class,
                                QUANTITY
                        )
                )
                .from(CLAIM)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(SUBJECT_IDENTIFIER.eq(subjectIdentifier))
                .fetchOne(0, Long.class);
        return value == null ? 0L : value;
    }

    private AllocationClaim claim(Record row) {
        return new AllocationClaim(
                row.get(CLAIM_IDENTIFIER),
                new QuantityAllocationScope(
                        row.get(SUBJECT_IDENTIFIER),
                        row.get(QUANTITY)
                ),
                row.get(USE_IDENTIFIER),
                row.get(CLAIMED_AT)
        );
    }

    private static QuantityAllocationScope requireQuantityScope(
            AllocationScope scope
    ) {
        Objects.requireNonNull(scope, "scope");
        if (!(scope instanceof QuantityAllocationScope quantityScope)) {
            throw new IllegalArgumentException(
                    "Quantity Inventory authority requires a quantity allocation scope"
            );
        }
        return quantityScope;
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
