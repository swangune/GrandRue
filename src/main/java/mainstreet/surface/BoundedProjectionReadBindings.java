package mainstreet.surface;

/** Package-owned issuer for opaque bounded Projection read bindings. */
final class BoundedProjectionReadBindings {

    private BoundedProjectionReadBindings() {
    }

    static BoundedProjectionReadBinding issue() {
        return new RuntimeBoundedProjectionReadBinding();
    }
}
