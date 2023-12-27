package com.lucid.recruit.referencedata.service;

import com.lucid.recruit.referencedata.dto.AddressFormatDTO;
import com.lucid.recruit.referencedata.entity.AddressFormat;
import com.lucid.recruit.referencedata.entity.Country;

import javax.naming.RefAddr;
import java.util.List;

public interface AddressFormatService {
    AddressFormatDTO createAddressFormat(AddressFormatDTO addressFormatDTO);

    AddressFormatDTO updateAddressFormat(String countryCode,AddressFormatDTO addressFormatDTO);

    AddressFormatDTO getAddressFormat(String countryCode);

    List<Country> getAddrFormatCountrys();
}
