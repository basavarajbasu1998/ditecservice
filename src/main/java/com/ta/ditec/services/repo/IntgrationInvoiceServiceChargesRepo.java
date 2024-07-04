package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.IntgrationInvoiceServiceCharges;

@Repository
public interface IntgrationInvoiceServiceChargesRepo extends JpaRepository<IntgrationInvoiceServiceCharges, Long> {

	List<IntgrationInvoiceServiceCharges> findBySubAuaId(String subAuaId);

}
