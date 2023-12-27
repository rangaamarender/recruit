/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.worker.entity.Worker;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = ChargeCodeResource.TABLE_NAME)
public class ChargeCodeResource extends AuditableEntity {

	private static final long serialVersionUID = 1050096144385248029L;
	public static final String TABLE_NAME = "c_chargecode_resource";

	// UUID for each worker associated
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "contract_resrc_id", nullable = false, length = 75)
	private String contractResourceID;

	// UUID of the project
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "charge_code_id", nullable = false, updatable = false)
	private WorkOrderChargeCode woChrageCode;

	// UUID of worker
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "worker_id", nullable = false)
	private Worker worker;

	// start date of this record
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// end date of this record
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chargeCodeResource")
	private List<WorkOrderExpenseBudget> payProfile;

	public String getContractResourceID() {
		return contractResourceID;
	}

	public void setContractResourceID(String contractResourceID) {
		this.contractResourceID = contractResourceID;
	}

	public WorkOrderChargeCode getWoChrageCode() {
		return woChrageCode;
	}

	public void setWoChrageCode(WorkOrderChargeCode woChrageCode) {
		this.woChrageCode = woChrageCode;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
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

	public List<WorkOrderExpenseBudget> getPayProfile() {
		return payProfile;
	}

	public void setPayProfile(List<WorkOrderExpenseBudget> payProfile) {
		this.payProfile = payProfile;
	}
	
}
