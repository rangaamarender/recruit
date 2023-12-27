package com.lucid.recruit.org.dto;

import com.lucid.core.dto.BaseAddressDTO;

import java.time.LocalDate;

public class OrgAddressDTO extends BaseAddressDTO {

    private String orgAddressID;

    private OrgAddressTypeDTO orgAddressType;

    private LocalDate startDate;

    private LocalDate endDate;

    public String getOrgAddressID() {
        return orgAddressID;
    }

    public void setOrgAddressID(String orgAddressID) {
        this.orgAddressID = orgAddressID;
    }

    public OrgAddressTypeDTO getOrgAddressType() {
        return orgAddressType;
    }

    public void setOrgAddressType(OrgAddressTypeDTO orgAddressType) {
        this.orgAddressType = orgAddressType;
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
