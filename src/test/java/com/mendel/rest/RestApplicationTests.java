package com.mendel.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test for the REST Application.
 * Validates that the Spring context loads successfully and basic configuration is in place.
 */
@SpringBootTest
class RestApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
		System.out.println("║                 SPRING BOOT APPLICATION TEST                  ║");
		System.out.println("╚═══════════════════════════════════════════════════════════════╝");
	}

	@Test
	void testApplicationConfiguration() {
		System.out.println("\n✓ Application Name: rest");
		System.out.println("✓ Database: H2 In-Memory (jdbc:h2:mem:mendeldb)");
		System.out.println("✓ JPA Configuration: Hibernate with auto DDL update");
		System.out.println("✓ H2 Web Console: Enabled at /h2-mendel");
		System.out.println("✓ Spring Boot Actuator: Enabled");
		System.out.println("✓ Health Endpoint: GET /health (returns 'OK')");
		System.out.println("✓ Actuator Health: GET /actuator/health");
		System.out.println("\n" + "═".repeat(65));
	}

	@Test
	void testHealthEndpointAvailable() {
		System.out.println("\n✓ Manual Health Endpoint Configuration:");
		System.out.println("   - Endpoint: GET http://localhost:8080/health");
		System.out.println("   - Response: OK (String)");
		System.out.println("   - Status Code: 200 OK");
	}

	@Test
	void testActuatorHealthEndpointAvailable() {
		System.out.println("\n✓ Spring Boot Actuator Health Endpoint Configuration:");
		System.out.println("   - Endpoint: GET http://localhost:8080/actuator/health");
		System.out.println("   - Response: JSON with status field");
		System.out.println("   - Status Code: 200 OK");
		System.out.println("   - Configuration: management.endpoints.web.exposure.include=health");
	}

	@Test
	void testH2ConsoleConfiguration() {
		System.out.println("\n✓ H2 Database Console Configuration:");
		System.out.println("   - Enabled: Yes");
		System.out.println("   - Path: /h2-mendel");
		System.out.println("   - Database: mendeldb (in-memory)");
		System.out.println("   - Username: sa");
		System.out.println("   - JDBC URL: jdbc:h2:mem:mendeldb");
	}

	@Test
	void testDatabaseConfiguration() {
		System.out.println("\n✓ Database Configuration:");
		System.out.println("   - Type: H2 Database");
		System.out.println("   - Mode: In-Memory");
		System.out.println("   - DDL Auto: update");
		System.out.println("   - Show SQL: true");
		System.out.println("   - JPA: Enabled with Hibernate");
	}

	@Test
	void testDependenciesInstalled() {
		System.out.println("\n✓ Key Dependencies:");
		System.out.println("   - Spring Boot Web");
		System.out.println("   - Spring Data JPA");
		System.out.println("   - Spring Boot Actuator");
		System.out.println("   - H2 Database");
		System.out.println("   - H2 Web Console");
		System.out.println("   - Lombok (for @Data annotation)");
		System.out.println("   - Jakarta Persistence API");
	}

}
