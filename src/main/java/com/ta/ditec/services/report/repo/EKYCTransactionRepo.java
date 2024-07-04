package com.ta.ditec.services.report.repo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.report.domain.EKYCTransaction;

@Repository
public interface EKYCTransactionRepo extends JpaRepository<EKYCTransaction, Long> {

	Long countByRequestdateBetween(Timestamp timestampstart, Timestamp timestampend);

	Long countByRequestdateBetweenAndOtp(Timestamp timestampstart, Timestamp timestampend, String string);

	Long countByRequestdateBetweenAndBio(Timestamp timestampstart, Timestamp timestampend, String string);

	Long countByRequestdateBetweenAndBioAndOtp(Timestamp timestampstart, Timestamp timestampend, String string,
			String string2);

	Long countByRequestdateBetweenAndErrorcode(Timestamp timestampstart, Timestamp timestampend, String string);

	Long countByRequestdateBetweenAndErrorcodeNot(Timestamp timestampstart, Timestamp timestampend, String string);

	List<EKYCTransaction> findByRequestdateBetween(Timestamp timestampstart, Timestamp timestampend);

	List<EKYCTransaction> findByRequestdateBetweenAndOtp(Timestamp todaytimestampstart, Timestamp toadytimestampend,
			String string);

	List<EKYCTransaction> findByRequestdateBetweenAndBio(Timestamp todaytimestampstart, Timestamp toadytimestampend,
			String string);

	List<EKYCTransaction> findByRequestdateBetweenAndOtpAndAgencycode(Timestamp todaytimestampstart,
			Timestamp toadytimestampend, String string, String string2);

	List<EKYCTransaction> findByRequestdateBetweenAndBioAndAgencycode(Timestamp todaytimestampstart,
			Timestamp toadytimestampend, String string, String string2);

	Long countByRequestdateBetweenAndAgencycode(Timestamp timestampstart, Timestamp timestampend, String string);

	List<EKYCTransaction> findByRequestdateBetweenAndAgencycode(Timestamp timestampstart, Timestamp timestampend,
			String string);

	List<EKYCTransaction> findByAgencycode(String subAuaId);

	List<EKYCTransaction> findFirst10ByAgencycodeOrderByRequestdateDesc(String subAuaId);

	List<EKYCTransaction> findByAgencycodeAndRequestdateBetween(String subAuaId, Timestamp yeartimestampstart, Timestamp yeartimestampend);

	List<EKYCTransaction> findFirst100ByAgencycodeOrderByRequestdateDesc(String subAuaId);

}
