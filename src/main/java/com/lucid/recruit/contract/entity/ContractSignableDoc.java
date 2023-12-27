/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.entity;

import com.lucid.recruit.docs.entity.BaseSignableDocument;

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
@Table(name = ContractSignableDoc.TABLE_NAME)
public class ContractSignableDoc extends BaseSignableDocument {

	private static final long serialVersionUID = 8439853915481350533L;
	public static final String TABLE_NAME = "c_cntrct_sig_doc";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "sign_doc_id", nullable = false, length = 75)
	private String signDocID;

	// organization id of the party B
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "doc_id", nullable = false)
	private ContractDocument contractDocument;

	public String getSignDocID() {
		return signDocID;
	}

	public void setSignDocID(String signDocID) {
		this.signDocID = signDocID;
	}

	public ContractDocument getContractDocument() {
		return contractDocument;
	}

	public void setContractDocument(ContractDocument contractDocument) {
		this.contractDocument = contractDocument;
	}

}
