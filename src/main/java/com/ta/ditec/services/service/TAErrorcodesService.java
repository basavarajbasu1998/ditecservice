package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.TAErrorCodes;



public interface TAErrorcodesService {

	public TAErrorCodes adderor(TAErrorCodes t);
	
	public List<TAErrorCodes>  getall();

	public TAErrorCodes getbycode(String errorcode);
}
