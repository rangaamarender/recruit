package com.lucid.recruit.docs.dto;

import java.util.List;

import com.lucid.recruit.docs.constants.EnumDocDefStatus;

import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.worker.entity.WorkerTypeCode;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;

public class DocumentDefDTO {
	@Nullable
	private String documentDefID;
	@NotNull
	private String documentName;
	@NotNull
	private String docDisplayName;
	@Nullable
	private EnumDocDefStatus status;
	@NotNull
	private Boolean downloadable;
	@NotNull
	private Boolean monitorable;
	@NotNull
	private Boolean secure;

	private boolean expiryInd;

	@Nullable
	private List<DocAttributeDefDTO> docAttrDef;

	@Nullable
	private Boolean autoAssigned;

	@Nullable
	private EnumDocRelatedEntity relatedEntity;

	@Nullable
	private WorkerTypeCode relatedSubEntity;

	public DocumentDefDTO() {
		super();
	}

	public String getDocumentDefID() {
		return documentDefID;
	}

	public void setDocumentDefID(String documentDefID) {
		this.documentDefID = documentDefID;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocDisplayName() {
		return docDisplayName;
	}

	public void setDocDisplayName(String docDisplayName) {
		this.docDisplayName = docDisplayName;
	}

	public EnumDocDefStatus getStatus() {
		return status;
	}

	public void setStatus(EnumDocDefStatus status) {
		this.status = status;
	}

	public List<DocAttributeDefDTO> getDocAttrDef() {
		return docAttrDef;
	}

	public void setDocAttrDef(List<DocAttributeDefDTO> docAttrDef) {
		this.docAttrDef = docAttrDef;
	}

	public Boolean getDownloadable() {
		return downloadable;
	}

	public void setDownloadable(Boolean downloadable) {
		this.downloadable = downloadable;
	}

	public Boolean getMonitorable() {
		return monitorable;
	}

	public void setMonitorable(Boolean monitorable) {
		this.monitorable = monitorable;
	}

	public Boolean getSecure() {
		return secure;
	}

	public void setSecure(Boolean secure) {
		this.secure = secure;
	}

	public boolean isExpiryInd() {
		return expiryInd;
	}

	public void setExpiryInd(boolean expiryInd) {
		this.expiryInd = expiryInd;
	}

	@Nullable
	public Boolean getAutoAssigned() {
		return autoAssigned;
	}

	public void setAutoAssigned(@Nullable Boolean autoAssigned) {
		this.autoAssigned = autoAssigned;
	}

	@Nullable
	public EnumDocRelatedEntity getRelatedEntity() {
		return relatedEntity;
	}

	public void setRelatedEntity(@Nullable EnumDocRelatedEntity relatedEntity) {
		this.relatedEntity = relatedEntity;
	}

	@Nullable
	public WorkerTypeCode getRelatedSubEntity() {
		return relatedSubEntity;
	}

	public void setRelatedSubEntity(@Nullable WorkerTypeCode relatedSubEntity) {
		this.relatedSubEntity = relatedSubEntity;
	}
}
