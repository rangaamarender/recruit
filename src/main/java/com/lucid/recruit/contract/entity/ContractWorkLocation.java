package com.lucid.recruit.contract.entity;

import java.util.List;

import com.lucid.core.entity.BaseAddress;

import jakarta.persistence.*;

/*
 * Contract work location
 */
@Entity
@Table(name = ContractWorkLocation.TABLE_NAME)
public class ContractWorkLocation extends BaseAddress {

	public static final String TABLE_NAME = "c_cntrct_wrk_loc";
	private static final long serialVersionUID = 8423905393070824737L;

	// UUID of the contract work location
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "c_work_loc_id", nullable = false, length = 75)
	private String contractwlID;

	// Work Location name
	@Column(name = "c_work_loc_name", nullable = false, length = 75)
	private String workLocationName;

	// UUID of the work order
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_wrk_order_id", nullable = false, updatable = false)
	private ContractWorkOrder workOrder;

	// List of charge codes associated
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractWorkLocation")
	@OrderBy("chargeCodeName")
	private List<WorkOrderChargeCode> chargeCodes;

	public ContractWorkLocation() {
		super();
	}

	public String getContractwlID() {
		return contractwlID;
	}

	public void setContractwlID(String contractwlID) {
		this.contractwlID = contractwlID;
	}

	public String getWorkLocationName() {
		return workLocationName;
	}

	public void setWorkLocationName(String workLocationName) {
		this.workLocationName = workLocationName;
	}

	public ContractWorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(ContractWorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public List<WorkOrderChargeCode> getChargeCodes() {
		return chargeCodes;
	}

	public void setChargeCodes(List<WorkOrderChargeCode> chargeCodes) {
		this.chargeCodes = chargeCodes;
	}
}
