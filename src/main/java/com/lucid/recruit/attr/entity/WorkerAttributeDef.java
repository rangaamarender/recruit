
package com.lucid.recruit.attr.entity;

import com.lucid.recruit.referencedata.entity.WorkerType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = WorkerAttributeDef.TABLE_NAME)
public class WorkerAttributeDef extends BaseAttributeDef {

	private static final long serialVersionUID = -3280321113063035670L;
	public static final String TABLE_NAME = "xt_worker_attr_def";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "attr_def_id", nullable = false, length = 75)
	private String attrDefId;

	@ManyToOne
	@JoinColumn(name = "worker_type_code",nullable = true)
	private WorkerType workerType;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workerAttributeDef", cascade = CascadeType.ALL)
	private List<WorkerAttrDefListValues> attrListValues;
	public String getAttrDefId() {
		return attrDefId;
	}
	public void setAttrDefId(String attrDefId) {
		this.attrDefId = attrDefId;
	}
	public WorkerType getWorkerType() {
		return workerType;
	}
	public void setWorkerType(WorkerType workerType) {
		this.workerType = workerType;
	}

	public List<WorkerAttrDefListValues> getAttrListValues() {
		return attrListValues;
	}

	public void setAttrListValues(List<WorkerAttrDefListValues> attrListValues) {
		this.attrListValues = attrListValues;
	}
}
