package com.biljartline.billiardsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class BilliardsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilliardsApiApplication.class, args);
    }
}
