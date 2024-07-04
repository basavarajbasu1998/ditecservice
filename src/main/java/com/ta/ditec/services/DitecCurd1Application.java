package com.ta.ditec.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@PropertySource(value = "file:D:/application.properties", ignoreResourceNotFound = true)
//@PropertySource(value = "file:D:/DBpropertires/assampropertites/application.properties", ignoreResourceNotFound = true)
//@PropertySource(value = "file:D:/DBpropertires/application.properties", ignoreResourceNotFound = true)
//@PropertySource(value = "file:/apps/apiservices/application.properties", ignoreResourceNotFound = true)
public class DitecCurd1Application {
	public static void main(String[] args) {
		SpringApplication.run(DitecCurd1Application.class, args);
	}

}
