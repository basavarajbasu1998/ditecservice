package com.ta.ditec.services.serviceimpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.report.domain.AUTHTransaction;
import com.ta.ditec.services.report.domain.AuthOtp;
import com.ta.ditec.services.report.domain.EKYCTransaction;
import com.ta.ditec.services.report.repo.AUTHTransactionRepo;
import com.ta.ditec.services.report.repo.AgencyLogRepo;
import com.ta.ditec.services.report.repo.AuthOtpRepo;
import com.ta.ditec.services.report.repo.EKYCTransactionRepo;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.AllSubAuaAuthResponse;
import com.ta.ditec.services.response.AllSubAuaKuaResponse;
import com.ta.ditec.services.response.AuthTotalResponse;
import com.ta.ditec.services.response.SubAuaTransactionResponse;
import com.ta.ditec.services.response.TodayAuthResponse;
import com.ta.ditec.services.response.TodayEkycResponse;
import com.ta.ditec.services.service.AllSubAuaService;

@Service
public class AllSubAuaServiceImpl implements AllSubAuaService {

	private static final Logger logger = LoggerFactory.getLogger(AllSubAuaServiceImpl.class);
	@Autowired
	private AUTHTransactionRepo authTransactionRepo;

	@Autowired
	private EKYCTransactionRepo ekycTransactionRepo;

	@Autowired
	private AuthOtpRepo authOtpRepo;

	@Autowired
	private AgencyLogRepo agencyLogRepo;

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	@Override
	public AuthTotalResponse getAuthTotalResponse() {

		LocalDateTime todayfromDateTime = LocalDateTime.now();
		LocalDateTime todayendDateTime = LocalDateTime.now();

		LocalDateTime fromDateTime = LocalDateTime.now().minusYears(1);
		LocalDateTime endDateTime = LocalDateTime.now();

		Timestamp timestampstart = Timestamp.valueOf(fromDateTime);
		Timestamp timestampend = Timestamp.valueOf(endDateTime);
		timestampstart.setHours(0);
		timestampstart.setMinutes(0);
		timestampstart.setSeconds(0);
		timestampstart.setNanos(0);
		timestampend.setHours(23);
		timestampend.setMinutes(59);
		timestampend.setMinutes(59);
		timestampend.setNanos(000);

		Timestamp todaytimestampstart = Timestamp.valueOf(todayfromDateTime);
		Timestamp toadytimestampend = Timestamp.valueOf(todayendDateTime);
		todaytimestampstart.setHours(0);
		todaytimestampstart.setMinutes(0);
		todaytimestampstart.setSeconds(0);
		todaytimestampstart.setNanos(0);
		toadytimestampend.setHours(23);
		toadytimestampend.setMinutes(59);
		toadytimestampend.setMinutes(59);
		toadytimestampend.setNanos(000);

		List<AUTHTransaction> authlist = authTransactionRepo.findByRequestdateBetween(timestampstart, timestampend);
		logger.info("Data fecthed from database:" + authlist.toString());
		List<EKYCTransaction> ekyclist = ekycTransactionRepo.findByRequestdateBetween(timestampstart, timestampend);

		List<AuthOtp> authOtplist = authOtpRepo.findByRequestdateBetweenAndErrorcodeAndErrormsg(timestampstart,
				timestampend, "000", "Success");

		logger.info("Data fecthed from database:" + ekyclist.toString());
		logger.info("Data fecthed from database:");

		List<AUTHTransaction> todayauthlist = authTransactionRepo.findByRequestdateBetween(todaytimestampstart,
				toadytimestampend);

		logger.info("Data fecthed from database:" + todayauthlist.toString());
		List<EKYCTransaction> todayekyclist = ekycTransactionRepo.findByRequestdateBetween(todaytimestampstart,
				toadytimestampend);

		List<AuthOtp> todayauthOtplist = authOtpRepo.findByRequestdateBetweenAndErrorcodeAndErrormsg(
				todaytimestampstart, toadytimestampend, "000", "Success");

		logger.info("Data fetched from database:" + todayekyclist.toString());

//		List<AgencyLog> subaua = agencyLogRepo.findAll();
		List<SubAuaUser> subaua = subAuaUserRepo.findAll();

		logger.info("Data fetched from database:" + subaua.toString());

		Long AllAuthData = authlist.stream().count();
		Long AllEKYCData = ekyclist.stream().count();
		Long AllOtpData = authOtplist.stream().count();

		Long todayAllAuthData = todayauthlist.stream().count();
		Long todayAllEKYCData = todayekyclist.stream().count();
		Long todayAllOTPData = todayauthOtplist.stream().count();
		Long yeartotal = AllAuthData + AllEKYCData + AllOtpData;
		Long todaytotal = todayAllAuthData + todayAllEKYCData + todayAllOTPData;

//		Long subaua = authlist.stream().map(X -> X.getAgencycode()).distinct().count();

		System.out.println(subaua);

		AuthTotalResponse res = new AuthTotalResponse();
		res.setYearauthtotal(yeartotal);
		res.setTodayauthtotal(todaytotal);
		res.setSubaua((long) subaua.size());
		System.out.println("hitting dbbbbb");
		logger.debug(res.toString());
		return res;

	}

	@Override
	public TodayAuthResponse getYearAuthResponse(SubAuaUserRequest req) {
		if (req.getSubAuaId().equals("admin")) {
			LocalDateTime todayfromDateTime = LocalDateTime.now().minusYears(1);
			LocalDateTime todayendDateTime = LocalDateTime.now();
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
			List<AUTHTransaction> authotplist = authTransactionRepo.findByRequestdateBetween(yeartimestampstart,
					yeartimestampend);

			logger.info("data fetched from database:");
			Long otp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();

			Long bio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
//			Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;

			Long demo = otp + pa + pfa + pi;
			System.out.println(otp + "otp" + pa + "pa" + pfa + "pfa" + pi + "pi" + bio + "bio");
			TodayAuthResponse res = new TodayAuthResponse();
			List<Long> counts = Arrays.asList(demo, bio, iris);
			List<String> labels = Arrays.asList("Demogrphic", "Fingerprint", "Iris");
			res.setAuthcount(counts);
			res.setAuthnames(labels);
			logger.debug(res.toString());
			return res;

		} else {
			LocalDateTime todayfromDateTime = LocalDateTime.now().minusYears(1);
			LocalDateTime todayendDateTime = LocalDateTime.now();
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
			List<AUTHTransaction> authotplist = authTransactionRepo
					.findByRequestdateBetweenAndAgencycode(yeartimestampstart, yeartimestampend, req.getSubAuaId());

			Long otp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
			Long bio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();

//			Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;

			Long demo = otp + pa + pfa + pi;
			System.out.println(otp + "otp" + pa + "pa" + pfa + "pfa" + pi + "pi" + bio + "bio");
			TodayAuthResponse res = new TodayAuthResponse();

			List<Long> counts = Arrays.asList(demo, bio, iris);
			List<String> labels = Arrays.asList("Demogrphic", "Fingerprint", "Iris");
			res.setAuthcount(counts);
			res.setAuthnames(labels);

			logger.debug(res.toString());
			return res;
		}

	}

	@Override
	public TodayEkycResponse getYearKycResponse(SubAuaUserRequest req) {
		if (req.getSubAuaId().equals("admin")) {
			LocalDateTime todayfromDateTime = LocalDateTime.now().minusYears(1);
			LocalDateTime todayendDateTime = LocalDateTime.now();
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
			List<EKYCTransaction> authotplist = ekycTransactionRepo.findByRequestdateBetween(yeartimestampstart,
					yeartimestampend);
			Long otp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();

			Long bio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();

//			Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;

			Long demo = otp + pa + pfa + pi;
			System.out.println(otp + "otp" + pa + "pa" + pfa + "pfa" + pi + "pi" + bio + "bio");

			List<Long> counts = Arrays.asList(demo, bio, iris);
			List<String> labels = Arrays.asList("OTP", "Fingerprint", "Iris");
			TodayEkycResponse res = new TodayEkycResponse();
			res.setEkyccount(counts);
			res.setEkycnames(labels);
			logger.debug(res.toString());
			return res;

		} else {

			LocalDateTime todayfromDateTime = LocalDateTime.now().minusYears(1);
			LocalDateTime todayendDateTime = LocalDateTime.now();
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
			List<EKYCTransaction> authotplist = ekycTransactionRepo
					.findByRequestdateBetweenAndAgencycode(yeartimestampstart, yeartimestampend, req.getSubAuaId());
			Long otp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
			Long bio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
//			Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;
			Long demo = otp + pa + pfa + pi;
			System.out.println(otp + "otp" + pa + "pa" + pfa + "pfa" + pi + "pi" + bio + "bio");

			List<Long> counts = Arrays.asList(demo, bio, iris);
			List<String> labels = Arrays.asList("OTP", "Fingerprint", "Iris");
			TodayEkycResponse res = new TodayEkycResponse();
			res.setEkyccount(counts);
			res.setEkycnames(labels);
			logger.debug(res.toString());
			return res;
		}

	}

	@Override
	public TodayAuthResponse getTodayAuthResponse(SubAuaUserRequest req) {
		if (req.getSubAuaId().equals("admin")) {
			LocalDateTime todayfromDateTime = LocalDateTime.now();
			LocalDateTime todayendDateTime = LocalDateTime.now();
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
			List<AUTHTransaction> authotplist = authTransactionRepo.findByRequestdateBetween(yeartimestampstart,
					yeartimestampend);
			Long otp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();

			Long bio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
//			Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;

			Long demo = otp + pa + pfa + pi;
			System.out.println(otp + "otp" + pa + "pa" + pfa + "pfa" + pi + "pi" + bio + "bio");
			TodayAuthResponse res = new TodayAuthResponse();
			List<Long> counts = Arrays.asList(demo, bio, iris);
			List<String> labels = Arrays.asList("Demogrphic", "Fingerprint", "Iris");
			res.setAuthcount(counts);
			res.setAuthnames(labels);
			logger.debug(res.toString());
			return res;

		} else {

			LocalDateTime todayfromDateTime = LocalDateTime.now();
			LocalDateTime todayendDateTime = LocalDateTime.now();
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
			List<AUTHTransaction> authotplist = authTransactionRepo
					.findByRequestdateBetweenAndAgencycode(yeartimestampstart, yeartimestampend, req.getSubAuaId());
			Long otp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
			Long bio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
//			Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;
			Long demo = otp + pa + pfa + pi;
			System.out.println(otp + "otp" + pa + "pa" + pfa + "pfa" + pi + "pi" + bio + "bio");
			TodayAuthResponse res = new TodayAuthResponse();
			List<Long> counts = Arrays.asList(demo, bio, iris);
			List<String> labels = Arrays.asList("Demogrphic", "Fingerprint", "Iris");
			res.setAuthcount(counts);
			res.setAuthnames(labels);
			logger.debug(res.toString());
			return res;
		}
	}

	@Override
	public TodayEkycResponse getTodayKycResponse(SubAuaUserRequest req) {
		if (req.getSubAuaId().equals("admin")) {
			LocalDateTime todayfromDateTime = LocalDateTime.now();
			LocalDateTime todayendDateTime = LocalDateTime.now();
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
			List<EKYCTransaction> authotplist = ekycTransactionRepo.findByRequestdateBetween(yeartimestampstart,
					yeartimestampend);
			Long otp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
			Long bio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
//			Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;
			Long demo = otp + pa + pfa + pi;

			System.out.println(otp + "otp" + pa + "pa" + pfa + "pfa" + pi + "pi" + bio + "bio");

			List<Long> counts = Arrays.asList(demo, bio, iris);
			List<String> labels = Arrays.asList("OTP", "Fingerprint", "Iris");
			System.out.println(counts);
			System.out.println(labels);
			TodayEkycResponse res = new TodayEkycResponse();
			res.setEkyccount(counts);
			res.setEkycnames(labels);
			logger.debug(res.toString());
			return res;

		} else {

			LocalDateTime todayfromDateTime = LocalDateTime.now();
			LocalDateTime todayendDateTime = LocalDateTime.now();
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
			List<EKYCTransaction> authotplist = ekycTransactionRepo
					.findByRequestdateBetweenAndAgencycode(yeartimestampstart, yeartimestampend, req.getSubAuaId());
			Long otp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
			Long bio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();

//			Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;
			Long demo = otp + pa + pfa + pi;

			System.out.println(otp + "otp" + pa + "pa" + pfa + "pfa" + pi + "pi" + bio + "bio");
			List<Long> counts = Arrays.asList(demo, bio, iris);
			List<String> labels = Arrays.asList("OTP", "Fingerprint", "Iris");
			TodayEkycResponse res = new TodayEkycResponse();
			res.setEkyccount(counts);
			res.setEkycnames(labels);
			logger.debug(res.toString());
			return res;
		}
	}

	@Override
	public Map<String, Object> getTodaysubauaAuthResponseForEachAgency(SubAuaUserRequest req) {
		LocalDateTime todayfromDateTime = LocalDateTime.now();
		LocalDateTime todayendDateTime = LocalDateTime.now();
		Timestamp todaytimestampstart = Timestamp.valueOf(todayfromDateTime);
		Timestamp toadytimestampend = Timestamp.valueOf(todayendDateTime);
		todaytimestampstart.setHours(0);
		todaytimestampstart.setMinutes(0);
		todaytimestampstart.setSeconds(0);
		todaytimestampstart.setNanos(0);
		toadytimestampend.setHours(23);
		toadytimestampend.setMinutes(59);
		toadytimestampend.setSeconds(59);
		toadytimestampend.setNanos(0);

		List<String> agencyCodes = getAuthUniqueAgencyCodes(authTransactionRepo.findAll());
		AllSubAuaAuthResponse[] respArray = new AllSubAuaAuthResponse[agencyCodes.size()];

		int index = 0;

		List<AllSubAuaAuthResponse> resList = new ArrayList<>();
		List<Long> demoData = new ArrayList<>();
		List<Long> fingerprintData = new ArrayList<>();
		List<Long> irisData = new ArrayList();
		List<String> agencyNames = new ArrayList();

		for (String authagencyCode : agencyCodes) {
			List<AUTHTransaction> todayauthotplist = authTransactionRepo
					.findByRequestdateBetweenAndAgencycode(todaytimestampstart, toadytimestampend, authagencyCode);

			Long otp = todayauthotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = todayauthotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = todayauthotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = todayauthotplist.stream().filter(x -> x.getPi().equals("y")).count();

			Long bio = todayauthotplist.stream().filter(x -> x.getBio().equals("y")).count();

//			Long iris = todayauthotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;

			Long demo = otp + pa + pfa + pi;

			AllSubAuaAuthResponse res = new AllSubAuaAuthResponse();
			res.setAuthdemographic(demo);
			res.setAuthfingerprint(bio);
			res.setAuthiris(iris);
			res.setSubAuaId(authagencyCode);

			logger.debug(res.toString());
			resList.add(res);

			demoData.add(demo);
			fingerprintData.add(bio);
			irisData.add(iris);
			agencyNames.add(authagencyCode);
		}

		Map<String, Object> responseMap = new LinkedHashMap<>();
		List<Map<String, Object>> data = new ArrayList<>();

		data.add(createDataMap("Demographic", demoData));
		data.add(createDataMap("Fingerprint", fingerprintData));
		data.add(createDataMap("Iris", irisData));

		responseMap.put("data", data);
		responseMap.put("agencyname", agencyNames);

		return responseMap;
	}

	private Map<String, Object> createDataMap(String name, List<Long> data) {
		Map<String, Object> dataMap = new LinkedHashMap<>();
		dataMap.put("name", name);
		dataMap.put("data", data);
		return dataMap;
	}

	private List<String> getAuthUniqueAgencyCodes(List<AUTHTransaction> authTransactions) {
		return authTransactions.stream().map(AUTHTransaction::getAgencycode).distinct().collect(Collectors.toList());
	}

	@Override
	public Map<String, Object> getTodaysubauaKuaResponseForEachAgency(SubAuaUserRequest req) {
		LocalDateTime todayfromDateTime = LocalDateTime.now();
		LocalDateTime todayendDateTime = LocalDateTime.now();
		Timestamp todaytimestampstart = Timestamp.valueOf(todayfromDateTime);
		Timestamp toadytimestampend = Timestamp.valueOf(todayendDateTime);
		todaytimestampstart.setHours(0);
		todaytimestampstart.setMinutes(0);
		todaytimestampstart.setSeconds(0);
		todaytimestampstart.setNanos(0);
		toadytimestampend.setHours(23);
		toadytimestampend.setMinutes(59);
		toadytimestampend.setSeconds(59);
		toadytimestampend.setNanos(0);

		List<String> agencyCodes = getKuaUniqueAgencyCodes(ekycTransactionRepo.findAll());
		AllSubAuaKuaResponse[] respArray = new AllSubAuaKuaResponse[agencyCodes.size()];
		int index = 0;

		List<AllSubAuaKuaResponse> resList = new ArrayList<>();
		List<Long> demoData = new ArrayList<>();
		List<Long> fingerprintData = new ArrayList<>();
		List<Long> irisData = new ArrayList();
		List<String> agencyNames = new ArrayList();

		for (String kuaAgencyCode : agencyCodes) {
			List<EKYCTransaction> todayKuaList = ekycTransactionRepo
					.findByRequestdateBetweenAndAgencycode(todaytimestampstart, toadytimestampend, kuaAgencyCode);
			Long otp = todayKuaList.stream().filter(x -> x.getOtp().equals("y")).count();
			Long pa = todayKuaList.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = todayKuaList.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = todayKuaList.stream().filter(x -> x.getPi().equals("y")).count();
			Long bio = todayKuaList.stream().filter(x -> x.getBio().equals("y")).count();

//			Long iris = todayKuaList.stream().filter(x -> x.getBio().equals("y")).count();
			Long iris = 0l;

			Long demo = otp + pa + pfa + pi;

			AllSubAuaKuaResponse res = new AllSubAuaKuaResponse();
			res.setKuademographic(demo);
			res.setKuafingerprint(bio);
			res.setKuairis(iris);
			res.setSubAuaId(kuaAgencyCode);
			respArray[index++] = res;

			demoData.add(demo);
			fingerprintData.add(bio);
			irisData.add(iris);
			agencyNames.add(kuaAgencyCode);
		}
		Map<String, Object> responseMap = new LinkedHashMap<>();
		List<Map<String, Object>> data = new ArrayList<>();

		data.add(createDataMap("Demographic", demoData));
		data.add(createDataMap("Fingerprint", fingerprintData));
		data.add(createDataMap("Iris", irisData));

		responseMap.put("data", data);
		responseMap.put("agencyname", agencyNames);

		return responseMap;

	}

	private List<String> getKuaUniqueAgencyCodes(List<EKYCTransaction> authTransaction) {
		return authTransaction.stream().map(EKYCTransaction::getAgencycode).distinct().collect(Collectors.toList());
	}

//	@Override
//	public Map<String, Object> getTotalsubauaKuaResponseForEachAgency() {
//
////		LocalDateTime todayfromDateTime = LocalDateTime.of(2023, Month.JULY, 1, 0, 0);
//		LocalDateTime todayfromDateTime = LocalDateTime.now();
//		LocalDateTime todayendDateTime = LocalDateTime.now();
//		Timestamp todaytimestampstart = Timestamp.valueOf(todayfromDateTime);
//		Timestamp toadytimestampend = Timestamp.valueOf(todayendDateTime);
//		todaytimestampstart.setHours(0);
//		todaytimestampstart.setMinutes(0);
//		todaytimestampstart.setSeconds(0);
//		todaytimestampstart.setNanos(0);
//		toadytimestampend.setHours(23);
//		toadytimestampend.setMinutes(59);
//		toadytimestampend.setSeconds(59);
//		toadytimestampend.setNanos(0);
//
//		List<String> agencyCodes = getKuaUniqueAgencyCodes(ekycTransactionRepo.findAll());
//		
//		
//		AllSubAuaKuaResponse[] respArray = new AllSubAuaKuaResponse[agencyCodes.size()];
//		int index = 0;
//
//		List<AllSubAuaKuaResponse> resList = new ArrayList<>();
//		List<Long> demoData = new ArrayList<>();
//		List<Long> fingerprintData = new ArrayList<>();
//		List<Long> irisData = new ArrayList();
//		List<String> agencyNames = new ArrayList();
//
//		for (String kuaAgencyCode : agencyCodes) {
//			List<EKYCTransaction> todayKuaList = ekycTransactionRepo
//					.findByRequestdateBetweenAndAgencycode(todaytimestampstart, toadytimestampend, kuaAgencyCode);
//
//			List<AUTHTransaction> authotplist = authTransactionRepo
//					.findByRequestdateBetweenAndAgencycode(todaytimestampstart, toadytimestampend, kuaAgencyCode);
//
////			Long otpbio = todayKuaList.stream().filter(x -> x.getBiodivise().equals("OTP")).count();
////			Long fmr = todayKuaList.stream().filter(x -> x.getBiodivise().equals("FMR")).count();
////			Long iir = todayKuaList.stream().filter(x -> x.getBiodivise().equals("IIR")).count();
////
////			Long otpbioauth = authotplist.stream().filter(x -> {
////				String biodevise = x.getBiodevise();
////				return biodevise != null && biodevise.equals("FMR_OTP");
////			}).count();
////
////			Long bioauth = authotplist.stream().filter(x -> {
////				String biodevise = x.getBiodevise();
////				return biodevise != null && biodevise.equals("FMR");
////			}).count();
////
////			Long otpauth = authotplist.stream().filter(x -> {
////				String biodevise = x.getBiodevise();
////				return biodevise != null && biodevise.equals("OTP");
////			}).count();
//
////			System.out.println("otp" + otpbio + "fmr" + fmr + "iir" + iir);
//
////			System.out.println("otpbioauth" + otpbioauth + "bioauth" + bioauth + "otpauth" + otpauth);
//
////			Long count = otpbio + fmr + iir + otpbioauth + bioauth + otpauth;
////
////			System.out.println("countttttttttttttttttttttttttttttttttttttttttttttttttttttt" + count);
//
//			Long ekycotp = todayKuaList.stream().filter(x -> x.getOtp().equals("y")).count();
//			Long ekycpa = todayKuaList.stream().filter(x -> x.getPa().equals("y")).count();
//			Long ekycpfa = todayKuaList.stream().filter(x -> x.getPfa().equals("y")).count();
//			Long ekycpi = todayKuaList.stream().filter(x -> x.getPi().equals("y")).count();
//			Long ekycbio = todayKuaList.stream().filter(x -> x.getBio().equals("y")).count();
//			Long ekyciris = todayKuaList.stream().filter(x -> x.getBio().equals("y")).count();
//
//			Long authotp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
//			Long authpa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
//			Long authpfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
//			Long authpi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
//			Long authbio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
//			Long authiris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
//
//			Long authdemo = authotp + authpa + authpfa + authpi;
//			Long ekycdemo = ekycotp + ekycpa + ekycpfa + ekycpi;
//
//			Long demo = authdemo + ekycdemo;
//			Long bio = authbio + ekycbio;
//			Long iris = 0l;
//			List<SubAuaUser> subaua = subAuaUserRepo.findBySubAuaId(kuaAgencyCode);
//
////			Long demo = otpauth + otpbio;
////			Long bio = fmr + bioauth + otpbioauth;
////			Long iris = iir;
//
//			AllSubAuaKuaResponse res = new AllSubAuaKuaResponse();
//			res.setKuademographic(demo);
//			res.setKuafingerprint(bio);
//			res.setKuairis(iris);
//			res.setSubAuaId(subaua.get(index).getOrganisationName());
//			respArray[index++] = res;
//
//			demoData.add(demo);
//			fingerprintData.add(bio);
//			irisData.add(iris);
//			agencyNames.add(subaua.get(index).getOrganisationName());
//		}
//		Map<String, Object> responseMap = new LinkedHashMap<>();
//		List<Map<String, Object>> data = new ArrayList<>();
//
//		data.add(createDataMap("Demographic", demoData));
//		data.add(createDataMap("Fingerprint", fingerprintData));
//		data.add(createDataMap("Iris", irisData));
//
//		responseMap.put("data", data);
//		responseMap.put("agencyname", agencyNames);
//
//		return responseMap;
//
//	}

	@Override
	public Map<String, Object> getTodayTotalsubauaKuaResponseForEachAgency() {
		LocalDateTime todayfromDateTime = LocalDateTime.now();
		LocalDateTime todayendDateTime = LocalDateTime.now();
		Timestamp todaytimestampstart = Timestamp.valueOf(todayfromDateTime);
		Timestamp toadytimestampend = Timestamp.valueOf(todayendDateTime);
		todaytimestampstart.setHours(0);
		todaytimestampstart.setMinutes(0);
		todaytimestampstart.setSeconds(0);
		todaytimestampstart.setNanos(0);
		toadytimestampend.setHours(23);
		toadytimestampend.setMinutes(59);
		toadytimestampend.setSeconds(59);
		toadytimestampend.setNanos(0);

		List<String> agencyCodes = getKuaUniqueAgencyCodes(ekycTransactionRepo.findAll());

		List<Map<String, Long>> data = new ArrayList<>();
		List<String> agencyNames = new ArrayList<>();

		for (String kuaAgencyCode : agencyCodes) {
			List<EKYCTransaction> todayKuaList = ekycTransactionRepo
					.findByRequestdateBetweenAndAgencycode(todaytimestampstart, toadytimestampend, kuaAgencyCode);
			List<AUTHTransaction> authotplist = authTransactionRepo
					.findByRequestdateBetweenAndAgencycode(todaytimestampstart, toadytimestampend, kuaAgencyCode);

			Long ekycotp = todayKuaList.stream().filter(x -> x.getOtp().equals("y")).count();
			Long ekycpa = todayKuaList.stream().filter(x -> x.getPa().equals("y")).count();
			Long ekycpfa = todayKuaList.stream().filter(x -> x.getPfa().equals("y")).count();
			Long ekycpi = todayKuaList.stream().filter(x -> x.getPi().equals("y")).count();
			Long ekycbio = todayKuaList.stream().filter(x -> x.getBio().equals("y")).count();
			Long ekyciris = todayKuaList.stream().filter(x -> x.getBio().equals("y")).count();

			Long authotp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long authpa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long authpfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long authpi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
			Long authbio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long authiris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();

			Long authdemo = authotp + authpa + authpfa + authpi;
			Long ekycdemo = ekycotp + ekycpa + ekycpfa + ekycpi;

			Long demo = authdemo + ekycdemo;
			Long bio = authbio + ekycbio;
			Long iris = 0L; // You might want to update this value accordingly

			Map<String, Long> agencyData = new LinkedHashMap<>();
			agencyData.put("Demographic", demo);
			agencyData.put("Fingerprint", bio);
			agencyData.put("Iris", iris);

			data.add(agencyData);

			SubAuaUser subaua = subAuaUserRepo.getSubAuaId(kuaAgencyCode);
			agencyNames.add(kuaAgencyCode);
		}

		Map<String, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("data", data);
		responseMap.put("agencyname", agencyNames);

		return responseMap;
	
	}

	@Override
	public SubAuaTransactionResponse getAuthTotalRequest(SubAuaUserRequest req) {
//		LocalDateTime todayfromDateTime = LocalDateTime.now();
//		LocalDateTime todayendDateTime = LocalDateTime.now();
		LocalDateTime fromDateTime = LocalDateTime.now().minusYears(1);
		LocalDateTime endDateTime = LocalDateTime.now();

		Timestamp timestampstart = Timestamp.valueOf(fromDateTime);
		Timestamp timestampend = Timestamp.valueOf(endDateTime);
		timestampstart.setHours(0);
		timestampstart.setMinutes(0);
		timestampstart.setSeconds(0);
		timestampstart.setNanos(0);
		timestampend.setHours(23);
		timestampend.setMinutes(59);
		timestampend.setMinutes(59);
		timestampend.setNanos(000);

//		Timestamp todaytimestampstart = Timestamp.valueOf(todayfromDateTime);
//		Timestamp toadytimestampend = Timestamp.valueOf(todayendDateTime);
//		todaytimestampstart.setHours(0);
//		todaytimestampstart.setMinutes(0);
//		todaytimestampstart.setSeconds(0);
//		todaytimestampstart.setNanos(0);
//		toadytimestampend.setHours(23);
//		toadytimestampend.setMinutes(59);
//		toadytimestampend.setMinutes(59);
//		toadytimestampend.setNanos(000);

		List<AUTHTransaction> authlist = authTransactionRepo.findByRequestdateBetweenAndAgencycode(timestampstart,
				timestampend, req.getSubAuaId());
		List<EKYCTransaction> ekyclist = ekycTransactionRepo.findByRequestdateBetweenAndAgencycode(timestampstart,
				timestampend, req.getSubAuaId());

		Long AllAuthData = authlist.stream().count();
		Long AllEKYCData = ekyclist.stream().count();
		Long total = AllAuthData + AllEKYCData;
		SubAuaTransactionResponse res = new SubAuaTransactionResponse();

		res.setKuacount(AllEKYCData);
		res.setAuacount(AllAuthData);
		res.setTotal(total);

		System.out.println("resssssssssssssssssssssssssssssssssssss" + res);

		logger.debug(res.toString());
		return res;
	}

	@Override
	public TodayAuthResponse getSubAuaWiseTodayAuthResponse(SubAuaUserRequest req) {

		LocalDateTime todayfromDateTime = LocalDateTime.of(2023, 3, 21, 12, 30, 0);
		LocalDateTime todayendDateTime = LocalDateTime.now();
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
		List<AUTHTransaction> authotplist = authTransactionRepo
				.findByRequestdateBetweenAndAgencycode(yeartimestampstart, yeartimestampend, req.getSubAuaId());

		List<EKYCTransaction> ekycauthotplist = ekycTransactionRepo
				.findByRequestdateBetweenAndAgencycode(yeartimestampstart, yeartimestampend, req.getSubAuaId());

		Long authotp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
		Long authpa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
		Long authpfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
		Long authpi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
		Long authbio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();

//		Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
		Long authiris = 0l;
		Long authdemo = authotp + authpa + authpfa + authpi;

		System.out.println(authotp + authpa + authpfa + authpi + authbio);

		Long ekycotp = ekycauthotplist.stream().filter(x -> x.getOtp().equals("y")).count();
		Long ekycpa = ekycauthotplist.stream().filter(x -> x.getPa().equals("y")).count();
		Long ekycpfa = ekycauthotplist.stream().filter(x -> x.getPfa().equals("y")).count();
		Long ekycpi = ekycauthotplist.stream().filter(x -> x.getPi().equals("y")).count();
		Long ekycbio = ekycauthotplist.stream().filter(x -> x.getBio().equals("y")).count();

//		Long iris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
		Long ekyciris = 0l;
		Long ekycdemo = authotp + authpa + authpfa + authpi;

		System.out.println(authotp + authpa + authpfa + authpi + ekycbio);

		Long demo = ekycdemo + authdemo;
		Long bio = authbio + ekycbio;
		Long iris = 0l;

		TodayAuthResponse res = new TodayAuthResponse();
		List<Long> counts = Arrays.asList(demo, bio, iris);
		List<String> labels = Arrays.asList("Demogrphic", "Fingerprint", "Iris");
		res.setAuthcount(counts);
		res.setAuthnames(labels);
		logger.debug(res.toString());
		System.out.println(counts + " " + labels);
		return res;

	}

	@Override
	public Map<String, Object> getTotalsubauaKuaResponseForEachAgency() {
		LocalDateTime todayfromDateTime = LocalDateTime.of(2023, Month.JULY, 1, 0, 0);
		LocalDateTime todayendDateTime = LocalDateTime.now();
		Timestamp todaytimestampstart = Timestamp.valueOf(todayfromDateTime);
		Timestamp toadytimestampend = Timestamp.valueOf(todayendDateTime);
		todaytimestampstart.setHours(0);
		todaytimestampstart.setMinutes(0);
		todaytimestampstart.setSeconds(0);
		todaytimestampstart.setNanos(0);
		toadytimestampend.setHours(23);
		toadytimestampend.setMinutes(59);
		toadytimestampend.setSeconds(59);
		toadytimestampend.setNanos(0);

		List<String> agencyCodes = getKuaUniqueAgencyCodes(ekycTransactionRepo.findAll());

		List<Map<String, Long>> data = new ArrayList<>();
		List<String> agencyNames = new ArrayList<>();

		for (String kuaAgencyCode : agencyCodes) {
			List<EKYCTransaction> todayKuaList = ekycTransactionRepo
					.findByRequestdateBetweenAndAgencycode(todaytimestampstart, toadytimestampend, kuaAgencyCode);
			List<AUTHTransaction> authotplist = authTransactionRepo
					.findByRequestdateBetweenAndAgencycode(todaytimestampstart, toadytimestampend, kuaAgencyCode);

			Long ekycotp = todayKuaList.stream().filter(x -> x.getOtp().equals("y")).count();
			Long ekycpa = todayKuaList.stream().filter(x -> x.getPa().equals("y")).count();
			Long ekycpfa = todayKuaList.stream().filter(x -> x.getPfa().equals("y")).count();
			Long ekycpi = todayKuaList.stream().filter(x -> x.getPi().equals("y")).count();
			Long ekycbio = todayKuaList.stream().filter(x -> x.getBio().equals("y")).count();
			Long ekyciris = todayKuaList.stream().filter(x -> x.getBio().equals("y")).count();

			Long authotp = authotplist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long authpa = authotplist.stream().filter(x -> x.getPa().equals("y")).count();
			Long authpfa = authotplist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long authpi = authotplist.stream().filter(x -> x.getPi().equals("y")).count();
			Long authbio = authotplist.stream().filter(x -> x.getBio().equals("y")).count();
			Long authiris = authotplist.stream().filter(x -> x.getBio().equals("y")).count();

			Long authdemo = authotp + authpa + authpfa + authpi;
			Long ekycdemo = ekycotp + ekycpa + ekycpfa + ekycpi;

			Long demo = authdemo + ekycdemo;
			Long bio = authbio + ekycbio;
			Long iris = 0L; // You might want to update this value accordingly

			Map<String, Long> agencyData = new LinkedHashMap<>();
			agencyData.put("Demographic", demo);
			agencyData.put("Fingerprint", bio);
			agencyData.put("Iris", iris);

			data.add(agencyData);

			SubAuaUser subaua = subAuaUserRepo.getSubAuaId(kuaAgencyCode);
			agencyNames.add(kuaAgencyCode);
		}

		Map<String, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("data", data);
		responseMap.put("agencyname", agencyNames);

		return responseMap;
	}

}
