package com.lucid.recruit.referencedata.dto;

import com.lucid.recruit.referencedata.constants.EnumAddressLines;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Comparator;

public class AddressLineDTO {
    @NotNull
    private EnumAddressLines addressLine;

    @NotNull
    private Boolean mandatory;

    @NotNull
    @Min(1)
    @Max(9)
    private Integer displayOrder;

    @NotBlank
    private String displayLabel;

    public AddressLineDTO() {
    }

    public AddressLineDTO(EnumAddressLines addressLine, Boolean mandatory, Integer displayOrder, String displayLabel) {
        this.addressLine = addressLine;
        this.mandatory = mandatory;
        this.displayOrder = displayOrder;
        this.displayLabel = displayLabel;
    }

    public AddressLineDTO(EnumAddressLines addressLine, Boolean mandatory, Integer displayOrder) {
        this.addressLine = addressLine;
        this.mandatory = mandatory;
        this.displayOrder = displayOrder;
    }

    public EnumAddressLines getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(EnumAddressLines addressLine) {
        this.addressLine = addressLine;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public static Comparator<AddressLineDTO> displayOrderComparator(){
        return Comparator.comparing(AddressLineDTO::getDisplayOrder);
    }

}
