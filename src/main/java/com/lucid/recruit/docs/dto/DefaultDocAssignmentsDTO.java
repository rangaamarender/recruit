package com.lucid.recruit.docs.dto;

import com.lucid.recruit.docs.constants.EnumDefaultDocStatus;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.constants.EnumDocStatus;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;

public class DefaultDocAssignmentsDTO {

	@Nullable
	private String documentAssignDefID;

	@NotNull
	private RelatedDocumentDefDTO documentDef;

	@NotNull
	private EnumDocRelatedEntity relatedEnity;

	@Nullable
	private String relatedSubEnity;

	@Nullable
	private EnumDefaultDocStatus status;

	@NotNull
	private boolean autoAssigned;

	public DefaultDocAssignmentsDTO() {
		super();
	}

	public String getDocumentAssignDefID() {
		return documentAssignDefID;
	}

	public void setDocumentAssignDefID(String documentAssignDefID) {
		this.documentAssignDefID = documentAssignDefID;
	}

	public RelatedDocumentDefDTO getDocumentDef() {
		return documentDef;
	}

	public void setDocumentDef(RelatedDocumentDefDTO documentDef) {
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
