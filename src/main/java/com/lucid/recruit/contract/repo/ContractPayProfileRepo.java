package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.WorkOrderExpenseBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractPayProfileRepo extends JpaRepository<WorkOrderExpenseBudget,String> {
}
