package com.lucid.recruit.org.dto;

import com.lucid.recruit.docs.dto.BaseSignableDocumentDTO;
import com.lucid.recruit.org.entity.OrganizationDocument;

public class OrganizationSignableDocDTO extends BaseSignableDocumentDTO {

    private String signDocID;

    private OrganizationDocumentDTO organizationDocument;

    public String getSignDocID() {
        return signDocID;
    }

    public void setSignDocID(String signDocID) {
        this.signDocID = signDocID;
    }

    public OrganizationDocumentDTO getOrganizationDocument() {
        return organizationDocument;
    }

    public void setOrganizationDocument(OrganizationDocumentDTO organizationDocument) {
        this.organizationDocument = organizationDocument;
    }
}
