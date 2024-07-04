package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.MasterDataTypeDetails;
import com.ta.ditec.services.request.MasterDataTypeDetailsRequest;


public class MasterDataTypeDetailTransfrom {

	public MasterDataTypeDetails getMasterDataTypeDetailTransfrom(MasterDataTypeDetailsRequest req) {
		MasterDataTypeDetails mdtype = new MasterDataTypeDetails();
		BeanUtils.copyProperties(req, mdtype);
		mdtype.setCreatedDate(new Date());
		mdtype.setCreatedBy("superadmin");
		mdtype.setLastModifiedBy("superadmin");
		mdtype.setLastModifiedDate(new Date());
		return mdtype;
	}
}
