package com.lucid.recruit.contract.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lucid.recruit.contract.entity.WorkOrderExpenseCodes;

public interface WorkOrderExpenseCodeRepo extends JpaRepository<WorkOrderExpenseCodes, String> {

	@Query("select woexp from WorkOrderExpenseCodes woexp "
			+ "JOIN ChargeCodeResource ccr on woexp.woChrageCode.chargeCodeId=ccr.woChrageCode.chargeCodeId"
			+ " where woexp.expenseCodeId=:expid "
			+ "and ((woexp.startDate<=:expdate) and ((woexp.endDate is null) or (woexp.endDate>=:expdate))) "
			+ "and ccr.worker.workerID=:wid and ((ccr.startDate<=:expdate) and ((ccr.endDate is null) or (ccr.endDate>=:expdate)))")
	Optional<WorkOrderExpenseCodes> getExpCodeForTS(@Param("expid") String expenseId,
			@Param("expdate") LocalDate expDate, @Param("wid") String workerId);
}
