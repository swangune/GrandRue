package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypePublicationProjection;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

/** Prototype-only read surface for an information-publication merchant. */
@RestController
@RequestMapping("/prototype/merchants/{merchantIdentifier}/publications")
@Profile("prototype")
public final class PrototypePublicationController {

    private final PrototypePublicationProjection publications;

    public PrototypePublicationController(
            PrototypePublicationProjection publications
    ) {
        this.publications = Objects.requireNonNull(publications, "publications");
    }

    @GetMapping
    public List<PrototypePublicationResponse> publications(
            @PathVariable String merchantIdentifier
    ) {
        try {
            return publications.publications(merchantIdentifier).stream()
                    .map(PrototypePublicationResponse::from)
                    .toList();
        } catch (IllegalArgumentException inapplicable) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Publication is not active for this merchant"
            );
        }
    }
}
