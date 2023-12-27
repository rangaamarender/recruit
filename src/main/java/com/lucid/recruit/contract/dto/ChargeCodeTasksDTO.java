package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ChargeCodeTasksDTO {


	// UUID of the task
	@Nullable
	private String taskId;

	// charge code name
	@NotBlank
	private String taskName;

	// task description
	@Nullable
	private String taskDesc;

	// start date of the task
	@NotNull
	private LocalDate startDate;

	// end date of the task
	// null represent active still work order end
	@Nullable
	private LocalDate endDate;

	public ChargeCodeTasksDTO() {
		super();
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
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

}
