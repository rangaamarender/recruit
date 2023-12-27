package com.lucid.recruit.person.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;

import com.lucid.core.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = PersonLegal.TABLE_NAME)
public class PersonLegal extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 9136123385223325814L;
	public static final String TABLE_NAME = "p_person_legal";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "person_legal_id", nullable = false, length = 75)
	private String personLegalID;

	@Column(name = "given_name", nullable = false, length = 100)
	private String givenName;

	@Column(name = "middle_name", nullable = true, length = 100)
	private String middleName;

	@Column(name = "family_name", nullable = false, length = 50)
	private String familyName;

	@Column(name = "preferred_name", nullable = true, length = 200)
	private String preferredName;

	// The birth date of a person.
	@Column(name = "birth_dt", nullable = true)
	private LocalDate birthDate;

	// The gender of a person.
	@Column(name = "gender", nullable = true, length = 15)
	private String gender;

	// The martial status of the person.
	@Column(name = "martial_status", nullable = true, length = 25)
	private String martialStatus;

	// List of Person Contact
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personLegal")
	@OrderBy("contactType, startDate desc")
	private List<PersonContactDetails> personContactDetails;

	// List of Person Address
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personLegal")
	@OrderBy("addressType, startDate desc")
	private List<PersonAddress> personAddress;

	// List of Person Bank
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personLegal")
	@OrderBy("startDate desc")
	private List<PersonBankDetails> personBankDetails;

	// List of Person Emergency Contact
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personLegal")
	@OrderBy("startDate desc")
	private List<PersonEmergencyContact> personEmergencyContact;

	// List of Person Dependents
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personLegal")
	private List<PersonDependents> personDependents;

	/**
	 * Create a new <code>PersonLegal</code>
	 */
	public PersonLegal() {
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

	public String getMartialStatus() {
		return martialStatus;
	}

	public void setMartialStatus(String martialStatus) {
		this.martialStatus = martialStatus;
	}

	public List<PersonContactDetails> getPersonContactDetails() {
		return personContactDetails;
	}

	public void setPersonContactDetails(List<PersonContactDetails> personContactDetails) {
		this.personContactDetails = personContactDetails;
	}

	public List<PersonBankDetails> getPersonBankDetails() {
		return personBankDetails;
	}

	public void setPersonBankDetails(List<PersonBankDetails> personBankDetails) {
		this.personBankDetails = personBankDetails;
	}

	public List<PersonEmergencyContact> getPersonEmergencyContact() {
		return personEmergencyContact;
	}

	public void setPersonEmergencyContact(List<PersonEmergencyContact> personEmergencyContact) {
		this.personEmergencyContact = personEmergencyContact;
	}

	public List<PersonDependents> getPersonDependents() {
		return personDependents;
	}

	public void setPersonDependents(List<PersonDependents> personDependents) {
		this.personDependents = personDependents;
	}

	public List<PersonAddress> getPersonAddress() {
		return personAddress;
	}

	public void setPersonAddress(List<PersonAddress> personAddress) {
		this.personAddress = personAddress;
	}



}
