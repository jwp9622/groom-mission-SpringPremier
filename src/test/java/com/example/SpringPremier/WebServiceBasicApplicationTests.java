package com.example.SpringPremier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class WebServiceBasicApplicationTests {

	@BeforeAll
	static void loadEnv() {

		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("SPRING_SECURITY_USER_NAME", dotenv.get("SPRING_SECURITY_USER_NAME"));
		System.setProperty("SPRING_SECURITY_USER_PASSWORD", dotenv.get("SPRING_SECURITY_USER_PASSWORD"));
		System.setProperty("SPRING_JWT_SECRET", dotenv.get("SPRING_JWT_SECRET"));
	}

	@Test
	void contextLoads() {

	}

}
