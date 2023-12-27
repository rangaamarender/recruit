package com.lucid.recruit.docs.dto;

import com.lucid.core.dto.BasePhoneNbrDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class BaseSignableDocumentDTO {

    @NotBlank
    private String givenName;

    private String middleName;

    @NotBlank
    private String familyName;

    @NotBlank
    private String emailId;

    @Valid
    private BasePhoneNbrDTO phoneNumber;

    // document comments if any
    private String comments;

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public BasePhoneNbrDTO getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(BasePhoneNbrDTO phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
