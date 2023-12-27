package com.lucid.recruit.org.dto;

import com.lucid.recruit.attr.dto.OrgAttributeDefDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class OrganizationAttributesDTO {

	@Nullable
	private String orgAttrID;
	@NotNull
	private OrgAttributeDefDTO orgAttributeDef;
	@Nullable
	private String attrValue;

	@Nullable
	private LocalDate startDate;

	@Nullable
	private LocalDate endDate;

	@Nullable
	public String getOrgAttrID() {
		return orgAttrID;
	}

	public void setOrgAttrID(@Nullable String orgAttrID) {
		this.orgAttrID = orgAttrID;
	}

	public OrgAttributeDefDTO getOrgAttributeDef() {
		return orgAttributeDef;
	}

	public void setOrgAttributeDef(OrgAttributeDefDTO orgAttributeDef) {
		this.orgAttributeDef = orgAttributeDef;
	}

	@Nullable
	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(@Nullable String attrValue) {
		this.attrValue = attrValue;
	}

	@Nullable
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(@Nullable LocalDate startDate) {
		this.startDate = startDate;
	}

	@Nullable
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(@Nullable LocalDate endDate) {
		this.endDate = endDate;
	}
}
