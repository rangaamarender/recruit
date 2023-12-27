package com.lucid.recruit.referencedata.repo;

import com.lucid.recruit.referencedata.entity.WorkerInActiveStatusCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkerInActiveStatusCodesRepo extends JpaRepository<WorkerInActiveStatusCodes, String > {

}
