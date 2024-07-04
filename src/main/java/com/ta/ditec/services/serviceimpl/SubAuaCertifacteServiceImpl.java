package com.ta.ditec.services.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.Notification;
import com.ta.ditec.services.model.ProgressStatus;
import com.ta.ditec.services.model.SubAuaCertifacte;
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.repo.ApplicationStatusRepo;
import com.ta.ditec.services.repo.NotificationRepo;
import com.ta.ditec.services.repo.ProgressStatusRepo;
import com.ta.ditec.services.repo.SubAuaCertifacteRepo;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.request.CertificateRequest;
import com.ta.ditec.services.request.SubAuaCertifacteApproveRequest;
import com.ta.ditec.services.service.SubAuaCertifacteService;

@Service
public class SubAuaCertifacteServiceImpl implements SubAuaCertifacteService {

	private static final Logger logger = LoggerFactory.getLogger(SubAuaCertifacteServiceImpl.class);

	@Autowired
	JavaMailSender primarySender;

	@Autowired
	private SubAuaCertifacteRepo subAuaCertifacteRepo;

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	@Autowired
	private ApplicationStatusRepo asRepo;

	@Autowired
	private ProgressStatusRepo progressStatusRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	@Value("${upload-dir}")
	private String uploadDir;

	private static final long MAX_FILE_SIZE = 300 * 1024 * 1024;

	@Override
	public SubAuaCertifacte uploadFiles(MultipartFile file, String subauaid, String certificateId)
			throws IOException, FileUploadException {

		try {

			if (subauaid != null) {

				String fileName = file.getOriginalFilename();
				String filePath = uploadDir + File.separator + subauaid + File.separator + fileName;

				File f1 = new File(uploadDir + File.separator + subauaid);

				if (!f1.isDirectory()) {
					boolean directoryCreated = f1.mkdir();
					if (directoryCreated) {
						System.out.println("Folder is created successfully: " + f1.getAbsolutePath());
					} else {
						System.out.println("Error creating folder: " + f1.getAbsolutePath());
					}
				}
				if (file.isEmpty()) {
					throw new IllegalArgumentException("One or both files are empty");
				}

				if (file.getSize() > MAX_FILE_SIZE) {
					throw new IllegalArgumentException("File size exceeds the allowed limit of 300MB");
				}

				file.transferTo(new File(filePath));

				List<SubAuaCertifacte> certifactes = subAuaCertifacteRepo.findByCertificateId(certificateId);
				logger.info("Data fetched successfully");

				System.out.println("certifactes  certifactes " + certifactes);

				List<SubAuaCertifacte> findBySubauaId = subAuaCertifacteRepo.findBySubauaId(subauaid);
				logger.info("Data fetched successfully");

				findBySubauaId.get(0).setNavigateStatus(false);
				subAuaCertifacteRepo.save(findBySubauaId.get(0));

				if (CollectionUtils.isNotEmpty(certifactes)) {
					SubAuaCertifacte certifacteToUpdate = certifactes.get(0);

					if (certifacteToUpdate.getCertificateStatus().equals("Rejected")) {
						certifacteToUpdate.setSubauaId(subauaid);
						certifacteToUpdate.setCertificateId(certificateId);
						certifacteToUpdate.setCertificatePath(filePath);
						certifacteToUpdate.setCertificateStatus("Pending");
						certifacteToUpdate.setCreatedBy("superadmin");
						certifacteToUpdate.setCreatedDate(new Date());
						certifacteToUpdate.setLastModifiedBy("superadmin");
						certifacteToUpdate.setLastModifiedDate(new Date());

						SubAuaCertifacte updatedCertificate = subAuaCertifacteRepo.save(certifacteToUpdate);
						logger.info("Data saved successfully");

						return updatedCertificate;
					} else {
						certifacteToUpdate.setSubauaId(subauaid);
						certifacteToUpdate.setCertificateId(certificateId);
						certifacteToUpdate.setCertificatePath(filePath);
						certifacteToUpdate.setCertificateStatus("Pending");
						certifacteToUpdate.setCreatedBy("superadmin");
						certifacteToUpdate.setCreatedDate(new Date());
						certifacteToUpdate.setLastModifiedBy("superadmin");
						certifacteToUpdate.setLastModifiedDate(new Date());

						SubAuaCertifacte updatedCertificate = subAuaCertifacteRepo.save(certifacteToUpdate);
						logger.info("Data saved successfully");

						ProgressStatus status = new ProgressStatus();
						List<ProgressStatus> progresslist = progressStatusRepo.findBySubAuaId(subauaid);

						logger.info("Data fetched successfully");

						if (CollectionUtils.isNotEmpty(progresslist)) {
							progresslist.get(0).setApplicationDescription("document upload...");
							progresslist.get(0).setApplicationStatus(2);
							progressStatusRepo.save(progresslist.get(0));
						}
						return updatedCertificate;
					}
				} else {
					logger.error("Exception occurred as there is a bad request");
					throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
				}
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public List<SubAuaCertifacte> getCertificate(SubAuaCertifacteApproveRequest req) {

		List<SubAuaCertifacte> subAuaCertifacteList = subAuaCertifacteRepo.findByCertificateId(req.getCertificateId());

		List<SubAuaUser> lists = subAuaUserRepo.findBySubAuaId(subAuaCertifacteList.get(0).getSubauaId());
		System.out.println(lists.get(0).getManagementEmail());

		if (!subAuaCertifacteList.isEmpty()) {
			SubAuaCertifacte certificate = subAuaCertifacteList.get(0);

			if (req.getStatus().equals("Accept")) {
				certificate.setCertificateStatus("Accepted");
				Notification noti = new Notification();
				noti.setDescription("Your file has bean Accepted");
				noti.setCreatedDate(new Date());
				noti.setIsUnRead(true);
				noti.setCreatedBy("super admin");
				noti.setLastModifiedBy("super admin");
				noti.setLastModifiedDate(new Date());
				noti.setSubAuaId(certificate.getSubauaId());
				if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file1")) {
					subAuaCertifacteList.get(0).setNavigateStatus(true);
					noti.setTitle("Sub AUA Form");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file2")) {
					subAuaCertifacteList.get(0).setNavigateStatus(true);
					noti.setTitle("Sub KUA Form");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file3")) {
					subAuaCertifacteList.get(0).setNavigateStatus(true);
					noti.setTitle("SubauaMOU");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file4")) {
					subAuaCertifacteList.get(0).setNavigateStatus(true);
					noti.setTitle("SubauaAggrementForm");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file5")) {
					subAuaCertifacteList.get(0).setNavigateStatus(true);
					noti.setTitle("SubauaEnquiryForm");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file6")) {
					subAuaCertifacteList.get(0).setNavigateStatus(true);
					noti.setTitle("SubauaUndertakingForm");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file7")) {
					subAuaCertifacteList.get(0).setNavigateStatus(true);
					noti.setTitle("RequestForm");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file8")) {
					subAuaCertifacteList.get(0).setNavigateStatus(true);
					noti.setTitle("ApplicationForm");
				}
				subAuaCertifacteRepo.save(subAuaCertifacteList.get(0));
				notificationRepo.save(noti);

			} else if (req.getStatus().equals("Reject")) {
				certificate.setCertificateStatus("Rejected");
				certificate.setCertificatRemarks(req.getRemarks());
				String managementEmail = lists.get(0).getManagementEmail();
				sendEmail(managementEmail, req.getRemarks());

				Notification noti = new Notification();
				noti.setDescription("Your file has bean Rejected");
				noti.setCreatedDate(new Date());
				noti.setIsUnRead(true);
				noti.setCreatedBy("super admin");
				noti.setLastModifiedBy("super admin");
				noti.setLastModifiedDate(new Date());
				noti.setSubAuaId(certificate.getSubauaId());
				if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file1")) {
					subAuaCertifacteList.get(0).setNavigateStatus(false);
					noti.setTitle("Sub AUA Form");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file2")) {
					subAuaCertifacteList.get(0).setNavigateStatus(false);
					noti.setTitle("Sub KUA Form");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file3")) {
					subAuaCertifacteList.get(0).setNavigateStatus(false);
					noti.setTitle("Subaua MOU");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file4")) {
					subAuaCertifacteList.get(0).setNavigateStatus(false);
					noti.setTitle("Subaua AggrementForm");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file5")) {
					subAuaCertifacteList.get(0).setNavigateStatus(false);
					noti.setTitle("Subaua EnquiryForm");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file6")) {
					subAuaCertifacteList.get(0).setNavigateStatus(false);
					noti.setTitle("Subaua Undertaking Form");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file7")) {
					subAuaCertifacteList.get(0).setNavigateStatus(false);
					noti.setTitle("RequestForm");
				} else if (certificate.getCertificateId().equals(certificate.getSubauaId() + "_file8")) {
					subAuaCertifacteList.get(0).setNavigateStatus(false);
					noti.setTitle("ApplicationForm");
				}
				subAuaCertifacteRepo.save(subAuaCertifacteList.get(0));
				notificationRepo.save(noti);
				logger.info("Data saved successfully");
			}
			subAuaCertifacteRepo.save(certificate);
			logger.info("Data saved successfully");

		} else {
			logger.error("Data not found in the database");
			throw new TaException(ErrorCode.NOT_FOUND, "Certificate not found"); // Handle not found case
		}

		return subAuaCertifacteList;
	}

	private void sendEmail(String managementEmail2, String managementEmail) {
		System.out.println("managementEmail" + managementEmail);
		try {
			MimeMessage message = primarySender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			helper.setTo(managementEmail2);
			helper.setText(getHtmlMessage(managementEmail), true);
			helper.setSubject("DITEC Document Status");
			primarySender.send(message);
		} catch (Exception e) {

		}

	}

	private String getHtmlMessage(String managementEmail) {
		String result = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
				+ "<head>\r\n" + "<!--[if gte mso 9]>\r\n" + "<xml>\r\n" + "  <o:OfficeDocumentSettings>\r\n"
				+ "    <o:AllowPNG/>\r\n" + "    <o:PixelsPerInch>96</o:PixelsPerInch>\r\n"
				+ "  </o:OfficeDocumentSettings>\r\n" + "</xml>\r\n" + "<![endif]-->\r\n"
				+ "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "  <meta name=\"x-apple-disable-message-reformatting\">\r\n"
				+ "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\r\n"
				+ "  <title></title>\r\n" + "  \r\n" + "    <style type=\"text/css\">\r\n"
				+ "      @media only screen and (min-width: 570px) {\r\n" + "  .u-row {\r\n"
				+ "    width: 550px !important;\r\n" + "  }\r\n" + "  .u-row .u-col {\r\n"
				+ "    vertical-align: top;\r\n" + "  }\r\n" + "\r\n" + "  .u-row .u-col-50 {\r\n"
				+ "    width: 275px !important;\r\n" + "  }\r\n" + "\r\n" + "  .u-row .u-col-100 {\r\n"
				+ "    width: 550px !important;\r\n" + "  }\r\n" + "\r\n" + "}\r\n" + "\r\n"
				+ "@media (max-width: 570px) {\r\n" + "  .u-row-container {\r\n" + "    max-width: 100% !important;\r\n"
				+ "    padding-left: 0px !important;\r\n" + "    padding-right: 0px !important;\r\n" + "  }\r\n"
				+ "  .u-row .u-col {\r\n" + "    min-width: 320px !important;\r\n"
				+ "    max-width: 100% !important;\r\n" + "    display: block !important;\r\n" + "  }\r\n"
				+ "  .u-row {\r\n" + "    width: 100% !important;\r\n" + "  }\r\n" + "  .u-col {\r\n"
				+ "    width: 100% !important;\r\n" + "  }\r\n" + "  .u-col > div {\r\n" + "    margin: 0 auto;\r\n"
				+ "  }\r\n" + "}\r\n" + "body {\r\n" + "  margin: 0;\r\n" + "  padding: 0;\r\n" + "}\r\n" + "\r\n"
				+ "table,\r\n" + "tr,\r\n" + "td {\r\n" + "  vertical-align: top;\r\n"
				+ "  border-collapse: collapse;\r\n" + "}\r\n" + "\r\n" + "p {\r\n" + "  margin: 0;\r\n" + "}\r\n"
				+ "\r\n" + ".ie-container table,\r\n" + ".mso-container table {\r\n" + "  table-layout: fixed;\r\n"
				+ "}\r\n" + "\r\n" + "* {\r\n" + "  line-height: inherit;\r\n" + "}\r\n" + "\r\n"
				+ "a[x-apple-data-detectors='true'] {\r\n" + "  color: inherit !important;\r\n"
				+ "  text-decoration: none !important;\r\n" + "}\r\n" + "\r\n"
				+ "table, td { color: #000000; } @media (max-width: 480px) { #u_content_text_1 .v-container-padding-padding { padding: 30px 30px 30px 20px !important; } }\r\n"
				+ "    </style>\r\n" + "  \r\n" + "  \r\n" + "\r\n" + "</head>\r\n" + "\r\n"
				+ "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #8ecad8;color: #000000\">\r\n"
				+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\r\n"
				+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\r\n"
				+ "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #8ecad8;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "  <tbody>\r\n" + "  <tr style=\"vertical-align: top\">\r\n"
				+ "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n"
				+ "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #8ecad8;\"><![endif]-->\r\n"
				+ "    \r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"550\" style=\"width: 550px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"height: 100%;width: 100% !important;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
				+ "  \r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: transparent;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"550\" style=\"width: 550px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\r\n"
				+ "  \r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 0px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
				+ "    <tbody>\r\n" + "      <tr style=\"vertical-align: top\">\r\n"
				+ "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
				+ "          <span>&#160;</span>\r\n" + "        </td>\r\n" + "      </tr>\r\n" + "    </tbody>\r\n"
				+ "  </table>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"542\" style=\"width: 542px;padding: 0px;border-top: 4px solid #d9d8d8;border-left: 4px solid #d9d8d8;border-right: 4px solid #d9d8d8;border-bottom: 4px solid #d9d8d8;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 4px solid #d9d8d8;border-left: 4px solid #d9d8d8;border-right: 4px solid #d9d8d8;border-bottom: 4px solid #d9d8d8;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\r\n"
				+ "  \r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div style=\"font-size: 22px; line-height: 140%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 140%;\"><span style=\"line-height: 30.8px;\"><strong><span style=\"color: #236fa1;\">DITEC</span> </strong></span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table id=\"u_content_text_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 30px 30px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div style=\"font-size: 14px; color: #333333; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"color: #374151; font-family: 'Segoe UI', sans-serif; font-size: 14.6667px; text-align: left; white-space: normal; background-color: #f7f7f8; float: none; display: inline; line-height: 19.6px;\">The documents were rejected due to missing  </span><strong style=\"line-height: inherit; color: #374151; font-family: 'Segoe UI', sans-serif; font-size: 14.6667px; text-align: left; white-space: normal;\"><span style=\"line-height: 19.6px; background-color: yellow;\">"
				+ managementEmail + "</span></strong></p>\r\n"
				+ "<span style=\"color: #374151; font-family: 'Segoe UI', sans-serif; font-size: 14.6667px; text-align: left; white-space: normal; background-color: #f7f7f8; float: none; display: inline; line-height: 19.6px;\">Kindly upload the corrected documents as per the specified requirements for further processing  </span><strong style=\"line-height: inherit; color: #374151; font-family: 'Segoe UI', sans-serif; font-size: 14.6667px; text-align: left; white-space: normal;\">"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"> </p>\r\n" + "  </div>\r\n" + "\r\n"
				+ "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: transparent;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"550\" style=\"width: 550px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\r\n"
				+ "  \r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px 0px 11px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px 0px 11px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: transparent;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"275\" style=\"width: 275px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 275px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\r\n"
				+ "  \r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"275\" style=\"width: 275px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 275px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\r\n"
				+ "  \r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n"
				+ "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\r\n" + "    </td>\r\n" + "  </tr>\r\n"
				+ "  </tbody>\r\n" + "  </table>\r\n" + "  <!--[if mso]></div><![endif]-->\r\n"
				+ "  <!--[if IE]></div><![endif]-->\r\n" + "</body>\r\n" + "\r\n" + "</html>\r\n";
		return result;

	}

	@Override
	public List<SubAuaCertifacte> getCertificatelist(CertificateRequest req) {
		List<SubAuaCertifacte> list = subAuaCertifacteRepo.findBySubauaId(req.getSubAuaId());
		if (CollectionUtils.isNotEmpty(list)) {
			return list;
		} else {
			List<SubAuaCertifacte> lists = subAuaCertifacteRepo.findBySubauaId(req.getSubAuaId());
			if (CollectionUtils.isNotEmpty(lists)) {
				SubAuaCertifacte sts = lists.get(0);
				sts.setCertificateStatus("File is Not Upload");
				subAuaCertifacteRepo.save(lists.get(0));
				return lists;
			} else {
				return null;
			}
		}

	}

	@Override
	public String getCertificateFilePath(String certificateId) {
		List<SubAuaCertifacte> subAuaCertifacteList = subAuaCertifacteRepo.findByCertificateId(certificateId);
		if (!subAuaCertifacteList.isEmpty()) {
			SubAuaCertifacte subAuaCertifacte = subAuaCertifacteList.get(0);
			return subAuaCertifacte.getCertificatePath();
		}
		return "Certificate not found";

	}

}
