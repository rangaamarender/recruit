package com.lucid.recruit.attr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = OrgAttrDefListValues.TABLE_NAME)
public class OrgAttrDefListValues extends BaseAttrDefListValues {

	private static final long serialVersionUID = -789790508275105499L;
	public static final String TABLE_NAME = "xt_orgattrdeflistvalues";
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "attr_list_value_id", nullable = false, length = 75)
	private String attrListValueID;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_def_id", nullable = false, updatable = false)
	private OrgAttributeDef orgAttributeDef;

	public OrgAttrDefListValues() {
		super();
	}

	public OrgAttrDefListValues(String value, Integer displayOrder, OrgAttributeDef orgAttributeDef) {
		this.setValue(value);
		this.setDisplayOrder(displayOrder);
		this.orgAttributeDef = orgAttributeDef;
	}

	public String getAttrListValueID() {
		return attrListValueID;
	}

	public void setAttrListValueID(String attrListValueID) {
		this.attrListValueID = attrListValueID;
	}

	public OrgAttributeDef getOrgAttributeDef() {
		return orgAttributeDef;
	}

	public void setOrgAttributeDef(OrgAttributeDef orgAttributeDef) {
		this.orgAttributeDef = orgAttributeDef;
	}
}
