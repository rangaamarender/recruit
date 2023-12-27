package com.lucid.recruit.referencedata.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RefDepartmentDTO {
	@Nullable
	private String deptID;
	@NotBlank
	private String deptName;
	@Nullable
	private String deptDesc;

	@Nullable
	private Boolean billable;

	@Nullable
	private List<RefJobDTO> jobsList = new ArrayList<>();

	public RefDepartmentDTO(@NotNull @NotEmpty String deptID, String deptName, String deptDesc, List<RefJobDTO> job) {
		super();
		this.deptID = deptID;
		this.deptName = deptName;
		this.deptDesc = deptDesc;
	}

	public RefDepartmentDTO(@NotNull @NotEmpty String deptID) {
		super();
		this.deptID = deptID;
	}

	public RefDepartmentDTO() {
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

	@Nullable
	public List<RefJobDTO> getJobsList() {
		return jobsList;
	}

	public void setJobsList(@Nullable List<RefJobDTO> jobsList) {
		this.jobsList = jobsList;
	}

	@Nullable
	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(@Nullable Boolean billable) {
		this.billable = billable;
	}
}
