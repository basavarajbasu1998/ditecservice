package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.OnboardDocuments;

@Repository
public interface OnboardDocumentsRepo extends JpaRepository<OnboardDocuments, Long> {

	List<OnboardDocuments> findByDocumnetid(String certificateId);

}
