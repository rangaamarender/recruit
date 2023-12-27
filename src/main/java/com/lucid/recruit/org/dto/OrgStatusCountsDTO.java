package com.lucid.recruit.org.dto;

import com.lucid.recruit.org.constants.OrgStatus;

public class OrgStatusCountsDTO {

    private OrgStatus orgStatus;
    private Long count;

    public OrgStatusCountsDTO() {
        super();
    }

    public OrgStatusCountsDTO(OrgStatus orgStatus, Long count) {
        this.orgStatus = orgStatus;
        this.count = count;
    }

    public OrgStatus getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(OrgStatus orgStatus) {
        this.orgStatus = orgStatus;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
