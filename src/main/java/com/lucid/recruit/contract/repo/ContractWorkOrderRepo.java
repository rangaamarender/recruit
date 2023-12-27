package com.lucid.recruit.contract.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.lucid.recruit.contract.constants.EnumContractWOStatus;
import com.lucid.recruit.contract.entity.ContractWorkOrder;

public interface ContractWorkOrderRepo
		extends JpaRepository<ContractWorkOrder, String>, JpaSpecificationExecutor<ContractWorkOrder> {

	boolean existsByWbsCode(String wbsCode);

	@Query("SELECT wo FROM ContractWorkOrder wo WHERE wo.contractAccount.contractAccountId IN :woGroupIds AND wo.status=:enumContractWOStatus")
	List<ContractWorkOrder> findActiveWorkOrders(@PathVariable(name = "woGroupIds") List<String> woGroupIds,
			@PathVariable(name = "enumContractWOStatus") EnumContractWOStatus enumContractWOStatus);

	Page<ContractWorkOrder> findAll(Specification<ContractWorkOrder> specification, Pageable pageable);

	@Query("SELECT wo FROM ContractWorkOrder wo "			
			+ "JOIN Contract c on wo.contractAccount.contract.contractID=c.contractID "
			+ "WHERE wo.workOrderID=:woid and " + "wo.startDate<=:sdate "
			+ "and (wo.endDate is null or wo.endDate>=:edate) " + "and wo.status=:status "
			+ "and ((wo.terminationDate is null) or ((wo.terminationDate>=:sdate) and (wo.terminationDate>=:edate))) "
			+ "and c.contractID=:cid ")
	Optional<ContractWorkOrder> findByContractAndDates(@Param("cid") String contractID, @Param("woid") String workOrderId,
			@Param("sdate") LocalDate sdate, @Param("edate") LocalDate edate,
			@PathVariable(name = "status") EnumContractWOStatus status);

	@Query("SELECT min(cwo.startDate) FROM ContractWorkOrder cwo WHERE cwo.contractAccount.contractAccountId=:contractAccountId")
	LocalDate getMinWoDate(@Param("contractAccountId") String contractAccountId);

}
