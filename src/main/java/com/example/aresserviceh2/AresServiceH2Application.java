package com.example.aresserviceh2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class AresServiceH2Application {

    public static void main(String[] args) {
        SpringApplication.run(AresServiceH2Application.class, args);
    }

}