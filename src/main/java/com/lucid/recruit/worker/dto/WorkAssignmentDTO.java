package com.lucid.recruit.worker.dto;

import java.time.LocalDate;

import com.lucid.recruit.referencedata.dto.JobDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class WorkAssignmentDTO {
	@Nullable
	private String workerAssgmtId;
	@NotNull
	private JobDTO job;
	@NotNull
	private LocalDate startDate;
	@Nullable
	private LocalDate endDate;
	@NotNull
	private LocalDate expectedStartDate;
	@Nullable
	private LocalDate poStartDate;
	@Nullable	
	private LocalDate poEndDate;

	public WorkAssignmentDTO() {
		super();
	}

	public WorkAssignmentDTO(String workerAssgmtId, @NotNull JobDTO job, @NotNull LocalDate startDate, LocalDate endDate,
			@NotNull LocalDate expectedStartDate, LocalDate poStartDate, LocalDate poEndDate) {
		super();
		this.workerAssgmtId = workerAssgmtId;
		this.job = job;
		this.startDate = startDate;
		this.endDate = endDate;
		this.expectedStartDate = expectedStartDate;
		this.poStartDate = poStartDate;
		this.poEndDate = poEndDate;
	}

	public String getWorkerAssgmtId() {
		return workerAssgmtId;
	}

	public void setWorkerAssgmtId(String workerAssgmtId) {
		this.workerAssgmtId = workerAssgmtId;
	}

	public JobDTO getJob() {
		return job;
	}

	public void setJob(JobDTO job) {
		this.job = job;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getExpectedStartDate() {
		return expectedStartDate;
	}

	public void setExpectedStartDate(LocalDate expectedStartDate) {
		this.expectedStartDate = expectedStartDate;
	}

	public LocalDate getPoStartDate() {
		return poStartDate;
	}

	public void setPoStartDate(LocalDate poStartDate) {
		this.poStartDate = poStartDate;
	}

	public LocalDate getPoEndDate() {
		return poEndDate;
	}

	public void setPoEndDate(LocalDate poEndDate) {
		this.poEndDate = poEndDate;
	}

}
