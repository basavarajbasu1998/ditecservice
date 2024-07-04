package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.ApplicationStatus;

@Repository
public interface ApplicationStatusRepo extends JpaRepository<ApplicationStatus, Long> {

	List<ApplicationStatus> findAllByUserId(String subauaid);

}
