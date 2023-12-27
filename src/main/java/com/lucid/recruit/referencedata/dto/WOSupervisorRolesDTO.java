package com.lucid.recruit.referencedata.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public class WOSupervisorRolesDTO {

	@Nullable
	private String supervisorRoleID;

	@NotBlank
	private String supervisorRoleName;

	@NotBlank
	private String supervisorRoleDesc;

	public String getSupervisorRoleID() {
		return supervisorRoleID;
	}

	public void setSupervisorRoleID(String supervisorRoleID) {
		this.supervisorRoleID = supervisorRoleID;
	}

	public String getSupervisorRoleName() {
		return supervisorRoleName;
	}

	public void setSupervisorRoleName(String supervisorRoleName) {
		this.supervisorRoleName = supervisorRoleName;
	}

	public String getSupervisorRoleDesc() {
		return supervisorRoleDesc;
	}

	public void setSupervisorRoleDesc(String supervisorRoleDesc) {
		this.supervisorRoleDesc = supervisorRoleDesc;
	}
}
