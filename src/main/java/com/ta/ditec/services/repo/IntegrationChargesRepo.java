package com.ta.ditec.services.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.IntegrationCharges;

@Repository
public interface IntegrationChargesRepo extends JpaRepository<IntegrationCharges, Long> {

	IntegrationCharges findBySubAuaId(String subAuaId);

	
}
