package com.tanzu.demo.odata.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class OdataBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdataBatchApplication.class, args);
    }

}
