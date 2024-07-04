package com.ta.ditec.services.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.IntegrationInvoiceDetiles;
import com.ta.ditec.services.response.InvoiceResponse;

@Repository
public interface IntegrationInvoiceDetilesRepo extends JpaRepository<IntegrationInvoiceDetiles, Long> {

	IntegrationInvoiceDetiles findBySubAuaId(String subAuaId);

	IntegrationInvoiceDetiles save(InvoiceResponse response);

}
