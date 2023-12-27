/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without
 * permission.
 */
package com.lucid.core.email.entity;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sgutti
 * @date Oct 29, 2018 9:50:34 PM *  * 
 */

@Entity
@Table(name = EmailContent.TABLE_NAME)
public class EmailContent {

	public static final String TABLE_NAME="email_content";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String emailContentId;

	@Column(name = "from_mail",nullable = false)
	private String from;

/*	@Column(name = "to_mail",nullable = false)
	private String toMail;*/

	@Column(name = "from_name")
	private String fromName;

	@Column(name = "subject",nullable = false)
	private String subject;

	@Column(name = "body",nullable = false)
	private String body;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "to_mails", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "to_mail",nullable = false)
	private List<String> toList = new ArrayList<>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "cc_mails", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "to_mail")
	private List<String> ccList = new ArrayList<>();

	@Column(name = "status",nullable = false)
	private String status;

	@Column(name = "resend_count")
	private int resendCount;

	// ------------------------------------------------------------ Constructors
	/**
	 * Create a new <code>EmailContent</code>
	 */
	public EmailContent() {
		super();
	}
	// ---------------------------------------------------------- Public Methods

	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from The from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return Returns the body.
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body The body to set.
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return Returns the toList.
	 */
	public List<String> getToList() {
		return toList;
	}

	/**
	 * @param toList The toList to set.
	 */
	public void setToList(List<String> toList) {
		this.toList = toList;
	}

	/**
	 * @param to
	 */
	public void addToList(String to) {
		if (toList != null) {
			toList.add(to);
		}
	}

	/**
	 * @return Returns the ccList.
	 */
	public List<String> getCcList() {
		return ccList;
	}

	/**
	 * @param ccList The ccList to set.
	 */
	public void setCcList(List<String> ccList) {
		this.ccList = ccList;
	}

	/**
	 * @return Returns the fromName.
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @param fromName The fromName to set.
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * @return
	 */
	public String[] getCc() {
		return ccList.toArray(new String[ccList.size()]);
	}

	/**
	 * @return
	 */
	public String[] getTo() {
		return toList.toArray(new String[toList.size()]);
	}

	public String getEmailContentId() {
		return emailContentId;
	}

	public void setEmailContentId(String emailContentId) {
		this.emailContentId = emailContentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getResendCount() {
		return resendCount;
	}

	public void setResendCount(int resendCount) {
		this.resendCount = resendCount;
	}
/*
	public String getToMail() {
		return toMail;
	}

	public void setToMail(String toMail) {
		this.toMail = toMail;
	}*/
}
