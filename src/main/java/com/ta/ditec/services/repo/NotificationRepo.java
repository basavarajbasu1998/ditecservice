package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.Notification;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

	List<Notification> findBySubAuaIdAndId(String subAuaId, Long id);

	List<Notification> findBySubAuaIdOrderByCreatedDateDesc(String subAuaId);

}
