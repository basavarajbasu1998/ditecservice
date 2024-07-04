package com.ta.ditec.services.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.InvoiceSubTrans;
import com.ta.ditec.services.model.InvoiceTrans;
import com.ta.ditec.services.model.InvoiceTransDetiles;
import com.ta.ditec.services.model.NewInvoice;
import com.ta.ditec.services.model.PaymentPlanDetails;
import com.ta.ditec.services.model.SubAuaAPI;
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.repo.InvoiceSubTransRepo;
import com.ta.ditec.services.repo.InvoiceTransDetilesRepo;
import com.ta.ditec.services.repo.InvoiceTransRepo;
import com.ta.ditec.services.repo.NewInvoiceRepo;
import com.ta.ditec.services.repo.PaymentPlanDetailsRepo;
import com.ta.ditec.services.repo.SubAuaAPIRepo;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.report.domain.AUTHTransaction;
import com.ta.ditec.services.report.domain.EKYCTransaction;
import com.ta.ditec.services.report.repo.AUTHTransactionRepo;
import com.ta.ditec.services.report.repo.AuthOtpRepo;
import com.ta.ditec.services.report.repo.EKYCTransactionRepo;
import com.ta.ditec.services.request.InvoiceTransDetilesRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.InvoiceServiceResponse;
import com.ta.ditec.services.response.InvoiceTransResponse;
import com.ta.ditec.services.service.InvoiceTransService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class InvoiceTransServiceImpl implements InvoiceTransService {

	@Autowired
	private InvoiceTransRepo invoiceTransRepo;

	@Autowired
	private InvoiceSubTransRepo invoiceSubTransRepo;

	@Autowired
	private InvoiceTransDetilesRepo invoiceTransDetilesRepo;

	@Autowired
	private AUTHTransactionRepo authTransactionRepo;

	@Autowired
	private EKYCTransactionRepo ekycTransactionRepo;

	@Autowired
	private AuthOtpRepo authOtpRepo;

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	@Autowired
	private PaymentPlanDetailsRepo paymentPlanDetailsRepo;

	@Autowired
	private SubAuaAPIRepo subAuaAPIRepo;

	@Autowired
	private NewInvoiceRepo newInvoiceRepo;

	@Override
	public InvoiceTrans getInvoiceTrans(InvoiceTrans req) {
		InvoiceTrans invoice = invoiceTransRepo.save(req);
		List<PaymentPlanDetails> paydet = paymentPlanDetailsRepo.findAll();

		List<SubAuaUser> sublist = subAuaUserRepo.findBySubAuaId(invoice.getSubAuaId());

		System.out.println("sublist  sublist  sublist  sublist  sublist" + sublist);

		SubAuaUser user = sublist.get(0);
		PaymentPlanDetails pay = paydet.get(0);

		invoice.setInvoiceId(TAUtils.generateInvoiceId(invoice.getId()));
		invoice.setStatus("Wait for Payment");
		invoice.setCreatedBy(req.getSubAuaId());
		invoice.setLastModifiedBy(req.getSubAuaId());
		invoice.setCreatedDate(new Date());
		invoice.setLastModifiedDate(new Date());
		invoiceTransRepo.save(invoice);
		LocalDateTime startDateTime = LocalDateTime.parse(req.getStartDate() + "T00:00:00");
		LocalDateTime endDateTime = LocalDateTime.parse(req.getEndDate() + "T23:59:59");

		Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
		Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

		List<AUTHTransaction> authList = authTransactionRepo.findByAgencycodeAndRequestdateBetween(req.getSubAuaId(),
				startTimestamp, endTimestamp);
		List<EKYCTransaction> ekycList = ekycTransactionRepo.findByAgencycodeAndRequestdateBetween(req.getSubAuaId(),
				startTimestamp, endTimestamp);

		InvoiceSubTrans authSubTrans = new InvoiceSubTrans();
		InvoiceSubTrans ekycSubTrans = new InvoiceSubTrans();
		if (user.getModelTransaction().equals(paydet.get(0).getPlanType())) {

			if (user.getKuaFingerprint().equals(true) || user.getKuaOtp().equals(true)
					|| user.getKuaIris().equals(true) && pay.getAuthcationtype().equals("Ekyc")) {

				System.out.println("ekycccccccccccccccccccccccccccccccc");

				ekycSubTrans.setInvoiceId(invoice.getInvoiceId());
				ekycSubTrans.setNotrans(ekycList.size());
				ekycSubTrans.setTransamount(ekycList.size() * pay.getEachtransamount());
				ekycSubTrans.setTotalTrans(ekycList.size());
				ekycSubTrans.setTransType(pay.getAuthcationtype());
				ekycSubTrans.setPerTrnas(pay.getEachtransamount());
				ekycSubTrans.setCreatedBy(req.getSubAuaId());
				ekycSubTrans.setLastModifiedBy(req.getSubAuaId());
				ekycSubTrans.setCreatedDate(new Date());
				ekycSubTrans.setLastModifiedDate(new Date());
				invoiceSubTransRepo.save(ekycSubTrans);
			}

			if ((user.getAuaFingerprint().equals(true) || user.getAuaOtp().equals(true)
					|| user.getAuaIris().equals(true)) && pay.getAuthcationtype().equals("Auth")) {

				System.out.println("Authhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

				if (authList != null && !authList.isEmpty()) {
					authSubTrans.setInvoiceId(invoice.getInvoiceId());
					authSubTrans.setNotrans(authList.size());
					authSubTrans.setTransamount(authList.size() * pay.getEachtransamount());
					authSubTrans.setTotalTrans(authList.size());
					authSubTrans.setTransType(pay.getAuthcationtype());
					authSubTrans.setPerTrnas(pay.getEachtransamount());
					authSubTrans.setCreatedBy(req.getSubAuaId());
					authSubTrans.setLastModifiedBy(req.getSubAuaId());
					authSubTrans.setCreatedDate(new Date());
					authSubTrans.setLastModifiedDate(new Date());
					invoiceSubTransRepo.save(authSubTrans);
				}
			}
		} else {
//			throw new TaException(ErrorCode, null)
		}

		InvoiceTransDetiles detiles = new InvoiceTransDetiles();
		detiles.setInvoiceId(invoice.getInvoiceId());
		detiles.setRemarks("admin invoice created");
		detiles.setStatus("new invoice generated");
		detiles.setSubAuaId(invoice.getSubAuaId());
		detiles.setCreatedBy(req.getSubAuaId());
		detiles.setLastModifiedBy(req.getSubAuaId());
		detiles.setCreatedDate(new Date());
		detiles.setLastModifiedDate(new Date());
		invoiceTransDetilesRepo.save(detiles);

		double auth = ekycSubTrans.getTransamount() + authSubTrans.getTransamount();
		double cgst = auth * 0.09;
		double sgst = auth * 0.09;
		double amount = auth + cgst + sgst;
		invoice.setAmount(auth);
		invoice.setCgst(cgst);
		invoice.setIgst(0);
		invoice.setNetamount(amount);
		invoice.setSgst(sgst);
		invoiceTransRepo.save(invoice);
		return invoice;
	}

	@Override
	public List<InvoiceTransResponse> getallInvoiceTrans(SubAuaUserRequest req) {
		List<InvoiceTrans> list = invoiceTransRepo.findBySubAuaId(req.getSubAuaId());
		return list.stream().map(invoice -> {
			InvoiceTransResponse res = new InvoiceTransResponse();
			res.setId(invoice.getId());
			res.setSubAuaId(invoice.getSubAuaId());
			res.setInvoiceId(invoice.getInvoiceId());
			res.setStartDate(invoice.getStartDate());
			res.setEndDate(invoice.getEndDate());
			res.setNetamount(invoice.getNetamount());
			res.setStatus(invoice.getStatus());
			return res;
		}).collect(Collectors.toList());
	}

	@Override
	public List<InvoiceTrans> getInvoiceTransDetilesRequest(InvoiceTransDetilesRequest req) {
		List<InvoiceTrans> trans = invoiceTransRepo.findByInvoiceId(req.getInvoiceId());
		InvoiceTrans intrns = trans.get(0);
		if (CollectionUtils.isNotEmpty(trans)) {
			intrns.setStatus(req.getStatus());
			invoiceTransRepo.save(intrns);
			InvoiceTransDetiles detiles = new InvoiceTransDetiles();
			detiles.setInvoiceId(intrns.getInvoiceId());
			detiles.setRemarks(req.getRemarks());
			detiles.setStatus(req.getStatus());
			detiles.setSubAuaId(intrns.getSubAuaId());
			detiles.setCreatedBy(intrns.getSubAuaId());
			detiles.setLastModifiedBy(intrns.getSubAuaId());
			detiles.setCreatedDate(new Date());
			detiles.setLastModifiedDate(new Date());
			invoiceTransDetilesRepo.save(detiles);
			return trans;
		} else {
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}

	}

	@Override
	public InvoiceServiceResponse getInvoiceServiceResponse(InvoiceTransDetilesRequest req) {
		List<InvoiceTrans> list = invoiceTransRepo.findByInvoiceId(req.getInvoiceId());

		List<InvoiceSubTrans> subtrnslist = invoiceSubTransRepo.findByInvoiceId(req.getInvoiceId());
		List<SubAuaUser> sublist = subAuaUserRepo.findBySubAuaId(list.get(0).getSubAuaId());

		System.out.println("list  list  list" + subtrnslist);

		if (list.isEmpty()) {
			System.out.println("no data find");
			throw new RuntimeException("No data found for given list");
		}

		if (sublist.isEmpty()) {
			System.out.println("no data find");
			throw new RuntimeException("No data found for given sublist");
		}

		if (subtrnslist.isEmpty()) {
			System.out.println("No data found for given subtrnslist");
			throw new RuntimeException("No data found for given subtrnslist");
		}

		InvoiceTrans trns = list.get(0);
		SubAuaUser use = sublist.get(0);

		InvoiceServiceResponse res = new InvoiceServiceResponse();
		Date createDate = new Date();
		LocalDate localDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = localDate.format(formatter);

		res.setInvoiceDate(formattedDate);
		res.setAddress(use.getAddress());
		res.setGstNumber(" ");
		res.setManagementEmail(use.getManagementEmail());
		res.setManagementMobilenumber(use.getManagementMobilenumber());
		res.setOrganisationName(use.getOrganisationName());
		res.setManagementName(use.getManagementName());
		res.setPincode(use.getPincode());
		res.setSubAuaId(use.getSubAuaId());
		res.setInvoiceNumber("#DSA" + TAUtils.generateInvoiceId(use.getId()));
		res.setStatus(1);
		res.setBillingcycle("Quarterly");
		double authAuaAmount = 0.0;
		int authTransCount = 0;
		double ekycAuaAmount = 0.0;
		int ekycTransCount = 0;

		for (InvoiceSubTrans subtrns : subtrnslist) {
			if (subtrns.getTransType().equals("Auth")) {
				authAuaAmount += subtrns.getTransamount();
				authTransCount += subtrns.getNotrans();
			} else if (subtrns.getTransType().equals("Ekyc")) {
				ekycAuaAmount += subtrns.getTransamount();
				ekycTransCount += subtrns.getNotrans();
			}
		}

		// Round amounts to 2 decimal places
		authAuaAmount = roundToTwoDecimalPlaces(authAuaAmount);
		ekycAuaAmount = roundToTwoDecimalPlaces(ekycAuaAmount);

		res.setAua(authAuaAmount);
		res.setAuthTrns(authTransCount);
		res.setKua(ekycAuaAmount);
		res.setEkycTrns(ekycTransCount);

		// Round the amounts in the invoice transaction
		res.setCgst(roundToTwoDecimalPlaces(trns.getCgst()));
		res.setSgst(roundToTwoDecimalPlaces(trns.getSgst()));
		res.setSubtotal(roundToTwoDecimalPlaces(trns.getAmount()));
		res.setTotal(roundToTwoDecimalPlaces(trns.getNetamount()));

		BigDecimal b = new BigDecimal(String.valueOf(trns.getNetamount()));
		res.setAmountword(TAUtils.convert(b));

		return res;
	}

	@Override
	public InvoiceServiceResponse getNewTry(InvoiceTransDetilesRequest req) {

		InvoiceServiceResponse res = new InvoiceServiceResponse();
		// subaua
		List<SubAuaUser> sublist = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());
		SubAuaUser user = sublist.get(0);

		// api key
		List<SubAuaAPI> apikey = subAuaAPIRepo.findBySubAuaId(req.getSubAuaId());
		SubAuaAPI api = apikey.get(0);

		// paymentpalans
		List<PaymentPlanDetails> paydet = paymentPlanDetailsRepo.findAll();
		PaymentPlanDetails pay = paydet.get(0);

		if (user.getModelTransaction().equals(pay.getPlanType())) {

			Timestamp startTimestamp = (Timestamp) api.getCreatedDate();
			System.out.println("time stamp" + startTimestamp);
			long millisecondsIn90Days = 90 * 24 * 60 * 60 * 1000L;
			long startMilliseconds = startTimestamp.getTime() + millisecondsIn90Days;
			Timestamp endTimestamp = new Timestamp(startMilliseconds);
			System.out.println("endTimestamp" + endTimestamp);

			Timestamp currentTimestamp = new Timestamp(new Date().getTime());

			// Check if 90 days have passed since the start timestamp
			if (currentTimestamp.after(endTimestamp)) {
				List<AUTHTransaction> authList = authTransactionRepo
						.findByAgencycodeAndRequestdateBetween(req.getSubAuaId(), startTimestamp, endTimestamp);
				List<EKYCTransaction> ekycList = ekycTransactionRepo
						.findByAgencycodeAndRequestdateBetween(req.getSubAuaId(), startTimestamp, endTimestamp);

				InvoiceSubTrans authSubTrans = new InvoiceSubTrans();
				InvoiceSubTrans ekycSubTrans = new InvoiceSubTrans();
				if (user.getModelTransaction().equals(paydet.get(0).getPlanType())) {
					if (user.getKuaFingerprint().equals(true) || user.getKuaOtp().equals(true)
							|| user.getKuaIris().equals(true) && pay.getAuthcationtype().equals("Ekyc")) {
						res.setEkycpertrans(pay.getEachtransamount().toString());
						res.setKua(ekycList.size() * pay.getEachtransamount());
						long ekyctrans = ekycList.size();
						res.setEkycTrns(ekyctrans);
						System.out.println("ekycList.size()" + ekycList.size());
					}
					if ((user.getAuaFingerprint().equals(true) || user.getAuaOtp().equals(true)
							|| user.getAuaIris().equals(true)) && pay.getAuthcationtype().equals("Auth")) {
						System.out.println("Authhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
						if (authList != null && !authList.isEmpty()) {
							res.setAuthpertrans(pay.getEachtransamount().toString());
							res.setAua(authList.size() * pay.getEachtransamount());
							res.setAuthTrns(authList.size());
						}
					}

					double auth = ekycList.size() * pay.getEachtransamount()
							+ authList.size() * pay.getEachtransamount();
					double cgst = auth * 0.09;
					double sgst = auth * 0.09;
					double netamount = auth + cgst + sgst;

					double authAuaAmount = 0.0;
					int authTransCount = 0;
					double ekycAuaAmount = 0.0;
					int ekycTransCount = 0;

					// Round amounts to 2 decimal places
					authAuaAmount = roundToTwoDecimalPlaces(authAuaAmount);
					ekycAuaAmount = roundToTwoDecimalPlaces(ekycAuaAmount);
					res.setCgst(roundToTwoDecimalPlaces(cgst));
					res.setSgst(roundToTwoDecimalPlaces(sgst));
					res.setSubtotal(roundToTwoDecimalPlaces(auth));
					res.setTotal(roundToTwoDecimalPlaces(netamount));

					BigDecimal b = new BigDecimal(String.valueOf(netamount));
					res.setAmountword(TAUtils.convert(b));

					NewInvoice newInvoice = new NewInvoice();

					// Map values from the response object to the new invoice entity
					newInvoice.setSubAuaId(req.getSubAuaId());
					newInvoice.setInvoiceNumber("#DSA" + TAUtils.generateInvoiceId(user.getId()));
					newInvoice.setGstNumber("GST123");
					newInvoice.setBillingcycle("Quarterly");
					newInvoice.setStatus("waiting for payment");
//					newInvoice.setRemarks("");
					newInvoice.setKuaamount(res.getKua());
					newInvoice.setAuaamount(res.getAua());
					newInvoice.setCgst(roundToTwoDecimalPlaces(cgst));
					newInvoice.setSgst(res.getSgst());
					newInvoice.setSubtotal(res.getSubtotal());
					newInvoice.setTotal(res.getTotal());
					newInvoice.setAuthTrns(res.getAuthTrns());
					newInvoice.setEkycTrns(res.getEkycTrns());
					newInvoice.setAmountword(res.getAmountword());

					// Save the new invoice entity
					newInvoiceRepo.save(newInvoice);

					return res;
				} else {
					// not seletcted plan type
				}

			} else {
				long daysLeft = (endTimestamp.getTime() - currentTimestamp.getTime()) / (24 * 60 * 60 * 1000);
//				res.setMessage("Invoice generation will take time. " + daysLeft
//						+ " days are left until 90 days from the start date.");
				throw new TaException(ErrorCode.INVOICE_GEN_VALID,
						"Invoice generation will take time " + daysLeft + " days from the start date.");
			}

		} else {

		}

		return null;
	}

	private double roundToTwoDecimalPlaces(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public InvoiceServiceResponse getNewInvoiceId(InvoiceTransDetilesRequest req) {
		List<SubAuaUser> sublist = subAuaUserRepo.findBySubAuaId(req.getSubAuaId());

		List<NewInvoice> newInvoice = newInvoiceRepo.findByInvoiceNumber(req.getInvoiceId());
		NewInvoice invoice = newInvoice.get(0);

		SubAuaUser user = sublist.get(0);

		System.out.println("user  user  user  user" + user);

		InvoiceServiceResponse res = new InvoiceServiceResponse();

		Date createDate = new Date();
		LocalDate localDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = localDate.format(formatter);
		res.setInvoiceDate(formattedDate);
		res.setAddress(user.getAddress());
		res.setGstNumber(" ");
		res.setManagementEmail(user.getManagementEmail());
		res.setManagementMobilenumber(user.getManagementMobilenumber());
		res.setOrganisationName(user.getOrganisationName());
		res.setManagementName(user.getManagementName());
		res.setPincode(user.getPincode());
		res.setSubAuaId(user.getSubAuaId());
		res.setInvoiceNumber("#DSA" + TAUtils.generateInvoiceId(user.getId()));
		res.setStatus(1);
		res.setBillingcycle("Quarterly");

		res.setInvoiceNumber(invoice.getInvoiceNumber());
		res.setAmountword(invoice.getAmountword());
		res.setAua(invoice.getAuaamount());
		res.setKua(invoice.getKuaamount());
		res.setAuthTrns(invoice.getAuthTrns());
		res.setEkycTrns(invoice.getEkycTrns());
		res.setCgst(invoice.getCgst());
		res.setSgst(invoice.getSgst());
		res.setSubtotal(invoice.getSubtotal());
		res.setTotal(invoice.getTotal());
		return res;
	}
}
