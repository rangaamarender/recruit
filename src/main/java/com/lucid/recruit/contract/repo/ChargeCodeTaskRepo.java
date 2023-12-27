package com.lucid.recruit.contract.repo;

import com.lucid.recruit.contract.entity.ChargeCodeTasks;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChargeCodeTaskRepo extends JpaRepository<ChargeCodeTasks, String> {

	@Query("select ctask from ChargeCodeTasks ctask " + "where ctask.taskId=:tid and ctask.startDate<=:itemdate "
			+ "and ((ctask.endDate is null) or (ctask.endDate>=:itemdate))")
	Optional<ChargeCodeTasks> findByIdAndDates(@Param("tid") String taskId, @Param("itemdate") LocalDate itemdate);
}
