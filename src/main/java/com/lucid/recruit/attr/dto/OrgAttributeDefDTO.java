package com.lucid.recruit.attr.dto;

import jakarta.annotation.Nullable;

import java.util.List;

public class OrgAttributeDefDTO extends BaseAttributeDefDTO{
	@Nullable
	private String attrDefId;

	@Nullable
	private List<OrgAttrDefListValuesDTO> attrListValues;

	@Nullable
	public String getAttrDefId() {
		return attrDefId;
	}

	public void setAttrDefId(@Nullable String attrDefId) {
		this.attrDefId = attrDefId;
	}

	@Nullable
	public List<OrgAttrDefListValuesDTO> getAttrListValues() {
		return attrListValues;
	}

	public void setAttrListValues(@Nullable List<OrgAttrDefListValuesDTO> attrListValues) {
		this.attrListValues = attrListValues;
	}
}
