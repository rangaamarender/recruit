package com.lucid.recruit.contract.entity;

import com.lucid.recruit.docs.entity.BaseDocAttributes;
import jakarta.persistence.*;

@Entity
@Table(name = WorkOrderDocAttributes.TABLE_NAME)
public class WorkOrderDocAttributes extends BaseDocAttributes {

	private static final long serialVersionUID = 3127766575009544916L;
	public static final String TABLE_NAME = "c_wo_doc_attributes";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "c_wo_attr_id", nullable = false, length = 75)
	private String woDocAttrID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wo_doc_id", nullable = false, updatable = false)
	private WorkOrderDocument woDocument;

	public WorkOrderDocAttributes() {
		super();
	}

	public String getWoDocAttrID() {
		return woDocAttrID;
	}

	public void setWoDocAttrID(String woDocAttrID) {
		this.woDocAttrID = woDocAttrID;
	}

	public WorkOrderDocument getWoDocument() {
		return woDocument;
	}

	public void setWoDocument(WorkOrderDocument woDocument) {
		this.woDocument = woDocument;
	}

}
