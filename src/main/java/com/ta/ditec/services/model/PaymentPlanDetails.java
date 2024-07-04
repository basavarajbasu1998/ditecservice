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
@Entity
@Table(name = "paymentPlanDetails")
public class PaymentPlanDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String planType;

	@Column
	private String authcationtype;

	@Column
	private String billingCycle;

	@Column
	private String startTrans;

	@Column
	private String endTrans;

	@Column
	private Double eachtransamount;

	@Column
	private Double amount;
}
