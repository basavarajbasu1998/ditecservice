package com.ta.ditec.services.serviceimpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.IntegrationInvoiceDetiles;
import com.ta.ditec.services.model.IntgrationInvoiceServiceCharges;
import com.ta.ditec.services.model.MainSubService;
import com.ta.ditec.services.model.ServiceCharges;
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.repo.IntegrationInvoiceDetilesRepo;
import com.ta.ditec.services.repo.IntgrationInvoiceServiceChargesRepo;
import com.ta.ditec.services.repo.MainSubServiceRepo;
import com.ta.ditec.services.repo.ServiceChargesRepo;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.report.domain.AUTHTransaction;
import com.ta.ditec.services.report.domain.AuthOtp;
import com.ta.ditec.services.report.domain.EKYCTransaction;
import com.ta.ditec.services.report.repo.AUTHTransactionRepo;
import com.ta.ditec.services.report.repo.AuthOtpRepo;
import com.ta.ditec.services.report.repo.EKYCTransactionRepo;
import com.ta.ditec.services.request.SubAuaInvoiceUserRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.InvoiceResponse;
import com.ta.ditec.services.response.InvoiceServiceResponse;
import com.ta.ditec.services.service.IntegrationInvoiceDetilesService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class IntegrationInvoiceDetilesServiceImpl implements IntegrationInvoiceDetilesService {
	private static final Logger logger = LoggerFactory.getLogger(IntegrationInvoiceDetilesServiceImpl.class);

	@Autowired
	private IntegrationInvoiceDetilesRepo integrationInvoiceDetilesRepo;

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	@Autowired
	private MainSubServiceRepo mainSubServiceRepo;

	@Autowired
	private IntgrationInvoiceServiceChargesRepo intgrationInvoiceServiceChargesRepo;

	@Autowired
	private AUTHTransactionRepo authTransactionRepo;

	@Autowired
	private EKYCTransactionRepo ekycTransactionRepo;

	@Autowired
	private AuthOtpRepo authOtpRepo;

	@Autowired
	private ServiceChargesRepo serviceChargesRepo;

	@Override
//	public InvoiceResponse getInvoiceRequestAndCharges(SubAuaUserRequest req) {
//
//		try {
//
//			if (req.getSubAuaId() != null) {
//
//				InvoiceResponse response = new InvoiceResponse();
//
//				List<IntgrationInvoiceServiceCharges> chargesList = new ArrayList<>();
//				IntegrationInvoiceDetiles invoiceDetails = null;
//
//				List<SubAuaUser> sublist = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
//				logger.info("Data fetched from database");
//				if (CollectionUtils.isNotEmpty(sublist)) {
//					SubAuaUser subAuaUser = sublist.get(0);
////			invoiceDetails = integrationInvoiceDetilesRepo.findBySubAuaId(subAuaUser.getSubAuaId());
//					Map<String, Boolean> parameterMap = getParameterMap(subAuaUser);
//					List<String> trueKeys = parameterMap.entrySet().stream().filter(Map.Entry::getValue)
//							.map(Map.Entry::getKey).collect(Collectors.toList());
//					for (String parameterName : trueKeys) {
//						IntgrationInvoiceServiceCharges charges = new IntgrationInvoiceServiceCharges();
//						List<MainSubService> subservice = mainSubServiceRepo.findByParametername(parameterName);
//
//						logger.info("Data fetched from databse: " + subservice.toString());
//						if (!subservice.isEmpty()) {
//							charges.setAmount(subservice.get(0).getServicecharge());
//							charges.setMainService(subservice.get(0).getServicename());
//							charges.setSubAuaId(req.getSubAuaId());
//							intgrationInvoiceServiceChargesRepo.save(charges);
//							logger.info("data saved succeddfully");
//							chargesList.add(charges);
//						}
//					}
//				}
//
//				IntegrationInvoiceDetiles detiles = new IntegrationInvoiceDetiles();
//
//				System.out.println("sublist.get(0).getOrganisationName()" + sublist.get(0).getOrganisationName());
//
//				detiles.setOrganisationName(sublist.get(0).getOrganisationName());
//				detiles.setManagementName(sublist.get(0).getManagementName());
//				detiles.setInvoiceNumber(TAUtils.generateInvoiceId(sublist.get(0).getId()));
//				detiles.setManagementEmail(sublist.get(0).getManagementEmail());
//				detiles.setManagementMobilenumber(sublist.get(0).getManagementMobilenumber());
//				detiles.setAddress(sublist.get(0).getAddress());
//				detiles.setGstNumber("12345");
//				detiles.setPincode(sublist.get(0).getPincode());
//				detiles.setStatus(0);
//				detiles.setSubAuaId(sublist.get(0).getSubAuaId());
//				detiles.setSubtotal(sublist.get(0).getIntegrationcharges());
//
//				if (sublist.get(0).getIntegrationcharges() != null) {
//					Double taxs = sublist.get(0).getIntegrationcharges() * 0.09;
//					Double total = sublist.get(0).getIntegrationcharges()
//							+ sublist.get(0).getIntegrationcharges() * 0.18;
//					detiles.setSgst(taxs);
//					detiles.setCgst(taxs);
//					detiles.setTotal(total);
//				}
//				detiles.setCreatedBy("super admin");
//				detiles.setLastModifiedBy("super admin");
//				detiles.setLastModifiedDate(new Date());
//				detiles.setCreatedDate(new Date());
//				integrationInvoiceDetilesRepo.save(detiles);
//
//				logger.info("Data saved successfully into the database");
//				response.setAddress(detiles.getAddress());
//
//				response.setOrganisationName(detiles.getOrganisationName());
//				response.setManagementName(detiles.getManagementName());
//				response.setInvoiceNumber(detiles.getInvoiceNumber());
//				response.setManagementEmail(detiles.getManagementEmail());
//				response.setManagementMobilenumber(detiles.getManagementMobilenumber());
//				response.setAddress(detiles.getAddress());
//				response.setGstNumber(detiles.getGstNumber());
//				response.setPincode(detiles.getPincode());
//				response.setStatus(detiles.getStatus());
//				response.setSubAuaId(detiles.getSubAuaId());
//				response.setSubtotal(detiles.getSubtotal());
//
//				Date createDate = detiles.getCreatedDate();
//				LocalDate localDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//				String formattedDate = localDate.format(formatter);
//				response.setInvoiceDate(formattedDate);
//
//				response.setSgst(detiles.getSgst());
//				response.setCgst(detiles.getCgst());
//				response.setTotal(detiles.getTotal());
//
//				response.setIntgrationInvoiceServiceCharges(chargesList);
//
//				logger.debug(response.toString());
//				return response;
//			} else {
//
//				logger.error("Exception occurred in request ");
//				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
//			}
//		} catch (TaException e) {
//			logger.error("Ta exception handled");
//			throw e;
//		}
//
//		catch (Exception e) {
//			logger.error("Exception occurred in server side");
//			e.printStackTrace();
//			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
//		}
//	}

//	@Override
	public InvoiceResponse getInvoiceRequestAndCharges(SubAuaUserRequest req) {

		try {

			if (req.getSubAuaId() != null) {

				InvoiceResponse response = new InvoiceResponse();

				// Initialize chargesList
				List<IntgrationInvoiceServiceCharges> chargesList = new ArrayList<>();
				IntegrationInvoiceDetiles invoiceDetails = null;

				// Fetch sublist
				List<SubAuaUser> sublist = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
				logger.info("Data fetched from database");

				// Check if sublist is not empty
				if (CollectionUtils.isNotEmpty(sublist)) {
					SubAuaUser subAuaUser = sublist.get(0);
					// Uncomment this line if needed
					// invoiceDetails =
					// integrationInvoiceDetilesRepo.findBySubAuaId(subAuaUser.getSubAuaId());

					// Get parameterMap from subAuaUser
					Map<String, Boolean> parameterMap = getParameterMap(subAuaUser);

					// Filter true keys
					List<String> trueKeys = parameterMap.entrySet().stream().filter(Map.Entry::getValue)
							.map(Map.Entry::getKey).collect(Collectors.toList());

					// Iterate over trueKeys
					for (String parameterName : trueKeys) {
						IntgrationInvoiceServiceCharges charges = new IntgrationInvoiceServiceCharges();
						List<MainSubService> subservice = mainSubServiceRepo.findByParametername(parameterName);
						logger.info("Data fetched from databse: " + subservice.toString());

						// Check if subservice is not empty
						if (!subservice.isEmpty()) {
							charges.setAmount(subservice.get(0).getServicecharge());
							charges.setMainService(subservice.get(0).getServicename());
							charges.setSubAuaId(req.getSubAuaId());

							// Save charges to repository
							intgrationInvoiceServiceChargesRepo.save(charges);
							logger.info("data saved succeddfully");
							chargesList.add(charges);
						}
					}
				}

				// Create new IntegrationInvoiceDetiles instance
				IntegrationInvoiceDetiles detiles = new IntegrationInvoiceDetiles();

				// Set values for detiles
				detiles.setOrganisationName(sublist.get(0).getOrganisationName());
				detiles.setManagementName(sublist.get(0).getManagementName());
				detiles.setInvoiceNumber(TAUtils.generateInvoiceId(sublist.get(0).getId()));
				detiles.setManagementEmail(sublist.get(0).getManagementEmail());
				detiles.setManagementMobilenumber(sublist.get(0).getManagementMobilenumber());
				detiles.setAddress(sublist.get(0).getAddress());
				detiles.setGstNumber("12345");
				detiles.setPincode(sublist.get(0).getPincode());
				detiles.setStatus(0);
				detiles.setSubAuaId(sublist.get(0).getSubAuaId());
				detiles.setSubtotal(sublist.get(0).getIntegrationcharges());

				// Calculate taxes and total
				if (sublist.get(0).getIntegrationcharges() != null) {
					Double taxs = sublist.get(0).getIntegrationcharges() * 0.09;
					Double total = sublist.get(0).getIntegrationcharges()
							+ sublist.get(0).getIntegrationcharges() * 0.18;
					detiles.setSgst(taxs);
					detiles.setCgst(taxs);
					detiles.setTotal(total);
				}

				detiles.setCreatedBy("super admin");
				detiles.setLastModifiedBy("super admin");
				detiles.setLastModifiedDate(new Date());
				detiles.setCreatedDate(new Date());

				// Save detiles to repository
				integrationInvoiceDetilesRepo.save(detiles);
				logger.info("Data saved successfully into the database");

				// Set response values
				response.setAddress(detiles.getAddress());
				response.setOrganisationName(detiles.getOrganisationName());
				response.setManagementName(detiles.getManagementName());
				response.setInvoiceNumber(detiles.getInvoiceNumber());
				response.setManagementEmail(detiles.getManagementEmail());
				response.setManagementMobilenumber(detiles.getManagementMobilenumber());
				response.setAddress(detiles.getAddress());
				response.setGstNumber(detiles.getGstNumber());
				response.setPincode(detiles.getPincode());
				response.setStatus(detiles.getStatus());
				response.setSubAuaId(detiles.getSubAuaId());
				response.setSubtotal(detiles.getSubtotal());

				// Format invoice date
				Date createDate = detiles.getCreatedDate();
				LocalDate localDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				String formattedDate = localDate.format(formatter);
				response.setInvoiceDate(formattedDate);

				response.setSgst(detiles.getSgst());
				response.setCgst(detiles.getCgst());
				response.setTotal(detiles.getTotal());

				response.setIntgrationInvoiceServiceCharges(chargesList);

				logger.debug(response.toString());
				return response;
			} else {
				// Handle case when req.getSubAuaId() is null
				logger.error("Exception occurred in request ");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			// Handle TaException
			logger.error("Ta exception handled");
			throw e;
		} catch (Exception e) {
			// Handle other exceptions
			logger.error("Exception occurred in server side");
			e.printStackTrace();
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	private Map<String, Boolean> getParameterMap(SubAuaUser subAuaUser) {
		Map<String, Boolean> parameterMap = new HashMap<>();
		parameterMap.put("applicationEnvironmentjava", subAuaUser.getApplicationEnvironmentjava());
		parameterMap.put("applicationEnvironmentnet", subAuaUser.getApplicationEnvironmentnet());
		parameterMap.put("applicationEnvironmentphp", subAuaUser.getApplicationEnvironmentphp());
		parameterMap.put("auaDemographic", subAuaUser.getAuaDemoghrapicFull());
		parameterMap.put("auaFingerprint", subAuaUser.getAuaFingerprint());
		parameterMap.put("auaIris", subAuaUser.getAuaIris());
		parameterMap.put("kuaotp", subAuaUser.getKuaOtp());
		parameterMap.put("kuaFingerprint", subAuaUser.getKuaFingerprint());
		parameterMap.put("kuaIris", subAuaUser.getKuaIris());
		parameterMap.put("integrationApprochAPI", subAuaUser.getIntegrationApprochAPI());
		parameterMap.put("integrationApprochThinClient", subAuaUser.getIntegrationApprochThinClient());
		parameterMap.put("integrationApprochweb", subAuaUser.getIntegrationApprochweb());
		parameterMap.put("integrationApprochmobile", subAuaUser.getIntegrationApprochmobile());
		parameterMap.put("otherservicesdbt", subAuaUser.getOtherservicesdbt());
		parameterMap.put("otherservicesdigitalsignature", subAuaUser.getOtherservicesdigitalsignature());
		return parameterMap;
	}

	@Override
	public InvoiceServiceResponse getEachTransactionCharges(SubAuaInvoiceUserRequest req,
			HttpServletResponse response) {
		System.out.println(req);
		InvoiceServiceResponse res = new InvoiceServiceResponse();
		List<SubAuaUser> sublist = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());

		System.out.println("sublist  sublist" + sublist.get(0));

		String stdate = req.getStartDate();

		String endDate = req.getEndDate();

		LocalDateTime startDateTime = LocalDateTime.parse(stdate + "T00:00:00");
		LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");
		LocalDateTime todayfromDateTime = startDateTime;
		LocalDateTime todayendDateTime = endDateTime;

		Timestamp yeartimestampstart = Timestamp.valueOf(todayfromDateTime);
		Timestamp yeartimestampend = Timestamp.valueOf(todayendDateTime);
		yeartimestampstart.setHours(0);
		yeartimestampstart.setMinutes(0);
		yeartimestampstart.setSeconds(0);
		yeartimestampstart.setNanos(0);
		yeartimestampend.setHours(23);
		yeartimestampend.setMinutes(59);
		yeartimestampend.setMinutes(59);
		yeartimestampend.setNanos(000);

		List<AUTHTransaction> authlist = authTransactionRepo.findByAgencycodeAndRequestdateBetween(req.getSubAuaId(),
				yeartimestampstart, yeartimestampend);
		List<EKYCTransaction> ekyclist = ekycTransactionRepo.findByAgencycodeAndRequestdateBetween(req.getSubAuaId(),
				yeartimestampstart, yeartimestampend);
		List<AuthOtp> otplist = authOtpRepo.findByAgencycodeAndRequestdateBetween(req.getSubAuaId(), yeartimestampstart,
				yeartimestampend);

		Long authbio = authlist.stream().filter(x -> x.getBio().equals("y")).count();
		Long authotp = authlist.stream().filter(x -> x.getOtp().equals("y")).count();
		Long authpa = authlist.stream().filter(x -> x.getPa().equals("y")).count();
		Long authpi = authlist.stream().filter(x -> x.getPi().equals("y")).count();
		Long authpfa = authlist.stream().filter(x -> x.getPfa().equals("y")).count();

		Long ekycbio = ekyclist.stream().filter(x -> x.getBio().equals("y")).count();
		Long ekycotp = ekyclist.stream().filter(x -> x.getOtp().equals("y")).count();
		Long otp = otplist.stream().filter(x -> x.getErrorcode().equals("000")).count();

		List<ServiceCharges> authli = serviceChargesRepo.findBySubService("auaDemographic");
		List<ServiceCharges> authli2 = serviceChargesRepo.findBySubService("auaDemographic");
		List<ServiceCharges> authli3 = serviceChargesRepo.findBySubService("auaOtp");
		List<ServiceCharges> authli4 = serviceChargesRepo.findBySubService("auaFingerprint");
		List<ServiceCharges> authli5 = serviceChargesRepo.findBySubService("auaIris");
		List<ServiceCharges> ekycli6 = serviceChargesRepo.findBySubService("kuaOtp");
		List<ServiceCharges> ekycli7 = serviceChargesRepo.findBySubService("kuaFingerprint");
		List<ServiceCharges> ekycli8 = serviceChargesRepo.findBySubService("kuaIris");

//		Long lis = authli4.get(0).getServiceCharges() * authbio;
//
//		Long lis1 = authli2.get(0).getServiceCharges() * authotp;
//		Long lis2 = authli3.get(0).getServiceCharges() * authpi;
//		Long lis3 = authli.get(0).getServiceCharges() * authpa;
//		Long lis4 = authli5.get(0).getServiceCharges() * authpfa;
//		Long ekycl1 = ekycli6.get(0).getServiceCharges() * ekycbio;
//		Long ekycl2 = ekycli6.get(0).getServiceCharges() * ekycotp;

		Long lis = Optional.ofNullable(authli4)
				.map(list -> list.isEmpty() ? 0L : list.get(0).getServiceCharges() * authbio).orElse(0L);

		Long lis1 = Optional.ofNullable(authli2)
				.map(list -> list.isEmpty() ? 0L : list.get(0).getServiceCharges() * authotp).orElse(0L);

		Long lis2 = Optional.ofNullable(authli3)
				.map(list -> list.isEmpty() ? 0L : list.get(0).getServiceCharges() * authpi).orElse(0L);

		Long lis3 = Optional.ofNullable(authli)
				.map(list -> list.isEmpty() ? 0L : list.get(0).getServiceCharges() * authpa).orElse(0L);

		Long lis4 = Optional.ofNullable(authli5)
				.map(list -> list.isEmpty() ? 0L : list.get(0).getServiceCharges() * authpfa).orElse(0L);

		Long ekycl1 = Optional.ofNullable(ekycli6)
				.map(list -> list.isEmpty() ? 0L : list.get(0).getServiceCharges() * ekycbio).orElse(0L);

		Long ekycl2 = Optional.ofNullable(ekycli6)
				.map(list -> list.isEmpty() ? 0L : list.get(0).getServiceCharges() * ekycotp).orElse(0L);

		Long aua = lis + lis1 + lis2 + lis3 + lis4;
		Long kua = ekycl1 + ekycl2;

		double subtotal = aua + kua;
		double cgst = subtotal * 0.09;
		double sgst = subtotal * 0.09;

		double total = subtotal + cgst + sgst;
		SubAuaUser use = sublist.get(0);

		Date createDate = new Date();
		LocalDate localDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = localDate.format(formatter);
		res.setInvoiceDate(formattedDate);
		res.setAddress(use.getAddress());
		res.setGstNumber("GSTNUM123");
		res.setManagementEmail(use.getManagementEmail());
		res.setManagementMobilenumber(use.getManagementMobilenumber());
		res.setOrganisationName(use.getOrganisationName());
		res.setManagementName(use.getManagementName());
		res.setPincode(use.getPincode());
		res.setSubAuaId(use.getSubAuaId());
		res.setInvoiceNumber("#DSA" + TAUtils.generateInvoiceId(use.getId()));
		res.setStatus(1);
		res.setBillingcycle(use.getModelTransaction());
		res.setAua((double) aua);
		res.setKua((double) kua);
		res.setCgst(cgst);
		res.setSgst(sgst);
		res.setSubtotal(subtotal);
		res.setTotal(total);
		BigDecimal b = new BigDecimal(String.valueOf(total));
		res.setAmountword(TAUtils.convert(b));
		return res;

	}

}
