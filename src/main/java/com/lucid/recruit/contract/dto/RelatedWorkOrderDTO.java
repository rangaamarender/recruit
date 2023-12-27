package com.lucid.recruit.contract.dto;

import com.lucid.recruit.timecard.dto.TimeCardExpenseDTO;
import com.lucid.recruit.timecard.dto.TimeCardItemDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/***
 * 
 * DTO represent the related work order
 *
 */
public class RelatedWorkOrderDTO {
	@NotNull
	private String workOrderID;
	@Nullable
	private String workorderName;

	@NotNull(message = "Start date is required and cannot be null")
	private LocalDate startDate;

	@NotNull(message = "End date is required and cannot be null")
	private LocalDate endDate;

	@NotNull(message = "Total hours are required and cannot be null")
	private double totalHours;

	@Nullable
	private String approvedBy;

	@Nullable
	private LocalDate approvedDate;

	@Nullable
	private String rejectedBy;

	@Nullable
	private LocalDate rejectedDate;


	@NotNull(message = "TimeCard item are required and cannot be null")
	@NotEmpty
	@Valid
	private List<TimeCardItemDTO> timeCardItems;

	@Nullable
	@Valid
	private List<TimeCardExpenseDTO> timeCardExpenses;

	public String getWorkOrderID() {
		return workOrderID;
	}

	public void setWorkOrderID(String workOrderID) {
		this.workOrderID = workOrderID;
	}

	public String getWorkorderName() {
		return workorderName;
	}

	public void setWorkorderName(String workorderName) {
		this.workorderName = workorderName;
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

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	@Nullable
	public String getApprovedBy() {return approvedBy;}

	public void setApprovedBy(@Nullable String approvedBy) {this.approvedBy = approvedBy;}

	@Nullable
	public LocalDate getApprovedDate() {return approvedDate;}

	public void setApprovedDate(@Nullable LocalDate approvedDate) {this.approvedDate = approvedDate;}

	@Nullable
	public String getRejectedBy() {return rejectedBy;}

	public void setRejectedBy(@Nullable String rejectedBy) {this.rejectedBy = rejectedBy;}

	@Nullable
	public LocalDate getRejectedDate() {return rejectedDate;}

	public void setRejectedDate(@Nullable LocalDate rejectedDate) {this.rejectedDate = rejectedDate;}

	public List<TimeCardItemDTO> getTimeCardItems() {
		return timeCardItems;
	}

	public void setTimeCardItems(List<TimeCardItemDTO> timeCardItems) {
		this.timeCardItems = timeCardItems;
	}

	@Nullable
	public List<TimeCardExpenseDTO> getTimeCardExpenses() {
		return timeCardExpenses;
	}

	public void setTimeCardExpenses(@Nullable List<TimeCardExpenseDTO> timeCardExpenses) {this.timeCardExpenses = timeCardExpenses;}
}
