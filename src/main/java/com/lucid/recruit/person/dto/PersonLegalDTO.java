package com.lucid.recruit.person.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author chandu
 *
 */
public class PersonLegalDTO {
	@Nullable
	private String personLegalID;
	@NotNull
	@Size(min = 2, message = "first name should have at least 2 characters")
	private String givenName;
	@Nullable
	private String middleName;
	@NotNull
	@Size(min = 2, message = "family name should have at least 2 characters")
	private String familyName;
	@Nullable
	private String preferredName;
	@Nullable
	private LocalDate birthDate;
	@Nullable
	private String gender;
	@Nullable
	private String maritalStatus;
	@NotEmpty(message = "PersonContactDetails mandatory")
	@Valid
	private List<PersonContactDetailsDTO> primaryContactDetails;

	@Nullable
	private List<PersonContactDetailsDTO> secondaryContactDetails;

	@Nullable
	private List<PersonAddressDTO> personAddress;

	@Nullable
	private List<PersonBankDetailsDTO> personBankDetails;
	@Nullable
	private List<PersonEmergencyContactDTO> personEmergencyContact;

	@Nullable
	private List<PersonDependentsDTO> personDependents;

	public PersonLegalDTO(String givenName, String middleName, String familyName,
			List<PersonContactDetailsDTO> primaryContactDetails) {
		super();
		this.givenName = givenName;
		this.middleName = middleName;
		this.familyName = familyName;
		this.primaryContactDetails = primaryContactDetails;
	}

	public PersonLegalDTO(String personLegalID,
			@NotNull @Size(min = 2, message = "first name should have at least 2 characters") String givenName,
			String middleName,
			@NotNull @Size(min = 2, message = "first name should have at least 2 characters") String familyName,
			String preferredName, LocalDate birthDate, String gender, String maritalStatus,
			@NotNull @NotEmpty List<PersonContactDetailsDTO> primaryContactDetails,
			List<PersonBankDetailsDTO> personBankDetails, List<PersonEmergencyContactDTO> personEmergencyContact,
			List<PersonDependentsDTO> personDependents) {
		super();
		this.personLegalID = personLegalID;
		this.givenName = givenName;
		this.middleName = middleName;
		this.familyName = familyName;
		this.preferredName = preferredName;
		this.birthDate = birthDate;
		this.gender = gender;
		this.maritalStatus = maritalStatus;
		this.primaryContactDetails = primaryContactDetails;
		this.personBankDetails = personBankDetails;
		this.personEmergencyContact = personEmergencyContact;
		this.personDependents = personDependents;
	}

	public PersonLegalDTO() {
		super();
	}

	public String getPersonLegalID() {
		return personLegalID;
	}

	public void setPersonLegalID(String personLegalID) {
		this.personLegalID = personLegalID;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public List<PersonContactDetailsDTO> getPrimaryContactDetails() {
		return primaryContactDetails;
	}

	public void setPrimaryContactDetails(List<PersonContactDetailsDTO> primaryContactDetails) {
		this.primaryContactDetails = primaryContactDetails;
	}

	@Nullable
	public List<PersonContactDetailsDTO> getSecondaryContactDetails() {
		return secondaryContactDetails;
	}

	public void setSecondaryContactDetails(@Nullable List<PersonContactDetailsDTO> secondaryContactDetails) {
		this.secondaryContactDetails = secondaryContactDetails;
	}

	public List<PersonBankDetailsDTO> getPersonBankDetails() {
		return personBankDetails;
	}

	public void setPersonBankDetails(List<PersonBankDetailsDTO> personBankDetails) {
		this.personBankDetails = personBankDetails;
	}

	public List<PersonEmergencyContactDTO> getPersonEmergencyContact() {
		return personEmergencyContact;
	}

	public void setPersonEmergencyContact(List<PersonEmergencyContactDTO> personEmergencyContact) {
		this.personEmergencyContact = personEmergencyContact;
	}

	public List<PersonDependentsDTO> getPersonDependents() {
		return personDependents;
	}

	public void setPersonDependents(List<PersonDependentsDTO> personDependents) {
		this.personDependents = personDependents;
	}

	public List<PersonAddressDTO> getPersonAddress() {
		return personAddress;
	}

	public void setPersonAddress(List<PersonAddressDTO> personAddress) {
		this.personAddress = personAddress;
	}

}
