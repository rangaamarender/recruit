package com.lucid.recruit.referencedata.repo;

import com.lucid.recruit.referencedata.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<Country,String> {
}
