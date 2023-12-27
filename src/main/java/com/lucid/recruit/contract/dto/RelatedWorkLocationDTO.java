package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RelatedWorkLocationDTO {
    @Nullable
    private String contractwlID;
    @Nullable
    private String workLocationName;


    @Nullable
    public String getContractwlID() {return contractwlID;}

    public void setContractwlID(@Nullable String contractwlID) {this.contractwlID = contractwlID;}

    @Nullable
    public String getWorkLocationName() {return workLocationName;}

    public void setWorkLocationName(@Nullable String workLocationName) {this.workLocationName = workLocationName;}
}
