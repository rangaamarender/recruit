package com.lucid.recruit.person.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

public class PersonBankDetailsDTO {
	@Nullable
	private String personBankDetailsID;

	@NotBlank
	private String accountName;
	@NotBlank
	private String accountNumber;
	@NotBlank
	private String bankName;
	@NotBlank
	private String branchName;
	@NotBlank
	private String bankID;
	@NotBlank
	private String routingNumber;
	@NotBlank
	private String accountType;
	@Nullable
	private LocalDate startDate;

	@Nullable
	private LocalDate endDate;

	public PersonBankDetailsDTO(){
		super();
	}
	public PersonBankDetailsDTO(String personBankDetailsID ,String accountName, String accountNumber, String bankName, String branchName, String bankID, String routingNumber, String accountType, LocalDate startDate) {
		this.personBankDetailsID = personBankDetailsID;
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.bankName = bankName;
		this.branchName = branchName;
		this.bankID = bankID;
		this.routingNumber = routingNumber;
		this.accountType = accountType;
		this.startDate = startDate;
	}

	public String getPersonBankDetailsID() {
		return personBankDetailsID;
	}

	public void setPersonBankDetailsID(String personBankDetailsID) {
		this.personBankDetailsID = personBankDetailsID;
	}

	@Nullable
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(@Nullable LocalDate startDate) {
		this.startDate = startDate;
	}

	@Nullable
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(@Nullable LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBankID() {
		return bankID;
	}

	public void setBankID(String bankID) {
		this.bankID = bankID;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

}
