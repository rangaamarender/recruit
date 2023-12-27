package com.lucid.recruit.referencedata.repo;

import com.lucid.recruit.referencedata.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RefCountryRepo extends JpaRepository<Country,String> {

    List<Country> findByCountryCodeIn(List<String> countryCodes);
}
