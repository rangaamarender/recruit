
package com.lucid.recruit.person.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.core.entity.BasePhoneNbr;

import com.lucid.recruit.org.entity.OrgAddress;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = PersonContactDetails.TABLE_NAME)
public class PersonContactDetails extends AuditableEntity {

	private static final long serialVersionUID = 1511878007648695013L;
	public static final String TABLE_NAME = "p_prsn_contact_details";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "prsn_legal_contact_id", nullable = false, length = 75)
	private String personLegalContactID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_legal_id", nullable = false, updatable = false)
	private PersonLegal personLegal;

	@Enumerated(EnumType.STRING)
	@Column(name = "contact_type", nullable = false, length = 25)
	private ContactType contactType;

	@Embedded
	private BasePhoneNbr phoneNumber;

	@Column(name = "email_id", nullable = false, length = 75)
	private String emailId;

	@Column(name = "start_dat", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_dat", nullable = true)
	private LocalDate endDate;

	public String getPersonLegalContactID() {
		return personLegalContactID;
	}

	public void setPersonLegalContactID(String personLegalContactID) {
		this.personLegalContactID = personLegalContactID;
	}

	public PersonLegal getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegal personLegal) {
		this.personLegal = personLegal;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public BasePhoneNbr getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(BasePhoneNbr phoneNumber) {
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

	public static Comparator<PersonContactDetails> createPersonContactLambdaComparator(){
		return Comparator.comparing(PersonContactDetails::getStartDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}

		PersonContactDetails personContactDetails = (PersonContactDetails) obj;

		if (getEmailId() == null) {
			if (personContactDetails.getEmailId() != null) {
				return false;
			}
		} else if (!getEmailId().equals(personContactDetails.getEmailId())) {
			return false;
		}

		if(getPhoneNumber() == null){
			if (personContactDetails.getPhoneNumber() != null) {
				return false;
			}
		} else if(!getPhoneNumber().equals(personContactDetails.getPhoneNumber())){
			return false;
		}
		return true;
	}

}
