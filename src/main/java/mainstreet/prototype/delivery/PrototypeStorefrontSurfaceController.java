package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypeStorefrontSurfaceProjection;
import mainstreet.prototype.PrototypeStorefrontSurfaceView;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Profile("prototype")
public final class PrototypeStorefrontSurfaceController {

    private final PrototypeStorefrontSurfaceProjection projection;

    public PrototypeStorefrontSurfaceController(
            PrototypeStorefrontSurfaceProjection projection
    ) {
        this.projection = Objects.requireNonNull(projection, "projection");
    }

    @GetMapping(
            "/prototype/merchants/{merchantIdentifier}/storefront-surface"
    )
    public PrototypeStorefrontSurfaceView surface(
            @PathVariable String merchantIdentifier
    ) {
        return projection.surface(merchantIdentifier);
    }
}
