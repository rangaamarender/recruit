package com.lucid.recruit.docs.dto;

import jakarta.validation.constraints.NotNull;

public class BaseDocAttributesDTO {
	@NotNull
	private RelatedDocAttributeDefDTO docAttributeDef;
	@NotNull
	private String docAttrValue;

	public RelatedDocAttributeDefDTO getDocAttributeDef() {
		return docAttributeDef;
	}

	public void setDocAttributeDef(RelatedDocAttributeDefDTO docAttributeDef) {
		this.docAttributeDef = docAttributeDef;
	}

	public String getDocAttrValue() {
		return docAttrValue;
	}

	public void setDocAttrValue(String docAttrValue) {
		this.docAttrValue = docAttrValue;
	}

}
