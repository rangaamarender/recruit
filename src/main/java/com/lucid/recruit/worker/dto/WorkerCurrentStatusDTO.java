package com.lucid.recruit.worker.dto;

import java.time.LocalDate;

import com.lucid.recruit.worker.entity.WorkerStatusCode;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class WorkerCurrentStatusDTO {
	@Nullable
	private String id;
	@NotNull
	private WorkerStatusCode status;
	@NotNull
	private LocalDate effectiveDate;

	public WorkerCurrentStatusDTO() {
		super();

	}

	public WorkerCurrentStatusDTO(String id, @NotNull WorkerStatusCode status, @NotNull LocalDate effectiveDate) {
		super();
		this.id = id;
		this.status = status;
		this.effectiveDate = effectiveDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public WorkerStatusCode getStatus() {
		return status;
	}

	public void setStatus(WorkerStatusCode status) {
		this.status = status;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

}
