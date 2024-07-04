package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.MasterDataDetails;
import com.ta.ditec.services.request.MasterDataDetailsRequest;

public class MasterDataDetailTransfrom {

	public MasterDataDetails getMasterDataDetailTransfrom(MasterDataDetailsRequest req) {

		MasterDataDetails mddetl = new MasterDataDetails();
		BeanUtils.copyProperties(req, mddetl);
		mddetl.setCreatedDate(new Date());
		mddetl.setCreatedBy("superadmin");
		mddetl.setLastModifiedBy("superadmin");
		mddetl.setLastModifiedDate(new Date());
		return mddetl;
	}
}
