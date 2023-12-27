
package com.lucid.recruit.attr.entity;

import com.lucid.recruit.referencedata.entity.WorkerType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = ContractAttributeDef.TABLE_NAME)
public class ContractAttributeDef extends BaseAttributeDef {

	private static final long serialVersionUID = -3280321113063035670L;
	public static final String TABLE_NAME = "xt_contract_attr_def";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "attr_def_id", nullable = false, length = 75)
	private String attrDefId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractAttributeDef", cascade = CascadeType.ALL)
	private List<ContractAttrDefListValues> attrListValues;
	public String getAttrDefId() {
		return attrDefId;
	}
	public void setAttrDefId(String attrDefId) {
		this.attrDefId = attrDefId;
	}

	public List<ContractAttrDefListValues> getAttrListValues() {
		return attrListValues;
	}

	public void setAttrListValues(List<ContractAttrDefListValues> attrListValues) {
		this.attrListValues = attrListValues;
	}
}
