package com.lucid.recruit.referencedata.entity;

import com.lucid.core.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = WOSupervisorRoles.TABLE_NAME)
public class WOSupervisorRoles extends AuditableEntity {

	private static final long serialVersionUID = -1490279986889176399L;
	public static final String TABLE_NAME = "ref_wo_supervisor_roles";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "role_supr_id", nullable = false, length = 75)
	private String supervisorRoleID;

	@Column(name = "role_supr_name", nullable = false, length = 100)
	private String supervisorRoleName;

	@Column(name = "role_supr_desc", nullable = false, length = 2000)
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
