package com.lucid.recruit.org.entity;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

import com.lucid.core.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = OrgCommunication.TABLE_NAME)
public class OrgCommunication extends AuditableEntity {

	private static final long serialVersionUID = -286472131363904229L;
	public static final String TABLE_NAME = "o_org_communication";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "org_cmnc_id", nullable = false, length = 75)
	private String orgCmncID;

	@Column(name = "start_dat", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_dat", nullable = true)
	private LocalDate endDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = false, updatable = false)
	private Organization organization;

	// Auth. signatory First Name
	@Column(name = "auth_signatory_fn", nullable = false, length = 100)
	private String authSignataryFn;

	// Auth. signatory Last Name
	@Column(name = "auth_signatory_ln", nullable = false, length = 100)
	private String authSignataryLn;

	// Auth. signatory Email
	@Email
	@Column(name = "auth_signatory_email", nullable = false, length = 100)
	private String authSignataryEmail;

	// Auth. signatory Phone
	@Column(name = "auth_signatory_phone", nullable = false, length = 20)
	private String authSignataryPhone;

	public OrgCommunication() {
		super();
	}

	public String getOrgCmncID() {
		return orgCmncID;
	}

	public void setOrgCmncID(String orgCmncID) {
		this.orgCmncID = orgCmncID;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getAuthSignataryFn() {
		return authSignataryFn;
	}

	public void setAuthSignataryFn(String authSignataryFn) {
		this.authSignataryFn = authSignataryFn;
	}

	public String getAuthSignataryLn() {
		return authSignataryLn;
	}

	public void setAuthSignataryLn(String authSignataryLn) {
		this.authSignataryLn = authSignataryLn;
	}

	public String getAuthSignataryEmail() {
		return authSignataryEmail;
	}

	public void setAuthSignataryEmail(String authSignataryEmail) {
		this.authSignataryEmail = authSignataryEmail;
	}

	public String getAuthSignataryPhone() {
		return authSignataryPhone;
	}

	public void setAuthSignataryPhone(String authSignataryPhone) {
		this.authSignataryPhone = authSignataryPhone;
	}


	public void setOrganization(Optional<Organization> organization) {
	}

	public static Comparator<OrgCommunication> createCommunicationLambdaComparator(){
		return Comparator.comparing(OrgCommunication::getStartDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}

		OrgCommunication orgCommunication = (OrgCommunication) obj; // Cast obj to your class type
		// Compare relevant fields for equality

		if(authSignataryEmail == null){
			if(orgCommunication.authSignataryEmail != null){
				return false;
			}
		} else if(!authSignataryEmail.equals(orgCommunication.authSignataryEmail)){
			return false;
		}
		if(authSignataryFn == null){
			if(orgCommunication.authSignataryFn != null){
				return false;
			}
		} else if(!authSignataryFn.equals(orgCommunication.authSignataryFn)){
			return false;
		}
		if(authSignataryLn == null){
			if(orgCommunication.authSignataryLn != null){
				return false;
			}
		} else if(!authSignataryLn.equals(orgCommunication.authSignataryLn)){
			return false;
		}
		if(authSignataryPhone == null){
			if(orgCommunication.authSignataryPhone != null){
				return false;
			}
		} else if(!authSignataryPhone.equals(orgCommunication.authSignataryPhone)){
			return false;
		}
		return true;
	}

}
