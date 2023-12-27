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
@Table(name = WorkOrderExpenseCodes.TABLE_NAME)
public class WorkOrderExpenseCodes extends AuditableEntity {

	private static final long serialVersionUID = -7019257212227079995L;

	protected static final String TABLE_NAME = "c_wrkordr_exp_code";

	// UUID of the expense
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "expense_code_id", nullable = false, length = 75)
	private String expenseCodeId;

	// expense code name
	@Column(name = "expense_code_name", nullable = false, length = 75)
	private String expenseCodeName;

	// can bill to client
	@Column(name = "billable_boo", nullable = false)
	private boolean billable;

	// start date of the expense
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// end date of the expense
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	// Associated charge code
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "charge_code_id", nullable = false, updatable = false)
	private WorkOrderChargeCode woChrageCode;

	public WorkOrderExpenseCodes() {
		super();
	}

	public String getExpenseCodeId() {
		return expenseCodeId;
	}

	public void setExpenseCodeId(String expenseCodeId) {
		this.expenseCodeId = expenseCodeId;
	}

	public String getExpenseCodeName() {
		return expenseCodeName;
	}

	public void setExpenseCodeName(String expenseCodeName) {
		this.expenseCodeName = expenseCodeName;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
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

	public WorkOrderChargeCode getWoChrageCode() {
		return woChrageCode;
	}

	public void setWoChrageCode(WorkOrderChargeCode woChrageCode) {
		this.woChrageCode = woChrageCode;
	}

}
