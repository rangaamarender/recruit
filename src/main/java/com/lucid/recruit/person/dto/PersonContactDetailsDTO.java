package com.lucid.recruit.person.dto;

import java.time.LocalDate;

import com.lucid.core.dto.BasePhoneNbrDTO;
import com.lucid.core.entity.BasePhoneNbr;
import com.lucid.recruit.person.entity.ContactType;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class PersonContactDetailsDTO {
	@Nullable
	private String personLegalContactID;
	@Nullable
	private ContactType contactType;

	@Valid
	private BasePhoneNbrDTO phoneNumber;
	@NotNull
	@Email
	private String emailId;

	@Nullable
	private LocalDate startDate;
	@Nullable
	private LocalDate endDate;

	public PersonContactDetailsDTO(String personLegalContactID, ContactType contactType, BasePhoneNbrDTO phoneNumber,
			String emailId, LocalDate startDate, LocalDate endDate) {
		super();
		this.personLegalContactID = personLegalContactID;
		this.contactType = contactType;
		this.phoneNumber = phoneNumber;
		this.emailId = emailId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public PersonContactDetailsDTO() {
		super();
	}

	public String getPersonLegalContactID() {
		return personLegalContactID;
	}

	public void setPersonLegalContactID(String personLegalContactID) {
		this.personLegalContactID = personLegalContactID;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public BasePhoneNbrDTO getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(BasePhoneNbrDTO phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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
