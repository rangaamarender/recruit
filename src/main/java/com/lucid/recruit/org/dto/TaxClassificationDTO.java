package com.lucid.recruit.org.dto;

import com.lucid.recruit.referencedata.dto.TaxClassificationDefDTO;

import jakarta.annotation.Nullable;

public class TaxClassificationDTO {
	@Nullable
	private TaxClassificationDefDTO taxClassification;
	@Nullable
	private String stateOfInc;

	public TaxClassificationDefDTO getTaxClassification() {
		return taxClassification;
	}

	public void setTaxClassification(TaxClassificationDefDTO taxClassification) {
		this.taxClassification = taxClassification;
	}

	public String getStateOfInc() {
		return stateOfInc;
	}

	public void setStateOfInc(String stateOfInc) {
		this.stateOfInc = stateOfInc;
	}

}
