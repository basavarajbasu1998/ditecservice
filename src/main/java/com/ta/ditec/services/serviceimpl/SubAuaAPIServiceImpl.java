package com.ta.ditec.services.serviceimpl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.SubAuaAPI;
import com.ta.ditec.services.repo.SubAuaAPIRepo;
import com.ta.ditec.services.repo.SubAuaUserRepo;
import com.ta.ditec.services.request.SubAuaApiKeysRequest;
import com.ta.ditec.services.service.SubAuaAPIService;
import com.ta.ditec.services.utils.APIUtiles;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class SubAuaAPIServiceImpl implements SubAuaAPIService {

	private static final Logger logger = LoggerFactory.getLogger(SubAuaAPIServiceImpl.class);

	@Autowired
	private SubAuaAPIRepo subAuaAPIRepo;

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	@Override
	public SubAuaAPI getApikeys(SubAuaAPI req) {

		System.out.println("api request" + req);

		List<SubAuaAPI> existingApis = subAuaAPIRepo.findBySubAuaId(req.getSubAuaId());
		if (CollectionUtils.isNotEmpty(existingApis)) {
			
			System.out.println("idddddddddd is thereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
			
			SubAuaAPI existingApi = existingApis.get(0);
			String stagekey = APIUtiles.generateAPIkey();
			String prokey = APIUtiles.generateAPIkey();
			existingApi.setStagingkey(stagekey);
			existingApi.setProdkey(prokey);
			return subAuaAPIRepo.save(existingApi);
		} else {
			System.out.println("idddddddddd is not thereeeeeeeeeeeeeee");

			String stagekey = APIUtiles.generateAPIkey();
			String prokey = APIUtiles.generateAPIkey();

			SubAuaAPI api = subAuaAPIRepo.save(req);
			String subauaapi = TAUtils.getMerchantId(req.getId());
			req.setStagingkey(stagekey);
			req.setProdkey(prokey);
			req.setSubauaApikey(subauaapi);
			return subAuaAPIRepo.save(req);
		}
	}

	@Override
	public List<SubAuaAPI> getSubAuaAPIAll(SubAuaApiKeysRequest req) {
		List<SubAuaAPI> lists = subAuaAPIRepo.findBySubAuaId(req.getSubAuaId());
		logger.info("Data fetched successfully");
		return lists;
	}

}
