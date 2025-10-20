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

		// 2️⃣ 우선순위: 환경변수 > .env
		setSystemProperty("SPRING_DATASOURCE_USERNAME", dotenv);
		setSystemProperty("SPRING_DATASOURCE_PASSWORD", dotenv);
		setSystemProperty("SPRING_SECURITY_USER_NAME", dotenv);
		setSystemProperty("SPRING_SECURITY_USER_PASSWORD", dotenv);
		setSystemProperty("SPRING_JWT_SECRET", dotenv);

		SpringApplication.run(SpringPremierApplication.class);

	}

	@Test
	void contextLoads() {

	}



	private static void setSystemProperty(String key, Dotenv dotenv) {
		String value = System.getenv(key); // 환경변수 우선
		if (value == null && dotenv != null) {
			value = dotenv.get(key);
		}
		if (value != null) {
			System.setProperty(key, value);
		} else {
			System.out.println("[WARN] 환경변수 없음: " + key);
		}
	}

	private static void setPropertyFromEnvOrDotenv(String key, Dotenv dotenv) {
		String value = System.getenv(key);
		if (value == null) {
			value = dotenv.get(key);
		}

		if (value != null) {
			System.setProperty(key, value);
		} else {
			System.err.printf("❗ 환경변수 또는 .env에 '%s'가 설정되지 않았습니다.%n", key);
		}
	}

}
