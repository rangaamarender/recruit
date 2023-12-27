package com.lucid.recruit.contract.dto;

import com.lucid.recruit.contract.constants.EnumRateUnits;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * Table holds the details of W/O rates for T&M contracts.
 */

public class ChargeCodeRateDTO {


	// UUID for each work order rate
	@Nullable
	private String woRateID;


	@NotNull
	private int stepNumber;

	@NotNull
	private double stepThreshold;

	// rate
	@NotNull
	private double rate;

	@NotNull
	private EnumRateUnits rateFrequency;

	// rate
	@NotBlank
	private String rateDesc;

	public ChargeCodeRateDTO() {
		super();
	}

	public String getWoRateID() {
		return woRateID;
	}

	public void setWoRateID(String woRateID) {
		this.woRateID = woRateID;
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

}
