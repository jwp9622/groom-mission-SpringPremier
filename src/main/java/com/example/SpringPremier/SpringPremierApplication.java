package com.example.SpringPremier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing	//객체 생성 변경시 자동으로 값 등록
public class SpringPremierApplication {

	public static void main(String[] args) {

		// 수정 코드 (안전하게 사용)
		Dotenv dotenv = Dotenv.configure()
			.ignoreIfMissing()  // ✅ .env 없으면 무시
			.load();

		setPropertyFromEnvOrDotenv("SPRING_DATASOURCE_USERNAME", dotenv);
		setPropertyFromEnvOrDotenv("SPRING_DATASOURCE_PASSWORD", dotenv);
		setPropertyFromEnvOrDotenv("SPRING_SECURITY_USER_NAME", dotenv);
		setPropertyFromEnvOrDotenv("SPRING_SECURITY_USER_PASSWORD", dotenv);
		setPropertyFromEnvOrDotenv("SPRING_JWT_SECRET", dotenv);

		SpringApplication.run(SpringPremierApplication.class, args);

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
