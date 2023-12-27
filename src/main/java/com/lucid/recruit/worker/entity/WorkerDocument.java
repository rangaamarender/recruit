package com.lucid.recruit.worker.entity;

import java.util.List;

import com.lucid.recruit.docs.constants.EnumDocSource;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.entity.BaseDocument;
import com.lucid.recruit.docs.entity.DocumentDef;

import jakarta.persistence.*;

@Entity
@Table(name = WorkerDocument.TABLE_NAME)
public class WorkerDocument extends BaseDocument {

	// Constants
	public static final String TABLE_NAME = "w_worker_document";
	private static final long serialVersionUID = -1161945458547022528L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "worker_doc_id", nullable = false, length = 75)
	private String workerdocID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "worker_id", nullable = false, updatable = false)
	private Worker worker;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "WorkerDocument")
	@OrderBy(value = "workerDocAttrID")
	private List<WorkerDocAttributes> workerDocAttributes;

	public WorkerDocument() {
		super();
	}

	public WorkerDocument(String workerdocID, Worker worker, DocumentDef documentDef, String fileName, String fileType,
			String fileExt, EnumDocSource docSource, EnumDocStatus status, String url) {
		super(documentDef, fileName, fileType, fileExt, docSource, status, url);
		this.workerdocID = workerdocID;
		this.worker = worker;
	}

	public String getWorkerdocID() {
		return workerdocID;
	}

	public void setWorkerdocID(String workerdocID) {
		this.workerdocID = workerdocID;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public List<WorkerDocAttributes> getWorkerDocAttributes() {
		return workerDocAttributes;
	}

	public void setWorkerDocAttributes(List<WorkerDocAttributes> workerDocAttributes) {
		this.workerDocAttributes = workerDocAttributes;
	}

}
