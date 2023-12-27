package com.lucid.recruit.org.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.referencedata.entity.OrgInActiveStatusCodes;

import jakarta.persistence.*;

@Entity
@Table(name = OrganizationStatus.TABLE_NAME, uniqueConstraints = {
		@UniqueConstraint(name = "UQORGSTATUS_1", columnNames = { "org_id", "status_code", "effective_date" }) })

public class OrganizationStatus extends AuditableEntity {

	private static final long serialVersionUID = -6067266782461486321L;
	public static final String TABLE_NAME = "o_orgstatus";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "org_status_id", nullable = false, length = 75)
	private String orgStatusID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = false, updatable = false)
	private Organization organization;

	// Status of the organization
	@Enumerated(EnumType.STRING)
	@Column(name = "status_code",nullable = false)
	private OrgStatus statusCode;

	@ManyToOne
	@JoinColumn(name = "inactive_status_def",nullable = true)
	private OrgInActiveStatusCodes inactiveStatusCode;

	@Column(name = "effective_date", nullable = false)
	private LocalDate effectiveDate;

	// A description of the status. Free flow
	@Column(name = "status_reason_text", nullable = true, length = 255)
	private String statusReasonTxt;

	public String getOrgStatusID() {
		return orgStatusID;
	}

	public void setOrgStatusID(String orgStatusID) {
		this.orgStatusID = orgStatusID;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public OrgStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(OrgStatus statusCode) {
		this.statusCode = statusCode;
	}

	public OrgInActiveStatusCodes getInactiveStatusCode() {
		return inactiveStatusCode;
	}

	public void setInactiveStatusCode(OrgInActiveStatusCodes inactiveStatusCode) {
		this.inactiveStatusCode = inactiveStatusCode;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getStatusReasonTxt() {
		return statusReasonTxt;
	}

	public void setStatusReasonTxt(String statusReasonTxt) {
		this.statusReasonTxt = statusReasonTxt;
	}

	public static Comparator<OrganizationStatus> createOrgStatusLambdaComparator(){
		return Comparator.comparing(OrganizationStatus::getEffectiveDate);
	}

}
