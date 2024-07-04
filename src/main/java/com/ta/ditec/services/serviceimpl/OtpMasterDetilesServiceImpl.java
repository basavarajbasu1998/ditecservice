package com.ta.ditec.services.serviceimpl;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//import com.google.common.base.Optional;
import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.OtpMasterDetiles;
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.repo.OtpMasterDetilesRepo;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.request.OtpRequset;
import com.ta.ditec.services.request.OtpResendRequset;
import com.ta.ditec.services.response.LoginResponse;
import com.ta.ditec.services.service.OtpMasterDetilesService;

@Service
public class OtpMasterDetilesServiceImpl implements OtpMasterDetilesService {

	private static final Logger logger = LoggerFactory.getLogger(OnboardDocumentsSeviceImpl.class);

	@Autowired
	JavaMailSender primarySender;

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	@Autowired
	private OtpMasterDetilesRepo otpMasterDetilesRepo;

	@Override
	public LoginResponse getOtpMasterDetiles(OtpRequset req) {
		List<OtpMasterDetiles> otpfind = otpMasterDetilesRepo.findByOtpOrResendOtp(req.getOtp());

		try {
			if (CollectionUtils.isNotEmpty(otpfind)) {
				List<SubAuaUser> list = subAuaUserRepo.findByManagementEmailOrManagementMobileNumber(req.getUserName());
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				if (CollectionUtils.isNotEmpty(list)) {
					LoginResponse res = new LoginResponse();
					if (list.get(0).getStatus().equals("Pending")) {
						res.setDocumentstatus(false);
					} else {
						res.setDocumentstatus(true);
					}
					res.setFirsttimeuser(list.get(0).getFirsttimeuser());
					res.setUser("SUBAUA");
					res.setUserName(list.get(0).getSubAuaId());
					return res;
				} else {
					logger.error("Data not found in database");
					throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getErrorMsg());
				}
			}
		} catch (TaException e) {
			e.printStackTrace();
			logger.error("Exception occurrred as TaException");
			throw e;
		} catch (Exception e) {
			logger.error("Exception from server side ");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());

		}
		return null;

	}

	@Override
	public SubAuaUser user(OtpRequset req) {

		System.out.println("reqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq" + req);

		List<SubAuaUser> approvelist = subAuaUserRepo.findByManagementEmailOrManagementMobileNumber(req.getUserName());
		try {
			if (CollectionUtils.isNotEmpty(approvelist)) {
				String subAuaId = approvelist.get(0).getSubAuaId();
				Optional<OtpMasterDetiles> existingOtpOptional = otpMasterDetilesRepo.findByUserName(subAuaId);
				logger.info("data fetched from data base");
				logger.debug(existingOtpOptional.toString());
				if (existingOtpOptional.isPresent()) {
					OtpMasterDetiles existingOtp = existingOtpOptional.get();
					existingOtp.setStatus("first time");
					existingOtp.setOtp("1234");
					existingOtp.setCreatedBy("super admin");
					existingOtp.setLastModifiedBy("super admin");
					existingOtp.setCreatedDate(new Date());
					existingOtp.setLastModifiedDate(new Date());
					existingOtp.setOtpCount("0");
					otpMasterDetilesRepo.save(existingOtp);
					logger.info("Data Saved siccessfully");
					if (req.getUserName().equals(approvelist.get(0).getManagementEmail())) {
						System.out.println("inside the values");
						try {
							MimeMessage message = primarySender.createMimeMessage();
							MimeMessageHelper helper = new MimeMessageHelper(message, true,
									StandardCharsets.UTF_8.name());
							helper.setTo(req.getUserName());
							helper.setText(getHtmlMessage(existingOtp), true);
							helper.setSubject("Email Login Otp ");
							primarySender.send(message);
							logger.info("Mail sent successfully");
						} catch (Exception e) {
							logger.error("Error occurred in sending mail");
						}
					}
				} else {
					OtpMasterDetiles otp = new OtpMasterDetiles();
					otp.setUserName(subAuaId);
					otp.setOtptype(req.getOtpType());
					otp.setStatus("first time");
					otp.setOtp("1234");
					otp.setCreatedBy("super admin");
					otp.setLastModifiedBy("super admin");
					otp.setCreatedDate(new Date());
					otp.setLastModifiedDate(new Date());
					otp.setOtpCount("0");
					otpMasterDetilesRepo.save(otp);
					logger.info("Data saved successfully");
					if (req.getUserName().equals(approvelist.get(0).getManagementEmail())) {
						System.out.println("new email enter values");
						try {
							MimeMessage message = primarySender.createMimeMessage();
							MimeMessageHelper helper = new MimeMessageHelper(message, true,
									StandardCharsets.UTF_8.name());
							helper.setTo(req.getUserName());
							helper.setText(getHtmlMessage(otp), true);
							helper.setSubject("Email Login Otp ");
							primarySender.send(message);
							logger.info("Email of login OTP has been sent successfully");

						} catch (Exception e) {
							logger.error("Error in sending emal");
						}
					}

				}
				return approvelist.get(0);
			} else {
				logger.error("User not found in data base");
				throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} catch (TaException e) {
			e.printStackTrace();
			logger.error("Some unknown exception has been occurred");
			throw e;
		} catch (Exception e) {
			logger.error("Exception due to internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());

		}
	}

	@Override
	public SubAuaUser resenduser(OtpResendRequset req) {
		List<SubAuaUser> approvelist = subAuaUserRepo.findByManagementEmailOrManagementMobileNumber(req.getUserName());
		try {
			if (CollectionUtils.isNotEmpty(approvelist)) {
				String subAuaId = approvelist.get(0).getSubAuaId();
				Optional<OtpMasterDetiles> existingOtpOptional = otpMasterDetilesRepo.findByUserName(subAuaId);
				logger.info("Data fetched successfully");
				if (existingOtpOptional.isPresent()) {
					OtpMasterDetiles existingOtp = existingOtpOptional.get();
					if ("0".equals(existingOtp.getOtpCount())) {
						existingOtp.setStatus("second time");
						existingOtp.setOtp("1234");
						existingOtp.setCreatedBy("super admin");
						existingOtp.setLastModifiedBy("super admin");
						existingOtp.setCreatedDate(new Date());
						existingOtp.setLastModifiedDate(new Date());
						existingOtp.setOtpCount("1");
						existingOtp.setResendOtp("5678");
						otpMasterDetilesRepo.save(existingOtp);
						logger.info("Data saved successflly");
						try {
							MimeMessage message = primarySender.createMimeMessage();
							MimeMessageHelper helper = new MimeMessageHelper(message, true,
									StandardCharsets.UTF_8.name());
							helper.setTo(req.getUserName());
							helper.setText(getHtmlMessageResend(existingOtp), true);
							helper.setSubject("Resend Otp ");
							primarySender.send(message);
							logger.info("Mail sent successfully");
						} catch (Exception e) {
							logger.error("mail could not be sent as an exception occurred");
						}
						return approvelist.get(0);
					} else {
						logger.error("Resend OTP time expired");
						throw new TaException(ErrorCode.RESEND_OTP_EXPIRED, ErrorCode.RESEND_OTP_EXPIRED.getErrorMsg());
					}
				} else {
					logger.error("Data not found in database");
					throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			}
		} catch (TaException e) {
			logger.error("An exception has occurred");
			throw e;
		} catch (Exception e) {
			logger.error("An exception occurred as there is an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());

		}
		return null;
	}

	private String getHtmlMessage(OtpMasterDetiles otp) {
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
				+ "    vertical-align: top;\r\n" + "  }\r\n" + "\r\n" + "  .u-row .u-col-100 {\r\n"
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
				+ "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_text_1 .v-container-padding-padding { padding: 30px 30px 30px 20px !important; } #u_content_button_1 .v-container-padding-padding { padding: 10px 20px !important; } #u_content_button_1 .v-size-width { width: 100% !important; } #u_content_button_1 .v-text-align { text-align: left !important; } #u_content_button_1 .v-padding { padding: 15px 40px !important; } #u_content_text_3 .v-container-padding-padding { padding: 30px 30px 80px 20px !important; } }\r\n"
				+ "    </style>\r\n" + "  \r\n" + "  \r\n" + "\r\n" + "</head>\r\n" + "\r\n"
				+ "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #fbeeb8;color: #000000\">\r\n"
				+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\r\n"
				+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\r\n"
				+ "  <table id=\"u_body\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #fbeeb8;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "  <tbody>\r\n" + "  <tr style=\"vertical-align: top\">\r\n"
				+ "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n"
				+ "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #fbeeb8;\"><![endif]-->\r\n"
				+ "    \r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #a2c0e0\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #a2c0e0;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: transparent;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"542\" style=\"background-color: #ffffff;width: 542px;padding: 0px;border-top: 4px solid transparent;border-left: 4px solid transparent;border-right: 4px solid transparent;border-bottom: 4px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 4px solid transparent;border-left: 4px solid transparent;border-right: 4px solid transparent;border-bottom: 4px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\r\n"
				+ "  \r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 22px; line-height: 140%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 140%;\"><span data-contrast=\"auto\" class=\"TextRun SCXW227316484 BCX9\" style=\"accent-color: auto; place-content: normal; place-items: normal; place-self: auto; alignment-baseline: auto; animation: 0s ease 0s 1 normal none running none; app-region: none; appearance: none; aspect-ratio: auto; backdrop-filter: none; backface-visibility: visible; background: none 0% 0% / auto repeat scroll padding-box border-box rgba(0, 0, 0, 0); background-blend-mode: normal; baseline-shift: 0px; border-collapse: separate; border-end-end-radius: 0px; border-end-start-radius: 0px; border-start-end-radius: 0px; border-start-start-radius: 0px; border-radius: 0px; inset: auto; box-shadow: none; box-sizing: content-box; break-after: auto; break-before: auto; break-inside: auto; buffered-rendering: auto; caption-side: top; caret-color: #000000; clear: none; clip: auto; clip-path: none; clip-rule: nonzero; color: #3598db; color-interpolation: srgb; color-interpolation-filters: linearrgb; color-rendering: auto; color-scheme: normal; columns: auto; column-fill: balance; gap: normal; column-rule: 0px none #000000; column-span: none; contain: none; contain-intrinsic-block-size: none; contain-intrinsic-size: none; contain-intrinsic-inline-size: none; content: normal; content-visibility: visible; counter-increment: none; counter-reset: none; counter-set: none; cursor: text; cx: 0px; cy: 0px; d: none; direction: ltr; display: inline; dominant-baseline: auto; empty-cells: show; fill: #000000; fill-opacity: 1; fill-rule: nonzero; filter: none; flex: 0 1 auto; flex-flow: row nowrap; float: none; flood-color: #000000; flood-opacity: 1; font-feature-settings: normal; font-kerning: none; font-optical-sizing: auto; font-palette: normal; font-stretch: 100%; font-synthesis: weight style small-caps; font-variation-settings: normal; grid-area: auto / auto / auto / auto; grid: auto-flow auto / none; height: auto; hyphens: manual; image-orientation: from-image; image-rendering: auto; inline-size: auto; inset-block: auto; inset-inline: auto; isolation: auto; lighting-color: #ffffff; line-break: auto; line-height: 30.8px; list-style: outside none disc; margin: 0px; marker: none; mask: none; mask-type: luminance; max-height: none; max-width: none; min-height: 0px; min-width: 0px; mix-blend-mode: normal; object-fit: fill; object-position: 50% 50%; offset: none 0px auto 0deg; opacity: 1; order: 0; outline: #000000 none 0px; outline-offset: 0px; overflow: visible; overflow-anchor: auto; overflow-clip-margin: 0px; overflow-wrap: break-word; overscroll-behavior-block: auto; overscroll-behavior-inline: auto; overscroll-behavior: auto; padding: 0px; page: auto; paint-order: normal; perspective: none; perspective-origin: 0px 0px; pointer-events: auto; quotes: auto; r: 0px; resize: none; ruby-position: over; rx: auto; ry: auto; scroll-behavior: auto; scroll-margin-block: 0px; scroll-margin: 0px; scroll-margin-inline: 0px; scroll-padding-block: auto; scroll-padding: auto; scroll-padding-inline: auto; scroll-snap-align: none; scroll-snap-stop: normal; scroll-snap-type: none; scrollbar-gutter: auto; shape-image-threshold: 0; shape-margin: 0px; shape-outside: none; shape-rendering: auto; speak: normal; stop-color: #000000; stop-opacity: 1; stroke: none; stroke-dasharray: none; stroke-dashoffset: 0px; stroke-linecap: butt; stroke-linejoin: miter; stroke-miterlimit: 4; stroke-opacity: 1; stroke-width: 1px; tab-size: 8; table-layout: auto; text-align: center; text-align-last: auto; text-anchor: start; text-combine-upright: none; text-decoration-skip-ink: auto; text-emphasis: none #000000; text-emphasis-position: over; text-orientation: mixed; text-overflow: clip; text-rendering: auto; text-shadow: none; text-underline-offset: auto; text-underline-position: auto; touch-action: auto; transform: none; transform-box: view-box; transform-origin: 0px 0px; transform-style: flat; transition: all 0s ease 0s; unicode-bidi: normal; user-select: text; vector-effect: none; vertical-align: baseline; visibility: visible; border-block: 0px none #000000; border-inline: 0px none #000000; hyphenate-character: auto; block-size: auto; margin-block: 0px; margin-inline: 0px; max-block-size: none; max-inline-size: none; min-block-size: 0px; min-inline-size: 0px; padding-block: 0px; padding-inline: 0px; white-space: pre-wrap; width: auto; will-change: auto; word-break: normal; writing-mode: horizontal-tb; x: 0px; y: 0px; z-index: auto; zoom: 1; border: 0px none #000000;\"><span class=\"NormalTextRun SCXW227316484 BCX9\" style=\"line-height: 30.8px;\">Greetings</span></span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 14px; line-height: 200%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 200%;\"><span style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; line-height: 28px;\" class=\"TextRun SCXW233324968 BCX9\" data-contrast=\"auto\"><span style=\"margin: 0px; padding: 0px; user-select: text; line-height: 28px;\" class=\"NormalTextRun SCXW233324968 BCX9\">We are delighted to inform you that your Inquiry form has been approved and registered with DITEC for sub-AUA (Authentication User Agency).</span></span><span style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; line-height: 28px;\" data-ccp-props=\"{&quot;201341983&quot;:0,&quot;335559739&quot;:160,&quot;335559740&quot;:360}\" class=\"EOP SCXW233324968 BCX9\"> </span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 140%;\"><span data-contrast=\"auto\" class=\"TextRun SCXW89560798 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; color: #3598db; text-align: left; white-space: pre-wrap; background-color: #ffffff; line-height: 19.6px;\"><span class=\"NormalTextRun SCXW89560798 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; line-height: 19.6px;\"><span style=\"color: #236fa1; line-height: 19.6px;\">Please find below the login credentials for accessing the portal</span></span></span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 15px; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 140%;\"><strong>Login Id  : You can Login Mobile Number Or Management Email"
				+ " </strong><strong><span style=\"color: #000000; line-height: 21px;\"></span></strong></p>\r\n"
				+ "<p style=\"line-height: 140%;\"> </p>\r\n"
				+ "<p style=\"line-height: 140%;\"><strong><span style=\"color: #000000; line-height: 21px;\">Password  : "
				+ otp.getOtp() + " </span></strong></p>\r\n" + "  </div>\r\n" + "\r\n" + "      </td>\r\n"
				+ "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
				+ "<table id=\"u_content_text_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 30px 30px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 14px; color: #333333; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span data-contrast=\"auto\" class=\"TextRun SCXW225495794 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; font-size: 12pt; line-height: 22.4px; font-family: Calibri, Calibri_EmbeddedFont, Calibri_MSFontService, sans-serif;\"><span class=\"NormalTextRun SCXW225495794 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; line-height: 19.6px;\">Kindly log in to the portal using the provided credentials. Once logged in, you will be able to download the prerequisite documents and upload them as per the provided instructions. Our team will promptly review the documents and work towards completing the process as soon as possible.</span></span><span class=\"EOP SCXW225495794 BCX9\" data-ccp-props=\"{&quot;201341983&quot;:0,&quot;335559739&quot;:160,&quot;335559740&quot;:360}\" style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; font-size: 12pt; line-height: 22.4px; font-family: Calibri, Calibri_EmbeddedFont, Calibri_MSFontService, sans-serif;\"> </span></p>\r\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"> </p>\r\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"><span data-contrast=\"auto\" style=\"line-height: 19.6px;\">If you have any questions or require assistance during the process, please do not hesitate to reach out to our dedicated support team. You can also check the status of your application in the portal.</span><span data-ccp-props=\"{&quot;201341983&quot;:0,&quot;335559739&quot;:160,&quot;335559740&quot;:360}\" style=\"line-height: 19.6px;\"> </span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table id=\"u_content_button_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->\r\n"
				+ "<div class=\"v-text-align\" align=\"center\">\r\n"
				+ "  <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"https://aua.assam.gov.in/auth/login\" style=\"height:48px; v-text-anchor:middle; width:457px;\" arcsize=\"6.5%\"  strokecolor=\"#ced4d9\" strokeweight=\"3px\" fillcolor=\"#91a5e2\"><w:anchorlock/><center style=\"color:#000000;font-family:arial,helvetica,sans-serif;\"><![endif]-->  \r\n"
				+ "    <a href=\"https://aua.assam.gov.in/auth/login\" target=\"_blank\" class=\"v-button v-size-width\" style=\"box-sizing: border-box;display: inline-block;font-family:arial,helvetica,sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #000000; background-color: #91a5e2; border-radius: 3px;-webkit-border-radius: 3px; -moz-border-radius: 3px; width:100%; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;border-top-color: #ced4d9; border-top-style: solid; border-top-width: 3px; border-left-color: #ced4d9; border-left-style: solid; border-left-width: 3px; border-right-color: #ced4d9; border-right-style: solid; border-right-width: 3px; border-bottom-color: #ced4d9; border-bottom-style: solid; border-bottom-width: 3px;font-size: 14px;\">\r\n"
				+ "      <span class=\"v-padding\" style=\"display:block;padding:15px 40px;line-height:120%;\"><span style=\"font-size: 14px; line-height: 16.8px; font-family: verdana, geneva;\">Login</span></span>\r\n"
				+ "    </a>\r\n" + "  <!--[if mso]></center></v:roundrect><![endif]-->\r\n" + "</div>\r\n" + "\r\n"
				+ "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
				+ "<table id=\"u_content_text_3\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 30px 80px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 14px; color: #333333; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span data-contrast=\"auto\" class=\"TextRun SCXW191459631 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; font-size: 12pt; line-height: 22.4px; font-family: Calibri, Calibri_EmbeddedFont, Calibri_MSFontService, sans-serif;\"><span class=\"NormalTextRun SCXW191459631 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; line-height: 19.6px;\">Thank you for your cooperation, and we are excited to have you as a sub-AUA with DITEC.</span></span><span class=\"EOP SCXW191459631 BCX9\" data-ccp-props=\"{&quot;201341983&quot;:0,&quot;335559739&quot;:160,&quot;335559740&quot;:360}\" style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; font-size: 12pt; line-height: 22.4px; font-family: Calibri, Calibri_EmbeddedFont, Calibri_MSFontService, sans-serif;\"> </span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n"
				+ "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\r\n" + "    </td>\r\n" + "  </tr>\r\n"
				+ "  </tbody>\r\n" + "  </table>\r\n" + "  <!--[if mso]></div><![endif]-->\r\n"
				+ "  <!--[if IE]></div><![endif]-->\r\n" + "</body>\r\n" + "\r\n" + "</html>\r\n";
		return result;

	}

	private String getHtmlMessageResend(OtpMasterDetiles otp) {
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
				+ "    vertical-align: top;\r\n" + "  }\r\n" + "\r\n" + "  .u-row .u-col-100 {\r\n"
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
				+ "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_text_1 .v-container-padding-padding { padding: 30px 30px 30px 20px !important; } #u_content_button_1 .v-container-padding-padding { padding: 10px 20px !important; } #u_content_button_1 .v-size-width { width: 100% !important; } #u_content_button_1 .v-text-align { text-align: left !important; } #u_content_button_1 .v-padding { padding: 15px 40px !important; } #u_content_text_3 .v-container-padding-padding { padding: 30px 30px 80px 20px !important; } }\r\n"
				+ "    </style>\r\n" + "  \r\n" + "  \r\n" + "\r\n" + "</head>\r\n" + "\r\n"
				+ "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #fbeeb8;color: #000000\">\r\n"
				+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\r\n"
				+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\r\n"
				+ "  <table id=\"u_body\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #fbeeb8;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "  <tbody>\r\n" + "  <tr style=\"vertical-align: top\">\r\n"
				+ "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n"
				+ "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #fbeeb8;\"><![endif]-->\r\n"
				+ "    \r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #a2c0e0\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #a2c0e0;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: transparent;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"542\" style=\"background-color: #ffffff;width: 542px;padding: 0px;border-top: 4px solid transparent;border-left: 4px solid transparent;border-right: 4px solid transparent;border-bottom: 4px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 4px solid transparent;border-left: 4px solid transparent;border-right: 4px solid transparent;border-bottom: 4px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\r\n"
				+ "  \r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 22px; line-height: 140%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 140%;\"><span data-contrast=\"auto\" class=\"TextRun SCXW227316484 BCX9\" style=\"accent-color: auto; place-content: normal; place-items: normal; place-self: auto; alignment-baseline: auto; animation: 0s ease 0s 1 normal none running none; app-region: none; appearance: none; aspect-ratio: auto; backdrop-filter: none; backface-visibility: visible; background: none 0% 0% / auto repeat scroll padding-box border-box rgba(0, 0, 0, 0); background-blend-mode: normal; baseline-shift: 0px; border-collapse: separate; border-end-end-radius: 0px; border-end-start-radius: 0px; border-start-end-radius: 0px; border-start-start-radius: 0px; border-radius: 0px; inset: auto; box-shadow: none; box-sizing: content-box; break-after: auto; break-before: auto; break-inside: auto; buffered-rendering: auto; caption-side: top; caret-color: #000000; clear: none; clip: auto; clip-path: none; clip-rule: nonzero; color: #3598db; color-interpolation: srgb; color-interpolation-filters: linearrgb; color-rendering: auto; color-scheme: normal; columns: auto; column-fill: balance; gap: normal; column-rule: 0px none #000000; column-span: none; contain: none; contain-intrinsic-block-size: none; contain-intrinsic-size: none; contain-intrinsic-inline-size: none; content: normal; content-visibility: visible; counter-increment: none; counter-reset: none; counter-set: none; cursor: text; cx: 0px; cy: 0px; d: none; direction: ltr; display: inline; dominant-baseline: auto; empty-cells: show; fill: #000000; fill-opacity: 1; fill-rule: nonzero; filter: none; flex: 0 1 auto; flex-flow: row nowrap; float: none; flood-color: #000000; flood-opacity: 1; font-feature-settings: normal; font-kerning: none; font-optical-sizing: auto; font-palette: normal; font-stretch: 100%; font-synthesis: weight style small-caps; font-variation-settings: normal; grid-area: auto / auto / auto / auto; grid: auto-flow auto / none; height: auto; hyphens: manual; image-orientation: from-image; image-rendering: auto; inline-size: auto; inset-block: auto; inset-inline: auto; isolation: auto; lighting-color: #ffffff; line-break: auto; line-height: 30.8px; list-style: outside none disc; margin: 0px; marker: none; mask: none; mask-type: luminance; max-height: none; max-width: none; min-height: 0px; min-width: 0px; mix-blend-mode: normal; object-fit: fill; object-position: 50% 50%; offset: none 0px auto 0deg; opacity: 1; order: 0; outline: #000000 none 0px; outline-offset: 0px; overflow: visible; overflow-anchor: auto; overflow-clip-margin: 0px; overflow-wrap: break-word; overscroll-behavior-block: auto; overscroll-behavior-inline: auto; overscroll-behavior: auto; padding: 0px; page: auto; paint-order: normal; perspective: none; perspective-origin: 0px 0px; pointer-events: auto; quotes: auto; r: 0px; resize: none; ruby-position: over; rx: auto; ry: auto; scroll-behavior: auto; scroll-margin-block: 0px; scroll-margin: 0px; scroll-margin-inline: 0px; scroll-padding-block: auto; scroll-padding: auto; scroll-padding-inline: auto; scroll-snap-align: none; scroll-snap-stop: normal; scroll-snap-type: none; scrollbar-gutter: auto; shape-image-threshold: 0; shape-margin: 0px; shape-outside: none; shape-rendering: auto; speak: normal; stop-color: #000000; stop-opacity: 1; stroke: none; stroke-dasharray: none; stroke-dashoffset: 0px; stroke-linecap: butt; stroke-linejoin: miter; stroke-miterlimit: 4; stroke-opacity: 1; stroke-width: 1px; tab-size: 8; table-layout: auto; text-align: center; text-align-last: auto; text-anchor: start; text-combine-upright: none; text-decoration-skip-ink: auto; text-emphasis: none #000000; text-emphasis-position: over; text-orientation: mixed; text-overflow: clip; text-rendering: auto; text-shadow: none; text-underline-offset: auto; text-underline-position: auto; touch-action: auto; transform: none; transform-box: view-box; transform-origin: 0px 0px; transform-style: flat; transition: all 0s ease 0s; unicode-bidi: normal; user-select: text; vector-effect: none; vertical-align: baseline; visibility: visible; border-block: 0px none #000000; border-inline: 0px none #000000; hyphenate-character: auto; block-size: auto; margin-block: 0px; margin-inline: 0px; max-block-size: none; max-inline-size: none; min-block-size: 0px; min-inline-size: 0px; padding-block: 0px; padding-inline: 0px; white-space: pre-wrap; width: auto; will-change: auto; word-break: normal; writing-mode: horizontal-tb; x: 0px; y: 0px; z-index: auto; zoom: 1; border: 0px none #000000;\"><span class=\"NormalTextRun SCXW227316484 BCX9\" style=\"line-height: 30.8px;\">Greetings</span></span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 14px; line-height: 200%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 200%;\"><span style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; line-height: 28px;\" class=\"TextRun SCXW233324968 BCX9\" data-contrast=\"auto\"><span style=\"margin: 0px; padding: 0px; user-select: text; line-height: 28px;\" class=\"NormalTextRun SCXW233324968 BCX9\">We are delighted to inform you that your Inquiry form has been approved and registered with DITEC for sub-AUA (Authentication User Agency).</span></span><span style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; line-height: 28px;\" data-ccp-props=\"{&quot;201341983&quot;:0,&quot;335559739&quot;:160,&quot;335559740&quot;:360}\" class=\"EOP SCXW233324968 BCX9\"> </span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 140%;\"><span data-contrast=\"auto\" class=\"TextRun SCXW89560798 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; color: #3598db; text-align: left; white-space: pre-wrap; background-color: #ffffff; line-height: 19.6px;\"><span class=\"NormalTextRun SCXW89560798 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; line-height: 19.6px;\"><span style=\"color: #236fa1; line-height: 19.6px;\">Please find below the login credentials for accessing the portal</span></span></span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 15px; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 140%;\"><strong>Login Id  : You can Login Mobile Number Or Management Email"
				+ " </strong><strong><span style=\"color: #000000; line-height: 21px;\"></span></strong></p>\r\n"
				+ "<p style=\"line-height: 140%;\"> </p>\r\n"
				+ "<p style=\"line-height: 140%;\"><strong><span style=\"color: #000000; line-height: 21px;\">Password  : "
				+ otp.getResendOtp() + " </span></strong></p>\r\n" + "  </div>\r\n" + "\r\n" + "      </td>\r\n"
				+ "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
				+ "<table id=\"u_content_text_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 30px 30px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 14px; color: #333333; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span data-contrast=\"auto\" class=\"TextRun SCXW225495794 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; font-size: 12pt; line-height: 22.4px; font-family: Calibri, Calibri_EmbeddedFont, Calibri_MSFontService, sans-serif;\"><span class=\"NormalTextRun SCXW225495794 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; line-height: 19.6px;\">Kindly log in to the portal using the provided credentials. Once logged in, you will be able to download the prerequisite documents and upload them as per the provided instructions. Our team will promptly review the documents and work towards completing the process as soon as possible.</span></span><span class=\"EOP SCXW225495794 BCX9\" data-ccp-props=\"{&quot;201341983&quot;:0,&quot;335559739&quot;:160,&quot;335559740&quot;:360}\" style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; font-size: 12pt; line-height: 22.4px; font-family: Calibri, Calibri_EmbeddedFont, Calibri_MSFontService, sans-serif;\"> </span></p>\r\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"> </p>\r\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"><span data-contrast=\"auto\" style=\"line-height: 19.6px;\">If you have any questions or require assistance during the process, please do not hesitate to reach out to our dedicated support team. You can also check the status of your application in the portal.</span><span data-ccp-props=\"{&quot;201341983&quot;:0,&quot;335559739&quot;:160,&quot;335559740&quot;:360}\" style=\"line-height: 19.6px;\"> </span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n"
				+ "<table id=\"u_content_button_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->\r\n"
				+ "<div class=\"v-text-align\" align=\"center\">\r\n"
				+ "  <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"https://aua.assam.gov.in/auth/login\" style=\"height:48px; v-text-anchor:middle; width:457px;\" arcsize=\"6.5%\"  strokecolor=\"#ced4d9\" strokeweight=\"3px\" fillcolor=\"#91a5e2\"><w:anchorlock/><center style=\"color:#000000;font-family:arial,helvetica,sans-serif;\"><![endif]-->  \r\n"
				+ "    <a href=\"https://aua.assam.gov.in/auth/login\" target=\"_blank\" class=\"v-button v-size-width\" style=\"box-sizing: border-box;display: inline-block;font-family:arial,helvetica,sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #000000; background-color: #91a5e2; border-radius: 3px;-webkit-border-radius: 3px; -moz-border-radius: 3px; width:100%; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;border-top-color: #ced4d9; border-top-style: solid; border-top-width: 3px; border-left-color: #ced4d9; border-left-style: solid; border-left-width: 3px; border-right-color: #ced4d9; border-right-style: solid; border-right-width: 3px; border-bottom-color: #ced4d9; border-bottom-style: solid; border-bottom-width: 3px;font-size: 14px;\">\r\n"
				+ "      <span class=\"v-padding\" style=\"display:block;padding:15px 40px;line-height:120%;\"><span style=\"font-size: 14px; line-height: 16.8px; font-family: verdana, geneva;\">Login</span></span>\r\n"
				+ "    </a>\r\n" + "  <!--[if mso]></center></v:roundrect><![endif]-->\r\n" + "</div>\r\n" + "\r\n"
				+ "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
				+ "<table id=\"u_content_text_3\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 30px 80px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div class=\"v-text-align\" style=\"font-size: 14px; color: #333333; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span data-contrast=\"auto\" class=\"TextRun SCXW191459631 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; font-size: 12pt; line-height: 22.4px; font-family: Calibri, Calibri_EmbeddedFont, Calibri_MSFontService, sans-serif;\"><span class=\"NormalTextRun SCXW191459631 BCX9\" style=\"margin: 0px; padding: 0px; user-select: text; line-height: 19.6px;\">Thank you for your cooperation, and we are excited to have you as a sub-AUA with DITEC.</span></span><span class=\"EOP SCXW191459631 BCX9\" data-ccp-props=\"{&quot;201341983&quot;:0,&quot;335559739&quot;:160,&quot;335559740&quot;:360}\" style=\"margin: 0px; padding: 0px; user-select: text; color: #000000; text-align: left; white-space: pre-wrap; background-color: #ffffff; font-size: 12pt; line-height: 22.4px; font-family: Calibri, Calibri_EmbeddedFont, Calibri_MSFontService, sans-serif;\"> </span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n"
				+ "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\r\n" + "    </td>\r\n" + "  </tr>\r\n"
				+ "  </tbody>\r\n" + "  </table>\r\n" + "  <!--[if mso]></div><![endif]-->\r\n"
				+ "  <!--[if IE]></div><![endif]-->\r\n" + "</body>\r\n" + "\r\n" + "</html>\r\n";
		return result;

	}

}
