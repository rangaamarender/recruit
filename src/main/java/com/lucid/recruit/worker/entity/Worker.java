package com.lucid.recruit.worker.entity;

import java.time.LocalDate;
import java.util.List;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.contract.entity.ChargeCodeResource;
import com.lucid.recruit.org.entity.Organization;
import com.lucid.recruit.person.entity.PersonLegal;
import com.lucid.recruit.referencedata.entity.WorkerType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/**
 * Worker is a Person Role and is defined as someone who performs labor for an
 * organization.
 * 
 *
 */
@Entity
@Table(name = Worker.TABLE_NAME)
public class Worker extends AuditableEntity {

	// Constants
	private static final long serialVersionUID = -7312399985160551758L;
	public static final String TABLE_NAME = "w_worker";

	// Class Variables
	// Worker Unique Integration ID UUID value, will be used for any integrations
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "worker_id", nullable = false, length = 75)
	private String workerID;

	// Provides a unique identifier to a specific worker (an employer specific
	// identifier), will be display in UI
	@Column(name = "worker_code", nullable = true)
	private String workerCode;

	@Column(name = "joining_date",nullable = true)
	private LocalDate joiningDate;

	// The relationship of the worker to the organization.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "worker_type_code", nullable = false)
	private WorkerType workerType;

	// Information about the person.
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_legal_id", nullable = false)
	private PersonLegal personLegal;

	// Organization that worker belong to
	@ManyToOne
	@JoinColumn(name = "org_id", nullable = true)
	private Organization organization;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "worker")
	@OrderBy(value = "effectiveDate desc ")
	private List<WorkerStatus> workerStatus;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "worker")
	@OrderBy(value = "startDate desc ")
	private List<WorkAssignment> workAssignment;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "worker")
	@OrderBy(value = "workerdocID")
	private List<WorkerDocument> workerDocuments;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "worker")
	private List<WorkerAttributes> workerAttributes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "worker")
	private List<ChargeCodeResource> chargeCodeResource;

	// override for worker, data protection policy will be maintained at Org level
	@Column(name = "data_protction_policy_id", nullable = true)
	private String dataProtectionPolicyId;

	// This determines if a person is covered by wage hour laws.
	@Column(name = "wage_hour_law_coverage_ind", nullable = true, columnDefinition = "boolean default false")
	private boolean wageHRLawCoverageInd;

	// Indicates if the person is part of a labor bargaining unit, such as a union.
	@Column(name = "labor_bargaing_unit_ind", nullable = true, columnDefinition = "boolean default false")
	private boolean laborBargaingUnitInd;

	// An indicator of whether the employee is covered by some type of unemployment
	// insurance.
	@Column(name = "unemply_comp_covergae_ind", nullable = true, columnDefinition = "boolean default false")
	private boolean unEmplyCompCovergaeInd;

	// An indicator of whether the employee is covered by some type of worker
	// compensation insurance.
	@Column(name = "worker_comp_coverage_ind", nullable = true, columnDefinition = "boolean default false")
	private boolean workerCompCoverageInd;

	public Worker() {
		super();
	}

	public String getWorkerID() {
		return workerID;
	}

	public void setWorkerID(String workerID) {
		this.workerID = workerID;
	}

	public String getWorkerCode() {
		return workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public List<ChargeCodeResource> getChargeCodeResource() {
		return chargeCodeResource;
	}

	public void setChargeCodeResource(List<ChargeCodeResource> chargeCodeResource) {
		this.chargeCodeResource = chargeCodeResource;
	}

	public WorkerType getWorkerType() {
		return workerType;
	}

	public void setWorkerType(WorkerType workerType) {
		this.workerType = workerType;
	}

	public PersonLegal getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegal personLegal) {
		this.personLegal = personLegal;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getDataProtectionPolicyId() {
		return dataProtectionPolicyId;
	}

	public void setDataProtectionPolicyId(String dataProtectionPolicyId) {
		this.dataProtectionPolicyId = dataProtectionPolicyId;
	}

	public List<WorkerStatus> getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(List<WorkerStatus> workerStatus) {
		this.workerStatus = workerStatus;
	}

	public boolean isWageHRLawCoverageInd() {
		return wageHRLawCoverageInd;
	}

	public void setWageHRLawCoverageInd(boolean wageHRLawCoverageInd) {
		this.wageHRLawCoverageInd = wageHRLawCoverageInd;
	}

	public boolean isLaborBargaingUnitInd() {
		return laborBargaingUnitInd;
	}

	public void setLaborBargaingUnitInd(boolean laborBargaingUnitInd) {
		this.laborBargaingUnitInd = laborBargaingUnitInd;
	}

	public boolean isUnEmplyCompCovergaeInd() {
		return unEmplyCompCovergaeInd;
	}

	public void setUnEmplyCompCovergaeInd(boolean unEmplyCompCovergaeInd) {
		this.unEmplyCompCovergaeInd = unEmplyCompCovergaeInd;
	}

	public boolean isWorkerCompCoverageInd() {
		return workerCompCoverageInd;
	}

	public void setWorkerCompCoverageInd(boolean workerCompCoverageInd) {
		this.workerCompCoverageInd = workerCompCoverageInd;
	}

	public List<WorkAssignment> getWorkAssignment() {
		return workAssignment;
	}

	public void setWorkAssignment(List<WorkAssignment> workAssignment) {
		this.workAssignment = workAssignment;
	}

	public List<WorkerDocument> getWorkerDocuments() {
		return workerDocuments;
	}

	public void setWorkerDocuments(List<WorkerDocument> workerDocuments) {
		this.workerDocuments = workerDocuments;
	}

	public List<ChargeCodeResource> getContractResource() {
		return chargeCodeResource;
	}

	public void setContractResource(List<ChargeCodeResource> chargeCodeResource) {
		this.chargeCodeResource = chargeCodeResource;
	}

	public List<WorkerAttributes> getWorkerAttributes() {
		return workerAttributes;
	}

	public void setWorkerAttributes(List<WorkerAttributes> workerAttributes) {
		this.workerAttributes = workerAttributes;
	}

}
