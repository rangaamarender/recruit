package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.WorkOrderChargeCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractChargeCodeRepo extends JpaRepository<WorkOrderChargeCode, String> {
}
