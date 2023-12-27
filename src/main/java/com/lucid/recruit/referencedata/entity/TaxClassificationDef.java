package com.lucid.recruit.referencedata.entity;

import jakarta.persistence.*;

@Entity
@Table(name = TaxClassificationDef.TABLE_NAME,uniqueConstraints = @UniqueConstraint(name = "UNQTAXCLASSDEF",columnNames = {"taxclass_code","country_code"}))
public class TaxClassificationDef {
	public static final String TABLE_NAME = "ref_taxclassfication";


	@Id
	@Column(name = "taxclass_code", nullable = false, length = 24, updatable = false)
	private String taxClassCode;

	// Country Code
	@ManyToOne
	@JoinColumn(name = "country_code", nullable = false)
	private Country country;

	@Column(name = "taxclass_name", nullable = false, length = 75, updatable = false)
	private String taxClassName;

	@Column(name = "description", nullable = true, length = 240, updatable = false)
	private String description;

	@Column(name = "stateinc_boo", nullable = false, updatable = false)
	private Boolean stateInc;

	public String getTaxClassCode() {
		return taxClassCode;
	}

	public void setTaxClassCode(String taxClassCode) {
		this.taxClassCode = taxClassCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
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

	public Boolean getStateInc() {
		return stateInc;
	}

	public void setStateInc(Boolean stateInc) {
		this.stateInc = stateInc;
	}
}
