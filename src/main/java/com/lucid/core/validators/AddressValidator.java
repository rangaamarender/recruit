package com.lucid.core.validators;

import com.lucid.core.entity.BaseAddress;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.referencedata.entity.AddressFormat;
import com.lucid.recruit.referencedata.repo.AddressFormatRepo;
import com.lucid.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressValidator {

    @Autowired
    private AddressFormatRepo addressFormatRepo;

    public void validateAddress(BaseAddress address){
        List<AddressFormat> addressFormats = addressFormatRepo.findByCountryCode(address.getCountry().getCountryCode());
        if(addressFormats != null && !addressFormats.isEmpty()){
            addressFormats.forEach(addressFormat -> {
                switch (addressFormat.getAddressLine()){
                    case address1 -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getAddress1())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0002,"address","address1",address.getAddress1(),"Address1 mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    case address2 -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getAddress2())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0003,"address","address2",address.getAddress2(),"Address2 mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    case address3 -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getAddress3())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0004,"address","address3",address.getAddress3(),"Address3 mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    case address4 -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getAddress4())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0005,"address","address4",address.getAddress4(),"Address4 mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    case address5 -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getAddress5())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0006,"address","address5",address.getAddress5(),"Address5 mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    case city -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getCity())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0007,"address","city",address.getCity(),"City mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    case state -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getState())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0008,"address","state",address.getState(),"State mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    case postalCode -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getPostalCode())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0009,"address","postalCode",address.getPostalCode(),"PostalCode mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    case postOfficeBox -> {
                        if(addressFormat.getMandatory() && Strings.isNullOrEmpty(address.getPostOfficeBox())){
                            throw new InvalidDataException(ErrorCodes.ADDR_V_0010,"address","postOfficeBox",address.getPostOfficeBox(),"PostOfficeBox mandatory for "+address.getCountry().getCountryCode());
                        }
                    }
                    default -> {

                    }
                }
            });
        }
        else{
            throw new InvalidDataException(ErrorCodes.ADDR_V_0001,"address","countryCode",address.getCountry().getCountryCode(),"Address format not defined with countryCode:"+address.getCountry().getCountryCode());
        }
    }
}
