package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.NewInvoice;

@Repository
public interface NewInvoiceRepo extends JpaRepository<NewInvoice, Long> {

	List<NewInvoice> findByInvoiceNumber(String invoiceId);

}
