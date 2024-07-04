package com.ta.ditec.services.report.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="otp_log")
public class AuthOtp {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "agency_code")
	private String agencycode;

	@Column(name = "code")
	private String code;

	@Column(name = "request_date")
	private Timestamp requestdate;

	@Column(name = "error_code")
	private String errorcode;

	@Column(name = "error_msg")
	private String errormsg;

	@Column(name = "request_txn_id")
	private String requesttxnid;

	@Column(name = "service_type")
	private String servicetype;

	@Column(name = "txn_id")
	private String txnid;

	@Column(name = "response_date")
	private Timestamp responsedate;

}
