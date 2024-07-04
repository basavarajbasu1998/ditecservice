package com.ta.ditec.services.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TAErrorCodes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String errorcode;

	private String errordesc;

}
