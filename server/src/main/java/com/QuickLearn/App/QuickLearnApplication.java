package com.QuickLearn.App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.QuickLearn")
public class QuickLearnApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickLearnApplication.class, args);
    }
}