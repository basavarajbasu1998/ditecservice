package com.ta.ditec.services.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.SubAuaCertifacteMaster;

@Repository
public interface SubAuaCertifacteMasterRepo extends JpaRepository<SubAuaCertifacteMaster, Long> {

	boolean existsByCertfacteMasterTitle(String certfacteMasterTitle);

}
