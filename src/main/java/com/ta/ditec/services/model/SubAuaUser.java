package com.ta.ditec.services.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "subaua")
public class SubAuaUser implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String subAuaId;

	@Column
	private String organisationName;
	@Column
	private String businessApplicationName;

	@Column
	private String status;

	@Column
	private String applicationstatus;

	@Column
	private String address;

	@Column
	private String district;
	@Column
	private String city;
	@Column
	private String pincode;
	@Column
	private String managementName;
	@Column
	private String managementDesignationName;

	@Column
	private String managementMobilenumber;
	@Column
	private String managementEmail;
	@Column
	private String technicalName;

	@Column
	private String technicalDesignationName;
	@Column
	private String technicalEmail;
	@Column
	private String technicalMobilenumber;

	@Column
	private String accountStatus;

	@Column
	private int loginAttempts;

	@Column
	private Boolean auaDemographicCategory;

	@Column
	private Boolean auaDemographic;

	@Column
	private Boolean auaOtp;

	@Column
	private Boolean auaFingerprint;

	@Column
	private Boolean auaIris;

	@Column
	private Boolean kuaOtp;

	@Column
	private Boolean kuaFingerprint;

	@Column
	private Boolean kuaIris;

	@Column
	private Boolean auaDemoghrapicBasic;
	@Column
	private Boolean auaDemoghrapicpoi;
	@Column
	private Boolean auaDemoghrapicFull;

	@Column
	private String authenticationPurpose;

	@Column
	private String kycpurpose;

	@Column
	private Boolean applicationEnvironmentjava;

	@Column
	private Boolean applicationEnvironmentnet;

	@Column
	private Boolean applicationEnvironmentphp;

	@Column
	private Boolean rddevicesMantra;

	@Column
	private Boolean rddevicesStartek;

	@Column
	private Boolean integrationApprochAPI;

	@Column
	private Boolean integrationApprochThinClient;

	@Column
	private Boolean integrationApprochweb;

	@Column
	private Boolean integrationApprochmobile;

	@Column
	private Boolean otherservices;

	@Column
	private String userName;

	@Column
	private String password;

	@Column
	private Boolean otherservicesdbt;

	@Column
	private Boolean otherservicesdigitalsignature;

	@Column
	private String applicationupload;

	@Column
	private String applicationapproved;

	@Column
	private String applicationReject;

	@Column
	private Integer firsttimeuser;

	@Column
	private Integer forgotpassword;

	@Column
	private String modelTransaction;

	@Column
	private Boolean navigateStatus;

	@Column
	private Double integrationcharges;

	@CreatedBy
	@Size(max = 25)
	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name = "created_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date createdDate;

	@LastModifiedBy
	@Size(max = 25)
	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "last_modified_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date lastModifiedDate;

	private String role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}

	@Override
	public String getUsername() {
		return managementEmail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
