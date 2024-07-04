package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.MainSubService;

@Repository
public interface MainSubServiceRepo extends JpaRepository<MainSubService, Long> {

	List<MainSubService> findByParametername(String sts);

}
