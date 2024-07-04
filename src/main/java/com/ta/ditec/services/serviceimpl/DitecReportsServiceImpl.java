package com.ta.ditec.services.serviceimpl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.report.domain.AUTHTransaction;
import com.ta.ditec.services.report.domain.EKYCTransaction;
import com.ta.ditec.services.request.DitecReportsRequest;
import com.ta.ditec.services.response.DitecReportsServiceResponse;
import com.ta.ditec.services.response.LatestTransResponse;
import com.ta.ditec.services.response.SubAuaOrgResponse;
import com.ta.ditec.services.service.DitecReportsService;

@Service
public class DitecReportsServiceImpl implements DitecReportsService {

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	private final EntityManager entityManager; // Assuming primary database

	@Autowired
	public DitecReportsServiceImpl(@Qualifier("primaryEntityManagerFactory") EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<DitecReportsServiceResponse> getDitecReportsServiceResponse(DitecReportsRequest req) {

		String sqlQuery = "WITH kua_aggregated AS ( " + "    SELECT " + "        agency_code, "
				+ "        DATE(request_date) AS date, " + "        COUNT(*) AS ekyc_total_count_kua, "
				+ "        COUNT(CASE WHEN otp = 'y' THEN 1 END) AS otp_y_count_kua, "
				+ "        COUNT(CASE WHEN bio = 'y' THEN 1 END) AS bio_y_count_kua " + "    FROM "
				+ "        public.kua_log " + "    GROUP BY " + "        agency_code, DATE(request_date) " + "), " + " "
				+ "aua_aggregated AS ( " + "    SELECT " + "        agency_code, "
				+ "        DATE(request_date) AS date, " + "        COUNT(*) AS ekyc_total_count_aua, "
				+ "        COUNT(CASE WHEN otp = 'y' THEN 1 END) AS otp_y_count_aua, "
				+ "        COUNT(CASE WHEN bio = 'y' THEN 1 END) AS bio_y_count_aua " + "    FROM "
				+ "        public.aua_log " + "    GROUP BY " + "        agency_code, DATE(request_date) " + ") " + " "
				+ "SELECT " + "    COALESCE(kua.agency_code, aua.agency_code) AS agency_code, "
				+ "    COALESCE(kua.date, aua.date) AS date, "
				+ "    COALESCE(kua.ekyc_total_count_kua, 0) AS ekyc_total_count_kua, "
				+ "    COALESCE(kua.otp_y_count_kua, 0) AS otp_y_count_kua, "
				+ "    COALESCE(kua.bio_y_count_kua, 0) AS bio_y_count_kua, "
				+ "    COALESCE(aua.ekyc_total_count_aua, 0) AS ekyc_total_count_aua, "
				+ "    COALESCE(aua.otp_y_count_aua, 0) AS otp_y_count_aua, "
				+ "    COALESCE(aua.bio_y_count_aua, 0) AS bio_y_count_aua " + "FROM " + "    kua_aggregated kua "
				+ "FULL OUTER JOIN "
				+ "    aua_aggregated aua ON kua.agency_code = aua.agency_code AND kua.date = aua.date ";

		if (req != null) {
			String startDate = req.getStartDate();
			String endDate = req.getEndDate();
			String agencyCode = req.getSubAuaId();

			// Construct WHERE clause based on non-empty values
			if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
				sqlQuery += "WHERE DATE(kua.date) BETWEEN '" + startDate + "' AND '" + endDate + "' ";
			}

			if (req.getSubAuaId() != null) {
				if (agencyCode != null && !agencyCode.isEmpty()) {
					if (sqlQuery.contains("WHERE")) {
						sqlQuery += "AND kua.agency_code = '" + agencyCode + "' ";
					} else {
						sqlQuery += "WHERE kua.agency_code = '" + agencyCode + "' ";
					}
				}
			}
		}

		sqlQuery += "ORDER BY " + "    agency_code, date;";

		Query refundquery = entityManager.createNativeQuery(sqlQuery);
		List<Object[]> results = refundquery.getResultList();

		List<DitecReportsServiceResponse> responses = new ArrayList<>();

		Long serialNumber = 1l; // Initialize serial number

		for (Object[] row : results) {
			DitecReportsServiceResponse response = new DitecReportsServiceResponse();
			response.setAgencyCode((String) row[0]); // Assuming agency_code is a string
			response.setDate((Date) row[1]); // Assuming date is a Date object
			response.setEkycTotalCountKua((BigInteger) row[2]);
			response.setOtpYCountKua((BigInteger) row[3]);
			response.setBioYCountKua((BigInteger) row[4]);
			response.setEkycTotalCountAua((BigInteger) row[5]);
			response.setOtpYCountAua((BigInteger) row[6]);
			response.setBioYCountAua((BigInteger) row[7]);
			response.setSlNo(serialNumber);
			serialNumber++; // Increment serial number
			responses.add(response);
		}
		return responses;
	}

	@Override
	public List<SubAuaOrgResponse> getList() {
		List<SubAuaUser> repo = subAuaUserRepo.findAll();
		List<SubAuaOrgResponse> resp = new ArrayList<>();
		for (SubAuaUser user : repo) {
			SubAuaOrgResponse res = new SubAuaOrgResponse();
			res.setOrgName(user.getOrganisationName());
			res.setSubAuaID(user.getSubAuaId());
			resp.add(res);
		}
		return resp;
	}

	@Override
	public List<LatestTransResponse> geteTop100Trans(DitecReportsRequest req) {
		return null;
//		List<EKYCTransaction> ekyctrns = ekycTransactionRepo
//				.findFirst100ByAgencycodeOrderByRequestdateDesc(req.getSubAuaId());
//
//		List<AUTHTransaction> authtrans = authTransactionRepo
//				.findFirst100ByAgencycodeOrderByRequestdateDesc(req.getSubAuaId());
//
//		return getlistekycof(ekyctrns);

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

}
