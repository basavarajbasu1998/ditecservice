package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.SubAuaCertifacte;

@Repository
public interface SubAuaCertifacteRepo extends JpaRepository<SubAuaCertifacte, Long> {

	List<SubAuaCertifacte> findBySubauaId(String subauaid);

	List<SubAuaCertifacte> findByCertificateId(String certificateId);

//	SubAuaCertifacte findBySubauaid(String subauaid);
}
