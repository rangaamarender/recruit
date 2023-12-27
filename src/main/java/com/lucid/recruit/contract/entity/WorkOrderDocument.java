/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.entity;

import com.lucid.recruit.docs.entity.BaseDocument;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = WorkOrderDocument.TABLE_NAME)
public class WorkOrderDocument extends BaseDocument {

	private static final long serialVersionUID = 8419402802337278054L;
	public static final String TABLE_NAME = "c_wo_doc";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "doc_id", nullable = false, length = 75)
	private String docID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_order_id",updatable = false)
	private ContractWorkOrder contractWorkOrder;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "woDocument",cascade = CascadeType.ALL)
	@OrderBy(value = "woDocAttrID")
	private List<WorkOrderDocAttributes> workOrderDocAttributes;


	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public ContractWorkOrder getContractWorkOrder() {
		return contractWorkOrder;
	}

	public void setContractWorkOrder(ContractWorkOrder contractWorkOrder) {
		this.contractWorkOrder = contractWorkOrder;
	}

	public List<WorkOrderDocAttributes> getWorkOrderDocAttributes() {
		return workOrderDocAttributes;
	}

	public void setWorkOrderDocAttributes(List<WorkOrderDocAttributes> workOrderDocAttributes) {
		this.workOrderDocAttributes = workOrderDocAttributes;
	}

}
