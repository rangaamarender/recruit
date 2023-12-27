package com.lucid.recruit.attr.dto;

import jakarta.annotation.Nullable;

import java.util.List;

public class ContractAttributeDefDTO extends BaseAttributeDefDTO{
	@Nullable
	private String attrDefId;

	@Nullable
	private List<ContractAttrDefListValuesDTO> attrListValues;

	@Nullable
	public String getAttrDefId() {
		return attrDefId;
	}

	public void setAttrDefId(@Nullable String attrDefId) {
		this.attrDefId = attrDefId;
	}

	@Nullable
	public List<ContractAttrDefListValuesDTO> getAttrListValues() {
		return attrListValues;
	}

	public void setAttrListValues(@Nullable List<ContractAttrDefListValuesDTO> attrListValues) {
		this.attrListValues = attrListValues;
	}
}
