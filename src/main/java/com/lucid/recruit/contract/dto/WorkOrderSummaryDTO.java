package com.lucid.recruit.contract.dto;

import com.lucid.recruit.contract.constants.EnumContractWOStatus;
import com.lucid.recruit.org.dto.RelatedOrgDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class WorkOrderSummaryDTO implements Serializable {

    private String workOrderID;

    private String wbsCode;

    private RelatedOrgDTO client;

    private RelatedOrgDTO endClient;

    private LocalDate startDate;

    private LocalDate endDate;

    private EnumContractWOStatus status;

    private List<ChargeCodeResourceDTO> contractResources;

    public String getWorkOrderID() {
        return workOrderID;
    }

    public void setWorkOrderID(String workOrderID) {
        this.workOrderID = workOrderID;
    }

    public String getWbsCode() {
        return wbsCode;
    }

    public void setWbsCode(String wbsCode) {
        this.wbsCode = wbsCode;
    }

    public RelatedOrgDTO getClient() {
        return client;
    }

    public void setClient(RelatedOrgDTO client) {
        this.client = client;
    }

    public RelatedOrgDTO getEndClient() {
        return endClient;
    }

    public void setEndClient(RelatedOrgDTO endClient) {
        this.endClient = endClient;
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

    public List<ChargeCodeResourceDTO> getContractResources() {
        return contractResources;
    }

    public void setContractResources(List<ChargeCodeResourceDTO> contractResources) {
        this.contractResources = contractResources;
    }

    public EnumContractWOStatus getStatus() {
        return status;
    }

    public void setStatus(EnumContractWOStatus status) {
        this.status = status;
    }
}
