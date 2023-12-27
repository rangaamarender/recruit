package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RelatedChargeCodeTasksDTO {

	@NotNull
	private String taskId;
	@Nullable
	private String taskName;
	@Nullable
	private String chargeCodeId;
	@Nullable
	private String chargeCodeName;

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

	public String getChargeCodeId() {
		return chargeCodeId;
	}

	public void setChargeCodeId(String chargeCodeId) {
		this.chargeCodeId = chargeCodeId;
	}

	public String getChargeCodeName() {
		return chargeCodeName;
	}

	public void setChargeCodeName(String chargeCodeName) {
		this.chargeCodeName = chargeCodeName;
	}

}
