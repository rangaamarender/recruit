package com.lucid.recruit.timecard.entity;

import com.lucid.recruit.contract.constants.EnumTimeCardApproval;

import com.lucid.recruit.timecard.constants.EnumTimeCardStepStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = TimeCardApprovalActions.TABLE_NAME)
public class TimeCardApprovalActions {
	public static final String TABLE_NAME = "t_tc_approvalaction";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "tcaction_id", nullable = false, length = 75)
	private String tcActionId;

	// time card
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "timecard_id", nullable = false, updatable = false)
	private TimeCard timeCard;

	@Column(name = "total_steps", nullable = false)
	private int totalSteps;

	@Column(name = "step_number", nullable = false)
	private int stepNumber;

	@Column(name = "final_step", nullable = false)
	private boolean finalStep;

	@Enumerated(EnumType.STRING)
	@Column(name = "approval_flow", nullable = false)
	private EnumTimeCardApproval tsApprovalFlow;

	@Enumerated(EnumType.STRING)
	@Column(name = "step_status", nullable = false)
	private EnumTimeCardStepStatus stepStatus;

	@Column(name = "approver", nullable = false, length = 100)
	private String approver;

	@Column(name = "reminder_count", nullable = false)
	private int reminderCount;

	public String getTcActionId() {
		return tcActionId;
	}

	public void setTcActionId(String tcActionId) {
		this.tcActionId = tcActionId;
	}

	public TimeCard getTimeCard() {
		return timeCard;
	}

	public void setTimeCard(TimeCard timeCard) {
		this.timeCard = timeCard;
	}

	public int getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(int totalSteps) {
		this.totalSteps = totalSteps;
	}

	public int getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	public boolean isFinalStep() {
		return finalStep;
	}

	public void setFinalStep(boolean finalStep) {
		this.finalStep = finalStep;
	}

	public EnumTimeCardApproval getTsApprovalFlow() {
		return tsApprovalFlow;
	}

	public void setTsApprovalFlow(EnumTimeCardApproval tsApprovalFlow) {
		this.tsApprovalFlow = tsApprovalFlow;
	}

	public EnumTimeCardStepStatus getStepStatus() {
		return stepStatus;
	}

	public void setStepStatus(EnumTimeCardStepStatus stepStatus) {this.stepStatus = stepStatus;}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public int getReminderCount() {
		return reminderCount;
	}

	public void setReminderCount(int reminderCount) {
		this.reminderCount = reminderCount;
	}

}
