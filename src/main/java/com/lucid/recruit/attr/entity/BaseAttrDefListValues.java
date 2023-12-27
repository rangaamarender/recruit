package com.lucid.recruit.attr.entity;

import com.lucid.core.entity.AuditableEntity;
import jakarta.persistence.*;

@MappedSuperclass
public  class BaseAttrDefListValues extends AuditableEntity {
	@Column(name = "attr_list_value", nullable = false, length = 200)
	private String value;

	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;

	public BaseAttrDefListValues() {
		super();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

}
