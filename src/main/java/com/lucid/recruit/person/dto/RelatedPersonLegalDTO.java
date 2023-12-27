package com.lucid.recruit.person.dto;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;

public class RelatedPersonLegalDTO {
	@Nullable
	private String givenName;
	@Nullable
	private String middleName;
	@Nullable
	private String familyName;

	public RelatedPersonLegalDTO() {
		super();
	}

	public RelatedPersonLegalDTO(String givenName, String middleName, String familyName) {
		super();
		this.givenName = givenName;
		this.middleName = middleName;
		this.familyName = familyName;
	}

	@Nullable
	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(@Nullable String givenName) {
		this.givenName = givenName;
	}

	@Nullable
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(@Nullable String middleName) {
		this.middleName = middleName;
	}

	@Nullable
	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(@Nullable String familyName) {
		this.familyName = familyName;
	}
}
