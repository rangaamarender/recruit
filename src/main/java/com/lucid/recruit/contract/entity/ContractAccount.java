/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.List;

import com.lucid.core.entity.AuditableEntity;

import jakarta.persistence.*;

/**
 * 
 * Contract work order group, groups all the work orders having same discounts
 * Billing characteristics
 *
 */
@Entity
@Table(name = ContractAccount.TABLE_NAME,uniqueConstraints = {@UniqueConstraint(name = "UNIQUECONTRACTACCOUNT",columnNames = {"cntrct_account_name","cntrct_id"})})
public class ContractAccount extends AuditableEntity {

	private static final long serialVersionUID = 1067742755203960531L;
	public static final String TABLE_NAME = "c_contract_account";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "cntrct_account_id", nullable = false, length = 75, updatable = false)
	private String contractAccountId;

	// Name (title) of the MSA
	@Column(name = "cntrct_account_name", nullable = false, length = 255)
	private String contractAccountName;

	// Contract ID
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cntrct_id", nullable = false, updatable = false)
	private Contract contract;

	// MSA notes
	@Column(name = "notes", nullable = true, length = 2500)
	private String notes;

	@Column(name = "start_dt",nullable = false)
	private LocalDate startDate;

	// Date when the next bill shall be generated
	@Column(name = "next_bill_date", nullable = false)
	private LocalDate nextBillDate;

	// Date when the last bill was generated
	@Column(name = "last_bill_date", nullable = true)
	private LocalDate lastBillDate;
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "contractAccount")
	private List<ContractAccountStatus> getAccountStatuses;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractAccount")
	private List<ContractBillingDetails> contractBillingDetails;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractAccount")
	private List<ContractDiscount> contractDiscounts;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractAccount")
	private List<ContractExpense> contractExpenses;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractAccount")
	private List<ContractWorkOrder> contractWorkOrders;

	public ContractAccount() {
		super();
	}

	public String getContractAccountId() {
		return contractAccountId;
	}

	public void setContractAccountId(String contractAccountId) {
		this.contractAccountId = contractAccountId;
	}

	public String getContractAccountName() {
		return contractAccountName;
	}

	public void setContractAccountName(String contractAccountName) {
		this.contractAccountName = contractAccountName;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getNextBillDate() {
		return nextBillDate;
	}

	public void setNextBillDate(LocalDate nextBillDate) {
		this.nextBillDate = nextBillDate;
	}

	public LocalDate getLastBillDate() {
		return lastBillDate;
	}

	public void setLastBillDate(LocalDate lastBillDate) {
		this.lastBillDate = lastBillDate;
	}

	public List<ContractAccountStatus> getGetAccountStatuses() {
		return getAccountStatuses;
	}

	public void setGetAccountStatuses(List<ContractAccountStatus> getAccountStatuses) {
		this.getAccountStatuses = getAccountStatuses;
	}

	public List<ContractBillingDetails> getContractBillingDetails() {
		return contractBillingDetails;
	}

	public void setContractBillingDetails(List<ContractBillingDetails> contractBillingDetails) {
		this.contractBillingDetails = contractBillingDetails;
	}

	public List<ContractDiscount> getContractDiscounts() {
		return contractDiscounts;
	}

	public void setContractDiscounts(List<ContractDiscount> contractDiscounts) {
		this.contractDiscounts = contractDiscounts;
	}

	public List<ContractWorkOrder> getContractWorkOrders() {
		return contractWorkOrders;
	}

	public void setContractWorkOrders(List<ContractWorkOrder> contractWorkOrders) {
		this.contractWorkOrders = contractWorkOrders;
	}

	public List<ContractExpense> getContractExpenses() {
		return contractExpenses;
	}

	public void setContractExpenses(List<ContractExpense> contractExpenses) {
		this.contractExpenses = contractExpenses;
	}
}
