/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.List;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.contract.constants.EnumContractWOStatus;
import com.lucid.recruit.contract.constants.EnumTimeCardApproval;
import com.lucid.recruit.contract.constants.EnumWorkOrderType;

import jakarta.persistence.*;

@Entity
@Table(name = ContractWorkOrder.TABLE_NAME)
public class ContractWorkOrder extends AuditableEntity {

	// --------------------------------------------------------------- Constants
	private static final long serialVersionUID = -8734089172619443722L;
	public static final String TABLE_NAME = "c_contract_workorder";

	// UUID for each work order
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "c_wrk_order_id", nullable = false, length = 75)
	private String workOrderID;

	@Column(name = "c_wrk_order_name", nullable = false)
	private String workOrderName;

	@Column(name = "c_wrk_order_desc", nullable = true, length = 2000)
	private String workOrderDesc;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cntrct_account_id", nullable = false, updatable = false)
	private ContractAccount contractAccount;

	// W/O start date
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// W/O end date
	// null represent active forever
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	// WBS code
	@Column(name = "wbs_code", nullable = false, length = 50)
	private String wbsCode;

	// Contract Status like Active,Terminated
	@Enumerated(value = EnumType.STRING)
	@Column(name = "status", nullable = false)
	private EnumContractWOStatus status;

	// work order termination date
	@Column(name = "termination_date")
	private LocalDate terminationDate;

	// Termination code
	@Column(name = "termination_reason_code", length = 50)
	private String terminationReasonCode;

	// termination reason text
	@Column(name = "termination_reason_txt", length = 500)
	private String terminationReasonText;

	// FP:Fixed price,TMBR:T&M Blended Rate,TMIR: T&M Individual Rate
	@Enumerated(value = EnumType.STRING)
	@Column(name = "workorder_type", nullable = false, length = 75)
	private EnumWorkOrderType workOrderType;

	// True - for multiple resource,False - for single resource
	@Column(name = "multi_resource_boo", nullable = false)
	private Boolean multiResource;

	// Blended Rate in contract currency
	// Will only be populated if contract is T&M and Blended
	@Column(name = "blended_rate_mny", nullable = true)
	private Double blendedRateMny;

	// List of work order locations
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workOrder")
	@OrderBy("workLocationName")
	private List<ContractWorkLocation> workLocation;

	// List of work order supplementary charges
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workOrder")
	private List<WorkOrderSuppCharges> suppCharges;

	// Work Order Client
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workOrder")
	@OrderBy("clientSequence")
	private List<ContractClient> client;

	@Enumerated(EnumType.STRING)
	@Column(name = "ts_approval_flow", nullable = false)
	private EnumTimeCardApproval tsApprovalFlow;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workOrder")
	private List<WorkOrderResource> workOrderResources;

	public ContractWorkOrder() {
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

	public String getWorkOrderDesc() {
		return workOrderDesc;
	}

	public void setWorkOrderDesc(String workOrderDesc) {
		this.workOrderDesc = workOrderDesc;
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

	public Double getBlendedRateMny() {
		return blendedRateMny;
	}

	public void setBlendedRateMny(Double blendedRateMny) {
		this.blendedRateMny = blendedRateMny;
	}

	public List<ContractWorkLocation> getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(List<ContractWorkLocation> workLocation) {
		this.workLocation = workLocation;
	}

	public List<ContractClient> getClient() {
		return client;
	}

	public void setClient(List<ContractClient> client) {
		this.client = client;
	}

	public List<WorkOrderSuppCharges> getSuppCharges() {
		return suppCharges;
	}

	public void setSuppCharges(List<WorkOrderSuppCharges> suppCharges) {
		this.suppCharges = suppCharges;
	}

	public EnumTimeCardApproval getTsApprovalFlow() {
		return tsApprovalFlow;
	}

	public void setTsApprovalFlow(EnumTimeCardApproval tsApprovalFlow) {
		this.tsApprovalFlow = tsApprovalFlow;
	}

	public List<WorkOrderResource> getWorkOrderResources() {
		return workOrderResources;
	}

	public void setWorkOrderResources(List<WorkOrderResource> workOrderResources) {
		this.workOrderResources = workOrderResources;
	}

}
