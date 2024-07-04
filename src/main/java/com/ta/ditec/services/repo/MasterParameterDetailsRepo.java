package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.MasterParameterDetails;

@Repository
public interface MasterParameterDetailsRepo extends JpaRepository<MasterParameterDetails, Long> {

	List<MasterParameterDetails> findByParameterId(String id);

}
