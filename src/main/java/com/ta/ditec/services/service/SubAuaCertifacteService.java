package com.ta.ditec.services.service;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import com.ta.ditec.services.model.SubAuaCertifacte;
import com.ta.ditec.services.request.CertificateRequest;
import com.ta.ditec.services.request.SubAuaCertifacteApproveRequest;

public interface SubAuaCertifacteService {

	public SubAuaCertifacte uploadFiles(MultipartFile file, String subauaId, String certificateId
//			String certificateStatus, String certificateReamrk, String certificateApprovedReamrk,
//			String certificateRejectReamrk

	) throws IOException, FileUploadException;

	List<SubAuaCertifacte> getCertificate(SubAuaCertifacteApproveRequest req);

	List<SubAuaCertifacte> getCertificatelist(CertificateRequest req);

	public String getCertificateFilePath(String certificateId);

}
