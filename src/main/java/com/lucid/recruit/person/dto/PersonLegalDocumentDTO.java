package com.lucid.recruit.person.dto;

import com.lucid.recruit.docs.dto.BaseDocumentDTO;

import jakarta.annotation.Nullable;

public class PersonLegalDocumentDTO extends BaseDocumentDTO {
	@Nullable
	private String personDocID;

	public String getLegalDocumentID() {
		return personDocID;
	}

	public void setLegalDocumentID(String legalDocumentID) {
		this.personDocID = legalDocumentID;
	}

}
