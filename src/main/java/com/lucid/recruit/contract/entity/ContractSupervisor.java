package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.referencedata.entity.WOSupervisorRoles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = ContractSupervisor.TABLE_NAME, uniqueConstraints = {
		@UniqueConstraint(name = "UQCNTRCTSUP_1", columnNames = { "role_supr_id", "start_dat" }) })
public class ContractSupervisor extends AuditableEntity {

	private static final long serialVersionUID = -286472131363904229L;
	public static final String TABLE_NAME = "c_cntrct_supervisor";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "cntrct_supr_id", nullable = false, length = 75)
	private String contractSupervisorID;

	// Associated role
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_supr_id", nullable = false, updatable = false)
	private WOSupervisorRoles role;

	@Column(name = "start_dat", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_dat", nullable = true)
	private LocalDate endDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wo_resource_id", nullable = false, updatable = false)
	private WorkOrderResource workOrderResource;

	@Column(name = "supr_fn", nullable = false, length = 100)
	private String firstName;

	@Column(name = "supr_ln", nullable = false, length = 100)
	private String lastName;

	@Email
	@Column(name = "supr_email", nullable = false, length = 100)
	private String email;

	@Column(name = "supr_phone", nullable = false, length = 20)
	private String phone;

	public String getContractSupervisorID() {
		return contractSupervisorID;
	}

	public void setContractSupervisorID(String contractSupervisorID) {
		this.contractSupervisorID = contractSupervisorID;
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

	public WorkOrderResource getWorkOrderResource() {
		return workOrderResource;
	}

	public void setWorkOrderResource(WorkOrderResource workOrderResource) {
		this.workOrderResource = workOrderResource;
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

	public WOSupervisorRoles getRole() {
		return role;
	}

	public void setRole(WOSupervisorRoles role) {
		this.role = role;
	}

	public static Comparator<ContractSupervisor> contractSupervisorLambdaComparator(){
		return Comparator.comparing(ContractSupervisor::getStartDate).thenComparing(cs -> cs.getRole().getSupervisorRoleID());
	}

}
