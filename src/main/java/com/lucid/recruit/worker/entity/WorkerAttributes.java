package com.lucid.recruit.worker.entity;

import com.lucid.recruit.attr.entity.WorkerAttributeDef;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = WorkerAttributes.TABLE_NAME)
public class WorkerAttributes {

	private static final long serialVersionUID = 3127766575009544916L;
	public static final String TABLE_NAME = "w_worker_attributes";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "worker_attr_id", nullable = false, length = 75)
	private String workerAttrID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "worker_id", nullable = false, updatable = false)
	private Worker worker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "w_attr_group_def_id", nullable = false, updatable = false)
	private WorkerAttributeDef workerAttributeDef;

	@Column(name = "attr_value")
	private String attrValue;

	@Column(name = "start_dt",nullable = false)
	private LocalDate startDate;

	@Column(name = "end_dt")
	private LocalDate endDate;

	public WorkerAttributes() {
		super();
	}

	public String getWorkerAttrID() {
		return workerAttrID;
	}

	public void setWorkerAttrID(String workerAttrID) {
		this.workerAttrID = workerAttrID;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}


	public WorkerAttributeDef getWorkerAttributeDef() {
		return workerAttributeDef;
	}

	public void setWorkerAttributeDef(WorkerAttributeDef workerAttributeDef) {
		this.workerAttributeDef = workerAttributeDef;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

}
