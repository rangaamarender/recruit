package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.ContractAccountStatus;
import com.lucid.recruit.org.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ContractAccountStatusRepo extends JpaRepository<ContractAccountStatus,String> {

    @Query("SELECT cas FROM ContractAccountStatus cas WHERE cas.contractAccount.contractAccountId=:contractAccountId AND cas.effectiveDate= "
            +"(select max(icas.effectiveDate) from ContractAccountStatus icas where cas.contractAccount.contractAccountId=icas.contractAccount.contractAccountId AND icas.effectiveDate<=:effectiveDate)")
    ContractAccountStatus getCurrentAccountStatus(@Param("contractAccountId") String contractAccountId, @Param("effectiveDate") LocalDate effectiveDate);

}
