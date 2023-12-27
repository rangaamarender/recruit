package com.lucid.recruit.worker.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lucid.recruit.org.dto.OrganizationDTO;
import com.lucid.recruit.org.dto.RelatedOrgDTO;
import com.lucid.recruit.person.dto.PersonLegalDTO;
import com.lucid.recruit.referencedata.dto.RelatedDepartmentDTO;
import com.lucid.recruit.referencedata.dto.RelatedWorkerTypeDTO;

import com.lucid.recruit.worker.entity.WorkerAttributes;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class WorkerDTO {
	@Nullable
	private String workerID;
	@Nullable
	private String workerCode;

	@NotNull
	private LocalDate joiningDate;
	@NotNull
	@Valid
	private RelatedWorkerTypeDTO workerType;
	@NotNull
	@Valid
	private RelatedDepartmentDTO department;
	@NotNull
	private boolean billable;
	@NotNull
	@Valid
	private PersonLegalDTO personLegal;
	@JsonIgnoreProperties({ "description", "taxId" })
	@Nullable
	private RelatedOrgDTO organization;
	@Nullable
	private List<WorkerStatusDTO> workerStatus;
	@Nullable
	private List<WorkAssignmentDTO> workAssignment;
	@Nullable
	private List<WorkerDocumentDTO> workerDocuments;
	@Nullable
	@Valid
	List<WorkerAttributesDTO> workerAttributes;

	public WorkerDTO() {
		super();
	}

	public WorkerDTO(String workerID, String workerCode, @NotNull RelatedWorkerTypeDTO workerType,
			@NotNull PersonLegalDTO personLegal, @NotNull RelatedOrgDTO organization,
			@NotNull List<WorkerStatusDTO> workerStatus, @NotNull List<WorkAssignmentDTO> workAssignment) {
		super();
		this.workerID = workerID;
		this.workerCode = workerCode;
		this.workerType = workerType;
		this.personLegal = personLegal;
		this.organization = organization;
		this.workerStatus = workerStatus;
		this.workAssignment = workAssignment;
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

	public RelatedWorkerTypeDTO getWorkerType() {
		return workerType;
	}

	@Nullable
	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(@Nullable LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}
	public void setWorkerType(RelatedWorkerTypeDTO workerType) {
		this.workerType = workerType;
	}

	public PersonLegalDTO getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegalDTO personLegal) {
		this.personLegal = personLegal;
	}

	public RelatedOrgDTO getOrganization() {
		return organization;
	}

	public void setOrganization(RelatedOrgDTO organization) {
		this.organization = organization;
	}

	public List<WorkerStatusDTO> getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(List<WorkerStatusDTO> workerStatus) {
		this.workerStatus = workerStatus;
	}

	public List<WorkAssignmentDTO> getWorkAssignment() {
		return workAssignment;
	}

	public void setWorkAssignment(List<WorkAssignmentDTO> workAssignment) {
		this.workAssignment = workAssignment;
	}
	public List<WorkerDocumentDTO> getWorkerDocuments() {
		return workerDocuments;
	}
	public void setWorkerDocuments(List<WorkerDocumentDTO> workerDocuments) {
		this.workerDocuments = workerDocuments;
	}
	@Nullable
	public List<WorkerAttributesDTO> getWorkerAttributes() {
		return workerAttributes;
	}
	public void setWorkerAttributes(@Nullable List<WorkerAttributesDTO> workerAttributes) {
		this.workerAttributes = workerAttributes;
	}

	public RelatedDepartmentDTO getDepartment() {
		return department;
	}

	public void setDepartment(RelatedDepartmentDTO department) {
		this.department = department;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}
}
