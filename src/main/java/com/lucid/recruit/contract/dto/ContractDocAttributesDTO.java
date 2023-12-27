package com.lucid.recruit.contract.dto;

import com.lucid.recruit.docs.dto.BaseDocAttributesDTO;
import jakarta.annotation.Nullable;


public class ContractDocAttributesDTO extends BaseDocAttributesDTO {

	@Nullable
	private String contractDocAttrID;

	public ContractDocAttributesDTO() {
		super();
	}

	public String getContractDocAttrID() {
		return contractDocAttrID;
	}

	public void setContractDocAttrID(String contractDocAttrID) {
		this.contractDocAttrID = contractDocAttrID;
	}


}
