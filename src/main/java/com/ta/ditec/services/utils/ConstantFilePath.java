package com.ta.ditec.services.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConstantFilePath {

	// in your localpaths

//	public static final String UPLOAD_DIR = "D:/filesaua";
//	public static final String EMPTY_PDF = "D:/filesaua/SUBAUA.pdf";
//	public static final String DOWNLOAD_PDF = "D:/";
//
//	public static final String UPLOAD_DIR = "D:/Basavaraj";
//	public static final String EMPTY_PDF = "D:/Basavaraj/SUBAUA.pdf";
//	public static final String DOWNLOAD_PDF = "D:/";
//
//	public static final String Invoice_EMPTY_PDF = "D://invoice/invoice.pdf";
//	public static final String Invoice_DOWNLOAD_PDF = "D:/invoice/";
//	public static final String ONBOARDING_UPLOAD_DOCUMENTS = "D:/Basavaraj";

	// server path
//	public static final String UPLOAD_DIR = "/apps/uploadfilesditec";
//	public static final String EMPTY_PDF = "/apps/SUBAUA/SUBAUA.pdf";
//	public static final String DOWNLOAD_PDF = "/apps/register_form/";
//	public static final String ONBOARDING_UPLOAD_DOCUMENTS = "/apps/diteconboard";

	@Value("${upload-dir}")
	private String uploadDir;

//	@Value("${empty-pdf}")
//	private String emptyPdf;
//
//	@Value("${download-pdf}")
//	private String downloadPdf;

	@Value("${onboarding-upload-documents}")
	private String onboardingUploadDocuments;

	
	

}
