
package com.ta.ditec.services.utils;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender emailSender;

	public EmailService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void sendEmail(String recipientEmail, String loginDetailsHtml, String subject) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			helper.setTo(recipientEmail);
			helper.setText(loginDetailsHtml, true);
			helper.setSubject(subject);
			emailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
