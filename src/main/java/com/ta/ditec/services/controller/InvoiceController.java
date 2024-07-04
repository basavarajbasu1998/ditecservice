package com.ta.ditec.services.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.convertfiles.InvoicePdf;
import com.ta.ditec.services.convertfiles.PDFGenerationService;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.request.SubAuaInvoiceUserRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.InvoiceResponse;
import com.ta.ditec.services.response.InvoiceServiceResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.IntegrationInvoiceDetilesService;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("/api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Invoice Detilles ", tags = "Invoice Detilles", description = "Invoice Detilles  ")
public class InvoiceController {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

	@Autowired
	private IntegrationInvoiceDetilesService integrationInvoiceDetilesService;

//	@GetMapping("/invoice")
//	public String invoice(@RequestBody InvoiceRequest req) throws IOException {
//		SUBAUAuserdetailsPDF auserdetailsPDF = new SUBAUAuserdetailsPDF();
//		auserdetailsPDF.editinvoice(req);
//		return "invoice edited";
//
//	}

	@PostMapping("/invoice")
	public ResponseEntity<TaResponse<InvoiceResponse>> getalldata(@RequestBody @Valid SubAuaUserRequest req,
			HttpServletResponse response) throws IOException {

		logger.debug(req.toString());

		System.out.println(req);
		InvoiceResponse resp = integrationInvoiceDetilesService.getInvoiceRequestAndCharges(req);

		logger.info(resp.toString());
		InvoicePdf pdf = new InvoicePdf();
		pdf.editinvoice(resp, response);
		return createResponse(resp, "success", 1000, Type.INFORMATION);

	}
//
//	@GetMapping("/alldatas")
//	public ResponseEntity<TaResponse<List<IntegrationInvoiceDetiles>>> getal() {
//		List<IntegrationInvoiceDetiles> lists = integrationInvoiceDetilesService.getAllIntegrationInvoiceDetiles();
//		TaResponse<List<IntegrationInvoiceDetiles>> res = new TaResponse<>();
//		res.setCode(1000);
//		res.setMessage("sucess");
//		res.setResponseData(lists);
//		res.setType(Type.INFORMATION);
//		return ResponseEntity.ok(res);
//	}

	@PostMapping("/eachinvoice")
	public ResponseEntity<TaResponse<InvoiceServiceResponse>> getalldatas(
			@RequestBody @Valid SubAuaInvoiceUserRequest req, HttpServletResponse response) throws IOException {
		logger.debug(req.toString());
		InvoicePdf pdf = new InvoicePdf();
		System.out.println(req);
		InvoiceServiceResponse resp = integrationInvoiceDetilesService.getEachTransactionCharges(req, response);
		pdf.eachserviceinvoice(resp, response);
		return createResponse(resp, "success", 1000, Type.INFORMATION);

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
