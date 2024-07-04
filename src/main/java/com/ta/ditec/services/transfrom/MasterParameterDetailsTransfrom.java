package com.ta.ditec.services.transfrom;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.MasterParameterDetails;
import com.ta.ditec.services.request.MasterParameterDetailsRequest;

public class MasterParameterDetailsTransfrom {

	public MasterParameterDetails getMasterParameterDetailsTransfrom(MasterParameterDetailsRequest req) {
		MasterParameterDetails mpd = new MasterParameterDetails();
		BeanUtils.copyProperties(req, mpd);
		return mpd;
	}
}
