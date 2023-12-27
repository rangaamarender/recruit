package com.lucid.recruit.timecard.constants;

public enum EnumTimeCardStatus {
	DRAFT, // created the time card but haven’t submitted it yet.
	SUBMIT, // Submit will move to PENDING_APPROVAL
	PENDING_APPROVAL, // Pending approval from Tenant
	PENDING_APPROVAL_CLIENT, // Pending approval from client
	APPROVED, // Approved
	REJECTED;// An approved rejected this time card
}
