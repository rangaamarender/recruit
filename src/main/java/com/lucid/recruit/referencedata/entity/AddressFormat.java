package com.lucid.recruit.referencedata.entity;

import com.lucid.recruit.referencedata.constants.EnumAddressLines;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name =AddressFormat.TABLE_NAME )
@IdClass(AddressFormatId.class)
public class AddressFormat {

    public static final String TABLE_NAME = "address_format";

    @Id
    @Column(name = "country_code",nullable = false)
    private String countryCode;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "address_line",nullable = false,updatable = false)
    private EnumAddressLines addressLine;

    @Column(name = "mandatory_boo",nullable = false,updatable = false)
    private Boolean mandatory;

    @Column(name = "display_order",nullable = false)
    @Min(1)
    @Max(9)
    private Integer displayOrder;

    @Column(name = "display_label",nullable = false)
    private String displayLabel;

    public AddressFormat() {
        super();
    }

    public AddressFormat(String countryCode, EnumAddressLines addressLine, Boolean mandatory, Integer displayOrder, String displayLabel) {
        this.countryCode = countryCode;
        this.addressLine = addressLine;
        this.mandatory = mandatory;
        this.displayOrder = displayOrder;
        this.displayLabel = displayLabel;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
}
