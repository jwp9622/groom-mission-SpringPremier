package com.example.SpringPremier.global.config;

import com.example.SpringPremier.global.exception.ValidateHandling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateHandlingConfig {

    @Bean
    public ValidateHandling handling(){
        return new ValidateHandling();
    }
}