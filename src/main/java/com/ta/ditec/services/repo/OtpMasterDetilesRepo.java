package com.ta.ditec.services.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.OtpMasterDetiles;

@Repository
public interface OtpMasterDetilesRepo extends JpaRepository<OtpMasterDetiles, Long> {

	List<OtpMasterDetiles> findByOtp(String otp);

	Optional<OtpMasterDetiles> findByUserName(String string);

//	List<OtpMasterDetiles> findByOtpOrResendOtp(String otp);
	
	@Query("SELECT u FROM OtpMasterDetiles u WHERE u.otp = :userName OR u.resendOtp = :userName")
	List<OtpMasterDetiles> findByOtpOrResendOtp(@Param("userName") String userName);

}
