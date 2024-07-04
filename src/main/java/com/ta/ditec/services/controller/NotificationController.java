package com.ta.ditec.services.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.Notification;
import com.ta.ditec.services.request.NotificationRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.NotificationService;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Notification Detilles ", tags = "Notification  ", description = "Notification Detilles  ")

public class NotificationController {

	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	private NotificationService notificationService;

	@PostMapping("/getallnotification")
	ResponseEntity<TaResponse<List<Notification>>> getdata(@RequestBody @Valid NotificationRequest req) {

		logger.debug(req.toString());

		List<Notification> lists = notificationService.getNotification(req);
		return createResponse(lists, "success", 1000, Type.INFORMATION);

	}

	@PostMapping("/notification")
	ResponseEntity<TaResponse<Object>> getdatasubaua(@RequestBody @Valid NotificationRequest req) {
		logger.debug(req.toString());
		if (req != null) {
			Notification lists = notificationService.getNotificationSuAua(req);
			logger.debug(lists.toString());
			return createResponse(null, "success", 1000, Type.INFORMATION);
		} else {
			return createResponse(null, "failure!", 1001, Type.INFORMATION);
		}
	}

	<T> ResponseEntity<TaResponse<T>> createResponse(T data, String message, int code, Type type) {
		TaResponse<T> response = new TaResponse<>();
		response.setResponseData(data);
		response.setMessage(message);
		response.setCode(code);
		response.setType(type);
		return ResponseEntity.ok(response);
	}

}
