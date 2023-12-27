package com.lucid.recruit.referencedata.entity;

import com.lucid.recruit.referencedata.constants.EnumAddressLines;

import java.io.Serializable;
import java.util.Objects;

public class AddressFormatId implements Serializable {

    private String countryCode;
    private EnumAddressLines addressLine;

    // Constructors, getters, and setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressFormatId that = (AddressFormatId) o;
        return Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(addressLine, that.addressLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, addressLine);
    }

}
