package com.lucid.recruit.contract.dto;

import com.lucid.recruit.contract.constants.EnumContractStatus;
import com.lucid.recruit.org.dto.RelatedOrgDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;

public class ContractDTO {

    @Nullable
    private String contractID;

    // Name (title) of the MSA
    @NotBlank
    private String contractName;

    // organization id of the party B
    @NotNull
    @Valid
    private RelatedOrgDTO relatedOrg;

    // MSA start date
    @NotNull
    private LocalDate startDate;

    // MSA end date null represent active forever
    @Nullable
    private LocalDate endDate;

    // Billing Currency code (ISO 4217)
    @Nullable
    @Length(max=3)
    private String billingCurrCode;

    // MSA notes
    @Nullable
    private String notes;

    // contract status like Active,Terminated
    @Nullable
    private EnumContractStatus contractStatus;

    // MSA termination date
    @Nullable
    private LocalDate terminationDate;

    // termination code
    @Nullable
    private String terminationReasonCode;

    // termination reason text
    @Nullable
    private String terminationReasonNotes;

    @Valid
    @Nullable
    private List<ContractAccountDTO> contractAccounts;

    @Nullable
    public String getContractID() {
        return contractID;
    }

    public void setContractID(@Nullable String contractID) {
        this.contractID = contractID;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public RelatedOrgDTO getRelatedOrg() {
        return relatedOrg;
    }

    public void setRelatedOrg(RelatedOrgDTO relatedOrg) {
        this.relatedOrg = relatedOrg;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getBillingCurrCode() {
        return billingCurrCode;
    }

    public void setBillingCurrCode(String billingCurrCode) {
        this.billingCurrCode = billingCurrCode;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    @Nullable
    public EnumContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(@Nullable EnumContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    @Nullable
    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(@Nullable LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    @Nullable
    public String getTerminationReasonCode() {
        return terminationReasonCode;
    }

    public void setTerminationReasonCode(@Nullable String terminationReasonCode) {
        this.terminationReasonCode = terminationReasonCode;
    }

    @Nullable
    public String getTerminationReasonNotes() {
        return terminationReasonNotes;
    }

    public void setTerminationReasonNotes(@Nullable String terminationReasonNotes) {
        this.terminationReasonNotes = terminationReasonNotes;
    }

    @Nullable
    public List<ContractAccountDTO> getContractAccounts() {
        return contractAccounts;
    }

    public void setContractAccounts(@Nullable List<ContractAccountDTO> contractAccounts) {
        this.contractAccounts = contractAccounts;
    }
}
