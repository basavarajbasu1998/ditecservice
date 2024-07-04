package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.PaymentPlanDetails;

@Repository
public interface PaymentPlanDetailsRepo extends JpaRepository<PaymentPlanDetails, Long> {

	List<PaymentPlanDetails> findByPlanType(String planType);

	
}
