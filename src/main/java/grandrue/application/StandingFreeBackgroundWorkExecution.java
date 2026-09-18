package grandrue.application;

import grandrue.application.StandingFreeBackgroundWorkContract;
import grandrue.application.StandingFreeClaimedWorkExecution;
import grandrue.background.BackgroundWorkContractAffinity;
import grandrue.background.BackgroundWorkContractDefinition;
import grandrue.background.BackgroundWorkContractRegistrySnapshot;
import grandrue.background.BackgroundWorkResultClassification;
import grandrue.background.ClaimedWork;
import grandrue.background.DurableWorkInstruction;
import grandrue.background.DurableWorkStore;
import grandrue.background.WorkAttempt;
import mainstreet.commercial.StandingFreeBaseline;
import mainstreet.application.MerchantScope;
import grandrue.application.StandingFreeFromMerchantAccountEstablishedHandler;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrenceLookup;
import mainstreet.runtime.ScheduledBackgroundWorkExecutionAuthority;
import mainstreet.runtime.TrustedExecutionContext;

import java.time.Clock;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Registered Standing Free durable-work binding to the Commercial owner operation.
 *
 * <p>The legacy {@link #execute(DurableWorkInstruction)} path retains the bounded
 * C2B semantic binding. Claimed production work must use {@link #executeClaimed}
 * so MS-PROT-065 v1.1 §§11, 24-27 and MS-PROT-056 v1.6 §§5-7 attempt/recovery
 * evidence is progressed around the owner consequence.</p>
 */
public final class StandingFreeBackgroundWorkExecution implements StandingFreeClaimedWorkExecution {
    private final MerchantAccountEstablishedOccurrenceLookup sourceAuthority;
    private final Function<String, Optional<BackgroundWorkContractRegistrySnapshot>> historicalContracts;
    private final Supplier<BackgroundWorkContractRegistrySnapshot> currentContracts;
    private final ScheduledBackgroundWorkExecutionAuthority executionAuthority;
    private final StandingFreeFromMerchantAccountEstablishedHandler ownerOperation;
    private final Clock clock;

    public StandingFreeBackgroundWorkExecution(
            MerchantAccountEstablishedOccurrenceLookup sourceAuthority,
            Function<String, Optional<BackgroundWorkContractRegistrySnapshot>> historicalContracts,
            Supplier<BackgroundWorkContractRegistrySnapshot> currentContracts,
            ScheduledBackgroundWorkExecutionAuthority executionAuthority,
            StandingFreeFromMerchantAccountEstablishedHandler ownerOperation,
            Clock clock) {
        this.sourceAuthority = Objects.requireNonNull(sourceAuthority, "sourceAuthority");
        this.historicalContracts = Objects.requireNonNull(historicalContracts, "historicalContracts");
        this.currentContracts = Objects.requireNonNull(currentContracts, "currentContracts");
        this.executionAuthority = Objects.requireNonNull(executionAuthority, "executionAuthority");
        this.ownerOperation = Objects.requireNonNull(ownerOperation, "ownerOperation");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public StandingFreeBaseline execute(DurableWorkInstruction instruction) {
        PreparedExecution prepared = prepare(instruction);
        return Objects.requireNonNull(
                ownerOperation.handle(prepared.occurrence().fact()),
                "Standing Free owner operation outcome");
    }

    /**
     * Executes one technically claimed Standing Free work item with durable
     * attempt evidence around the Commercial consequence.
     *
     * <p>An empty result means recovery found no committed baseline and explicitly
     * rescheduled the work. An expired worker can still be running: retry safety
     * comes from the Commercial owner's duplicate/concurrent-delivery invariant,
     * not from treating an absent row as proof that the prior effect cannot occur.
     * See MS-PROT-056 v1.6,
     * designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md,
     * §5 — Idempotency and duplicate delivery; and MS-PROT-065 v1.1,
     * designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer,
     * Attempt & Retry Execution Contract Amendment.md, §24 — RETRY_SAFE and
     * §52 — Claim Does Not Replace Domain Concurrency.</p>
     */
    @Override
    public Optional<StandingFreeBaseline> executeClaimed(
            ClaimedWork claimedWork,
            DurableWorkStore workStore,
            String attemptIdentity) {
        Objects.requireNonNull(claimedWork, "claimedWork");
        Objects.requireNonNull(workStore, "workStore");
        requireIdentifier(attemptIdentity, "attemptIdentity");

        DurableWorkInstruction instruction = claimedWork.instruction();
        PreparedExecution prepared = prepare(instruction);
        String workIdentity = instruction.workIdentity();
        String workerIdentity = claimedWork.workerIdentity();

        Optional<WorkAttempt> latest = workStore.latestAttempt(workIdentity);
        if (latest.isPresent()) {
            Optional<StandingFreeBaseline> recovered = recoverPriorAttempt(
                    latest.orElseThrow(),
                    prepared,
                    workStore,
                    workIdentity,
                    workerIdentity);
            if (recovered != null) {
                return recovered;
            }
        }

        WorkAttempt started = workStore.startAttempt(
                attemptIdentity,
                workIdentity,
                workerIdentity,
                clock.instant(),
                prepared.context().principal().identifier());

        StandingFreeBaseline outcome = Objects.requireNonNull(
                ownerOperation.handle(prepared.occurrence().fact()),
                "Standing Free owner operation outcome");
        workStore.recordAttemptOutcome(
                started.attemptIdentity(),
                workerIdentity,
                BackgroundWorkResultClassification.SUCCESS,
                Optional.of(evidenceReference(outcome)));
        workStore.finalise(
                workIdentity,
                workerIdentity,
                BackgroundWorkResultClassification.SUCCESS,
                clock.instant());
        return Optional.of(outcome);
    }

    private Optional<StandingFreeBaseline> recoverPriorAttempt(
            WorkAttempt prior,
            PreparedExecution prepared,
            DurableWorkStore workStore,
            String workIdentity,
            String workerIdentity) {
        Optional<BackgroundWorkResultClassification> classification = prior.resultClassification();

        if (classification.equals(Optional.of(BackgroundWorkResultClassification.RETRY_SAFE))) {
            return null;
        }

        if (classification.equals(Optional.of(BackgroundWorkResultClassification.SUCCESS))) {
            StandingFreeBaseline committed = ownerOperation
                    .committedBaseline(prepared.occurrence().fact())
                    .orElseThrow(() -> new IllegalStateException(
                            "Successful Standing Free attempt lacks committed owner evidence"));
            workStore.finalise(
                    workIdentity,
                    workerIdentity,
                    BackgroundWorkResultClassification.SUCCESS,
                    clock.instant());
            return Optional.of(committed);
        }

        if (classification.isPresent()) {
            throw new IllegalStateException(
                    "Standing Free work cannot execute after prior attempt classification: "
                            + classification.orElseThrow());
        }

        Optional<StandingFreeBaseline> committed = ownerOperation
                .committedBaseline(prepared.occurrence().fact());
        if (committed.isPresent()) {
            StandingFreeBaseline baseline = committed.orElseThrow();
            workStore.recordAttemptOutcome(
                    prior.attemptIdentity(),
                    workerIdentity,
                    BackgroundWorkResultClassification.SUCCESS,
                    Optional.of(evidenceReference(baseline)));
            workStore.finalise(
                    workIdentity,
                    workerIdentity,
                    BackgroundWorkResultClassification.SUCCESS,
                    clock.instant());
            return Optional.of(baseline);
        }

        workStore.recordAttemptOutcome(
                prior.attemptIdentity(),
                workerIdentity,
                BackgroundWorkResultClassification.RETRY_SAFE,
                Optional.empty());
        workStore.rescheduleRetry(workIdentity, workerIdentity, clock.instant());
        return Optional.empty();
    }

    private PreparedExecution prepare(DurableWorkInstruction instruction) {
        Objects.requireNonNull(instruction, "instruction");
        BackgroundWorkContractAffinity affinity = instruction.contractAffinity()
                .orElseThrow(() -> new IllegalStateException(
                        "Legacy or unregistered durable work is not executable"));
        BackgroundWorkContractRegistrySnapshot historicalRegistry = historicalContracts
                .apply(affinity.semanticRegistryReleaseIdentifier())
                .orElseThrow(() -> new IllegalStateException(
                        "Historical Background Work Contract release is unavailable"));
        BackgroundWorkContractDefinition capturedContract = instruction
                .registeredContract(historicalRegistry)
                .filter(StandingFreeBackgroundWorkContract.DEFINITION::equals)
                .orElseThrow(() -> new IllegalStateException(
                        "Durable work does not match the captured Standing Free contract"));
        if (!capturedContract.identity().equals(StandingFreeBackgroundWorkContract.IDENTITY)) {
            throw new IllegalStateException("Durable work belongs to another owner contract");
        }
        if (clock.instant().isBefore(instruction.dueAt())) {
            throw new IllegalStateException("Standing Free background work is not yet due");
        }

        BackgroundWorkContractRegistrySnapshot currentRegistry = Objects.requireNonNull(
                currentContracts.get(), "current Background Work Contract registry");
        BackgroundWorkContractDefinition currentContract = currentRegistry
                .contract(currentRegistry.semanticRegistryReleaseIdentifier(), capturedContract.identity())
                .filter(StandingFreeBackgroundWorkContract.DEFINITION::equals)
                .orElseThrow(() -> new IllegalStateException(
                        "Standing Free Background Work Contract is not currently registered"));

        String establishmentIdentity = instruction.causationIdentifier()
                .orElseThrow(() -> new IllegalStateException(
                        "Standing Free work lacks establishment causation"));
        MerchantAccountEstablishedOccurrence occurrence = sourceAuthority
                .authoritativeOccurrence(establishmentIdentity)
                .filter(value -> value.fact().establishmentIdentity().equals(establishmentIdentity))
                .orElseThrow(() -> new IllegalStateException(
                        "Merchant Account establishment is not authoritative"));
        MerchantScope trustedScope = occurrence.fact().merchantScope();
        if (instruction.merchantScope().isEmpty()
                || !instruction.merchantScope().orElseThrow().equals(trustedScope)) {
            throw new IllegalStateException(
                    "Durable work Merchant Scope does not match owner authority");
        }

        TrustedExecutionContext context = Objects.requireNonNull(
                executionAuthority.establish(currentContract, trustedScope),
                "scheduled Background Work execution context");
        if (!context.merchantScope().equals(trustedScope)) {
            throw new IllegalStateException(
                    "Scheduled Background Work authority returned another Merchant Scope");
        }
        return new PreparedExecution(occurrence, context);
    }

    private static String evidenceReference(StandingFreeBaseline baseline) {
        return "standing-free-baseline/" + baseline.baselineIdentity();
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private record PreparedExecution(
            MerchantAccountEstablishedOccurrence occurrence,
            TrustedExecutionContext context) {
        private PreparedExecution {
            Objects.requireNonNull(occurrence, "occurrence");
            Objects.requireNonNull(context, "context");
        }
    }
}
