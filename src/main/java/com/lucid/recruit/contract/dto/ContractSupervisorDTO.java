package com.lucid.recruit.contract.dto;

import com.lucid.core.annotations.Phone;
import com.lucid.recruit.referencedata.dto.WOSupervisorRolesDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ContractSupervisorDTO{

	@Nullable
	private String contractSupervisorID;

	// Associated role
	@NotNull
	private WOSupervisorRolesDTO role;

	@NotNull
	private LocalDate startDate;

	@Nullable
	private LocalDate endDate;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Phone
	private String phone;

	@Nullable
	public String getContractSupervisorID() {
		return contractSupervisorID;
	}

	public void setContractSupervisorID(@Nullable String contractSupervisorID) {
		this.contractSupervisorID = contractSupervisorID;
	}

	public WOSupervisorRolesDTO getRole() {
		return role;
	}

	public void setRole(WOSupervisorRolesDTO role) {
		this.role = role;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Nullable
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(@Nullable LocalDate endDate) {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
