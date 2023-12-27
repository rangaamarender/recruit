package com.lucid.recruit.org.dto;

import com.lucid.core.annotations.Phone;
import com.lucid.core.dto.BaseDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;


public class OrgCommunicationDTO {

    @Nullable
    private String orgCmncID;

    @Nullable
    private LocalDate startDate;

    @Nullable
    private LocalDate endDate;

    
    @NotBlank(message = "Admin First Name Not Blank")
    private String authSignataryFn;

    // Auth. signatory Last Name
    @NotBlank(message = "Admin Last Name Not Blank")
    private String authSignataryLn;

    // Auth. signatory Email
    @NotBlank(message = "authSignataryEmail Not Blank")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",message = "Invalid email")
    private String authSignataryEmail;

    //@NotBlank(message = "authSignataryPhone Not Blank")
    @Phone
    private String authSignataryPhone;

    public String getOrgCmncID() {
        return orgCmncID;
    }

    public void setOrgCmncID(String orgCmncID) {
        this.orgCmncID = orgCmncID;
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

    public String getAuthSignataryEmail() {
        return authSignataryEmail;
    }

    public void setAuthSignataryEmail(String authSignataryEmail) {
        this.authSignataryEmail = authSignataryEmail;
    }

    public String getAuthSignataryPhone() {
        return authSignataryPhone;
    }

    public void setAuthSignataryPhone(String authSignataryPhone) {
        this.authSignataryPhone = authSignataryPhone;
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
