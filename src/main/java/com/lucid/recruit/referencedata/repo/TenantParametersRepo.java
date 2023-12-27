package com.lucid.recruit.referencedata.repo;
import com.lucid.recruit.referencedata.entity.TenantParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.Optional;

public interface TenantParametersRepo extends JpaRepository<TenantParameters,String> {

    @Query("SELECT t FROM TenantParameters t " +
            "WHERE t.name = :name " +
            "AND :date >= t.date")
    Optional<TenantParameters> findTenantParameterByNameAndDate(
            @Param("name") String name,
            @Param("date") LocalDate date
    );

    boolean existsByName(String name);
}


