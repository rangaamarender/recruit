package com.lucid.recruit.contract.entity;

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
import jakarta.persistence.UniqueConstraint;

import java.util.Comparator;

/**
 * 
 * Table holds the details of W/O rates for T&M contracts.
 */
@Entity
@Table(name = ChargeCodeRate.TABLE_NAME, uniqueConstraints = {
		@UniqueConstraint(name = "UQCRRATE_1", columnNames = { "charge_code_id", "step_number" }) })

public class ChargeCodeRate extends AuditableEntity {

	private static final long serialVersionUID = -8734089172619443722L;
	public static final String TABLE_NAME = "c_chargecode_rate";

	// UUID for each work order rate
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "wrkorder_rate_id", nullable = false, length = 75)
	private String woRateID;

	// Associated charge code
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "charge_code_id", nullable = false, updatable = false)
	private WorkOrderChargeCode woChrageCode;

	@Column(name = "step_number", nullable = false)
	private int stepNumber;

	@Column(name = "step_threshold", nullable = false)
	private double stepThreshold;

	// rate
	@Column(name = "rate", nullable = false, updatable = false)
	private double rate;

	@Enumerated(EnumType.STRING)
	@Column(name = "rate_frequency", nullable = false, updatable = false)
	private EnumRateUnits rateFrequency;

	// rate
	@Column(name = "rate_desc", nullable = false, length = 75)
	private String rateDesc;

	public ChargeCodeRate() {
		super();
	}

	public String getWoRateID() {
		return woRateID;
	}

	public void setWoRateID(String woRateID) {
		this.woRateID = woRateID;
	}

	public WorkOrderChargeCode getWoChrageCode() {
		return woChrageCode;
	}

	public void setWoChrageCode(WorkOrderChargeCode woChrageCode) {
		this.woChrageCode = woChrageCode;
	}

	public int getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	public double getStepThreshold() {
		return stepThreshold;
	}

	public void setStepThreshold(double stepThreshold) {
		this.stepThreshold = stepThreshold;
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

	public static Comparator<ChargeCodeRate> chargeCodeRateLambdaComparator(){
		return Comparator.comparing(ChargeCodeRate::getStepThreshold);
	}

}
