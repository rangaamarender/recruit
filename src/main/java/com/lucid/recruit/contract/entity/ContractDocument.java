/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.entity;

import java.util.List;

import com.lucid.recruit.docs.entity.BaseDocument;

import jakarta.persistence.*;

@Entity
@Table(name = ContractDocument.TABLE_NAME)
public class ContractDocument extends BaseDocument {

	private static final long serialVersionUID = 8419402802337278054L;
	public static final String TABLE_NAME = "c_cntrct_doc";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "doc_id", nullable = false, length = 75)
	private String docID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cntrct_id",updatable = false)
	private Contract contract;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractDocument",cascade = CascadeType.ALL)
	@OrderBy(value = "contractDocAttrID")
	private List<ContractDocAttributes> contractDocAttributes;

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public List<ContractDocAttributes> getContractDocAttributes() {
		return contractDocAttributes;
	}

	public void setContractDocAttributes(List<ContractDocAttributes> contractDocAttributes) {
		this.contractDocAttributes = contractDocAttributes;
	}
}
