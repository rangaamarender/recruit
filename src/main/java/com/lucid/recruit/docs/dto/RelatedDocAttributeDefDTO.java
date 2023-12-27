package com.lucid.recruit.docs.dto;

import com.lucid.recruit.docs.constants.EnumDocAttrType;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;

public class RelatedDocAttributeDefDTO {
	@NotNull
	private String documentAttrDefID;
	@Nullable
	private String documentAttrName;
	@Nullable
	private String attrDisplayName;
	@Nullable
	private String description;
	@Nullable
	private int displayOrder;
	@Nullable
	private EnumDocAttrType attrType;
	@Nullable
	private boolean required;

	public RelatedDocAttributeDefDTO() {
		super();
	}

	public String getDocumentAttrDefID() {
		return documentAttrDefID;
	}

	public void setDocumentAttrDefID(String documentAttrDefID) {
		this.documentAttrDefID = documentAttrDefID;
	}

	public String getDocumentAttrName() {
		return documentAttrName;
	}

	public void setDocumentAttrName(String documentAttrName) {
		this.documentAttrName = documentAttrName;
	}

	public String getAttrDisplayName() {
		return attrDisplayName;
	}

	public void setAttrDisplayName(String attrDisplayName) {
		this.attrDisplayName = attrDisplayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public EnumDocAttrType getAttrType() {
		return attrType;
	}

	public void setAttrType(EnumDocAttrType attrType) {
		this.attrType = attrType;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
