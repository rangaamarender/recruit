/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.dto;

import com.lucid.recruit.contract.constants.EnumContractWOStatus;
import com.lucid.recruit.contract.constants.EnumTimeCardApproval;
import com.lucid.recruit.contract.constants.EnumWorkOrderType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class ContractWorkOrderDTO{


	// UUID for each work order
	@Nullable
	private String workOrderID;

	@NotBlank
	private String workOrderName;

	@Nullable
	private String workOrderDesc;

	// W/O start date
	@NotNull
	private LocalDate startDate;

	// W/O end date
	// null represent active forever
	@Nullable
	private LocalDate endDate;

	// WBS code
	@NotBlank
	private String wbsCode;

	// Contract Status like Active,Terminated
	private EnumContractWOStatus status;

	// MSA termination date
	private LocalDate terminationDate;

	// Termination code
	private String terminationReasonCode;

	// termination reason text
	private String terminationReasonText;

	// FP:Fixed price,TMBR:T&M Blended Rate,TMIR: T&M Individual Rate
	private EnumWorkOrderType workOrderType;

	// True - for multiple resource,False - for single resource
	@NotNull
	private Boolean multiResource;

	// Blended Rate in contract currency
	// Will only be populated if contract is T&M and Blended
	private double blendedRateMny;

	// List of work order locations
	@NotNull
	@Size(min = 1)
	@Valid
	private List<ContractWorkLocationDTO> workLocation;

	// Work Order Client
	@Nullable
	private List<ContractClientDTO> client;

	@Nullable
	private List<WorkOrderSuppChargesDTO> suppCharges;

	@NotNull
	private EnumTimeCardApproval tsApprovalFlow;

	@NotNull
	@Size(min = 1)
	@Valid
	private List<WorkOrderResourceDTO> workOrderResources;

	public ContractWorkOrderDTO() {
		super();
	}

	public String getWorkOrderID() {
		return workOrderID;
	}

	public void setWorkOrderID(String workOrderID) {
		this.workOrderID = workOrderID;
	}

	public String getWorkOrderName() {
		return workOrderName;
	}

	public void setWorkOrderName(String workOrderName) {
		this.workOrderName = workOrderName;
	}

	@Nullable
	public String getWorkOrderDesc() {
		return workOrderDesc;
	}

	public void setWorkOrderDesc(@Nullable String workOrderDesc) {
		this.workOrderDesc = workOrderDesc;
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

	public String getWbsCode() {
		return wbsCode;
	}

	public void setWbsCode(String wbsCode) {
		this.wbsCode = wbsCode;
	}

	public EnumContractWOStatus getStatus() {
		return status;
	}

	public void setStatus(EnumContractWOStatus status) {
		this.status = status;
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

	public String getTerminationReasonText() {
		return terminationReasonText;
	}

	public void setTerminationReasonText(String terminationReasonText) {
		this.terminationReasonText = terminationReasonText;
	}

	public EnumWorkOrderType getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(EnumWorkOrderType workOrderType) {
		this.workOrderType = workOrderType;
	}

	public Boolean getMultiResource() {
		return multiResource;
	}

	public void setMultiResource(Boolean multiResource) {
		this.multiResource = multiResource;
	}

	public double getBlendedRateMny() {
		return blendedRateMny;
	}

	public void setBlendedRateMny(double blendedRateMny) {
		this.blendedRateMny = blendedRateMny;
	}

	public List<ContractWorkLocationDTO> getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(List<ContractWorkLocationDTO> workLocation) {
		this.workLocation = workLocation;
	}

	public List<ContractClientDTO> getClient() {
		return client;
	}

	public void setClient(List<ContractClientDTO> client) {
		this.client = client;
	}

	public List<WorkOrderSuppChargesDTO> getSuppCharges() {
		return suppCharges;
	}

	public void setSuppCharges(List<WorkOrderSuppChargesDTO> suppCharges) {
		this.suppCharges = suppCharges;
	}

	public EnumTimeCardApproval getTsApprovalFlow() {
		return tsApprovalFlow;
	}

	public void setTsApprovalFlow(EnumTimeCardApproval tsApprovalFlow) {
		this.tsApprovalFlow = tsApprovalFlow;
	}

	@Nullable
	public List<WorkOrderResourceDTO> getWorkOrderResources() {
		return workOrderResources;
	}

	public void setWorkOrderResources(@Nullable List<WorkOrderResourceDTO> workOrderResources) {
		this.workOrderResources = workOrderResources;
	}
}
