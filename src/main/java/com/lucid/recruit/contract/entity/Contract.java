/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.List;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.contract.constants.EnumContractStatus;
import com.lucid.recruit.org.entity.Organization;

import jakarta.persistence.*;

/**
 * 
 * Contract entity allows to define the type of contract, WO/PAY Rates and
 * on-boards the resource to client
 *
 */
@Entity
@Table(name = Contract.TABLE_NAME)
public class Contract extends AuditableEntity {

	private static final long serialVersionUID = 1067742755203960531L;
	public static final String TABLE_NAME = "c_contract";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "cntrct_id", nullable = false, length = 75, updatable = false)
	private String contractID;

	// Name (title) of the MSA
	@Column(name = "contract_name", nullable = false, length = 255,unique = true)
	private String contractName;

	// organization id of the party B
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "related_org_id", referencedColumnName = "org_id", nullable = false)
	private Organization relatedOrg;

	// MSA start date
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// MSA end date null represent active forever
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	// Billing Currency code (ISO 4217)
	@Column(name = "billing_curr_code", nullable = true, length = 3)
	private String billingCurrCode;

	// MSA notes
	@Column(name = "notes", nullable = true, length = 2500)
	private String notes;

	// contract status like Active,Terminated
	@Enumerated(value = EnumType.STRING)
	@Column(name = "contract_status", nullable = false)
	private EnumContractStatus contractStatus;

	// MSA termination date
	@Column(name = "termination_date", nullable = true)
	private LocalDate terminationDate;

	// termination code
	@Column(name = "termination_reason_code", nullable = true, length = 50)
	private String terminationReasonCode;

	// termination reason text
	@Column(name = "termination_reason_notes", nullable = true, length = 2500)
	private String terminationReasonNotes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
	private List<ContractAccount> contractAccounts;

	public Contract() {
		super();
	}

	public String getContractID() {
		return contractID;
	}

	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Organization getRelatedOrg() {
		return relatedOrg;
	}

	public void setRelatedOrg(Organization relatedOrg) {
		this.relatedOrg = relatedOrg;
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

	public String getBillingCurrCode() {
		return billingCurrCode;
	}

	public void setBillingCurrCode(String billingCurrCode) {
		this.billingCurrCode = billingCurrCode;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public EnumContractStatus getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(EnumContractStatus contractStatus) {
		this.contractStatus = contractStatus;
	}

	public LocalDate getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(LocalDate terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getTerminationReasonCode() {
		return terminationReasonCode;
	}

	public void setTerminationReasonCode(String terminationReasonCode) {
		this.terminationReasonCode = terminationReasonCode;
	}

	public String getTerminationReasonNotes() {
		return terminationReasonNotes;
	}

	public void setTerminationReasonNotes(String terminationReasonNotes) {
		this.terminationReasonNotes = terminationReasonNotes;
	}

	public List<ContractAccount> getContractAccounts() {
		return contractAccounts;
	}

	public void setContractAccounts(List<ContractAccount> contractAccounts) {
		this.contractAccounts = contractAccounts;
	}
}
