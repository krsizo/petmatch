package com.petmatch.petmatchapi.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(properties = {
	"spring.jpa.hibernate.ddl-auto=create-drop",
	"spring.sql.init.mode=never",
	"spring.rabbitmq.listener.simple.auto-startup=false",
	"spring.jpa.properties.hibernate.jdbc.time_zone=UTC"
})
public abstract class IntegrationTestBase {

	@ServiceConnection
	static PostgreSQLContainer<?> postgres =
		new PostgreSQLContainer<>("postgres:16")
			.withEnv("TZ", "UTC")
			.withEnv("PGTZ", "UTC");;
}
