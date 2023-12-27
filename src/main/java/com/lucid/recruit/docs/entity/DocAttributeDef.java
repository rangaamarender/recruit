package com.lucid.recruit.docs.entity;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.docs.constants.EnumDocAttrType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = DocAttributeDef.TABLE_NAME, uniqueConstraints = {
		@UniqueConstraint(name = "UQDocAttrDef1", columnNames = { "doc_def_id","doc_attr_name" }) })
public class  DocAttributeDef extends AuditableEntity {
	private static final long serialVersionUID = 6535012923072605247L;
	public static final String TABLE_NAME = "d_doc_attr_def";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "doc_attr_def_id", nullable = false, length = 75,updatable = false)
	private String documentAttrDefID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_def_id", nullable = false, updatable = false)
	private DocumentDef documentDef;

	@Column(name = "doc_attr_name", nullable = false, length = 50)
	private String documentAttrName;

	@Column(name = "attr_display_name", nullable = false, length = 50)
	private String attrDisplayName;

	@Column(name = "description", nullable = true, length = 400)
	private String description;

	@Column(name = "display_order", nullable = false)
	private int displayOrder;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "attr_type", nullable = false,updatable = false)
	private EnumDocAttrType attrType;

	@Column(name = "is_required", nullable = false,updatable = false)
	private boolean required;

	public DocAttributeDef() {
		super();
	}

	public String getDocumentAttrDefID() {
		return documentAttrDefID;
	}

	public void setDocumentAttrDefID(String documentAttrDefID) {
		this.documentAttrDefID = documentAttrDefID;
	}

	public DocumentDef getDocumentDef() {
		return documentDef;
	}

	public void setDocumentDef(DocumentDef documentDef) {
		this.documentDef = documentDef;
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
