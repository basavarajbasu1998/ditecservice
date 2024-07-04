package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.InvoiceTrans;

@Repository
public interface InvoiceTransRepo extends JpaRepository<InvoiceTrans, Long> {

	List<InvoiceTrans> findBySubAuaId(String subAuaId);

	List<InvoiceTrans> findByInvoiceId(String invoiceId);

}
