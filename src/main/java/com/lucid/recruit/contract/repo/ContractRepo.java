package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContractRepo extends JpaRepository<Contract, String>, JpaSpecificationExecutor<Contract> {
	Page<Contract> findAll(Specification specification,Pageable pageable);

    Contract findByContractName(String contractName);
}
