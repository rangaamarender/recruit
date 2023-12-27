/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.person.entity;

import java.io.Serial;

import com.lucid.recruit.docs.entity.BaseDocument;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = PersonLegalDocument.TABLE_NAME)
public class PersonLegalDocument extends BaseDocument {

	@Serial
	private static final long serialVersionUID = 7591561944068781281L;
	public static final String TABLE_NAME = "p_prsn_legal_doc";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "person_doc_id", nullable = false, length = 75)
	private String personDocID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_legal_id", nullable = false, updatable = false)
	private PersonLegal personLegal;

	public PersonLegalDocument() {
		super();
	}

	public String getLegalDocumentID() {
		return personDocID;
	}

	public void setLegalDocumentID(String legalDocumentID) {
		this.personDocID = legalDocumentID;
	}

	public PersonLegal getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegal personLegal) {
		this.personLegal = personLegal;
	}

}
