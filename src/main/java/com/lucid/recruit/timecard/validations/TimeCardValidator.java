package com.lucid.recruit.timecard.validations;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.contract.constants.EnumContractWOStatus;
import com.lucid.recruit.contract.constants.EnumTimeCardApproval;
import com.lucid.recruit.contract.entity.ChargeCodeResource;
import com.lucid.recruit.contract.entity.ChargeCodeTasks;
import com.lucid.recruit.contract.entity.Contract;
import com.lucid.recruit.contract.entity.ContractWorkOrder;
import com.lucid.recruit.contract.entity.WorkOrderExpenseCodes;
import com.lucid.recruit.contract.entity.WorkOrderResource;
import com.lucid.recruit.contract.repo.ChargeCodeResourceRepo;
import com.lucid.recruit.contract.repo.ChargeCodeTaskRepo;
import com.lucid.recruit.contract.repo.ContractRepo;
import com.lucid.recruit.contract.repo.ContractWorkOrderRepo;
import com.lucid.recruit.contract.repo.WorkOrderExpenseCodeRepo;
import com.lucid.recruit.contract.repo.WorkOrderResourceRepo;
import com.lucid.recruit.timecard.constants.EnumTimeCardStatus;
import com.lucid.recruit.timecard.controller.TimeCardController;
import com.lucid.recruit.timecard.dto.TimeCardDTO;
import com.lucid.recruit.timecard.dto.TimeCardExpenseDTO;
import com.lucid.recruit.timecard.dto.TimeCardItemDTO;
import com.lucid.recruit.timecard.entity.TimeCard;
import com.lucid.recruit.timecard.repo.TimeCardRepo;
import com.lucid.recruit.worker.entity.Worker;
import com.lucid.recruit.worker.repo.WorkerRepository;

@Component
public class TimeCardValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeCardController.class);
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private TimeCardRepo timeCardRepo;
	@Autowired
	private ContractWorkOrderRepo contractWorkOrderRepo;
	@Autowired
	private ContractRepo contractRepo;
	@Autowired
	private ChargeCodeTaskRepo chargeCodeTaskRepo;
	@Autowired
	private ChargeCodeResourceRepo cntrctResRepo;
	@Autowired
	private WorkOrderResourceRepo workOrderResRepo;
	@Autowired
	private WorkOrderExpenseCodeRepo wrkOrderExpCodeRepo;
	private static final double MAX_HRS_DAY = 24;

	// Validate the Worker
	public Worker getWorker(String workerId) {
		Optional<Worker> worker = workerRepo.findById(workerId);
		if (worker.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Worker by id " + workerId + " not found");
		}
		return worker.get();
	}

	// Validate the Contract
	public Contract getContract(String contractId) {
		Optional<Contract> contract = contractRepo.findById(contractId);
		if (contract.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Contract by id " + contractId + " not found");
		}
		return contract.get();
	}

	// Get the work order associated with contract for time card period
	public ContractWorkOrder getWorkOrder(Contract contract, String workOrderID, LocalDate startDate,
			LocalDate endate) {
		Optional<ContractWorkOrder> workOrder = contractWorkOrderRepo.findByContractAndDates(contract.getContractID(),
				workOrderID, startDate, endate, EnumContractWOStatus.EXECUTED);
		if (workOrder.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Active work order not found by id " + workOrderID + " under contract by id "
							+ contract.getContractID() + " between start date " + startDate + " end date " + endate);
		}
		return workOrder.get();
	}

	// validate the work order for given resource in the period
	public WorkOrderResource validateWrkOrderRes(LocalDate startDate, LocalDate endDate, Worker worker,
			ContractWorkOrder workOrder) {
		Optional<WorkOrderResource> workOrderResource = workOrderResRepo.findByIdAndDates(workOrder.getWorkOrderID(),
				worker.getWorkerID(), startDate, endDate);
		if (workOrderResource.isEmpty()) {
			LOGGER.error("Work order " + workOrder.getWorkOrderID() + " not active for worker " + worker.getWorkerID()
					+ " for the period of " + startDate + " to " + endDate);
			throw new InvalidDataException(ErrorCodes.TC_V_00007, "workOrder", "workOrderID",
					workOrder.getWorkOrderID(), "Work order " + workOrder.getWorkOrderID() + " not active for worker "
							+ worker.getWorkerID() + " for the period of " + startDate + " to " + endDate);
		}
		return workOrderResource.get();
	}

	// validate missing time periods
	public void validateMissingTime(LocalDate startDate, LocalDate endDate, Worker worker, ContractWorkOrder workOrder,
			WorkOrderResource wrkOrderResource) {
		// Holidays are not included, need to design the same
		// work order resource start date and time card start date matching, 1st entry
		// for this time card no missing time

		Optional<TimeCard> latestTimeCard = timeCardRepo.getLatestTimeCard(worker.getWorkerID(),
				workOrder.getWorkOrderID());
		if (latestTimeCard.isEmpty()) {
			if (startDate.compareTo(wrkOrderResource.getStartDate()) != 0) {
				LOGGER.error("Hours shall be reported from " + wrkOrderResource.getStartDate());
				throw new InvalidDataException(ErrorCodes.TC_V_00009, "timeCardItems", "itemDate",
						wrkOrderResource.getStartDate().toString(),
						"Hours shall be reported from " + wrkOrderResource.getStartDate());
			}
		} else {
			long diffDays = ChronoUnit.DAYS.between(startDate, latestTimeCard.get().getEndDate());
			if (diffDays != 1) {
				LOGGER.error("Time card period shall start from " + latestTimeCard.get().getEndDate().plusDays(1));
				throw new InvalidDataException(ErrorCodes.TC_V_00008, "timeCardItems", "itemDate",
						latestTimeCard.get().getEndDate().plusDays(1).toString(),
						"Time card period shall start from " + latestTimeCard.get().getEndDate().plusDays(1));
			}
		}

	}

	public HashMap<String, ChargeCodeTasks> validateTimeCardItems(TimeCardDTO timeCard) {
		HashMap<String, ChargeCodeTasks> taskMap = new HashMap<>();
		// 1. validate time card dates
		LocalDate tcStartDate = timeCard.getContract().getWorkOrder().getStartDate();
		LocalDate tcEndDate = timeCard.getContract().getWorkOrder().getEndDate();

		if (tcStartDate.compareTo(tcEndDate) > 0) {
			LOGGER.error("Start Date " + tcStartDate.toString() + " shall less than or equal to End Date "
					+ tcEndDate.toString());
			throw new InvalidDataException(ErrorCodes.TC_V_00001, "workOrder", "startDate", tcStartDate.toString(),
					"Start Date " + tcStartDate.toString() + " shall less than or equal to End Date "
							+ tcEndDate.toString());
		}

		// 2.sort the array by item date and check missing dates
		List<TimeCardItemDTO> timeCardItems = timeCard.getContract().getWorkOrder().getTimeCardItems();
		sortTCItemList(timeCardItems);
		validateItemDates(tcStartDate, tcEndDate, timeCardItems, timeCard.getContract().getWorkOrder().getTotalHours());

		// 3.validate the time card items
		timeCardItems.forEach(tci -> {
			// get charge code for task and item date
			Optional<ChargeCodeTasks> chrgCodeTask = chargeCodeTaskRepo
					.findByIdAndDates(tci.getChargeCodeTask().getTaskId(), tci.getItemDate());
			if (chrgCodeTask.isEmpty()) {
				LOGGER.error(
						"Task Id " + tci.getChargeCodeTask().getTaskId() + " on " + tci.getItemDate() + " not found");
				throw new InvalidDataException(ErrorCodes.TC_V_00003, "chargeCodeTask", "taskId",
						tci.getChargeCodeTask().getTaskId(),
						"Task Id " + tci.getChargeCodeTask().getTaskId() + " on " + tci.getItemDate() + " not found");
			}
			taskMap.put(tci.getChargeCodeTask().getTaskId(), chrgCodeTask.get());
			// Validate if charge code associated to worker on the date
			List<ChargeCodeResource> crList = cntrctResRepo.findByWIDAndChargeCode(
					chrgCodeTask.get().getWoChrageCode().getChargeCodeId(), timeCard.getWorker().getWorkerID(),
					tci.getItemDate());
			if (crList.isEmpty()) {
				LOGGER.error("Task Id " + tci.getChargeCodeTask().getTaskId() + " on " + tci.getItemDate()
						+ " not associated to worker " + timeCard.getWorker().getWorkerID());
				throw new InvalidDataException(ErrorCodes.TC_V_00004, "chargeCodeTask", "taskId",
						tci.getChargeCodeTask().getTaskId(),
						"Task Id " + tci.getChargeCodeTask().getTaskId() + " on " + tci.getItemDate()
								+ " not associated to worker " + timeCard.getWorker().getWorkerID());
			}

		});

		return taskMap;
	}

	private void sortTCItemList(List<TimeCardItemDTO> timeCardItems) {
		Comparator<TimeCardItemDTO> comparator = (tc1, tc2) -> {
			return tc1.getItemDate().compareTo(tc2.getItemDate());
		};
		Collections.sort(timeCardItems, comparator);
	}

	private void validateItemDates(LocalDate tcStartDate, LocalDate tcEndDate, List<TimeCardItemDTO> timeCardItems,
			double totalHours) {
		LOGGER.debug("validating the item dates start");
		if (tcStartDate.compareTo(timeCardItems.get(0).getItemDate()) != 0) {
			LOGGER.error("Item dates should in range of time sheet date " + tcStartDate + " to " + tcEndDate);
			throw new InvalidDataException(ErrorCodes.TC_V_00002, "timeCardItems", "itemDate", tcStartDate.toString(),
					"Item dates should in range of time sheet date " + tcStartDate + " to " + tcEndDate);
		}

		if (tcEndDate.compareTo(timeCardItems.get(timeCardItems.size() - 1).getItemDate()) != 0) {
			LOGGER.error("Item dates should in range of time sheet date " + tcStartDate + " to " + tcEndDate);
			throw new InvalidDataException(ErrorCodes.TC_V_00002, "timeCardItems", "itemDate", tcStartDate.toString(),
					"Item dates should in range of time sheet date " + tcStartDate + " to " + tcEndDate);
		}

		LocalDate prvDate = tcStartDate;
		double dailyhours = 0;
		double totalHrsReported = 0;
		for (int i = 0; i < timeCardItems.size(); i++) {
			LocalDate itemDate = timeCardItems.get(i).getItemDate();
			double reportedHours = timeCardItems.get(i).getHours();

			long diffDays = ChronoUnit.DAYS.between(prvDate, itemDate);
			if (diffDays > 1) {
				LOGGER.error("Hours missing for " + prvDate.plusDays(1));
				throw new InvalidDataException(ErrorCodes.TC_V_00002, "timeCardItems", "itemDate",
						prvDate.plusDays(1).toString(), "Hours missing for " + prvDate.plusDays(1));
			}
			if (itemDate.compareTo(prvDate) == 0) {
				dailyhours += reportedHours;
			} else {
				dailyhours = reportedHours;
			}
			if (dailyhours > MAX_HRS_DAY) {
				LOGGER.error("maximum hours per day exceeded on " + itemDate);
				throw new InvalidDataException(ErrorCodes.TC_V_00005, "timeCardItems", "hours",
						String.valueOf(dailyhours), "maximum hours per day exceeded on " + itemDate);
			}
			prvDate = itemDate;
			totalHrsReported += reportedHours;
		}
		if (totalHrsReported != totalHours) {
			LOGGER.error("Total hours in time card items " + totalHrsReported
					+ " not matching with reported totalHours " + totalHours);
			throw new InvalidDataException(ErrorCodes.TC_V_00006, "contract", "totalHours",
					String.valueOf(totalHrsReported), "Total hours in time card items " + totalHrsReported
							+ " not matching with reported totalHours " + totalHours);
		}
		LOGGER.debug("validating the item dates end");

	}

	// check if any overlap of time for the given worker, work order & period
	public void validateTimeCardOverlap(LocalDate startDate, LocalDate endDate, Worker worker,
			ContractWorkOrder workOrder) {
		List<TimeCard> findByTSWoDates = timeCardRepo.findOverlapWoDates(worker.getWorkerID(),
				workOrder.getWorkOrderID(), startDate, endDate);

		if (findByTSWoDates != null && !findByTSWoDates.isEmpty()) {
			LOGGER.error("Worker " + worker.getWorkerID() + " work order " + workOrder.getWorkOrderID() + " from "
					+ startDate + " to " + endDate + " have overlap time ");
			throw new InvalidDataException(ErrorCodes.TC_V_00011, "timeCard", "Start date/End Date",
					startDate.toString(), "Worker " + worker.getWorkerID() + " work order " + workOrder.getWorkOrderID()
							+ " from " + startDate + " to " + endDate + " have overlap time ");
		}
	}

	// Validate timeCard Expenses
	public HashMap<String, WorkOrderExpenseCodes> validateExpenses(List<TimeCardExpenseDTO> timeCardExpenses,
			LocalDate startDate, LocalDate endDate, Worker worker, ContractWorkOrder workOrder) {
		HashMap<String, WorkOrderExpenseCodes> expCodeMap = new HashMap<>();
		for (Iterator<TimeCardExpenseDTO> iterator = timeCardExpenses.iterator(); iterator.hasNext();) {
			TimeCardExpenseDTO tcExpenseDTO = iterator.next();
			if (!isWithinRange(tcExpenseDTO.getExpenseDate(), startDate, endDate)) {
				LOGGER.error("Expense dates should in range of time sheet date " + startDate + " to " + endDate);
				throw new InvalidDataException(ErrorCodes.TC_V_00002, "timeCardExpenses", "expenseDate",
						tcExpenseDTO.getExpenseDate().toString(),
						"Expense dates should in range of time sheet date " + startDate + " to " + endDate);

			}
			// Validate if expense code is active for given worker
			LOGGER.debug("validating the expense code start");
			Optional<WorkOrderExpenseCodes> expCodeForTS = wrkOrderExpCodeRepo.getExpCodeForTS(
					tcExpenseDTO.getExpenseCode().getExpenseCodeId(), tcExpenseDTO.getExpenseDate(),
					worker.getWorkerID());
			if (expCodeForTS.isEmpty()) {
				LOGGER.error("Active expense code by id " + tcExpenseDTO.getExpenseID() + " not found for worker by id "
						+ worker.getWorkerID() + " on " + tcExpenseDTO.getExpenseDate());
				throw new InvalidDataException(ErrorCodes.TC_V_00013, "timeCardExpenses", "expenseCodeId",
						tcExpenseDTO.getExpenseCode().getExpenseCodeId(),
						"Active expense code by id " + tcExpenseDTO.getExpenseCode().getExpenseCodeId()
								+ " not found for worker by id " + worker.getWorkerID() + " on "
								+ tcExpenseDTO.getExpenseDate());
			}
			expCodeMap.put(tcExpenseDTO.getExpenseCode().getExpenseCodeId(), expCodeForTS.get());
			LOGGER.debug("validating the expense code end");

		}
		return expCodeMap;
	}

	private boolean isWithinRange(LocalDate expDate, LocalDate startDate, LocalDate endDate) {
		return ((expDate.equals(startDate)) || (expDate.isAfter(startDate)) && (expDate.equals(endDate))
				|| (expDate.isBefore(endDate)));

	}

	public EnumTimeCardStatus getCreateStatus(EnumTimeCardStatus status, ContractWorkOrder workOrder) {
		if (status == null || status.equals(EnumTimeCardStatus.DRAFT)) {
			return EnumTimeCardStatus.DRAFT;
		}
		if (status != EnumTimeCardStatus.SUBMIT) {
			LOGGER.error("Time card can not be created with status " + status);
			throw new InvalidDataException(ErrorCodes.TC_V_00014, "timecard", "status", status.toString(),
					"Time card can not be created with status " + status);
		}
		EnumTimeCardStatus returnStatus = EnumTimeCardStatus.PENDING_APPROVAL;
		if (workOrder.getTsApprovalFlow().equals(EnumTimeCardApproval.DirectClient)) {
			returnStatus = EnumTimeCardStatus.PENDING_APPROVAL_CLIENT;
		}
		return returnStatus;
	}

	public TimeCard fetchTimeCard(String timeCardID) {
		return timeCardRepo.findById(timeCardID).orElseThrow(() -> {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Time card by id:" + timeCardID + " not found");
		});
	}

}
