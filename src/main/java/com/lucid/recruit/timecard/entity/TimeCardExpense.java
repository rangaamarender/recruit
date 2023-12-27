/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.timecard.entity;

import java.time.LocalDate;

import com.lucid.core.embeddable.Amount;
import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.contract.entity.WorkOrderExpenseCodes;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = TimeCardExpense.TABLE_NAME)
public class TimeCardExpense extends AuditableEntity {

	private static final long serialVersionUID = -2315752050897625175L;
	public static final String TABLE_NAME = "t_timecard_expense";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "expense_id", nullable = false, length = 75)
	private String expenseID;

	@Column(name = "name", nullable = true, length = 50)
	private String name;

	// time card for expenses
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "timecard_id", nullable = false, updatable = false)
	private TimeCard timeCard;

	@Column(name = "expense_date", nullable = false)
	private LocalDate expenseDate;

	// Category under the project link to pay rate
	@Column(name = "expense_code_id", nullable = false)
	private WorkOrderExpenseCodes expenseCode;

	@Embedded
	private Amount amount;

	public String getExpenseID() {
		return expenseID;
	}

	public void setExpenseID(String expenseID) {
		this.expenseID = expenseID;
	}

	public TimeCard getTimeCard() {
		return timeCard;
	}

	public void setTimeCard(TimeCard timeCard) {
		this.timeCard = timeCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(LocalDate expenseDate) {
		this.expenseDate = expenseDate;
	}

	public WorkOrderExpenseCodes getExpenseCode() {
		return expenseCode;
	}

	public void setExpenseCode(WorkOrderExpenseCodes expenseCode) {
		this.expenseCode = expenseCode;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}
}
