package com.lucid.recruit.worker.dto;

import com.lucid.recruit.attr.dto.RefAttributeDefDTO;
import com.lucid.recruit.attr.dto.WorkerAttributeDefDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class WorkerAttributesDTO {

	@Nullable
	private String workerAttrID;
	@NotNull
	private RefAttributeDefDTO workerAttributeDef;
	@Nullable
	private String attrValue;

	@Nullable
	private LocalDate startDate;

	@Nullable
	private LocalDate endDate;

	public WorkerAttributesDTO() {
		super();
	}

	public String getWorkerAttrID() {
		return workerAttrID;
	}

	public void setWorkerAttrID(String workerAttrID) {
		this.workerAttrID = workerAttrID;
	}

	public RefAttributeDefDTO getWorkerAttributeDef() {
		return workerAttributeDef;
	}

	public void setWorkerAttributeDef(RefAttributeDefDTO workerAttributeDef) {
		this.workerAttributeDef = workerAttributeDef;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

}
