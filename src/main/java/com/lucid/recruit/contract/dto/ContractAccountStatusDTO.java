package com.lucid.recruit.contract.dto;

import com.lucid.recruit.contract.constants.EnumContractAccountStatus;
import jakarta.annotation.Nullable;


import java.time.LocalDate;

public class ContractAccountStatusDTO {
    @Nullable
    private String accStatusId;

    @Nullable
    private EnumContractAccountStatus status;

    @Nullable
    private LocalDate effectiveDate;

    public String getAccStatusId() {
        return accStatusId;
    }

    public void setAccStatusId(String accStatusId) {
        this.accStatusId = accStatusId;
    }

    public EnumContractAccountStatus getStatus() {
        return status;
    }

    public void setStatus(EnumContractAccountStatus status) {
        this.status = status;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

}
