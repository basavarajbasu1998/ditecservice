package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.SubAuaURL;

@Repository
public interface SubAuaURLRepo extends JpaRepository<SubAuaURL, Long> {

	List<SubAuaURL> findByUrlId(String forgot);

}
