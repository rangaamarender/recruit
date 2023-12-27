package com.lucid.recruit.referencedata.service;

import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.referencedata.constants.EnumAddressLines;
import com.lucid.recruit.referencedata.dto.AddressFormatDTO;
import com.lucid.recruit.referencedata.dto.AddressLineDTO;
import com.lucid.recruit.referencedata.entity.AddressFormat;
import com.lucid.recruit.referencedata.entity.Country;
import com.lucid.recruit.referencedata.repo.AddressFormatRepo;
import com.lucid.recruit.referencedata.repo.RefCountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressFormatServiceImpl implements AddressFormatService{

    @Autowired
    private AddressFormatRepo addressFormatRepo;

    @Autowired
    private RefCountryRepo refCountryRepo;


    @Override
    public AddressFormatDTO createAddressFormat(AddressFormatDTO addressFormatDTO) {
        if(!refCountryRepo.existsById(addressFormatDTO.getCountryCode())){
            throw new InvalidDataException(ErrorCodes.ADD_FMT_V_0001,null,"countryCode",addressFormatDTO.getCountryCode(),"Country not configured with code:"+addressFormatDTO.getCountryCode());
        }
        validateAddressFormat(addressFormatDTO);
        List<AddressFormat> addressFormats = new ArrayList<>();
        for(AddressLineDTO addressLineDTO:addressFormatDTO.getAddressLines()){
            addressFormats.add(new AddressFormat(addressFormatDTO.getCountryCode(),addressLineDTO.getAddressLine(),addressLineDTO.getMandatory(),addressLineDTO.getDisplayOrder(),addressLineDTO.getDisplayLabel()));
        }
        addressFormatRepo.saveAll(addressFormats);
        return addressFormatDTO;
    }

    @Override
    public AddressFormatDTO updateAddressFormat(String countryCode, AddressFormatDTO addressFormatDTO) {

        // checking the country code exist
        if (!refCountryRepo.existsById(countryCode)) {
            throw new InvalidDataException(ErrorCodes.ADD_FMT_V_0001, null, "countryCode", countryCode, "Country not find with code:" + countryCode);
        }
        validateAddressFormat(addressFormatDTO);
        // if exists get address formate
        List<AddressFormat> exstAddressFormats = addressFormatRepo.findByCountryCode(countryCode);
        // Validate and update address formats
        for (AddressLineDTO addressLineDTO : addressFormatDTO.getAddressLines()) {
            AddressFormat existingAddressFormat = findAddressFormatByLine(exstAddressFormats, addressLineDTO.getAddressLine());
            if (existingAddressFormat != null) {
                // Updating displayOrder and displayLabel
                existingAddressFormat.setDisplayOrder(addressLineDTO.getDisplayOrder());
                existingAddressFormat.setDisplayLabel(addressLineDTO.getDisplayLabel());
            }
        }
        // Save the updated address
        addressFormatRepo.saveAll(exstAddressFormats);
        return addressFormatDTO;
    }

        private AddressFormat findAddressFormatByLine (List <AddressFormat> addressFormats, EnumAddressLines addressLine) {
            for (AddressFormat addressFormat : addressFormats) {
                if (addressFormat.getAddressLine().equals(addressLine)){
                    return addressFormat;
                }
            }
            return null;
        }

    @Override
    public AddressFormatDTO getAddressFormat(String countryCode) {
        if(!refCountryRepo.existsById(countryCode)){
            throw new InvalidDataException(ErrorCodes.ADD_FMT_V_0001,null,"countryCode",countryCode,"Country not configured with code:"+countryCode);
        }
        List<AddressFormat> addressFormats = addressFormatRepo.findByCountryCode(countryCode);
        if (addressFormats.isEmpty()) {
            throw new InvalidDataException(ErrorCodes.ADD_FMT_V_0004,null,"countryCode",countryCode,"Address format not defined with countryCode:"+countryCode);
        }
        List<AddressLineDTO> addressLines = new ArrayList<>();
        addressFormats.stream().forEach(addressFormat -> {
            addressLines.add(new AddressLineDTO(addressFormat.getAddressLine(),addressFormat.getMandatory(),addressFormat.getDisplayOrder(),addressFormat.getDisplayLabel()));
        });
        return new AddressFormatDTO(countryCode, addressLines);
    }

    @Override
    public List<Country> getAddrFormatCountrys() {
        return refCountryRepo.findByCountryCodeIn(addressFormatRepo.getAddressFormatCountryCodes());
    }

    private void validateAddressFormat(AddressFormatDTO addressFormat) {
        List<EnumAddressLines> addressLines = new ArrayList<>();
        int i =1;
        addressFormat.getAddressLines().sort(AddressLineDTO.displayOrderComparator());
        for (AddressLineDTO addressLineDTO : addressFormat.getAddressLines()) {
            if (addressLines.contains(addressLineDTO.getAddressLine())) {
                throw new InvalidDataException(ErrorCodes.ADD_FMT_V_0002, "addressFormat", "addressLines", addressLineDTO.getAddressLine().toString(), "Duplicate addressLine");
            } else {
                addressLines.add(addressLineDTO.getAddressLine());
            }
            if (addressLineDTO.getDisplayOrder() != i) {
                throw new InvalidDataException(ErrorCodes.ADD_FMT_V_0003, "addressFormat", "displayOrder", addressLineDTO.getDisplayOrder().toString(), "Invalid displayOrder");
            }
            i++;
        }
    }

}
