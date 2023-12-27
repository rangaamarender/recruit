package com.lucid.recruit.org.entity;

import com.lucid.recruit.docs.entity.BaseDocAttributes;
import jakarta.persistence.*;

@Entity
@Table(name = OrgDocAttributes.TABLE_NAME)
public class OrgDocAttributes extends BaseDocAttributes {


    public static final String TABLE_NAME = "o_org_doc_attributes";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "org_doc_attr_id", nullable = false, length = 75)
    private String orgDocAttrID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_doc_id", nullable = false, updatable = false)
    private OrganizationDocument organizationDocument;


    public String getOrgDocAttrID() {
        return orgDocAttrID;
    }

    public void setOrgDocAttrID(String orgDocAttrID) {
        this.orgDocAttrID = orgDocAttrID;
    }

    public OrganizationDocument getOrganizationDocument() {
        return organizationDocument;
    }

    public void setOrganizationDocument(OrganizationDocument organizationDocument) {
        this.organizationDocument = organizationDocument;
    }
}
