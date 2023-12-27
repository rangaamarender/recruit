/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.dto;

import com.lucid.recruit.docs.dto.BaseDocumentDTO;
import jakarta.annotation.Nullable;
import java.util.List;

public class WorkOrderDocumentDTO extends BaseDocumentDTO{

	@Nullable
	private String docID;
	@Nullable
	private List<WorkOrderDocAttributesDTO> workOrderDocAttributes;


	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}


	public List<WorkOrderDocAttributesDTO> getWorkOrderDocAttributes() {
		return workOrderDocAttributes;
	}

	public void setWorkOrderDocAttributes(List<WorkOrderDocAttributesDTO> workOrderDocAttributes) {
		this.workOrderDocAttributes = workOrderDocAttributes;
	}

}
