package com.lucid.recruit.docs.entity;

import com.lucid.core.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseDocAttributes extends AuditableEntity {

	private static final long serialVersionUID = -1573743318293235405L;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_attr_def_id", nullable = false, updatable = false)
	private DocAttributeDef docAttributeDef;

	@Column(name = "doc_attr_value", nullable = false, length = 250)
	private String docAttrValue;

	public DocAttributeDef getDocAttributeDef() {
		return docAttributeDef;
	}

	public void setDocAttributeDef(DocAttributeDef docAttributeDef) {
		this.docAttributeDef = docAttributeDef;
	}

	public String getDocAttrValue() {
		return docAttrValue;
	}

	public void setDocAttrValue(String docAttrValue) {
		this.docAttrValue = docAttrValue;
	}

}
