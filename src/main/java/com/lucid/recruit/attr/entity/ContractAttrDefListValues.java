package com.lucid.recruit.attr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = ContractAttrDefListValues.TABLE_NAME)
public class ContractAttrDefListValues extends BaseAttrDefListValues {

	private static final long serialVersionUID = -789790508275105499L;
	public static final String TABLE_NAME = "xt_cntrctattrdeflistvalues";
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "attr_list_value_id", nullable = false, length = 75)
	private String attrListValueID;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_def_id", nullable = false, updatable = false)
	private ContractAttributeDef contractAttributeDef;

	public ContractAttrDefListValues() {
		super();
	}

	public ContractAttrDefListValues(String value, Integer displayOrder, ContractAttributeDef contractAttributeDef) {
		this.setValue(value);
		this.setDisplayOrder(displayOrder);
		this.contractAttributeDef = contractAttributeDef;
	}

	public String getAttrListValueID() {
		return attrListValueID;
	}

	public void setAttrListValueID(String attrListValueID) {
		this.attrListValueID = attrListValueID;
	}

	public ContractAttributeDef getContractAttributeDef() {
		return contractAttributeDef;
	}

	public void setContractAttributeDef(ContractAttributeDef contractAttributeDef) {
		this.contractAttributeDef = contractAttributeDef;
	}
}
