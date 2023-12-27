package com.lucid.recruit.timecard.dto;

import java.time.LocalDate;

import com.lucid.recruit.contract.dto.RelatedChargeCodeTasksDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class TimeCardItemDTO {

	private String timecardItemID;
	@NotNull
	private LocalDate itemDate;
	@NotNull
	private RelatedChargeCodeTasksDTO chargeCodeTask;
	@NotNull
	private double hours;
	@Nullable
	private String comment;
	@Nullable
	private String statusComments;

	@Nullable
	public String getTimecardItemID() {
		return timecardItemID;
	}

	public void setTimecardItemID(@Nullable String timecardItemID) {
		this.timecardItemID = timecardItemID;
	}

	public LocalDate getItemDate() {
		return itemDate;
	}

	public void setItemDate(LocalDate itemDate) {
		this.itemDate = itemDate;
	}

	public RelatedChargeCodeTasksDTO getChargeCodeTask() {
		return chargeCodeTask;
	}

	public void setChargeCodeTask(RelatedChargeCodeTasksDTO chargeCodeTask) {
		this.chargeCodeTask = chargeCodeTask;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(@Nullable String comment) {
		this.comment = comment;
	}

	public String getStatusComments() {
		return statusComments;
	}

	public void setStatusComments(@Nullable String statusComments) {
		this.statusComments = statusComments;
	}
}
