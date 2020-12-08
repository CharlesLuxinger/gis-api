package com.github.charlesluxinger.gisapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.github.charlesluxinger.gisapi")
@EntityScan(basePackages = "com.github.charlesluxinger.gisapi")
public class GisApiApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(GisApiApplication.class, args);
	}

}
