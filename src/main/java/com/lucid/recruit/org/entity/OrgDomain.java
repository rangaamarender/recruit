package com.lucid.recruit.org.entity;

import com.lucid.core.entity.AuditableEntity;

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
@Table(name = OrgDomain.TABLE_NAME)
public class OrgDomain extends AuditableEntity {

	private static final long serialVersionUID = -50103487964927851L;

	public static final String TABLE_NAME = "o_orgdomains";

	@Id
	@Column(name = "domain_id", nullable = false, length = 75)
	@GeneratedValue(strategy = GenerationType.UUID)
	private String domainID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "org_id", nullable = false, updatable = true)
	private Organization organization;

	@Column(name = "domain_name", nullable = false, updatable = true, length = 254)
	private String domain;

	@Column(name = "webdomain",nullable = false,updatable = true)
	private String webDomain;

	// deleted flag
	@Column(name = "deleted", nullable = true)
	private Boolean deleted;

	public String getDomainID() {
		return domainID;
	}

	public void setDomainID(String domainID) {
		this.domainID = domainID;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getWebDomain() {
		return webDomain;
	}

	public void setWebDomain(String webDomain) {
		this.webDomain = webDomain;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
