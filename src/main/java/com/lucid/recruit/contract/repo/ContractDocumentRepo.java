package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.ContractDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractDocumentRepo extends JpaRepository<ContractDocument,String> {
}
