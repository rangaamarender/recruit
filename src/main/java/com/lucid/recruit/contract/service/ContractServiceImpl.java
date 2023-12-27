package com.lucid.recruit.contract.service;

import com.lucid.core.azure.AzureBlobService;
import com.lucid.core.azure.EnumAzureContainers;
import com.lucid.core.embeddable.GeoCode;
import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.contract.constants.EnumBillPeriodUnit;
import com.lucid.recruit.contract.constants.EnumContractAccountStatus;
import com.lucid.recruit.contract.constants.EnumContractStatus;
import com.lucid.recruit.contract.constants.EnumContractWOStatus;
import com.lucid.recruit.contract.dto.*;
import com.lucid.recruit.contract.entity.*;
import com.lucid.recruit.contract.repo.*;
import com.lucid.recruit.docs.constants.EnumDocSource;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.entity.BaseDocument;
import com.lucid.recruit.org.dto.RelatedOrgDTO;
import com.lucid.recruit.org.entity.Organization;
import com.lucid.recruit.referencedata.entity.WorkerType;
import com.lucid.recruit.referencedata.repo.WOSupervisorRoleRepo;
import com.lucid.recruit.worker.entity.WorkerStatus;
import com.lucid.recruit.worker.entity.WorkerStatusCode;
import com.lucid.recruit.worker.repo.WorkerRepository;
import com.lucid.util.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

	private static final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

	@Autowired
	private ContractRepo contractRepo;
	@Autowired
	private ContractValidator contractValidator;
	@Autowired
	private ContractModelConverter contractModelConverter;
	@Autowired
	private ContractAccountRepo contractAccountRepo;
	@Autowired
	private ContractAccountStatusRepo contractAccountStatusRepo;
	@Autowired
	private ContractBillingDetailsRepo contractBillingDetailsRepo;
	@Autowired
	private ContractDiscountRepo contractDiscountRepo;
	@Autowired
	private ContractExpenseRepo contractExpenseRepo;
	@Autowired
	private ContractWorkOrderRepo contractWorkOrderRepo;
	@Autowired
	private WorkOrderResourceRepo workOrderResourceRepo;
	@Autowired
	private ContractResourceWorkerTypeRepo contractResourceWorkerTypeRepo;
	@Autowired
	private ContractWorkLocationRepo contractWorkLocationRepo;
	@Autowired
	private WorkOrderSuppChargesRepo workOrderSuppChargesRepo;
	@Autowired
	private ContractClientRepo contractClientRepo;
	@Autowired
	private ContractSupervisorRepo contractSupervisorRepo;
	@Autowired
	private WorkOrderChargeCodeRepo workOrderChargeCodeRepo;
	@Autowired
	private ChargeCodeTaskRepo chargeCodeTaskRepo;
	@Autowired
	private ChargeCodeResourceRepo chargeCodeResourceRepo;
	@Autowired
	private ContractWorkOrderRateRepo workOrderRateRepo;
	@Autowired
	private WorkOrderExpenseCodeRepo workOrderExpenseCodeRepo;
	@Autowired
	private WOSupervisorRoleRepo woSupervisorRoleRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ContractSpecification contractSpecification;
	@Autowired
	private ContractDocumentRepo contractDocumentRepo;
	@Autowired
	private WorkOrderDocRepo workOrderDocRepo;
	@Autowired
	private AzureBlobService azureBlobService;
	@Autowired
	private WorkOrderDocAttrRepo woDocAttrRepo;
	@Autowired
	private ContractDocAttributeRepo contractDocAttrRepo;
	@Autowired
	private WorkerRepository workerRepository;

	@Override
	@Transactional
	public ContractDTO createContractMSA(ContractDTO contractDTO) {
		contractValidator.validateContract(contractDTO);
		Contract contract = contractModelConverter.convertToEntity(contractDTO);
		contract.setContractStatus(EnumContractStatus.ACTIVE);
		contract.setContractStatus(EnumContractStatus.ACTIVE);
		contractRepo.save(contract);
		//create contractAccount
		List<ContractAccount> contractAccounts = new ArrayList<>();
		if(contractDTO.getContractAccounts() != null && !contractDTO.getContractAccounts().isEmpty()){
			for(ContractAccountDTO contractAccountDTO : contractDTO.getContractAccounts()){
				ContractAccount contractAccount = createContractAccount(contractAccountDTO,contract);
				//create accountStatus
				createAccountStatus(contractAccount);
				contractAccounts.add(contractAccount);
			}
		}
		if(!contractAccounts.isEmpty()){
			contractValidator.validateContractAccountNames(contractAccounts);
			contractAccountRepo.saveAll(contractAccounts);
			for(ContractAccount contractAccount:contractAccounts){
				if(contractAccount.getContractBillingDetails() != null && !contractAccount.getContractBillingDetails().isEmpty()){
					contractBillingDetailsRepo.saveAll(contractAccount.getContractBillingDetails());
				}
				if(contractAccount.getContractDiscounts() != null && !contractAccount.getContractDiscounts().isEmpty()){
					contractDiscountRepo.saveAll(contractAccount.getContractDiscounts());
				}
				if(contractAccount.getContractExpenses() != null && !contractAccount.getContractExpenses().isEmpty()){
					contractExpenseRepo.saveAll(contractAccount.getContractExpenses());
				}
				if(!contractAccount.getGetAccountStatuses().isEmpty()){
					contractAccountStatusRepo.saveAll(contractAccount.getGetAccountStatuses());
				}
			}
		}
		contract.setContractAccounts(contractAccounts);
		return contractModelConverter.convertToDTO(contract);
	}

	private void createAccountStatus(ContractAccount contractAccount) {
		LocalDate currentDate = LocalDate.now();
		List<ContractAccountStatus> contractAccountStatuses = new ArrayList<>();
		if(currentDate.isAfter(contractAccount.getStartDate()) || currentDate.equals(contractAccount.getStartDate())){
			ContractAccountStatus accountStatus = new ContractAccountStatus();
			accountStatus.setContractAccount(contractAccount);
			accountStatus.setStatus(EnumContractAccountStatus.ACTIVE);
			accountStatus.setEffectiveDate(contractAccount.getStartDate());
			contractAccountStatuses.add(accountStatus);
		} else{
			ContractAccountStatus accountStatus1 = new ContractAccountStatus();
			accountStatus1.setContractAccount(contractAccount);
			accountStatus1.setStatus(EnumContractAccountStatus.PENDING);
			accountStatus1.setEffectiveDate(LocalDate.now());
			contractAccountStatuses.add(accountStatus1);
			//active from
			ContractAccountStatus accountStatus = new ContractAccountStatus();
			accountStatus.setContractAccount(contractAccount);
			accountStatus.setStatus(EnumContractAccountStatus.ACTIVE);
			accountStatus.setEffectiveDate(contractAccount.getStartDate());
			contractAccountStatuses.add(accountStatus);
		}
		contractAccount.setGetAccountStatuses(contractAccountStatuses);
	}


	@Override
	@Transactional
	public ContractAccountDTO createContractAccount(String contractID, ContractAccountDTO contractAccountDTO) {
		Contract contract = contractValidator.findContract(contractID);
		//create contractWOGroup
		ContractAccount contractAccount = createContractAccount(contractAccountDTO,contract);
		List<ContractAccount> contractAccounts = contract.getContractAccounts();
		if(!contractAccounts.isEmpty()){
			contractAccounts.add(contractAccount);
			contractValidator.validateContractAccountNames(contractAccounts);
		}
		contractAccountRepo.save(contractAccount);
		if(contractAccount.getContractBillingDetails() != null && !contractAccount.getContractBillingDetails().isEmpty()){
			contractBillingDetailsRepo.saveAll(contractAccount.getContractBillingDetails());
		}
		if(contractAccount.getContractDiscounts() != null && !contractAccount.getContractDiscounts().isEmpty()){
			contractDiscountRepo.saveAll(contractAccount.getContractDiscounts());
		}
		if(contractAccount.getContractExpenses() != null && !contractAccount.getContractExpenses().isEmpty()){
			contractExpenseRepo.saveAll(contractAccount.getContractExpenses());
		}
		return contractModelConverter.convertToDTO(contractAccount);
	}

	@Override
	@Transactional
	public ContractDiscountDTO createContractDiscount(String contractAccId, ContractDiscountDTO contractDiscountDTO) {
		//create contract discount
		ContractAccount contractAccount = contractValidator.findContractAccount(contractAccId);
		ContractDiscount contractDiscount = contractModelConverter.convertToEntity(contractDiscountDTO);
		contractValidator.validateContractDiscountDates(contractDiscount,contractAccount);
		computeContractDiscount(contractDiscount);
		contractDiscount.setContractAccount(contractAccount);
		return contractModelConverter.convertToDTO(contractDiscountRepo.save(contractDiscount));
	}

	@Override
	@Transactional
	public ContractExpenseDTO createContractExpense(String contractWOGroupID, ContractExpenseDTO contractExpenseDTO) {
		ContractAccount contractAccount = contractValidator.findContractAccount(contractWOGroupID);
		ContractExpense contractExpense = contractModelConverter.convertToEntity(contractExpenseDTO);
		contractExpense.setContractAccount(contractAccount);
		return contractModelConverter.convertToDTO(contractExpenseRepo.save(contractExpense));
	}

	@Override
	@Transactional
	public ContractWorkOrderDTO createContractWorkOrder(String contractAccountId, ContractWorkOrderDTO contractWorkOrderDTO) {
		//validate wbs code
		if(contractWorkOrderDTO.getWbsCode() != null && !StringUtils.isAllBlank(contractWorkOrderDTO.getWbsCode())){
			if (isExistsByWbsCode(contractWorkOrderDTO.getWbsCode())) {
				log.warn("WorkOrder already exists by wbsCode:" + contractWorkOrderDTO.getWbsCode());
				throw new InvalidDataException(ErrorCodes.CNT_BV_0017, "contractWorkOrder", "wbsCode", contractWorkOrderDTO.getWbsCode(), "WorkOrder already exists by wbsCode:" + contractWorkOrderDTO.getWbsCode());
			}
		}
		//fetch contract workOrderGroup
		ContractAccount contractAccount = contractValidator.findContractAccount(contractAccountId);
		//create workOrders
		ContractWorkOrder contractWorkOrder = contractModelConverter.convertToEntity(contractWorkOrderDTO);
		if (contractWorkOrder.getStatus() == null) {
			contractWorkOrder.setStatus(EnumContractWOStatus.DRAFT);
		}
		contractWorkOrder.setContractAccount(contractAccount);
		contractWorkOrderRepo.save(contractWorkOrder);

		//create workOrderResource

		if(!contractWorkOrderDTO.getWorkOrderResources().isEmpty()) {
			//validate resource
			if (!contractWorkOrderDTO.getMultiResource()) {
				if (contractWorkOrderDTO.getWorkOrderResources().size() > 1) {
					throw new InvalidDataException(ErrorCodes.CNT_BV_0036, "contractWorkOrder", "workOrderResources", String.valueOf(contractWorkOrderDTO.getWorkOrderResources().size()), "Multiple resources not allows for this workOrder");
				}
			}
			List<WorkOrderResource> workOrderResources = new ArrayList<>();
			for(WorkOrderResourceDTO workOrderResourceDTO:contractWorkOrderDTO.getWorkOrderResources()){
                WorkOrderResource workOrderResource = createWorkOrderResource(workOrderResourceDTO);
				workOrderResource.setWorkOrder(contractWorkOrder);
				workOrderResources.add(workOrderResource);
			}
			if(!workOrderResources.isEmpty()) {
				workOrderResourceRepo.saveAll(workOrderResources);
				for(WorkOrderResource workOrderResource:workOrderResources){
					if(workOrderResource.getSupervisors() != null && !workOrderResource.getSupervisors().isEmpty()){
						contractSupervisorRepo.saveAll(workOrderResource.getSupervisors());
					}
					if(workOrderResource.getWorkerType() != null && !workOrderResource.getWorkerType().isEmpty()){
						contractResourceWorkerTypeRepo.saveAll(workOrderResource.getWorkerType());
					}
				}
			}
			contractWorkOrder.setWorkOrderResources(workOrderResources);
		}

		//create contract workLocations
		List<ContractWorkLocation> contractWorkLocations = new ArrayList<>();
		if (contractWorkOrderDTO.getWorkLocation() != null && !contractWorkOrderDTO.getWorkLocation().isEmpty()) {
			for (ContractWorkLocationDTO workLocationDTO : contractWorkOrderDTO.getWorkLocation()) {
				ContractWorkLocation contractWorkLocation = createWorkLocation(workLocationDTO);
				contractWorkLocation.setWorkOrder(contractWorkOrder);
				contractWorkLocations.add(contractWorkLocation);
			}
		}
		if(!contractWorkLocations.isEmpty()){
			contractWorkLocationRepo.saveAll(contractWorkLocations);
			for(ContractWorkLocation contractWorkLocation:contractWorkLocations){
				if(contractWorkLocation.getChargeCodes() != null && !contractWorkLocation.getChargeCodes().isEmpty()){
					workOrderChargeCodeRepo.saveAll(contractWorkLocation.getChargeCodes());
					for(WorkOrderChargeCode workOrderChargeCode:contractWorkLocation.getChargeCodes()){
						if(workOrderChargeCode.getChargeCodeTasks() != null && !workOrderChargeCode.getChargeCodeTasks().isEmpty()){
							chargeCodeTaskRepo.saveAll(workOrderChargeCode.getChargeCodeTasks());
						}
						if(workOrderChargeCode.getContractResources() != null && !workOrderChargeCode.getContractResources().isEmpty()){
							chargeCodeResourceRepo.saveAll(workOrderChargeCode.getContractResources());
						}
						if(workOrderChargeCode.getWorkOrderRates() != null && !workOrderChargeCode.getWorkOrderRates().isEmpty()){
							workOrderRateRepo.saveAll(workOrderChargeCode.getWorkOrderRates());
						}
						if(workOrderChargeCode.getWorkOrderExpenses() != null && !workOrderChargeCode.getWorkOrderExpenses().isEmpty()){
							workOrderExpenseCodeRepo.saveAll(workOrderChargeCode.getWorkOrderExpenses());
						}
					}
				}
			}
		}
		contractWorkOrder.setWorkLocation(contractWorkLocations);

		//create workOrderSupCharges
		List<WorkOrderSuppCharges> workOrderSuppCharges = new ArrayList<>();
		if (contractWorkOrderDTO.getSuppCharges() != null && !contractWorkOrderDTO.getSuppCharges().isEmpty()) {
			for (WorkOrderSuppChargesDTO workOrderSuppChargesDTO : contractWorkOrderDTO.getSuppCharges()) {
				WorkOrderSuppCharges workOrderSuppCharge = contractModelConverter.convertToEntity(workOrderSuppChargesDTO);
				workOrderSuppCharge.setWorkOrder(contractWorkOrder);
				workOrderSuppCharges.add(workOrderSuppCharge);
			}
		}
		if(!workOrderSuppCharges.isEmpty()){
			workOrderSuppChargesRepo.saveAll(workOrderSuppCharges);
		}
		contractWorkOrder.setSuppCharges(workOrderSuppCharges);

		//create workOrder clients
		List<ContractClient> contractClients = new ArrayList<>();
		if (contractWorkOrderDTO.getClient() != null && !contractWorkOrderDTO.getClient().isEmpty()) {
			for (ContractClientDTO contractClientDTO : contractWorkOrderDTO.getClient()) {
				ContractClient contractClient = contractModelConverter.convertToEntity(contractClientDTO);
				contractClient.setWorkOrder(contractWorkOrder);
				contractClients.add(contractClient);
			}
		}
		contractClients.sort(ContractClient.createContractClientLambdaComparator());
		contractValidator.validateContractClients(contractClients);
		if(!contractClients.isEmpty()){
			contractClientRepo.saveAll(contractClients);
		}
		contractWorkOrder.setClient(contractClients);
		return contractModelConverter.convertToDTO(contractWorkOrder);
	}

	@Override
	@Transactional
	public ContractDTO updateContractMSA(String contractID,ContractDTO contractDTO) {
		Contract contract = contractValidator.findContract(contractID);
		updateContractMSAData(contract,contractDTO);
		ContractDTO response = contractModelConverter.convertToDTO(contractRepo.save(contract));
        /*//if contractWorkOrderNeed to create or update
		if(contractDTO.getContractAccounts() != null && !contractDTO.getContractAccounts().isEmpty()){
			List<ContractAccountDTO> contractAccountDTOS = new ArrayList<>();
			for(ContractAccountDTO contractAccountDTO :contractDTO.getContractAccounts()){
				if(!StringUtils.isAllBlank(contractAccountDTO.getContractAccountId())){
					contractAccountDTOS.add(updateContractAccount(contractAccountDTO.getContractAccountId(), contractAccountDTO));
				} else{
					contractAccountDTOS.add(createContractAccount(contract.getContractID(), contractAccountDTO));
				}
			}
			response.setContractAccounts(contractAccountDTOS);
		}*/
		return response;
	}

	@Override
	@Transactional
	public ContractAccountDTO updateContractAccount(String contractAccountId, ContractAccountDTO contractAccountDTO) {
		ContractAccount updatableAccount = contractValidator.findContractAccount(contractAccountId);
		updateContractAccountData(updatableAccount, contractAccountDTO);
		contractAccountRepo.save(updatableAccount);
		if(updatableAccount.getContractBillingDetails() != null && !updatableAccount.getContractBillingDetails().isEmpty()){
			contractBillingDetailsRepo.saveAll(updatableAccount.getContractBillingDetails());
		}
		if(updatableAccount.getContractExpenses() != null && !updatableAccount.getContractExpenses().isEmpty()){
			contractExpenseRepo.saveAll(updatableAccount.getContractExpenses());
		}
		return contractModelConverter.convertToDTO(updatableAccount);
	}

	@Override
	@Transactional
	public ContractWorkOrderDTO updateContractWorkOrder(String workOrderID, ContractWorkOrderDTO contractWorkOrderDTO) {
		ContractWorkOrder contractWorkOrder = contractValidator.findContractWorkOrder(workOrderID);
		updateContractWorkOrderData(contractWorkOrder,contractWorkOrderDTO);
		contractWorkOrderRepo.save(contractWorkOrder);
		if(contractWorkOrder.getWorkLocation() != null && !contractWorkOrder.getWorkLocation().isEmpty()){
			contractWorkLocationRepo.saveAll(contractWorkOrder.getWorkLocation());
			for(ContractWorkLocation contractWorkLocation:contractWorkOrder.getWorkLocation()){
				if(contractWorkLocation.getChargeCodes() != null && !contractWorkLocation.getChargeCodes().isEmpty()){
					workOrderChargeCodeRepo.saveAll(contractWorkLocation.getChargeCodes());
					for(WorkOrderChargeCode workOrderChargeCode:contractWorkLocation.getChargeCodes()){
						if(workOrderChargeCode.getChargeCodeTasks() != null && !workOrderChargeCode.getChargeCodeTasks().isEmpty()){
							chargeCodeTaskRepo.saveAll(workOrderChargeCode.getChargeCodeTasks());
						}
						if(workOrderChargeCode.getContractResources() != null && !workOrderChargeCode.getContractResources().isEmpty()){
							chargeCodeResourceRepo.saveAll(workOrderChargeCode.getContractResources());
						}
						if(workOrderChargeCode.getWorkOrderRates() != null && !workOrderChargeCode.getWorkOrderRates().isEmpty()){
							workOrderRateRepo.saveAll(workOrderChargeCode.getWorkOrderRates());
						}
						if(workOrderChargeCode.getWorkOrderExpenses() != null && !workOrderChargeCode.getWorkOrderExpenses().isEmpty()){
							workOrderExpenseCodeRepo.saveAll(workOrderChargeCode.getWorkOrderExpenses());
						}
					}
				}
			}
		}
		if(contractWorkOrder.getClient() != null && !contractWorkOrder.getClient().isEmpty()){
           contractClientRepo.saveAll(contractWorkOrder.getClient());
		}
		if(contractWorkOrder.getSuppCharges() != null && !contractWorkOrder.getSuppCharges().isEmpty()){
           workOrderSuppChargesRepo.saveAll(contractWorkOrder.getSuppCharges());
		}
		if(contractWorkOrder.getWorkOrderResources() != null && !contractWorkOrder.getWorkOrderResources().isEmpty()){
			workOrderResourceRepo.saveAll(contractWorkOrder.getWorkOrderResources());
			for(WorkOrderResource workOrderResource:contractWorkOrder.getWorkOrderResources()) {
				if(workOrderResource.getSupervisors() != null && !workOrderResource.getSupervisors().isEmpty()) {
					if (workOrderResource.getSupervisors() != null && !workOrderResource.getSupervisors().isEmpty()) {
						contractSupervisorRepo.saveAll(workOrderResource.getSupervisors());
					}
					if(workOrderResource.getWorkerType() != null && !workOrderResource.getWorkerType().isEmpty()){
						contractResourceWorkerTypeRepo.saveAll(workOrderResource.getWorkerType());
					}
				}
			}
		}
		return contractModelConverter.convertToDTO(contractWorkOrder);
	}

	@Override
	public ContractDTO getContractMSA(String contractID) {
		Contract contract = contractValidator.findContract(contractID);
		return contractModelConverter.convertToDTO(contract);
	}

	@Override
	public ContractAccountDTO getContractAccount(String contractWOGroupID) {
		ContractAccount contractAccount = contractValidator.findContractAccount(contractWOGroupID);
		return contractModelConverter.convertToDTO(contractAccount);
	}

	@Override
	public ContractWorkOrderDTO getContractWorkOrder(String workOrderID) {
		ContractWorkOrder contractWorkOrder = contractValidator.findContractWorkOrder(workOrderID);
		return contractModelConverter.convertToDTO(contractWorkOrder);
	}

	@Override
	public Page<ContractSummaryDTO> getAllContracts(Integer offset, Integer limit, String contractName, String organizationID) {
		List<ContractSummaryDTO> contractSummaryDTOS = new ArrayList<>();
		Pageable pageable = ServiceUtils.getPageableObject(offset,limit,null);
		Specification<Contract> specification = contractSpecification.conditionalSearchForContractMSA(contractName,organizationID);
		Page<Contract> contractsPage;
		if(specification != null){
			contractsPage = contractRepo.findAll(specification,pageable);
		} else{
			contractsPage = contractRepo.findAll(pageable);
		}
		if(contractsPage != null){
			List<Contract> contracts = contractsPage.getContent();
			contractSummaryDTOS = convertToContractMSASummary(contracts);
			return new PageImpl<>(contractSummaryDTOS,pageable,contractsPage.getTotalElements());
		} else{
			return  new PageImpl<>(contractSummaryDTOS,pageable,0);
		}
	}

	private List<ContractSummaryDTO> convertToContractMSASummary(List<Contract> contracts) {
		List<ContractSummaryDTO> contractSummaryDTOS = new ArrayList<>();
		for(Contract contract:contracts){
			ContractSummaryDTO  contractSummary = new ContractSummaryDTO();
			contractSummary.setContractName(contract.getContractName());
			contractSummary.setContractID(contract.getContractID());
			contractSummary.setRelatedOrg(modelMapper.map(contract.getRelatedOrg(),RelatedOrgDTO.class));
			contractSummary.setStartDate(contract.getStartDate());
			contractSummary.setEndDate(contract.getEndDate());
			List<String> woGroupIds = contract.getContractAccounts().stream().map(ContractAccount::getContractAccountId).collect(Collectors.toList());
			List<ContractWorkOrder> contractWorkOrders = contractWorkOrderRepo.findActiveWorkOrders(woGroupIds,EnumContractWOStatus.INPROGRESS);
			if(contractWorkOrders != null && !contractWorkOrders.isEmpty()){
				List<WorkOrderSummaryDTO> workOrderSummaryList = convertToWOSummary(contractWorkOrders);
				contractSummary.setWorkOrders(workOrderSummaryList);
			}
			contractSummaryDTOS.add(contractSummary);
		}
		return contractSummaryDTOS;
	}

	@Override
	public Page<WorkOrderSummaryDTO> getAllWorkOrders(Integer offset, Integer limit, String wbsCode, String organizationID) {
		List<WorkOrderSummaryDTO> contractWOSummaryDTOS = new ArrayList<>();
		Pageable pageable = ServiceUtils.getPageableObject(offset,limit,null);
		Specification<ContractWorkOrder> specification = contractSpecification.conditionalSearchForContractWO(wbsCode,organizationID);
		Page<ContractWorkOrder> woPage;
		if(specification != null){
			woPage = contractWorkOrderRepo.findAll(specification,pageable);
		} else{
			woPage = contractWorkOrderRepo.findAll(pageable);
		}
		if(woPage != null){
			List<ContractWorkOrder> contractWorkOrders = woPage.getContent();
			contractWOSummaryDTOS = convertToWOSummary(contractWorkOrders);
			return new PageImpl<>(contractWOSummaryDTOS,pageable,woPage.getTotalElements());
		} else{
			return  new PageImpl<>(contractWOSummaryDTOS,pageable,0);
		}
	}

	@Override
	public boolean updateWOStatus(String workOrderID, EnumContractWOStatus status) {
		ContractWorkOrder contractWorkOrder = contractValidator.findContractWorkOrder(workOrderID);
		contractWorkOrder.setStatus(status);
		contractWorkOrderRepo.save(contractWorkOrder);
		return true;
	}

	@Override
	@Transactional
	public ContractDocumentDTO createContractDocument(String contractID, ContractDocumentDTO contractDocumentDTO) {
		Contract contract = contractValidator.findContract(contractID);
        //validate docDTO
		ContractDocument contractDocument = contractValidator.validateContractDocument(contractDocumentDTO);
		if(contractDocument == null) {
			contractDocument = contractModelConverter.convertToEntity(contractDocumentDTO);
			contractDocument.setContract(contract);
			contractDocument.setDocSource(EnumDocSource.UPLOADED);
		}
		contractDocument.setStatus(EnumDocStatus.PendingDocument);
		if(contractDocumentDTO.getContractDocAttributes() != null && !contractDocumentDTO.getContractDocAttributes().isEmpty()){
			List<ContractDocAttributes> contractDocAttributesList = new ArrayList<>();
			for(ContractDocAttributesDTO contractDocAttributesDTO:contractDocumentDTO.getContractDocAttributes()){
				ContractDocAttributes contractDocAttributes = contractModelConverter.convertToEntity(contractDocAttributesDTO);
				contractDocAttributes.setContractDocument(contractDocument);
				contractDocAttributesList.add(contractDocAttributes);
			}
			contractDocument.setContractDocAttributes(contractDocAttributesList);
		}
		return contractModelConverter.convertToDTO(contractDocumentRepo.save(contractDocument));
	}

	@Override
	@Transactional
	public WorkOrderDocumentDTO createWorkOrderDocument(String workOrderID, WorkOrderDocumentDTO workOrderDocumentDTO) {
		ContractWorkOrder workOrder = contractValidator.findContractWorkOrder(workOrderID);
		//validate docDTO
		WorkOrderDocument workOrderDocument = contractValidator.validateWorkOrderDocument(workOrderDocumentDTO);
		if(workOrderDocument == null) {
			workOrderDocument = contractModelConverter.convertToEntity(workOrderDocumentDTO);
			workOrderDocument.setContractWorkOrder(workOrder);
			workOrderDocument.setDocSource(EnumDocSource.UPLOADED);
		}
		workOrderDocument.setStatus(EnumDocStatus.PendingDocument);
		List<WorkOrderDocAttributes> workOrderDocAttributesList = new ArrayList<>();
		if(workOrderDocumentDTO.getWorkOrderDocAttributes() != null && !workOrderDocumentDTO.getWorkOrderDocAttributes().isEmpty()){
			for(WorkOrderDocAttributesDTO workOrderDocAttributesDTO:workOrderDocumentDTO.getWorkOrderDocAttributes()){
				WorkOrderDocAttributes workOrderDocAttributes = contractModelConverter.convertToEntity(workOrderDocAttributesDTO);
				workOrderDocAttributes.setWoDocument(workOrderDocument);
				workOrderDocAttributesList.add(workOrderDocAttributes);
			}
		}
		workOrderDocument.setWorkOrderDocAttributes(workOrderDocAttributesList);
		return contractModelConverter.convertToDTO(workOrderDocRepo.save(workOrderDocument));
	}


	@Override
	public String uploadContractDocFile(String contractDocID, MultipartFile file) {
		ContractDocument contractDocument = contractValidator.getContractDoc(contractDocID);
		String fileName = contractDocID + file.getOriginalFilename();
		try {
			azureBlobService.upload(file, fileName, EnumAzureContainers.contracts);
		} catch (IOException e) {
			log.error("Error while storing MultipartFile ");
			// throw new InvalidDataException(ErrorCodes.W_BV_0010, "WorkerDocuent", "File
			// data", null, e.getMessage());
			throw new RuntimeException(e);
		}
		contractDocument.setFileName(file.getOriginalFilename());
		contractDocument.setFileType(file.getContentType());
		contractDocument.setStatus(EnumDocStatus.Active);
		contractDocument.setUrl(fileName);
		contractDocumentRepo.save(contractDocument);
		return file.getOriginalFilename() + "Uploaded sucessfully ";
	}

	@Override
	public String uploadWorkOrderDocFile(String woDocID, MultipartFile file) {
		WorkOrderDocument workOrderDocument = contractValidator.getWorkOrderDocument(woDocID);
		String fileName = woDocID + file.getOriginalFilename();
		try {
			azureBlobService.upload(file, fileName,EnumAzureContainers.contracts);
		} catch (IOException e) {
			log.error("Error while storing MultipartFile ");
			// throw new InvalidDataException(ErrorCodes.W_BV_0010, "WorkerDocuent", "File
			// data", null, e.getMessage());
			throw new RuntimeException(e);
		}
		workOrderDocument.setFileName(file.getOriginalFilename());
		workOrderDocument.setFileType(file.getContentType());
		workOrderDocument.setStatus(EnumDocStatus.Active);
		workOrderDocument.setUrl(fileName);
		workOrderDocRepo.save(workOrderDocument);
		return file.getOriginalFilename() + "Uploaded sucessfully ";
	}

	@Override
	@Transactional
	public String deleteContractDocument(String contractDocID) {
		ContractDocument contractDocument = contractValidator.getContractDoc(contractDocID);
		//delete document from storage
		if (!StringUtils.isEmpty(contractDocument.getUrl())) {
			azureBlobService.deleteBlob(contractDocument.getUrl(),EnumAzureContainers.contracts);
		}
		//delete all attributes
		contractDocAttrRepo.deleteAll(contractDocument.getContractDocAttributes());
		//If Document is Auto assigned, then delete only document from storage, and make the document name , other fields to null which are set while create document and delete attributes and move the status Pending
		if (contractDocument.getDocSource().compareTo(EnumDocSource.AUTOASSIGNED) == 0) {
			resetValues(contractDocument);
			contractDocumentRepo.save(contractDocument);
		}
		//If Document is not Auto assigned delete the document form storage and delete the worker doc and attributes
		else {
			contractDocumentRepo.delete(contractDocument);
		}
		return "Document by id:"+contractDocID+" deleted successfully";
	}


	@Override
	@Transactional
	public String deleteWorkOrderDocument(String woDocID) {
		WorkOrderDocument workOrderDocument = contractValidator.getWorkOrderDocument(woDocID);
		//delete document from storage
		if (!StringUtils.isEmpty(workOrderDocument.getUrl())) {
			azureBlobService.deleteBlob(workOrderDocument.getUrl(),EnumAzureContainers.contracts);
		}
		//delete all attributes
		woDocAttrRepo.deleteAll(workOrderDocument.getWorkOrderDocAttributes());
		//If Document is Auto assigned, then delete only document from storage, and make the document name , other fields to null which are set while create document and delete attributes and move the status Pending
		if (workOrderDocument.getDocSource().compareTo(EnumDocSource.AUTOASSIGNED) == 0) {
			resetValues(workOrderDocument);
			workOrderDocRepo.save(workOrderDocument);
		}
		//If Document is not Auto assigned delete the document form storage and delete the worker doc and attributes
		else {
			workOrderDocRepo.delete(workOrderDocument);
		}
		return "Document by id:"+woDocID+" deleted successfully";
	}

	@Override
	public byte[] retrieveContractRelatedFile(String url) {
		byte[] filedata = null;
		try {
			filedata = azureBlobService.getFile(url,EnumAzureContainers.contracts);
		} catch (URISyntaxException e) {
			log.error("Error while reading file from storage ");
			throw new RuntimeException(e);
		}
		return filedata;
	}

	@Override
	public ContractDocument retrieveContractDocument(String contractDocID) {
		return contractDocumentRepo.findById(contractDocID).orElseThrow(() -> {throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractDocument by id:"+contractDocID+" not found");});
	}

	@Override
	public WorkOrderDocument retrieveWorkOrderDocument(String woDocID) {
		return workOrderDocRepo.findById(woDocID).orElseThrow(() -> {throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrder document by id:"+woDocID+" not found");});
	}

	@Override
	public String deleteDiscount(String contractAccountId, String discountId,LocalDate endDate) {
		ContractDiscount discount = contractDiscountRepo.findByAccIdAndDisId(contractAccountId,discountId);
		if(discount == null){
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractDiscount by id:"+discountId+" not found");
		}
		if(endDate == null){
			endDate = LocalDate.now();
		}
		discount.setEndDate(endDate);
		contractDiscountRepo.save(discount);
		return "Discount ended on "+endDate;
	}

	@Override
	public ContractDiscountDTO updateContractDiscount(String contractAccountId, String discountId, ContractDiscountDTO contractDiscountDTO) {
		ContractDiscount contractDiscount = contractDiscountRepo.findByAccIdAndDisId(contractAccountId,discountId);
		if(contractDiscount == null){
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractDiscount by id:"+discountId+" not found");
		}
		updateContractDiscountData(contractDiscount,contractDiscountDTO);
		contractDiscountRepo.save(contractDiscount);
		return modelMapper.map(contractDiscount,ContractDiscountDTO.class);
	}

	@Override
	public List<ContractAccountDTO> getContractAccounts(String contractID) {
		List<ContractAccount> contractAccounts = contractAccountRepo.findByContractId(contractID);
		List<ContractAccountDTO> contractAccountDTOS = new ArrayList<>();
		if(contractAccounts != null && !contractAccounts.isEmpty()){
			for(ContractAccount contractAccount:contractAccounts) {
				contractAccountDTOS.add(prepareResponseData(contractAccount));
			}
		}
		return contractAccountDTOS;
	}

	private ContractAccountDTO prepareResponseData(ContractAccount contractAccount) {
		ContractAccountDTO contractAccountDTO = new ContractAccountDTO();
		BeanUtils.copyProperties(contractAccount,contractAccountDTO);
		if(contractAccount.getContractBillingDetails() != null && !contractAccount.getContractBillingDetails().isEmpty()){
			contractAccount.getContractBillingDetails().sort(ContractBillingDetails.createContractBillingLambdaComparator());
			List<ContractBillingDetailsDTO> contractBillingDetailsDTOList = new ArrayList<>();
			ContractBillingDetailsDTO contractBillingDetailsDTO = new ContractBillingDetailsDTO();
			BeanUtils.copyProperties(contractAccount.getContractBillingDetails().get(contractAccount.getContractBillingDetails().size()-1),contractBillingDetailsDTO);
			contractBillingDetailsDTOList.add(contractBillingDetailsDTO);
			contractAccountDTO.setContractBillingDetails(contractBillingDetailsDTOList);
		}
		if(contractAccount.getContractDiscounts() != null && !contractAccount.getContractDiscounts().isEmpty()){
			List<ContractDiscountDTO> contractDiscountDTOS = new ArrayList<>();
			for(ContractDiscount contractDiscount:contractAccount.getContractDiscounts()){
				contractDiscountDTOS.add(modelMapper.map(contractDiscount,ContractDiscountDTO.class));
			}
			contractAccountDTO.setContractDiscounts(contractDiscountDTOS);
		}
		ContractAccountStatus contractAccountStatus = contractAccountStatusRepo.getCurrentAccountStatus(contractAccount.getContractAccountId(),LocalDate.now());
		List<ContractAccountStatusDTO> contractAccountStatusDTOList = new ArrayList<>();
		if(contractAccountStatus != null){
			contractAccountStatusDTOList.add(modelMapper.map(contractAccountStatus,ContractAccountStatusDTO.class));
		}
		contractAccountDTO.setAccountStatuses(contractAccountStatusDTOList);
		return contractAccountDTO;
	}

	private void resetValues(BaseDocument document){
		document.setExpiryDate(null);
		document.setIssueDate(null);
		document.setFileExt(null);
		document.setFileName(null);
		document.setFileType(null);
		document.setStatus(EnumDocStatus.Pending);
		document.setUrl(null);
	}

	private ContractAccount createContractAccount(ContractAccountDTO contractAccountDTO,Contract contract) {
		ContractAccount contractAccount = contractModelConverter.convertToEntity(contractAccountDTO);
		contractAccount.setContract(contract);
		contractValidator.validateContractAccountStartDate(contractAccount,contract);
		//create contract billing details
		List<ContractBillingDetails> contractBillingDetailsList = new ArrayList<>();
		if (contractAccountDTO.getContractBillingDetails() != null && !contractAccountDTO.getContractBillingDetails().isEmpty()) {
			//first element will be taken remaining will be discarded
			ContractBillingDetailsDTO contractBillingDetailsDTO = contractAccountDTO.getContractBillingDetails().get(0);
			//validate billingDetails
			contractValidator.validateBillingDetails(contractBillingDetailsDTO);
			ContractBillingDetails contractBillingDetails = contractModelConverter.convertToEntity(contractBillingDetailsDTO);
			contractBillingDetails.setContractAccount(contractAccount);
			contractValidator.validateContractBillingDates(contractBillingDetails,contractAccount);
			//set nextBillDate for account
			contractAccount.setNextBillDate(getNextBillDate(contractBillingDetails.getBillCycle(),contractBillingDetails.getBillPeriod(),contractBillingDetails.getBillPeriodUnits(),contractAccount.getStartDate()));
			contractBillingDetailsList.add(contractBillingDetails);
		}
		contractAccount.setContractBillingDetails(contractBillingDetailsList);

		//create contract discounts
		List<ContractDiscount> contractDiscounts = new ArrayList<>();
		if(contractAccountDTO.getContractDiscounts() != null && !contractAccountDTO.getContractDiscounts().isEmpty()){
			for(ContractDiscountDTO contractDiscountDTO : contractAccountDTO.getContractDiscounts()){
				ContractDiscount contractDiscount = contractModelConverter.convertToEntity(contractDiscountDTO);
				contractDiscount.setContractAccount(contractAccount);
				computeContractDiscount(contractDiscount);
				contractValidator.validateContractDiscountDates(contractDiscount,contractAccount);
				contractDiscounts.add(contractDiscount);
			}
		}
		contractDiscounts.sort(ContractDiscount.createContractDiscountLambdaComparator());
		contractValidator.validateContractDiscounts(contractDiscounts);
		contractAccount.setContractDiscounts(contractDiscounts);

		//create contract expense
		List<ContractExpense> contractExpenses = new ArrayList<>();
		if(contractAccountDTO.getContractExpenses() != null && !contractAccountDTO.getContractExpenses().isEmpty()){
			for(ContractExpenseDTO contractExpenseDTO: contractAccountDTO.getContractExpenses()) {
				ContractExpense contractExpense = contractModelConverter.convertToEntity(contractExpenseDTO);
				contractExpense.setContractAccount(contractAccount);
				contractValidator.validateContractExpenseDates(contractExpense,contractAccount);
				contractExpenses.add(contractExpense);
			}
		}
		contractAccount.setContractExpenses(contractExpenses);
		return contractAccount;
	}

	private List<WorkOrderSummaryDTO> convertToWOSummary(List<ContractWorkOrder> contractWorkOrders) {
		List<WorkOrderSummaryDTO> workOrderSummaryList = new ArrayList<>();
		if(contractWorkOrders != null && !contractWorkOrders.isEmpty()){
			for(ContractWorkOrder contractWorkOrder:contractWorkOrders) {
				WorkOrderSummaryDTO workOrderSummaryDTO = new WorkOrderSummaryDTO();
				workOrderSummaryDTO.setWbsCode(contractWorkOrder.getWbsCode());
				workOrderSummaryDTO.setStartDate(contractWorkOrder.getStartDate());
				workOrderSummaryDTO.setEndDate(contractWorkOrder.getEndDate());
				workOrderSummaryDTO.setStatus(contractWorkOrder.getStatus());
				workOrderSummaryDTO.setWorkOrderID(contractWorkOrder.getWorkOrderID());
				//set client and end client
				if(!contractWorkOrder.getClient().isEmpty()){
					List<ContractClient> contractClients = contractWorkOrder.getClient();
					contractClients.sort(ContractClient.createContractClientLambdaComparator());
					workOrderSummaryDTO.setClient(modelMapper.map(contractClients.get(0).getClient(), RelatedOrgDTO.class));
					workOrderSummaryDTO.setEndClient(modelMapper.map(contractClients.get(contractClients.size()-1).getClient(), RelatedOrgDTO.class));
				} else {
					ContractAccount contractAccount = contractWorkOrder.getContractAccount();
					workOrderSummaryDTO.setClient(modelMapper.map(contractAccount.getContract().getRelatedOrg(),RelatedOrgDTO.class));
					workOrderSummaryDTO.setEndClient(modelMapper.map(contractAccount.getContract().getRelatedOrg(),RelatedOrgDTO.class));
				}
				//set resources
				List<ChargeCodeResourceDTO> contractResources = new ArrayList<>();
				if(!contractWorkOrder.getWorkLocation().isEmpty()){
					for(ContractWorkLocation contractWorkLocation:contractWorkOrder.getWorkLocation()){
						if(!contractWorkLocation.getChargeCodes().isEmpty()){
							for(WorkOrderChargeCode workOrderChargeCode:contractWorkLocation.getChargeCodes()){
								if(!workOrderChargeCode.getContractResources().isEmpty()){
									for(ChargeCodeResource chargeCodeResource :workOrderChargeCode.getContractResources()) {
										contractResources.add(contractModelConverter.convertToDTO(chargeCodeResource));
									}
								}
							}
						}
					}
				}
				workOrderSummaryDTO.setContractResources(contractResources);
				workOrderSummaryList.add(workOrderSummaryDTO);
			}
		}
		return workOrderSummaryList;
	}

	private void updateContractAccountData(ContractAccount contractAccount, ContractAccountDTO contractAccountDTO) {
		if(contractAccountDTO.getStartDate() != null && !contractAccountDTO.getStartDate().equals(contractAccount.getStartDate())){
			LocalDate msaDate=contractAccount.getContract().getStartDate();
			LocalDate minOfWoDate = contractWorkOrderRepo.getMinWoDate(contractAccount.getContractAccountId());
			if(contractAccountDTO.getStartDate().isBefore(msaDate)){
				throw new InvalidDataException(ErrorCodes.CNT_BV_0045,"contractAccount","startDate",contractAccountDTO.getStartDate().toString(),"ContractAccount startDate not be before contract startDate:"+msaDate);
			}
			if(minOfWoDate != null && contractAccountDTO.getStartDate().isAfter(minOfWoDate)){
				throw new InvalidDataException(ErrorCodes.CNT_BV_0045,"contractAccount","startDate",contractAccountDTO.getStartDate().toString(),"ContractAccount startDate not be after workOrder startDate:"+minOfWoDate);
			}
			contractAccount.setStartDate(contractAccountDTO.getStartDate());
		}
		if(!StringUtils.isAllBlank(contractAccountDTO.getContractAccountName())
				&& !contractAccount.getContractAccountName().equals(contractAccountDTO.getContractAccountName())){
			contractAccount.setContractAccountName(contractAccountDTO.getContractAccountName());
		}
		if(!StringUtils.isAllBlank(contractAccountDTO.getNotes()) && (contractAccount.getNotes() == null || !contractAccountDTO.getNotes().equals(contractAccount.getNotes()))){
			contractAccount.setNotes(contractAccountDTO.getNotes());
		}
		if(contractAccountDTO.getContractBillingDetails() != null && !contractAccountDTO.getContractBillingDetails().isEmpty()){
			//first one will be taken remaining will be discarded
			updateContractBillingDetails(contractAccount, contractAccountDTO.getContractBillingDetails().get(0));
		}

		if(contractAccountDTO.getContractExpenses() != null && !contractAccountDTO.getContractExpenses().isEmpty()) {
			List<ContractExpense> contractExpenses = contractAccount.getContractExpenses();
			for(ContractExpenseDTO contractExpenseDTO: contractAccountDTO.getContractExpenses()){
				if(!StringUtils.isAllBlank(contractExpenseDTO.getExpenseId())){
					ContractExpense updatableExpense = null;
					if(contractExpenses == null || contractExpenses.isEmpty()) {
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "ContractExpense by id:" + contractExpenseDTO.getExpenseId() + " not found");
					}
					for(ContractExpense contractExpense:contractExpenses){
						if(!StringUtils.isAllBlank(contractExpense.getExpenseId()) && contractExpenseDTO.getExpenseId().equals(contractExpense.getExpenseId())){
							updatableExpense=contractExpense;
							break;
						}
					}
					if(updatableExpense == null){
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "ContractExpense by id:" + contractExpenseDTO.getExpenseId() + " not found");
					}
					updateExpenseData(updatableExpense,contractExpenseDTO);
				} else{
					ContractExpense contractExpense = contractModelConverter.convertToEntity(contractExpenseDTO);
					contractExpense.setContractAccount(contractAccount);
					if(contractExpenses != null){
						contractExpenses.add(contractExpense);
					} else{
						contractExpenses = new ArrayList<>();
						contractExpenses.add(contractExpense);
					}
				}
			}
			contractAccount.setContractExpenses(contractExpenses);
		}
	}

	private void updateContractBillingDetails(ContractAccount contractAccount, ContractBillingDetailsDTO contractBillingDetailsDTO){
		List<ContractBillingDetails> contractBillingDetails = contractAccount.getContractBillingDetails();
		contractBillingDetails.sort(ContractBillingDetails.createContractBillingLambdaComparator());
		//take latest one from list
		ContractBillingDetails existedBillingDetails = contractBillingDetails.get(contractBillingDetails.size()-1);
		ContractBillingDetails newBillingDetails = contractModelConverter.convertToEntity(contractBillingDetailsDTO);
		if(!existedBillingDetails.equals(newBillingDetails)){
			if(!existedBillingDetails.getStartDate().equals(LocalDate.now())){
				//set end date to previous one
				existedBillingDetails.setEndDate(LocalDate.now().minusDays(1));
				//set workOrderGroup to new billing details
				newBillingDetails.setContractAccount(contractAccount);
				newBillingDetails.setStartDate(LocalDate.now());
				contractBillingDetails.add(newBillingDetails);
			} else{
				existedBillingDetails.setBillPeriodUnits(contractBillingDetailsDTO.getBillPeriodUnits());
				existedBillingDetails.setBillPeriod(contractBillingDetailsDTO.getBillPeriod());
				existedBillingDetails.setBillCycle(contractBillingDetailsDTO.getBillCycle());
				existedBillingDetails.setGracePeriodDays(contractBillingDetailsDTO.getGracePeriodDays());
				existedBillingDetails.setPaymentDueDays(contractBillingDetailsDTO.getPaymentDueDays());
				existedBillingDetails.setExpensesBillBoo(contractBillingDetailsDTO.isExpensesBillBoo());
			}
		}
		contractAccount.setContractBillingDetails(contractBillingDetails);
	}

	private void updateContractWorkOrderData(ContractWorkOrder contractWorkOrder, ContractWorkOrderDTO contractWorkOrderDTO) {
		if (!StringUtils.isAllBlank(contractWorkOrderDTO.getWbsCode())
				&& (StringUtils.isAllBlank(contractWorkOrder.getWbsCode())
				|| !contractWorkOrder.getWbsCode().equals(contractWorkOrderDTO.getWbsCode()))) {
			if(isExistsByWbsCode(contractWorkOrderDTO.getWbsCode())){
				throw new InvalidDataException(ErrorCodes.CNT_BV_0009,"contractWorkOrder","wbsCode",contractWorkOrderDTO.getWbsCode(),"WorkOrder already exists by wbsCode:"+contractWorkOrderDTO.getWbsCode());
			}
			contractWorkOrder.setWbsCode(contractWorkOrderDTO.getWbsCode());
		}
		if(contractWorkOrderDTO.getWorkOrderName() != null){
			contractWorkOrder.setWorkOrderName(contractWorkOrderDTO.getWorkOrderName());
		}
		if(contractWorkOrderDTO.getWorkOrderDesc() != null){
			contractWorkOrder.setWorkOrderDesc(contractWorkOrderDTO.getWorkOrderDesc());
		}
		if(contractWorkOrderDTO.getWorkOrderType() != null && !contractWorkOrder.getWorkOrderType().equals(contractWorkOrderDTO.getWorkOrderType())){
			contractWorkOrder.setWorkOrderType(contractWorkOrderDTO.getWorkOrderType());
		}
		if(contractWorkOrderDTO.getMultiResource() !=contractWorkOrder.getMultiResource()){
			contractWorkOrder.setMultiResource(contractWorkOrderDTO.getMultiResource());
		}
		if(contractWorkOrderDTO.getBlendedRateMny() >0 && contractWorkOrder.getBlendedRateMny() != contractWorkOrderDTO.getBlendedRateMny()){
			contractWorkOrder.setBlendedRateMny(contractWorkOrderDTO.getBlendedRateMny());
		}
		if(contractWorkOrderDTO.getStartDate() != null &&  !contractWorkOrder.getStartDate().equals(contractWorkOrderDTO.getStartDate())){
			if(contractWorkOrder.getEndDate() != null && contractWorkOrderDTO.getStartDate().isAfter(contractWorkOrder.getEndDate())){
				log.warn("WorkOrder startDate not be after workOrder endDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0018,"contractWorkOrder","startDate",contractWorkOrderDTO.getStartDate().toString(),"WorkOrder startDate not be after workOrder endDate");
			}
			contractWorkOrder.setStartDate(contractWorkOrderDTO.getStartDate());
		}
		if(contractWorkOrderDTO.getEndDate() != null && ( contractWorkOrder.getEndDate() == null || !contractWorkOrder.getEndDate().equals(contractWorkOrderDTO.getEndDate()))){
			if(contractWorkOrder.getEndDate() != null && contractWorkOrderDTO.getEndDate().isBefore(contractWorkOrder.getStartDate())){
				log.warn("WorkOrder endDate not be befor workOrder startDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0019,"contractWorkOrder","endDate",contractWorkOrderDTO.getEndDate().toString(),"WorkOrder endDate not be befor workOrder startDate");
			}
			contractWorkOrder.setEndDate(contractWorkOrderDTO.getEndDate());
		}
		if(contractWorkOrderDTO.getStatus() != null && !contractWorkOrder.getStatus().equals(contractWorkOrderDTO.getStatus())){
			contractWorkOrder.setStatus(contractWorkOrderDTO.getStatus());
		}
		//update supporting charges
		if(contractWorkOrderDTO.getSuppCharges() != null && !contractWorkOrderDTO.getSuppCharges().isEmpty()){
            List<WorkOrderSuppCharges> suppCharges = contractWorkOrder.getSuppCharges();
			for(WorkOrderSuppChargesDTO workOrderSuppChargesDTO:contractWorkOrderDTO.getSuppCharges()){
				if(!StringUtils.isAllBlank(workOrderSuppChargesDTO.getWoSupChargeId())){
					WorkOrderSuppCharges updatableSppCharge=null;
					if(suppCharges == null || suppCharges.isEmpty()) {
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "workOrderSuppCharges by id:" + workOrderSuppChargesDTO.getWoSupChargeId() + " not found");
					}
					for(WorkOrderSuppCharges workOrderSuppCharges:suppCharges){
						if(!StringUtils.isAllBlank(workOrderSuppCharges.getWoSupChargeId()) && workOrderSuppCharges.getWoSupChargeId().equals(workOrderSuppChargesDTO.getWoSupChargeId())){
							updatableSppCharge=workOrderSuppCharges;
							break;
						}
					}
					if(updatableSppCharge == null){
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "workOrderSuppCharges by id:" + workOrderSuppChargesDTO.getWoSupChargeId() + " not found");
					}
					updateSuppCharge(updatableSppCharge,workOrderSuppChargesDTO);
				} else{
					WorkOrderSuppCharges workOrderSuppCharges = contractModelConverter.convertToEntity(workOrderSuppChargesDTO);
					workOrderSuppCharges.setWorkOrder(contractWorkOrder);
					if(suppCharges != null){
						suppCharges.add(workOrderSuppCharges);
					} else{
						suppCharges = new ArrayList<>();
						suppCharges.add(workOrderSuppCharges);
					}
				}
				contractWorkOrder.setSuppCharges(suppCharges);
			}
		}
		//update contract clients
		if(contractWorkOrderDTO.getClient() != null && !contractWorkOrderDTO.getClient().isEmpty()){
			List<ContractClient> contractClients = contractWorkOrder.getClient();
			for(ContractClientDTO contractClientDTO:contractWorkOrderDTO.getClient()){
				if(!StringUtils.isAllBlank(contractClientDTO.getContractClientID())){
					ContractClient updatableClient = null;
					if(contractClients == null || contractClients.isEmpty()){
						log.error("ContractClint by id:" + contractClientDTO.getContractClientID() + " not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "ContractClint by id:" + contractClientDTO.getContractClientID() + " not found");
					}
					for(ContractClient contractClient:contractClients){
						if(!StringUtils.isAllBlank(contractClient.getContractClientID()) && contractClient.getContractClientID().equals(contractClientDTO.getContractClientID())){
							updatableClient = contractClient;
							break;
						}
					}
					if(updatableClient == null){
						log.error("ContractClint by id:" + contractClientDTO.getContractClientID() + " not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "ContractClint by id:" + contractClientDTO.getContractClientID() + " not found");
					}
					updateClientData(updatableClient,contractClientDTO);
				} else{
					ContractClient contractClient = contractModelConverter.convertToEntity(contractClientDTO);
					contractClient.setWorkOrder(contractWorkOrder);
					if(contractClients != null){
						contractClients.add(contractClient);
					} else{
						contractClients = new ArrayList<>();
						contractClients.add(contractClient);
					}
				}
			}
			contractClients.sort(ContractClient.createContractClientLambdaComparator());
			contractValidator.validateContractClients(contractClients);
			contractWorkOrder.setClient(contractClients);
		}

		//update workLocation
		if(contractWorkOrderDTO.getWorkLocation() != null && !contractWorkOrderDTO.getWorkLocation().isEmpty()){
			List<ContractWorkLocation> contractWorkLocations = contractWorkOrder.getWorkLocation();
			for(ContractWorkLocationDTO contractWorkLocationDTO:contractWorkOrderDTO.getWorkLocation()){
				if(!StringUtils.isAllBlank(contractWorkLocationDTO.getContractwlID())){
					ContractWorkLocation updatableLocation = null;
                   if(contractWorkLocations == null || contractWorkLocations.isEmpty()){
					   log.error("ContractWorkLocation by id:"+contractWorkLocationDTO.getContractwlID()+" not found");
					   throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractWorkLocation by id:"+contractWorkLocationDTO.getContractwlID()+" not found");
				   }
					for(ContractWorkLocation contractWorkLocation:contractWorkLocations){
						if(!StringUtils.isAllBlank(contractWorkLocation.getContractwlID()) && contractWorkLocation.getContractwlID().equals(contractWorkLocationDTO.getContractwlID())){
							updatableLocation = contractWorkLocation;
							break;
						}
					}
					if(updatableLocation == null){
						log.error("ContractWorkLocation by id:"+contractWorkLocationDTO.getContractwlID()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractWorkLocation by id:"+contractWorkLocationDTO.getContractwlID()+" not found");
					}
					updateContractWorkLocation(updatableLocation,contractWorkLocationDTO);
				} else{
					ContractWorkLocation contractWorkLocation = createWorkLocation(contractWorkLocationDTO);
					contractWorkLocation.setWorkOrder(contractWorkOrder);
					if(contractWorkLocations != null) {
						contractWorkLocations.add(contractWorkLocation);
					} else{
						contractWorkLocations = new ArrayList<>();
						contractWorkLocations.add(contractWorkLocation);
					}
				}
			}
			contractWorkOrder.setWorkLocation(contractWorkLocations);
		}
		//update workOrderResource
		if(contractWorkOrderDTO.getWorkOrderResources() != null && !contractWorkOrderDTO.getWorkOrderResources().isEmpty()){
			List<WorkOrderResource> workOrderResources = contractWorkOrder.getWorkOrderResources();
			for(WorkOrderResourceDTO workOrderResourceDTO:contractWorkOrderDTO.getWorkOrderResources()){
				if(!StringUtils.isEmpty(workOrderResourceDTO.getWoResourceID())){
					WorkOrderResource extWoResource = null;
					if(workOrderResources == null || workOrderResources.isEmpty()){
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrderResource by id:"+workOrderResourceDTO.getWoResourceID()+" not found");
					}
					for(WorkOrderResource workOrderResource:workOrderResources){
						if(workOrderResource.getWoResourceID() != null && workOrderResource.getWoResourceID().equals(workOrderResourceDTO.getWoResourceID())){
							extWoResource = workOrderResource;
							break;
						}
					}
					if(extWoResource == null){
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrderResource by id:"+workOrderResourceDTO.getWoResourceID()+" not found");
					}
					updateWorkOrderResource(extWoResource,workOrderResourceDTO);
				} else{
					WorkOrderResource workOrderResource = createWorkOrderResource(workOrderResourceDTO);
					workOrderResource.setWorkOrder(contractWorkOrder);
					workOrderResources.add(workOrderResource);
				}
			}
			contractWorkOrder.setWorkOrderResources(workOrderResources);
		}
	}

	private void updateWorkOrderResource(WorkOrderResource extWoResource, WorkOrderResourceDTO workOrderResourceDTO) {
		if(workOrderResourceDTO.getRemoteWorkLoc() != null && !workOrderResourceDTO.getRemoteWorkLoc().equals(extWoResource.getRemoteWorkLoc())){
			extWoResource.setRemoteWorkLoc(workOrderResourceDTO.getRemoteWorkLoc());
		}
		if(!StringUtils.isEmpty(workOrderResourceDTO.getWorkEmail()) && (extWoResource.getWorkEmail() == null || !extWoResource.getWorkEmail().equals(workOrderResourceDTO.getWorkEmail()))){
			extWoResource.setWorkEmail(workOrderResourceDTO.getWorkEmail());
		}
		if(!StringUtils.isEmpty(workOrderResourceDTO.getWorkPhone()) && (extWoResource.getWorkPhone() == null || !extWoResource.getWorkPhone().equals(workOrderResourceDTO.getWorkPhone()))){
			extWoResource.setWorkPhone(workOrderResourceDTO.getWorkPhone());
		}
		if(workOrderResourceDTO.getStartDate() != null &&  !extWoResource.getStartDate().equals(workOrderResourceDTO.getStartDate())){
			if(extWoResource.getEndDate() != null && workOrderResourceDTO.getStartDate().isAfter(extWoResource.getEndDate())){
				log.warn("WorkOrderResource startDate not be after workOrderResource endDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0029,"workOrderResource","startDate",workOrderResourceDTO.getStartDate().toString(),"WorkOrderResource startDate not be after workOrderResource endDate");
			}
			extWoResource.setStartDate(workOrderResourceDTO.getStartDate());
		}
		if(workOrderResourceDTO.getEndDate() != null &&  !extWoResource.getEndDate().equals(workOrderResourceDTO.getEndDate())){
			if(extWoResource.getStartDate() != null && workOrderResourceDTO.getEndDate().isBefore(extWoResource.getStartDate())){
				log.warn("WorkOrderResource endDate not be before workOrderResource startDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0030,"workOrderResource","endDate",workOrderResourceDTO.getEndDate().toString(),"WorkOrderResource endDate not be before workOrderResource startDate");
			}
			extWoResource.setEndDate(workOrderResourceDTO.getEndDate());
		}
		if(workOrderResourceDTO.getWorkerType() != null && !workOrderResourceDTO.getWorkerType().isEmpty()){
			List<ContractResourceWorkerType> workerTypes = extWoResource.getWorkerType();
			workerTypes.sort(ContractResourceWorkerType.createContractResourceWokerTypeComparator());
			ContractResourceWorkerType latestWorkerType = workerTypes.get(workerTypes.size()-1);
			//first one will be taken remaining will be discarded
			if(!latestWorkerType.getWorkerType().getWorkerTypeCode().equals(workOrderResourceDTO.getWorkerType().get(0).getWorkerType().getWorkerTypeCode())){
				if(!latestWorkerType.getStartDate().equals(LocalDate.now())){
					latestWorkerType.setEndDate(LocalDate.now().minusDays(1));
					ContractResourceWorkerType newWorkerType= contractModelConverter.convertToEntity(workOrderResourceDTO.getWorkerType().get(0));
					newWorkerType.setStartDate(LocalDate.now());
					newWorkerType.setWorkOrderResource(extWoResource);
					workerTypes.add(newWorkerType);
				} else{
					latestWorkerType.setWorkerType(modelMapper.map(workOrderResourceDTO.getWorkerType().get(0).getWorkerType(), WorkerType.class));
				}
			}
			extWoResource.setWorkerType(workerTypes);
		}
		//update supervisor
		if(workOrderResourceDTO.getSupervisors() != null && !workOrderResourceDTO.getSupervisors().isEmpty()){
			List<ContractSupervisor> contractSupervisors = extWoResource.getSupervisors();
			for(ContractSupervisorDTO contractSupervisorDTO:workOrderResourceDTO.getSupervisors()){
				if(!StringUtils.isAllBlank(contractSupervisorDTO.getContractSupervisorID())){
					ContractSupervisor updatableSupervisor = null;
					if(contractSupervisors == null || contractSupervisors.isEmpty()){
						log.error("ContractSupervisor by id:"+contractSupervisorDTO.getContractSupervisorID()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractSupervisor by id:"+contractSupervisorDTO.getContractSupervisorID()+" not found");
					}
					for(ContractSupervisor contractSupervisor:contractSupervisors){
						if(!StringUtils.isAllBlank(contractSupervisor.getContractSupervisorID()) && contractSupervisor.getContractSupervisorID().equals(contractSupervisorDTO.getContractSupervisorID())){
							updatableSupervisor =  contractSupervisor;
							break;
						}
					}
					if(updatableSupervisor == null){
						log.error("ContractSupervisor by id:"+contractSupervisorDTO.getContractSupervisorID()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractSupervisor by id:"+contractSupervisorDTO.getContractSupervisorID()+" not found");
					}
					updateContractSupervisor(updatableSupervisor,contractSupervisorDTO);
				} else{
					ContractSupervisor contractSupervisor = contractModelConverter.convertToEntity(contractSupervisorDTO);
					contractSupervisor.setWorkOrderResource(extWoResource);
					if(contractSupervisors == null){
						contractSupervisors = new ArrayList<>();
					}
					contractSupervisors.add(contractSupervisor);
				}
			}
			contractSupervisors.sort(ContractSupervisor.contractSupervisorLambdaComparator());
			contractValidator.validateContractSuperVisors(contractSupervisors);
			extWoResource.setSupervisors(contractSupervisors);
		}
	}

	private void updateContractWorkLocation(ContractWorkLocation updatableLocation, ContractWorkLocationDTO contractWorkLocationDTO){
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getWorkLocationName()) && !updatableLocation.getWorkLocationName().equals(contractWorkLocationDTO.getWorkLocationName())){
			updatableLocation.setWorkLocationName(contractWorkLocationDTO.getWorkLocationName());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getAddress1())
				&&(StringUtils.isAllBlank(updatableLocation.getAddress1()) || !updatableLocation.getAddress1().equals(contractWorkLocationDTO.getAddress1()))){
			updatableLocation.setAddress1(contractWorkLocationDTO.getAddress1());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getAddress2())
				&&(StringUtils.isAllBlank(updatableLocation.getAddress2()) || !updatableLocation.getAddress2().equals(contractWorkLocationDTO.getAddress2()))){
			updatableLocation.setAddress2(contractWorkLocationDTO.getAddress2());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getAddress3())
				&&(StringUtils.isAllBlank(updatableLocation.getAddress3()) || !updatableLocation.getAddress3().equals(contractWorkLocationDTO.getAddress3()))){
			updatableLocation.setAddress3(contractWorkLocationDTO.getAddress3());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getAddress4())
				&&(StringUtils.isAllBlank(updatableLocation.getAddress4()) || !updatableLocation.getAddress4().equals(contractWorkLocationDTO.getAddress4()))){
			updatableLocation.setAddress4(contractWorkLocationDTO.getAddress4());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getAddress5())
				&&(StringUtils.isAllBlank(updatableLocation.getAddress5()) || !updatableLocation.getAddress5().equals(contractWorkLocationDTO.getAddress5()))){
			updatableLocation.setAddress5(contractWorkLocationDTO.getAddress5());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getCity())
				&&(StringUtils.isAllBlank(updatableLocation.getCity()) || !updatableLocation.getCity().equals(contractWorkLocationDTO.getCity()))){
			updatableLocation.setCity(contractWorkLocationDTO.getCity());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getState())
				&&(StringUtils.isAllBlank(updatableLocation.getState()) || !updatableLocation.getState().equals(contractWorkLocationDTO.getState()))){
			updatableLocation.setState(contractWorkLocationDTO.getState());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getPostalCode())
				&&(StringUtils.isAllBlank(updatableLocation.getPostalCode()) || !updatableLocation.getPostalCode().equals(contractWorkLocationDTO.getPostalCode()))){
			updatableLocation.setPostalCode(contractWorkLocationDTO.getPostalCode());
		}
		if(!StringUtils.isAllBlank(contractWorkLocationDTO.getPostOfficeBox())
				&&(StringUtils.isAllBlank(updatableLocation.getPostOfficeBox()) || !updatableLocation.getPostOfficeBox().equals(contractWorkLocationDTO.getPostOfficeBox()))){
			updatableLocation.setPostOfficeBox(contractWorkLocationDTO.getPostOfficeBox());
		}
		if(contractWorkLocationDTO.getGeoCode() != null){
			GeoCode geoCode = updatableLocation.getGeoCode();
			if(geoCode != null){
                if(!StringUtils.isAllBlank(contractWorkLocationDTO.getGeoCode().getName())){
					geoCode.setName(contractWorkLocationDTO.getGeoCode().getName());
				}
				if(contractWorkLocationDTO.getGeoCode().getAltitude() != null){
					geoCode.setAltitude(contractWorkLocationDTO.getGeoCode().getLatitude());
				}
				if(contractWorkLocationDTO.getGeoCode().getLatitude() != null){
					geoCode.setLatitude(contractWorkLocationDTO.getGeoCode().getLatitude());
				}
				if(contractWorkLocationDTO.getGeoCode().getLongitude() != null){
					geoCode.setLongitude(contractWorkLocationDTO.getGeoCode().getLongitude());
				}
			} else{
				geoCode = modelMapper.map(contractWorkLocationDTO.getGeoCode(),GeoCode.class);
			}
			updatableLocation.setGeoCode(geoCode);
		}

		if(contractWorkLocationDTO.getChargeCodes() != null && !contractWorkLocationDTO.getChargeCodes().isEmpty()){
			List<WorkOrderChargeCode> workOrderChargeCodes = updatableLocation.getChargeCodes();
			for(WorkOrderChargeCodeDTO workOrderChargeCodeDTO:contractWorkLocationDTO.getChargeCodes()){
				if(!StringUtils.isAllBlank(workOrderChargeCodeDTO.getChargeCodeId())){
					WorkOrderChargeCode updatableChargeCode=null;
					if(workOrderChargeCodes == null || workOrderChargeCodes.isEmpty()){
						log.error("WorkOrderChargeCode by id:"+workOrderChargeCodeDTO.getChargeCodeId()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrderChargeCode by id:"+workOrderChargeCodeDTO.getChargeCodeId()+" not found");
					}
					for(WorkOrderChargeCode workOrderChargeCode:workOrderChargeCodes){
						if(!StringUtils.isAllBlank(workOrderChargeCode.getChargeCodeId())
								&& workOrderChargeCode.getChargeCodeId().equals(workOrderChargeCodeDTO.getChargeCodeId())){
							updatableChargeCode=workOrderChargeCode;
							break;
						}
					}
					if(updatableChargeCode == null){
						log.error("WorkOrderChargeCode by id:"+workOrderChargeCodeDTO.getChargeCodeId()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrderChargeCode by id:"+workOrderChargeCodeDTO.getChargeCodeId()+" not found");
					}
					updateWorkOrderChargeCode(updatableChargeCode,workOrderChargeCodeDTO);
				} else{
					WorkOrderChargeCode workOrderChargeCode = createWorkOrderChargeCode(workOrderChargeCodeDTO);
					workOrderChargeCode.setContractWorkLocation(updatableLocation);
					if(workOrderChargeCodes != null){
						workOrderChargeCodes.add(workOrderChargeCode);
					} else{
						workOrderChargeCodes = new ArrayList<>();
						workOrderChargeCodes.add(workOrderChargeCode);
					}
				}
			}
			updatableLocation.setChargeCodes(workOrderChargeCodes);
		}
	}

	private void updateWorkOrderChargeCode(WorkOrderChargeCode updatableChargeCode, WorkOrderChargeCodeDTO workOrderChargeCodeDTO) {
		if(!StringUtils.isAllBlank(workOrderChargeCodeDTO.getChargeCodeName()) && !updatableChargeCode.getChargeCodeName().equals(workOrderChargeCodeDTO.getChargeCodeName())){
			updatableChargeCode.setChargeCodeName(workOrderChargeCodeDTO.getChargeCodeName());
		}
		if(workOrderChargeCodeDTO.getStartDate() != null && !updatableChargeCode.getStartDate().equals(workOrderChargeCodeDTO.getStartDate())){
			if(updatableChargeCode.getEndDate() != null && workOrderChargeCodeDTO.getStartDate().isAfter(updatableChargeCode.getEndDate())){
				log.warn("WorkOrderChargeCode startDate not be after workOrderChargeCode endDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0020,"workOrderChargeCode","startDate",workOrderChargeCodeDTO.getStartDate().toString(),"WorkOrderChargeCode startDate not be after workOrderChargeCode endDate");
			}
			updatableChargeCode.setStartDate(workOrderChargeCodeDTO.getStartDate());
		}
		if(workOrderChargeCodeDTO.getEndDate() != null && !updatableChargeCode.getEndDate().equals(workOrderChargeCodeDTO.getEndDate())){
			if(updatableChargeCode.getStartDate() != null && workOrderChargeCodeDTO.getEndDate().isBefore(updatableChargeCode.getStartDate())){
				log.warn("WorkOrderChargeCode endDate not be before workOrderChargeCode startDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0021,"workOrderChargeCode","endDate",workOrderChargeCodeDTO.getEndDate().toString(),"WorkOrderChargeCode endDate not be before workOrderChargeCode startDate");
			}
			updatableChargeCode.setEndDate(workOrderChargeCodeDTO.getEndDate());
		}
		//update WorkOrderRates
		if(workOrderChargeCodeDTO.getWorkOrderRates() != null && !workOrderChargeCodeDTO.getWorkOrderRates().isEmpty()){
			List<ChargeCodeRate> chargeCodeRates = updatableChargeCode.getWorkOrderRates();
			for(ChargeCodeRateDTO chargeCodeRateDTO:workOrderChargeCodeDTO.getWorkOrderRates()){
				if(!StringUtils.isAllBlank(chargeCodeRateDTO.getWoRateID())){
					ChargeCodeRate updatableChargeCodeRate = null;
					if(chargeCodeRates == null || chargeCodeRates.isEmpty()){
						log.error("ChargeCodeRate by id:"+chargeCodeRateDTO.getWoRateID()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ChargeCodeRate by id:"+chargeCodeRateDTO.getWoRateID()+" not found");
					}
					for(ChargeCodeRate chargeCodeRate:chargeCodeRates){
						if(!StringUtils.isAllBlank(chargeCodeRate.getWoRateID()) && chargeCodeRate.getWoRateID().equals(chargeCodeRateDTO.getWoRateID())){
							updatableChargeCodeRate=chargeCodeRate;
							break;
						}
					}
					if(updatableChargeCodeRate== null){
						log.error("ChargeCodeRate by id:"+chargeCodeRateDTO.getWoRateID()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ChargeCodeRate by id:"+chargeCodeRateDTO.getWoRateID()+" not found");
					}
					updateChargeCodeRate(updatableChargeCodeRate,chargeCodeRateDTO);
				} else{
					ChargeCodeRate chargeCodeRate = contractModelConverter.convertToEntity(chargeCodeRateDTO);
					chargeCodeRate.setWoChrageCode(updatableChargeCode);
					if(chargeCodeRates == null){
						chargeCodeRates = new ArrayList<>();
					}
					chargeCodeRates.add(chargeCodeRate);
				}
			}
			chargeCodeRates.sort(ChargeCodeRate.chargeCodeRateLambdaComparator());
			contractValidator.validateChargeCodeRates(chargeCodeRates);
			updatableChargeCode.setWorkOrderRates(chargeCodeRates);
		}

        //update charge code tasks
		if(workOrderChargeCodeDTO.getChargeCodeTasks() != null && !workOrderChargeCodeDTO.getChargeCodeTasks().isEmpty()){
			List<ChargeCodeTasks> chargeCodeTasks = updatableChargeCode.getChargeCodeTasks();
			for(ChargeCodeTasksDTO chargeCodeTasksDTO:workOrderChargeCodeDTO.getChargeCodeTasks()){
				if(!StringUtils.isAllBlank(chargeCodeTasksDTO.getTaskId())){
					ChargeCodeTasks updatableTask = null;
					if(chargeCodeTasks == null || chargeCodeTasks.isEmpty()) {
						log.error("ChargeCodeTask by id:"+chargeCodeTasksDTO.getTaskId()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ChargeCodeTask by id:"+chargeCodeTasksDTO.getTaskId()+" not found");
					}
					for(ChargeCodeTasks chargeCodeTask:chargeCodeTasks){
						if(!StringUtils.isAllBlank(chargeCodeTask.getTaskId()) && chargeCodeTask.getTaskId().equals(chargeCodeTasksDTO.getTaskId())){
							updatableTask = chargeCodeTask;
							break;
						}
					}
					if(updatableTask == null){
						log.error("ChargeCodeTask by id:"+chargeCodeTasksDTO.getTaskId()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ChargeCodeTask by id:"+chargeCodeTasksDTO.getTaskId()+" not found");
					}
					updateChargeCodeTask(updatableTask,chargeCodeTasksDTO);
                }
                else{
                    ChargeCodeTasks chargeCodeTask = contractModelConverter.convertToEntity(chargeCodeTasksDTO);
                    chargeCodeTask.setWoChrageCode(updatableChargeCode);
                    if(chargeCodeTasks == null){
                        chargeCodeTasks = new ArrayList<>();
                    }
                    chargeCodeTasks.add(chargeCodeTask);
                }
			}
		}

		//update woExpenseCodes
		if(workOrderChargeCodeDTO.getWorkOrderExpenses() != null && !workOrderChargeCodeDTO.getWorkOrderExpenses().isEmpty()){
			List<WorkOrderExpenseCodes> workOrderExpenseCodes = updatableChargeCode.getWorkOrderExpenses();
			for(WorkOrderExpenseCodesDTO workOrderExpenseCodesDTO : workOrderChargeCodeDTO.getWorkOrderExpenses()){
				if(!StringUtils.isAllBlank(workOrderExpenseCodesDTO.getExpenseCodeId())){
					WorkOrderExpenseCodes updatableWoExpenseCode = null;
					if(workOrderExpenseCodes == null || workOrderExpenseCodes.isEmpty()){
						log.error("WorkOrderExpenseCode by id:"+workOrderExpenseCodesDTO.getExpenseCodeId()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrderExpenseCode by id:"+workOrderExpenseCodesDTO.getExpenseCodeId()+" not found");
					}
					for(WorkOrderExpenseCodes workOrderExpenseCode:workOrderExpenseCodes){
						if(!StringUtils.isAllBlank(workOrderExpenseCode.getExpenseCodeId()) && workOrderExpenseCode.getExpenseCodeId().equals(workOrderExpenseCodesDTO.getExpenseCodeId())){
							updatableWoExpenseCode = workOrderExpenseCode;
							break;
						}
					}
					if(updatableWoExpenseCode == null){
						log.error("WorkOrderExpenseCode by id:"+workOrderExpenseCodesDTO.getExpenseCodeId()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrderExpenseCode by id:"+workOrderExpenseCodesDTO.getExpenseCodeId()+" not found");
					}
					updateWoExpenseCode(updatableWoExpenseCode,workOrderExpenseCodesDTO);
				} else{
					WorkOrderExpenseCodes  workOrderExpenseCode = contractModelConverter.convertToEntity(workOrderExpenseCodesDTO);
					workOrderExpenseCode.setWoChrageCode(updatableChargeCode);
					if(workOrderExpenseCodes == null){
						workOrderExpenseCodes = new ArrayList<>();
					}
					workOrderExpenseCodes.add(workOrderExpenseCode);
				}
			}
		}

		//update contract resource
		if(workOrderChargeCodeDTO.getContractResources() != null && !workOrderChargeCodeDTO.getContractResources().isEmpty()){
			List<ChargeCodeResource> chargeCodeResources = updatableChargeCode.getContractResources();
			for(ChargeCodeResourceDTO chargeCodeResourceDTO :workOrderChargeCodeDTO.getContractResources()){
				if(!StringUtils.isAllBlank(chargeCodeResourceDTO.getContractResourceID())){
					ChargeCodeResource updatableResource = null;
					if(chargeCodeResources == null || chargeCodeResources.isEmpty()){
						log.error("ContractResource by id:"+ chargeCodeResources +" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractResource by id:"+ chargeCodeResources +" not found");
					}
					for(ChargeCodeResource chargeCodeResource : chargeCodeResources){
						if(!StringUtils.isAllBlank(chargeCodeResource.getContractResourceID()) && chargeCodeResource.getContractResourceID().equals(chargeCodeResourceDTO.getContractResourceID())){
							updatableResource= chargeCodeResource;
							break;
						}
					}
					if(updatableResource == null){
						log.error("ContractResource by id:"+ chargeCodeResources +" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractResource by id:"+ chargeCodeResources +" not found");
					}
					updateContractResource(updatableResource, chargeCodeResourceDTO);
				} else{
					ChargeCodeResource chargeCodeResource = createChargeCodeResource(chargeCodeResourceDTO);
					chargeCodeResource.setWoChrageCode(updatableChargeCode);
					if(chargeCodeResources == null){
						chargeCodeResources = new ArrayList<>();
					}
					chargeCodeResources.add(chargeCodeResource);
				}
			}
		}
	}

	private void updateContractResource(ChargeCodeResource updatableResource, ChargeCodeResourceDTO chargeCodeResourceDTO) {
		if(chargeCodeResourceDTO.getStartDate() != null && !chargeCodeResourceDTO.getStartDate().equals(updatableResource.getStartDate())){
			if(updatableResource.getEndDate() != null && chargeCodeResourceDTO.getStartDate().isAfter(updatableResource.getEndDate())){
				log.warn("ContractResource startDate not be after contractResource endDate");
				throw  new InvalidDataException(ErrorCodes.CNT_BV_0022,"contractResource","startDate", chargeCodeResourceDTO.getStartDate().toString(),"ContractResource startDate not be after contractResource endDate");
			}
			updatableResource.setStartDate(chargeCodeResourceDTO.getStartDate());
		}
		if(chargeCodeResourceDTO.getEndDate() != null && (updatableResource.getEndDate() == null || !chargeCodeResourceDTO.getEndDate().equals(updatableResource.getEndDate()))){
			if(updatableResource.getStartDate() != null && chargeCodeResourceDTO.getEndDate().isBefore(updatableResource.getStartDate())){
				log.warn("ContractResource endDate not be before contractResource startDate");
				throw  new InvalidDataException(ErrorCodes.CNT_BV_0023,"contractResource","endDate", chargeCodeResourceDTO.getEndDate().toString(),"ContractResource endDate not be before contractResource startDate");
			}
			updatableResource.setEndDate(chargeCodeResourceDTO.getEndDate());
		}
		if(chargeCodeResourceDTO.getPayProfile() != null && !chargeCodeResourceDTO.getPayProfile().isEmpty()){
			updatePayProfile(updatableResource, chargeCodeResourceDTO.getPayProfile().get(0));
		}
	}

	private void updatePayProfile(ChargeCodeResource chargeCodeResource, WorkOrderExpenseBudgetDTO workOrderExpenseBudgetDTO) {

		List<WorkOrderExpenseBudget> workOrderExpenseBudgets = chargeCodeResource.getPayProfile();
		workOrderExpenseBudgets.sort(WorkOrderExpenseBudget.createExpenseBudgetComparatorLambda());
		if(!workOrderExpenseBudgets.isEmpty()){
			WorkOrderExpenseBudget existedExpBudget = workOrderExpenseBudgets.get(workOrderExpenseBudgets.size()-1);
			WorkOrderExpenseBudget newExpBudget = contractModelConverter.convertToEntity(workOrderExpenseBudgetDTO);
			if(!existedExpBudget.equals(newExpBudget)){
				if( !existedExpBudget.getStartDate().equals(LocalDate.now())){
					existedExpBudget.setEndDate(LocalDate.now().minusDays(1));
					newExpBudget.setStartDate(LocalDate.now());
					newExpBudget.setContractResource(chargeCodeResource);
					workOrderExpenseBudgets.add(newExpBudget);
				} else{
					if(newExpBudget.getRate() != existedExpBudget.getRate()){
						existedExpBudget.setRate(newExpBudget.getRate());
					}
					if(newExpBudget.getRateFrequency() != null && !existedExpBudget.getRateFrequency().equals(newExpBudget.getRateFrequency())){
						existedExpBudget.setRateFrequency(newExpBudget.getRateFrequency());
					}
					if(!StringUtils.isAllBlank(newExpBudget.getRateDesc()) &&(StringUtils.isAllBlank(existedExpBudget.getRateDesc()) || !existedExpBudget.getRateDesc().equals(newExpBudget.getRateDesc()))){
						existedExpBudget.setRateDesc(newExpBudget.getRateDesc());
					}
				}

			}
		} else{
			WorkOrderExpenseBudget workOrderExpenseBudget = contractModelConverter.convertToEntity(workOrderExpenseBudgetDTO);
			workOrderExpenseBudget.setStartDate(LocalDate.now());
			workOrderExpenseBudget.setContractResource(chargeCodeResource);
			workOrderExpenseBudgets.add(workOrderExpenseBudget);
		}
		chargeCodeResource.setPayProfile(workOrderExpenseBudgets);
	}

	private void updateContractSupervisor(ContractSupervisor updatableSupervisor, ContractSupervisorDTO contractSupervisorDTO) {
		if(!StringUtils.isAllBlank(contractSupervisorDTO.getFirstName())
				&& (StringUtils.isAllBlank(updatableSupervisor.getFirstName()) || !updatableSupervisor.getFirstName().equals(contractSupervisorDTO.getFirstName()))){
			updatableSupervisor.setFirstName(updatableSupervisor.getFirstName());
		}
		if(!StringUtils.isAllBlank(contractSupervisorDTO.getLastName())
				&& (StringUtils.isAllBlank(updatableSupervisor.getLastName()) || !updatableSupervisor.getLastName().equals(contractSupervisorDTO.getLastName()))){
			updatableSupervisor.setLastName(updatableSupervisor.getLastName());
		}
		if(!StringUtils.isAllBlank(contractSupervisorDTO.getEmail())
				&& (StringUtils.isAllBlank(updatableSupervisor.getEmail()) || !updatableSupervisor.getEmail().equals(contractSupervisorDTO.getEmail()))){
			updatableSupervisor.setEmail(updatableSupervisor.getEmail());
		}
		if(!StringUtils.isAllBlank(contractSupervisorDTO.getPhone())
				&& (StringUtils.isAllBlank(updatableSupervisor.getPhone()) || !updatableSupervisor.getPhone().equals(contractSupervisorDTO.getPhone()))){
			updatableSupervisor.setPhone(updatableSupervisor.getPhone());
		}
		if(contractSupervisorDTO.getStartDate() != null && !contractSupervisorDTO.getStartDate().equals(updatableSupervisor.getStartDate())){
			if(updatableSupervisor.getEndDate() != null && contractSupervisorDTO.getStartDate().isAfter(updatableSupervisor.getEndDate())){
				throw  new InvalidDataException(ErrorCodes.CNT_BV_0009,"contractSupervisor","startDate",contractSupervisorDTO.getStartDate().toString(),"ContractSupervisor startDate not be after endDate of contractSupervisor");
			}
			updatableSupervisor.setStartDate(contractSupervisorDTO.getStartDate());
		}
		if(contractSupervisorDTO.getEndDate() != null && (updatableSupervisor.getEndDate() == null || !contractSupervisorDTO.getEndDate().equals(updatableSupervisor.getEndDate()))){
			if(updatableSupervisor.getStartDate() != null && contractSupervisorDTO.getEndDate().isBefore(updatableSupervisor.getStartDate())){
				throw  new InvalidDataException(ErrorCodes.CNT_BV_0009,"contractSupervisor","endDate",contractSupervisorDTO.getEndDate().toString(),"ContractSupervisor endDate not be before  startDate of contractSupervisor");
			}
			updatableSupervisor.setEndDate(contractSupervisorDTO.getEndDate());
		}

	}

	private void updateWoExpenseCode(WorkOrderExpenseCodes updatableWoExpenseCode, WorkOrderExpenseCodesDTO workOrderExpenseCodesDTO){
		if(!StringUtils.isAllBlank(workOrderExpenseCodesDTO.getExpenseCodeName()) && !updatableWoExpenseCode.getExpenseCodeName().equals(workOrderExpenseCodesDTO.getExpenseCodeName())){
			updatableWoExpenseCode.setExpenseCodeName(workOrderExpenseCodesDTO.getExpenseCodeName());
		}
		if(workOrderExpenseCodesDTO.getStartDate() != null && !workOrderExpenseCodesDTO.getStartDate().equals(updatableWoExpenseCode.getStartDate())){
			if(updatableWoExpenseCode.getEndDate() != null && workOrderExpenseCodesDTO.getStartDate().isAfter(updatableWoExpenseCode.getEndDate())){
				log.warn("WoExpenseCode startDate not be after woExpenseCode endDate");
				throw  new InvalidDataException(ErrorCodes.CNT_BV_0024,"woExpenseCode","startDate",workOrderExpenseCodesDTO.getStartDate().toString(),"WoExpenseCode startDate not be after woExpenseCode endDate");
			}
			updatableWoExpenseCode.setStartDate(workOrderExpenseCodesDTO.getStartDate());
		}
		if(workOrderExpenseCodesDTO.getEndDate() != null && (updatableWoExpenseCode.getEndDate() == null || !workOrderExpenseCodesDTO.getEndDate().equals(updatableWoExpenseCode.getEndDate()))){
			if(updatableWoExpenseCode.getStartDate() != null && workOrderExpenseCodesDTO.getEndDate().isBefore(updatableWoExpenseCode.getStartDate())){
				log.warn("WoExpenseCode endDate not be before woExpenseCode startDate");
				throw  new InvalidDataException(ErrorCodes.CNT_BV_0025,"woExpenseCode","endDate",workOrderExpenseCodesDTO.getEndDate().toString(),"WoExpenseCode endDate not be before woExpenseCode startDate");
			}
			updatableWoExpenseCode.setEndDate(workOrderExpenseCodesDTO.getEndDate());
		}
	}

	private void updateChargeCodeTask(ChargeCodeTasks updatableTask, ChargeCodeTasksDTO chargeCodeTasksDTO) {
		if(!StringUtils.isAllBlank(chargeCodeTasksDTO.getTaskName()) && !chargeCodeTasksDTO.getTaskName().equals(updatableTask.getTaskName())){
			updatableTask.setTaskName(chargeCodeTasksDTO.getTaskName());
		}
		if(!StringUtils.isAllBlank(chargeCodeTasksDTO.getTaskDesc()) && !chargeCodeTasksDTO.getTaskDesc().equals(updatableTask.getTaskDesc())){
			updatableTask.setTaskDesc(chargeCodeTasksDTO.getTaskDesc());
		}
		if(chargeCodeTasksDTO.getStartDate() != null && !chargeCodeTasksDTO.getStartDate().equals(updatableTask.getStartDate())){
			if(updatableTask.getEndDate() != null && chargeCodeTasksDTO.getStartDate().isAfter(updatableTask.getEndDate())){
				log.warn("ChargeCodeTask startDate not be after chargeCodeTask endDate");
				throw  new InvalidDataException(ErrorCodes.CNT_BV_0026,"chargeCodeTask","startDate",chargeCodeTasksDTO.getStartDate().toString(),"ChargeCodeTask startDate not be after chargeCodeTask endDate");
			}
			updatableTask.setStartDate(chargeCodeTasksDTO.getStartDate());
		}
		if(chargeCodeTasksDTO.getEndDate() != null && (updatableTask.getEndDate() == null || !chargeCodeTasksDTO.getEndDate().equals(updatableTask.getEndDate()))){
			if(updatableTask.getStartDate() != null && chargeCodeTasksDTO.getEndDate().isBefore(updatableTask.getStartDate())){
				log.warn("ChargeCodeTask endDate not be before chargeCodeTask startDate");
				throw  new InvalidDataException(ErrorCodes.CNT_BV_0027,"chargeCodeTask","endDate",chargeCodeTasksDTO.getEndDate().toString(),"ChargeCodeTask endDate not be before chargeCodeTask startDate");
			}
			updatableTask.setStartDate(chargeCodeTasksDTO.getStartDate());
		}
	}

	private void updateChargeCodeRate(ChargeCodeRate updatableChargeCodeRate, ChargeCodeRateDTO chargeCodeRateDTO) {
		if(chargeCodeRateDTO.getRate() != updatableChargeCodeRate.getRate()){
			updatableChargeCodeRate.setRate(chargeCodeRateDTO.getRate());
		}
		if(!StringUtils.isAllBlank(chargeCodeRateDTO.getRateDesc()) && (StringUtils.isAllBlank(updatableChargeCodeRate.getRateDesc()) || !updatableChargeCodeRate.getRateDesc().equals(chargeCodeRateDTO.getRateDesc()))){
			updatableChargeCodeRate.setRateDesc(chargeCodeRateDTO.getRateDesc());
		}
		if(chargeCodeRateDTO.getRateFrequency() != null && !updatableChargeCodeRate.getRateFrequency().equals(chargeCodeRateDTO.getRateFrequency())){
			updatableChargeCodeRate.setRateFrequency(chargeCodeRateDTO.getRateFrequency());
		}
		if(chargeCodeRateDTO.getStepThreshold() != updatableChargeCodeRate.getStepThreshold()){
			updatableChargeCodeRate.setStepThreshold(chargeCodeRateDTO.getStepThreshold());
		}
	}

	private WorkOrderResource createWorkOrderResource(WorkOrderResourceDTO workOrderResourceDTO) {
		WorkOrderResource workOrderResource = contractModelConverter.convertToEntity(workOrderResourceDTO);
		//create supervisors
		List<ContractSupervisor> contractSupervisors = new ArrayList<>();
		if(workOrderResourceDTO.getSupervisors() != null && !workOrderResourceDTO.getSupervisors().isEmpty()){
			for(ContractSupervisorDTO contractSupervisorDTO:workOrderResourceDTO.getSupervisors()) {
				//check the given role is exists or not
				if(!woSupervisorRoleRepo.existsById(contractSupervisorDTO.getRole().getSupervisorRoleID())){
					throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrderSupervisor role by id:"+contractSupervisorDTO.getRole().getSupervisorRoleID()+" not found");
				}
				ContractSupervisor contractSupervisor = contractModelConverter.convertToEntity(contractSupervisorDTO);
				contractSupervisor.setWorkOrderResource(workOrderResource);
				contractSupervisors.add(contractSupervisor);
			}
		}
		contractSupervisors.sort(ContractSupervisor.contractSupervisorLambdaComparator());
		contractValidator. validateContractSuperVisors(contractSupervisors);
		workOrderResource.setSupervisors(contractSupervisors);
		//createWorkerType
		List<ContractResourceWorkerType> contractResourceWorkerTypes = new ArrayList<>();
		if(workOrderResourceDTO.getWorkerType() != null && !workOrderResourceDTO.getWorkerType().isEmpty()){
			ContractResourceWorkerType contractResourceWorkerType = contractModelConverter.convertToEntity(workOrderResourceDTO.getWorkerType().get(0));
			contractResourceWorkerType.setWorkOrderResource(workOrderResource);
			contractResourceWorkerType.setStartDate(LocalDate.now());
			contractResourceWorkerTypes.add(contractResourceWorkerType);
		}
		workOrderResource.setWorkerType(contractResourceWorkerTypes);
		return workOrderResource;
	}

	private ContractWorkLocation createWorkLocation(ContractWorkLocationDTO contractWorkLocationDTO) {
		ContractWorkLocation contractWorkLocation = contractModelConverter.convertToEntity(contractWorkLocationDTO);
		List<WorkOrderChargeCode> chargeCodes = new ArrayList<>();
		if (contractWorkLocationDTO.getChargeCodes() != null && !contractWorkLocationDTO.getChargeCodes().isEmpty()) {
			for (WorkOrderChargeCodeDTO workOrderChargeCodeDTO : contractWorkLocationDTO.getChargeCodes()) {
				WorkOrderChargeCode workOrderChargeCode = createWorkOrderChargeCode(workOrderChargeCodeDTO);
				workOrderChargeCode.setContractWorkLocation(contractWorkLocation);
				chargeCodes.add(workOrderChargeCode);
			}
		}
		contractWorkLocation.setChargeCodes(chargeCodes);
		return contractWorkLocation;
	}

	private WorkOrderChargeCode createWorkOrderChargeCode(WorkOrderChargeCodeDTO workOrderChargeCodeDTO){

		WorkOrderChargeCode workOrderChargeCode = contractModelConverter.convertToEntity(workOrderChargeCodeDTO);
		//create chargeCode tasks
		List<ChargeCodeTasks> chargeCodeTasks = new ArrayList<>();
		if (workOrderChargeCodeDTO.getChargeCodeTasks() != null && !workOrderChargeCodeDTO.getChargeCodeTasks().isEmpty()) {
			for (ChargeCodeTasksDTO chargeCodeTasksDTO : workOrderChargeCodeDTO.getChargeCodeTasks()) {
				ChargeCodeTasks chargeCodeTask = contractModelConverter.convertToEntity(chargeCodeTasksDTO);
				chargeCodeTask.setWoChrageCode(workOrderChargeCode);
				chargeCodeTasks.add(chargeCodeTask);
			}
		}
		workOrderChargeCode.setChargeCodeTasks(chargeCodeTasks);

		//create chargeCodeRates
		List<ChargeCodeRate> workOrderRates = new ArrayList<>();
		if (workOrderChargeCodeDTO.getWorkOrderRates() != null && !workOrderChargeCodeDTO.getWorkOrderRates().isEmpty()) {
			//first record will be taken remaining will be discarded
			ChargeCodeRate chargeCodeRate = contractModelConverter.convertToEntity(workOrderChargeCodeDTO.getWorkOrderRates().get(0));
			chargeCodeRate.setWoChrageCode(workOrderChargeCode);
			workOrderRates.add(chargeCodeRate);
		}
        workOrderRates.sort(ChargeCodeRate.chargeCodeRateLambdaComparator());
        contractValidator.validateChargeCodeRates(workOrderRates);
		workOrderChargeCode.setWorkOrderRates(workOrderRates);

		//create workOrderExpenseCode
		List<WorkOrderExpenseCodes> workOrderExpenseCodes = new ArrayList<>();
		if (workOrderChargeCodeDTO.getWorkOrderExpenses() != null && !workOrderChargeCodeDTO.getWorkOrderExpenses().isEmpty()) {
			for (WorkOrderExpenseCodesDTO workOrderExpenseCodesDTO : workOrderChargeCodeDTO.getWorkOrderExpenses()) {
				WorkOrderExpenseCodes workOrderExpenseCode = contractModelConverter.convertToEntity(workOrderExpenseCodesDTO);
				workOrderExpenseCode.setWoChrageCode(workOrderChargeCode);
				workOrderExpenseCodes.add(workOrderExpenseCode);
			}
		}
		workOrderChargeCode.setWorkOrderExpenses(workOrderExpenseCodes);

		//create resources
		List<ChargeCodeResource> chargeCodeResources = new ArrayList<>();
		if (workOrderChargeCodeDTO.getContractResources() != null && !workOrderChargeCodeDTO.getContractResources().isEmpty()) {
			for (ChargeCodeResourceDTO chargeCodeResourceDTO : workOrderChargeCodeDTO.getContractResources()) {
				//validate is worker is exists or not
				if(!workerRepository.existsById(chargeCodeResourceDTO.getWorker().getWorkerID())){
					throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"Worker by id:"+chargeCodeResourceDTO.getWorker().getWorkerID()+" not found");
				}
				ChargeCodeResource chargeCodeResource = createChargeCodeResource(chargeCodeResourceDTO);
				chargeCodeResource.setWoChrageCode(workOrderChargeCode);
				chargeCodeResources.add(chargeCodeResource);
			}
		}
		workOrderChargeCode.setContractResources(chargeCodeResources);
		return workOrderChargeCode;
	}

	private ChargeCodeResource createChargeCodeResource(ChargeCodeResourceDTO chargeCodeResourceDTO){

		ChargeCodeResource chargeCodeResource = contractModelConverter.convertToEntity(chargeCodeResourceDTO);
		//create payProfile
		List<WorkOrderExpenseBudget> workOrderExpenseBudgets = new ArrayList<>();
		if (chargeCodeResourceDTO.getPayProfile() != null && !chargeCodeResourceDTO.getPayProfile().isEmpty()) {
			//first will be taken remaining will be discarded
			WorkOrderExpenseBudget workOrderExpenseBudget = contractModelConverter.convertToEntity(chargeCodeResourceDTO.getPayProfile().get(0));
			workOrderExpenseBudget.setContractResource(chargeCodeResource);
			workOrderExpenseBudget.setStartDate(LocalDate.now());
			workOrderExpenseBudgets.add(workOrderExpenseBudget);
		}
		chargeCodeResource.setPayProfile(workOrderExpenseBudgets);
		return chargeCodeResource;
	}

	private void updateClientData(ContractClient updatableClient, ContractClientDTO contractClientDTO) {
		if(contractClientDTO.getClientSequence() >0 && contractClientDTO.getClientSequence() != updatableClient.getClientSequence()){
			updatableClient.setClientSequence(contractClientDTO.getClientSequence());
		}
		if(contractClientDTO.getClient() != null && !StringUtils.isAllBlank(contractClientDTO.getClient().getOrganizationID())
				&& (updatableClient.getClient()== null || !contractClientDTO.getClient().getOrganizationID().equals(updatableClient.getClient().getOrganizationID()))){
			Organization  organization = modelMapper.map(contractClientDTO.getClient(),Organization.class);
			updatableClient.setClient(organization);
		}
	}

	private void updateSuppCharge(WorkOrderSuppCharges updatableSppCharge, WorkOrderSuppChargesDTO workOrderSuppChargesDTO) {
		if(workOrderSuppChargesDTO.getChargeType() != null && !updatableSppCharge.getChargeType().equals(workOrderSuppChargesDTO.getChargeType())){
			updatableSppCharge.setChargeType(workOrderSuppChargesDTO.getChargeType());
		}
		if(!StringUtils.isAllBlank(workOrderSuppChargesDTO.getWoSupChargeName()) && !updatableSppCharge.getWoSupChargeName().equals(workOrderSuppChargesDTO.getWoSupChargeName())){
			updatableSppCharge.setWoSupChargeName(workOrderSuppChargesDTO.getWoSupChargeName());
		}
		if(workOrderSuppChargesDTO.getAmount() != null){
			updatableSppCharge.setAmount(workOrderSuppChargesDTO.getAmount());
		}
		if(workOrderSuppChargesDTO.getStartDate() != null &&  !updatableSppCharge.getStartDate().equals(workOrderSuppChargesDTO.getStartDate())){
			if(updatableSppCharge.getEndDate() != null && workOrderSuppChargesDTO.getStartDate().isAfter(updatableSppCharge.getEndDate())){
				log.warn("WorkOrderSuppCharge startDate not be after workOrderSuppCharge endDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0029,"workOrderSuppCharges","startDate",workOrderSuppChargesDTO.getStartDate().toString(),"WorkOrderSuppCharge startDate not be after workOrderSuppCharge endDate");
			}
			updatableSppCharge.setStartDate(workOrderSuppChargesDTO.getStartDate());
		}
		if(workOrderSuppChargesDTO.getEndDate() != null &&  !updatableSppCharge.getEndDate().equals(workOrderSuppChargesDTO.getEndDate())){
			if(updatableSppCharge.getStartDate() != null && workOrderSuppChargesDTO.getEndDate().isBefore(updatableSppCharge.getStartDate())){
				log.warn("WorkOrderSuppCharge endDate not be before workOrderSuppCharge startDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0030,"workOrderSuppCharges","endDate",workOrderSuppChargesDTO.getEndDate().toString(),"WorkOrderSuppCharge endDate not be before workOrderSuppCharge startDate");
			}
			updatableSppCharge.setEndDate(workOrderSuppChargesDTO.getEndDate());
		}

	}

	private void computeContractDiscount(ContractDiscount contractDis){
		if (contractDis.getDiscountStep() != null && !contractDis.getDiscountStep().isEmpty()) {
			for (ContractDiscountStep contractDiscountStep : contractDis.getDiscountStep()) {
				if(contractDiscountStep.getContractDiscount() == null || StringUtils.isAllBlank(contractDiscountStep.getContractDiscount().getDiscountId())){
					contractDiscountStep.setContractDiscount(contractDis);
				}
			}
			contractDis.getDiscountStep().sort(ContractDiscountStep.createContractDiscountStepLambdaComparator());
			contractValidator.validateDiscountSteps(contractDis.getDiscountStep());
		} else{
			log.warn("AtLeast one discountStep mandatory");
			throw new InvalidDataException(ErrorCodes.CNT_BV_0006,"contractDiscount","discountStep",null,"AtLeast one discountStep mandatory");
		}
	}

	private void updateContractMSAData(Contract contract, ContractDTO contractDTO) {
		if(contractDTO.getRelatedOrg() != null
				&& !StringUtils.isAllBlank(contractDTO.getRelatedOrg().getOrganizationID())
				&& !contract.getRelatedOrg().getOrganizationID().equals(contractDTO.getRelatedOrg().getOrganizationID())){
			contractValidator.validateOrganization(contractDTO.getRelatedOrg().getOrganizationID(),contractDTO.getStartDate());
			contract.getRelatedOrg().setOrganizationID(contractDTO.getRelatedOrg().getOrganizationID());
		}
		if(!StringUtils.isAllBlank(contractDTO.getContractName()) && !contract.getContractName().equals(contractDTO.getContractName())){
			contractValidator.validateName(contract.getContractID(),contractDTO.getContractName());
			contract.setContractName(contractDTO.getContractName());
		}
		if(!StringUtils.isAllBlank(contractDTO.getBillingCurrCode()) && (StringUtils.isAllBlank(contract.getBillingCurrCode()) || contract.getBillingCurrCode().equals(contractDTO.getBillingCurrCode()))){
			contractValidator.validateBillingCurrency(contractDTO.getBillingCurrCode());
			contract.setBillingCurrCode(contractDTO.getBillingCurrCode());
		}
		if(contractDTO.getStartDate() != null && !contract.getStartDate().equals(contractDTO.getStartDate())){
			LocalDate minAccStartDate = contractAccountRepo.getMinContractAccDate(contract.getContractID());
			if(minAccStartDate != null && minAccStartDate.isBefore(contractDTO.getStartDate())){
               throw new InvalidDataException(ErrorCodes.CNT_BV_0044,"contract","contractAccount",contractDTO.getStartDate().toString(),"Contract start date cannot be after contractAccount startDate:"+minAccStartDate);
			}
			contract.setStartDate(contractDTO.getStartDate());
		}
		if(contractDTO.getEndDate() != null && (contract.getEndDate() == null || !contractDTO.getEndDate().equals(contract.getEndDate()))){
			if(contractDTO.getEndDate().isBefore(contract.getStartDate())){
				log.warn("Contract endDate:"+contractDTO.getEndDate()+" not be before contract startDate:"+contract.getStartDate());
				throw new InvalidDataException(ErrorCodes.CNT_BV_0031,"contract","endDate",contractDTO.getEndDate().toString(),"Contract endDate:"+contractDTO.getEndDate()+" not be before contract startDate:"+contract.getStartDate());
			}
			contract.setEndDate(contractDTO.getEndDate());
		}
	}

	private void updateContractDiscountData(ContractDiscount updatableContractDiscount, ContractDiscountDTO contractDiscountDTO) {
		ContractAccount contractAccount = updatableContractDiscount.getContractAccount();
		if(!StringUtils.isAllBlank(contractDiscountDTO.getDiscountName())
				&& updatableContractDiscount.getDiscountName().equals(contractDiscountDTO.getDiscountName())){
			updatableContractDiscount.setDiscountName(contractDiscountDTO.getDiscountName());
		}
		if(!StringUtils.isAllBlank(contractDiscountDTO.getDiscountDesc())
				&& (StringUtils.isAllBlank(updatableContractDiscount.getDiscountDesc())
				|| !updatableContractDiscount.getDiscountDesc().equals(contractDiscountDTO.getDiscountDesc()))){
			updatableContractDiscount.setDiscountDesc(contractDiscountDTO.getDiscountDesc());
		}
		if(contractDiscountDTO.getPriority() >0 && contractDiscountDTO.getPriority() != updatableContractDiscount.getPriority()){
			updatableContractDiscount.setPriority(contractDiscountDTO.getPriority());
		}
		if(contractDiscountDTO.getStartDate() != null && !updatableContractDiscount.getStartDate().equals(contractDiscountDTO.getStartDate())){
			if(contractDiscountDTO.getStartDate().isBefore(contractAccount.getStartDate())){
				log.warn("ContractDiscount startDate not be before account startDate:"+contractAccount.getStartDate());
				throw new InvalidDataException(ErrorCodes.CNT_BV_0032,"contractDiscount","startDate",contractDiscountDTO.getStartDate().toString(),"ContractDiscount startDate not be before account startDate:"+contractAccount.getStartDate());
			}
			updatableContractDiscount.setStartDate(contractDiscountDTO.getStartDate());
		}
		if(contractDiscountDTO.getEndDate() != null && (updatableContractDiscount.getEndDate() == null || !updatableContractDiscount.getEndDate().equals(contractDiscountDTO.getEndDate()))){
			if(contractDiscountDTO.getEndDate().isBefore(updatableContractDiscount.getStartDate())){
				log.warn("ContractDiscount endDate not be previous dates of start date");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0033,"contractDiscount","endDate",contractDiscountDTO.getEndDate().toString(),"ContractDiscount endDate not be previous dates of start date");
			}
			updatableContractDiscount.setEndDate(contractDiscountDTO.getEndDate());
		}
		if(contractDiscountDTO.getDiscountStep() != null && !contractDiscountDTO.getDiscountStep().isEmpty()){
			List<ContractDiscountStep> contractDiscountSteps = updatableContractDiscount.getDiscountStep();
			for(ContractDiscountStepDTO contractDiscountStepDTO:contractDiscountDTO.getDiscountStep()){
				if(!StringUtils.isAllBlank(contractDiscountStepDTO.getDiscSetpId())){
					if(contractDiscountSteps== null || contractDiscountSteps.isEmpty()){
						log.error("ContractDiscountStep by id:"+contractDiscountStepDTO.getDiscSetpId()+" not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractDiscountStep by id:"+contractDiscountStepDTO.getDiscSetpId()+" not found");
					} else {
						ContractDiscountStep updatableDiscountStep= null;
						for(ContractDiscountStep contractDiscountStep:contractDiscountSteps){
							if(contractDiscountStep.getDiscSetpId().equals(contractDiscountStepDTO.getDiscSetpId())){
								updatableDiscountStep=contractDiscountStep;
								break;
							}
						}
						if(updatableDiscountStep==null){
							log.error("ContractDiscountStep by id:"+contractDiscountStepDTO.getDiscSetpId()+" not found");
							throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractDiscountStep by id:"+contractDiscountStepDTO.getDiscSetpId()+" not found");
						} else{
							updateDiscountStepData(updatableDiscountStep,contractDiscountStepDTO);
						}
					}
				} else{
					ContractDiscountStep contractDiscountStep = contractModelConverter.convertToEntity(contractDiscountStepDTO);
					contractDiscountStep.setContractDiscount(updatableContractDiscount);
					if(contractDiscountSteps != null){
						contractDiscountSteps.add(contractDiscountStep);
					} else{
						contractDiscountSteps = new ArrayList<>();
						contractDiscountSteps.add(contractDiscountStep);
					}
				}
			}
			contractDiscountSteps.sort(ContractDiscountStep.createContractDiscountStepLambdaComparator());
			contractValidator.validateDiscountSteps(contractDiscountSteps);
			updatableContractDiscount.setDiscountStep(contractDiscountSteps);
		}
	}

	private void updateDiscountStepData(ContractDiscountStep updatableDiscountStep, ContractDiscountStepDTO contractDiscountStepDTO) {
		if(contractDiscountStepDTO.getStepThreshold()>0 && updatableDiscountStep.getStepThreshold() != contractDiscountStepDTO.getStepThreshold()){
			updatableDiscountStep.setStepThreshold(contractDiscountStepDTO.getStepThreshold());
		}
		if(contractDiscountStepDTO.getDiscountPct() >0 && updatableDiscountStep.getDiscountPct() != contractDiscountStepDTO.getDiscountPct()){
			updatableDiscountStep.setDiscountPct(contractDiscountStepDTO.getDiscountPct());
		}
		if(contractDiscountStepDTO.getDiscountAmount() >0 && updatableDiscountStep.getDiscountAmount() != contractDiscountStepDTO.getDiscountAmount()){
			updatableDiscountStep.setDiscountAmount(contractDiscountStepDTO.getDiscountAmount());
		}
		if(contractDiscountStepDTO.getDiscountType() != null && !contractDiscountStepDTO.getDiscountType().equals(updatableDiscountStep.getDiscountType())){
			updatableDiscountStep.setDiscountType(contractDiscountStepDTO.getDiscountType());
		}
	}

	private void updateExpenseData(ContractExpense updatableExpense, ContractExpenseDTO contractExpenseDTO) {
		if(contractExpenseDTO.getExpenseAmount() >0 && updatableExpense.getExpenseAmount() != contractExpenseDTO.getExpenseAmount()){
			updatableExpense.setExpenseAmount(contractExpenseDTO.getExpenseAmount());
		}
		if(!StringUtils.isAllBlank(contractExpenseDTO.getExpenseDesc()) && (updatableExpense.getExpenseDesc() == null || !updatableExpense.getExpenseDesc().equals(contractExpenseDTO.getExpenseDesc()))){
			updatableExpense.setExpenseDesc(contractExpenseDTO.getExpenseDesc());
		}
		if(!StringUtils.isAllBlank(contractExpenseDTO.getExpenseName()) && (updatableExpense.getExpenseName() == null || !updatableExpense.getExpenseName().equals(contractExpenseDTO.getExpenseName()))){
			updatableExpense.setExpenseName(contractExpenseDTO.getExpenseName());
		}
		if(contractExpenseDTO.getStartDate() != null){
			if(updatableExpense.getEndDate() != null && contractExpenseDTO.getStartDate().isAfter(updatableExpense.getEndDate())){
				log.warn("ContractExpense startDate not be after dates of expense endDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0034,"contractExpense","startDate",contractExpenseDTO.getStartDate().toString(),"ContractExpense startDate not be after dates of expense endDate");
			}
			updatableExpense.setStartDate(contractExpenseDTO.getStartDate());
		}
		if(contractExpenseDTO.getEndDate() != null){
			if(updatableExpense.getStartDate() != null && contractExpenseDTO.getEndDate().isBefore(contractExpenseDTO.getStartDate())){
				log.warn("ContractExpense endDate not be previous date of expense startDate");
				throw new InvalidDataException(ErrorCodes.CNT_BV_0035,"contractExpense","endDate",contractExpenseDTO.getEndDate().toString(),"ContractExpense endDate not be previous date of expense startDate");
			}
			updatableExpense.setStartDate(contractExpenseDTO.getStartDate());
		}
	}

	private boolean isExistsByWbsCode(String wbsCode){
		return contractWorkOrderRepo.existsByWbsCode(wbsCode);
	}

	private LocalDate getNextBillDate(int billCycle, int billPeriod, EnumBillPeriodUnit billPeriodUnit, LocalDate startDate){
		LocalDate nextBillDate =null;
		if(billPeriodUnit.equals(EnumBillPeriodUnit.MONTHLY)){
			LocalDate startDate1 = startDate.plusMonths(billPeriod);
			LocalDate localDate =  LocalDate.of(startDate1.getYear(),startDate1.getMonth(),billCycle);
			if(localDate.isBefore(startDate1)){
				nextBillDate=localDate.plusMonths(1)  ;
			} else{
				nextBillDate = localDate;
			}
			//nextBillDate= LocalDate.of(startDate1.getYear(),startDate1.getMonth(),billCycle);
           /* if(ChronoUnit.DAYS.between(nextBillDate,startDate)<15){
                nextBillDate=nextBillDate.plusMonths(1);
            }*/
		} else{
			LocalDate startDate1 = startDate.plusWeeks(billPeriod);
			DayOfWeek dayOfWeek = getDayOfWeek(billCycle);
			nextBillDate= startDate1.with(TemporalAdjusters.next(dayOfWeek));
		}
		return nextBillDate;
	}

	private DayOfWeek getDayOfWeek(Integer billCycle) {
		DayOfWeek dayOfWeek = null;
		switch (billCycle) {
			case 1:
				dayOfWeek = DayOfWeek.SUNDAY;
				break;
			case 2:
				dayOfWeek = DayOfWeek.MONDAY;
				break;
			case 3:
				dayOfWeek = DayOfWeek.TUESDAY;
				break;
			case 4:
				dayOfWeek = DayOfWeek.WEDNESDAY;
				break;
			case 5:
				dayOfWeek = DayOfWeek.THURSDAY;
				break;
			case 6:
				dayOfWeek = DayOfWeek.FRIDAY;
				break;
			case 7:
				dayOfWeek = DayOfWeek.SATURDAY;
				break;
		}
		return dayOfWeek;
	}


}
