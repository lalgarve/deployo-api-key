package io.deployo.apikey;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

/**
 * Smoke test for the actual entry point -- proves the application boots for real through its
 * own main() (not just through Spring's test context loader, which ApiKeysMigrationTest
 * already exercises). Uses its own isolated in-memory H2 database so it doesn't interfere
 * with the schema state other tests depend on.
 */
class DeployoApiKeyApplicationTests {

    @Test
    void mainBootsTheApplication() {
        assertThatCode(() -> DeployoApiKeyApplication.main(new String[] {
                "--spring.datasource.url=jdbc:h2:mem:deployo_api_key_boot;MODE=PostgreSQL"
        })).doesNotThrowAnyException();
    }
}
