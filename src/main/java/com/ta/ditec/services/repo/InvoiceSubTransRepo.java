package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.InvoiceSubTrans;

@Repository
public interface InvoiceSubTransRepo extends JpaRepository<InvoiceSubTrans, Long> {

	List<InvoiceSubTrans> findByInvoiceId(String invoiceId);

}
