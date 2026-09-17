package grandrue.media;

import java.util.List;
import java.util.Optional;

/** Persistence/idempotency boundary for Media-owned source and rendition metadata. */
public interface MediaStore {
    MediaAsset establishAsset(MediaAsset asset);
    MediaRendition establishRendition(MediaRendition rendition);
    Optional<MediaAsset> findAsset(String assetIdentity);
    Optional<MediaRendition> findRendition(String renditionIdentity);
    List<MediaRendition> renditionsForSource(String assetIdentity);
}
