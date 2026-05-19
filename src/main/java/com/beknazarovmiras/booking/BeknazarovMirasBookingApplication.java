package com.beknazarovmiras.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BeknazarovMirasBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeknazarovMirasBookingApplication.class, args);
    }
}
