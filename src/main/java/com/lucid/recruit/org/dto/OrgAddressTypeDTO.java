package com.lucid.recruit.org.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class OrgAddressTypeDTO {
    @NotNull
    private String addressType;
    @Nullable
    private String displayName;

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }
}
