
package com.lucid.recruit.attr.dto;

import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.constants.EnumAttributeType;
import com.lucid.recruit.attr.constants.EnumAttributeUniqueType;
import com.lucid.recruit.referencedata.dto.RelatedWorkerTypeDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BaseAttributeDefDTO {
	@NotNull
	private String attrName;
	@NotNull
	private String attrDspName;
	@NotNull
	private Integer displayOrder;
	@NotNull
	private EnumAttributeType attrType;

	@NotNull
	private Boolean definedList;
	@Nullable
	private EnumAttributeStatus status;
	@NotNull
	private Boolean required;
	@Nullable
	private String defaultValue;
	@Nullable
	private EnumAttributeUniqueType uniqueType;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrDspName() {
		return attrDspName;
	}

	public void setAttrDspName(String attrDspName) {
		this.attrDspName = attrDspName;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public EnumAttributeType getAttrType() {
		return attrType;
	}

	public void setAttrType(EnumAttributeType attrType) {
		this.attrType = attrType;
	}

	public boolean getDefinedList() {
		return definedList;
	}

	public void setDefinedList(boolean definedList) {
		this.definedList = definedList;
	}

	@Nullable
	public EnumAttributeStatus getStatus() {
		return status;
	}

	public void setStatus(@Nullable EnumAttributeStatus status) {
		this.status = status;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Nullable
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(@Nullable String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Nullable
	public EnumAttributeUniqueType getUniqueType() {
		return uniqueType;
	}

	public void setUniqueType(@Nullable EnumAttributeUniqueType uniqueType) {
		this.uniqueType = uniqueType;
	}


}
