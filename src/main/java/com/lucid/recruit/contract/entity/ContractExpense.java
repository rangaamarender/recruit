package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.Comparator;

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
@Table(name = ContractExpense.TABLE_NAME)
public class ContractExpense extends AuditableEntity {

	private static final long serialVersionUID = 1016586762382630927L;

	public static final String TABLE_NAME = "c_contract_expenses";

	// UUID for each discount
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "expense_id", length = 75, nullable = false)
	private String expenseId;

	// Name of the discount, will be displayed on invoice if required
	@Column(name = "expense_name", length = 75, nullable = false)
	private String expenseName;

	// Name of the discount description
	@Column(name = "expense_desc", length = 255, nullable = true)
	private String expenseDesc;

	@Column(name = "expense_amount", nullable = true)
	private double expenseAmount;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cntrct_account_id", nullable = false, updatable = false)
	private ContractAccount contractAccount;

	// Date from which expense is active
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// Last day on of the expense
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	public ContractExpense() {
		super();
	}

	public String getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}

	public String getExpenseName() {
		return expenseName;
	}

	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}

	public String getExpenseDesc() {
		return expenseDesc;
	}

	public void setExpenseDesc(String expenseDesc) {
		this.expenseDesc = expenseDesc;
	}

	public ContractAccount getContractAccount() {
		return contractAccount;
	}

	public void setContractAccount(ContractAccount contractAccount) {
		this.contractAccount = contractAccount;
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

	public double getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(double expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public static Comparator<ContractExpense> createContractExpenseLambdaComparator() {
		return Comparator.comparing(ContractExpense::getStartDate);
	}

}
