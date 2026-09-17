package grandrue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GrandRue backend process entry point.
 *
 * <p>Spring Boot owns application/runtime wiring. GrandRue semantic and
 * capability-domain code remains plain Java and must not depend on this shell
 * merely to express business meaning.</p>
 */
@SpringBootApplication
public class GrandRueApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrandRueApplication.class, args);
    }
}
