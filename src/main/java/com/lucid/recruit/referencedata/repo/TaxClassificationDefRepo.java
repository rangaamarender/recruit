package com.lucid.recruit.referencedata.repo;

import com.lucid.recruit.referencedata.entity.TaxClassificationDef;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxClassificationDefRepo extends JpaRepository<TaxClassificationDef,String> {
    @Query("SELECT txdef FROM TaxClassificationDef txdef WHERE txdef.country.countryCode=:countryCode")
    List<TaxClassificationDef> fetchTaxClassByCountry(@Param("countryCode") String countryCode);

    @Query(value = "Select * from ref_taxclassfication where ref_taxclassfication_id=:taxClassificationID",nativeQuery = true)
    TaxClassificationDef getTaxClassificationById(@Param("taxClassificationID") String taxClassificationID);

    @Query("SELECT txdef FROM TaxClassificationDef txdef WHERE txdef.country.countryCode=:countryCode AND txdef.taxClassCode=:taxClassCode")
    TaxClassificationDef getByTaxClassCodeAndCountry(@Param("taxClassCode")String taxClassCode, @Param("countryCode")String countryCode);
}
