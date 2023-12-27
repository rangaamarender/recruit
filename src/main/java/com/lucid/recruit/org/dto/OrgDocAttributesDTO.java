package com.lucid.recruit.org.dto;

import com.lucid.recruit.docs.dto.BaseDocAttributesDTO;
import jakarta.annotation.Nullable;

public class OrgDocAttributesDTO extends BaseDocAttributesDTO {

    @Nullable
    private String orgDocAttrID;

    @Nullable
    public String getOrgDocAttrID() {
        return orgDocAttrID;
    }

    public void setOrgDocAttrID(@Nullable String orgDocAttrID) {
        this.orgDocAttrID = orgDocAttrID;
    }
}
