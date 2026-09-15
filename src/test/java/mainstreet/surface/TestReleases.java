package mainstreet.surface;

import mainstreet.semantic.configuration.ActiveRelease;
import mainstreet.semantic.configuration.ConfigurationRelease;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.executable.ExecutableMerchantModel;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

final class TestReleases {

    private TestReleases() {
    }

    static ActiveRelease activeRelease(
            String merchantIdentifier,
            String semanticRelease
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                merchantIdentifier,
                "configuration-1",
                1,
                semanticRelease,
                Set.of(),
                Set.of(),
                Optional.empty()
        );
        ExecutableMerchantModel model = new ExecutableMerchantModel(
                merchantIdentifier,
                "configuration-1",
                1,
                semanticRelease,
                Set.of(),
                List.of(),
                List.of()
        );
        return new ActiveRelease(new ConfigurationRelease(
                "release-" + merchantIdentifier,
                configuration,
                model,
                "test-compiler",
                Instant.parse("2026-09-01T08:10:00Z")
        ));
    }
}
