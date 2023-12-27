package com.lucid.recruit.org.dto;

import java.time.LocalDate;
import java.util.List;

import com.lucid.core.annotations.Phone;
import com.lucid.recruit.referencedata.dto.RefCountryDTO;
import com.lucid.recruit.referencedata.dto.TaxClassificationDefDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrganizationDTO {
	@Nullable
	private String organizationID;
	@NotNull
	private String name;
	@Nullable
	private String tradeName;
	@Phone
	@NotNull
	private String phoneNumber;
	@Nullable
	private String fax;
	@Nullable
	private String description;
	@Nullable
	private String code;
	@NotNull
	private RefCountryDTO country;
	@Nullable
	private String taxId;
	@Nullable
	private LocalDate createdDt;
	@Nullable
	private TaxClassificationDefDTO taxClassification;
	@Nullable
	private String stateOfInc;
	@Nullable
	private List<OrganizationStatusDTO> status;
	@NotNull
	@Valid
	@Size(min = 1, message = "AtLeast one domain is required")
	private List<OrgDomainDTO> orgDomains;
	@Nullable
	@Valid
	@Size(min = 1, max = 1, message = "CommunicationDetails mandatory")
	private List<OrgCommunicationDTO> orgCommunications;
	@Nullable
	private List<OrgAddressDTO> orgAddresses;
	@Nullable
	private List<OrganizationAttributesDTO> organizationAttributes;

	@Nullable
	private List<OrganizationDocumentDTO> organizationDocuments;

	@Nullable
	public String getOrganizationID() {
		return organizationID;
	}

	public OrganizationDTO(String name) {
		this.name = name;
	}

	public OrganizationDTO() {
	}

	public void setOrganizationID(@Nullable String organizationID) {
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Nullable
	public String getFax() {
		return fax;
	}

	public void setFax(@Nullable String fax) {
		this.fax = fax;
	}

	@Nullable
	public String getDescription() {
		return description;
	}

	public void setDescription(@Nullable String description) {
		this.description = description;
	}

	@Nullable
	public String getCode() {
		return code;
	}

	public void setCode(@Nullable String code) {
		this.code = code;
	}

	@Nullable
	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(@Nullable String taxId) {
		this.taxId = taxId;
	}

	public List<OrgDomainDTO> getOrgDomains() {
		return orgDomains;
	}

	public void setOrgDomains(List<OrgDomainDTO> orgDomains) {
		this.orgDomains = orgDomains;
	}

	public void setTaxClassification(@Nullable TaxClassificationDefDTO taxClassification) {
		this.taxClassification = taxClassification;
	}

	public List<OrgAddressDTO> getOrgAddresses() {
		return orgAddresses;
	}

	public void setOrgAddresses(List<OrgAddressDTO> orgAddresses) {
		this.orgAddresses = orgAddresses;
	}

	public void setStatus(List<OrganizationStatusDTO> status) {
		this.status = status;
	}

	public List<OrgCommunicationDTO> getOrgCommunications() {
		return orgCommunications;
	}

	public void setOrgCommunications(List<OrgCommunicationDTO> orgCommunications) {
		this.orgCommunications = orgCommunications;
	}

	public RefCountryDTO getCountry() {
		return country;
	}

	public void setCountry(RefCountryDTO country) {
		this.country = country;
	}

	@Nullable
	public TaxClassificationDefDTO getTaxClassification() {
		return taxClassification;
	}

	@Nullable
	public String getStateOfInc() {
		return stateOfInc;
	}

	public void setStateOfInc(@Nullable String stateOfInc) {
		this.stateOfInc = stateOfInc;
	}

	@Nullable
	public List<OrganizationStatusDTO> getStatus() {
		return status;
	}

	@Nullable
	public LocalDate getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(@Nullable LocalDate createdDt) {
		this.createdDt = createdDt;
	}

	@Nullable
	public List<OrganizationAttributesDTO> getOrganizationAttributes() {
		return organizationAttributes;
	}

	public void setOrganizationAttributes(@Nullable List<OrganizationAttributesDTO> organizationAttributes) {
		this.organizationAttributes = organizationAttributes;
	}

	@Nullable
	public List<OrganizationDocumentDTO> getOrganizationDocuments() {
		return organizationDocuments;
	}

	public void setOrganizationDocuments(@Nullable List<OrganizationDocumentDTO> organizationDocuments) {
		this.organizationDocuments = organizationDocuments;
	}
}
