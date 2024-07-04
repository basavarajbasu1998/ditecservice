package com.ta.ditec.services.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.TAErrorCodes;



@Repository
public interface TAErrorRepo extends JpaRepository<TAErrorCodes, Integer> {

	public TAErrorCodes findByErrorcode(String errorcode);

}
