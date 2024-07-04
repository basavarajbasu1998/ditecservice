package com.ta.ditec.services.serviceimpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.encrypt.SHAEnc;
import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.ApplicationStatus;
import com.ta.ditec.services.model.DitecUserRoles;
import com.ta.ditec.services.model.IntegrationCharges;
import com.ta.ditec.services.model.MainSubService;
import com.ta.ditec.services.model.PaymentMode;
import com.ta.ditec.services.model.ProgressStatus;
import com.ta.ditec.services.model.SubAuaAPI;
import com.ta.ditec.services.model.SubAuaCertifacte;
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.password.Password;
import com.ta.ditec.services.repo.ApplicationStatusRepo;
import com.ta.ditec.services.repo.DitecUserRolesRepo;
import com.ta.ditec.services.repo.IntegrationChargesRepo;
import com.ta.ditec.services.repo.MainSubServiceRepo;
import com.ta.ditec.services.repo.PaymentModeRepo;
import com.ta.ditec.services.repo.ProgressStatusRepo;
import com.ta.ditec.services.repo.SubAuaAPIRepo;
import com.ta.ditec.services.repo.SubAuaCertifacteRepo;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.request.SubAuaApproveRequest;
import com.ta.ditec.services.request.SubAuaChangePasswordRequest;
import com.ta.ditec.services.request.SubAuaLoginRequest;
import com.ta.ditec.services.request.SubAuaUpdateRequest;
import com.ta.ditec.services.request.SubAuaUserForgotPasswodRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.request.SubAuaUserResetPasswordRequest;
import com.ta.ditec.services.request.SubAuaforgotPasswordCheckLinkRequest;
import com.ta.ditec.services.request.TransactionModelRequest;
import com.ta.ditec.services.response.LoginResponse;
import com.ta.ditec.services.response.SubAuaUserResponse;
import com.ta.ditec.services.response.TransactionModelResponse;
import com.ta.ditec.services.securityconfig.JWTUtils;
import com.ta.ditec.services.service.SubAuaUserService;
import com.ta.ditec.services.utils.AsyncEmailSender;
import com.ta.ditec.services.utils.DitecMailConstant;
import com.ta.ditec.services.utils.TAConstantes;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class SubAuaUserServiceimpl implements SubAuaUserService {

	private static final Logger logger = LoggerFactory.getLogger(SubAuaUserServiceimpl.class);

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	@Autowired
	private SubAuaAPIRepo subAuaAPIRepo;

	@Autowired
	private ApplicationStatusRepo asRepo;

	@Autowired
	private SubAuaCertifacteRepo auaCertifacteRepo;

	@Autowired
	private ProgressStatusRepo progressStatusRepo;

	@Autowired
	private PaymentModeRepo paymentModeRepo;

	@Autowired
	private IntegrationChargesRepo integrationChargesRepo;

	@Autowired
	JavaMailSender primarySender;

	@Autowired
	private SubAuaCertifacteRepo subAuaCertifacteRepo;

	@Autowired
	private MainSubServiceRepo mainSubServiceRepo;

	@Autowired
	private AsyncEmailSender asyncEmailSender;

	@Autowired
	private JWTUtils jwtUtils;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DitecUserRolesRepo ditecUserRolesRepo;

	@Override
	public SubAuaUser savesubaua(SubAuaUser omdetil, HttpServletRequest servletRequest)
			throws IOException, MessagingException {

		try {

			if (omdetil != null) {

				logger.info("sub aua register request");

				List<SubAuaUser> existingUsersByEmail = subAuaUserRepo
						.findByManagementEmail(omdetil.getManagementEmail());
				logger.info("Data fetched successfully");
				List<SubAuaUser> existingUsersByMobile = subAuaUserRepo
						.findByManagementMobilenumber(omdetil.getManagementMobilenumber());
				logger.info("Data fetched successfully");
				if (!existingUsersByEmail.isEmpty()) {
					throw new TaException(ErrorCode.ALREADY_EXIST_EMAIL, ErrorCode.ALREADY_EXIST_EMAIL.getErrorMsg());
				} else if (!existingUsersByMobile.isEmpty()) {
					throw new TaException(ErrorCode.ALREADY_EXIST_MOBILENUMBER,
							ErrorCode.ALREADY_EXIST_MOBILENUMBER.getErrorMsg());
				}

				SimpleMailMessage mailMessage = new SimpleMailMessage();
				if (mailMessage != null) {
					String subauapsw = Password.generatePassword();
					omdetil.setPassword(subauapsw);
					omdetil.setFirsttimeuser(0);
					omdetil.setForgotpassword(0);
					omdetil.setPassword(SHAEnc.encryptData(subauapsw));
					omdetil.setAccountStatus("Active");
					omdetil.setRole("SUBAUA");
					omdetil = subAuaUserRepo.save(omdetil);
					logger.info("Data saved successfully");
					omdetil.setStatus("Pending");
					omdetil.setNavigateStatus(false);
					omdetil.setApplicationstatus("Form Approval Request Pending.");
					if (omdetil.getBusinessApplicationName() != null)
						omdetil.setSubAuaId(omdetil.getBusinessApplicationName().toUpperCase().substring(0, 3) + "SA"
								+ TAUtils.dateformat("MMYY") + "" + TAUtils.getId(omdetil.getId()));

					System.out.println("idddddddddddddddddddddddddddd" + omdetil.getSubAuaId());

					subAuaUserRepo.saveAndFlush(omdetil);

					SubAuaCertifacte certifacte1 = new SubAuaCertifacte();
					SubAuaCertifacte certifacte2 = new SubAuaCertifacte();
					SubAuaCertifacte certifacte3 = new SubAuaCertifacte();
					SubAuaCertifacte certifacte4 = new SubAuaCertifacte();
					SubAuaCertifacte certifacte5 = new SubAuaCertifacte();
					SubAuaCertifacte certifacte6 = new SubAuaCertifacte();
					SubAuaCertifacte certifacte7 = new SubAuaCertifacte();
					SubAuaCertifacte certifacte8 = new SubAuaCertifacte();

					certifacte1.setCertificateStatus("File not upload");
					certifacte2.setCertificateStatus("File not upload");
					certifacte3.setCertificateStatus("File not upload");
					certifacte4.setCertificateStatus("File not upload");
					certifacte5.setCertificateStatus("File not upload");
					certifacte6.setCertificateStatus("File not upload");
					certifacte7.setCertificateStatus("File not upload");
					certifacte8.setCertificateStatus("File not upload");

					certifacte1.setSubauaId(omdetil.getSubAuaId());
					certifacte2.setSubauaId(omdetil.getSubAuaId());
					certifacte3.setSubauaId(omdetil.getSubAuaId());
					certifacte4.setSubauaId(omdetil.getSubAuaId());
					certifacte5.setSubauaId(omdetil.getSubAuaId());
					certifacte6.setSubauaId(omdetil.getSubAuaId());
					certifacte7.setSubauaId(omdetil.getSubAuaId());
					certifacte8.setSubauaId(omdetil.getSubAuaId());

					certifacte1.setCertificateId(omdetil.getSubAuaId() + "_file1");
					certifacte2.setCertificateId(omdetil.getSubAuaId() + "_file2");
					certifacte3.setCertificateId(omdetil.getSubAuaId() + "_file3");
					certifacte4.setCertificateId(omdetil.getSubAuaId() + "_file4");
					certifacte5.setCertificateId(omdetil.getSubAuaId() + "_file5");
					certifacte6.setCertificateId(omdetil.getSubAuaId() + "_file6");
					certifacte7.setCertificateId(omdetil.getSubAuaId() + "_file7");
					certifacte8.setCertificateId(omdetil.getSubAuaId() + "_file8");

					System.out.println("cerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + certifacte1);
					auaCertifacteRepo.save(certifacte1);
					auaCertifacteRepo.save(certifacte2);
					auaCertifacteRepo.save(certifacte3);
					auaCertifacteRepo.save(certifacte4);
					auaCertifacteRepo.save(certifacte5);
					auaCertifacteRepo.save(certifacte6);
					auaCertifacteRepo.save(certifacte7);
					auaCertifacteRepo.save(certifacte8);

					logger.info("sub aua register data saved");

					saveApplicationStatus("Enquiry Form Request Submit", omdetil.getSubAuaId(),
							"Admin Verify Enquiry Form.");
					saveProgressbarStatus("1", "Registered successfully next upload document ", omdetil.getSubAuaId());

					asyncEmailSender.sendEmailAsync(omdetil.getManagementEmail(),
							DitecMailConstant.getHtmlMessage(omdetil, servletRequest), TAConstantes.LOGIN);

					asyncEmailSender.sendEmailAsync(TAConstantes.DITECEMail,
							DitecMailConstant.getHtmlMessageadmin(omdetil, servletRequest), TAConstantes.LOGIN);

				}
				return omdetil;
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	private void saveApplicationStatus(String status, String userId, String nextStatus) {
		ApplicationStatus astatus = new ApplicationStatus();
		astatus.setApplicationStatus(status);
		astatus.setCreatedDate(new Date());
		astatus.setCreatedBy(userId);
		astatus.setNextStatus(nextStatus);
		astatus.setUserId(userId);
		asRepo.save(astatus);
		logger.info("Data saved successfully");

	}

	private void saveProgressbarStatus(String applicationStatus, String applicationDescription, String subAuaId) {
		ProgressStatus status = new ProgressStatus();
		status.setApplicationStatus(1);
		status.setSubAuaId(subAuaId);
		progressStatusRepo.save(status);
		logger.info("Data saved successfully");
	}

	@Override
	public SubAuaUser getsubauaupdateuser(SubAuaUpdateRequest req) {

		logger.info("subaua update request ");

		try {
			if (req != null && req.getSubAuaId() != null) {
				List<SubAuaUser> depDBDetails = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
				SubAuaUser depDB = null;
				if (Objects.nonNull(depDBDetails) && CollectionUtils.isNotEmpty(depDBDetails)) {
					depDB = depDBDetails.get(0);
				}

				if (Objects.nonNull(req.getAuaDemographic())) {
					depDB.setAuaDemographic(req.getAuaDemographic());
				}

				if (Objects.nonNull(req.getAddress()) && StringUtils.isNotEmpty(req.getAddress())) {
					depDB.setAddress(req.getAddress());
				}

				if (Objects.nonNull(req.getAuaDemographicCategory())) {
					depDB.setAuaDemographicCategory(req.getAuaDemographicCategory());
				}

				if (Objects.nonNull(req.getAuaFingerprint())) {
					depDB.setAuaFingerprint(req.getAuaFingerprint());
				}

				if (Objects.nonNull(req.getAuaIris())) {
					depDB.setAuaIris(req.getAuaIris());
				}

				if (Objects.nonNull(req.getAuaOtp())) {
					depDB.setAuaOtp(req.getAuaOtp());
				}

				if (Objects.nonNull(req.getCity()) && StringUtils.isNotEmpty(req.getCity())) {
					depDB.setCity(req.getCity());
				}

				if (Objects.nonNull(req.getDistrict()) && StringUtils.isNotEmpty(req.getDistrict())) {
					depDB.setDistrict(req.getDistrict());
				}

				if (Objects.nonNull(req.getKuaFingerprint())) {
					depDB.setKuaFingerprint(req.getKuaFingerprint());
				}

				if (Objects.nonNull(req.getKuaIris())) {
					depDB.setKuaIris(req.getKuaIris());
				}

				if (Objects.nonNull(req.getKuaOtp())) {
					depDB.setKuaOtp(req.getKuaOtp());
				}

				if (Objects.nonNull(req.getManagementDesignationName())
						&& StringUtils.isNotEmpty(req.getManagementDesignationName())) {
					depDB.setManagementDesignationName(req.getManagementDesignationName());
				}

				if (Objects.nonNull(req.getManagementEmail()) && StringUtils.isNotEmpty(req.getManagementEmail())) {
					depDB.setManagementEmail(req.getManagementEmail());
				}

				if (Objects.nonNull(req.getManagementMobilenumber())
						&& StringUtils.isNotEmpty(req.getManagementMobilenumber())) {
					depDB.setManagementMobilenumber(req.getManagementMobilenumber());
				}

				if (Objects.nonNull(req.getManagementName()) && StringUtils.isNotEmpty(req.getManagementName())) {
					depDB.setManagementName(req.getManagementName());
				}

				if (Objects.nonNull(req.getPincode()) && StringUtils.isNotEmpty(req.getPincode())) {
					depDB.setPincode(req.getPincode());
				}
				if (Objects.nonNull(req.getTechnicalDesignationName())
						&& StringUtils.isNotEmpty(req.getTechnicalDesignationName())) {
					depDB.setTechnicalDesignationName(req.getTechnicalDesignationName());
				}

				if (Objects.nonNull(req.getTechnicalEmail()) && StringUtils.isNotEmpty(req.getTechnicalEmail())) {
					depDB.setTechnicalEmail(req.getTechnicalEmail());
				}

				if (Objects.nonNull(req.getTechnicalMobilenumber())
						&& StringUtils.isNotEmpty(req.getTechnicalMobilenumber())) {
					depDB.setTechnicalMobilenumber(req.getTechnicalMobilenumber());
				}

				if (Objects.nonNull(req.getTechnicalName()) && StringUtils.isNotEmpty(req.getTechnicalName())) {
					depDB.setTechnicalName(req.getTechnicalName());
				}

				if (Objects.nonNull(req.getAuaDemoghrapicBasic())) {
					depDB.setAuaDemoghrapicBasic(req.getAuaDemoghrapicBasic());
				}

				if (Objects.nonNull(req.getAuaDemoghrapicFull())) {
					depDB.setAuaDemoghrapicFull(req.getAuaDemoghrapicFull());
				}

				if (Objects.nonNull(req.getAuaDemoghrapicpoi())) {
					depDB.setAuaDemoghrapicpoi(req.getAuaDemoghrapicpoi());
				}

				if (Objects.nonNull(req.getOrganisationName()) && StringUtils.isNotEmpty(req.getOrganisationName())) {
					depDB.setOrganisationName(req.getOrganisationName());
				}

				if (Objects.nonNull(req.getBusinessApplicationName())
						&& StringUtils.isNotEmpty(req.getBusinessApplicationName())) {
					depDB.setBusinessApplicationName(req.getBusinessApplicationName());
				}

				if (Objects.nonNull(req.getAuthenticationPurpose())
						&& StringUtils.isNotEmpty(req.getAuthenticationPurpose())) {
					depDB.setAuthenticationPurpose(req.getAuthenticationPurpose());
				}

				if (Objects.nonNull(req.getKycpurpose()) && StringUtils.isNotEmpty(req.getKycpurpose())) {
					depDB.setKycpurpose(req.getKycpurpose());
				}

				if (Objects.nonNull(req.getApplicationEnvironmentjava())) {
					depDB.setApplicationEnvironmentjava(req.getApplicationEnvironmentjava());
				}
				if (Objects.nonNull(req.getApplicationEnvironmentnet())) {
					depDB.setApplicationEnvironmentnet(req.getApplicationEnvironmentnet());
				}
				if (Objects.nonNull(req.getApplicationEnvironmentphp())) {
					depDB.setApplicationEnvironmentphp(req.getApplicationEnvironmentphp());
				}
				if (Objects.nonNull(req.getRddevicesMantra())) {
					depDB.setRddevicesMantra(req.getRddevicesMantra());
				}
				if (Objects.nonNull(req.getRddevicesStartek())) {
					depDB.setRddevicesStartek(req.getRddevicesStartek());
				}
				if (Objects.nonNull(req.getIntegrationApprochAPI())) {
					depDB.setIntegrationApprochAPI(req.getIntegrationApprochAPI());
				}
				if (Objects.nonNull(req.getIntegrationApprochThinClient())) {
					depDB.setIntegrationApprochThinClient(req.getIntegrationApprochThinClient());
				}

				if (Objects.nonNull(req.getIntegrationApprochweb())) {
					depDB.setIntegrationApprochweb(req.getIntegrationApprochweb());
				}

				if (Objects.nonNull(req.getIntegrationApprochmobile())) {
					depDB.setIntegrationApprochmobile(req.getIntegrationApprochmobile());
				}
				if (Objects.nonNull(req.getOtherservices())) {
					depDB.setOtherservices(req.getOtherservices());
				}
				if (Objects.nonNull(req.getOtherservicesdbt())) {
					depDB.setOtherservicesdbt(req.getOtherservicesdbt());
				}
				if (Objects.nonNull(req.getOtherservicesdigitalsignature())) {
					depDB.setOtherservicesdigitalsignature(req.getOtherservicesdigitalsignature());
				}
				if (Objects.nonNull(req.getModelTransaction()) && StringUtils.isNotEmpty(req.getModelTransaction())) {
					depDB.setModelTransaction(req.getModelTransaction());
				}

				if (Objects.nonNull(req.getIntegrationcharges())) {
					depDB.setIntegrationcharges(req.getIntegrationcharges());
				}

				depDB.setNavigateStatus(true);

				PaymentMode mode = new PaymentMode();
				System.out.println("subauaid" + req.getSubAuaId());
				mode.setSubAuaId(req.getSubAuaId());
				mode.setNavigateStatus(false);
				paymentModeRepo.save(mode);
				logger.info("Data saved successfully");
//				logger.debug("subaua data updated");
				return subAuaUserRepo.save(depDB);

			} else

			{
				logger.error("Data not found in the database");
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());

			}
		} catch (TaException e) {
			throw e;
		} catch (Exception e) {

			logger.error("An error subaua update occurred: {}", e.getMessage(), e);
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	IntegrationCharges intcharge = new IntegrationCharges();

	@Override
	public TransactionModelResponse getsubauaEachPayment(TransactionModelRequest req) {

		try {

			if (req != null) {

				logger.info("sub aua TransactionModelRequest ");

				List<MainSubService> services = mainSubServiceRepo.findAll();
				System.out.println("callllllllllllllllllllll" + services);
				double totalAmount = services.stream()
						.filter(service -> isParameterTrue(service.getParametername(), req)).mapToDouble(service -> {
							double amount = Double.parseDouble(service.getServicecharge());
							updateIntegrationCharges(service.getParametername(), amount, req.getSubAuaId());
							return amount;
						}).sum();
				double transactionModel = services.stream()
						.filter(element -> element.getServicename().equals(req.getTransactionModel()))
						.mapToDouble(element -> Double.parseDouble(element.getServicecharge())).findFirst().orElse(0.0);
				TransactionModelResponse res = new TransactionModelResponse();

				System.out.println("amounttttttttttttttttttttttttttttt" + totalAmount);
				res.setTotalamount(totalAmount);
				return res;
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			throw e;
		} catch (Exception e) {
			logger.error("An error TransactionModelRequest occurred: {}", e.getMessage(), e);
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	private void updateIntegrationCharges(String parameterName, double amount, String subAuaId) {
		intcharge.setSubAuaId(subAuaId);
		switch (parameterName) {
		case "applicationEnvironmentjava":
			intcharge.setApplicationEnvironmentjava(amount);
			break;
		case "applicationEnvironmentnet":
			intcharge.setApplicationEnvironmentnet(amount);
			break;
		case "applicationEnvironmentphp":
			intcharge.setApplicationEnvironmentphp(amount);
			break;
		case "auaDemographic":
			intcharge.setAuaDemographic(amount);
			break;
		case "auaFingerprint":
			intcharge.setAuaFingerprint(amount);
			break;
		case "auaOtp":
			intcharge.setAuaOtp(amount);
			break;
		case "auaIris":
			intcharge.setAuaIris(amount);
			break;
		case "integrationApprochAPI":
			intcharge.setIntegrationApprochAPI(amount);
			break;
		case "integrationApprochThinClient":
			intcharge.setIntegrationApprochThinClient(amount);
			break;
		case "integrationApprochmobile":
			intcharge.setIntegrationApprochmobile(amount);
			break;
		case "integrationApprochweb":
			intcharge.setIntegrationApprochweb(amount);
			break;
		case "kuaFingerprint":
			intcharge.setKuaFingerprint(amount);
			break;
		case "kuaIris":
			intcharge.setKuaIris(amount);
			break;
		case "kuaotp":
			intcharge.setKuaotp(amount);
			break;
		case "otherservicesdbt":
			intcharge.setOtherservicesdbt(amount);
			break;
		case "otherservicesdigitalsignature":
			intcharge.setOtherservicesdigitalsignature(amount);
			break;
		default:

		}
	}

	private boolean isParameterTrue(String parameterName, TransactionModelRequest req) {
		switch (parameterName) {
		case "applicationEnvironmentjava":
			return req.isApplicationEnvironmentjava();
		case "applicationEnvironmentnet":
			return req.isApplicationEnvironmentnet();
		case "applicationEnvironmentphp":
			return req.isApplicationEnvironmentphp();
		case "auaDemographic":
			return req.isAuaDemographic();
		case "auaFingerprint":
			return req.isAuaFingerprint();
		case "auaOtp":
			return req.isAuaOtp();
		case "auaIris":
			return req.isAuaIris();
		case "integrationApprochAPI":
			return req.isIntegrationApprochAPI();
		case "integrationApprochThinClient":
			return req.isIntegrationApprochThinClient();
		case "integrationApprochmobile":
			return req.isIntegrationApprochmobile();
		case "integrationApprochweb":
			return req.isIntegrationApprochweb();
		case "kuaFingerprint":
			return req.isKuaFingerprint();
		case "kuaIris":
			return req.isKuaIris();
		case "kuaotp":
			return req.isKuaotp();
		case "otherservicesdbt":
			return req.isOtherservicesdbt();
		case "otherservicesdigitalsignature":
			return req.isOtherservicesdigitalsignature();
//		case "rddevicesMantra":
//			return req.isRddevicesMantra();
//		case "rddevicesStartek":
//			return req.isRddevicesStartek();
		default:
			return false; // Default to false if the parameter name is not recognized
		}

	}

	@Override
	public void deleteAgencyDetailsById(String id) {
		try {
			if (id != null) {
				List<SubAuaUser> d = subAuaUserRepo.findBySubAuaId(id);
				if (!CollectionUtils.isEmpty(d)) {
					subAuaUserRepo.delete(d.get(0));
				} else {
					logger.error("Data not found in the database");
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			}
		} catch (TaException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public List<SubAuaUser> getAllData() {
		List<SubAuaUser> listdata = subAuaUserRepo.findAll();
		Collections.reverse(listdata);
		for (int i = 0; i < listdata.size(); i++) {
			listdata.get(i).setId((long) (i + 1));
		}
		return listdata;
	}

	@Override
	public List<SubAuaUser> getapprove(SubAuaApproveRequest req, HttpServletRequest servletRequest)
			throws MessagingException {
		List<SubAuaUser> approvelist = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
		try {
			if (!approvelist.isEmpty()) {
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				if (mailMessage != null) {
					SubAuaUser user = approvelist.get(0);
					user.setStatus(req.getStatus());

					saveApplicationStatus("Admin Verified Enquiry Form.", user.getUsername(), "Certification Upload");
					if ("Rejected".equals(req.getStatus())) {
						asyncEmailSender.sendEmailAsync(TAConstantes.DITECEMail,
								DitecMailConstant.getHtmlMessageadminReject(req, servletRequest),
								TAConstantes.ALERT_MESSAGE);
						subAuaUserRepo.saveAndFlush(user);
					} else if ("Approved".equals(req.getStatus())) {

						List<ProgressStatus> progresslist = progressStatusRepo.findBySubAuaId(req.getSubAuaId());

						if (CollectionUtils.isNotEmpty(progresslist)) {
							progresslist.get(0).setApplicationDescription("document upload...");
							progresslist.get(0).setApplicationStatus(3);
							progressStatusRepo.save(progresslist.get(0));
						}

						try {
							user.setUserName(req.getSubAuaId());
							subAuaUserRepo.saveAndFlush(user);
							SubAuaCertifacte fileUpload = new SubAuaCertifacte();
							List<SubAuaCertifacte> filesList = subAuaCertifacteRepo.findBySubauaId(req.getSubAuaId());
							if (CollectionUtils.isNotEmpty(filesList)) {
								fileUpload = filesList.get(0);
							}
							fileUpload.setSubauaId(req.getSubAuaId());
							fileUpload.setCreatedDate(new Date());
							fileUpload.setLastModifiedDate(new Date());
							fileUpload.setCreatedBy("superadmin");
							subAuaCertifacteRepo.save(fileUpload);
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}

					return approvelist;
				}
			}
		} catch (TaException e) {
			throw e;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return approvelist;
	}

	@Override
	public Page<SubAuaUser> getAllDataResponse(Pageable pageable) {
		Page<SubAuaUser> pageData = subAuaUserRepo.findAll(pageable);
		List<SubAuaUser> listData = new ArrayList<>(pageData.getContent());
		Collections.reverse(listData);
		for (int i = 0; i < listData.size(); i++) {
			listData.get(i).setId((long) (i + 1));
		}
		return new PageImpl<>(listData, pageable, pageData.getTotalElements());
	}

	@Override
	public boolean getApplicationstatus(SubAuaLoginRequest req, HttpServletRequest servl) {
		List<SubAuaUser> approvelist = subAuaUserRepo.findByManagementEmailOrManagementMobileNumber(req.getUserName());
		if (!approvelist.isEmpty()) {

			if ("Active".equals(approvelist.get(0).getStatus())) {
				return true;
			}

		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}
		return false;

	}

	@Override
	public SubAuaUser changePassword(SubAuaUserResetPasswordRequest req) {
		logger.info("SubAuaUserResetPasswordRequest req");
		try {
			List<SubAuaUser> user = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
			if (user != null && CollectionUtils.isNotEmpty(user)) {
				SubAuaUser userss = user.get(0);
				System.out.println("secondddddddddddddddddddddddddd" + req);
				if (userss.getPassword().equals(SHAEnc.encryptData(req.getPassword()))) {
					logger.error("Same password as before");
					throw new TaException(ErrorCode.PASSWORD_EXIST, ErrorCode.PASSWORD_EXIST.getErrorMsg());
				}
				System.out.println("afer change password" + req.getPassword());
				user.get(0).setPassword(SHAEnc.encryptData(req.getPassword()));
				user.get(0).setFirsttimeuser(1);
				SubAuaUser users = subAuaUserRepo.save(user.get(0));
				logger.info("data saved successfully");
				return users;
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			throw e;
		} catch (Exception e) {
			logger.error("An  Login error  occurred: {}", e.getMessage(), e);
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public SubAuaUser getforgotPassword(SubAuaUserForgotPasswodRequest req, HttpServletRequest servletRequest)
			throws MessagingException {
		try {
			if (req != null) {
				System.out.println("mail request bantu " + req);
				List<SubAuaUser> user = subAuaUserRepo.findByManagementEmail(req.getManagementEmail());
				if (CollectionUtils.isNotEmpty(user)) {
					SubAuaUser users = user.get(0);
					users.setForgotpassword(1);
					subAuaUserRepo.save(users);
					asyncEmailSender.sendEmailAsync(users.getManagementEmail(),
							DitecMailConstant.getforgotHtmlMessage(users, servletRequest),
							TAConstantes.FORGOT_PASSWORD);
					return users;
				} else {
					logger.error("user not found in data base");
					throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getErrorMsg());
				}
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public SubAuaUser forgotchangepassword(SubAuaChangePasswordRequest request) {
		System.out.println("reqchangepassword" + request);

		if (request.getSubAuaId() != null) {
			try {
				String sts = SHAEnc.decryptData(request.getSubAuaId());
				List<SubAuaUser> list = subAuaUserRepo.findBySubAuaId(sts);
//				if (!list.isEmpty()) {
				if (CollectionUtils.isNotEmpty(list)) {
					String stpass = SHAEnc.encryptData(request.getPassword());
					list.get(0).setPassword(stpass);
					list.get(0).setForgotpassword(1);
					subAuaUserRepo.save(list.get(0));
					return list.get(0);
				} else {
					logger.error("User not found in database");
					throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getErrorMsg());
				}
			} catch (TaException e) {
				throw e;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
			}
		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}
	}

	@Override
	public SubAuaUser chceckactivelink(SubAuaforgotPasswordCheckLinkRequest req) {

		try {
			if (req.getSubAuaId() != null) {
				String subid = SHAEnc.encrypt(req.getSubAuaId());
				List<SubAuaUser> finduser = subAuaUserRepo.findBySubAuaId(subid);
				return finduser.get(0);
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public SubAuaUser getAllDataResponse(@Valid SubAuaApproveRequest req) {
		List<SubAuaUser> listdata = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
		if (CollectionUtils.isNotEmpty(listdata)) {
			return listdata.get(0);
		} else
			return null;
	}

	@Override
	public Page<SubAuaUserResponse> getSubAuaRePage(Pageable pageable) {
		Page<SubAuaUser> pageData = subAuaUserRepo.findAll(pageable);
		List<SubAuaUserResponse> modifiedList = new ArrayList<>();
		List<SubAuaUser> content = pageData.getContent();
		int totalElements = (int) pageData.getTotalElements();
		for (int i = content.size() - 1; i >= 0; i--) {
			SubAuaUser user = content.get(i);
			int currentIndex = content.size() - 1 - i;
			int sequence = totalElements - i;
			SubAuaUserResponse modifiedResponse = createModifiedResponse(user, totalElements, currentIndex, sequence,
					sequence);
			modifiedList.add(modifiedResponse);
		}
		return new PageImpl<>(modifiedList, pageable, totalElements);
	}

	private SubAuaUserResponse createModifiedResponse(SubAuaUser user, int totalElements, int i, int currentIndex,
			long sequence) {
		SubAuaUserResponse res = new SubAuaUserResponse();
		res.setId((long) sequence);
		res.setSubAuaId(user.getSubAuaId());
		res.setOrganisationName(user.getOrganisationName());
		res.setBusinessApplicationName(user.getBusinessApplicationName());
		res.setStatus(user.getStatus());
		res.setApplicationstatus(user.getApplicationstatus());
		res.setAddress(user.getAddress());
		res.setDistrict(user.getDistrict());
		res.setCity(user.getCity());
		res.setPincode(user.getPincode());
		res.setManagementName(user.getManagementName());
		res.setManagementDesignationName(user.getManagementDesignationName());
		res.setManagementMobilenumber(user.getManagementMobilenumber());
		res.setManagementEmail(user.getManagementEmail());
		res.setTechnicalName(user.getTechnicalName());
		res.setTechnicalDesignationName(user.getTechnicalDesignationName());
		res.setTechnicalEmail(user.getTechnicalEmail());
		res.setTechnicalMobilenumber(user.getTechnicalMobilenumber());
		res.setCreatedBy(user.getCreatedBy());
		res.setCreatedDate(formatDate(user.getCreatedDate()));
		res.setLastModifiedBy(user.getLastModifiedBy());
		res.setLastModifiedDate(formatDate(user.getLastModifiedDate()));
		res.setAuthenticationPurpose(user.getAuthenticationPurpose());
		res.setKycpurpose(user.getKycpurpose());
		res.setOtherservices(user.getOtherservices());
		res.setUserName(user.getUsername());
		res.setPassword(user.getPassword());
		res.setApplicationEnvironmentjava(user.getApplicationEnvironmentjava());
		res.setApplicationEnvironmentnet(user.getApplicationEnvironmentnet());
		res.setApplicationEnvironmentphp(user.getApplicationEnvironmentphp());
		res.setRddevicesMantra(user.getRddevicesMantra());
		res.setRddevicesStartek(user.getRddevicesStartek());
		res.setIntegrationApprochAPI(user.getIntegrationApprochAPI());
		res.setIntegrationApprochThinClient(user.getIntegrationApprochThinClient());
		res.setIntegrationApprochweb(user.getIntegrationApprochweb());
		res.setIntegrationApprochmobile(user.getIntegrationApprochmobile());
		res.setAuaDemographicCategory(user.getAuaDemographicCategory());
		res.setAuaDemographic(user.getAuaDemographic());
		res.setAuaOtp(user.getAuaOtp());
		res.setAuaFingerprint(user.getAuaFingerprint());
		res.setAuaIris(user.getAuaIris());
		res.setKuaOtp(user.getKuaOtp());
		res.setKuaFingerprint(user.getKuaFingerprint());
		res.setKuaIris(user.getKuaIris());
		res.setAuaDemoghrapicBasic(user.getAuaDemoghrapicBasic());
		res.setAuaDemoghrapicpoi(user.getAuaDemoghrapicpoi());
		res.setAuaDemoghrapicFull(user.getAuaDemoghrapicFull());
		res.setOtherservicesdbt(user.getOtherservicesdbt());
		res.setOtherservicesdigitalsignature(user.getOtherservicesdigitalsignature());
		res.setNavigateStatus(user.getNavigateStatus());
		res.setModelTransaction(user.getModelTransaction());
		res.setIntegrationcharges(user.getIntegrationcharges());
		return res;
	}

	private String formatDate(Date date) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			return formatter.format(date);
		}
		return null;
	}

	@Override
	public SubAuaUser getChangePassword(SubAuaChangePasswordRequest req) {
		List<SubAuaUser> lists = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
		if (CollectionUtils.isNotEmpty(lists)) {
			String psw = SHAEnc.decryptData(lists.get(0).getPassword());
			if (req.getPassword().equals(psw)) {
				if (req.getNewpassword().equals(psw)) {
					throw new TaException(ErrorCode.OLDPASSWORD_NEWPASSWORD,
							ErrorCode.OLDPASSWORD_NEWPASSWORD.getErrorMsg());
				}
				SubAuaUser newpsw = lists.get(0);
				newpsw.setPassword(SHAEnc.encryptData(req.getNewpassword()));
				subAuaUserRepo.save(newpsw);
				return newpsw;
			} else {
				throw new TaException(ErrorCode.USER_PASSWORD_NOT_VALID,
						ErrorCode.USER_PASSWORD_NOT_VALID.getErrorMsg());
			}
		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}

	}

	@Override
	public SubAuaUser getAccountDetiles(SubAuaUserRequest req) {
		List<SubAuaUser> accountactive = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
		if (CollectionUtils.isNotEmpty(accountactive)) {
			if (req.getAccountStaus().equalsIgnoreCase("Active")) {
				SubAuaUser sts = accountactive.get(0);
				sts.setAccountStatus("Active");
				subAuaUserRepo.save(sts);
				return sts;
			} else {
				SubAuaUser sts = accountactive.get(0);
				sts.setAccountStatus("Deactive");
				subAuaUserRepo.save(sts);
				return sts;
			}
		} else {
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}

	}

	@Override
	public LoginResponse loginstatus(SubAuaLoginRequest req, HttpServletRequest servl) {
		logger.info("Login request received");
		try {
			LoginResponse res = new LoginResponse();

			if (StringUtils.isNotEmpty(req.getUserName()) && StringUtils.isNotEmpty(req.getPassword())) {

				String encryptedPassword = SHAEnc.encryptData(req.getPassword());

				// Check if user roles exist
				List<DitecUserRoles> roles = ditecUserRolesRepo.findByUserName(req.getUserName());
				if (CollectionUtils.isNotEmpty(roles) && roles.get(0).getPassword().equals(encryptedPassword)) {
					DitecUserRoles role = roles.get(0);
					res.setUser(role.getRole());
					res.setUserName(role.getUsername());
					res.setToken(jwtUtils.generateToken(role));
					authenticationManager.authenticate(
							new UsernamePasswordAuthenticationToken(req.getUserName(), encryptedPassword));
				} else {

					// Check if user exists
					List<SubAuaUser> approvelist = subAuaUserRepo
							.findByManagementEmailOrManagementMobileNumber(req.getUserName());
					if (CollectionUtils.isEmpty(approvelist)) {
						logger.info("User not found");
						throw new TaException(ErrorCode.USER_CRED_VALID, ErrorCode.USER_CRED_VALID.getErrorMsg());
					}

					SubAuaUser user = approvelist.get(0);
					if (!user.getAccountStatus().equals("Active")) {
						logger.info("User account is not active");
						throw new TaException(ErrorCode.ACCOUNT_DEACTIVATED,
								"User account is not active, please contact support");
					}
					if (user.getPassword().equals(encryptedPassword)) {
						user.setLoginAttempts(0);
						subAuaUserRepo.save(user);
						authenticationManager.authenticate(
								new UsernamePasswordAuthenticationToken(req.getUserName(), encryptedPassword));

//						List<SubAuaUser> approvelist = subAuaUserRepo.findByManagementEmailOrManagementMobileNumber(req.getUserName());

//						if ("Active".equals(approvelist.get(0).getStatus())) {
//							return true;
//						}
//						if (user.getStatus().equals("Active")) {
//							res.setDocumentstatus(true);
//						} else {
//							res.setDocumentstatus(false);
//						}

						if (user.getStatus().equals("Pending")) {
							res.setDocumentstatus(true);
						} else {
							res.setDocumentstatus(false);
						}

						res.setFirsttimeuser(user.getFirsttimeuser());
						res.setUser(user.getRole());
						res.setUserName(user.getSubAuaId());
						res.setToken(jwtUtils.generateToken(user));
						List<SubAuaAPI> listA = subAuaAPIRepo.findBySubAuaId(user.getSubAuaId());
						res.setSubauaname(user.getManagementName());
						if (CollectionUtils.isNotEmpty(listA)) {
							res.setSubauakey(listA.get(0).getProdkey());
						}
						return res;
					} else {
						user.setLoginAttempts(user.getLoginAttempts() + 1);
						subAuaUserRepo.save(user);
						int remainingAttempts = 5 - user.getLoginAttempts();

						if (user.getLoginAttempts() == 3) {
							throw new TaException(ErrorCode.ACCOUNT_DEACTIVATED,
									"Incorrect password. This is your third attempt. Two attempts remaining.");
						} else if (user.getLoginAttempts() == 4) {
							throw new TaException(ErrorCode.ACCOUNT_DEACTIVATED,
									"Incorrect password. This is your fourth attempt. One attempt remaining.");
						} else if (user.getLoginAttempts() == 5) {

//						asyncEmailSender.sendEmailAsync(TAConstantes.DITECEMail,
//								DitecMailConstant.getHtmlMessageadmin(approvelist.get(0).getManagementEmail(), servl), TAConstantes.LOGIN);

							user.setAccountStatus("Deactive");
							subAuaUserRepo.save(user);
							throw new TaException(ErrorCode.ACCOUNT_DEACTIVATED,
									"Account deactivated after 5 failed login attempts");
						} else {
							throw new TaException(ErrorCode.USER_CRED_VALID, ErrorCode.USER_CRED_VALID.getErrorMsg());
						}
					}

				}

			} else {
				System.out.println("BAD_REQUEST");
				throw new TaException(ErrorCode.BAD_REQUEST, "Username and password cannot be empty");

			}
			return res;

		} catch (TaException e) {
			e.printStackTrace();
			logger.error("TaException occurred during login: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("An error occurred during login: {}", e.getMessage(), e);
			throw new TaException(ErrorCode.INTERNAL_ERROR, "Internal server error during login");
		}
	}

}
