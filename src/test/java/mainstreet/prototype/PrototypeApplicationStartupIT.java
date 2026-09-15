package mainstreet.prototype;

import mainstreet.GrandRueApplication;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrototypeApplicationStartupIT {

    @Test
    void prototype_profile_boots_real_spring_application_and_serves_http()
            throws Exception {
        SpringApplication application = new SpringApplication(
                GrandRueApplication.class
        );
        try (ConfigurableApplicationContext context = application.run(
                "--spring.profiles.active=prototype",
                "--server.port=0",
                "--spring.datasource.url="
                        + requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                "--spring.datasource.username="
                        + requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                "--spring.datasource.password="
                        + requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        )) {
            PrototypeMerchantRuntime merchants = context.getBean(
                    PrototypeMerchantRuntime.class
            );
            assertEquals(
                    "prototype-retailer-release-1",
                    merchants.merchant("prototype-retailer").releaseIdentifier()
            );
            assertNotNull(context.getBean(PrototypeOrderUseCase.class));
            assertNotNull(context.getBean(PrototypeBookingUseCase.class));
            assertNotNull(context.getBean(PrototypeAppointmentUseCase.class));
            assertNotNull(context.getBean(PrototypePublicOrderUseCase.class));
            assertNotNull(context.getBean(PrototypePublicBookingUseCase.class));
            assertNotNull(context.getBean(PrototypePublicAppointmentUseCase.class));
            assertEquals(
                    "publication-1",
                    context.getBean(PrototypePublicationProjection.class)
                            .publications("prototype-publisher")
                            .getFirst()
                            .publicationIdentifier()
            );

            String port = context.getEnvironment().getProperty(
                    "local.server.port"
            );
            assertNotNull(port);
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder(uri(
                                    port,
                                    "/prototype/merchants/prototype-retailer"
                            ))
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("prototype-retailer"));
            assertTrue(response.body().contains("ordering.commit"));

            provePublicAppointmentIntent(client, port);
            provePublicBookingIntent(client, port);
            resetRetailerStock(context.getBean(DSLContext.class));
            provePublicOrderIntent(client, port);
        }
    }

    private static void provePublicAppointmentIntent(
            HttpClient client,
            String port
    ) throws Exception {
        String identifier = "startup-public-appointment-1";
        HttpResponse<String> receipt = postJson(
                client,
                port,
                "/prototype/public/merchants/prototype-gardener-bookable/appointments",
                "startup-public-appointment-command-1",
                """
                        {
                          "appointmentIdentifier":"startup-public-appointment-1",
                          "subjectReference":"garden-maintenance",
                          "startsAt":"2037-01-12T13:00:00Z",
                          "endsAt":"2037-01-12T15:00:00Z"
                        }
                        """
        );
        assertEquals(201, receipt.statusCode());
        assertTrue(receipt.body().contains("garden-maintenance"));
        assertFalse(receipt.body().contains("gardening.perform"));
        assertFalse(receipt.body().contains("customer-1"));
        assertFalse(receipt.body().contains("capacity"));

        HttpResponse<String> internal = get(
                client,
                port,
                "/prototype/merchants/prototype-gardener-bookable/appointments/"
                        + identifier
        );
        assertEquals(200, internal.statusCode());
        assertTrue(internal.body().contains("gardening.perform"));
        assertTrue(internal.body().contains("customer-1"));
    }

    private static void provePublicBookingIntent(
            HttpClient client,
            String port
    ) throws Exception {
        String identifier = "startup-public-booking-1";
        HttpResponse<String> receipt = postJson(
                client,
                port,
                "/prototype/public/merchants/prototype-daycare/bookings",
                "startup-public-booking-command-1",
                """
                        {
                          "bookingIdentifier":"startup-public-booking-1",
                          "subjectReference":"full-day-care-session",
                          "startsAt":"2037-01-13T08:00:00Z",
                          "endsAt":"2037-01-13T17:00:00Z"
                        }
                        """
        );
        assertEquals(201, receipt.statusCode());
        assertTrue(receipt.body().contains("full-day-care-session"));
        assertFalse(receipt.body().contains("daycare-session"));
        assertFalse(receipt.body().contains("customer-1"));

        HttpResponse<String> internal = get(
                client,
                port,
                "/prototype/merchants/prototype-daycare/bookings/" + identifier
        );
        assertEquals(200, internal.statusCode());
        assertTrue(internal.body().contains("daycare-session"));
        assertTrue(internal.body().contains("customer-1"));
    }

    private static void provePublicOrderIntent(
            HttpClient client,
            String port
    ) throws Exception {
        String identifier = "startup-public-order-1";
        HttpResponse<String> receipt = postJson(
                client,
                port,
                "/prototype/public/merchants/prototype-retailer/orders",
                "startup-public-order-command-1",
                """
                        {
                          "orderIdentifier":"startup-public-order-1",
                          "portions":[{
                            "portionIdentifier":"startup-public-portion-1",
                            "subjectReference":"milk-2l",
                            "quantity":1
                          }]
                        }
                        """
        );
        assertEquals(201, receipt.statusCode());
        assertTrue(receipt.body().contains("milk-2l"));
        assertFalse(receipt.body().contains("sku-1"));
        assertFalse(receipt.body().contains("prototype-offering"));

        HttpResponse<String> internal = get(
                client,
                port,
                "/prototype/merchants/prototype-retailer/orders/" + identifier
        );
        assertEquals(200, internal.statusCode());
        assertTrue(internal.body().contains("sku-1"));
        assertTrue(internal.body().contains("prototype-offering:sku-1@1"));
    }

    private static void resetRetailerStock(DSLContext dsl) {
        dsl.update(DSL.table(DSL.name("inventory_stock_position")))
                .set(DSL.field(DSL.name("stock_on_hand"), Long.class), 10L)
                .where(DSL.field(DSL.name("merchant_identifier"), String.class)
                        .eq("prototype-retailer"))
                .and(DSL.field(DSL.name("subject_identifier"), String.class)
                        .eq("sku-1"))
                .execute();
    }

    private static HttpResponse<String> postJson(
            HttpClient client,
            String port,
            String path,
            String idempotencyKey,
            String body
    ) throws Exception {
        return client.send(
                HttpRequest.newBuilder(uri(port, path))
                        .header("Content-Type", "application/json")
                        .header("Idempotency-Key", idempotencyKey)
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
    }

    private static HttpResponse<String> get(
            HttpClient client,
            String port,
            String path
    ) throws Exception {
        return client.send(
                HttpRequest.newBuilder(uri(port, path))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
    }

    private static URI uri(String port, String path) {
        return URI.create("http://localhost:" + port + path);
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: "
                            + name
            );
        }
        return value;
    }
}
