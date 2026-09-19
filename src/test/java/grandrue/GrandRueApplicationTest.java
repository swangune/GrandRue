package grandrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GrandRueApplicationTest {

    @Test
    void backend_entry_point_is_a_spring_boot_application_shell() {
        assertTrue(
                GrandRueApplication.class.isAnnotationPresent(
                        SpringBootApplication.class
                )
        );
    }
}
