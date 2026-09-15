package mainstreet.api;

/**
 * One governed mapping from exact owner evidence to safe API problem meaning.
 */
public interface ApiProblemMappingAuthority {

    String mappingRuleReference();

    ApiOwnerContractReference ownerContractReference();

    Class<? extends ApiProblemEvidence> evidenceType();

    ApiProblem map(ApiProblemEvidence evidence);
}
