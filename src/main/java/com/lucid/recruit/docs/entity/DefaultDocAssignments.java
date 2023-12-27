package com.lucid.recruit.docs.entity;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.docs.constants.EnumDefaultDocStatus;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.constants.EnumDocStatus;

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
@Table(name = DefaultDocAssignments.TABLE_NAME, uniqueConstraints = {
		@UniqueConstraint(name = "UQDocAttrDef1", columnNames = { "related_entity", "related_sub_entity", "doc_def_id",
				"status" }) })
public class DefaultDocAssignments extends AuditableEntity {

	private static final long serialVersionUID = 3585501344385018655L;
	public static final String TABLE_NAME = "d_def_doc_assignments";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "doc_assign_def_id", nullable = false, length = 75)
	private String documentAssignDefID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_def_id", nullable = false, updatable = false)
	private DocumentDef documentDef;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "related_entity", nullable = false)
	private EnumDocRelatedEntity relatedEnity;

	@Column(name = "related_sub_entity", nullable = true, length = 75)
	private String relatedSubEnity;

	// active/inactive
	@Enumerated(value = EnumType.STRING)
	@Column(name = "status", nullable = false)
	private EnumDefaultDocStatus status;

	@Column(name = "auto_assigned", nullable = false)
	private boolean autoAssigned;

	public DefaultDocAssignments() {
		super();
	}

	public String getDocumentAssignDefID() {
		return documentAssignDefID;
	}

	public void setDocumentAssignDefID(String documentAssignDefID) {
		this.documentAssignDefID = documentAssignDefID;
	}

	public DocumentDef getDocumentDef() {
		return documentDef;
	}

	public void setDocumentDef(DocumentDef documentDef) {
		this.documentDef = documentDef;
	}

	public EnumDocRelatedEntity getRelatedEnity() {
		return relatedEnity;
	}

	public void setRelatedEnity(EnumDocRelatedEntity relatedEnity) {
		this.relatedEnity = relatedEnity;
	}

	public String getRelatedSubEnity() {
		return relatedSubEnity;
	}

	public void setRelatedSubEnity(String relatedSubEnity) {
		this.relatedSubEnity = relatedSubEnity;
	}

	public EnumDefaultDocStatus getStatus() {
		return status;
	}

	public void setStatus(EnumDefaultDocStatus status) {
		this.status = status;
	}

	public boolean isAutoAssigned() {
		return autoAssigned;
	}

	public void setAutoAssigned(boolean autoAssigned) {
		this.autoAssigned = autoAssigned;
	}

}
