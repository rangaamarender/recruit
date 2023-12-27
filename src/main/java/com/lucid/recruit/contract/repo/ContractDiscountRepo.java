package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.ContractDiscount;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

public interface ContractDiscountRepo extends JpaRepository<ContractDiscount,String> {

    @Query("SELECT cd FROM ContractDiscount cd WHERE cd.contractAccount.contractAccountId=:contractAccountId AND cd.discountId=:discountId")
    ContractDiscount findByAccIdAndDisId(@Parameter(name = "contractAccountId") String contractAccountId,@Parameter(name = "discountId") String discountId);
}
