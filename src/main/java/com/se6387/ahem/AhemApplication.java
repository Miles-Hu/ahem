package com.se6387.ahem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AhemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AhemApplication.class, args);
    }

}
