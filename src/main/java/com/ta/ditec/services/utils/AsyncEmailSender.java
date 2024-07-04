package com.ta.ditec.services.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsyncEmailSender {

	private final ExecutorService executorService = Executors.newFixedThreadPool(5);

	@Autowired
	private EmailService emailService;

	public CompletableFuture<Void> sendEmailAsync(String recipientEmail, String htmlMessage, String subject) {
		return CompletableFuture.runAsync(() -> {
			emailService.sendEmail(recipientEmail, htmlMessage, subject);
		}, executorService);
	}

	@PreDestroy
	public void shutdownExecutorService() {
		executorService.shutdown();
	}
}

