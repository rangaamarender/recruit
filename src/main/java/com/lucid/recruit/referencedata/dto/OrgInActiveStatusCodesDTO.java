package com.lucid.recruit.referencedata.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class OrgInActiveStatusCodesDTO {

	@NotNull
	private String code;
	@Nullable
	private boolean reinstate;
	@Nullable
	private String displayName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isReinstate() {
		return reinstate;
	}

	public void setReinstate(boolean reinstate) {
		this.reinstate = reinstate;
	}

	@Nullable
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(@Nullable String displayName) {
		this.displayName = displayName;
	}

}
