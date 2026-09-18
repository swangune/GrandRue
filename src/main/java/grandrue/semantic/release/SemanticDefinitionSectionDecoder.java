package grandrue.semantic.release;

/**
 * Registry-owned deterministic decoder for one immutable definition section of
 * a packaged semantic release bundle.
 *
 * @param <T> exact immutable registry snapshot produced by decoding
 */
@FunctionalInterface
public interface SemanticDefinitionSectionDecoder<T> {

    T decode(byte[] publishedDefinitions);
}
