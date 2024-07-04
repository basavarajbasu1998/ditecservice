package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.PaymentMode;

@Repository
public interface PaymentModeRepo extends JpaRepository<PaymentMode, Long> {

	List<PaymentMode> findBySubAuaId(String subAuaId);

}
