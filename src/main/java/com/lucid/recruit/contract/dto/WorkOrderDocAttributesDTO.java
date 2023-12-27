package com.lucid.recruit.contract.dto;

import com.lucid.recruit.docs.dto.BaseDocAttributesDTO;
import jakarta.annotation.Nullable;

public class WorkOrderDocAttributesDTO extends BaseDocAttributesDTO {

    @Nullable
	private String woDocAttrID;

	public WorkOrderDocAttributesDTO() {
		super();
	}

	public String getWoDocAttrID() {
		return woDocAttrID;
	}


}
