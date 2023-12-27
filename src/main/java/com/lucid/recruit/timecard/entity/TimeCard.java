/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.timecard.entity;

import java.time.LocalDate;
import java.util.List;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.contract.entity.Contract;
import com.lucid.recruit.contract.entity.ContractWorkOrder;
import com.lucid.recruit.timecard.constants.EnumTimeCardStatus;
import com.lucid.recruit.worker.entity.Worker;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = TimeCard.TABLE_NAME)
public class TimeCard extends AuditableEntity {

	private static final long serialVersionUID = -619170040525323189L;
	public static final String TABLE_NAME = "t_timecard";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "timecard_id", nullable = false, length = 75)
	private String timecardID;

	// Worker whose time card is for
	@ManyToOne
	@JoinColumn(name = "worker_id", nullable = false, updatable = false)
	private Worker worker;

	// Contract id
	@ManyToOne
	@JoinColumn(name = "cntrct_id", nullable = false, updatable = false)
	private Contract contract;

	// Contract work order id
	@ManyToOne
	@JoinColumn(name = "c_wrk_order_id", nullable = false, updatable = false)
	private ContractWorkOrder workOrder;

	@Column(name = "start_dat", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_dat", nullable = false)
	private LocalDate endDate;

	@Column(name = "total_time", nullable = false)
	private double totalHours;

	@Column(name = "timecard_ver", nullable = true)
	private int timecardVersion;

	@Enumerated(EnumType.STRING)
	@Column(name = "timecard_status", nullable = false, length = 25)
	private EnumTimeCardStatus timeCardStatus;

	@Column(name = "approved_by", nullable = true, length = 75)
	private String approvedBy;

	@Column(name = "approved_date", nullable = true, length = 75)
	private LocalDate approvedDate;

	@Column(name = "rejected_by", nullable = true, length = 75)
	private String rejectedBy;

	@Column(name = "rejected_date", nullable = true, length = 75)
	private LocalDate rejectedDate;

	// List of Time Card Items
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "timeCard")
	private List<TimeCardItem> timeCardItems;

	// List of Time Card Expenses
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "timeCard")
	private List<TimeCardExpense> timeCardExpenses;

	// List of Time card documents
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "timeCard")
	private List<TimeCardDocument> timeCardDocuments;

	// List of Time card expenses documents
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "timeCard")
	private List<TimeCardExpenseDocument> timeCardExpenseDocuments;

	// List of Time card actions
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "timeCard")
	private List<TimeCardApprovalActions> timeCardApprovalAction;

	public TimeCard() {
		super();
	}

	public String getTimecardID() {
		return timecardID;
	}

	public void setTimecardID(String timecardID) {
		this.timecardID = timecardID;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public ContractWorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(ContractWorkOrder workOrder) {
		this.workOrder = workOrder;
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

	public int getTimecardVersion() {
		return timecardVersion;
	}

	public void setTimecardVersion(int timecardVersion) {
		this.timecardVersion = timecardVersion;
	}

	public EnumTimeCardStatus getTimeCardStatus() {
		return timeCardStatus;
	}

	public void setTimeCardStatus(EnumTimeCardStatus timeCardStatus) {
		this.timeCardStatus = timeCardStatus;
	}

	public List<TimeCardItem> getTimeCardItems() {
		return timeCardItems;
	}

	public void setTimeCardItems(List<TimeCardItem> timeCardItems) {
		this.timeCardItems = timeCardItems;
	}

	public List<TimeCardExpense> getTimeCardExpenses() {
		return timeCardExpenses;
	}

	public void setTimeCardExpenses(List<TimeCardExpense> timeCardExpenses) {
		this.timeCardExpenses = timeCardExpenses;
	}

	public List<TimeCardDocument> getTimeCardDocuments() {
		return timeCardDocuments;
	}

	public void setTimeCardDocuments(List<TimeCardDocument> timeCardDocuments) {
		this.timeCardDocuments = timeCardDocuments;
	}

	public List<TimeCardExpenseDocument> getTimeCardExpenseDocuments() {
		return timeCardExpenseDocuments;
	}

	public void setTimeCardExpenseDocuments(List<TimeCardExpenseDocument> timeCardExpenseDocuments) {
		this.timeCardExpenseDocuments = timeCardExpenseDocuments;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDate getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDate approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getRejectedBy() {
		return rejectedBy;
	}

	public void setRejectedBy(String rejectedBy) {
		this.rejectedBy = rejectedBy;
	}

	public LocalDate getRejectedDate() {
		return rejectedDate;
	}

	public void setRejectedDate(LocalDate rejectedDate) {
		this.rejectedDate = rejectedDate;
	}

	public List<TimeCardApprovalActions> getTimeCardApprovalAction() {
		return timeCardApprovalAction;
	}

	public void setTimeCardApprovalAction(List<TimeCardApprovalActions> timeCardApprovalAction) {
		this.timeCardApprovalAction = timeCardApprovalAction;
	}

}
