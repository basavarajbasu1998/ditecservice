package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.ConfigMasterDetiles;

@Repository
public interface ConfigMasterDetilesRepo extends JpaRepository<ConfigMasterDetiles, Long> {

	List<ConfigMasterDetiles> findByKey(String key);

}
