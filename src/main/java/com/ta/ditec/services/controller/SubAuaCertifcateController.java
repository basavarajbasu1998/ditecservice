package com.ta.ditec.services.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.SubAuaCertifacte;
import com.ta.ditec.services.request.CertificateRequest;
import com.ta.ditec.services.request.SubAuaCertifacteApproveRequest;
import com.ta.ditec.services.response.SubAuaCertifacteApproveResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.SubAuaCertifacteService;
import com.ta.ditec.services.utils.TAConstantesResponse;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "SubAuaCertifacte ", tags = "SubAua Certifacte", description = "SubAuaCertifacte Detiles and Documnets Uploades ")
public class SubAuaCertifcateController {

	private static final Logger logger = LoggerFactory.getLogger(SubAuaCertifcateController.class);

	@Autowired
	private SubAuaCertifacteService subAuaCertifacteService;

	@PostMapping("/uploadcertifacte")
	public ResponseEntity<TaResponse<Object>> getCertifacte(HttpServletRequest req,
			@Valid @RequestParam(value = "subaua", required = false) MultipartFile file1,
			@RequestParam(value = "subkua", required = false) MultipartFile file2,
			@RequestParam(value = "subauaMOU", required = false) MultipartFile file3,
			@RequestParam(value = "subauaAggrementForm", required = false) MultipartFile file4,
			@RequestParam(value = "subauaEnquiryForm", required = false) MultipartFile file5,
			@RequestParam(value = "subauaUndertakingForm", required = false) MultipartFile file6,
			@RequestParam(value = "requestForm", required = false) MultipartFile file7,
			@RequestParam(value = "applicationForm", required = false) MultipartFile file8,
			@RequestParam(value = "subAuaId", required = true) String subauaid)
			throws FileUploadException, IOException {

		if (file1 != null) {
			System.out.println("1");
			subAuaCertifacteService.uploadFiles(file1, subauaid, subauaid + "_file1");

		}
		if (file2 != null) {
			System.out.println("2");
			subAuaCertifacteService.uploadFiles(file2, subauaid, subauaid + "_file2");
		}
		if (file3 != null) {
			System.out.println("3");
			subAuaCertifacteService.uploadFiles(file3, subauaid, subauaid + "_file3");
		}
		if (file4 != null) {
			System.out.println("4");
			subAuaCertifacteService.uploadFiles(file4, subauaid, subauaid + "_file4");
		}
		if (file5 != null) {
			System.out.println("5");
			subAuaCertifacteService.uploadFiles(file5, subauaid, subauaid + "_file5");
		}
		if (file6 != null) {
			System.out.println("6");
			subAuaCertifacteService.uploadFiles(file6, subauaid, subauaid + "_file6");
		}
		if (file7 != null) {
			System.out.println("7");
			subAuaCertifacteService.uploadFiles(file7, subauaid, subauaid + "_file7");
		}

		if (file8 != null) {
			System.out.println("8");
			subAuaCertifacteService.uploadFiles(file8, subauaid, subauaid + "_file8");
		}
		return TAConstantesResponse.createResponse(null, "success!", 1000, Type.INFORMATION);
	}

	@PostMapping("/eachcertifacetestatus")
	public ResponseEntity<TaResponse<SubAuaCertifacteApproveResponse>> getSubAuaCertifacte(
			@RequestBody @Valid SubAuaCertifacteApproveRequest req) {

		logger.debug(req.toString());

		List<SubAuaCertifacte> res = subAuaCertifacteService.getCertificate(req);
		SubAuaCertifacteApproveResponse stusres = new SubAuaCertifacteApproveResponse();
		stusres.setStatus(req.getStatus());
		return TAConstantesResponse.createResponse(stusres, "success!", 1000, Type.INFORMATION);

	}

	@PostMapping("/allcertificatestatus")
	public ResponseEntity<TaResponse<List<SubAuaCertifacte>>> getlist(@RequestBody @Valid CertificateRequest req) {

		logger.debug(req.toString());

		List<SubAuaCertifacte> resp = subAuaCertifacteService.getCertificatelist(req);
		return TAConstantesResponse.createResponse(resp, "success!", 1000, Type.INFORMATION);

	}

//	@RequestMapping(value = "/getpdffile/{fileId}", method = RequestMethod.GET)
//	public void getFile(HttpServletResponse response, @Valid @PathVariable String fileId) {
//		String filePath = subAuaCertifacteService.getCertificateFilePath(fileId);
//		System.out.println(filePath);
//
//		logger.debug(filePath);
//
//		if (filePath != null) {
//			try {
//				File file = new File(filePath);
//				if (file.exists()) {
//					response.setContentType("application/pdf");
//					response.setHeader("Content-disposition", "attachment; filename=\"" + fileId + ".pdf" + "\"");
//					FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
//				} else {
//					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			}
//		} else {
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//		}
//	}

}
