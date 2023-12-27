package com.lucid.recruit.worker.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.referencedata.entity.Job;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Worker assignments of the worker organization.
 * 
 * @author chandu
 *
 */
@Entity
@Table(name = WorkAssignment.TABLE_NAME)
public class WorkAssignment extends AuditableEntity {
	public static final String TABLE_NAME = "w_work_assignment";
	private static final long serialVersionUID = -4060856231425415538L;

	// Worker Assignment Unique Integration ID UUID value
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "work_assignment_id", nullable = false)
	private String workerAssgmtId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "worker_id", nullable = false, updatable = false)
	private Worker worker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id", nullable = false, updatable = false)
	private Job job;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	@Column(name = "expected_start_date", nullable = false)
	private LocalDate expectedStartDate;

	@Column(name = "probationary_start_date", nullable = true)
	private LocalDate poStartDate;

	@Column(name = "probationary_end_date", nullable = true)
	private LocalDate poEndDate;

	public WorkAssignment() {
		super();
	}

	public WorkAssignment(String workerAssgmtId, Worker worker, Job job, LocalDate startDate, LocalDate endDate,
						  LocalDate expectedStartDate, LocalDate poStartDate, LocalDate poEndDate) {
		super();
		this.workerAssgmtId = workerAssgmtId;
		this.worker = worker;
		this.job = job;
		this.startDate = startDate;
		this.endDate = endDate;
		this.expectedStartDate = expectedStartDate;
		this.poStartDate = poStartDate;
		this.poEndDate = poEndDate;
	}

	public static Comparator<WorkAssignment> createLambdaComparatorByStartDate() {
		return (o1, o2) -> o1.getStartDate().compareTo(o2.startDate);
	}

	public String getWorkerAssgmtId() {
		return workerAssgmtId;
	}

	public void setWorkerAssgmtId(String workerAssgmtId) {
		this.workerAssgmtId = workerAssgmtId;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
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

	public LocalDate getExpectedStartDate() {
		return expectedStartDate;
	}

	public void setExpectedStartDate(LocalDate expectedStartDate) {
		this.expectedStartDate = expectedStartDate;
	}

	public LocalDate getPoStartDate() {
		return poStartDate;
	}

	public void setPoStartDate(LocalDate poStartDate) {
		this.poStartDate = poStartDate;
	}

	public LocalDate getPoEndDate() {
		return poEndDate;
	}

	public void setPoEndDate(LocalDate poEndDate) {
		this.poEndDate = poEndDate;
	}


}
