package com.lucid.recruit.person.entity;

import java.time.LocalDate;
import java.util.Date;

import com.lucid.core.entity.AuditableEntity;

import com.lucid.recruit.org.entity.OrgAddress;
import jakarta.persistence.Column;
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
@Table(name = PersonDependents.TABLE_NAME)
public class PersonDependents extends AuditableEntity {

	private static final long serialVersionUID = -6959149206833850931L;
	public static final String TABLE_NAME = "p_prsn_dep_list";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "prsn_dep_id", nullable = false, updatable = false, length = 75)
	private String personDepId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_legal_id", nullable = false, updatable = false)
	private PersonLegal personLegal;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(name = "relation", nullable = false)
	private RelationshipCode relation;

	@Column(name = "birth_date", nullable = false)
	private LocalDate brithDate;

	@Column(name = "valid_from", nullable = false, updatable = false)
	private LocalDate validFrom;

	@Column(name = "valid_to", nullable = true, updatable = false)
	private LocalDate validTo;

	public String getPersonDepId() {
		return personDepId;
	}

	public void setPersonDepId(String personDepId) {
		this.personDepId = personDepId;
	}

	public PersonLegal getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegal personLegal) {
		this.personLegal = personLegal;
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

	public LocalDate getBrithDate() {
		return brithDate;
	}

	public void setBrithDate(LocalDate brithDate) {
		this.brithDate = brithDate;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}

		PersonDependents personDependents = (PersonDependents) obj;

		if (getBrithDate() == null) {
			if (personDependents.getBrithDate() != null) {
				return false;
			}
		} else if (!getBrithDate().equals(personDependents.getBrithDate())) {
			return false;
		}
		if (getFirstName() == null) {
			if (personDependents.getFirstName() != null) {
				return false;
			}
		} else if (!getFirstName().equals(personDependents.getFirstName())) {
			return false;
		}
		if (getLastName() == null) {
			if (personDependents.getLastName() != null) {
				return false;
			}
		} else if (!getLastName().equals(personDependents.getLastName())) {
			return false;
		}
		if (getRelation() == null) {
			if (personDependents.getRelation() != null) {
				return false;
			}
		} else if (!getRelation().equals(personDependents.getRelation())) {
			return false;
		}
        return true;
	}
}
