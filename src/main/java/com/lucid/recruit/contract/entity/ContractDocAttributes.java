package com.lucid.recruit.contract.entity;

import com.lucid.recruit.docs.entity.BaseDocAttributes;
import jakarta.persistence.*;

@Entity
@Table(name = ContractDocAttributes.TABLE_NAME)
public class ContractDocAttributes extends BaseDocAttributes {

	private static final long serialVersionUID = 3127766575009544916L;
	public static final String TABLE_NAME = "c_contract_doc_attributes";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "contract_doc_attr_id", nullable = false, length = 75)
	private String contractDocAttrID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_doc_id", nullable = false, updatable = false)
	private ContractDocument contractDocument;

	public ContractDocAttributes() {
		super();
	}

	public String getContractDocAttrID() {
		return contractDocAttrID;
	}

	public void setContractDocAttrID(String contractDocAttrID) {
		this.contractDocAttrID = contractDocAttrID;
	}

	public ContractDocument getContractDocument() {
		return contractDocument;
	}

	public void setContractDocument(ContractDocument contractDocument) {
		this.contractDocument = contractDocument;
	}

}
