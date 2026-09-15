package mainstreet.api;

/**
 * One exact, surface-affined scope-establishment rule implementation.
 * Implementations validate their evidence and return only the scope they have
 * established; principal, relationship and runtime-authority resolution remain
 * separate responsibilities.
 */
public interface ApiTransportScopeEstablishmentAuthority {

    String ruleReference();

    ApiSurfaceClass surface();

    Class<? extends ApiTransportScopeEvidence> evidenceType();

    ApiTransportScope establish(ApiTransportScopeEvidence evidence);
}
