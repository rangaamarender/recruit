package com.lucid.recruit.contract.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lucid.recruit.contract.entity.WorkOrderResource;

public interface WorkOrderResourceRepo extends JpaRepository<WorkOrderResource, String> {
	@Query("select wrkordrres from WorkOrderResource wrkordrres "
			+ "where wrkordrres.workOrder.workOrderID=:wrkorderid and wrkordrres.worker.workerID=:workerid "
			+ "and wrkordrres.startDate<=:startdate "
			+ "and ((wrkordrres.endDate is null) or (wrkordrres.endDate>=:enddate))")
	Optional<WorkOrderResource> findByIdAndDates(@Param("wrkorderid") String wrkOrderId,
			@Param("workerid") String workerid, @Param("startdate") LocalDate startDate,
			@Param("enddate") LocalDate endDate);

	@Query("SELECT min(wor.startDate) FROM WorkOrderResource wor WHERE wor.worker.workerID=:workerID")
	Optional<LocalDate> getMinStartDate(@Param("workerID")String workerID);
}
