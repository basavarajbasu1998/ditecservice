package com.ta.ditec.services.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ta.ditec.services.model.OnboardDocuments;
import com.ta.ditec.services.service.OnboardDocumentsSevice;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Onboard Documents", tags = "Onboard Documents", description = "Onboard Documents Downloades")
public class OnboardDocumentsController {

	private static final Logger logger = LoggerFactory.getLogger(OnboardDocumentsController.class);

	@Autowired
	private OnboardDocumentsSevice onboardDocumentsSevice;

	@PostMapping("/onboard")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
			@Valid @RequestParam("documentType") String documentType,
			@RequestParam("certificateId") String certificateId) {
		try {
			OnboardDocuments uploadFiles = onboardDocumentsSevice.uploadFiles(file, documentType, certificateId);
			logger.debug(uploadFiles.getDocumnetpath());
			return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@RequestMapping(value = "/getdocumentfile/{fileId}", method = RequestMethod.GET)
//	public void getFile(HttpServletResponse response, @Valid  @PathVariable String fileId) {
//		String filePath = onboardDocumentsSevice.getCertificateFilePath(fileId);
//		logger.debug(filePath);
//		System.out.println(filePath);
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
//	

}
