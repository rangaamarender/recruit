package com.lucid.recruit.contract.dto;

import com.lucid.recruit.contract.constants.EnumDiscountStepType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class ContractDiscountStepDTO{


	// UUID for each discount step
	@Nullable
	private String discSetpId;

	@NotNull
	private int stepNumber;

	@NotNull
	private double stepThreshold;

	// percentage discount, null if and only if discount type is monetary
	@Nullable
	private int discountPct;

	// total amount of the discounted amount, null if and only if discount type is
	// percentage
	@Nullable
	private double discountAmount;

	@NotNull
	private EnumDiscountStepType discountType;

	public ContractDiscountStepDTO() {
		super();
	}

	public String getDiscSetpId() {
		return discSetpId;
	}

	public void setDiscSetpId(String discSetpId) {
		this.discSetpId = discSetpId;
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

	public int getDiscountPct() {
		return discountPct;
	}

	public void setDiscountPct(int discountPct) {
		this.discountPct = discountPct;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public EnumDiscountStepType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(EnumDiscountStepType discountType) {
		this.discountType = discountType;
	}
}
