package com.lucid.recruit.referencedata.dto;

import com.lucid.core.dto.BaseDTO;
import com.lucid.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class AddressFormatDTO {
    @NotNull
    private String countryCode;

    @NotEmpty
    @Valid
    private List<AddressLineDTO> addressLines;

    public AddressFormatDTO() {
        super();
    }

    public AddressFormatDTO(String countryCode, List<AddressLineDTO> addressLines) {
        this.countryCode = countryCode;
        this.addressLines = addressLines;
    }
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<AddressLineDTO> getAddressLines() {
        return addressLines;
    }

    public void setAddressLines(List<AddressLineDTO> addressLines) {
        this.addressLines = addressLines;
    }


}
