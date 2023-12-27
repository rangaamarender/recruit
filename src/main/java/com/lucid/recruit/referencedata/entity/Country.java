package com.lucid.recruit.referencedata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = Country.TABLE_NAME)
public class Country {

	public static final String TABLE_NAME = "ref_country";

	@Id
	@Column(name = "country_code", nullable = false, length = 2, updatable = false)
	@NotBlank
	private String countryCode;

	@Column(name = "alpha3_code", nullable = false, length = 3, updatable = false, unique = true)
	private String alpha3Code;

	@Column(name = "numeric_code", nullable = false, length = 3, updatable = false, unique = true)
	private String numericCode;

	@Column(name = "country_name", nullable = false)
	private String countryName;

	@Column(name = "taxid_dspname", nullable = false)
	private String taxIdDisplayName;

	public Country() {
		super();
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

	public String getTaxIdDisplayName() {
		return taxIdDisplayName;
	}

	public void setTaxIdDisplayName(String taxIdDisplayName) {
		this.taxIdDisplayName = taxIdDisplayName;
	}

}
