package com.lucid.recruit.attr.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = WorkerAttrDefListValues.TABLE_NAME)
public class WorkerAttrDefListValues extends BaseAttrDefListValues {

	private static final long serialVersionUID = -789790508275105499L;
	public static final String TABLE_NAME = "xt_wrkattrdeflistvalues";
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "attr_list_value_id", nullable = false, length = 75)
	private String attrListValueID;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_def_id", nullable = false, updatable = false)
	private WorkerAttributeDef workerAttributeDef;

	public WorkerAttrDefListValues() {
		super();
	}

	public WorkerAttrDefListValues(String value, Integer displayOrder, WorkerAttributeDef workerAttributeDef) {
		this.setValue(value);
		this.setDisplayOrder(displayOrder);
		this.workerAttributeDef = workerAttributeDef;
	}

	public String getAttrListValueID() {
		return attrListValueID;
	}

	public void setAttrListValueID(String attrListValueID) {
		this.attrListValueID = attrListValueID;
	}

	public WorkerAttributeDef getWorkerAttributeDef() {
		return workerAttributeDef;
	}

	public void setWorkerAttributeDef(WorkerAttributeDef workerAttributeDef) {
		this.workerAttributeDef = workerAttributeDef;
	}
}
