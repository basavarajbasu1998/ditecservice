package com.ta.ditec.services.report.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.report.domain.AgencyLog;





@Repository
public interface AgencyLogRepo extends JpaRepository<AgencyLog, Long> {

}
