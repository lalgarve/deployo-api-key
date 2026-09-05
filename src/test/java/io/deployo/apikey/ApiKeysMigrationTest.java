package io.deployo.apikey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Proves the Flyway migration in data-model.md actually applies and enforces its invariants
 * -- run against the sandbox profile's H2 database (see src/test/resources/application.yml),
 * since this environment has no Docker/Postgres available. No ApiKey JPA entity exists yet
 * (that's T005); this only exercises the raw table via JdbcTemplate.
 *
 * Each test runs in its own transaction, rolled back afterwards -- no manual cleanup needed
 * between tests even though the H2 context (and its data) is reused across the class.
 */
@SpringBootTest(classes = DeployoApiKeyApplication.class)
@Transactional
class ApiKeysMigrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void insertsARowWithAllColumns() {
        Timestamp createdAt = Timestamp.from(Instant.now());
        Timestamp expiresAt = Timestamp.from(Instant.now().plusSeconds(90L * 24 * 60 * 60));

        jdbcTemplate.update(
                "INSERT INTO api_keys (service_name, key_hash, created_at, expires_at) VALUES (?, ?, ?, ?)",
                "email-service", "hash-with-validity", createdAt, expiresAt);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM api_keys WHERE service_name = ? AND key_hash = ?",
                Integer.class, "email-service", "hash-with-validity");
        assertThat(count).isEqualTo(1);
    }

    @Test
    void expiresAtIsNullableForKeysWithIndeterminateValidity() {
        jdbcTemplate.update(
                "INSERT INTO api_keys (service_name, key_hash, created_at, expires_at) VALUES (?, ?, ?, NULL)",
                "email-service", "hash-without-validity", Timestamp.from(Instant.now()));

        Timestamp expiresAt = jdbcTemplate.queryForObject(
                "SELECT expires_at FROM api_keys WHERE key_hash = ?",
                Timestamp.class, "hash-without-validity");
        assertThat(expiresAt).isNull();
    }

    @Test
    void serviceNameCannotBeNull() {
        assertThatThrownBy(() -> jdbcTemplate.update(
                "INSERT INTO api_keys (service_name, key_hash, created_at) VALUES (NULL, ?, ?)",
                "some-hash", Timestamp.from(Instant.now())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void keyHashCannotBeNull() {
        assertThatThrownBy(() -> jdbcTemplate.update(
                "INSERT INTO api_keys (service_name, key_hash, created_at) VALUES (?, NULL, ?)",
                "email-service", Timestamp.from(Instant.now())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void createdAtCannotBeNull() {
        assertThatThrownBy(() -> jdbcTemplate.update(
                "INSERT INTO api_keys (service_name, key_hash, created_at) VALUES (?, ?, NULL)",
                "email-service", "some-hash"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void keyHashMustBeUnique() {
        Timestamp now = Timestamp.from(Instant.now());
        jdbcTemplate.update(
                "INSERT INTO api_keys (service_name, key_hash, created_at) VALUES (?, ?, ?)",
                "email-service", "duplicate-hash", now);

        assertThatThrownBy(() -> jdbcTemplate.update(
                "INSERT INTO api_keys (service_name, key_hash, created_at) VALUES (?, ?, ?)",
                "another-service", "duplicate-hash", now))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
