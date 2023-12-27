package com.lucid.recruit.org.dto;

import java.util.List;

public class OrgCountsDTO {

    private long totalOrgs;

    private List<OrgStatusCountsDTO> organizationStatus;

    public long getTotalOrgs() {
        return totalOrgs;
    }

    public void setTotalOrgs(long totalOrgs) {
        this.totalOrgs = totalOrgs;
    }

    public List<OrgStatusCountsDTO> getOrganizationStatus() {
        return organizationStatus;
    }

    public void setOrganizationStatus(List<OrgStatusCountsDTO> organizationStatus) {
        this.organizationStatus = organizationStatus;
    }
}
