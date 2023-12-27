package com.lucid.recruit.referencedata.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RelatedDepartmentDTO {
	@NotNull
	private String deptID;
	@Nullable
	private String deptName;
	@Nullable
	private String deptDesc;

	public RelatedDepartmentDTO(@NotNull @NotEmpty String deptID, String deptName, String deptDesc, List<JobDTO> job) {
		super();
		this.deptID = deptID;
		this.deptName = deptName;
		this.deptDesc = deptDesc;
	}

	public RelatedDepartmentDTO(@NotNull @NotEmpty String deptID) {
		super();
		this.deptID = deptID;
	}

	public RelatedDepartmentDTO() {
		super();
	}

	public String getDeptID() {
		return deptID;
	}

	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

}
