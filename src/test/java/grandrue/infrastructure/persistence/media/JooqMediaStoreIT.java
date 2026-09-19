package grandrue.infrastructure.persistence.media;

import grandrue.application.MerchantScope;
import grandrue.media.MediaAsset;
import grandrue.media.MediaKind;
import grandrue.media.MediaProcessingOutcome;
import grandrue.media.MediaRendition;
import grandrue.media.MediaValidationState;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqMediaStoreIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-media-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-media-b");
    private static final Instant T0 = Instant.parse("2026-08-24T08:30:00Z");

    private DSLContext dsl;
    private JooqMediaStore store;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure().dataSource(dataSource)
                .locations("classpath:db/migration").load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
        store = new JooqMediaStore(dsl, new DataSourceTransactionManager(dataSource));
        clear();
        merchant(MERCHANT_A);
        merchant(MERCHANT_B);
    }

    @AfterEach
    void tearDown() {
        clear();
    }

    @Test
    void canonical_source_metadata_is_durable_and_idempotent() {
        MediaAsset asset = asset("asset-1", MERCHANT_A, "source-ref-1");
        assertEquals(asset, store.establishAsset(asset));
        assertEquals(asset, store.establishAsset(asset));
        assertEquals(asset, store.findAsset("asset-1").orElseThrow());
        assertEquals(1, count("media_asset"));
    }

    @Test
    void same_asset_identity_cannot_replace_canonical_source() {
        store.establishAsset(asset("asset-1", MERCHANT_A, "source-ref-1"));
        assertThrows(
                IllegalArgumentException.class,
                () -> store.establishAsset(asset("asset-1", MERCHANT_A, "source-ref-2"))
        );
        assertEquals("source-ref-1",
                store.findAsset("asset-1").orElseThrow().canonicalSourceReference());
    }

    @Test
    void rendition_is_derived_metadata_and_does_not_replace_source() {
        MediaAsset source = store.establishAsset(asset("asset-1", MERCHANT_A, "source-ref-1"));
        MediaRendition rendition = rendition("rendition-1", MERCHANT_A, "asset-1");
        store.establishRendition(rendition);

        assertEquals(source, store.findAsset("asset-1").orElseThrow());
        assertEquals(rendition, store.findRendition("rendition-1").orElseThrow());
        assertEquals(1, store.renditionsForSource("asset-1").size());
    }

    @Test
    void rendition_cannot_cross_merchant_source_affinity() {
        store.establishAsset(asset("asset-1", MERCHANT_A, "source-ref-1"));
        assertThrows(
                IllegalArgumentException.class,
                () -> store.establishRendition(
                        rendition("rendition-1", MERCHANT_B, "asset-1")
                )
        );
        assertEquals(0, count("media_rendition"));
    }

    @Test
    void rendition_identity_replay_is_exact_and_profile_provenance_is_preserved() {
        store.establishAsset(asset("asset-1", MERCHANT_A, "source-ref-1"));
        MediaRendition rendition = rendition("rendition-1", MERCHANT_A, "asset-1");
        assertEquals(rendition, store.establishRendition(rendition));
        assertEquals(rendition, store.establishRendition(rendition));
        assertEquals("public-hero-photo",
                store.findRendition("rendition-1").orElseThrow().profileIdentity());
        assertEquals(3,
                store.findRendition("rendition-1").orElseThrow().profileVersion());
    }

    private MediaAsset asset(String id, MerchantScope merchant, String sourceRef) {
        return new MediaAsset(
                id,
                merchant,
                MediaKind.IMAGE,
                sourceRef,
                "sha256:" + sourceRef,
                T0,
                MediaValidationState.VALIDATED
        );
    }

    private MediaRendition rendition(String id, MerchantScope merchant, String sourceId) {
        return new MediaRendition(
                id,
                merchant,
                sourceId,
                "public-hero-photo",
                3,
                "rendition-storage-ref-1",
                "sha256:rendition",
                T0.plusSeconds(5),
                MediaProcessingOutcome.SUCCEEDED
        );
    }

    private void merchant(MerchantScope merchant) {
        dsl.insertInto(DSL.table(DSL.name("merchant_account")))
                .columns(DSL.field(DSL.name("merchant_identifier")))
                .values(merchant.merchantIdentifier())
                .execute();
    }

    private void clear() {
        dsl.deleteFrom(DSL.table(DSL.name("media_rendition"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("media_asset"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("merchant_account")))
                .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                        .like("merchant-media-%"))
                .execute();
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
