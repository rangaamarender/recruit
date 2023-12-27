package com.lucid.recruit.person.entity;

import java.time.LocalDate;

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

@Entity
@Table(name = PersonEmergencyContact.TABLE_NAME)
public class PersonEmergencyContact extends AuditableEntity {

	private static final long serialVersionUID = 7609349402750903434L;
	public static final String TABLE_NAME = "p_prsn_emerg_contacts";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "prsn_emerg_contact_id", nullable = false, updatable = false, length = 75)
	private String personEmergContactId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_legal_id", nullable = false, updatable = false)
	private PersonLegal personLegal;

	@Column(name = "start_date", nullable = false, updatable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = true,updatable = false)
	private LocalDate endDate;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(name = "relation", nullable = false)
	private RelationshipCode relation;

	@Column(name = "email_type", nullable = false)
	private String emailType;

	@Column(name = "email_id", nullable = false)
	private String emailId;

	@Column(name = "phone_type", nullable = false)
	private String phoneType;

	@Embedded
	private BasePhoneNbr phoneNumber;

	public String getPersonEmergContactId() {
		return personEmergContactId;
	}

	public void setPersonEmergContactId(String personEmergContactId) {
		this.personEmergContactId = personEmergContactId;
	}

	public PersonLegal getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegal personLegal) {
		this.personLegal = personLegal;
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

	public BasePhoneNbr getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(BasePhoneNbr phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}

		PersonEmergencyContact personEmergencyContact = (PersonEmergencyContact) obj;

		if (getPhoneNumber() == null) {
			if (personEmergencyContact.getPhoneNumber() != null) {
				return false;
			}
		} else if (!getPhoneNumber().equals(personEmergencyContact.getPhoneNumber())) {
			return false;
		}
		if (getPhoneType() == null) {
			if (personEmergencyContact.getPhoneType() != null) {
				return false;
			}
		} else if (!getPhoneType().equals(personEmergencyContact.getPhoneType())) {
			return false;
		}
		if (getEmailId() == null) {
			if (personEmergencyContact.getEmailId() != null) {
				return false;
			}
		} else if (!getEmailId().equals(personEmergencyContact.getEmailId())) {
			return false;
		}
		if (getEmailType() == null) {
			if (personEmergencyContact.getEmailType() != null) {
				return false;
			}
		} else if (!getEmailType().equals(personEmergencyContact.getEmailType())) {
			return false;
		}
		if (getRelation() == null) {
			if (personEmergencyContact.getRelation() != null) {
				return false;
			}
		} else if (!getRelation().equals(personEmergencyContact.getRelation())) {
			return false;
		}
		if (getFirstName() == null) {
			if (personEmergencyContact.getFirstName() != null) {
				return false;
			}
		} else if (!getFirstName().equals(personEmergencyContact.getFirstName())) {
			return false;
		}
		if (getLastName() == null) {
			if (personEmergencyContact.getLastName() != null) {
				return false;
			}
		} else if (!getLastName().equals(personEmergencyContact.getLastName())) {
			return false;
		}
		return true;
	}

}
