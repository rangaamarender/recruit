/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.dto;

import com.lucid.recruit.docs.dto.BaseDocumentDTO;
import jakarta.annotation.Nullable;

import java.util.List;

public class ContractDocumentDTO extends BaseDocumentDTO {

	@Nullable
	private String docID;

	@Nullable
	private List<ContractDocAttributesDTO> contractDocAttributes;

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}


	public List<ContractDocAttributesDTO> getContractDocAttributes() {
		return contractDocAttributes;
	}

	public void setContractDocAttributes(List<ContractDocAttributesDTO> contractDocAttributes) {
		this.contractDocAttributes = contractDocAttributes;
	}
}
