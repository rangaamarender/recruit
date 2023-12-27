package com.lucid.recruit.contract.dto;

import com.lucid.core.embeddable.Amount;
import com.lucid.recruit.contract.constants.EnumWoSuppChargeType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public class WorkOrderSuppChargesDTO {


	// UUID for each work order rate
	@Nullable
	private String woSupChargeId;

	// Work Location name
	@NotBlank
	private String woSupChargeName;

	// start date of the work order rate
	@NotNull
	private LocalDate startDate;

	// end date of the work order rate
	// null represent active still work order end
	@Nullable
	private LocalDate endDate;

	@Embedded
	@NotNull
	private Amount amount;

	// Contract Status like Active,Terminated
	@NotNull
	private EnumWoSuppChargeType chargeType;

	public WorkOrderSuppChargesDTO() {
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
