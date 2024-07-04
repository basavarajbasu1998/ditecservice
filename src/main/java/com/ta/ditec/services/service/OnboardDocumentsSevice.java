package com.ta.ditec.services.service;

import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import com.ta.ditec.services.model.OnboardDocuments;

public interface OnboardDocumentsSevice {

	public OnboardDocuments uploadFiles(MultipartFile file, String documnettype, String certificateId)
			throws IOException, FileUploadException;

	public String getCertificateFilePath(String certificateId);
}
