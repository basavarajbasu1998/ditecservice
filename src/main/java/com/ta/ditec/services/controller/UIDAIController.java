package com.ta.ditec.services.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.model.ServiceCharges;
import com.ta.ditec.services.model.UIDAIErrorcodes;
import com.ta.ditec.services.request.Errorcode;
import com.ta.ditec.services.service.UIDAIErrorservice;

@RestController
@RequestMapping("api/v1")
public class UIDAIController {

	private static final Logger logger = LoggerFactory.getLogger(UIDAIController.class);

	@Autowired
	UIDAIErrorservice errorservice;

	@PostMapping("/adderror")
	public UIDAIErrorcodes add(@RequestBody @Valid UIDAIErrorcodes u) {
		logger.debug(u.toString());
		System.out.println("UIDAIController.add()");
		UIDAIErrorcodes add = errorservice.add(u);
		logger.debug(add.toString());
		return add;

	}

	@GetMapping("/geterror/{s}")
	public ResponseEntity<UIDAIErrorcodes> getbycode(@PathVariable @Valid String s) {
		logger.debug(s);
		System.out.println("UIDAIController.getbycode()");
		System.out.println("controller request=====" + s);
		UIDAIErrorcodes getbycodes = errorservice.getbycode(s);
		logger.debug(getbycodes.toString());
		return ResponseEntity.ok(getbycodes);
	}

	@GetMapping("/gets")
	public UIDAIErrorcodes geterror(@RequestBody @Valid Errorcode e) {
		logger.debug(e.toString());
		UIDAIErrorcodes getbycodes = errorservice.getbycode(e.getErrorcode());
		logger.debug(getbycodes.toString());
		return getbycodes;

	}

}
