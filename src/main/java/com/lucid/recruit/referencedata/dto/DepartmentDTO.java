package com.lucid.recruit.referencedata.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class DepartmentDTO {
	@Nullable
	private String deptID;
	@NotBlank
	private String deptName;
	@Nullable
	private String deptDesc;

	public DepartmentDTO(@NotNull @NotEmpty String deptID, String deptName, String deptDesc,List<JobDTO> job) {
		super();
		this.deptID = deptID;
		this.deptName = deptName;
		this.deptDesc = deptDesc;
	}

	public DepartmentDTO(@NotNull @NotEmpty String deptID) {
		super();
		this.deptID = deptID;
	}

	public DepartmentDTO() {
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
