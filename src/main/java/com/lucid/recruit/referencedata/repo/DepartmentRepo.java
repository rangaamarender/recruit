package com.lucid.recruit.referencedata.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucid.recruit.referencedata.entity.Department;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * @author chandu
 */
public interface DepartmentRepo extends JpaRepository<Department, String> {
    Department findByDeptName(String deptName);

}
