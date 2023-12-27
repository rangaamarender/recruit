package com.lucid.recruit.referencedata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = WorkerInActiveStatusCodes.TABLE_NAME)
public class WorkerInActiveStatusCodes {

	public static final String TABLE_NAME = "ref_worker_inactive_status_codes";

	@Id
	@Column(name = "status_code", nullable = false, length = 75, updatable = false)
	private String code;

	@Column(name = "display_name", nullable = false, length = 75)
	private String displayName;
	@Column(name = "reinstate", nullable = false, updatable = false)
	private boolean reinstate;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isReinstate() {
		return reinstate;
	}

	public void setReinstate(boolean reinstate) {
		this.reinstate = reinstate;
	}
}
