

package com.ta.ditec.services.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.report.domain.AgencyLog;
import com.ta.ditec.services.report.repo.AgencyLogRepo;
import com.ta.ditec.services.response.AgencyLogResponse;
import com.ta.ditec.services.service.AgencyLogService;

@Service
public class AgencyLogServiceImpl implements AgencyLogService {

    private static final Logger logger = LoggerFactory.getLogger(AgencyLogServiceImpl.class);

    @Autowired
    private AgencyLogRepo agencyLogRepo;

    @Autowired
    private SubAuaUserRepo subAuaUserRepo;

    @Override
    public AgencyLogResponse[] getAgencyLog() {
        List<AgencyLog> logs = agencyLogRepo.findAll();
        List<SubAuaUser> sublogs = subAuaUserRepo.findAll();

        List<AgencyLogResponse> matchingResponses = new ArrayList<>();

        // Find matching data between the two tables
        for (AgencyLog log : logs) {
            String agencyName = log.getAgencyName();
            for (SubAuaUser subAuaUser : sublogs) {
                String subAuaId = subAuaUser.getSubAuaId();
                if (agencyName.equals(subAuaId)) {
                    AgencyLogResponse response = convertToResponse(log, subAuaUser);
                    matchingResponses.add(response);
                    break; // Exit inner loop if a match is found
                }
            }
        }

        if (matchingResponses.isEmpty()) {
            logger.info("No matching data found between AgencyLog and SubAuaUser.");
            return new AgencyLogResponse[0]; // Return an empty array if no matches are found
        }

        logger.info("Matching data fetched from database");
        return matchingResponses.toArray(new AgencyLogResponse[0]);
    }

    private AgencyLogResponse convertToResponse(AgencyLog log, SubAuaUser subAuaUser) {
        AgencyLogResponse response = new AgencyLogResponse();
        response.setAgencyName(log.getAgencyName());
        response.setId(log.getId());
        response.setOrgName(subAuaUser.getOrganisationName());
        response.setPlanType(subAuaUser.getModelTransaction());
        logger.debug(response.toString());
        return response;
    }
}
