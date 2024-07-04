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

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "kua_log")
public class EKYCTransaction {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column(name = "agency_code")
	private String agencycode;

	@Column(name = "bio")
	private String bio;

	@Column(name = "code")
	private String code;

	@Column(name = "request_date")
	private Timestamp requestdate;

	@Column(name = "error_code")
	private String errorcode;

	@Column(name = "otp")
	private String otp;

	@Column(name = "pa")
	private String pa;

	@Column(name = "pfa")
	private String pfa;

	@Column(name = "pi")
	private String pi;

	@Column(name = "request_txn_id")
	private String requesttxnid;

	@Column(name = "tkn")
	private String tkn;

	@Column(name = "txn_id")
	private String txn_id;

	@Column(name = "bio_devise")
	private String biodivise;

	@Column(name = "response_date")
	private Timestamp responsedate;
	
	@Column(name = "ERROR_MSG")
    private String errorMsg;

}
