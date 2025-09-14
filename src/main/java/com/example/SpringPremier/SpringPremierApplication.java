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

		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("SPRING_SECURITY_USER_NAME", dotenv.get("SPRING_SECURITY_USER_NAME"));
		System.setProperty("SPRING_SECURITY_USER_PASSWORD", dotenv.get("SPRING_SECURITY_USER_PASSWORD"));
		System.setProperty("SPRING_JWT_SECRET", dotenv.get("SPRING_JWT_SECRET"));

		SpringApplication.run(SpringPremierApplication.class, args);

	}

}
