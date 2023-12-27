package com.lucid.recruit.org.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class OrgDomainDTO {
	@Nullable
	private String domainID;
	@NotNull
	private String domain;
	@Nullable
	private Boolean deleted;

	public String getDomainID() {
		return domainID;
	}

	public void setDomainID(String domainID) {
		this.domainID = domainID;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}




}
