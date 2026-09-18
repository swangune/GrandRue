package grandrue.merchantaccount;

import grandrue.application.MerchantScope;

import java.util.List;

/** Persistence/consistency boundary for MS-PROT-076 Merchant Account facts. */
public interface MerchantAccountLifecycleStore
        extends MerchantControllerRelationshipAuthority {

    MerchantControllerRelationship transferController(
            MerchantControllerTransferCommand command
    );

    MerchantAccountSuspension establishSuspension(
            MerchantAccountSuspensionCommand command
    );

    MerchantAccountSuspension releaseSuspension(
            MerchantAccountSuspensionReleaseCommand command
    );

    List<MerchantAccountSuspension> effectiveSuspensions(MerchantScope merchantScope);

    MerchantAccountLifecycle beginClosure(BeginMerchantAccountClosureCommand command);

    MerchantAccountLifecycle finalizeClosure(FinalizeMerchantAccountClosureCommand command);

    MerchantAccountLifecycle lifecycle(MerchantScope merchantScope);
}
