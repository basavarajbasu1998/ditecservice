package com.ta.ditec.services.report.domain;

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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AGENCY_MASTER")
public class AgencyLog implements Serializable {
	private static final long serialVersionUID = 2248450101270811244L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AGENCY_ID")
	private Long id;
	@Column(name = "AGENCY_NAME")
	private String agencyName;
	@Column(name = "STATUS")
	private String status;
	@CreationTimestamp
	@Column(name = "CREATE_DATE", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdDate;
	@UpdateTimestamp
	@Column(name = "UPDATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "SECURITY_KEY")
	private String securityKey;
	@CreationTimestamp
	@Column(name = "EXPIRE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date expireDate;
	@Column(name = "AUA")
	private String aua;
	@Column(name = "KUA")
	private String kua;
	@Column(name = "OTP")
	private String otp;
	@Column(name = "ENCRYPT")
	private String encrypt;
	@Column(name = "E_SIGN")
	private String eSign;
	@Column(name = "EMAIL_ID")
	private String emailId;
	
//	@Lob
//	private String encryptedImageData;

//	public Long getId() {
//		return this.id;
//	}
//
//	public void setId(final Long id) {
//		this.id = id;
//	}
//
//	public String getAgencyName() {
//		return this.agencyName;
//	}
//
//	public void setAgencyName(final String agencyName) {
//		this.agencyName = agencyName;
//	}
//
//	public String getStatus() {
//		return this.status;
//	}
//
//	public void setStatus(final String status) {
//		this.status = status;
//	}
//
//	public Date getCreatedDate() {
//		return this.createdDate;
//	}
//
//	public void setCreatedDate(final Date createdDate) {
//		this.createdDate = createdDate;
//	}
//
//	public Date getUpdatedDate() {
//		return this.updatedDate;
//	}
//
//	public void setUpdatedDate(final Date updatedDate) {
//		this.updatedDate = updatedDate;
//	}
//
//	public String getCreatedBy() {
//		return this.createdBy;
//	}
//
//	public void setCreatedBy(final String createdBy) {
//		this.createdBy = createdBy;
//	}
//
//	public String getSecurityKey() {
//		return this.securityKey;
//	}
//
//	public void setSecurityKey(final String securityKey) {
//		this.securityKey = securityKey;
//	}
//
//	public Date getExpireDate() {
//		return this.expireDate;
//	}
//
//	public void setExpireDate(final Date expireDate) {
//		this.expireDate = expireDate;
//	}
//
//	public String getAua() {
//		return this.aua;
//	}
//
//	public void setAua(final String aua) {
//		this.aua = aua;
//	}
//
//	public String getKua() {
//		return this.kua;
//	}
//
//	public void setKua(final String kua) {
//		this.kua = kua;
//	}
//
//	public String getEncrypt() {
//		return this.encrypt;
//	}
//
//	public void setEncrypt(final String encrypt) {
//		this.encrypt = encrypt;
//	}
//
//	public String geteSign() {
//		return this.eSign;
//	}
//
//	public void seteSign(final String eSign) {
//		this.eSign = eSign;
//	}
//
//	public String getEmailId() {
//		return this.emailId;
//	}
//
//	public void setEmailId(final String emailId) {
//		this.emailId = emailId;
//	}
//
//	public String getOtp() {
//		return this.otp;
//	}
//
//	public void setOtp(final String otp) {
//		this.otp = otp;
//	}
//
//	@Override
//	public String toString() {
//		return "AgencyLog [id=" + this.id + ", agencyName=" + this.agencyName + ", status=" + this.status
//				+ ", createdDate=" + this.createdDate + ", updatedDate=" + this.updatedDate + ", createdBy="
//				+ this.createdBy + ", securityKey=" + this.securityKey + ", expireDate=" + this.expireDate + ", aua="
//				+ this.aua + ", kua=" + this.kua + ", otp=" + this.otp + ", encrypt=" + this.encrypt + ", eSign="
//				+ this.eSign + ", emailId=" + this.emailId + "]";
//	}
}
