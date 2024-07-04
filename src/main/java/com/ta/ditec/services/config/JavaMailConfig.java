package com.ta.ditec.services.config;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.mail.first")
	public JavaMailSender primarySender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		return javaMailSenderWithProperties(javaMailSender);
	}

//	@Bean
//	@ConfigurationProperties(prefix = "spring.mail.second")
//	public JavaMailSender secondarySender() {
//		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//		return javaMailSenderWithProperties(javaMailSender);
//	}

	private JavaMailSender javaMailSenderWithProperties(JavaMailSenderImpl javaMailSender) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		javaMailSender.setJavaMailProperties(props);
		return javaMailSender;
	}
}
