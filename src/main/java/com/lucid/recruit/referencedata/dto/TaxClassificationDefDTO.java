package com.lucid.recruit.referencedata.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class TaxClassificationDefDTO {

	@NotNull
	private String taxClassCode;
	@NotNull
	private String taxClassName;
	@Nullable
	private String description;
	@NotNull
	private boolean stateInc;
	@Valid
	private CountryDTO country;

	public String getTaxClassCode() {
		return taxClassCode;
	}

	public void setTaxClassCode(String taxClassCode) {
		this.taxClassCode = taxClassCode;
	}

	public String getTaxClassName() {
		return taxClassName;
	}

	public void setTaxClassName(String taxClassName) {
		this.taxClassName = taxClassName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStateInc() {
		return stateInc;
	}

	public void setStateInc(boolean stateInc) {
		this.stateInc = stateInc;
	}

	public CountryDTO getCountry() {
		return country;
	}

	public void setCountry(CountryDTO country) {
		this.country = country;
	}
}
