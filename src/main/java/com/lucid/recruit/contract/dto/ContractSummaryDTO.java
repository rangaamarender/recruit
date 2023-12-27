package com.lucid.recruit.contract.dto;

import com.lucid.recruit.org.dto.RelatedOrgDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ContractSummaryDTO implements Serializable {

    private String contractID;

    private String contractName;

    private RelatedOrgDTO relatedOrg;

    private List<WorkOrderSummaryDTO> workOrders;

    private LocalDate startDate;

    private LocalDate endDate;

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
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

    public List<WorkOrderSummaryDTO> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrderSummaryDTO> workOrders) {
        this.workOrders = workOrders;
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
}
