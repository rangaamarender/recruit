package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.ContractExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractExpenseRepo extends JpaRepository<ContractExpense,String> {
}
