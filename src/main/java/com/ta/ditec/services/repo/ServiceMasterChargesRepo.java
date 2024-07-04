package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta.ditec.services.model.ServiceMasterCharges;

public interface ServiceMasterChargesRepo extends JpaRepository<ServiceMasterCharges, Long> {

	List<ServiceMasterCharges> findByServiceMasterChargesId(String serviceMasterChargesId);

	

}
