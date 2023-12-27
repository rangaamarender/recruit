package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.ContractBillingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ContractBillingDetailsRepo extends JpaRepository<ContractBillingDetails,String> {
    @Query("SELECT min(cbd.startDate) FROM ContractBillingDetails cbd WHERE cbd.contractAccount.contractAccountId=:contractAccId")
    Optional<LocalDate> getMinDate(@Param("contractAccId") String contractAccId);
}
