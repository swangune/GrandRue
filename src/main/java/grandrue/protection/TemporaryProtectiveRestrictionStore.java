package grandrue.protection;

/** Persistence boundary for bounded Resource Protection restrictions. */
@FunctionalInterface
public interface TemporaryProtectiveRestrictionStore {
    TemporaryProtectiveRestriction establish(TemporaryProtectiveRestriction restriction);
}
