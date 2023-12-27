package com.lucid.recruit.referencedata.constants;

import com.lucid.recruit.referencedata.dto.AddressLineDTO;

import java.util.ArrayList;
import java.util.List;

public enum EnumAddressLines {

    address1(1),
    address2(2),
    address3(3),
    address4(4),
    address5(5),
    state(6),
    city(7),
    postalCode(8),
    postOfficeBox(9);

    private final int defaultDisplayOrder;
    EnumAddressLines(int defaultDisplayOrder) {
        this.defaultDisplayOrder= defaultDisplayOrder;
    }

    public int getOrder() {
        return defaultDisplayOrder;
    }

    public static List<AddressLineDTO> getAllAddressLineDTO(){
        List<AddressLineDTO> addressLines = new ArrayList<>();
        addressLines.add(getAddressLineDTO(address1));
        addressLines.add(getAddressLineDTO(address2));
        addressLines.add(getAddressLineDTO(address3));
        addressLines.add(getAddressLineDTO(address4));
        addressLines.add(getAddressLineDTO(address5));
        addressLines.add(getAddressLineDTO(state));
        addressLines.add(getAddressLineDTO(city));
        addressLines.add(getAddressLineDTO(postalCode));
        addressLines.add(getAddressLineDTO(postOfficeBox));
        return addressLines;
    }

    private static AddressLineDTO getAddressLineDTO(EnumAddressLines enumAddressLines){
        return new AddressLineDTO(enumAddressLines,false,enumAddressLines.getOrder());
    }

}
