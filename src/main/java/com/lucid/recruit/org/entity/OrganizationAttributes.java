package com.lucid.recruit.org.entity;

import com.lucid.recruit.attr.entity.OrgAttributeDef;
import com.lucid.recruit.attr.entity.WorkerAttributeDef;
import com.lucid.recruit.worker.entity.Worker;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = OrganizationAttributes.TABLE_NAME)
public class OrganizationAttributes {

	public static final String TABLE_NAME = "o_organization_attributes";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "org_attr_id", nullable = false, length = 75)
	private String orgAttrID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", nullable = false, updatable = false)
	private Organization organization;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "o_attr_group_def_id", nullable = false, updatable = false)
	private OrgAttributeDef orgAttributeDef;

	@Column(name = "attr_value")
	private String attrValue;

	@Column(name = "start_dt",nullable = false)
	private LocalDate startDate;

	@Column(name = "end_dt")
	private LocalDate endDate;

	public String getOrgAttrID() {
		return orgAttrID;
	}

	public void setOrgAttrID(String orgAttrID) {
		this.orgAttrID = orgAttrID;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public OrgAttributeDef getOrgAttributeDef() {
		return orgAttributeDef;
	}

	public void setOrgAttributeDef(OrgAttributeDef orgAttributeDef) {
		this.orgAttributeDef = orgAttributeDef;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

}
