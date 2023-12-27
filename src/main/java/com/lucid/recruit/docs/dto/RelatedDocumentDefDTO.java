package com.lucid.recruit.docs.dto;

import jakarta.validation.constraints.NotNull;

public class RelatedDocumentDefDTO {
	@NotNull
	private String documentDefID;
	@NotNull
	private String documentName;
	@NotNull
	private String docDisplayName;

	public RelatedDocumentDefDTO() {
		super();
	}

	public String getDocumentDefID() {
		return documentDefID;
	}

	public void setDocumentDefID(String documentDefID) {
		this.documentDefID = documentDefID;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocDisplayName() {
		return docDisplayName;
	}

	public void setDocDisplayName(String docDisplayName) {
		this.docDisplayName = docDisplayName;
	}

}
