package com.lucid.recruit.worker.entity;

import com.lucid.recruit.docs.entity.BaseDocAttributes;

import jakarta.persistence.*;

@Entity
@Table(name = WorkerDocAttributes.TABLE_NAME)
public class WorkerDocAttributes extends BaseDocAttributes {

	private static final long serialVersionUID = 3127766575009544916L;
	public static final String TABLE_NAME = "w_worker_doc_attributes";

	@Id	
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "worker_doc_attr_id", nullable = false, length = 75)
	private String workerDocAttrID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "worker_doc_id", nullable = false, updatable = false)
	private WorkerDocument WorkerDocument;

	public WorkerDocAttributes() {
		super();
	}

	public String getWorkerDocAttrID() {
		return workerDocAttrID;
	}

	public void setWorkerDocAttrID(String workerDocAttrID) {
		this.workerDocAttrID = workerDocAttrID;
	}

	public WorkerDocument getWorkerDocument() {
		return WorkerDocument;
	}

	public void setWorkerDocument(WorkerDocument workerDocument) {
		WorkerDocument = workerDocument;
	}
}
