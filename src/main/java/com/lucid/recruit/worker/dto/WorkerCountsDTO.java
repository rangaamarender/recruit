package com.lucid.recruit.worker.dto;

import java.util.List;

public class WorkerCountsDTO {
	private long totalWorkers;
	private List<WorkerTypeCountDTO> workerTypes;
	private List<WorkerStatusCountsDTO> workerStatus;
	private List<WorkerCountsByBillableDTO> workerBillable;

	public long getTotalWorkers() {
		return totalWorkers;
	}

	public void setTotalWorkers(long totalWorkers) {
		this.totalWorkers = totalWorkers;
	}

	public List<WorkerTypeCountDTO> getWorkerTypes() {
		return workerTypes;
	}

	public void setWorkerTypes(List<WorkerTypeCountDTO> workerTypes) {
		this.workerTypes = workerTypes;
	}

	public List<WorkerStatusCountsDTO> getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(List<WorkerStatusCountsDTO> workerStatus) {
		this.workerStatus = workerStatus;
	}

	public List<WorkerCountsByBillableDTO> getWorkerBillable() {
		return workerBillable;
	}

	public void setWorkerBillable(List<WorkerCountsByBillableDTO> workerBillable) {
		this.workerBillable = workerBillable;
	}

}
