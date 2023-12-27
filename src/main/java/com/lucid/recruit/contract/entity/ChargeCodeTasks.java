package com.lucid.recruit.contract.entity;

import java.time.LocalDate;

import com.lucid.core.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = ChargeCodeTasks.TABLE_NAME)
public class ChargeCodeTasks extends AuditableEntity {

	private static final long serialVersionUID = -6150233078212767191L;

	protected static final String TABLE_NAME = "c_chrg_code_tasks";

	// UUID of the task
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "task_id", nullable = false, length = 75)
	private String taskId;

	// charge code name
	@Column(name = "task_name", nullable = false, length = 75)
	private String taskName;

	// task description
	@Column(name = "task_desc", length = 255, nullable = true)
	private String taskDesc;

	// start date of the task
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// end date of the task
	// null represent active still work order end
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	// Associated charge code
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "charge_code_id", nullable = false, updatable = false)
	private WorkOrderChargeCode woChrageCode;

	public ChargeCodeTasks() {
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

	public WorkOrderChargeCode getWoChrageCode() {
		return woChrageCode;
	}

	public void setWoChrageCode(WorkOrderChargeCode woChrageCode) {
		this.woChrageCode = woChrageCode;
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
