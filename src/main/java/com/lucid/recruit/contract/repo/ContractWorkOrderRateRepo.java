package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.ChargeCodeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractWorkOrderRateRepo extends JpaRepository<ChargeCodeRate,String>{

}