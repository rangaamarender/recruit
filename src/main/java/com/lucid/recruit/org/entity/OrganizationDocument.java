/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.org.entity;

import com.lucid.recruit.docs.entity.BaseDocument;

import com.lucid.recruit.worker.entity.WorkerDocAttributes;
import jakarta.persistence.*;

import java.util.List;

/**
 * @author sgutti
 * @date 26-Jan-2023 12:57:54 am
 */
@Entity
@Table(name = OrganizationDocument.TABLE_NAME)
public class OrganizationDocument extends BaseDocument {

	private static final long serialVersionUID = 8419402802337278054L;
	public static final String TABLE_NAME = "o_org_doc";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "org_doc_id", nullable = false, length = 75)
	private String organizationDocID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", updatable = false, nullable = false)
	private Organization organization;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organizationDocument")
	@OrderBy(value = "orgDocAttrID")
	private List<OrgDocAttributes> orgDocAttributes;

	/**
	 * Create a new <code>ContractDocument</code>
	 */
	public OrganizationDocument() {
		super();
	}

	public String getOrganizationDocID() {
		return organizationDocID;
	}

	public void setOrganizationDocID(String organizationDocID) {
		this.organizationDocID = organizationDocID;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public List<OrgDocAttributes> getOrgDocAttributes() {
		return orgDocAttributes;
	}

	public void setOrgDocAttributes(List<OrgDocAttributes> orgDocAttributes) {
		this.orgDocAttributes = orgDocAttributes;
	}
}
