package com.ta.ditec.services.service;

import javax.servlet.http.HttpServletResponse;

import com.ta.ditec.services.request.SubAuaInvoiceUserRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.InvoiceResponse;
import com.ta.ditec.services.response.InvoiceServiceResponse;

public interface IntegrationInvoiceDetilesService {

//	public IntegrationInvoiceDetiles getInvoiceRequest(SubAuaUserRequest req);
//
//	public List<IntegrationInvoiceDetiles> getAllIntegrationInvoiceDetiles();
//	
//	public IntgrationInvoiceServiceCharges getInvoiceRequests(SubAuaUserRequest req);

	public InvoiceResponse getInvoiceRequestAndCharges(SubAuaUserRequest req);

	public InvoiceServiceResponse getEachTransactionCharges(SubAuaInvoiceUserRequest req, HttpServletResponse response);

}
