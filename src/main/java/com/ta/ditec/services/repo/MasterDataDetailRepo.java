package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.MasterDataDetails;

@Repository
public interface MasterDataDetailRepo extends JpaRepository<MasterDataDetails, Long>  {

	List<MasterDataDetails> findByDataId(String dataId);

	Page<MasterDataDetails> findAllByDataTypeContainingOrDataParentContaining(String search, String search2,
			Pageable paging);

}
