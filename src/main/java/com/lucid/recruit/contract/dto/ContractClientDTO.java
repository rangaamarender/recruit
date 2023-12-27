package com.lucid.recruit.contract.dto;

import com.lucid.recruit.org.dto.OrganizationDTO;
import com.lucid.recruit.org.dto.RelatedOrgDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class ContractClientDTO {

    @Nullable
    private String contractClientID;

    // Client organization ID
    @NotNull
    private RelatedOrgDTO client;

    @Nullable
    private boolean endClientBoo;

    @NotNull
    private int clientSequence;

    @Nullable
    public String getContractClientID() {
        return contractClientID;
    }

    public void setContractClientID(@Nullable String contractClientID) {
        this.contractClientID = contractClientID;
    }

    public RelatedOrgDTO getClient() {
        return client;
    }

    public void setClient(RelatedOrgDTO client) {
        this.client = client;
    }

    public int getClientSequence() {
        return clientSequence;
    }

    public void setClientSequence(int clientSequence) {
        this.clientSequence = clientSequence;
    }
}
