package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.SubAuaAPI;

@Repository
public interface SubAuaAPIRepo extends JpaRepository<SubAuaAPI, Long> {

	List<SubAuaAPI> findBySubAuaId(String subAuaId);

	List<SubAuaAPI> findBySubauaApikey(String subauaApikey);

}
