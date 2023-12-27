package com.lucid.recruit.attr.dto;

import jakarta.annotation.Nullable;


public class RefAttributeDefDTO{
	@Nullable
	private String attrDefId;

	@Nullable
	public String getAttrDefId() {
		return attrDefId;
	}

	public void setAttrDefId(@Nullable String attrDefId) {
		this.attrDefId = attrDefId;
	}

}
