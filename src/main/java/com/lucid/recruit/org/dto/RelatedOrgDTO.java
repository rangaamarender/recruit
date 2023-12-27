package com.lucid.recruit.org.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RelatedOrgDTO {
    @NotNull
    private String organizationID;
    @Nullable
    private String name;

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
