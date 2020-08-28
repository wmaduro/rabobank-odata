package com.tanzu.demo.odata.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ODataClientApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ODataClientApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ODataClientApplication.class, args);
    }
}
