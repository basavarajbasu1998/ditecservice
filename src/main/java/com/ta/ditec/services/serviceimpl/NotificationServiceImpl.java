package com.ta.ditec.services.serviceimpl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.Notification;
import com.ta.ditec.services.repo.NotificationRepo;
import com.ta.ditec.services.request.NotificationRequest;
import com.ta.ditec.services.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	 private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
	@Autowired
	private NotificationRepo notificationRepo;

	@Override
	public List<Notification> getNotification(NotificationRequest req) {
		List<Notification> noti = notificationRepo.findBySubAuaIdOrderByCreatedDateDesc(req.getSubAuaId());
		logger.info("Data fetched successfully");
		return noti;
	}

	@Override
	public Notification getNotificationSuAua(NotificationRequest req) {
		List<Notification> noti = notificationRepo.findBySubAuaIdAndId(req.getSubAuaId(), req.getId());
		logger.info("Data fetched successfully");
		Notification list = noti.get(0);
		if (CollectionUtils.isNotEmpty(noti)) {
			list.setIsUnRead(false);
			notificationRepo.save(noti.get(0));
			logger.info("data saved successfully");
			return list;
		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}

	}

}
