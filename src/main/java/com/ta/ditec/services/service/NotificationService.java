package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.Notification;
import com.ta.ditec.services.request.NotificationRequest;

public interface NotificationService {

	public List<Notification> getNotification(NotificationRequest req);

	public Notification getNotificationSuAua(NotificationRequest req);
}
