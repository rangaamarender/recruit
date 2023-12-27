package com.lucid.recruit.person.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.entity.AuditableEntity;

import com.lucid.recruit.org.entity.OrgAddress;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = PersonBankDetails.TABLE_NAME)
public class PersonBankDetails extends AuditableEntity {

	private static final long serialVersionUID = 7609349402750903434L;
	public static final String TABLE_NAME = "p_prsn_bank_details";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "prsn_bank_details_id", nullable = false, length = 75)
	private String personBankDetailsID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_legal_id", nullable = false,updatable = false)
	private PersonLegal personLegal;
	
	// The name of the person on the bank account
	@Column(name = "account_name", nullable = false)
	private String accountName;

	// The bank account number
	@Column(name = "account_number", nullable = false)
	private String accountNumber;

	// The bank account number
	@Column(name = "bank_name", nullable = false)
	private String bankName;

	// The bank branch name
	@Column(name = "branch_name", nullable = false)
	private String branchName;

	// The bank branch id
	@Column(name = "bank_id", nullable = false)
	private String bankID;

	// The bank routing number
	@Column(name = "routing_number", nullable = false)
	private String routingNumber;

	// The bank accountTyoe
	@Column(name = "account_type", nullable = false)
	private String accountType;
	
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	public String getPersonBankDetailsID() {
		return personBankDetailsID;
	}

	public void setPersonBankDetailsID(String personBankDetailsID) {
		this.personBankDetailsID = personBankDetailsID;
	}

	public PersonLegal getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegal personLegal) {
		this.personLegal = personLegal;
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

	public static Comparator<PersonBankDetails> createPersonBankDetailsComparator() {
		return Comparator.comparing(PersonBankDetails::getStartDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}

		PersonBankDetails personBankDetails = (PersonBankDetails) obj;

		if (getBankID() == null) {
			if (personBankDetails.getBankID() != null) {
				return false;
			}
		} else if (!getBankID().equals(personBankDetails.getBankID())) {
			return false;
		}
		if (getAccountName() == null) {
			if (personBankDetails.getAccountName() != null) {
				return false;
			}
		} else if (!getAccountName().equals(personBankDetails.getAccountName())) {
			return false;
		}
		if (getBankName() == null) {
			if (personBankDetails.getBankName() != null) {
				return false;
			}
		} else if (!getBankName().equals(personBankDetails.getBankName())) {
			return false;
		}
		if (getAccountNumber() == null) {
			if (personBankDetails.getAccountNumber() != null) {
				return false;
			}
		} else if (!getAccountNumber().equals(personBankDetails.getAccountNumber())) {
			return false;
		}
		if (getAccountType() == null) {
			if (personBankDetails.getAccountType() != null) {
				return false;
			}
		} else if (!getAccountType().equals(personBankDetails.getAccountType())) {
			return false;
		}
		if (getBranchName() == null) {
			if (personBankDetails.getBranchName() != null) {
				return false;
			}
		} else if (!getBranchName().equals(personBankDetails.getBranchName())) {
			return false;
		}
		if (getRoutingNumber() == null) {
			if (personBankDetails.getRoutingNumber() != null) {
				return false;
			}
		} else if (!getRoutingNumber().equals(personBankDetails.getRoutingNumber())) {
			return false;
		}
		return true;
	}
}
