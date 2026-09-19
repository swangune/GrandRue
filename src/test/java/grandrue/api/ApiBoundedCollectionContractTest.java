package grandrue.api;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiBoundedCollectionContractTest {

    @Test
    void retains_the_complete_static_bounded_collection_contract() {
        ApiContractIdentity query = new ApiContractIdentity(
                "ordering",
                "merchant-order-list"
        );
        ApiBoundedCollectionContract contract = new ApiBoundedCollectionContract(
                query,
                100,
                "ordering.created-at-descending-then-order-id",
                Set.of("ordering.status", "ordering.created-range"),
                "ordering-keyset-continuation",
                "merchant-order-query-and-audience-affinity"
        );

        assertEquals(query, contract.queryContractIdentity());
        assertEquals(100, contract.maximumPageSize());
        assertEquals(
                Set.of("ordering.status", "ordering.created-range"),
                contract.permittedFilterReferences()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> contract.permittedFilterReferences().clear()
        );
    }

    @Test
    void permits_a_closed_empty_filter_set_without_creating_a_query_dsl() {
        ApiBoundedCollectionContract contract = new ApiBoundedCollectionContract(
                new ApiContractIdentity("audit", "merchant-audit-list"),
                50,
                "audit-recorded-at-descending-then-record-id",
                Set.of(),
                "audit-keyset-continuation",
                "merchant-audit-query-and-audience-affinity"
        );

        assertTrue(contract.permittedFilterReferences().isEmpty());
    }

    @Test
    void rejects_unbounded_pages_and_missing_order_filter_continuation_or_affinity_rules() {
        ApiContractIdentity query = new ApiContractIdentity(
                "ordering",
                "merchant-order-list"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiBoundedCollectionContract(
                        query,
                        0,
                        "ordering.stable-order",
                        Set.of(),
                        "continuation",
                        "affinity"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiBoundedCollectionContract(
                        query,
                        100,
                        " ",
                        Set.of(),
                        "continuation",
                        "affinity"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiBoundedCollectionContract(
                        query,
                        100,
                        "ordering.stable-order",
                        Set.of(" "),
                        "continuation",
                        "affinity"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiBoundedCollectionContract(
                        query,
                        100,
                        "ordering.stable-order",
                        Set.of(),
                        " ",
                        "affinity"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiBoundedCollectionContract(
                        query,
                        100,
                        "ordering.stable-order",
                        Set.of(),
                        "continuation",
                        " "
                )
        );
    }
}
