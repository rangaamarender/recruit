package com.lucid.recruit.referencedata.entity;

import jakarta.persistence.*;

/**
 * JOB Reference data
 * 
 * @author chandu
 *
 */
@Entity
@Table(name = Job.TABLE_NAME,uniqueConstraints = {@UniqueConstraint(name = "UNIQUEJOB",columnNames = {"job_name","dept_id"})})
public class Job {
	public static final String TABLE_NAME = "ref_job";

	// JOB Unique Integration ID UUID value
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "job_id", nullable = false)
	private String jobID;

	@Column(name = "job_name", nullable = false)
	private String jobName;

	@Column(name = "billable", nullable = false)
	private boolean billable;

	// Department of the job
	@ManyToOne
	@JoinColumn(name = "dept_id", nullable = false)
	private Department department;

	public Job(String jobID, String jobName, boolean billable, Department department) {
		super();
		this.jobID = jobID;
		this.jobName = jobName;
		this.billable = billable;
		this.department = department;
	}

	public Job() {
		super();

	}

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

}
