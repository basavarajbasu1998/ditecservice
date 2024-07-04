package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.ServiceCharges;
import com.ta.ditec.services.model.UIDAIErrorcodes;

public interface UIDAIErrorservice {

	public UIDAIErrorcodes add(UIDAIErrorcodes u);

	public List<UIDAIErrorcodes> getall();

	public UIDAIErrorcodes getbycode(String s);

}
