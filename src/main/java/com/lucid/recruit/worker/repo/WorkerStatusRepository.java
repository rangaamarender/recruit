/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.worker.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.lucid.recruit.worker.entity.WorkerStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lucid.recruit.worker.dto.WorkerStatusCountsDTO;
import com.lucid.recruit.worker.entity.WorkerStatus;
import org.springframework.data.repository.query.Param;

/**
 * 
 * @author chandu
 *
 */
public interface WorkerStatusRepository extends JpaRepository<WorkerStatus, String> {
	@Query("select new com.lucid.recruit.worker.dto.WorkerStatusCountsDTO(ws.status, count(ws)) from WorkerStatus ws where ws.effectiveDate= "
			+ "(select max(inws.effectiveDate) from WorkerStatus inws where ws.worker=inws.worker "
			+ "and inws.effectiveDate<=:effectiveDate) group by ws.status")
	List<WorkerStatusCountsDTO> countByWorkerStatus(LocalDate effectiveDate);

	@Query("SELECT min(ws.effectiveDate) FROM WorkerStatus ws WHERE ws.worker.workerID=:workerID AND ws.status=:workerStatusCode")
    Optional<LocalDate> getMinDateByStatus(@Param("workerID") String workerID, @Param("workerStatusCode") WorkerStatusCode workerStatusCode);

	@Query("SELECT ws FROM WorkerStatus ws WHERE ws.worker.workerID=:workerID AND ws.effectiveDate >:effectiveDate")
	List<WorkerStatus> getWorkerStatusAfter(@Param("workerID") String workerID, @Param("effectiveDate") LocalDate effectiveDate);

	@Query("select ws from WorkerStatus ws where ws.effectiveDate= "
			+ "(select max(inws.effectiveDate) from WorkerStatus inws where ws.worker=inws.worker "
			+ "and inws.effectiveDate<=:effectiveDate) and ws.worker.workerID=:workerID")
	List<WorkerStatus> getWorkerStatusByDate(@Param("effectiveDate") LocalDate date,@Param("workerID") String workerID);
}
