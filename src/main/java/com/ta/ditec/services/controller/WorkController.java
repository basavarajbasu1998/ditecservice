package com.ta.ditec.services.controller;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api/v1")
public class WorkController {
	
	private static final Logger logger = LoggerFactory.getLogger(WorkController.class);

	@Autowired
	private JavaMailSender primarySender;

	@GetMapping("/work")
	public String work() {
		MimeMessage message = primarySender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

			message.setContent("Hi", "text/html");
			String[] recipients = { "b@gmail.com", "c@gmail.com", "recipient3@example.com", "recipient4@example.com" };
			helper.setTo(recipients);
			String[] ccmail = { "ddd@gmail.com", "brskumar@taipl.com", "ccemail3@example.com" };
			helper.setCc(ccmail);
			helper.setBcc(ccmail);
			helper.setSubject("Verification Email");
			primarySender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Email not sent ");
			throw new TaException(ErrorCode.EMAIL_NOT_SENT, ErrorCode.EMAIL_NOT_SENT.getErrorMsg());
			
		}

		return "success";

	}
}
