package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.WorkOrderDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderDocRepo extends JpaRepository<WorkOrderDocument,String> {

}
