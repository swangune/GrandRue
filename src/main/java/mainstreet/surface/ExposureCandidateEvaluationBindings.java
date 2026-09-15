package mainstreet.surface;

/** Package-owned issuer for opaque candidate-evaluation bindings. */
final class ExposureCandidateEvaluationBindings {

    private ExposureCandidateEvaluationBindings() {
    }

    static ExposureCandidateEvaluationBinding issue() {
        return new RuntimeExposureCandidateEvaluationBinding();
    }
}
