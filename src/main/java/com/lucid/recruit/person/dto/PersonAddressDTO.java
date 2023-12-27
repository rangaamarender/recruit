package com.lucid.recruit.person.dto;

import java.time.LocalDate;

import com.lucid.core.dto.BaseAddressDTO;
import com.lucid.recruit.person.entity.PersonAddressType;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class PersonAddressDTO extends BaseAddressDTO {
	@Nullable
	private String personAddressID;

	@NotNull
	private PersonAddressType addressType;
	@Nullable
	private LocalDate startDate;
	@Nullable
	private LocalDate endDate;

	public String getPersonAddressID() {
		return personAddressID;
	}

	public void setPersonAddressID(String personAddressID) {
		this.personAddressID = personAddressID;
	}

	public PersonAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(PersonAddressType addressType) {
		this.addressType = addressType;
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

}
