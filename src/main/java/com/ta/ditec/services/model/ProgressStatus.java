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

@Entity
@Data
@Table(name = "progrssStatus")
@AllArgsConstructor
@NoArgsConstructor
public class ProgressStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String subAuaId;

	@Column
	private Integer applicationStatus;

	@Column
	private String applicationDescription;

}
