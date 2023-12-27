package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RelatedChargeCodeDTO {

    @NotNull
    private String chargeCodeId;

    @Nullable
    private String chargeCodeName;

    @Nullable
    private RelatedWorkLocationDTO workLocation;

    @Nullable
    private RelatedWorkOrderDTO workorder;

    @Nullable
    private RelatedContractDTO contract;

    public String getChargeCodeId() {return chargeCodeId;}

    public void setChargeCodeId(String chargeCodeId) {this.chargeCodeId = chargeCodeId;}

    @Nullable
    public String getChargeCodeName() {return chargeCodeName;}

    public void setChargeCodeName(@Nullable String chargeCodeName) {this.chargeCodeName = chargeCodeName;}

    @Nullable
    public RelatedWorkLocationDTO getWorkLocation() {return workLocation;}

    public void setWorkLocation(@Nullable RelatedWorkLocationDTO workLocation) {this.workLocation = workLocation;}

    @Nullable
    public RelatedWorkOrderDTO getWorkorder() {return workorder;}

    public void setWorkorder(@Nullable RelatedWorkOrderDTO workorder) {this.workorder = workorder;}

    @Nullable
    public RelatedContractDTO getContract() {return contract;}

    public void setContract(@Nullable RelatedContractDTO contract) {this.contract = contract;}
}
