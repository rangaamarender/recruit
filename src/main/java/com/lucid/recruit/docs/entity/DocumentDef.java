
package com.lucid.recruit.docs.entity;

import java.util.List;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.docs.constants.EnumDocDefStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = DocumentDef.TABLE_NAME)
public class DocumentDef extends AuditableEntity {
	private static final long serialVersionUID = 2398403819851179062L;

	public static final String TABLE_NAME = "d_document_def";
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "doc_def_id", nullable = false, length = 75)
	private String documentDefID;

	// Standard name given to the document name by system
	@Column(name = "doc_name", nullable = false, length = 50,updatable = false)
	private String documentName;

	// Standard name given to the document name by system
	@Column(name = "doc_display_name", nullable = false, length = 50)
	private String docDisplayName;

	@Column(name = "boo_downloadable",nullable = false)
	private Boolean downloadable;

	@Column(name = "boo_monitorable",nullable = false)
	private Boolean monitorable;

	@Column(name = "boo_secure",nullable = false)
	private Boolean secure;

	// Document is active/inactive/discarded in the system
	@Enumerated(value = EnumType.STRING)
	@Column(name = "status", nullable = false)
	private EnumDocDefStatus status;

	@Column(name = "expiry_ind", nullable = false,updatable = false)
	private boolean expiryInd;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documentDef")
	@OrderBy(value = "displayOrder desc ")
	private List<DocAttributeDef> docAttrDef;

	public DocumentDef() {
		super();
	}

	public String getDocumentDefID() {
		return documentDefID;
	}

	public void setDocumentDefID(String documentDefID) {
		this.documentDefID = documentDefID;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocDisplayName() {
		return docDisplayName;
	}

	public void setDocDisplayName(String docDisplayName) {
		this.docDisplayName = docDisplayName;
	}

	public EnumDocDefStatus getStatus() {
		return status;
	}

	public void setStatus(EnumDocDefStatus status) {
		this.status = status;
	}

	public List<DocAttributeDef> getDocAttrDef() {
		return docAttrDef;
	}

	public void setDocAttrDef(List<DocAttributeDef> docAttrDef) {
		this.docAttrDef = docAttrDef;
	}

	public Boolean getDownloadable() {
		return downloadable;
	}

	public void setDownloadable(Boolean downloadable) {
		this.downloadable = downloadable;
	}

	public Boolean getMonitorable() {
		return monitorable;
	}

	public void setMonitorable(Boolean monitorable) {
		this.monitorable = monitorable;
	}

	public Boolean getSecure() {
		return secure;
	}

	public void setSecure(Boolean secure) {
		this.secure = secure;
	}

	public boolean isExpiryInd() {
		return expiryInd;
	}

	public void setExpiryInd(boolean expiryInd) {
		this.expiryInd = expiryInd;
	}
}
