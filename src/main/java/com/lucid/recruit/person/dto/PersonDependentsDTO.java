package com.lucid.recruit.person.dto;

import java.time.LocalDate;

import com.lucid.recruit.person.entity.RelationshipCode;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PersonDependentsDTO {
	@Nullable
	private String personDepId;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotNull
	private RelationshipCode relation;
	@NotNull
	private LocalDate brithDate;
	@Nullable
	private LocalDate validFrom;
	@Nullable
	private LocalDate validTo;

	public PersonDependentsDTO(String personDepId, String firstName, String lastName, RelationshipCode relation, LocalDate brithDate,
			LocalDate validFrom, LocalDate validTo) {
		super();
		this.personDepId = personDepId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.relation = relation;
		this.brithDate = brithDate;
		this.validFrom = validFrom;
		this.validTo = validTo;
	}

	public PersonDependentsDTO() {
		super();

	}

	public String getPersonDepId() {
		return personDepId;
	}

	public void setPersonDepId(String personDepId) {
		this.personDepId = personDepId;
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

}
