package com.ta.ditec.services.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.convertfiles.InvoicePdf;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.InvoiceTrans;
import com.ta.ditec.services.request.InvoiceTransDetilesRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.InvoiceServiceResponse;
import com.ta.ditec.services.response.InvoiceTransResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.InvoiceTransService;

@RestController
@RequestMapping("api/v1/adminuser/")
public class InvoiceTransController {

	@Autowired
	private InvoiceTransService invoiceTransService;

	@PostMapping("/invoicetrans")
	public ResponseEntity<TaResponse<Object>> getTrns(@RequestBody InvoiceTrans req) {
		InvoiceTrans trns = invoiceTransService.getInvoiceTrans(req);
		return createResponse("", "", 1000, Type.INFORMATION);
	}

	@PostMapping("/eachinvoicetrans")
	public ResponseEntity<TaResponse<List<InvoiceTransResponse>>> getTrnsAll(@RequestBody SubAuaUserRequest req) {
		List<InvoiceTransResponse> trns = invoiceTransService.getallInvoiceTrans(req);
		return createResponse(trns, "sucesss", 1000, Type.INFORMATION);
	}

	@PostMapping("/invoicedetiles")
	public ResponseEntity<TaResponse<Object>> getInvoiceTransDetilesRequest(
			@RequestBody InvoiceTransDetilesRequest req) {
		List<InvoiceTrans> lists = invoiceTransService.getInvoiceTransDetilesRequest(req);
		return createResponse("", "sucesss", 1000, Type.INFORMATION);
	}

	@PostMapping("/invoiceprintdetiles")
	public ResponseEntity<TaResponse<InvoiceServiceResponse>> getInvoicePrintTransDetilesRequest(
			@RequestBody InvoiceTransDetilesRequest req, HttpServletResponse response) throws IOException {

		System.out.println("invoiece iddddddddddddddddddd" + req);

		InvoiceServiceResponse resp = invoiceTransService.getInvoiceServiceResponse(req);
		InvoicePdf pdf = new InvoicePdf();
		pdf.eachserviceinvoice(resp, response);
		return createResponse(resp, "sucesss", 1000, Type.INFORMATION);
	}

	@PostMapping("/newinvoice")
	public ResponseEntity<TaResponse<InvoiceServiceResponse>> getgetNewTry(@RequestBody InvoiceTransDetilesRequest req,
			HttpServletResponse response) throws IOException {
		InvoiceServiceResponse resp = invoiceTransService.getNewTry(req);
		InvoicePdf pdf = new InvoicePdf();
		pdf.eachserviceinvoice(resp, response);
		return createResponse(resp, "sucesss", 1000, Type.INFORMATION);
	}
	
	//getNewInvoiceId
	
	@PostMapping("/eachnewinvoice")
	public ResponseEntity<TaResponse<InvoiceServiceResponse>> getNewInvoiceId(@RequestBody InvoiceTransDetilesRequest req,
			HttpServletResponse response) throws IOException {
		InvoiceServiceResponse resp = invoiceTransService.getNewInvoiceId(req);
		InvoicePdf pdf = new InvoicePdf();
		pdf.eachserviceinvoice(resp, response);
		return createResponse(resp, "sucesss", 1000, Type.INFORMATION);
	}
	
	<T> ResponseEntity<TaResponse<T>> createResponse(T data, String message, int code, Type type) {
		TaResponse<T> response = new TaResponse<>();
		response.setResponseData(data);
		response.setMessage(message);
		response.setCode(code);
		response.setType(type);
		return ResponseEntity.ok(response);
	}
}
