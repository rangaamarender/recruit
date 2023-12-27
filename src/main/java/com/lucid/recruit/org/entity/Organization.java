package com.lucid.recruit.org.entity;

import java.util.List;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.referencedata.entity.Country;
import com.lucid.recruit.referencedata.entity.TaxClassificationDef;

import jakarta.persistence.*;

@Entity
@Table(name = Organization.TABLE_NAME,uniqueConstraints = {@UniqueConstraint(name = "UNQORG",columnNames = {"name"})})
public class Organization extends AuditableEntity {

	private static final long serialVersionUID = 859660616606497198L;
	public static final String TABLE_NAME = "o_organization";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "org_id", nullable = false, length = 75)
	private String organizationID;

	@Column(name = "name", nullable = false, length = 75)
	private String name;

	@Column(name = "trade_name", nullable = true, length = 75)
	private String tradeName;

	@Column(name = "phone", nullable = false, length = 15)
	private String phoneNumber;

	@Column(name = "fax", nullable = true, length = 20)
	private String fax;

	// Free text description for that entity
	@Column(name = "description", nullable = true, length = 400)
	private String description;

	// Organization code
	@Column(name = "code", nullable = true, length = 25)
	private String code;

	@ManyToOne
	@JoinColumn(name = "country_code", nullable = false)
	private Country country;

	// Federal Tax Identification Number or EIN
	@Column(name = "tax_id", nullable = true, length = 75)
	private String taxId;

	// Tax Classification
	@ManyToOne
	@JoinColumn(name = "taxclass_code", nullable = true)
	private TaxClassificationDef taxClassification;

	// Federal Tax Identification Number or EIN
	@Column(name = "state_inc", nullable = true, length = 75)
	private String stateOfInc;

	@Column(name = "logo_url",nullable = true,length = 240)
	private String logoUrl;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrganizationStatus> status;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrgDomain> orgDomains;

	// List of Communication
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrgCommunication> orgCommunications;

	// List of Address
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrgAddress> orgAddresses;

	// List of Org Documents
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrganizationDocument> organizationDocuments;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "organization")
	private List<OrganizationAttributes> organizationAttributes;


	public Organization() {
		super();
	}

	public String getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<OrgCommunication> getOrgCommunications() {
		return orgCommunications;
	}

	public void setOrgCommunications(List<OrgCommunication> orgCommunications) {
		this.orgCommunications = orgCommunications;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<OrganizationStatus> getStatus() {
		return status;
	}

	public void setStatus(List<OrganizationStatus> status) {
		this.status = status;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public List<OrgDomain> getOrgDomains() {
		return orgDomains;
	}

	public void setOrgDomains(List<OrgDomain> orgDomains) {
		this.orgDomains = orgDomains;
	}

	public List<OrgAddress> getOrgAddresses() {
		return orgAddresses;
	}

	public void setOrgAddresses(List<OrgAddress> orgAddresses) {
		this.orgAddresses = orgAddresses;
	}

	public List<OrganizationDocument> getOrganizationDocuments() {
		return organizationDocuments;
	}

	public void setOrganizationDocuments(List<OrganizationDocument> organizationDocuments) {
		this.organizationDocuments = organizationDocuments;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getStateOfInc() {
		return stateOfInc;
	}

	public void setStateOfInc(String stateOfInc) {
		this.stateOfInc = stateOfInc;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public TaxClassificationDef getTaxClassification() {
		return taxClassification;
	}

	public void setTaxClassification(TaxClassificationDef taxClassification) {
		this.taxClassification = taxClassification;
	}
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<OrganizationAttributes> getOrganizationAttributes() {
		return organizationAttributes;
	}

	public void setOrganizationAttributes(List<OrganizationAttributes> organizationAttributes) {
		this.organizationAttributes = organizationAttributes;
	}

}
