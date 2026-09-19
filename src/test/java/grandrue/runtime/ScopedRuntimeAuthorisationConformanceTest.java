package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.semantic.executable.ApplicableOperation;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScopedRuntimeAuthorisationConformanceTest {

    @Test
    void scoped_dispatch_accepts_only_an_established_trusted_execution_context()
            throws Exception {
        Method dispatch = ScopedOperationDispatcher.class.getMethod(
                "dispatch",
                TrustedExecutionContext.class,
                String.class,
                Object.class
        );

        assertEquals(
                TrustedExecutionContext.class,
                dispatch.getParameterTypes()[0]
        );
        assertThrows(
                NoSuchMethodException.class,
                () -> ScopedOperationDispatcher.class.getMethod(
                        "dispatch",
                        MerchantScope.class,
                        ExecutionPrincipal.class,
                        String.class,
                        Object.class
                )
        );
    }

    @Test
    void execution_context_carries_identity_only_execution_principal() {
        assertEquals(
                ExecutionPrincipal.class,
                OperationExecutionContext.class.getRecordComponents()[1].getType()
        );
    }

    @Test
    void legacy_pre_scoped_runtime_types_are_absent_from_production_classpath() {
        for (String legacyType : List.of(
                "mainstreet.runtime.Actor",
                "mainstreet.runtime.OperationRuntime",
                "mainstreet.runtime.OperationRequest",
                "mainstreet.runtime.OperationExecution",
                "mainstreet.runtime.OperationComposer",
                "mainstreet.runtime.ConsequenceResolver"
        )) {
            assertThrows(
                    ClassNotFoundException.class,
                    () -> Class.forName(legacyType),
                    legacyType + " must not return to the production classpath"
            );
        }
    }

    @Test
    void execution_guard_requires_explicit_scope_and_execution_principal()
            throws Exception {
        Method validate = OperationExecutionGuard.class.getMethod(
                "validate",
                MerchantScope.class,
                ExecutionPrincipal.class,
                ApplicableOperation.class
        );

        assertEquals(MerchantScope.class, validate.getParameterTypes()[0]);
        assertEquals(ExecutionPrincipal.class, validate.getParameterTypes()[1]);
    }
}
