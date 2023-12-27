package com.lucid.recruit.attr.dto;

import com.lucid.recruit.referencedata.dto.RelatedWorkerTypeDTO;
import jakarta.annotation.Nullable;

import java.util.List;

public class WorkerAttributeDefDTO extends BaseAttributeDefDTO{
	@Nullable
	private String attrDefId;
	@Nullable
	private RelatedWorkerTypeDTO workerType;

	@Nullable
	private List<WorkerAttrDefListValuesDTO> attrListValues;

	@Nullable
	public String getAttrDefId() {
		return attrDefId;
	}

	public void setAttrDefId(@Nullable String attrDefId) {
		this.attrDefId = attrDefId;
	}

	@Nullable
	public RelatedWorkerTypeDTO getWorkerType() {
		return workerType;
	}

	public void setWorkerType(@Nullable RelatedWorkerTypeDTO workerType) {
		this.workerType = workerType;
	}

	@Nullable
	public List<WorkerAttrDefListValuesDTO> getAttrListValues() {
		return attrListValues;
	}

	public void setAttrListValues(@Nullable List<WorkerAttrDefListValuesDTO> attrListValues) {
		this.attrListValues = attrListValues;
	}
}
