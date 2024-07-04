package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.ProgressStatus;

@Repository
public interface ProgressStatusRepo extends JpaRepository<ProgressStatus, Long> {

	List<ProgressStatus> findBySubAuaId(String subauaid);

}
