package com.lucid.recruit.worker.dto;

import com.lucid.recruit.worker.entity.WorkerStatusCode;

public class WorkerStatusCountsDTO {

	private WorkerStatusCode workerStatuCode;
	private Long count;

	public WorkerStatusCountsDTO(WorkerStatusCode workerStatuCode, Long count) {
		super();
		this.workerStatuCode = workerStatuCode;
		this.count = count;
	}

	public WorkerStatusCode getWorkerStatuCode() {
		return workerStatuCode;
	}

	public void setWorkerStatuCode(WorkerStatusCode workerStatuCode) {
		this.workerStatuCode = workerStatuCode;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
