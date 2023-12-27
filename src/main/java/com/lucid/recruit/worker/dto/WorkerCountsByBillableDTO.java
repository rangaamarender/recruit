package com.lucid.recruit.worker.dto;

public class WorkerCountsByBillableDTO {
	private boolean billable;
	private Long count;

	public WorkerCountsByBillableDTO(boolean billable, Long count) {
		super();
		this.billable = billable;
		this.count = count;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
