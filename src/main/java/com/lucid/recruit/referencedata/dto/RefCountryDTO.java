package com.lucid.recruit.referencedata.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RefCountryDTO {

	@NotNull
	private String countryCode;

	@Nullable
	private String alpha3Code;

	@Nullable
	private String numericCode;

	@Nullable
	private String countryName;

	@Nullable
	private String taxIdDisplayName;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAlpha3Code() {
		return alpha3Code;
	}

	public void setAlpha3Code(String alpha3Code) {
		this.alpha3Code = alpha3Code;
	}

	public String getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(String numericCode) {
		this.numericCode = numericCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getTaxIdDisplayName() {
		return taxIdDisplayName;
	}

	public void setTaxIdDisplayName(String taxIdDisplayName) {
		this.taxIdDisplayName = taxIdDisplayName;
	}
}
