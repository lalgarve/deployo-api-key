package io.deployo.apikey;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

/**
 * Smoke test for the actual entry point -- proves the application boots for real through its
 * own main() (not just through Spring's test context loader, which ApiKeysMigrationTest
 * already exercises). Uses its own isolated in-memory H2 database so it doesn't interfere
 * with the schema state other tests depend on.
 *
 * Overrides both the datasource URL AND driver-class-name: when CI runs with
 * SPRING_PROFILES_ACTIVE=docker, application-docker.yml sets driver-class-name to
 * org.postgresql.Driver, which otherwise wins over this test's H2 URL and fails with
 * "Driver org.postgresql.Driver claims to not accept jdbcUrl, jdbc:h2:...". Invisible locally
 * without Docker, since the docker profile was never actually exercised there.
 */
class DeployoApiKeyApplicationTests {

    @Test
    void mainBootsTheApplication() {
        assertThatCode(() -> DeployoApiKeyApplication.main(new String[] {
                "--spring.datasource.url=jdbc:h2:mem:deployo_api_key_boot;MODE=PostgreSQL",
                "--spring.datasource.driver-class-name=org.h2.Driver"
        })).doesNotThrowAnyException();
    }
}
