package com.ta.ditec.services.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.OnboardDocuments;
import com.ta.ditec.services.repo.OnboardDocumentsRepo;
import com.ta.ditec.services.service.OnboardDocumentsSevice;

@Service
public class OnboardDocumentsSeviceImpl implements OnboardDocumentsSevice {

	private static final Logger logger = LoggerFactory.getLogger(OnboardDocumentsSeviceImpl.class);
	@Autowired
	private OnboardDocumentsRepo onboardDocumentsRepo;

	@Value("${onboarding-upload-documents}")
	private String onboardingUploadDocumentsDir;

	private static final long MAX_FILE_SIZE = 300 * 1024 * 1024;

	@Override
	public OnboardDocuments uploadFiles(MultipartFile file, String documnettype, String certificateId)
			throws IOException, FileUploadException {

		try {

			String fileName = file.getOriginalFilename();
			String filePath = onboardingUploadDocumentsDir + File.separator + documnettype + File.separator + fileName;
			File f1 = new File(onboardingUploadDocumentsDir + File.separator + documnettype);

			if (!f1.isDirectory()) {
				boolean directoryCreated = f1.mkdir();
				if (directoryCreated) {
					logger.info("File created successfully");
					System.out.println("Folder is created successfully: " + f1.getAbsolutePath());
				} else {
					logger.error("Error in creating the file");
					System.out.println("Error creating folder: " + f1.getAbsolutePath());
				}
			}

			if (file.isEmpty()) {
				logger.error("One or both files are empty");
				throw new IllegalArgumentException("One or both files are empty");
			}

			if (file.getSize() > MAX_FILE_SIZE) {
				logger.error("Size of the file exceeded the limit of 300MB");
				throw new IllegalArgumentException("File size exceeds the allowed limit of 300MB");
			}
			file.transferTo(new File(filePath));
			OnboardDocuments document = new OnboardDocuments();
			document.setDocumnetpath(filePath);
			document.setDocumnettype(documnettype);
			document.setDocumentName(fileName);
			document.setDocumnetid(certificateId);
			document.setCreatedBy("super admin");
			document.setCreatedDate(new Date());
			document.setLastModifiedBy("super admin");
			document.setLastModifiedDate(new Date());
			logger.info("Data saved successfully");
			return onboardDocumentsRepo.save(document);
		} catch (TaException e) {
			logger.error("Exception occurred");
			throw e;
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	@Override
	public String getCertificateFilePath(String certificateId) {
		List<OnboardDocuments> subAuaCertifacteList = onboardDocumentsRepo.findByDocumnetid(certificateId);
		logger.info("Data fetched successfully");
		try {
			if (!subAuaCertifacteList.isEmpty()) {
				OnboardDocuments subAuaCertifacte = subAuaCertifacteList.get(0);
				return subAuaCertifacte.getDocumnetpath();
			} else {
				logger.error("Exception occurred a it is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());

			}
		} catch (TaException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());

		}

	}

}
