package com.example.SpringPremier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
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
		Dotenv dotenv = null;
		try {
			dotenv = Dotenv.configure().ignoreIfMissing().load();
		} catch (Exception e) {
			System.out.println(".env 파일이 없어 환경변수를 사용합니다.");
		}

		setEnvOrDefault("SPRING_DATASOURCE_USERNAME", dotenv, "sa");
		setEnvOrDefault("SPRING_DATASOURCE_PASSWORD", dotenv, "");
		setEnvOrDefault("SPRING_SECURITY_USER_NAME", dotenv, "test");
		setEnvOrDefault("SPRING_SECURITY_USER_PASSWORD", dotenv, "test1234");
		setEnvOrDefault("SPRING_JWT_SECRET", dotenv, "test-secret-key-for-jwt-1234567890");
	}

	private static void setEnvOrDefault(String key, Dotenv dotenv, String defaultValue) {
		String value = System.getenv(key);
		if (value == null && dotenv != null) {
			value = dotenv.get(key);
		}
		if (value == null) {
			value = defaultValue;
			System.out.println("[WARN] " + key + " 기본값 사용 → " + defaultValue);
		}
		System.setProperty(key, value);
	}

	@Test
	void contextLoads() {

	}


}
