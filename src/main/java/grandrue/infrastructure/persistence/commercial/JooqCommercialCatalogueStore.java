package grandrue.infrastructure.persistence.commercial;

import grandrue.commercial.CommercialCatalogueStore;

import grandrue.commercial.StandardPlanCatalogueHistory;

import grandrue.commercial.CataloguePublicationException;
import grandrue.commercial.CommercialCataloguePublicationAdmission;
import grandrue.commercial.CatalogueResolutionException;
import grandrue.application.TrustedPlatformExecutionContext;
import mainstreet.commercial.*;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Commercial-owned atomic publication and serialized historical resolution.
 * No Spring registration or production admission implementation is supplied here.
 * MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §7 — Publication authority; §8 — Publication operation;
 * §9 — Initial effective start and succession; §10 — Historical resolution;
 * §12 — Idempotency, concurrency and failure; §13 — Retention, recovery and runtime boundaries.
 * MS-PROT-072 v1.0 — Cross-Capability Application Orchestration & Consistency Model,
 * §5 — Transaction boundary rule; §8 — Layered idempotency.
 */
public final class JooqCommercialCatalogueStore implements CommercialCatalogueStore {
    private static final org.jooq.Table<?> PUBLICATIONS = DSL.table(DSL.name("commercial_catalogue_publication"));
    private static final org.jooq.Table<?> HEAD = DSL.table(DSL.name("commercial_catalogue_head"));
    private static final Field<String> ID = text("catalogue_identifier");
    private static final Field<String> REQUEST = text("request_identifier");
    private static final Field<byte[]> MANIFEST = DSL.field(DSL.name("manifest_content"), byte[].class);
    private static final Field<String> PREDECESSOR = text("predecessor_identifier");
    private static final Field<String> PRINCIPAL = text("publishing_principal_identifier");
    private static final Field<Instant> PUBLISHED = DSL.field(DSL.name("published_at"), Instant.class);
    private static final Field<Boolean> SINGLETON = DSL.field(DSL.name("singleton"), Boolean.class);
    private static final Field<String> LAST_SELECTION = text("last_selection_instant");

    private final DSLContext dsl;
    private final TransactionTemplate transaction;
    private final CommercialCataloguePublicationAdmission admission;
    private final Supplier<Instant> authoritativeTime;
    private final CommercialCatalogueManifestCodec codec = new CommercialCatalogueManifestCodec();

    public JooqCommercialCatalogueStore(DSLContext dsl, PlatformTransactionManager manager,
                                      CommercialCataloguePublicationAdmission admission) {
        this(dsl, manager, admission, () -> dsl.select(DSL.field("clock_timestamp()", Instant.class)).fetchSingle().value1());
    }

    // Only tests in this adapter package may substitute deterministic boundary time.
    JooqCommercialCatalogueStore(DSLContext dsl, PlatformTransactionManager manager,
                               CommercialCataloguePublicationAdmission admission, Supplier<Instant> authoritativeTime) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.admission = Objects.requireNonNull(admission, "admission");
        this.authoritativeTime = Objects.requireNonNull(authoritativeTime, "authoritativeTime");
        transaction = new TransactionTemplate(Objects.requireNonNull(manager, "manager"));
        // A returned receipt or successful read fence must already be committed,
        // even if an unrelated caller transaction subsequently rolls back.
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    @Override
    public CommercialCataloguePublication publish(CommercialCataloguePublicationRequest request,
                                                  TrustedPlatformExecutionContext context) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(context, "context");
        try {
            return Objects.requireNonNull(transaction.execute(status -> publishLocked(request, context)));
        } catch (CatalogueResolutionException failure) {
            throw publication(failure.reason() == CatalogueResolutionException.Reason.TECHNICAL_FAILURE
                    ? CataloguePublicationException.Reason.TECHNICAL_FAILURE : CataloguePublicationException.Reason.INTEGRITY_FAILURE,
                    "Retained catalogue evidence cannot be used for publication");
        } catch (DataAccessException | TransactionException failure) {
            throw publication(CataloguePublicationException.Reason.TECHNICAL_FAILURE, "Catalogue publication storage failed");
        }
    }

    private CommercialCataloguePublication publishLocked(CommercialCataloguePublicationRequest request,
                                                         TrustedPlatformExecutionContext context) {
        var head = lockHead();
        admission.requirePublicationAuthority(context);
        var history = retained(head);
        for (var previous : history) {
            if (previous.requestIdentifier().equals(request.requestIdentifier())) {
                if (!previous.manifest().equals(request.manifest())
                        || !previous.predecessor().equals(request.expectedPredecessor())
                        || !previous.publishingPrincipalIdentifier().equals(context.principal().identifier())) {
                    throw publication(CataloguePublicationException.Reason.IDENTITY_CONFLICT, "Publication request has different committed input");
                }
                return previous;
            }
        }
        if (!Optional.ofNullable(head.get(ID)).equals(request.expectedPredecessor())) {
            throw publication(CataloguePublicationException.Reason.PREDECESSOR_CONFLICT, "Expected predecessor is not current");
        }
        for (var previous : history) {
            if (previous.manifest().revision().catalogueRevisionIdentifier().equals(request.manifest().revision().catalogueRevisionIdentifier())) {
                throw publication(CataloguePublicationException.Reason.IDENTITY_CONFLICT, "Catalogue identity is already published");
            }
            try {
                request.manifest().requireCompatibleWith(previous.manifest());
            } catch (IllegalArgumentException failure) {
                throw publication(CataloguePublicationException.Reason.IDENTITY_CONFLICT, "Retained catalogue identities have different content");
            }
        }
        admission.requireApprovedManifest(request.manifest());
        Instant publishedAt = time().truncatedTo(ChronoUnit.MICROS);
        Instant lastSelection = selectionWatermark(head);
        if ((!history.isEmpty() && !publishedAt.isAfter(history.getLast().publishedAt()))
                || (lastSelection != null && !publishedAt.isAfter(lastSelection))) {
            throw publication(CataloguePublicationException.Reason.TECHNICAL_FAILURE,
                    "Authoritative time cannot preserve catalogue history at supported precision");
        }
        var result = new CommercialCataloguePublication(request.requestIdentifier(), request.manifest(),
                request.expectedPredecessor(), context.principal().identifier(), publishedAt);
        dsl.insertInto(PUBLICATIONS).columns(ID, REQUEST, MANIFEST, PREDECESSOR, PRINCIPAL, PUBLISHED)
                .values(request.manifest().revision().catalogueRevisionIdentifier(), request.requestIdentifier(),
                        codec.encode(request.manifest()), request.expectedPredecessor().orElse(null),
                        context.principal().identifier(), publishedAt).execute();
        dsl.update(HEAD).set(ID, request.manifest().revision().catalogueRevisionIdentifier()).where(SINGLETON.eq(true)).execute();
        return result;
    }

    @Override
    public Optional<CommercialCataloguePublication> exactGeneration(String identifier) {
        if (identifier == null || identifier.isBlank()) throw new IllegalArgumentException("Catalogue identity is required");
        return read(() -> retained(lockHead()).stream()
                .filter(value -> value.manifest().revision().catalogueRevisionIdentifier().equals(identifier)).findFirst());
    }

    @Override
    public CommercialCataloguePublication effectiveAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return read(() -> {
            var head = lockHead();
            var history = retained(head);
            Instant observedAt = time();
            if (!history.isEmpty() && history.getLast().publishedAt().isAfter(observedAt)) {
                throw resolution(CatalogueResolutionException.Reason.TECHNICAL_FAILURE, "Authoritative catalogue clock has regressed");
            }
            var snapshot = new StandardPlanCatalogueHistory(observedAt, history.stream().map(CommercialCataloguePublication::planProjection).toList());
            var selected = snapshot.effectiveAt(instant);
            Instant lastSelection = selectionWatermark(head);
            if (lastSelection == null || instant.isAfter(lastSelection)) {
                dsl.update(HEAD).set(LAST_SELECTION, instant.toString()).where(SINGLETON.eq(true)).execute();
            }
            return history.stream().filter(value -> value.manifest().revision().equals(selected.revision())).findFirst().orElseThrow();
        });
    }

    private Record lockHead() {
        var head = dsl.select(ID, LAST_SELECTION).from(HEAD).where(SINGLETON.eq(true)).forUpdate().fetchOne();
        if (head == null) throw resolution(CatalogueResolutionException.Reason.INTEGRITY_FAILURE, "Catalogue coordination state is missing");
        return head;
    }

    private List<CommercialCataloguePublication> retained(Record head) {
        try {
            var result = new ArrayList<CommercialCataloguePublication>();
            for (var row : dsl.select(ID, REQUEST, MANIFEST, PREDECESSOR, PRINCIPAL, PUBLISHED).from(PUBLICATIONS).orderBy(PUBLISHED).fetch()) {
                var manifest = codec.decode(row.get(MANIFEST));
                if (!manifest.revision().catalogueRevisionIdentifier().equals(row.get(ID))) {
                    throw new IllegalArgumentException("Catalogue content and identity disagree");
                }
                for (var previous : result) manifest.requireCompatibleWith(previous.manifest());
                result.add(new CommercialCataloguePublication(row.get(REQUEST), manifest,
                        Optional.ofNullable(row.get(PREDECESSOR)), row.get(PRINCIPAL), row.get(PUBLISHED)));
            }
            String actualHead = result.isEmpty() ? null : result.getLast().manifest().revision().catalogueRevisionIdentifier();
            if (!Objects.equals(head.get(ID), actualHead)) throw new IllegalArgumentException("Catalogue head and retained history disagree");
            Instant watermark = selectionWatermark(head);
            if (result.isEmpty() && watermark != null) throw new IllegalArgumentException("Read watermark exists without retained publication");
            new StandardPlanCatalogueHistory(result.isEmpty() ? Instant.EPOCH : result.getLast().publishedAt(),
                    result.stream().map(CommercialCataloguePublication::planProjection).toList());
            return List.copyOf(result);
        } catch (IllegalArgumentException | NullPointerException failure) {
            throw resolution(CatalogueResolutionException.Reason.INTEGRITY_FAILURE, "Retained catalogue evidence is inconsistent");
        }
    }

    private Instant selectionWatermark(Record head) {
        try {
            return head.get(LAST_SELECTION) == null ? null : Instant.parse(head.get(LAST_SELECTION));
        } catch (java.time.format.DateTimeParseException failure) {
            throw resolution(CatalogueResolutionException.Reason.INTEGRITY_FAILURE, "Catalogue read watermark is malformed");
        }
    }

    private <T> T read(Supplier<T> operation) {
        try {
            return Objects.requireNonNull(transaction.execute(status -> operation.get()));
        } catch (DataAccessException | TransactionException failure) {
            throw resolution(CatalogueResolutionException.Reason.TECHNICAL_FAILURE, "Catalogue historical storage is unavailable");
        }
    }

    private Instant time() { return Objects.requireNonNull(authoritativeTime.get(), "authoritative time"); }
    private static Field<String> text(String name) { return DSL.field(DSL.name(name), String.class); }
    private static CataloguePublicationException publication(CataloguePublicationException.Reason reason, String message) {
        return new CataloguePublicationException(reason, message);
    }
    private static CatalogueResolutionException resolution(CatalogueResolutionException.Reason reason, String message) {
        return new CatalogueResolutionException(reason, message);
    }
}
