package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class SubAuaApiKeysRequest {

	private String subAuaId;

	private String environment;

	private String subauastagekey;

	private String subauaproductionkey;

}
