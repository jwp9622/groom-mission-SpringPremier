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
@ActiveProfiles("test")  // application-test.properties 사용
class WebServiceBasicApplicationTests {

	@Test
	void contextLoads() {

	}


}
