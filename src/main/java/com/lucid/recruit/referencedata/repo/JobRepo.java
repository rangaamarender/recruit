package com.lucid.recruit.referencedata.repo;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lucid.recruit.referencedata.entity.Job;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 
 * @author chandu
 *
 */
public interface JobRepo extends JpaRepository<Job, String> {
    @Query("SELECT DISTINCT jb FROM Job jb WHERE (:deptID IS NULL OR jb.department.deptID =:deptID) AND (:billable IS NULL OR jb.billable =:billable) order by jb.department.deptID")
    List<Job> findAll(@Parameter(name = "deptID")String deptID,@Parameter(name = "billable")Boolean billable);

    @Query("SELECT jb FROM Job jb WHERE jb.jobName=:jobName AND jb.department.deptID=:deptID")
    Job findByJobNameAndDept(@Parameter(name = "jobName")String jobName,@Parameter(name = "deptID") String deptID);
}
