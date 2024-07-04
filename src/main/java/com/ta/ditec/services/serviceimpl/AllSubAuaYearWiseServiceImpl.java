package com.ta.ditec.services.serviceimpl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.report.domain.AUTHTransaction;
import com.ta.ditec.services.report.domain.AuthOtp;
import com.ta.ditec.services.report.domain.EKYCTransaction;
import com.ta.ditec.services.report.repo.AUTHTransactionRepo;
import com.ta.ditec.services.report.repo.AuthOtpRepo;
import com.ta.ditec.services.report.repo.EKYCTransactionRepo;
import com.ta.ditec.services.request.DitecReportsRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.AuaAuthMonthlyResponse;
import com.ta.ditec.services.response.AuaEKYCMonthlyResponse;
import com.ta.ditec.services.response.LatestTransResponse;
import com.ta.ditec.services.response.SubAuaMonthlyResponse;
import com.ta.ditec.services.response.YearWiseAllAuthResponse;
import com.ta.ditec.services.service.AllSubAuaYearWiseService;

@Service
public class AllSubAuaYearWiseServiceImpl implements AllSubAuaYearWiseService {

	private static final Logger logger = LoggerFactory.getLogger(AllSubAuaYearWiseServiceImpl.class);

	@Autowired
	private AUTHTransactionRepo authTransactionRepo;

	@Autowired
	private EKYCTransactionRepo ekycTransactionRepo;

	@Autowired
	private AuthOtpRepo authOtpRepo;

	@Override
	public YearWiseAllAuthResponse getYearWiseAllAuthResponse(SubAuaUserRequest req) {

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

			List<AUTHTransaction> authlist = authTransactionRepo.findByRequestdateBetween(yeartimestampstart,
					yeartimestampend);
			logger.info("Data fetched from database");
			logger.debug(authlist.toString());
			List<EKYCTransaction> ekyclist = ekycTransactionRepo.findByRequestdateBetween(yeartimestampstart,
					yeartimestampend);

			List<AuthOtp> authOtplist = authOtpRepo.findByRequestdateBetweenAndErrorcodeAndErrormsg(yeartimestampstart,
					yeartimestampend, "000", "Success");

			logger.info("Data fetched from database");

			logger.debug(ekyclist.toString());

			Long AllAuthData = authlist.stream().count();
			Long AllEKYCData = ekyclist.stream().count();
			Long AllOtpData = authOtplist.stream().count();

			Long yeartotal = AllAuthData + AllEKYCData + AllOtpData;

			System.out.println("AllAuthData :  " + AllAuthData + "AllEKYCData  :  " + AllEKYCData);

			YearWiseAllAuthResponse res = new YearWiseAllAuthResponse();
			res.setYearwisetotal(yeartotal);
			res.setYearwiseekyctotal(AllEKYCData);
			res.setYearwiseauthtotal(AllAuthData + AllOtpData);
			logger.debug(res.toString());

			Long authbio = authlist.stream().filter(x -> x.getBio().equals("y")).count();
			Long pa = authlist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authlist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authlist.stream().filter(x -> x.getPi().equals("y")).count();
			Long otp = authlist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long demo = pa + pfa + pi + otp;

			System.out.println("authbio  :  " + authbio + "  demo :" + demo);

			Long ekycbio = ekyclist.stream().filter(x -> x.getBio().equals("y")).count();
			Long ekycotp = ekyclist.stream().filter(x -> x.getOtp().equals("y")).count();
			res.setAuthbio(authbio);
			res.setAuthdemo(demo);
			res.setAuthiris((long) 0);
			res.setEkycbio(ekycbio);
			res.setEkycotp(ekycotp);
			res.setEkyciris((long) 0);

//			Long ekycotp = ekyclist.stream().filter(x -> x.getBiodivise().equals("OTP")).count();
//			Long ekycfmr = ekyclist.stream().filter(x -> x.getBiodivise().equals("FMR")).count();
//			Long ekyciir = ekyclist.stream().filter(x -> x.getBiodivise().equals("IIR")).count();
//
//			Long otpbioauth = authlist.stream().filter(x -> {
//				String biodevise = x.getBiodevise();
//				return biodevise != null && biodevise.equals("FMR_OTP");
//			}).count();
//
//			Long bioauth = authlist.stream().filter(x -> {
//				String biodevise = x.getBiodevise();
//				return biodevise != null && biodevise.equals("FMR");
//			}).count();
//
//			Long otpauth = authlist.stream().filter(x -> {
//				String biodevise = x.getBiodevise();
//				return biodevise != null && biodevise.equals("OTP");
//			}).count();

//			res.setAuthbio(otpbioauth + bioauth);
//			res.setAuthdemo(otpauth);
//			res.setAuthiris((long) 0);
//
//			res.setEkycbio(ekycfmr);
//			res.setEkycotp(ekycotp);
//			res.setEkyciris(ekyciir);

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
			List<AUTHTransaction> authlist = authTransactionRepo
					.findByRequestdateBetweenAndAgencycode(yeartimestampstart, yeartimestampend, req.getSubAuaId());
			List<EKYCTransaction> ekyclist = ekycTransactionRepo
					.findByRequestdateBetweenAndAgencycode(yeartimestampstart, yeartimestampend, req.getSubAuaId());

			Long AllAuthData = authlist.stream().count();
			Long AllEKYCData = ekyclist.stream().count();

			Long yeartotal = AllAuthData + AllEKYCData;
			YearWiseAllAuthResponse res = new YearWiseAllAuthResponse();
			res.setYearwisetotal(yeartotal);
			res.setYearwiseekyctotal(AllEKYCData);
			res.setYearwiseauthtotal(AllAuthData);

			logger.debug(res.toString());

//			Long ekycotp = ekyclist.stream().filter(x -> x.getBiodivise().equals("OTP")).count();
//			Long ekycfmr = ekyclist.stream().filter(x -> x.getBiodivise().equals("FMR")).count();
//			Long ekyciir = ekyclist.stream().filter(x -> x.getBiodivise().equals("IIR")).count();
//
//			Long otpbioauth = authlist.stream().filter(x -> {
//				String biodevise = x.getBiodevise();
//				return biodevise != null && biodevise.equals("FMR_OTP");
//			}).count();
//
//			Long bioauth = authlist.stream().filter(x -> {
//				String biodevise = x.getBiodevise();
//				return biodevise != null && biodevise.equals("FMR");
//			}).count();
//
//			Long otpauth = authlist.stream().filter(x -> {
//				String biodevise = x.getBiodevise();
//				return biodevise != null && biodevise.equals("OTP");
//			}).count();

			Long authbio = authlist.stream().filter(x -> x.getBio().equals("y")).count();
			Long pa = authlist.stream().filter(x -> x.getPa().equals("y")).count();
			Long pfa = authlist.stream().filter(x -> x.getPfa().equals("y")).count();
			Long pi = authlist.stream().filter(x -> x.getPi().equals("y")).count();
			Long otp = authlist.stream().filter(x -> x.getOtp().equals("y")).count();
			Long demo = pa + pfa + pi + otp;

			System.out.println("authbio  :  " + authbio + "  demo :" + demo);

			Long ekycbio = ekyclist.stream().filter(x -> x.getBio().equals("y")).count();
			Long ekycotp = ekyclist.stream().filter(x -> x.getOtp().equals("y")).count();

			res.setAuthbio(authbio);
			res.setAuthdemo(demo);
			res.setAuthiris((long) 0);
			res.setEkycbio(ekycbio);
			res.setEkycotp(ekycotp);
			res.setEkyciris((long) 0);

//			res.setAuthbio(otpbioauth + bioauth);
//			res.setAuthdemo(otpauth);
//			res.setAuthiris((long) 0);
//
//			res.setEkycbio(ekycfmr);
//			res.setEkycotp(ekycotp);
//			res.setEkyciris(ekyciir);
//			logger.debug(res.toString());
			return res;
		}

	}

	@Override
	public AuaAuthMonthlyResponse getAuthMonthDataResponse(SubAuaUserRequest req) {

		if (req.getSubAuaId().equals("admin")) {

			LocalDateTime currentDateTime = LocalDateTime.now();
			LocalDateTime startDateTime = currentDateTime.minusMonths(11);
			Timestamp timestampStart = Timestamp.valueOf(startDateTime);
			Timestamp timestampEnd = Timestamp.valueOf(currentDateTime);
			List<AUTHTransaction> listdata = authTransactionRepo.findByRequestdateBetween(timestampStart, timestampEnd);
			logger.info("Data fetched from database");
			AuaAuthMonthlyResponse res = new AuaAuthMonthlyResponse();

			res.setAuthdates(new ArrayList<>());
			res.setAuthfingerprint(new ArrayList<>());
			res.setAuthiris(new ArrayList<>());
			res.setAuthdemographic(new ArrayList<>());
			logger.debug(res.toString());
			Map<String, Map<String, Integer>> countMap = new HashMap<>();
			Long demoCount = 0L;
			for (AUTHTransaction transaction : listdata) {
				LocalDateTime transactionDate = transaction.getRequestdate().toLocalDateTime();
				String month = transactionDate.getYear() + "-" + String.format("%02d", transactionDate.getMonthValue())
						+ "-01";
				Map<String, Integer> methodCountMap = countMap.computeIfAbsent(month, k -> new HashMap<>());

				if (transaction.getBio() != null && transaction.getBio().equals("y")) {
					methodCountMap.put("fingerprint", methodCountMap.getOrDefault("fingerprint", 0) + 1);
				}
				methodCountMap.put("otp",
						methodCountMap.getOrDefault("otp", 0) + ("y".equals(transaction.getOtp()) ? 1 : 0));
				methodCountMap.put("pa",
						methodCountMap.getOrDefault("pa", 0) + ("y".equals(transaction.getPa()) ? 1 : 0));
				methodCountMap.put("pfa",
						methodCountMap.getOrDefault("pfa", 0) + ("y".equals(transaction.getPfa()) ? 1 : 0));
				methodCountMap.put("pi",
						methodCountMap.getOrDefault("pi", 0) + ("y".equals(transaction.getPi()) ? 1 : 0));

				// Calculate the total count for demographic methods
				int monthDemo = methodCountMap.getOrDefault("otp", 0) + methodCountMap.getOrDefault("pa", 0)
						+ methodCountMap.getOrDefault("pfa", 0) + methodCountMap.getOrDefault("pi", 0);
				demoCount += monthDemo;
			}

			YearMonth currentMonth = YearMonth.from(startDateTime.toLocalDate());
			YearMonth endMonth = YearMonth.from(currentDateTime.toLocalDate());
			while (!currentMonth.isAfter(endMonth)) {
				String monthString = currentMonth.getYear() + "-" + String.format("%02d", currentMonth.getMonthValue())
						+ "-01";
				res.getAuthdates().add(monthString);
				Map<String, Integer> methodCountMap = countMap.getOrDefault(monthString, Collections.emptyMap());

				res.getAuthfingerprint().add(methodCountMap.getOrDefault("fingerprint", 0));

				res.getAuthiris().add(methodCountMap.getOrDefault("iris", 0));
				res.getAuthdemographic()
						.add(methodCountMap.getOrDefault("otp", 0) + methodCountMap.getOrDefault("pa", 0)
								+ methodCountMap.getOrDefault("pfa", 0) + methodCountMap.getOrDefault("pi", 0));
				currentMonth = currentMonth.plusMonths(1);
			}

			res.getAuthdemographic().get(0);

			return res;
		} else {
			LocalDateTime currentDateTime = LocalDateTime.now();
			LocalDateTime startDateTime = currentDateTime.minusMonths(1);
			Timestamp timestampStart = Timestamp.valueOf(startDateTime);
			Timestamp timestampEnd = Timestamp.valueOf(currentDateTime);
			List<AUTHTransaction> listdata = authTransactionRepo.findByRequestdateBetweenAndAgencycode(timestampStart,
					timestampEnd, req.getSubAuaId());

			logger.info("Data fetched from database:" + listdata.toString());
			AuaAuthMonthlyResponse res = new AuaAuthMonthlyResponse();

			res.setAuthdates(new ArrayList<>());
			res.setAuthfingerprint(new ArrayList<>());
			res.setAuthiris(new ArrayList<>());
			res.setAuthdemographic(new ArrayList<>());
			logger.debug(res.toString());
			Map<String, Map<String, Integer>> countMap = new HashMap<>();
			Long demoCount = 0L;

			LocalDate currentDate = startDateTime.toLocalDate();

			while (!currentDate.isAfter(currentDateTime.toLocalDate())) {
				String dateString = currentDate.getYear() + "-" + String.format("%02d", currentDate.getMonthValue())
						+ "-" + String.format("%02d", currentDate.getDayOfMonth());
				res.getAuthdates().add(dateString);
				countMap.put(dateString, new HashMap<>());

				Map<String, Integer> methodCountMap = countMap.get(dateString);

				for (AUTHTransaction transaction : listdata) {
					LocalDateTime transactionDate = transaction.getRequestdate().toLocalDateTime();
					String day = transactionDate.getYear() + "-"
							+ String.format("%02d", transactionDate.getMonthValue()) + "-"
							+ String.format("%02d", transactionDate.getDayOfMonth());

					if (dateString.equals(day)) {
						if (transaction.getBio() != null && transaction.getBio().equals("y")) {
							methodCountMap.put("fingerprint", methodCountMap.getOrDefault("fingerprint", 0) + 1);
						}
						methodCountMap.put("otp",
								methodCountMap.getOrDefault("otp", 0) + ("y".equals(transaction.getOtp()) ? 1 : 0));
						methodCountMap.put("pa",
								methodCountMap.getOrDefault("pa", 0) + ("y".equals(transaction.getPa()) ? 1 : 0));
						methodCountMap.put("pfa",
								methodCountMap.getOrDefault("pfa", 0) + ("y".equals(transaction.getPfa()) ? 1 : 0));
						methodCountMap.put("pi",
								methodCountMap.getOrDefault("pi", 0) + ("y".equals(transaction.getPi()) ? 1 : 0));
						int dayDemo = methodCountMap.getOrDefault("otp", 0) + methodCountMap.getOrDefault("pa", 0)
								+ methodCountMap.getOrDefault("pfa", 0) + methodCountMap.getOrDefault("pi", 0);
						demoCount += dayDemo;
					}
				}
				res.getAuthfingerprint().add(methodCountMap.getOrDefault("fingerprint", 0));
				res.getAuthiris().add(methodCountMap.getOrDefault("iris", 0));
				res.getAuthdemographic()
						.add(methodCountMap.getOrDefault("otp", 0) + methodCountMap.getOrDefault("pa", 0)
								+ methodCountMap.getOrDefault("pfa", 0) + methodCountMap.getOrDefault("pi", 0));
				currentDate = currentDate.plusDays(1);
			}

			res.getAuthdemographic().get(0);
			logger.debug(res.toString());

			return res;

		}
	}

	@Override
	public AuaEKYCMonthlyResponse getAuaEKYCMonthlyResponse(SubAuaUserRequest req) {
		if (req.getSubAuaId().equals("admin")) {
			LocalDateTime currentDateTime = LocalDateTime.now();
			LocalDateTime startDateTime = currentDateTime.minusMonths(11);
			Timestamp timestampStart = Timestamp.valueOf(startDateTime);
			Timestamp timestampEnd = Timestamp.valueOf(currentDateTime);
			List<EKYCTransaction> listdata = ekycTransactionRepo.findByRequestdateBetween(timestampStart, timestampEnd);
			logger.info("Data fetched from database: " + listdata.toString());
			AuaEKYCMonthlyResponse res = new AuaEKYCMonthlyResponse();
			res.setEkycdates(new ArrayList<>());
			res.setEkycfingerprint(new ArrayList<>());
			res.setEkyciris(new ArrayList<>());
			res.setEkycdemographic(new ArrayList<>());
			logger.debug(res.toString());
			Map<String, Map<String, Integer>> countMap = new HashMap<>();
			Long demoCount = 0L;
			for (EKYCTransaction transaction : listdata) {
				LocalDateTime transactionDate = transaction.getRequestdate().toLocalDateTime();
				String month = transactionDate.getYear() + "-" + String.format("%02d", transactionDate.getMonthValue())
						+ "-01";
				Map<String, Integer> methodCountMap = countMap.computeIfAbsent(month, k -> new HashMap<>());
				if (transaction.getBio() != null && transaction.getBio().equals("y")) {
					methodCountMap.put("fingerprint", methodCountMap.getOrDefault("fingerprint", 0) + 1);
				}
				methodCountMap.put("otp",
						methodCountMap.getOrDefault("otp", 0) + ("y".equals(transaction.getOtp()) ? 1 : 0));
				methodCountMap.put("pa",
						methodCountMap.getOrDefault("pa", 0) + ("y".equals(transaction.getPa()) ? 1 : 0));
				methodCountMap.put("pfa",
						methodCountMap.getOrDefault("pfa", 0) + ("y".equals(transaction.getPfa()) ? 1 : 0));
				methodCountMap.put("pi",
						methodCountMap.getOrDefault("pi", 0) + ("y".equals(transaction.getPi()) ? 1 : 0));

				int monthDemo = methodCountMap.getOrDefault("otp", 0) + methodCountMap.getOrDefault("pa", 0)
						+ methodCountMap.getOrDefault("pfa", 0) + methodCountMap.getOrDefault("pi", 0);
				demoCount += monthDemo;
			}
			YearMonth currentMonth = YearMonth.from(startDateTime.toLocalDate());
			YearMonth endMonth = YearMonth.from(currentDateTime.toLocalDate());
			while (!currentMonth.isAfter(endMonth)) {
				String monthString = currentMonth.getYear() + "-" + String.format("%02d", currentMonth.getMonthValue())
						+ "-01";
				res.getEkycdates().add(monthString);
				Map<String, Integer> methodCountMap = countMap.getOrDefault(monthString, Collections.emptyMap());
				res.getEkycfingerprint().add(methodCountMap.getOrDefault("fingerprint", 0));
				res.getEkyciris().add(methodCountMap.getOrDefault("iris", 0));
				res.getEkycdemographic()
						.add(methodCountMap.getOrDefault("otp", 0) + methodCountMap.getOrDefault("pa", 0)
								+ methodCountMap.getOrDefault("pfa", 0) + methodCountMap.getOrDefault("pi", 0));
				currentMonth = currentMonth.plusMonths(1);
			}

			Integer integer = res.getEkycdemographic().get(0);
			logger.debug(res.toString());

			return res;
		} else {
			LocalDateTime currentDateTime = LocalDateTime.now();
			LocalDateTime startDateTime = currentDateTime.minusMonths(1);
			Timestamp timestampStart = Timestamp.valueOf(startDateTime);
			Timestamp timestampEnd = Timestamp.valueOf(currentDateTime);
			List<EKYCTransaction> listdata = ekycTransactionRepo.findByRequestdateBetweenAndAgencycode(timestampStart,
					timestampEnd, req.getSubAuaId());
			logger.info("Data fetched from database:  " + listdata.toString());
			AuaEKYCMonthlyResponse res = new AuaEKYCMonthlyResponse();

			res.setEkycdates(new ArrayList<>());
			res.setEkycfingerprint(new ArrayList<>());
			res.setEkyciris(new ArrayList<>());
			res.setEkycdemographic(new ArrayList<>());

			Map<String, Map<String, Integer>> countMap = new HashMap<>();
			Long demoCount = 0L;

			LocalDate currentDate = startDateTime.toLocalDate();

			while (!currentDate.isAfter(currentDateTime.toLocalDate())) {
				String dateString = currentDate.getYear() + "-" + String.format("%02d", currentDate.getMonthValue())
						+ "-" + String.format("%02d", currentDate.getDayOfMonth());
				res.getEkycdates().add(dateString);
				countMap.put(dateString, new HashMap<>());

				Map<String, Integer> methodCountMap = countMap.get(dateString);

				for (EKYCTransaction transaction : listdata) {
					LocalDateTime transactionDate = transaction.getRequestdate().toLocalDateTime();
					String day = transactionDate.getYear() + "-"
							+ String.format("%02d", transactionDate.getMonthValue()) + "-"
							+ String.format("%02d", transactionDate.getDayOfMonth());

					if (dateString.equals(day)) {
						if (transaction.getBio() != null && transaction.getBio().equals("y")) {
							methodCountMap.put("fingerprint", methodCountMap.getOrDefault("fingerprint", 0) + 1);
						}
						methodCountMap.put("otp",
								methodCountMap.getOrDefault("otp", 0) + ("y".equals(transaction.getOtp()) ? 1 : 0));
						methodCountMap.put("pa",
								methodCountMap.getOrDefault("pa", 0) + ("y".equals(transaction.getPa()) ? 1 : 0));
						methodCountMap.put("pfa",
								methodCountMap.getOrDefault("pfa", 0) + ("y".equals(transaction.getPfa()) ? 1 : 0));
						methodCountMap.put("pi",
								methodCountMap.getOrDefault("pi", 0) + ("y".equals(transaction.getPi()) ? 1 : 0));
						int dayDemo = methodCountMap.getOrDefault("otp", 0) + methodCountMap.getOrDefault("pa", 0)
								+ methodCountMap.getOrDefault("pfa", 0) + methodCountMap.getOrDefault("pi", 0);
						demoCount += dayDemo;
					}
				}
				res.getEkycfingerprint().add(methodCountMap.getOrDefault("fingerprint", 0));
				res.getEkyciris().add(methodCountMap.getOrDefault("iris", 0));
				res.getEkycdemographic()
						.add(methodCountMap.getOrDefault("otp", 0) + methodCountMap.getOrDefault("pa", 0)
								+ methodCountMap.getOrDefault("pfa", 0) + methodCountMap.getOrDefault("pi", 0));
				currentDate = currentDate.plusDays(1);
			}

			logger.info(res.toString());

			res.getEkycdemographic().get(0);

			return res;

		}
	}

	@Override
	public SubAuaMonthlyResponse getMonthlyResponse() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime startDateTime = currentDateTime.minusMonths(1);
		Timestamp timestampStart = Timestamp.valueOf(startDateTime);
		Timestamp timestampEnd = Timestamp.valueOf(currentDateTime);
		List<EKYCTransaction> ekyclistdata = ekycTransactionRepo.findByRequestdateBetween(timestampStart, timestampEnd);
		logger.info("Data fetched from database  :  " + ekyclistdata.toString());
		List<AUTHTransaction> authlistdata = authTransactionRepo.findByRequestdateBetween(timestampStart, timestampEnd);
		SubAuaMonthlyResponse res = new SubAuaMonthlyResponse();
		LocalDate currentDate = startDateTime.toLocalDate();
		LocalDate endDate = currentDateTime.toLocalDate();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<String> ekycdates = new ArrayList<>();
		List<Long> bothCounts = new ArrayList<>();
		while (!currentDate.isAfter(currentDateTime.toLocalDate())) {
			String dateKey = currentDate.format(dateFormatter);
			ekycdates.add(dateKey);
			long ekyctCount = ekyclistdata.stream()
					.filter(transaction -> dateKey
							.equals(transaction.getRequestdate().toLocalDateTime().toLocalDate().format(dateFormatter)))
					.count();
			long authCount = authlistdata.stream()
					.filter(transaction -> dateKey
							.equals(transaction.getRequestdate().toLocalDateTime().toLocalDate().format(dateFormatter)))
					.count();
			long totalCount = ekyctCount + authCount;
			bothCounts.add(totalCount);
			currentDate = currentDate.plusDays(1);
		}
		res.setDailydate(ekycdates);
		res.setDailycounts(bothCounts);

		logger.debug(res.toString());
		return res;
	}

	@Override
	public Map<String, Object> getTodayAllCount(SubAuaUserRequest req) {
		LocalDateTime todayfromDateTime = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime todayendDateTime = LocalDateTime.now().with(LocalTime.MAX);
		Timestamp yeartimestampstart = Timestamp.valueOf(todayfromDateTime);
		Timestamp yeartimestampend = Timestamp.valueOf(todayendDateTime);
		List<AUTHTransaction> authlist = authTransactionRepo.findByRequestdateBetween(yeartimestampstart,
				yeartimestampend);
		Long authbio = authlist.stream().filter(x -> "y".equals(x.getBio())).count();
		Long pa = authlist.stream().filter(x -> "y".equals(x.getPa())).count();
		Long pfa = authlist.stream().filter(x -> "y".equals(x.getPfa())).count();
		Long pi = authlist.stream().filter(x -> "y".equals(x.getPi())).count();
		Long otp = authlist.stream().filter(x -> "y".equals(x.getOtp())).count();
		Long demo = pa + pfa + pi + otp;
		Long authtotal = authbio + demo;
		List<Integer> chartData = Arrays.asList(demo.intValue(), authbio.intValue(), 0);
		List<String> chartLabel = Arrays.asList("Auth", "Fingerprint", "Iris");
		Map<String, Object> response = new HashMap<>();
		response.put("chartData", chartData);
		response.put("chartLabel", chartLabel);
		return response;
	}

	@Override
	public Map<String, Object> getTodayEkycAllCount(SubAuaUserRequest req) {
		LocalDateTime todayfromDateTime = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime todayendDateTime = LocalDateTime.now().with(LocalTime.MAX);

		Timestamp yeartimestampstart = Timestamp.valueOf(todayfromDateTime);
		Timestamp yeartimestampend = Timestamp.valueOf(todayendDateTime);

		List<EKYCTransaction> ekyclist = ekycTransactionRepo.findByRequestdateBetweenAndAgencycode(yeartimestampstart,
				yeartimestampend, req.getSubAuaId());

		Long ekycbio = ekyclist.stream().filter(x -> "y".equals(x.getBio())).count();
		Long ekycotp = ekyclist.stream().filter(x -> "y".equals(x.getOtp())).count();
		Long ekyctotal = ekycbio + ekycotp;
		List<Integer> chartData = Arrays.asList(ekycotp.intValue(), ekycbio.intValue(), 0);
		List<String> chartLabel = Arrays.asList("Auth", "Fingerprint", "Iris");
		Map<String, Object> response = new HashMap<>();
		response.put("chartData", chartData);
		response.put("chartLabel", chartLabel);
		return response;

	}

	@Override
	public List<LatestTransResponse> getAuthTrans(SubAuaUserRequest req) {
		List<AUTHTransaction> authtrans = authTransactionRepo
				.findFirst10ByAgencycodeOrderByRequestdateDesc(req.getSubAuaId());

		return getlistof(authtrans);
	}

	private List<LatestTransResponse> getlistof(List<AUTHTransaction> authtrans) {
		List<LatestTransResponse> resp = new ArrayList<>();
		for (int i = 0; i < authtrans.size(); i++) {
			LatestTransResponse res = new LatestTransResponse();
			res.setSlNo(i + 1);
			res.setErrorMsg(authtrans.get(i).getErrorMsg()); // Fixed here
			Timestamp timestamp = authtrans.get(i).getRequestdate(); // Fixed here
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss a"); // Changed pattern
			String dateString = sdf.format(new Date(timestamp.getTime()));
			res.setRequestdate(dateString);
			res.setRequesttxnid(authtrans.get(i).getTxn_id()); // Fixed here
			resp.add(res);
		}
		return resp;
	}

	@Override
	public List<LatestTransResponse> getekycTrans(SubAuaUserRequest req) {
		List<EKYCTransaction> ekyctrns = ekycTransactionRepo
				.findFirst10ByAgencycodeOrderByRequestdateDesc(req.getSubAuaId());
		return getlistekycof(ekyctrns);
	}

	private List<LatestTransResponse> getlistekycof(List<EKYCTransaction> authtrans) {
		List<LatestTransResponse> resp = new ArrayList<>();
		for (int i = 0; i < authtrans.size(); i++) {
			LatestTransResponse res = new LatestTransResponse();
			res.setSlNo(i + 1);
			res.setErrorMsg(authtrans.get(i).getErrorMsg()); // Fixed here
			Timestamp timestamp = authtrans.get(i).getRequestdate(); // Fixed here
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss a"); // Changed pattern
			String dateString = sdf.format(new Date(timestamp.getTime()));
			res.setRequestdate(dateString);
			res.setRequesttxnid(authtrans.get(i).getTxn_id()); // Fixed here
			resp.add(res);
		}
		return resp;
	}

	@Override
	public List<LatestTransResponse> getekycTop100Trans(DitecReportsRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LatestTransResponse> getAuthTop100Trans(DitecReportsRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
