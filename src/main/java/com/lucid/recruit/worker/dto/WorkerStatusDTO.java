package com.lucid.recruit.worker.dto;

import java.time.LocalDate;

import com.lucid.recruit.referencedata.dto.WorkerInActiveStatusCodesDTO;
import com.lucid.recruit.worker.entity.WorkerStatusCode;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class WorkerStatusDTO {
	@Nullable
	private String workerStatusId;
	@NotNull
	private WorkerStatusCode status;
	@Nullable
	private LocalDate effectiveDate;

	@Nullable
	private WorkerInActiveStatusCodesDTO workerInActiveStatusCode;
	@Nullable
	private String reasonCode;
	@Nullable
	private String statusReasonText;

	public WorkerStatusDTO() {
		super();
	}

	public WorkerStatusDTO(WorkerStatusCode status, LocalDate effectiveDate, String reasonCode, String statusReasonText) {
		super();
		this.status = status;
		this.effectiveDate = effectiveDate;
		this.reasonCode = reasonCode;
		this.statusReasonText = statusReasonText;
	}

	public WorkerStatusDTO(String workerStatusId, @NotNull WorkerStatusCode status, @NotNull LocalDate effectiveDate,
			@NotNull String reasonCode, @NotNull String statusReasonText) {
		super();
		this.workerStatusId = workerStatusId;
		this.status = status;
		this.effectiveDate = effectiveDate;
		this.reasonCode = reasonCode;
		this.statusReasonText = statusReasonText;
	}

	@Nullable
	public String getWorkerStatusId() {
		return workerStatusId;
	}

	public void setWorkerStatusId(@Nullable String workerStatusId) {
		this.workerStatusId = workerStatusId;
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

	@Nullable
	public WorkerInActiveStatusCodesDTO getWorkerInActiveStatusCode() {
		return workerInActiveStatusCode;
	}

	public void setWorkerInActiveStatusCode(@Nullable WorkerInActiveStatusCodesDTO workerInActiveStatusCode) {
		this.workerInActiveStatusCode = workerInActiveStatusCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getStatusReasonText() {
		return statusReasonText;
	}

	public void setStatusReasonText(String statusReasonText) {
		this.statusReasonText = statusReasonText;
	}
}
