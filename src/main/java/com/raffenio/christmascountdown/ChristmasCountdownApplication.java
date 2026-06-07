package com.raffenio.christmascountdown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChristmasCountdownApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChristmasCountdownApplication.class, args);
    }
}
