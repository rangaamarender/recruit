package com.lucid.recruit.referencedata.repo;

import com.lucid.recruit.referencedata.dto.AddressFormatDTO;
import com.lucid.recruit.referencedata.entity.AddressFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface AddressFormatRepo extends JpaRepository<AddressFormat,String> {
    List<AddressFormat> findByCountryCode(String countryCode);

    @Query("SELECT DISTINCT af.countryCode  FROM AddressFormat af")
    List<String> getAddressFormatCountryCodes();

}
