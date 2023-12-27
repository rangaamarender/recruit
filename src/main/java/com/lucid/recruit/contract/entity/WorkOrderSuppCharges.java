package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.embeddable.Amount;
import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.contract.constants.EnumWoSuppChargeType;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = WorkOrderSuppCharges.TABLE_NAME)

public class WorkOrderSuppCharges extends AuditableEntity {

	private static final long serialVersionUID = -5234896208092857041L;

	public static final String TABLE_NAME = "c_wo_sup_charges";

	// UUID for each work order rate
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "wo_sup_chrg_id", nullable = false, length = 75)
	private String woSupChargeId;

	// Work Location name
	@Column(name = "wo_sup_chrg_name", nullable = false, length = 75)
	private String woSupChargeName;

	// UUID of the work order
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_wrk_order_id", nullable = false, updatable = false)
	private ContractWorkOrder workOrder;

	// start date of the work order rate
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// end date of the work order rate
	// null represent active still work order end
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	@Embedded
	private Amount amount;

	// Contract Status like Active,Terminated
	@Enumerated(value = EnumType.STRING)
	@Column(name = "charge_type", nullable = false)
	private EnumWoSuppChargeType chargeType;

	public WorkOrderSuppCharges() {
		super();
	}

	public String getWoSupChargeId() {
		return woSupChargeId;
	}

	public void setWoSupChargeId(String woSupChargeId) {
		this.woSupChargeId = woSupChargeId;
	}

	public String getWoSupChargeName() {
		return woSupChargeName;
	}

	public void setWoSupChargeName(String woSupChargeName) {
		this.woSupChargeName = woSupChargeName;
	}

	public ContractWorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(ContractWorkOrder workOrder) {
		this.workOrder = workOrder;
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

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public EnumWoSuppChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(EnumWoSuppChargeType chargeType) {
		this.chargeType = chargeType;
	}

}
