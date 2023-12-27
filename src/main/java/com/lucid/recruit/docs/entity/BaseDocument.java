/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.docs.entity;

import java.time.LocalDate;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.docs.constants.EnumDocSource;
import com.lucid.recruit.docs.constants.EnumDocStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseDocument extends AuditableEntity {
	private static final long serialVersionUID = 8085608713400344403L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "doc_def_id", nullable = false)
	private DocumentDef documentDef;

	// File Name
	@Column(name = "file_name", nullable = true, length = 25)
	private String fileName;

	// Document file type, pdf, excel, xml etc
	@Column(name = "file_type", nullable = true, length = 25)
	private String fileType;

	// Document file extension
	@Column(name = "file_ext", nullable = true, length = 25)
	private String fileExt;

	// Document source like Default Assigned, Uploaded
	@Enumerated(value = EnumType.STRING)
	@Column(name = "doc_source", length = 25)
	private EnumDocSource docSource;

	// Document is active/inactive/discarded in the system
	@Enumerated(value = EnumType.STRING)
	@Column(name = "status")
	private EnumDocStatus status;

	@Column(name = "issue_date", nullable = true)
	private LocalDate issueDate;

	@Column(name = "expiry_date", nullable = true)
	private LocalDate expiryDate;

	// Document path in the CDN system
	@Column(name = "url", nullable = true, length = 500)
	private String url;

	public BaseDocument() {
		super();
	}

	public BaseDocument(DocumentDef documentDef, String fileName, String fileType, String fileExt,
			EnumDocSource docSource, EnumDocStatus status, String url) {
		super();
		this.documentDef = documentDef;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileExt = fileExt;
		this.docSource = docSource;
		this.status = status;
		this.url = url;
	}

	public DocumentDef getDocumentDef() {
		return documentDef;
	}

	public void setDocumentDef(DocumentDef documentDef) {
		this.documentDef = documentDef;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public EnumDocSource getDocSource() {
		return docSource;
	}

	public void setDocSource(EnumDocSource docSource) {
		this.docSource = docSource;
	}

	public EnumDocStatus getStatus() {
		return status;
	}

	public void setStatus(EnumDocStatus status) {
		this.status = status;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
}
