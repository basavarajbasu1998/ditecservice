package com.ta.ditec.services.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.constants.ProjectConstants;
import com.ta.ditec.services.encrypt.SHAEnc;
import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.UserMasterDetails;
import com.ta.ditec.services.password.Password;
import com.ta.ditec.services.repo.UserMasterDetailsRepository;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.request.UserMasterDetailesResetPasswordRequest;
import com.ta.ditec.services.request.UserMasterDetailsChangePasswordRequest;
import com.ta.ditec.services.request.UserMasterDetailsDeleteRequest;
import com.ta.ditec.services.request.UserMasterDetailsForgotPasswodRequest;
import com.ta.ditec.services.request.UserMasterDetailsLoginRequest;
import com.ta.ditec.services.request.UserMasterDetailsUpdateRequest;
import com.ta.ditec.services.request.UserMasterDetailsforgotPasswordCheckLinkRequest;
import com.ta.ditec.services.service.UserMasterDetailsService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class UserMasterDetailsServiceImpl implements UserMasterDetailsService {
	
	 private static final Logger logger = LoggerFactory.getLogger(UserMasterDetailsServiceImpl.class);

	@Autowired
	private UserMasterDetailsRepository userMasterDetailsRepository;

	@Autowired
	JavaMailSender primarySender;

	@Autowired
	JavaMailSender secondarySender;

	@Override
	public UserMasterDetails saveDetails(UserMasterDetails request, HttpServletRequest servletRequest) {

		MimeMessage message = primarySender.createMimeMessage();
		if (userMasterDetailsRepository.existsByuserName(request.getUserName())) {
			logger.error("User with the given username alrady exist in database");
			
			throw new TaException(ErrorCode.ALREADY_EXIST, ErrorCode.ALREADY_EXIST.getErrorMsg());
		}
		if (userMasterDetailsRepository.existsByuserEmail(request.getUserEmail())) {
			logger.error("User with the given email alrady exist in database");
			throw new TaException(ErrorCode.ALREADY_EXIST_EMAIL, ErrorCode.ALREADY_EXIST_EMAIL.getErrorMsg());
		}
		if (userMasterDetailsRepository.existsByuserMobileNumber(request.getUserMobileNumber())) {
			logger.error("User with the given mobile number alrady exist in database");
			throw new TaException(ErrorCode.ALREADY_EXIST_MOBILENUMBER,
					ErrorCode.ALREADY_EXIST_MOBILENUMBER.getErrorMsg());
		}
		UserMasterDetails umdetil = userMasterDetailsRepository.save(request);
		logger.info("Data saved successfully");
		umdetil.setUserId("UMD" + TAUtils.getId(umdetil.getId()));
		String password = Password.generatePassword();
		System.out.println(password);
		umdetil.setUserPassword(password);
		umdetil.setEmailVerifyLink(0);
		umdetil.setMoblieNumberVerifyLink(0);
		umdetil.setFirstTimeUser(0);
		umdetil.setForgotPasswordVerifyLink(0);
		umdetil.setStatus("Pending");

		System.out.println(umdetil);
		MimeMessage message1 = primarySender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message1, false, "utf-8");

			message1.setContent(getHtmlMessage(umdetil, servletRequest), "text/html");

			helper.setTo(umdetil.getUserEmail());
			helper.setSubject("Verfication Email");
			primarySender.send(message1);
			logger.info("Mail sent successfully");
		} catch (Exception e) {
			logger.error("Email not sent to the mail id given");
			throw new TaException(ErrorCode.EMAIL_NOT_SENT, ErrorCode.EMAIL_NOT_SENT.getErrorMsg());
		}

		umdetil.setUserPassword(SHAEnc.encryptData(umdetil.getUserPassword()));
		userMasterDetailsRepository.save(umdetil);
		logger.info("data saved successfully");
		

		return umdetil;

	}

	@Override
	public UserMasterDetails superAdminData(UserMasterDetails request, HttpServletRequest servletRequest) {
		try {
			if (userMasterDetailsRepository.existsByuserName(request.getUserName())) {
				logger.error("User already exists...Try with naother user");
				throw new TaException(ErrorCode.ALREADY_EXIST, ErrorCode.ALREADY_EXIST.getErrorMsg());
			}
			if (userMasterDetailsRepository.existsByuserEmail(request.getUserEmail())) {
				
				logger.error("Email Id already exits. Please try with another email id");
				throw new TaException(ErrorCode.ALREADY_EXIST_EMAIL, ErrorCode.ALREADY_EXIST_EMAIL.getErrorMsg());
			}
			if (userMasterDetailsRepository.existsByuserMobileNumber(request.getUserMobileNumber())) {
				
				logger.error("Moile already exits. Please try with another Mobile number");
				throw new TaException(ErrorCode.ALREADY_EXIST_MOBILENUMBER,
						ErrorCode.ALREADY_EXIST_MOBILENUMBER.getErrorMsg());
			}
			UserMasterDetails umdetil = userMasterDetailsRepository.save(request);
			umdetil.setUserId("UMD" + TAUtils.getId(umdetil.getId()));
			umdetil.setUserPassword(Password.generatePassword());
			umdetil.setEmailVerifyLink(0);
			umdetil.setMoblieNumberVerifyLink(0);
			umdetil.setFirstTimeUser(0);
			umdetil.setStatus("Pending");

			System.out.println(umdetil);

			umdetil.setUserPassword(SHAEnc.encryptData(umdetil.getUserPassword()));
			userMasterDetailsRepository.save(umdetil);
			logger.info("Data saved successfully");
			return umdetil;
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	private Object getHtmlMessage(UserMasterDetails umdetil, HttpServletRequest servletRequest) {

		String url = getURL(servletRequest);
		System.out.println(url);

		String strEmail = SHAEnc.encryptData(umdetil.getUserEmail());

		String baseUrl = ProjectConstants.mailLink;//"http://10.167.80.246:8082";
//		String baseUrl = "http://localhost:8082";
		String context = "/umd/activationmail?email=";

		String url1 = servletRequest.getRequestURL().toString();
		String baseURL = url.substring(0, url.length() - servletRequest.getRequestURI().length())
				+ servletRequest.getContextPath() + "/";
		System.out.println("base url" + baseURL);
		System.out.println("normal url" + url1);
		System.out.println("baseeeeee urllllllllll " + baseUrl + context + urlEncode(strEmail));

		String fullUrl = baseUrl + context + urlEncode(strEmail);
		System.out.println(fullUrl);

		String result = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">"
				+ "<head>" + "<!--[if gte mso 9]>" + "<xml>" + "  <o:OfficeDocumentSettings>" + "    <o:AllowPNG/>"
				+ "    <o:PixelsPerInch>96</o:PixelsPerInch>" + "  </o:OfficeDocumentSettings>" + "</xml>"
				+ "<![endif]-->" + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
				+ "  <meta name=\"x-apple-disable-message-reformatting\">"
				+ "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->"
				+ "  <title></title>" + "  " + "    <style type=\"text/css\">"
				+ "      @media only screen and (min-width: 620px) {" + "  .u-row {" + "    width: 600px !important;"
				+ "  }" + "  .u-row .u-col {" + "    vertical-align: top;" + "  }" + "" + "  .u-row .u-col-50 {"
				+ "    width: 300px !important;" + "  }" + "" + "  .u-row .u-col-100 {" + "    width: 600px !important;"
				+ "  }" + "" + "}" + "" + "@media (max-width: 620px) {" + "  .u-row-container {"
				+ "    max-width: 100% !important;" + "    padding-left: 0px !important;"
				+ "    padding-right: 0px !important;" + "  }" + "  .u-row .u-col {"
				+ "    min-width: 320px !important;" + "    max-width: 100% !important;"
				+ "    display: block !important;" + "  }" + "  .u-row {" + "    width: 100% !important;" + "  }"
				+ "  .u-col {" + "    width: 100% !important;" + "  }" + "  .u-col > div {" + "    margin: 0 auto;"
				+ "  }" + "}" + "body {" + "  margin: 0;" + "  padding: 0;" + "}" + "" + "table," + "tr," + "td {"
				+ "  vertical-align: top;" + "  border-collapse: collapse;" + "}" + "" + "p {" + "  margin: 0;" + "}"
				+ "" + ".ie-container table," + ".mso-container table {" + "  table-layout: fixed;" + "}" + "" + "* {"
				+ "  line-height: inherit;" + "}" + "" + "a[x-apple-data-detectors='true'] {"
				+ "  color: inherit !important;" + "  text-decoration: none !important;" + "}" + ""
				+ "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_image_1 .v-src-width { width: auto !important; } #u_content_image_1 .v-src-max-width { max-width: 25% !important; } #u_content_text_3 .v-container-padding-padding { padding: 10px 20px 20px !important; } #u_content_button_1 .v-size-width { width: 65% !important; } #u_content_text_2 .v-container-padding-padding { padding: 20px 20px 60px !important; } #u_content_heading_2 .v-container-padding-padding { padding: 30px 10px 0px !important; } #u_content_heading_2 .v-text-align { text-align: center !important; } #u_content_social_1 .v-container-padding-padding { padding: 10px 10px 10px 98px !important; } #u_content_text_5 .v-container-padding-padding { padding: 10px 20px 30px !important; } #u_content_text_5 .v-text-align { text-align: center !important; } }"
				+ "    </style>" + "  " + "  " + ""
				+ "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><link href=\"https://fonts.googleapis.com/css?family=Rubik:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->"
				+ "" + "</head>" + ""
				+ "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #000000;color: #000000\">"
				+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->"
				+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->"

				+ "" + "" + "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-1.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-1.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #ffffff;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:60px 10px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        " + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_text_3\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 100px 20px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 170%;\"><span style=\"font-size: 16px; line-height: 27.2px;\">Please Verify and login  </span><span style=\"font-size: 16px; line-height: 27.2px;\">"
				+ umdetil.getUserEmail()
				+ " and that you entered it when </span><span style=\"font-size: 16px; line-height: 27.2px;\">signing up for DITEC.</span></p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_button_1\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->"
				+ "<div class=\"v-text-align\" align=\"center\">"
				+ "  <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\""
				+ fullUrl

				+ "\" style=\"height:39px; v-text-anchor:middle; width:290px;\" arcsize=\"10.5%\"  stroke=\"f\" fillcolor=\"#000000\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:'Open Sans',sans-serif;\"><![endif]-->  "
				+ "    <a href=\"" + fullUrl
				+ "\" target=\"_blank\" class=\"v-button v-size-width\" style=\"box-sizing: border-box;display: inline-block;font-family:'Open Sans',sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #000000; border-radius: 4px;-webkit-border-radius: 4px; -moz-border-radius: 4px; width:50%; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;font-size: 14px;\">"
				+ "      <span style=\"display:block;padding:10px 20px;line-height:120%;\"><span style=\"font-size: 16px; line-height: 19.2px;\"><strong><span style=\"line-height: 19.2px; font-size: 16px;\">Verify Email</span></strong></span></span>"
				+ "    </a>" + "  <!--[if mso]></center></v:roundrect><![endif]-->" + "</div>" + "" + "      </td>"
				+ "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_text_2\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 100px 60px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"line-height: 170%;\"><span style=\"font-size: 16px; line-height: 27.2px;\">DITEC Welcomes You. </span></p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #000000;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #000000;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  " + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-3.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-3.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"300\" style=\"background-color: #f1c40f;width: 300px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 300px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #f1c40f;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table id=\"u_content_heading_2\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 10px 0px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <h1 class=\"v-text-align\" style=\"margin: 0px; line-height: 140%; text-align: left; word-wrap: break-word; font-family: 'Rubik',sans-serif; font-size: 22px; \"><div>"
				+ "<div><strong>DITEC</strong></div>" + "</div></h1>" + "" + "      </td>" + "    </tr>" + "  </tbody>"
				+ "</table>" + ""
				+ "<table id=\"u_content_social_1\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 30px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        " + "<div align=\"left\">" + "  <div style=\"display: table; max-width:-1px;\">"
				+ "  <!--[if (mso)|(IE)]><table width=\"-1\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"border-collapse:collapse;\" align=\"left\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse; mso-table-lspace: 0pt;mso-table-rspace: 0pt; width:-1px;\"><tr><![endif]-->"
				+ "  " + "    " + "    " + "    <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->"
				+ "  </div>" + "</div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"300\" style=\"background-color: #f1c40f;width: 300px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 300px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #f1c40f;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table id=\"u_content_text_5\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:31px 50px 30px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: right; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 170%;\">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidid</p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #000000;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #000000;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"color: #ffffff; line-height: 140%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\">DITEC @2023</p>" + "  </div>" + ""
				+ "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->" + "    </td>"
				+ "  </tr>" + "  </tbody>" + "  </table>" + "  <!--[if mso]></div><![endif]-->"
				+ "  <!--[if IE]></div><![endif]-->" + "</body>" + "" + "</html>" + "";
		return result;
	}

	private String urlEncode(String url) {
		try {
			String encodeURL = URLEncoder.encode(url, "UTF-8");
			return encodeURL;
		} catch (UnsupportedEncodingException e) {
			return "Issue while encoding" + e.getMessage();
		}

	}

	private String getURL(HttpServletRequest req) {

		String scheme = req.getScheme(); // http
		String serverName = req.getServerName(); // hostname.com
		int serverPort = req.getServerPort(); // 80
		String contextPath = req.getContextPath(); // /mywebapp
		String servletPath = req.getServletPath(); // /servlet/MyServlet
		String pathInfo = req.getPathInfo(); // /a/b;c=123
		String queryString = req.getQueryString(); // d=789

		// Reconstruct original requesting URL
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		if (serverPort != 80 && serverPort != 443) {
			url.append(":").append(serverPort);
		}

		url.append(contextPath).append(servletPath);

		/*
		 * if (pathInfo != null) { url.append(pathInfo); } if (queryString != null) {
		 * url.append("?").append(queryString); }
		 */
		return url.toString();
	}

	@Override
	public Page<UserMasterDetails> fetchUserMasterDetailsList(PaginationRequest request) {

		if (StringUtils.isEmpty(request.getViewType())) {
			if (StringUtils.isNotEmpty(request.getOrderBy()) && StringUtils.isNotEmpty(request.getOrder())) {

				if (!StringUtils.isAllEmpty(request.getSearchBy())) {

					Pageable paging = PageRequest.of(request.getStart(), request.getEnd());

					Page<UserMasterDetails> pagedResult = userMasterDetailsRepository
							.findAllByUserNameContainingOrUserMobileNumberContainingOrUserEmailContainingAndUserNameNot(
									request.getSearch(), request.getSearch(), request.getSearch(), paging,
									"superadmin");

					if (pagedResult != null) {
						return pagedResult;
					} else {
						throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				}
				Sort sort = request.getOrder().equalsIgnoreCase("ascending") ? Sort.by(request.getOrderBy()).ascending()
						: Sort.by(request.getOrderBy()).descending();

				Pageable paging = PageRequest.of(request.getStart(), request.getEnd(), sort);

				Page<UserMasterDetails> details = userMasterDetailsRepository.findAllByUserNameNot("superadmin",
						paging);

				return details;

			} else {
				Pageable paging = PageRequest.of(request.getStart(), request.getEnd());
				Page<UserMasterDetails> details = userMasterDetailsRepository.findAllByUserNameNot("superadmin",
						paging);
				return details;
			}

		} else {
			try {
				Pageable paging = PageRequest.of(0, Integer.MAX_VALUE);
				System.out.println("else part exceuted");
				Page<UserMasterDetails> details = userMasterDetailsRepository.findAllByUserNameNot("superadmin",
						paging);
				return details;
			} catch (Exception e) {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}

		}

	}

	@Override
	public UserMasterDetails updateUserMasterDetails(UserMasterDetailsUpdateRequest umdetil) {

		try {
			if (umdetil != null && umdetil.getUserId() != null) {

				List<UserMasterDetails> depDBDetails = userMasterDetailsRepository.findByUserId(umdetil.getUserId());

				UserMasterDetails depDB = null;
				System.out.println(depDB + "depbbbbbbbbbbbbbbbbbb");

				if (Objects.nonNull(depDBDetails) && CollectionUtils.isNotEmpty(depDBDetails)) {
					depDB = depDBDetails.get(0);
				}

				if (Objects.nonNull(umdetil.getUserName()) && StringUtils.isNotEmpty(umdetil.getUserName())) {
					depDB.setUserName(umdetil.getUserName());
				}
				if (Objects.nonNull(umdetil.getStateName()) && StringUtils.isNotEmpty(umdetil.getStateName())) {
					depDB.setStateName(umdetil.getStateName());
				}
				if (Objects.nonNull(umdetil.getLevel()) && StringUtils.isNotEmpty(umdetil.getLevel())) {
					depDB.setLevel(umdetil.getLevel());
				}
				if (Objects.nonNull(umdetil.getAuthorizedPersonName())
						&& StringUtils.isNotEmpty(umdetil.getAuthorizedPersonName())) {
					depDB.setAuthorizedPersonName(umdetil.getAuthorizedPersonName());
				}
				if (Objects.nonNull(umdetil.getAuthorizedPersonDesignation())
						&& StringUtils.isNotEmpty(umdetil.getAuthorizedPersonDesignation())) {
					depDB.setAuthorizedPersonDesignation(umdetil.getAuthorizedPersonDesignation());
				}
				if (Objects.nonNull(umdetil.getDepartmentName())
						&& StringUtils.isNotEmpty(umdetil.getDepartmentName())) {
					depDB.setDepartmentName(umdetil.getDepartmentName());
				}
				if (Objects.nonNull(umdetil.getDeptId()) && StringUtils.isNotEmpty(umdetil.getDeptId())) {
					depDB.setDeptId(umdetil.getDeptId());
				}
				if (Objects.nonNull(umdetil.getSchemeName()) && StringUtils.isNotEmpty(umdetil.getSchemeName())) {
					depDB.setSchemeName(umdetil.getSchemeName());
				}
				if (Objects.nonNull(umdetil.getDistricName()) && StringUtils.isNotEmpty(umdetil.getDistricName())) {
					depDB.setDistricName(umdetil.getDistricName());
				}
				if (Objects.nonNull(umdetil.getBlockLevels()) && StringUtils.isNotEmpty(umdetil.getBlockLevels())) {
					depDB.setBlockLevels(umdetil.getBlockLevels());
				}
				if (Objects.nonNull(umdetil.getServiceOpted()) && StringUtils.isNotEmpty(umdetil.getServiceOpted())) {
					depDB.setServiceOpted(umdetil.getServiceOpted());
				}

				if (Objects.nonNull(umdetil.getServiceStartDateAndTime())
						&& StringUtils.isNotEmpty(umdetil.getServiceStartDateAndTime())) {
					depDB.setServiceStartDateAndTime(umdetil.getServiceEndDataAndTime());
				}
				if (Objects.nonNull(umdetil.getServiceEndDataAndTime())
						&& StringUtils.isNotEmpty(umdetil.getServiceEndDataAndTime())) {
					depDB.setServiceEndDataAndTime(umdetil.getServiceEndDataAndTime());
					;
				}
				if (Objects.nonNull(umdetil.getIdNo()) && StringUtils.isNotEmpty(umdetil.getIdNo())) {
					depDB.setIdNo(umdetil.getIdNo());
				}
				if (Objects.nonNull(umdetil.getIdType()) && StringUtils.isNotEmpty(umdetil.getIdType())) {
					depDB.setIdType(umdetil.getIdType());
				}
				if (Objects.nonNull(umdetil.getAuthenticationType())
						&& StringUtils.isNotEmpty(umdetil.getAuthenticationType())) {
					depDB.setAuthenticationType(umdetil.getAuthenticationType());
				}
				if (Objects.nonNull(umdetil.getUserMobileNumber())
						&& StringUtils.isNotEmpty(umdetil.getUserMobileNumber())) {
					depDB.setUserMobileNumber(umdetil.getUserMobileNumber());
				}

				if (Objects.nonNull(umdetil.getUserEmail()) && StringUtils.isNotEmpty(umdetil.getUserEmail())) {
					depDB.setUserEmail(umdetil.getUserEmail());
				}
				if (Objects.nonNull(umdetil.getRemarks()) && StringUtils.isNotEmpty(umdetil.getRemarks())) {
					depDB.setRemarks(umdetil.getRemarks());
				}

				return userMasterDetailsRepository.save(depDB);
			} else {
				throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getErrorMsg());
			}

		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public void deleteUser(String id) {

		if (StringUtils.isNotEmpty(id)) {
			System.out.println("duuuu" + id);
			List<UserMasterDetails> d = userMasterDetailsRepository.findByUserId(id);
			if (!CollectionUtils.isEmpty(d)) {
				userMasterDetailsRepository.delete(d.get(0));
			} else {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} else {
			throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getErrorMsg());
		}
	}

	@Override

	public UserMasterDetails Login(UserMasterDetailsLoginRequest request) {
		if (StringUtils.isNotEmpty(request.getUserName()) && StringUtils.isNotEmpty(request.getUserPassword())) {
			List<UserMasterDetails> details = userMasterDetailsRepository.findByUserName(request.getUserName());
			System.out.println(details);
			if (CollectionUtils.isEmpty(details)) {
				throw new TaException(ErrorCode.USER_CRED_VALID, ErrorCode.USER_CRED_VALID.getErrorMsg());

			} else {
				System.out.println(request.getUserPassword());
				
				System.out.println(SHAEnc.decryptData(details.get(0).getUserPassword()));
				
				String strPassword = SHAEnc.encryptData(request.getUserPassword());
				
				System.out.println(strPassword);
				
				System.out.println(details.get(0).getUserPassword());
				
				if (details.get(0).getUserPassword().equals(strPassword)) {
					System.out.println("return this");
					return details.get(0);
				} else {
					throw new TaException(ErrorCode.USER_CRED_VALID, ErrorCode.USER_CRED_VALID.getErrorMsg());
				}
			}

		} else {
			throw new TaException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getErrorMsg());
		}
	}

	@Override
	public UserMasterDetails findById(UserMasterDetailsDeleteRequest request) {
		if (StringUtils.isNotEmpty(request.getUserId())) {
			List<UserMasterDetails> details = userMasterDetailsRepository.findByUserId(request.getUserId());
			if (CollectionUtils.isNotEmpty(details)) {
				return details.get(0);
			} else {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} else {
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}
	}

	@Override
	public List<UserMasterDetails> convertFiles() {
		List<UserMasterDetails> details = userMasterDetailsRepository.findAll();
		if (CollectionUtils.isNotEmpty(details)) {
			return details;
		} else {
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}
	}

	@Override
	public UserMasterDetails changePassword(UserMasterDetailesResetPasswordRequest req) {
		List<UserMasterDetails> usd = userMasterDetailsRepository.findByUserName(req.getUserName());

		if (usd != null && CollectionUtils.isNotEmpty(usd)) {
			UserMasterDetails usd1 = usd.get(0);
			System.out.println(SHAEnc.encryptData(req.getPassword()));
			System.out.println("database password" + usd1.getUserPassword());
			System.out.println("request password" + SHAEnc.encryptData(req.getPassword()));
			
			if (usd1.getUserPassword().equals(SHAEnc.encryptData(req.getPassword()))) {
				throw new TaException(ErrorCode.PASSWORD_EXIST, ErrorCode.PASSWORD_EXIST.getErrorMsg());
			}
			usd1.setUserPassword(SHAEnc.encryptData(req.getPassword()));
			usd1.setFirstTimeUser(1);
			UserMasterDetails us = userMasterDetailsRepository.save(usd1);
			return us;
		} else {
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}
	}

	public UserMasterDetails activeLink(String email, HttpServletRequest servletRequest) {
		if (StringUtils.isNoneEmpty(email)) {
			List<UserMasterDetails> details = userMasterDetailsRepository.findByuserEmail(email);
			if (details.get(0).getEmailVerifyLink() == 1) {
				throw new TaException(ErrorCode.EMAIL_ALREADY_VERIFIED, ErrorCode.EMAIL_ALREADY_VERIFIED.getErrorMsg());
			}
			if (CollectionUtils.isEmpty(details)) {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			} else {
				details.get(0).setEmailVerifyLink(1);
				userMasterDetailsRepository.save(details.get(0));
				MimeMessage message = primarySender.createMimeMessage();
				try {
					MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

					message.setContent(getHtmlMessageafrerLogin(details.get(0), servletRequest), "text/html");

					helper.setTo(details.get(0).getUserEmail());
					helper.setSubject("Verified successfully");
					primarySender.send(message);
				} catch (Exception e) {
					throw new TaException(ErrorCode.RESOURCE_ACCESS_ERROR,
							ErrorCode.RESOURCE_ACCESS_ERROR.getErrorMsg());
				}

				return null;
			}

		} else {
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	private Object getHtmlMessageafrerLogin(UserMasterDetails umdetil, HttpServletRequest servletRequest) {

		String strPassword = SHAEnc.decryptData(umdetil.getUserPassword());

		String baseUrl = ProjectConstants.mailLink;//"http://10.167.80.246:8082";
//		String baseUrl = "http://localhost:8082";

		String fullUrl = baseUrl ;//+ "/ta-ditec-web";
		System.out.println(fullUrl);

		String result = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">"
				+ "<head>" + "<!--[if gte mso 9]>" + "<xml>" + "  <o:OfficeDocumentSettings>" + "    <o:AllowPNG/>"
				+ "    <o:PixelsPerInch>96</o:PixelsPerInch>" + "  </o:OfficeDocumentSettings>" + "</xml>"
				+ "<![endif]-->" + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
				+ "  <meta name=\"x-apple-disable-message-reformatting\">"
				+ "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->"
				+ "  <title></title>" + "  " + "    <style type=\"text/css\">"
				+ "      @media only screen and (min-width: 620px) {" + "  .u-row {" + "    width: 600px !important;"
				+ "  }" + "  .u-row .u-col {" + "    vertical-align: top;" + "  }" + "" + "  .u-row .u-col-50 {"
				+ "    width: 300px !important;" + "  }" + "" + "  .u-row .u-col-100 {" + "    width: 600px !important;"
				+ "  }" + "" + "}" + "" + "@media (max-width: 620px) {" + "  .u-row-container {"
				+ "    max-width: 100% !important;" + "    padding-left: 0px !important;"
				+ "    padding-right: 0px !important;" + "  }" + "  .u-row .u-col {"
				+ "    min-width: 320px !important;" + "    max-width: 100% !important;"
				+ "    display: block !important;" + "  }" + "  .u-row {" + "    width: 100% !important;" + "  }"
				+ "  .u-col {" + "    width: 100% !important;" + "  }" + "  .u-col > div {" + "    margin: 0 auto;"
				+ "  }" + "}" + "body {" + "  margin: 0;" + "  padding: 0;" + "}" + "" + "table," + "tr," + "td {"
				+ "  vertical-align: top;" + "  border-collapse: collapse;" + "}" + "" + "p {" + "  margin: 0;" + "}"
				+ "" + ".ie-container table," + ".mso-container table {" + "  table-layout: fixed;" + "}" + "" + "* {"
				+ "  line-height: inherit;" + "}" + "" + "a[x-apple-data-detectors='true'] {"
				+ "  color: inherit !important;" + "  text-decoration: none !important;" + "}" + ""
				+ "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_image_1 .v-src-width { width: auto !important; } #u_content_image_1 .v-src-max-width { max-width: 25% !important; } #u_content_text_3 .v-container-padding-padding { padding: 10px 20px 20px !important; } #u_content_button_1 .v-size-width { width: 65% !important; } #u_content_text_2 .v-container-padding-padding { padding: 20px 20px 60px !important; } #u_content_heading_2 .v-container-padding-padding { padding: 30px 10px 0px !important; } #u_content_heading_2 .v-text-align { text-align: center !important; } #u_content_social_1 .v-container-padding-padding { padding: 10px 10px 10px 98px !important; } #u_content_text_5 .v-container-padding-padding { padding: 10px 20px 30px !important; } #u_content_text_5 .v-text-align { text-align: center !important; } }"
				+ "    </style>" + "  " + "  " + ""
				+ "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><link href=\"https://fonts.googleapis.com/css?family=Rubik:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->"
				+ "" + "</head>" + ""
				+ "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #000000;color: #000000\">"
				+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->"
				+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->"

				+ "" + "" + "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-1.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-1.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #ffffff;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:60px 10px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 170%;\"><span style=\"font-size: 20px; line-height: 34px;\"><strong><span style=\"line-height: 34px; font-size: 20px;\">UserName : "
				+ umdetil.getUserName() + "</span></strong></span></p>"
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: center; word-wrap: break-word;\">"
				+ " <p style=\"font-size: 14px; line-height: 170%;\"><span style=\"font-size: 20px; line-height: 34px;\"><strong><span style=\"line-height: 34px; font-size: 20px;\">Password : "
				+ strPassword + "</span></strong></span></p>" + "  </div>" + "" + "      </td>" + "    </tr>"
				+ "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_text_3\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 100px 20px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 170%;\"><span style=\"font-size: 16px; line-height: 27.2px;\">"
				+ umdetil.getUserEmail()
				+ " Email Verified and login Now </span><span style=\"font-size: 16px; line-height: 27.2px;\">"
				+ " using these credentials and </span><span style=\"font-size: 16px; line-height: 27.2px;\">enter into DITEC.</span></p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_button_1\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->"
				+ "<div class=\"v-text-align\" align=\"center\">"
				+ "  <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\""
				+ fullUrl
				+ "\" style=\"height:39px; v-text-anchor:middle; width:290px;\" arcsize=\"10.5%\"  stroke=\"f\" fillcolor=\"#000000\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:'Open Sans',sans-serif;\"><![endif]-->  "
				+ "    <a href=\"" + fullUrl
				+ "\" target=\"_blank\" class=\"v-button v-size-width\" style=\"box-sizing: border-box;display: inline-block;font-family:'Open Sans',sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #000000; border-radius: 4px;-webkit-border-radius: 4px; -moz-border-radius: 4px; width:50%; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;font-size: 14px;\">"
				+ "      <span style=\"display:block;padding:10px 20px;line-height:120%;\"><span style=\"font-size: 16px; line-height: 19.2px;\"><strong><span style=\"line-height: 19.2px; font-size: 16px;\">Login</span></strong></span></span>"
				+ "    </a>" + "  <!--[if mso]></center></v:roundrect><![endif]-->" + "</div>" + "" + "      </td>"
				+ "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_text_2\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 100px 60px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"line-height: 170%;\"><span style=\"font-size: 16px; line-height: 27.2px;\">DITEC Welcomes You. </span></p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #000000;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #000000;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  " + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-3.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-3.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"300\" style=\"background-color: #f1c40f;width: 300px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 300px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #f1c40f;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table id=\"u_content_heading_2\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 10px 0px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <h1 class=\"v-text-align\" style=\"margin: 0px; line-height: 140%; text-align: left; word-wrap: break-word; font-family: 'Rubik',sans-serif; font-size: 22px; \"><div>"
				+ "<div><strong>DITEC</strong></div>" + "</div></h1>" + "" + "      </td>" + "    </tr>" + "  </tbody>"
				+ "</table>" + ""
				+ "<table id=\"u_content_social_1\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 30px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        " + "<div align=\"left\">" + "  <div style=\"display: table; max-width:-1px;\">"
				+ "  <!--[if (mso)|(IE)]><table width=\"-1\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"border-collapse:collapse;\" align=\"left\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse; mso-table-lspace: 0pt;mso-table-rspace: 0pt; width:-1px;\"><tr><![endif]-->"
				+ "  " + "    " + "    " + "    <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->"
				+ "  </div>" + "</div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"300\" style=\"background-color: #f1c40f;width: 300px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 300px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #f1c40f;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table id=\"u_content_text_5\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:31px 50px 30px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: right; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 170%;\">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidid</p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #000000;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #000000;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"color: #ffffff; line-height: 140%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\">DITEC @2023</p>" + "  </div>" + ""
				+ "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->" + "    </td>"
				+ "  </tr>" + "  </tbody>" + "  </table>" + "  <!--[if mso]></div><![endif]-->"
				+ "  <!--[if IE]></div><![endif]-->" + "</body>" + "" + "</html>" + "";
		return result;
	}

	@Override
	public List<UserMasterDetails> checkData() {
		List<UserMasterDetails> details = userMasterDetailsRepository.findAll();
		if (CollectionUtils.isNotEmpty(details)) {
			return details;
		} else {
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}
	}

	@Override
	public UserMasterDetails forgotPassword(UserMasterDetailsForgotPasswodRequest request,
			HttpServletRequest servletRequest) {
		if (StringUtils.isNotEmpty(request.getUserEmail())) {
			List<UserMasterDetails> details = userMasterDetailsRepository.findByuserEmail(request.getUserEmail());
			if (CollectionUtils.isNotEmpty(details)) {
				details.get(0).setForgotPasswordVerifyLink(0);
				userMasterDetailsRepository.save(details.get(0));
				MimeMessage message = primarySender.createMimeMessage();
				try {
					MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

					message.setContent(getHtmlMessageforFogotpassword(details.get(0), servletRequest), "text/html");

					helper.setTo(details.get(0).getUserEmail());
					helper.setSubject("Reset Password");
					primarySender.send(message);
					return details.get(0);
				} catch (Exception e) {
					throw new TaException(ErrorCode.RESOURCE_ACCESS_ERROR,
							ErrorCode.RESOURCE_ACCESS_ERROR.getErrorMsg());
				}
			} else {
				throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getErrorMsg());

			}
		} else {
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}

	}

	private Object getHtmlMessageforFogotpassword(UserMasterDetails umdetil, HttpServletRequest servletRequest) {

		String url = getURL(servletRequest);
		System.out.println(url);

		String strUserId = SHAEnc.encryptData(umdetil.getUserId());

		String baseUrl = ProjectConstants.mailLink;//"http://10.167.80.246:8082";
//		String baseUrl = "http://localhost:8082";
		String context = "/umd/forgotpassword?userId=";

		String fullUrl = baseUrl + context + urlEncode(strUserId);
		System.out.println(fullUrl);

		String result = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">"
				+ "<head>" + "<!--[if gte mso 9]>" + "<xml>" + "  <o:OfficeDocumentSettings>" + "    <o:AllowPNG/>"
				+ "    <o:PixelsPerInch>96</o:PixelsPerInch>" + "  </o:OfficeDocumentSettings>" + "</xml>"
				+ "<![endif]-->" + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
				+ "  <meta name=\"x-apple-disable-message-reformatting\">"
				+ "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->"
				+ "  <title></title>" + "  " + "    <style type=\"text/css\">"
				+ "      @media only screen and (min-width: 620px) {" + "  .u-row {" + "    width: 600px !important;"
				+ "  }" + "  .u-row .u-col {" + "    vertical-align: top;" + "  }" + "" + "  .u-row .u-col-50 {"
				+ "    width: 300px !important;" + "  }" + "" + "  .u-row .u-col-100 {" + "    width: 600px !important;"
				+ "  }" + "" + "}" + "" + "@media (max-width: 620px) {" + "  .u-row-container {"
				+ "    max-width: 100% !important;" + "    padding-left: 0px !important;"
				+ "    padding-right: 0px !important;" + "  }" + "  .u-row .u-col {"
				+ "    min-width: 320px !important;" + "    max-width: 100% !important;"
				+ "    display: block !important;" + "  }" + "  .u-row {" + "    width: 100% !important;" + "  }"
				+ "  .u-col {" + "    width: 100% !important;" + "  }" + "  .u-col > div {" + "    margin: 0 auto;"
				+ "  }" + "}" + "body {" + "  margin: 0;" + "  padding: 0;" + "}" + "" + "table," + "tr," + "td {"
				+ "  vertical-align: top;" + "  border-collapse: collapse;" + "}" + "" + "p {" + "  margin: 0;" + "}"
				+ "" + ".ie-container table," + ".mso-container table {" + "  table-layout: fixed;" + "}" + "" + "* {"
				+ "  line-height: inherit;" + "}" + "" + "a[x-apple-data-detectors='true'] {"
				+ "  color: inherit !important;" + "  text-decoration: none !important;" + "}" + ""
				+ "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_image_1 .v-src-width { width: auto !important; } #u_content_image_1 .v-src-max-width { max-width: 25% !important; } #u_content_text_3 .v-container-padding-padding { padding: 10px 20px 20px !important; } #u_content_button_1 .v-size-width { width: 65% !important; } #u_content_text_2 .v-container-padding-padding { padding: 20px 20px 60px !important; } #u_content_heading_2 .v-container-padding-padding { padding: 30px 10px 0px !important; } #u_content_heading_2 .v-text-align { text-align: center !important; } #u_content_social_1 .v-container-padding-padding { padding: 10px 10px 10px 98px !important; } #u_content_text_5 .v-container-padding-padding { padding: 10px 20px 30px !important; } #u_content_text_5 .v-text-align { text-align: center !important; } }"
				+ "    </style>" + "  " + "  " + ""
				+ "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><link href=\"https://fonts.googleapis.com/css?family=Rubik:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->"
				+ "" + "</head>" + ""
				+ "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #000000;color: #000000\">"
				+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->"
				+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->"

				+ "" + "" + "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-1.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-1.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #ffffff;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:60px 10px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        " + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_text_3\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 100px 20px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 170%;\"><span style=\"font-size: 16px; line-height: 27.2px;\">Please click the below link  </span><span style=\"font-size: 16px; line-height: 27.2px;\">"
				+ " and proceed further then you can able to"
				+ " change your Password </span><span style=\"font-size: 16px; line-height: 27.2px;\"></span></p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_button_1\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->"
				+ "<div class=\"v-text-align\" align=\"center\">"
				+ "  <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\""
				+ fullUrl

				+ "\" style=\"height:39px; v-text-anchor:middle; width:290px;\" arcsize=\"10.5%\"  stroke=\"f\" fillcolor=\"#000000\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:'Open Sans',sans-serif;\"><![endif]-->  "
				+ "    <a href=\"" + fullUrl
				+ "\" target=\"_blank\" class=\"v-button v-size-width\" style=\"box-sizing: border-box;display: inline-block;font-family:'Open Sans',sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #000000; border-radius: 4px;-webkit-border-radius: 4px; -moz-border-radius: 4px; width:50%; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;font-size: 14px;\">"
				+ "      <span style=\"display:block;padding:10px 20px;line-height:120%;\"><span style=\"font-size: 16px; line-height: 19.2px;\"><strong><span style=\"line-height: 19.2px; font-size: 16px;\">Reset Password</span></strong></span></span>"
				+ "    </a>" + "  <!--[if mso]></center></v:roundrect><![endif]-->" + "</div>" + "" + "      </td>"
				+ "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "<table id=\"u_content_text_2\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 100px 60px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"line-height: 170%;\"><span style=\"font-size: 16px; line-height: 27.2px;\">Thankyou! </span></p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #000000;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #000000;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  " + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-3.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-3.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"300\" style=\"background-color: #f1c40f;width: 300px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 300px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #f1c40f;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table id=\"u_content_heading_2\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 10px 0px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <h1 class=\"v-text-align\" style=\"margin: 0px; line-height: 140%; text-align: left; word-wrap: break-word; font-family: 'Rubik',sans-serif; font-size: 22px; \"><div>"
				+ "<div><strong>DITEC</strong></div>" + "</div></h1>" + "" + "      </td>" + "    </tr>" + "  </tbody>"
				+ "</table>" + ""
				+ "<table id=\"u_content_social_1\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 30px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        " + "<div align=\"left\">" + "  <div style=\"display: table; max-width:-1px;\">"
				+ "  <!--[if (mso)|(IE)]><table width=\"-1\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"border-collapse:collapse;\" align=\"left\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse; mso-table-lspace: 0pt;mso-table-rspace: 0pt; width:-1px;\"><tr><![endif]-->"
				+ "  " + "    " + "    " + "    <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->"
				+ "  </div>" + "</div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"300\" style=\"background-color: #f1c40f;width: 300px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 300px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #f1c40f;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table id=\"u_content_text_5\" style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:31px 50px 30px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"line-height: 170%; text-align: right; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 170%;\">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidid</p>"
				+ "  </div>" + "" + "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + ""
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: transparent;\"><![endif]-->"
				+ "      "
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #000000;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"
				+ "  <div style=\"background-color: #000000;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->"
				+ "  "
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"
				+ "  <tbody>" + "    <tr>"
				+ "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:'Open Sans',sans-serif;\" align=\"left\">"
				+ "        "
				+ "  <div class=\"v-text-align\" style=\"color: #ffffff; line-height: 140%; text-align: center; word-wrap: break-word;\">"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\">DITEC @2023</p>" + "  </div>" + ""
				+ "      </td>" + "    </tr>" + "  </tbody>" + "</table>" + ""
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->" + "  </div>" + "</div>"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->" + "    </div>" + "  </div>"
				+ "</div>" + "" + "" + "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->" + "    </td>"
				+ "  </tr>" + "  </tbody>" + "  </table>" + "  <!--[if mso]></div><![endif]-->"
				+ "  <!--[if IE]></div><![endif]-->" + "</body>" + "" + "</html>" + "";
		return result;
	}

	@Override
	public UserMasterDetails forgotchangepassword(UserMasterDetailsChangePasswordRequest request) {
		System.out.println("ddddddddd" + request.getUserId());
		if (StringUtils.isNotEmpty(request.getUserId())) {
			String UserId = SHAEnc.decryptData(request.getUserId());
			List<UserMasterDetails> details = userMasterDetailsRepository.findByUserId(UserId);
			String strpassword = SHAEnc.encryptData(request.getUserPassword());
			details.get(0).setUserPassword(strpassword);
			details.get(0).setForgotPasswordVerifyLink(1);
			userMasterDetailsRepository.save(details.get(0));
			return details.get(0);
		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}

	}

	@Override
	public UserMasterDetails forgotPasswordCheckLink(UserMasterDetailsforgotPasswordCheckLinkRequest request) {
		System.out.println(request.getUserId());
		try {
			String UserId = SHAEnc.decryptData(request.getUserId());
			List<UserMasterDetails> details = userMasterDetailsRepository.findByUserId(UserId);
			logger.info("Data fetched successfully");
			return details.get(0);
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}
	
	/*
	 * public static void main(String[] args) { MimeMessage message =
	 * primarySender.createMimeMessage(); try { MimeMessageHelper helper = new
	 * MimeMessageHelper(message, false, "utf-8");
	 * 
	 * message.setContent(getHtmlMessageforFogotpassword(details.get(0),
	 * servletRequest), "text/html");
	 * 
	 * helper.setTo(details.get(0).getUserEmail());
	 * helper.setSubject("Reset Password"); primarySender.send(message); return
	 * details.get(0); } catch (Exception e) { throw new
	 * TaException(ErrorCode.RESOURCE_ACCESS_ERROR,
	 * ErrorCode.RESOURCE_ACCESS_ERROR.getErrorMsg()); } }
	 */

}
