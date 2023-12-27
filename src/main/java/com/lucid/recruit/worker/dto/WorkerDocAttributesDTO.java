package com.lucid.recruit.worker.dto;

import com.lucid.recruit.docs.dto.BaseDocAttributesDTO;

import jakarta.annotation.Nullable;

public class WorkerDocAttributesDTO extends BaseDocAttributesDTO {
	@Nullable
	private String workerDocAttrID;

	public String getWorkerDocAttrID() {
		return workerDocAttrID;
	}

	public void setWorkerDocAttrID(String workerDocAttrID) {
		this.workerDocAttrID = workerDocAttrID;
	}
}
