package com.lucid.recruit.org.dto;



import com.lucid.recruit.docs.dto.BaseDocumentDTO;
import io.micrometer.common.lang.Nullable;

import java.util.List;


public class OrganizationDocumentDTO extends BaseDocumentDTO {
    @Nullable
    private String organizationDocID;
    @Nullable
    private List<OrgDocAttributesDTO> orgDocAttributes;

    public OrganizationDocumentDTO() {
        super();
    }

    public String getOrganizationDocID() {
        return organizationDocID;
    }

    public void setOrganizationDocID(String organizationDocID) {
        this.organizationDocID = organizationDocID;
    }

    public List<OrgDocAttributesDTO> getOrgDocAttributes() {
        return orgDocAttributes;
    }

    public void setOrgDocAttributes( List<OrgDocAttributesDTO> orgDocAttributes) {
        this.orgDocAttributes = orgDocAttributes;
    }
}
