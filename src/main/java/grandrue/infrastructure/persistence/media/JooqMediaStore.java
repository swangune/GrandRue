package grandrue.infrastructure.persistence.media;

import grandrue.application.MerchantScope;
import grandrue.media.MediaAsset;
import grandrue.media.MediaKind;
import grandrue.media.MediaProcessingOutcome;
import grandrue.media.MediaRendition;
import grandrue.media.MediaStore;
import grandrue.media.MediaValidationState;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL/jOOQ metadata store; bytes remain behind provider/storage adapters. */
public final class JooqMediaStore implements MediaStore {

    private static final Table<?> ASSET = DSL.table(DSL.name("media_asset"));
    private static final Table<?> RENDITION = DSL.table(DSL.name("media_rendition"));

    private static final Field<String> ASSET_ID = text("asset_identity");
    private static final Field<String> MERCHANT_ID = text("merchant_identifier");
    private static final Field<String> MEDIA_KIND = text("media_kind");
    private static final Field<String> SOURCE_REF = text("canonical_source_reference");
    private static final Field<String> SOURCE_DIGEST = text("source_digest");
    private static final Field<Instant> CREATED_AT = instant("created_at");
    private static final Field<String> VALIDATION_STATE = text("validation_state");

    private static final Field<String> RENDITION_ID = text("rendition_identity");
    private static final Field<String> SOURCE_ASSET_ID = text("source_asset_identity");
    private static final Field<String> PROFILE_ID = text("profile_identity");
    private static final Field<Integer> PROFILE_VERSION =
            DSL.field(DSL.name("profile_version"), Integer.class);
    private static final Field<String> STORAGE_REF = text("storage_reference");
    private static final Field<String> RENDITION_DIGEST = text("rendition_digest");
    private static final Field<Instant> GENERATED_AT = instant("generated_at");
    private static final Field<String> PROCESSING_OUTCOME = text("processing_outcome");

    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMediaStore(DSLContext dsl, PlatformTransactionManager transactionManager) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MediaAsset establishAsset(MediaAsset asset) {
        Objects.requireNonNull(asset, "asset");
        MediaAsset result = transactions.execute(status -> {
            lock("asset", asset.assetIdentity(), 6601);
            MediaAsset existing = findAsset(asset.assetIdentity()).orElse(null);
            if (existing != null) {
                requireEqual(existing, asset, "MediaAsset");
                return existing;
            }
            dsl.insertInto(ASSET)
                    .columns(ASSET_ID, MERCHANT_ID, MEDIA_KIND, SOURCE_REF,
                            SOURCE_DIGEST, CREATED_AT, VALIDATION_STATE)
                    .values(asset.assetIdentity(), asset.merchantScope().merchantIdentifier(),
                            asset.mediaKind().name(), asset.canonicalSourceReference(),
                            asset.sourceDigest(), asset.createdAt(),
                            asset.validationState().name())
                    .execute();
            return asset;
        });
        return Objects.requireNonNull(result, "MediaAsset transaction returned no result");
    }

    @Override
    public MediaRendition establishRendition(MediaRendition rendition) {
        Objects.requireNonNull(rendition, "rendition");
        MediaRendition result = transactions.execute(status -> {
            lock("rendition", rendition.renditionIdentity(), 6602);
            MediaRendition existing = findRendition(rendition.renditionIdentity())
                    .orElse(null);
            if (existing != null) {
                requireEqual(existing, rendition, "MediaRendition");
                return existing;
            }
            MediaAsset source = findAsset(rendition.sourceAssetIdentity())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown source MediaAsset: "
                                    + rendition.sourceAssetIdentity()
                    ));
            if (!source.merchantScope().equals(rendition.merchantScope())) {
                throw new IllegalArgumentException(
                        "Media rendition source belongs to another Merchant Scope"
                );
            }
            dsl.insertInto(RENDITION)
                    .columns(RENDITION_ID, MERCHANT_ID, SOURCE_ASSET_ID, PROFILE_ID,
                            PROFILE_VERSION, STORAGE_REF, RENDITION_DIGEST,
                            GENERATED_AT, PROCESSING_OUTCOME)
                    .values(rendition.renditionIdentity(),
                            rendition.merchantScope().merchantIdentifier(),
                            rendition.sourceAssetIdentity(), rendition.profileIdentity(),
                            rendition.profileVersion(), rendition.storageReference(),
                            rendition.renditionDigest(), rendition.generatedAt(),
                            rendition.processingOutcome().name())
                    .execute();
            return rendition;
        });
        return Objects.requireNonNull(result, "MediaRendition transaction returned no result");
    }

    @Override
    public Optional<MediaAsset> findAsset(String identity) {
        require(identity, "MediaAsset identity");
        Record record = dsl.select(ASSET_ID, MERCHANT_ID, MEDIA_KIND, SOURCE_REF,
                        SOURCE_DIGEST, CREATED_AT, VALIDATION_STATE)
                .from(ASSET)
                .where(ASSET_ID.eq(identity))
                .fetchOne();
        if (record == null) {
            return Optional.empty();
        }
        return Optional.of(new MediaAsset(
                record.get(ASSET_ID),
                new MerchantScope(record.get(MERCHANT_ID)),
                MediaKind.valueOf(record.get(MEDIA_KIND)),
                record.get(SOURCE_REF),
                record.get(SOURCE_DIGEST),
                record.get(CREATED_AT),
                MediaValidationState.valueOf(record.get(VALIDATION_STATE))
        ));
    }

    @Override
    public Optional<MediaRendition> findRendition(String identity) {
        require(identity, "MediaRendition identity");
        Record record = renditionSelect().where(RENDITION_ID.eq(identity)).fetchOne();
        return Optional.ofNullable(record).map(this::toRendition);
    }

    @Override
    public List<MediaRendition> renditionsForSource(String assetIdentity) {
        require(assetIdentity, "MediaAsset identity");
        Result<? extends Record> records = renditionSelect()
                .where(SOURCE_ASSET_ID.eq(assetIdentity))
                .orderBy(GENERATED_AT, RENDITION_ID)
                .fetch();
        List<MediaRendition> renditions = new ArrayList<>();
        records.forEach(record -> renditions.add(toRendition(record)));
        return List.copyOf(renditions);
    }

    private org.jooq.SelectJoinStep<? extends Record> renditionSelect() {
        return dsl.select(RENDITION_ID, MERCHANT_ID, SOURCE_ASSET_ID, PROFILE_ID,
                        PROFILE_VERSION, STORAGE_REF, RENDITION_DIGEST,
                        GENERATED_AT, PROCESSING_OUTCOME)
                .from(RENDITION);
    }

    private MediaRendition toRendition(Record record) {
        return new MediaRendition(
                record.get(RENDITION_ID),
                new MerchantScope(record.get(MERCHANT_ID)),
                record.get(SOURCE_ASSET_ID),
                record.get(PROFILE_ID),
                record.get(PROFILE_VERSION),
                record.get(STORAGE_REF),
                record.get(RENDITION_DIGEST),
                record.get(GENERATED_AT),
                MediaProcessingOutcome.valueOf(record.get(PROCESSING_OUTCOME))
        );
    }

    private void lock(String kind, String identity, int namespace) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 66))",
                namespace + "\u001f" + kind + "\u001f" + identity
        );
    }

    private static Field<String> text(String name) {
        return DSL.field(DSL.name(name), String.class);
    }

    private static Field<Instant> instant(String name) {
        return DSL.field(DSL.name(name), Instant.class);
    }

    private static void requireEqual(Object existing, Object candidate, String label) {
        if (!Objects.equals(existing, candidate)) {
            throw new IllegalArgumentException(
                    label + " identity was reused for different metadata"
            );
        }
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
