package com.yushun.recommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.yushun")
public class RecommenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecommenderApplication.class, args);
    }
}