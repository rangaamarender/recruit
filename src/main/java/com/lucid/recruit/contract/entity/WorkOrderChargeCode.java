package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.List;

import com.lucid.core.entity.AuditableEntity;

import jakarta.persistence.*;

@Entity
@Table(name = WorkOrderChargeCode.TABLE_NAME)
public class WorkOrderChargeCode extends AuditableEntity {

	private static final long serialVersionUID = -4639012953737917332L;

	protected static final String TABLE_NAME = "c_wrkordr_chrg_code";

	// UUID of the task
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "charge_code_id", nullable = false, length = 75)
	private String chargeCodeId;

	// charge code name
	@Column(name = "charge_code_name", nullable = false, length = 75)
	private String chargeCodeName;

	// start date of the charge code
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// end date of the charge code
	// null represent active still work order end
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	// Work location
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_work_loc_id", nullable = false, updatable = false)
	private ContractWorkLocation contractWorkLocation;

	// List of tasks
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "woChrageCode")
	@OrderBy("taskName asc")
	private List<ChargeCodeTasks> chargeCodeTasks;

	// List of work order rates
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "woChrageCode")
	private List<ChargeCodeRate> workOrderRates;

	// List of allowed expenses
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "woChrageCode")
	@OrderBy("startDate asc")
	private List<WorkOrderExpenseCodes> workOrderExpenses;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "woChrageCode")
	private List<ChargeCodeResource> chargeCodeResources;

	public WorkOrderChargeCode() {
		super();
	}

	public String getChargeCodeId() {
		return chargeCodeId;
	}

	public void setChargeCodeId(String chargeCodeId) {
		this.chargeCodeId = chargeCodeId;
	}

	public String getChargeCodeName() {
		return chargeCodeName;
	}

	public void setChargeCodeName(String chargeCodeName) {
		this.chargeCodeName = chargeCodeName;
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

	public ContractWorkLocation getContractWorkLocation() {
		return contractWorkLocation;
	}

	public void setContractWorkLocation(ContractWorkLocation contractWorkLocation) {
		this.contractWorkLocation = contractWorkLocation;
	}

	public List<ChargeCodeRate> getWorkOrderRates() {
		return workOrderRates;
	}

	public void setWorkOrderRates(List<ChargeCodeRate> workOrderRates) {
		this.workOrderRates = workOrderRates;
	}

	public List<ChargeCodeResource> getContractResources() {
		return chargeCodeResources;
	}

	public void setContractResources(List<ChargeCodeResource> chargeCodeResources) {
		this.chargeCodeResources = chargeCodeResources;
	}

	public List<ChargeCodeTasks> getChargeCodeTasks() {
		return chargeCodeTasks;
	}

	public void setChargeCodeTasks(List<ChargeCodeTasks> chargeCodeTasks) {
		this.chargeCodeTasks = chargeCodeTasks;
	}

	public List<WorkOrderExpenseCodes> getWorkOrderExpenses() {
		return workOrderExpenses;
	}

	public void setWorkOrderExpenses(List<WorkOrderExpenseCodes> workOrderExpenses) {
		this.workOrderExpenses = workOrderExpenses;
	}


}
