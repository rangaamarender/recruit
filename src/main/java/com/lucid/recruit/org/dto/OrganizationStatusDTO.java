package com.lucid.recruit.org.dto;

import java.time.LocalDate;

import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.referencedata.dto.OrgInActiveStatusCodesDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class OrganizationStatusDTO {
	@Nullable
	private String orgStatusID;
	@NotNull
	private OrgStatus statusCode;

	@Nullable
	private OrgInActiveStatusCodesDTO inactiveStatusCode;

	@Nullable
	private LocalDate effectiveDate;
	@Nullable
	private String statusReasonTxt;

	@Nullable
	public String getOrgStatusID() {
		return orgStatusID;
	}

	public void setOrgStatusID(@Nullable String orgStatusID) {
		this.orgStatusID = orgStatusID;
	}

	public OrgStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(OrgStatus statusCode) {
		this.statusCode = statusCode;
	}

	@Nullable
	public OrgInActiveStatusCodesDTO getInactiveStatusCode() {
		return inactiveStatusCode;
	}

	public void setInactiveStatusCode(@Nullable OrgInActiveStatusCodesDTO inactiveStatusCode) {
		this.inactiveStatusCode = inactiveStatusCode;
	}

	@Nullable
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(@Nullable LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Nullable
	public String getStatusReasonTxt() {
		return statusReasonTxt;
	}

	public void setStatusReasonTxt(@Nullable String statusReasonTxt) {
		this.statusReasonTxt = statusReasonTxt;
	}
}
