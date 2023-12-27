package com.lucid.recruit.person.dto;

import java.time.LocalDate;
import java.util.Date;

import com.lucid.core.dto.BasePhoneNbrDTO;
import com.lucid.core.entity.BasePhoneNbr;
import com.lucid.recruit.person.entity.RelationshipCode;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PersonEmergencyContactDTO {
	@Nullable
	private String personEmergContactId;
	@Nullable
	private LocalDate validFrom;
	@Nullable
	private LocalDate validTo;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotNull
	private RelationshipCode relation;

	@NotBlank
	private String emailType;
	@NotBlank
	private String emailId;
	@NotBlank
	private String phoneType;
	@NotNull
	@Embedded
	private BasePhoneNbrDTO phoneNumber;

	public PersonEmergencyContactDTO(){
		super();
	}
	public PersonEmergencyContactDTO(@Nullable String personEmergContactId, LocalDate validFrom, LocalDate validTo, String firstName, String lastName, RelationshipCode relation, String emailType, String emailId, String phoneType, BasePhoneNbrDTO phoneNumber) {
		this.personEmergContactId = personEmergContactId;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.relation = relation;
		this.emailType = emailType;
		this.emailId = emailId;
		this.phoneType = phoneType;
		this.phoneNumber = phoneNumber;
	}

	public String getPersonEmergContactId() {
		return personEmergContactId;
	}

	public void setPersonEmergContactId(String personEmergContactId) {
		this.personEmergContactId = personEmergContactId;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDate getValidTo() {
		return validTo;
	}

	public void setValidTo(LocalDate validTo) {
		this.validTo = validTo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public RelationshipCode getRelation() {
		return relation;
	}

	public void setRelation(RelationshipCode relation) {
		this.relation = relation;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public BasePhoneNbrDTO getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(BasePhoneNbrDTO phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
