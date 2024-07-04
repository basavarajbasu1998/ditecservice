package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.ServiceCharges;

@Repository
public interface ServiceChargesRepo extends JpaRepository<ServiceCharges, Long> {

	List<ServiceCharges> findBySubService(String parmeter);

}
