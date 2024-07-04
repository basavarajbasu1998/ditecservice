package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.IntegrationMasterDetilles;

@Repository
public interface IntegrationMasterDetillesRepo extends JpaRepository<IntegrationMasterDetilles, Long> {



	List<IntegrationMasterDetilles> findByIntegrationId(String integrationId);

	

}
