package com.ta.ditec.services.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "integration_charges")
@Entity
public class IntegrationCharges {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column
	private Double applicationEnvironmentjava;
	@Column
	private Double applicationEnvironmentnet;
	@Column
	private Double applicationEnvironmentphp;
	@Column
	private Double auaDemographic;
	@Column
	private Double auaFingerprint;
	@Column
	private Double auaIris;
	@Column
	private Double auaOtp;
	@Column
	private Double integrationApprochAPI;
	@Column
	private Double integrationApprochThinClient;
	@Column
	private Double integrationApprochmobile;
	@Column
	private Double integrationApprochweb;
	@Column
	private Double kuaFingerprint;
	@Column
	private Double kuaIris;
	@Column
	private Double kuaotp;
	@Column
	private Double otherservicesdbt;
	@Column
	private Double otherservicesdigitalsignature;
	@Column
	private Double integrationcharges;
	@Column
	private String subAuaId;

//	@Column
//	private Double rddevicesMantra;
//	@Column
//	private Double rddevicesStartek;
//	@Column
//	private String transactionModel;

}
