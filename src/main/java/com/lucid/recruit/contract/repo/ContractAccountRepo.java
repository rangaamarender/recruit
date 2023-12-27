package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.ContractAccount;
import com.lucid.recruit.contract.entity.ContractAccountStatus;
import com.lucid.recruit.worker.entity.WorkerStatusCode;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractAccountRepo extends JpaRepository<ContractAccount,String> {

    @Query("SELECT min(ca.startDate) FROM ContractAccount ca WHERE ca.contract.contractID=:contractId")
    LocalDate getMinContractAccDate(@Param("contractId") String contractId);
    @Query("SELECT ca FROM ContractAccount ca WHERE ca.contract.contractID=:contractID")
    List<ContractAccount> findByContractId(@Parameter(name = "contractID") String contractID);

}
