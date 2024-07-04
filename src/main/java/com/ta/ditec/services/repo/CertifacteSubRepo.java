package com.ta.ditec.services.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.CertifacteSub;

@Repository
public interface CertifacteSubRepo extends JpaRepository<CertifacteSub, Long> {

	
	boolean existsByCertificateTitle(String certificateTitle);
	boolean existsByParentId(String parentId);

	

}
