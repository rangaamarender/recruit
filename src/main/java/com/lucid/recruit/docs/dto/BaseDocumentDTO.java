package com.lucid.recruit.docs.dto;

import java.time.LocalDate;

import com.lucid.recruit.docs.constants.EnumDocSource;
import com.lucid.recruit.docs.constants.EnumDocStatus;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public abstract class BaseDocumentDTO {

	@NotNull
	private DocumentDefDTO documentDef;
	@Nullable
	private String fileName;
	@Nullable
	private String fileType;
	@Nullable
	private String fileExt;
	@Nullable
	private EnumDocSource docSource;
	@Nullable
	private EnumDocStatus status;
	@Nullable
	private LocalDate issueDate;
	@Nullable
	private LocalDate expiryDate;
	@Nullable
	private String url;

	public DocumentDefDTO getDocumentDef() {
		return documentDef;
	}

	public void setDocumentDef(DocumentDefDTO documentDef) {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
