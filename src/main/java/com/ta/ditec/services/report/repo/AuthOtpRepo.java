package com.ta.ditec.services.report.repo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.report.domain.AuthOtp;

@Repository
public interface AuthOtpRepo extends JpaRepository<AuthOtp, Long> {

//	Long findByRequestdateBetween(Timestamp timestampstart, Timestamp timestampend);

	long countByRequestdateBetween(Timestamp startDate, Timestamp endDate);

	long countByRequestdateBetweenAndErrorcodeAndErrormsg(Timestamp startDate, Timestamp endDate, String errorCode,
			String errorMsg);

	List<AuthOtp> findByRequestdateBetween(Timestamp todaytimestampstart, Timestamp toadytimestampend);

	List<AuthOtp> findByRequestdateBetweenAndErrorcodeAndErrormsg(Timestamp todaytimestampstart,
			Timestamp toadytimestampend, String string, String string2);

	List<AuthOtp> findByAgencycode(String subAuaId);

	List<AuthOtp> findByAgencycodeAndRequestdateBetween(String subAuaId, Timestamp yeartimestampstart, Timestamp yeartimestampend);

}
