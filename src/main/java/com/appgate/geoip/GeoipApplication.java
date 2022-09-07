package com.appgate.geoip;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.appgate.geoip"})
public class GeoipApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoipApplication.class, args);
	}

}
