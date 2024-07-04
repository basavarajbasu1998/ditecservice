package com.ta.ditec.services.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.model.TAErrorCodes;
import com.ta.ditec.services.request.Errorcode;
import com.ta.ditec.services.service.TAErrorcodesService;

@RestController
public class TAErrorCodesController {
	
	private static final Logger logger = LoggerFactory.getLogger(TAErrorCodesController.class);
	
	@Autowired
	private TAErrorcodesService service;
	
	@PostMapping("/add")
	public TAErrorCodes adderror(@RequestBody @Valid  TAErrorCodes ta) {
		System.out.println(ta.getErrorcode());
		logger.debug(ta.toString());
		TAErrorCodes adderor = service.adderor(ta);
		return adderor;
		
	}
	
	@GetMapping("/getall")
	public List<TAErrorCodes>  all(){
		
		List<TAErrorCodes> getall = service.getall();
		logger.debug(getall.toString());
		return getall;
		
	}
	
	@GetMapping("/getbycode")
	public TAErrorCodes geterror(@RequestBody Errorcode e) {
		logger.debug(e.toString());
		TAErrorCodes getbycodes = service.getbycode(e.getErrorcode());
		logger.debug(getbycodes.toString());
		return getbycodes;
	}
	
	
	

}
