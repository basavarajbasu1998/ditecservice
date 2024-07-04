package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.MasterDataTypeDetails;

@Repository
public interface MasterDataTypeDetailRepo extends JpaRepository<MasterDataTypeDetails, Long> {

	List<MasterDataTypeDetails> findByDataTypeId(String dataTypeId);

	Page<MasterDataTypeDetails> findAllBydataTypeNameContaining(String search, Pageable paging);

}
