package com.lucid.recruit.timecard.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lucid.recruit.timecard.entity.TimeCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeCardRepo extends JpaRepository<TimeCard, String> {

	@Query("select tc from TimeCard tc where tc.timeCardStatus != 'REJECTED' and tc.workOrder.workOrderID =:woid and tc.worker.workerID =:wid "
			+ "and (:tsstartdt between tc.startDate and tc.endDate "
			+ "or :tsenddt between tc.startDate and tc.endDate "
			+ "or :tsstartdt < tc.startDate and :tsenddt > tc.endDate "
			+ "or :tsstartdt > tc.startDate and :tsenddt < tc.endDate)")
	List<TimeCard> findOverlapWoDates(@Param("wid") String workerId, @Param("woid") String workOrderID,
			@Param("tsstartdt") java.time.LocalDate tsStartDt, @Param("tsenddt") LocalDate tsEndDate);

	@Query("select tc from TimeCard tc where tc.workOrder.workOrderID =:woid and tc.worker.workerID =:wid "
			+ "and tc.endDate = (select max(tc1.endDate) from TimeCard tc1 where tc1.workOrder.workOrderID =:woid and tc1.worker.workerID =:wid)")
	Optional<TimeCard> getLatestTimeCard(@Param("wid") String workerId, @Param("woid") String workOrderID);

    Page<TimeCard> findAll(Specification<TimeCard> specification, Pageable pageable);
}
