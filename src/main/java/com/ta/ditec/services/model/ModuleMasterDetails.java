package com.ta.ditec.services.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "module_master_detail")
public class ModuleMasterDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "modul_id")
	@Size(max = 20)
	private String moduleId;
	
	@Column(name = "module_name")
	@Size(max = 50)
	private String moduleName;
	
	@Column(name = "module_icon")
	@Size(max = 50)
	private String moduleIcon;
	
	@Column(name = "module_url")
	@Size(max = 50)
	private String moduleUrl;
	
	@Column(name = "created_by")
	@CreatedBy
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	@CreatedDate
	private Date createdDate;
	
	@Column(name = "last_modified_by")
	@LastModifiedBy
	private String lastModifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="last_modified_date")
	@LastModifiedDate
	private Date lastModifiedDate;

}
