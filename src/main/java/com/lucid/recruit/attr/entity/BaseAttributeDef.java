
package com.lucid.recruit.attr.entity;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.constants.EnumAttributeType;
import com.lucid.recruit.attr.constants.EnumAttributeUniqueType;
import jakarta.persistence.*;

import java.util.List;

@MappedSuperclass
public abstract class BaseAttributeDef extends AuditableEntity {

	@Column(name = "attr_name", nullable = false, length = 50,unique = true)
	private String attrName;

	@Column(name = "attrdsp_name", nullable = true, length = 240)
	private String attrDspName;

	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "attr_type", nullable = false,updatable = false)
	private EnumAttributeType attrType;

	@Column(name = "defined_list",nullable = true)
	private Boolean definedList;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "status", nullable = false)
	private EnumAttributeStatus status;

	@Column(name = "is_required", nullable = false)
	private Boolean required;

	@Column(name = "default_value", nullable = true, length = 200)
	private String defaultValue;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "unique_type", nullable = true)
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

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
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

	public EnumAttributeStatus getStatus() {
		return status;
	}

	public void setStatus(EnumAttributeStatus status) {
		this.status = status;
	}

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public EnumAttributeUniqueType getUniqueType() {
		return uniqueType;
	}

	public void setUniqueType(EnumAttributeUniqueType uniqueType) {
		this.uniqueType = uniqueType;
	}

}
