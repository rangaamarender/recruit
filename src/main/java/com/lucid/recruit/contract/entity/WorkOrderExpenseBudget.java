
package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.contract.constants.EnumRateUnits;

import jakarta.persistence.Column;
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
@Table(name = WorkOrderExpenseBudget.TABLE_NAME)
public class WorkOrderExpenseBudget extends AuditableEntity {

	// --------------------------------------------------------------- Constants
	private static final long serialVersionUID = -8734089172619443722L;
	public static final String TABLE_NAME = "c_wrkordr_exp_budget";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "expense_budget_id", nullable = false, length = 75)
	private String expenseBudgetId;

	// UUID of the contract resource
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cntrct_resrc_id", nullable = false, updatable = false)
	private ChargeCodeResource chargeCodeResource;

	// start date of this record
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// end date of this record
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	// rate
	@Column(name = "rate", nullable = false, updatable = false)
	private double rate;

	@Enumerated(EnumType.STRING)
	@Column(name = "rate_frequency", nullable = false, updatable = false)
	private EnumRateUnits rateFrequency;

	// rate
	@Column(name = "rate_desc", nullable = true, length = 75)
	private String rateDesc;

	public WorkOrderExpenseBudget() {
		super();
	}

	public String getExpenseBudgetId() {
		return expenseBudgetId;
	}

	public void setExpenseBudgetId(String expenseBudgetId) {
		this.expenseBudgetId = expenseBudgetId;
	}

	public ChargeCodeResource getContractResource() {
		return chargeCodeResource;
	}

	public void setContractResource(ChargeCodeResource chargeCodeResource) {
		this.chargeCodeResource = chargeCodeResource;
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

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public EnumRateUnits getRateFrequency() {
		return rateFrequency;
	}

	public void setRateFrequency(EnumRateUnits rateFrequency) {
		this.rateFrequency = rateFrequency;
	}

	public String getRateDesc() {
		return rateDesc;
	}

	public void setRateDesc(String rateDesc) {
		this.rateDesc = rateDesc;
	}

	public static Comparator<WorkOrderExpenseBudget> createExpenseBudgetComparatorLambda(){
		return Comparator.comparing(WorkOrderExpenseBudget::getStartDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}
		WorkOrderExpenseBudget payProfile = (WorkOrderExpenseBudget) obj;

		if(rate != payProfile.getRate()){
			return false;
		}
		if(rateFrequency == null){
			if(payProfile.getRateFrequency() != null){
				return false;
			}
		}
		else if(!rateFrequency.equals(payProfile.getRateFrequency())){
			return false;
		}
		if(rateDesc == null){
			if(payProfile.getRateDesc() != null){
				return false;
			}
		}
		else if(!rateDesc.equals(payProfile.getRateDesc())){
			return false;
		}
		return true;
	}
}
