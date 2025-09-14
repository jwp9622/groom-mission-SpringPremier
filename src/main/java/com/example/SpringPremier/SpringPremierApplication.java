package com.example.SpringPremier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing	//객체 생성 변경시 자동으로 값 등록
public class SpringPremierApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringPremierApplication.class, args);
	}

}
