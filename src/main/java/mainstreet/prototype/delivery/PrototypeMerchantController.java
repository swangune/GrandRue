package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypeMerchantRuntime;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

/**
 * Prototype-only read delivery surface over active merchant configuration.
 * The controller translates transport concerns and owns no business semantics.
 */
@RestController
@RequestMapping("/prototype/merchants")
@Profile("prototype")
public final class PrototypeMerchantController {

    private final PrototypeMerchantRuntime runtime;

    public PrototypeMerchantController(PrototypeMerchantRuntime runtime) {
        this.runtime = Objects.requireNonNull(runtime, "runtime");
    }

    @GetMapping("/{merchantIdentifier}")
    public PrototypeMerchantResponse merchant(
            @PathVariable String merchantIdentifier
    ) {
        try {
            return PrototypeMerchantResponse.from(
                    runtime.merchant(merchantIdentifier)
            );
        } catch (IllegalArgumentException unknownMerchant) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Prototype merchant not found"
            );
        }
    }
}
