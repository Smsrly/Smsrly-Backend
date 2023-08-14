package com.example.smsrly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SmsrlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsrlyApplication.class, args);
    }

}
