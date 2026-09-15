package mainstreet.prototype;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PrototypeLocalRuntimeConfigurationTest {

    @Test
    void compose_and_spring_defaults_use_the_dedicated_prototype_postgres_host_port()
            throws IOException {
        String compose = Files.readString(Path.of("compose.prototype.yml"));
        String properties = Files.readString(Path.of(
                "src/main/resources/application-prototype.properties"
        ));

        assertTrue(
                compose.contains("\"55432:5432\""),
                "Prototype PostgreSQL must not assume host port 5432 is free"
        );
        assertTrue(
                properties.contains(
                        "jdbc:postgresql://localhost:55432/mainstreet"
                ),
                "Spring prototype datasource must match the Compose host port"
        );
    }

    @Test
    void postgres_18_uses_the_supported_parent_data_mount_and_versioned_prototype_volume()
            throws IOException {
        String compose = Files.readString(Path.of("compose.prototype.yml"));

        assertTrue(
                compose.contains("postgres:18-alpine"),
                "This contract test applies to the PostgreSQL 18 prototype image"
        );
        assertTrue(
                compose.contains(
                        "mainstreet-prototype-postgres-v18:/var/lib/postgresql"
                ),
                "PostgreSQL 18 must mount the parent /var/lib/postgresql directory"
        );
        assertTrue(
                !compose.contains("/var/lib/postgresql/data"),
                "The legacy /var/lib/postgresql/data mount is rejected by PostgreSQL 18 images"
        );
    }
}
