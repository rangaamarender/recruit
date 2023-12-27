/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.worker.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lucid.recruit.worker.dto.WorkerCountsByBillableDTO;
import com.lucid.recruit.worker.dto.WorkerTypeCountDTO;
import com.lucid.recruit.worker.entity.Worker;

/**
 * 
 * @author chandu
 *
 */
public interface WorkerRepository extends JpaRepository<Worker, String>, JpaSpecificationExecutor<Worker> {

	Page<Worker> findAll(Specification<Worker> specification, Pageable pageable);

	Page<Worker> findAll(Pageable pageable);

	@Query("select w from Worker w join w.personLegal pl join pl.personContactDetails pcd where pcd.emailId =:emailID and pcd.endDate = null and pcd.contactType = 'PRIMARY'")
	Worker findByEmailID(@Param("emailID") String emailID);

	// Custom query to get counts of records by status
	@Query("SELECT new com.lucid.recruit.worker.dto.WorkerTypeCountDTO(w.workerType.workerTypeCode, COUNT(w)) FROM Worker w  GROUP BY w.workerType.workerTypeCode")
	List<WorkerTypeCountDTO> countByWorkerType();

	@Query("select new com.lucid.recruit.worker.dto.WorkerCountsByBillableDTO(j.billable, count(w)) from Worker w "
			+ "JOIN WorkAssignment wa "
			+ "JOIN Job j "
			+ "where wa.startDate<=:effectiveDate and (wa.endDate is null or wa.endDate>=:effectiveDate) group by j.billable")
	List<WorkerCountsByBillableDTO> countByBillable(LocalDate effectiveDate);

}
