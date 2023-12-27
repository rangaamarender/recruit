/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.docs.entity;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.core.entity.BasePhoneNbr;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseSignableDocument extends AuditableEntity {
	private static final long serialVersionUID = 8692569169877888781L;

	@Column(name = "given_name", nullable = false, length = 100)
	private String givenName;

	@Column(name = "middle_name", nullable = true, length = 100)
	private String middleName;

	@Column(name = "family_name", nullable = false, length = 50)
	private String familyName;

	@Column(name = "email_id", nullable = false, length = 75)
	private String emailId;

	@Embedded
	private BasePhoneNbr phoneNumber;

	// document comments if any
	@Column(name = "comments", nullable = true, length = 2500)
	private String comments;

	public BaseSignableDocument() {
		super();
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public BasePhoneNbr getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(BasePhoneNbr phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
