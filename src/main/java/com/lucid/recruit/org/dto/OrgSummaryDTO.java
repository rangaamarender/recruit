package com.lucid.recruit.org.dto;

import com.lucid.core.dto.BaseDTO;
import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.org.entity.OrganizationStatus;
import com.lucid.recruit.person.dto.PersonLegalSummaryDTO;

import java.time.LocalDate;
import java.util.List;

public class OrgSummaryDTO{

    private String organizationID;
    private String name;
    private String authSignataryFn;
    private String authSignataryLn;
    private String authSignataryPhone;
    private String authSignataryEmail;
    private LocalDate createdOn;
    private OrganizationStatusDTO status;
    private OrgAddressDTO orgAddress;

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthSignataryFn() {
        return authSignataryFn;
    }

    public void setAuthSignataryFn(String authSignataryFn) {
        this.authSignataryFn = authSignataryFn;
    }

    public String getAuthSignataryLn() {
        return authSignataryLn;
    }

    public void setAuthSignataryLn(String authSignataryLn) {
        this.authSignataryLn = authSignataryLn;
    }

    public String getAuthSignataryPhone() {
        return authSignataryPhone;
    }

    public void setAuthSignataryPhone(String authSignataryPhone) {
        this.authSignataryPhone = authSignataryPhone;
    }

    public String getAuthSignataryEmail() {
        return authSignataryEmail;
    }

    public void setAuthSignataryEmail(String authSignataryEmail) {
        this.authSignataryEmail = authSignataryEmail;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public OrganizationStatusDTO getStatus() {
        return status;
    }

    public void setStatus(OrganizationStatusDTO status) {
        this.status = status;
    }

    public OrgAddressDTO getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(OrgAddressDTO orgAddress) {
        this.orgAddress = orgAddress;
    }
}
