package com.lucid.recruit.org.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class OrgListDTO {
    @Nullable
    private String organizationID;
    @NotNull
    private String name;

    public OrgListDTO(@Nullable String organizationID, String name) {
        this.organizationID = organizationID;
        this.name = name;
    }

    @Nullable
    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(@Nullable String organizationID) {
        this.organizationID = organizationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
