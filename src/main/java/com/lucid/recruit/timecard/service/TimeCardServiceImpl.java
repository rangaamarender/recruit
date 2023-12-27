package com.lucid.recruit.timecard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lucid.core.base.BaseServiceImpl;
import com.lucid.core.embeddable.Amount;
import com.lucid.recruit.contract.dto.RelatedChargeCodeTasksDTO;
import com.lucid.recruit.contract.dto.RelatedContractDTO;
import com.lucid.recruit.contract.dto.RelatedWorkOrderDTO;
import com.lucid.recruit.contract.dto.RelatedWorkOrderExpenseCodesDTO;
import com.lucid.recruit.contract.entity.ChargeCodeTasks;
import com.lucid.recruit.contract.entity.Contract;
import com.lucid.recruit.contract.entity.ContractWorkOrder;
import com.lucid.recruit.contract.entity.WorkOrderExpenseCodes;
import com.lucid.recruit.contract.entity.WorkOrderResource;
import com.lucid.recruit.person.dto.RelatedPersonLegalDTO;
import com.lucid.recruit.referencedata.dto.WorkerTypeDTO;
import com.lucid.recruit.timecard.constants.EnumTimeCardStatus;
import com.lucid.recruit.timecard.controller.TimeCardController;
import com.lucid.recruit.timecard.dto.TimeCardDTO;
import com.lucid.recruit.timecard.dto.TimeCardExpenseDTO;
import com.lucid.recruit.timecard.dto.TimeCardItemDTO;
import com.lucid.recruit.timecard.dto.TimeCardSummaryDTO;
import com.lucid.recruit.timecard.entity.TimeCard;
import com.lucid.recruit.timecard.entity.TimeCardExpense;
import com.lucid.recruit.timecard.entity.TimeCardItem;
import com.lucid.recruit.timecard.repo.TimeCardExpenseRepo;
import com.lucid.recruit.timecard.repo.TimeCardItemRepo;
import com.lucid.recruit.timecard.repo.TimeCardRepo;
import com.lucid.recruit.timecard.repo.TimeCardSpecification;
import com.lucid.recruit.timecard.validations.TimeCardValidator;
import com.lucid.recruit.worker.dto.RelatedWorkerDTO;
import com.lucid.recruit.worker.entity.Worker;
import com.lucid.util.ServiceUtils;

@Component(TimeCardServiceImpl.SERVICE_NAME)
public class TimeCardServiceImpl extends BaseServiceImpl implements TimeCardService {

	public static final String SERVICE_NAME = "timecardService";
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeCardController.class);
	@Autowired
	private TimeCardValidator timeCardValidator;
	@Autowired
	private TimeCardRepo timeCardRepo;
	@Autowired
	private TimeCardItemRepo timeCardItemRepo;
	@Autowired
	private TimeCardExpenseRepo timeCardExpRepo;
	@Autowired
	private TimeCardSpecification timeCardSpecification;

	public TimeCardServiceImpl() {
		super();
	}

	@Override
	@Transactional
	public TimeCardDTO createTimeCard(TimeCardDTO timeCardDTO) {
		LOGGER.debug("Create timeCard serviceImpl");
		Worker worker;
		Contract contract;
		ContractWorkOrder wrkOrder;

		// 1. Validate the input worker id by getting the worker from REPO
		LOGGER.debug("Create TC get the woker start");
		worker = timeCardValidator.getWorker(timeCardDTO.getWorker().getWorkerID());
		LOGGER.debug("Create TC get the woker end");

		// 2. Validate the input contract id by getting the worker from REPO
		LOGGER.debug("Create TC get the contract start");
		contract = timeCardValidator.getContract(timeCardDTO.getContract().getContractID());
		LOGGER.debug("Create TC get the contract end");
		
		
		// 4. Get the work order by contract, dates and active status
		LOGGER.debug("Create TC get the contract work order start");
		wrkOrder = timeCardValidator.getWorkOrder(contract, timeCardDTO.getContract().getWorkOrder().getWorkOrderID(),
				timeCardDTO.getContract().getWorkOrder().getStartDate(),
				timeCardDTO.getContract().getWorkOrder().getEndDate());
		LOGGER.debug("Create TC get the contract work order end");
		
		// 5. Validate worker associated with worker for given period
		LOGGER.debug("Create TC get the work order resource start");
		WorkOrderResource workOrderRes = timeCardValidator.validateWrkOrderRes(
				timeCardDTO.getContract().getWorkOrder().getStartDate(),
				timeCardDTO.getContract().getWorkOrder().getEndDate(), worker, wrkOrder);
		LOGGER.debug("Create TC get the work order resource end");
		
		// 6.Validate if any overlap TS
		LOGGER.debug("Create TC overlap time period validations start");
		timeCardValidator.validateTimeCardOverlap(timeCardDTO.getContract().getWorkOrder().getStartDate(),
				timeCardDTO.getContract().getWorkOrder().getEndDate(), worker, wrkOrder);
		LOGGER.debug("Create TC overlap time period validations end");

		// 7. validate missing time periods
		LOGGER.debug("Create TC missing time period validations start");
		timeCardValidator.validateMissingTime(timeCardDTO.getContract().getWorkOrder().getStartDate(),
				timeCardDTO.getContract().getWorkOrder().getEndDate(), worker, wrkOrder, workOrderRes);
		LOGGER.debug("Create TC missing time period validations end");

		// 9. Validate if worker associated with given work order and charge code tasks
		// on the given dates
		LOGGER.debug("Create TC validate the charge code tasks start");
		HashMap<String, ChargeCodeTasks> chrgCodeMap = timeCardValidator.validateTimeCardItems(timeCardDTO);
		LOGGER.debug("Create TC validate the charge code tasks end");

		// 10.Save Time Card Entity
		LOGGER.debug("Saving Time Card Start");
		TimeCard timeCard = prepareTimeCard(timeCardDTO, 1, contract, worker, wrkOrder);
		TimeCard savedTC = timeCardRepo.save(timeCard);
		LOGGER.debug("Saving Time Card End");

		// 11. Save Time Card Items
		List<TimeCardItem> timeCardItems = prepareTimeCardItem(
				timeCardDTO.getContract().getWorkOrder().getTimeCardItems(), savedTC, chrgCodeMap);
		List<TimeCardItem> savedTCI = timeCardItemRepo.saveAll(timeCardItems);
		savedTC.setTimeCardItems(savedTCI);

		// 12.Validate expenses and save
		LOGGER.debug("Create TC validate expenses start");
		List<TimeCardExpenseDTO> timeCardExpenses = timeCardDTO.getContract().getWorkOrder().getTimeCardExpenses();
		List<TimeCardExpense> tcExpenseList = new ArrayList<>();
		if (timeCardExpenses != null && !timeCardExpenses.isEmpty()) {
			HashMap<String, WorkOrderExpenseCodes> expCodeMap = timeCardValidator.validateExpenses(timeCardExpenses,
					timeCardDTO.getContract().getWorkOrder().getStartDate(),
					timeCardDTO.getContract().getWorkOrder().getEndDate(), worker, wrkOrder);
			tcExpenseList = prepareTimeCardExpense(timeCardExpenses, timeCard, expCodeMap);
		}

		List<TimeCardExpense> savedExpList = timeCardExpRepo.saveAll(tcExpenseList);
		savedTC.setTimeCardExpenses(savedExpList);

		return buildTCResponseDTO(timeCard, worker, contract, wrkOrder);
	}

	private TimeCard prepareTimeCard(TimeCardDTO timeCardDTO, int version, Contract contract, Worker worker,
			ContractWorkOrder workOrder) {
		LOGGER.debug("Create Time Card Start");
		TimeCard timeCard = new TimeCard();
		timeCard.setStartDate(timeCardDTO.getContract().getWorkOrder().getStartDate());
		timeCard.setEndDate(timeCardDTO.getContract().getWorkOrder().getEndDate());
		timeCard.setTotalHours(timeCardDTO.getContract().getWorkOrder().getTotalHours());
		timeCard.setTimecardVersion(version);
		// set status
		timeCard.setTimeCardStatus(timeCardValidator.getCreateStatus(timeCardDTO.getStatus(), workOrder));
		timeCard.setContract(contract);
		timeCard.setWorker(worker);
		timeCard.setWorkOrder(workOrder);
		LOGGER.debug("Create Time Card End");
		return timeCard;
	}

	private List<TimeCardItem> prepareTimeCardItem(List<TimeCardItemDTO> timeCardItemDto, TimeCard timeCard,
			HashMap<String, ChargeCodeTasks> chrgCodeMap) {
		LOGGER.debug("Create Time Card Item Start");
		List<TimeCardItem> timeCardItem = new ArrayList<>();
		timeCardItemDto.forEach(tcidto -> {
			TimeCardItem tci = new TimeCardItem();
			tci.setTimeCard(timeCard);
			tci.setItemDate(tcidto.getItemDate());
			tci.setChargeCodeTask(chrgCodeMap.get(tcidto.getChargeCodeTask().getTaskId()));
			tci.setChargeCode(chrgCodeMap.get(tcidto.getChargeCodeTask().getTaskId()).getWoChrageCode());
			tci.setHours(tcidto.getHours());
			tci.setComment(tcidto.getComment());
			timeCardItem.add(tci);
		});
		LOGGER.debug("Create Time Card Item Ends");
		return timeCardItem;
	}

	private List<TimeCardExpense> prepareTimeCardExpense(List<TimeCardExpenseDTO> tcxpensesDto, TimeCard timeCard,
			HashMap<String, WorkOrderExpenseCodes> expCodeMap) {
		LOGGER.debug("Create Time Card Expense Start");
		List<TimeCardExpense> tcExpenseList = new ArrayList<TimeCardExpense>();
		tcxpensesDto.forEach(tciExpdto -> {
			TimeCardExpense tcExp = new TimeCardExpense();
			tcExp.setName(tciExpdto.getName());
			tcExp.setTimeCard(timeCard);
			tcExp.setExpenseDate(tciExpdto.getExpenseDate());
			tcExp.setExpenseCode(expCodeMap.get(tciExpdto.getExpenseCode().getExpenseCodeId()));
			tcExp.setAmount(
					new Amount(tciExpdto.getAmount().getAmountValue(), tciExpdto.getAmount().getAmountCurrency()));
			tcExpenseList.add(tcExp);
		});
		LOGGER.debug("Create Time Card Expense End");
		return tcExpenseList;
	}

	private TimeCardDTO buildTCResponseDTO(TimeCard timeCard, Worker worker, Contract contract,
			ContractWorkOrder workOrder) {
		LOGGER.debug("Create Time Card Response DTO start");
		TimeCardDTO timeCardDTO = new TimeCardDTO();
		timeCardDTO.setTimecardID(timeCard.getTimecardID());
		timeCardDTO.setStatus(timeCard.getTimeCardStatus());

		RelatedWorkerDTO workerDto = new RelatedWorkerDTO();
		workerDto.setWorkerID(worker.getWorkerID());
		workerDto.setWorkerType(new WorkerTypeDTO(worker.getWorkerType().getWorkerTypeCode(),
				worker.getWorkerType().getWorkerTypeName(), worker.getWorkerType().getWorkerTypeDesc()));

		workerDto.setPersonLegal(new RelatedPersonLegalDTO(worker.getPersonLegal().getGivenName(),
				worker.getPersonLegal().getMiddleName(), worker.getPersonLegal().getFamilyName()));
		timeCardDTO.setWorker(workerDto);

		RelatedContractDTO contractDto = new RelatedContractDTO();
		contractDto.setContractID(contract.getContractID());
		contractDto.setContractName(contract.getContractName());

		RelatedWorkOrderDTO workOrderDto = new RelatedWorkOrderDTO();
		workOrderDto.setWorkOrderID(workOrder.getWorkOrderID());
		workOrderDto.setWorkorderName(workOrder.getWorkOrderName());
		workOrderDto.setStartDate(timeCard.getStartDate());
		workOrderDto.setEndDate(timeCard.getEndDate());
		workOrderDto.setTotalHours(timeCard.getTotalHours());

		List<TimeCardItemDTO> tciDTOList = new ArrayList<>();
		timeCard.getTimeCardItems().stream().forEach(tci -> {
			TimeCardItemDTO tciDto = new TimeCardItemDTO();
			tciDto.setTimecardItemID(tci.getTimecardItemID());
			tciDto.setItemDate(tci.getItemDate());
			tciDto.setHours(tci.getHours());
			tciDto.setComment(tci.getComment());
			tciDto.setStatusComments(tci.getStatusComments());
			RelatedChargeCodeTasksDTO chrgTaskDTO = new RelatedChargeCodeTasksDTO();
			chrgTaskDTO.setTaskId(tci.getChargeCodeTask().getTaskId());
			chrgTaskDTO.setChargeCodeName(tci.getChargeCodeTask().getTaskName());
			chrgTaskDTO.setChargeCodeId(tci.getChargeCode().getChargeCodeId());
			chrgTaskDTO.setChargeCodeName(tci.getChargeCode().getChargeCodeName());
			tciDto.setChargeCodeTask(chrgTaskDTO);
			tciDTOList.add(tciDto);
		});
		workOrderDto.setTimeCardItems(tciDTOList);

		List<TimeCardExpenseDTO> tcExpDTOList = new ArrayList<>();
		if (timeCard.getTimeCardExpenses() != null) {
			timeCard.getTimeCardExpenses().stream().forEach(tcexp -> {
				TimeCardExpenseDTO tcExpDto = new TimeCardExpenseDTO();
				tcExpDto.setExpenseID(tcexp.getExpenseID());
				tcExpDto.setName(tcexp.getName());
				tcExpDto.setExpenseDate(tcexp.getExpenseDate());
				RelatedWorkOrderExpenseCodesDTO expCodeDto = new RelatedWorkOrderExpenseCodesDTO();
				expCodeDto.setExpenseCodeId(tcexp.getExpenseCode().getExpenseCodeId());
				expCodeDto.setExpenseCodeName(tcexp.getExpenseCode().getExpenseCodeName());
				tcExpDto.setExpenseCode(expCodeDto);
				tcExpDto.setAmount(tcexp.getAmount());
				tcExpDTOList.add(tcExpDto);
			});
		}

		workOrderDto.setTimeCardExpenses(tcExpDTOList);
		contractDto.setWorkOrder(workOrderDto);
		timeCardDTO.setContract(contractDto);
		LOGGER.debug("Create Time Card Response DTO end");
		return timeCardDTO;
	}


	@Override
	public TimeCardDTO retrieveTimeCardById(String timeCardID) {
		LOGGER.debug("Retrieve timeCard by ID");

		// 1. Fetching the TimeCard entity using the ID.
		LOGGER.debug("Fetching TimeCard Start");
		TimeCard timeCard = timeCardValidator.fetchTimeCard(timeCardID);
		LOGGER.debug("Fetching TimeCard End");

		// fetching worker, contact, Wo associated with the TimeCard.
		Worker worker = timeCard.getWorker();
		Contract contract = timeCard.getContract();
		ContractWorkOrder workOrder = timeCard.getWorkOrder();

		//  transferring this fetched data into DTO format.
		return buildTCResponseDTO(timeCard, worker, contract, workOrder);
	}

	@Override
	public Page<TimeCardSummaryDTO> retrieveAllTimeCards(int offset, int limit, String contractID, String workerID, String timecardID, String workOrderID, LocalDate startDate, LocalDate endDate, EnumTimeCardStatus status) {
		LOGGER.debug("Find all specification creation start");
		Specification<TimeCard> specification = timeCardSpecification.conditionalSearchForTimeCard(contractID, workerID, timecardID, workOrderID, startDate, endDate, status);
		LOGGER.debug("Find all specification creation done");
		LOGGER.debug("Creating pageable object");
		Pageable pageable = ServiceUtils.getPageableObject(offset, limit,null);
		LOGGER.debug("Creating pageable object done");

		Page<TimeCard> pageTimeCards;
		LOGGER.info("Finding timeCards");
		if (specification != null) {
			LOGGER.debug("Finding timecard with specifications and pageable");
			pageTimeCards = timeCardRepo.findAll(specification, pageable);
			LOGGER.debug("Finding timeCards with specifications and pageable done");
		} else {
			LOGGER.debug("Finding timeCards with pageable");
			pageTimeCards = timeCardRepo.findAll(pageable);
			LOGGER.debug("Finding timeCards with pageable done");
		}
		List<TimeCardSummaryDTO> timeCardSummaryDTOS = new ArrayList<>();
		if (pageTimeCards != null) {
			if (!pageTimeCards.getContent().isEmpty()) {
				LOGGER.info("preparing summary");
				LOGGER.debug("Preparing summary");
				for (TimeCard timeCard : pageTimeCards.getContent()) {
					timeCardSummaryDTOS.add(convertToSummaryDTO(timeCard));
				}
				LOGGER.debug("Preparing summary done");
			}
			LOGGER.info("returns timeCards summary");
			return new PageImpl<>(timeCardSummaryDTOS, pageable, pageTimeCards.getTotalElements());
		}
		// if pageWorkers is null then we send empty list
		else {
			LOGGER.info("returns timeCards empty summary");
			return new PageImpl<>(timeCardSummaryDTOS, pageable, 0);
		}
	}


	private TimeCardSummaryDTO convertToSummaryDTO(TimeCard timeCard) {
		LOGGER.debug("Converting TimeCard to TimeCardSummaryDTO");

		TimeCardSummaryDTO timeCardSummary = new TimeCardSummaryDTO();
		if (timeCard != null) {
			timeCardSummary.setTimeCardID(timeCard.getTimecardID());

			if (timeCard.getWorker() != null && timeCard.getWorker().getPersonLegal() != null) {
				LOGGER.debug("Mapping GivenName from Worker's PersonLegal to TimeCardSummaryDTO");
				timeCardSummary.setResources(timeCard.getWorker().getPersonLegal().getGivenName());
			}

			if (timeCard.getWorker() != null && timeCard.getWorker().getWorkerType() != null) {
				LOGGER.debug("Mapping WorkerTypeCode from Worker's WorkerType to TimeCardSummaryDTO");
				timeCardSummary.setRole(timeCard.getWorker().getWorkerType().getWorkerTypeCode().getCode());
			}

			if (timeCard.getContract() != null && timeCard.getContract()!= null && timeCard.getWorkOrder() != null && timeCard.getContract() != null) {

				LOGGER.debug("Mapping ContractID and ContractName to TimeCardSummaryDTO");
				timeCardSummary.setContractID(timeCard.getContract().getContractID());
				timeCardSummary.setContractTitle(timeCard.getContract().getContractName());
			}

			timeCardSummary.setStartDate(timeCard.getStartDate());
			timeCardSummary.setEndDate(timeCard.getEndDate());

			LOGGER.debug("Calculating and setting TotalHours to TimeCardSummaryDTO");
			timeCardSummary.setTotalHours(BigDecimal.valueOf(timeCard.getTotalHours()));

			if (timeCard.getTimeCardStatus() != null) {
				LOGGER.debug("Mapping TimeCardStatus to TimeCardSummaryDTO");
				timeCardSummary.setStatus(timeCard.getTimeCardStatus().toString());
			}
		}

		LOGGER.debug("Returning constructed TimeCardSummaryDTO");
		return timeCardSummary;
	}



}
