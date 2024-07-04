package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.InvoiceTrans;
import com.ta.ditec.services.request.InvoiceTransDetilesRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.InvoiceServiceResponse;
import com.ta.ditec.services.response.InvoiceTransResponse;

public interface InvoiceTransService {

	InvoiceTrans getInvoiceTrans(InvoiceTrans req);

	List<InvoiceTransResponse> getallInvoiceTrans(SubAuaUserRequest req);

	public List<InvoiceTrans> getInvoiceTransDetilesRequest(InvoiceTransDetilesRequest req);

	public InvoiceServiceResponse getInvoiceServiceResponse(InvoiceTransDetilesRequest req);

	public InvoiceServiceResponse getNewTry(InvoiceTransDetilesRequest req);

	public InvoiceServiceResponse getNewInvoiceId(InvoiceTransDetilesRequest req);

}
