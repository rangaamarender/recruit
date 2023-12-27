package com.lucid.recruit.contract.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lucid.recruit.contract.entity.ChargeCodeResource;

public interface ChargeCodeResourceRepo extends JpaRepository<ChargeCodeResource, String> {

	@Query("select cr from ChargeCodeResource cr where cr.woChrageCode.chargeCodeId=:ccodeid "
			+ "and cr.worker.workerID=:wid and cr.startDate<=:itemdate and ((cr.endDate is null) or (cr.endDate>=:itemdate))")
	List<ChargeCodeResource> findByWIDAndChargeCode(@Param("ccodeid") String chrgCodeId,
														@Param("wid") String workerId, @Param("itemdate") LocalDate itemdate);

}
