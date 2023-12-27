package com.lucid.recruit.worker.dto;

import java.util.List;

import com.lucid.recruit.docs.dto.BaseDocumentDTO;

import jakarta.annotation.Nullable;

public class WorkerDocumentDTO extends BaseDocumentDTO {
	@Nullable
	private String workerdocID;
	@Nullable
	private List<WorkerDocAttributesDTO> workerDocAttributes;

	public WorkerDocumentDTO() {
		super();
	}

	public String getWorkerdocID() {
		return workerdocID;
	}

	public void setWorkerdocID(String workerdocID) {
		this.workerdocID = workerdocID;
	}

	public List<WorkerDocAttributesDTO> getWorkerDocAttributes() {
		return workerDocAttributes;
	}

	public void setWorkerDocAttributes(List<WorkerDocAttributesDTO> workerDocAttributes) {
		this.workerDocAttributes = workerDocAttributes;
	}
}
