package com.lucid.recruit.attr.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class WorkerAttrDefListValuesDTO {
    @Nullable
    private String attrListValueID;
    @NotNull
    private String value;
    @NotNull
    private Integer displayOrder;

    @Nullable
    public String getAttrListValueID() {
        return attrListValueID;
    }

    public void setAttrListValueID(@Nullable String attrListValueID) {
        this.attrListValueID = attrListValueID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
