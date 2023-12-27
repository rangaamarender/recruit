package com.lucid.recruit.worker.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.lucid.core.azure.EnumAzureContainers;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.contract.repo.WorkOrderResourceRepo;
import com.lucid.recruit.referencedata.dto.RelatedWorkerTypeDTO;
import com.lucid.recruit.referencedata.entity.Department;
import com.lucid.recruit.referencedata.repo.DepartmentRepo;
import com.lucid.recruit.worker.repo.*;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lucid.core.azure.AzureBlobService;
import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.constants.EnumDocSource;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;
import com.lucid.recruit.docs.repo.DefaultDocAssignmentsRepo;
import com.lucid.recruit.docs.repo.DocAttributeDefRepo;
import com.lucid.recruit.person.dto.PersonLegalDTO;
import com.lucid.recruit.person.dto.PersonLegalSummaryDTO;
import com.lucid.recruit.person.entity.PersonLegal;
import com.lucid.recruit.person.repo.PersonAddressRepo;
import com.lucid.recruit.person.repo.PersonBankDetailsRepo;
import com.lucid.recruit.person.repo.PersonContactDetailsRepo;
import com.lucid.recruit.person.repo.PersonDependentRepo;
import com.lucid.recruit.person.repo.PersonEmergencyContactRepo;
import com.lucid.recruit.person.repo.PersonLegalRepo;
import com.lucid.recruit.referencedata.dto.JobDTO;
import com.lucid.recruit.referencedata.entity.Job;
import com.lucid.recruit.referencedata.entity.WorkerInActiveStatusCodes;
import com.lucid.recruit.referencedata.repo.JobRepo;
import com.lucid.recruit.worker.dto.WorkAssignmentDTO;
import com.lucid.recruit.worker.dto.WorkerCountsDTO;
import com.lucid.recruit.worker.dto.WorkerCurrentStatusDTO;
import com.lucid.recruit.worker.dto.WorkerDTO;
import com.lucid.recruit.worker.dto.WorkerDocAttributesDTO;
import com.lucid.recruit.worker.dto.WorkerDocumentDTO;
import com.lucid.recruit.worker.dto.WorkerStatusDTO;
import com.lucid.recruit.worker.dto.WorkerSummaryDTO;
import com.lucid.recruit.worker.entity.WorkAssignment;
import com.lucid.recruit.worker.entity.Worker;
import com.lucid.recruit.worker.entity.WorkerDocAttributes;
import com.lucid.recruit.worker.entity.WorkerDocument;
import com.lucid.recruit.worker.entity.WorkerStatus;
import com.lucid.recruit.worker.entity.WorkerStatusCode;
import com.lucid.recruit.worker.validations.WorkerValidator;
import com.lucid.util.ServiceUtils;

import jakarta.validation.Validator;

@Service
public class WorkerServiceImpl implements WorkerService {
	private static final Logger log = LoggerFactory.getLogger(WorkerServiceImpl.class);

	@Autowired
	private WorkerRepository workerRepo;

	@Autowired
	private PersonLegalRepo personLegalRepo;
	@Autowired
	private PersonContactDetailsRepo personContactRepo;

	@Autowired
	private PersonAddressRepo personAddressRepo;
	@Autowired
	private WorkerStatusRepository workerStatusRepo;
	@Autowired
	private WorkeAssignmentRepo workeAssigmentRepo;
	@Autowired
	private PersonBankDetailsRepo personBankDetailsRepo;
	@Autowired
	private PersonEmergencyContactRepo personEmergencyContactRepo;
	@Autowired
	private PersonDependentRepo personDependentRepo;
	@Autowired
	private WorkerDocumentRepository workerDocumentRepo;
	@Autowired
	private WorkerDocAttributesRepository workerDocAttrRepo;
	@Autowired
	private JobRepo jobRepo;
	@Autowired
	private WorkOrderResourceRepo workOrderResourceRepo;

	@Autowired
	private DefaultDocAssignmentsRepo defaulDocAssignRepo;
	@Autowired
	private DocAttributeDefRepo docAttrDefRepo;
	@Autowired
	private ModelMapper modelMapper;
	// This instance is useful to validate the worker
	@Autowired
	private WorkerValidator workerValidator;
	@Autowired
	private Validator validator;
	@Autowired
	private AzureBlobService azureBlobService;
	@Autowired
	private WorkerSpecification workerSpecification;

	@Autowired
	private WorkerHelper workerHelper;

	@Autowired
	private WorkerAttributesRepo workerAttributesRepo;

	@Autowired
	private DepartmentRepo departmentRepo;

	public WorkerServiceImpl() {
		super();
	}

	/**
	 * This method take the WorkerDTO 1.validates the workerDTO 2.Create personLegal
	 * 4.Creates PersonContactDetails with personLegal 5.If documentDetails exists
	 * then insertDocumentDetails 6.Creates Worker with personLegal 7.Creates
	 * WorkerStatus with Worker 8.take the WorkAssignment form WorkerDTO and from
	 * workAssignment take the jobId and find the givenJob is exists if not throws
	 * error otherWise create workAssignmentWith worker 9.Once the All above
	 * operations done we need to sent mail to user for further details submission
	 *
	 * @param workerDto this
	 * @return created workerDTO
	 */
	@Override
	@Transactional
	public WorkerDTO createWorker(WorkerDTO workerDto) {
		// 1.validate worker before creating the worker
		// 1.validate and create workerData;
		Worker worker = null;
		worker = workerValidator.validateWorker(workerDto, worker);


		// 2.Handle Person
		PersonLegal personLegal = setPersonLegalData(workerDto.getPersonLegal());
		personLegalRepo.save(personLegal);
		worker.setPersonLegal(personLegal);

		// 3.Handle Person Contact, only 1st element will be considered other elements
		// will be discarded

		workerValidator.validatePersonContactDetails(worker,workerDto.getPersonLegal());
		personContactRepo.saveAll(worker.getPersonLegal().getPersonContactDetails());

		// 5.save worker
		//set worker code
		workerValidator.setWorkerCode(worker);
		workerRepo.save(worker);
		//create workAssignment
		createWorkAssignment(worker,workerDto);
		workeAssigmentRepo.saveAll(worker.getWorkAssignment());
		//create status
		createStatus(worker,workerDto);
		workerStatusRepo.saveAll(worker.getWorkerStatus());
		//Create Worker Documents assigned by default
		List<WorkerDocument> workerDocs = new ArrayList<>();
		List<DefaultDocAssignments> defaultAssignedDocs = defaulDocAssignRepo.findAutoAssignedDoc(
				EnumDocRelatedEntity.WORKER, worker.getWorkerType().getWorkerTypeCode().getCode());
		Worker finalWorker = worker;
		defaultAssignedDocs.forEach(workerDefDoc -> {
			WorkerDocument workerDoc = new WorkerDocument(null, finalWorker, workerDefDoc.getDocumentDef(), null, null,
					null, EnumDocSource.AUTOASSIGNED, EnumDocStatus.Pending, null);
			workerDocs.add(workerDoc);
		});

		List<WorkerDocument> savedWorkerDocs = new ArrayList<>();
		if (!workerDocs.isEmpty()) {
			savedWorkerDocs = workerDocumentRepo.saveAll(workerDocs);
		}
		worker.setWorkerDocuments(savedWorkerDocs);
		// create workerAttributes
        workerValidator.validateWorkerAttributes(worker,workerDto.getWorkerAttributes());
        workerAttributesRepo.saveAll(worker.getWorkerAttributes());
		return workerHelper.prepareResponseData(worker,false);
	}

	private void createStatus(Worker worker, WorkerDTO workerDTO) {
		//createStatus
		List<WorkerStatus> workerStatuses = worker.getWorkerStatus();
		if(workerStatuses == null){
			workerStatuses = new ArrayList<>();
		}
		if(LocalDate.now().isAfter(workerDTO.getJoiningDate()) || LocalDate.now().equals(workerDTO.getJoiningDate())){
			WorkerStatus workerStatus = new WorkerStatus();
			workerStatus.setWorker(worker);
			workerStatus.setStatus(WorkerStatusCode.ACTIVE);
			workerStatus.setEffectiveDate(worker.getJoiningDate());
			workerStatuses.add(workerStatus);
		} else{
			WorkerStatus workerStatus1 = new WorkerStatus();
			workerStatus1.setWorker(worker);
			workerStatus1.setStatus(WorkerStatusCode.PENDING);
			workerStatus1.setEffectiveDate(LocalDate.now());
			workerStatuses.add(workerStatus1);
			//active from
			WorkerStatus workerStatus = new WorkerStatus();
			workerStatus.setWorker(worker);
			workerStatus.setStatus(WorkerStatusCode.ACTIVE);
			workerStatus.setEffectiveDate(worker.getJoiningDate());
			workerStatuses.add(workerStatus);
		}
		worker.setWorkerStatus(workerStatuses);
	}

	private void updateJoiningDate(Worker worker,WorkerDTO workerDTO){
		if(!worker.getJoiningDate().equals(workerDTO.getJoiningDate())){
			Optional<LocalDate> minStartDate = workOrderResourceRepo.getMinStartDate(worker.getWorkerID());
			if (minStartDate.isPresent() && workerDTO.getJoiningDate().isAfter(minStartDate.get())) {
				throw new InvalidDataException(ErrorCodes.W_BV_0007, "worker", "joiningDate", String.valueOf(workerDTO.getJoiningDate()), "JoiningDate is before contract startDate:" + minStartDate.get());
			}
			Optional<LocalDate> minInactiveDate = workerStatusRepo.getMinDateByStatus(worker.getWorkerID(),WorkerStatusCode.INACTIVE);
			if (minInactiveDate.isPresent() && workerDTO.getJoiningDate().isAfter(minInactiveDate.get())) {
				throw new InvalidDataException(ErrorCodes.W_BV_0008, "worker", "joiningDate", String.valueOf(workerDTO.getJoiningDate()), "JoiningDate is before inactive date:" + minInactiveDate.get());
			}
			List<WorkerStatus> deletedWorkerStatus = new ArrayList<>();
			worker.getWorkerStatus().forEach(workerStatus -> {
				if(!workerStatus.getStatus().equals(WorkerStatusCode.INACTIVE)){
					deletedWorkerStatus.add(workerStatus);
					worker.getWorkerStatus().remove(workerStatus);
				}
			});
			if(!deletedWorkerStatus.isEmpty()){
				workerStatusRepo.deleteAll(deletedWorkerStatus);
			}
			createStatus(worker,workerDTO);
			workerStatusRepo.saveAll(worker.getWorkerStatus());
		}
	}

	private void createWorkAssignment(Worker worker, WorkerDTO workerDto) {
		Job job = getDepartment(workerDto);
		WorkAssignment workAssignment = new WorkAssignment();
		LocalDate startDate ;
		LocalDate expectedStartDate;
		if(LocalDate.now().isAfter(workerDto.getJoiningDate()) || LocalDate.now().equals(workerDto.getJoiningDate())){
			startDate= workerDto.getJoiningDate();
			expectedStartDate= startDate;
		} else{
			startDate = LocalDate.now();
			expectedStartDate = workerDto.getJoiningDate();
		}
		workAssignment.setStartDate(startDate);
		workAssignment.setExpectedStartDate(expectedStartDate);
		workAssignment.setJob(job);
		workAssignment.setWorker(worker);
		workeAssigmentRepo.save(workAssignment);
		List<WorkAssignment> workAssignments = new ArrayList<>();
		workAssignments.add(workAssignment);
		worker.setWorkAssignment(workAssignments);
	}

	private Job getDepartment(WorkerDTO workerDto) {
		Optional<Department> optionalDepartment = departmentRepo.findById(workerDto.getDepartment().getDeptID());
		if(optionalDepartment.isEmpty()){
			throw new InvalidDataException(ErrorCodes.W_BV_0003,"worker","department",workerDto.getDepartment().getDeptID(),"Department by id:"+workerDto.getDepartment().getDeptID()+" not found");
		}
		Department department = optionalDepartment.get();
		Map<Boolean, Job> jobsMap = new HashMap<>();
		department.getJobs().forEach(job -> {
			if(!jobsMap.containsKey(job.isBillable())){
				jobsMap.put(job.isBillable(),job);
			}
		});
		if(!jobsMap.containsKey(workerDto.isBillable())){
			String message = "Billable job not found";
			if(!workerDto.isBillable()){
				message = "NonBillable job not found";
			}
			throw new InvalidDataException(ErrorCodes.W_BV_0004,"worker","department",workerDto.getDepartment().getDeptID(),message);
		}
		return jobsMap.get(workerDto.isBillable());
	}

	private PersonLegal setPersonLegalData(PersonLegalDTO personLegalDTO) {
		PersonLegal personLegal = new PersonLegal();
		personLegal.setGivenName(personLegalDTO.getGivenName());
		personLegal.setMiddleName(personLegalDTO.getMiddleName());
		personLegal.setFamilyName(personLegalDTO.getFamilyName());
		personLegal.setPreferredName(personLegalDTO.getPreferredName());
		personLegal.setMartialStatus(personLegalDTO.getMaritalStatus());
		personLegal.setBirthDate(personLegalDTO.getBirthDate());
		personLegal.setGender(personLegalDTO.getGender());
		return personLegal;
	}

	@Override
	public WorkerDTO retriveWorkerById(String workerId, boolean history) {
		Worker worker = workerValidator.getWorker(workerId);
		List<WorkerDocument> workerDocuments = worker.getWorkerDocuments();
		worker.setWorkerDocuments(null);
		WorkerDTO workerDto = workerHelper.prepareResponseData(worker, history);
		// Convert Documents to Documents DTO
		List<WorkerDocumentDTO> documentsDTO = new ArrayList<>();
		workerDocuments.forEach(workerDoc -> {
			List<WorkerDocAttributes> docAttributes = workerDoc.getWorkerDocAttributes();
			workerDoc.setWorkerDocAttributes(null);
			WorkerDocumentDTO docDTO = modelMapper.map(workerDoc, WorkerDocumentDTO.class);
			List<WorkerDocAttributesDTO> docAttrDTOs = new ArrayList<>();
			for (WorkerDocAttributes docAttribute : docAttributes) {
				docAttrDTOs.add(modelMapper.map(docAttribute, WorkerDocAttributesDTO.class));
			}
			docDTO.setWorkerDocAttributes(docAttrDTOs);
			documentsDTO.add(docDTO);
		});
		workerDto.setWorkerDocuments(documentsDTO);
		return workerDto;
	}

	@Override
	@Transactional
	public WorkerDTO updateWorker(String workerID, WorkerDTO worker) {
		Worker updatableWorker = workerValidator.getWorker(workerID);
		updatableWorker = workerValidator.validateWorker(worker, updatableWorker);
		// Save the worker changes
		workerRepo.save(updatableWorker);
		if(worker.getJoiningDate() != null) {
			updateJoiningDate(updatableWorker, worker);
		}
		// Handle work assignments
		if(worker.getDepartment() != null) {
			updateWorkAssignments(updatableWorker, worker);
		}
		// Handle Person Legal, API will allow to change Legal information for the
		// associated person legal ID, so always legal ID have to pass
		if (worker.getPersonLegal() != null) {
			// validate and update person legal
			boolean update = updatePersonLegal(updatableWorker, worker.getPersonLegal());
			// Save the person legal changes
			if (update) {
				personLegalRepo.save(updatableWorker.getPersonLegal());
			}
			// update personContactDetails
			workerValidator.validatePersonContactDetails(updatableWorker, worker.getPersonLegal());
			personContactRepo.saveAll(updatableWorker.getPersonLegal().getPersonContactDetails());

			// handle the person address changes
			if (worker.getPersonLegal().getPersonAddress() != null
					&& !worker.getPersonLegal().getPersonAddress().isEmpty()) {
				workerValidator.validatePersonAddress(updatableWorker, worker.getPersonLegal());
				personAddressRepo.saveAll(updatableWorker.getPersonLegal().getPersonAddress());
			}

			// handle the person bankDetails changes
			if (worker.getPersonLegal().getPersonBankDetails() != null
					&& !worker.getPersonLegal().getPersonBankDetails().isEmpty()) {
				workerValidator.validatePersonBankDetails(updatableWorker, worker.getPersonLegal());
				personBankDetailsRepo.saveAll(updatableWorker.getPersonLegal().getPersonBankDetails());
			}

			// handle the personEmergencyContacts
			if (worker.getPersonLegal().getPersonEmergencyContact() != null
					&& !worker.getPersonLegal().getPersonEmergencyContact().isEmpty()) {
				workerValidator.validatePersonEmergencyContact(updatableWorker, worker.getPersonLegal());
				personEmergencyContactRepo.saveAll(updatableWorker.getPersonLegal().getPersonEmergencyContact());
			}

			// handle the personDependents
			if (worker.getPersonLegal().getPersonDependents() != null
					&& !worker.getPersonLegal().getPersonDependents().isEmpty()) {
				workerValidator.validatePersonDependents(updatableWorker, worker.getPersonLegal());
				personDependentRepo.saveAll(updatableWorker.getPersonLegal().getPersonDependents());
			}
		}
		if(worker.getWorkerAttributes() != null && !worker.getWorkerAttributes().isEmpty()){
			workerValidator.validateWorkerAttributes(updatableWorker,worker.getWorkerAttributes());
			workerAttributesRepo.saveAll(updatableWorker.getWorkerAttributes());
		}
		// Prepare Response
		return workerHelper.prepareResponseData(updatableWorker, false);
	}

	@Override
	public Page<WorkerSummaryDTO> retrieveAllWorkers(Integer offSet, Integer limit, String organizationID,
													 String workerType, WorkerStatusCode workerStatus, String emailId, String givenName, String familyName,
													 LocalDate effectiveDate, Boolean internalInd, Boolean benchInd) {
		log.info("Getting specification Object");
		// get specification with given filterData
		if (Objects.nonNull(benchInd) && benchInd) {
			internalInd = Boolean.FALSE;
			workerStatus = WorkerStatusCode.ACTIVE;

		}
		Specification<Worker> specification = workerSpecification.conditionalSearchForWorker(organizationID, workerType,
				workerStatus, emailId, givenName, familyName, effectiveDate, internalInd, benchInd);
		log.info("Getting Pageable Object");
		// get PageableObject
		Pageable pageable = ServiceUtils.getPageableObject(offSet, limit,null);
		log.info("Fetching workers");
		// get workers with specification and pagination
		Page<Worker> pageWorkers;
		if (specification != null) {
			pageWorkers = workerRepo.findAll(specification, pageable);
		} else {
			pageWorkers = workerRepo.findAll(pageable);
		}
		log.info("prepares workers summary");
		// prepare Summary if pageWorkers contains Data
		List<WorkerSummaryDTO> workerSummaryDTOList = new ArrayList<>();
		if (pageWorkers != null) {
			if (!pageWorkers.getContent().isEmpty()) {
				for (Worker worker : pageWorkers.getContent()) {
					workerSummaryDTOList.add(convertToSummary(worker));
				}
			}
			log.info("returns summary");
			return new PageImpl<>(workerSummaryDTOList, pageable, pageWorkers.getTotalElements());
		}
		// if pageWorkers is null then we send empty list
		else {
			log.info("returns summary");
			return new PageImpl<>(workerSummaryDTOList, pageable, 0);
		}
	}

	private void updateWorkAssignments(Worker updatableWorker, WorkerDTO workerDTO) {
		if(!updatableWorker.getJoiningDate().equals(workerDTO.getJoiningDate())){
			workeAssigmentRepo.deleteAll(updatableWorker.getWorkAssignment());
			updatableWorker.getWorkAssignment().clear();
			createWorkAssignment(updatableWorker,workerDTO);
			workeAssigmentRepo.saveAll(updatableWorker.getWorkAssignment());
		} else{
			List<WorkAssignment> workAssignments = updatableWorker.getWorkAssignment();
			workAssignments.sort(WorkAssignment.createLambdaComparatorByStartDate());
			WorkAssignment latestWorkAssignment = workAssignments.get(workAssignments.size()-1);
			Job latestJob = latestWorkAssignment.getJob();
			if((workerDTO.getDepartment() != null && !latestJob.getDepartment().getDeptID().equals(workerDTO.getDepartment().getDeptID())) || workerDTO.isBillable() != latestJob.isBillable()){
				Job job = getDepartment(workerDTO);
				if(latestWorkAssignment.getStartDate().equals(LocalDate.now())){
					latestWorkAssignment.setJob(job);
				} else{
					WorkAssignment workAssignment = new WorkAssignment();
					workAssignment.setJob(job);
					workAssignment.setStartDate(LocalDate.now());
					workAssignment.setExpectedStartDate(LocalDate.now());
					workAssignment.setWorker(updatableWorker);
					latestWorkAssignment.setEndDate(LocalDate.now().minusDays(1));
					workAssignments.add(workAssignment);
				}
				workeAssigmentRepo.saveAll(workAssignments);
				updatableWorker.setWorkAssignment(workAssignments);
			}
		}
	}

	private boolean updatePersonLegal(Worker updatableWorker, PersonLegalDTO newPersonLegal) {
		boolean updatable = false;
		// Validate the person legal and set the data
		PersonLegal updatablePersonLegal = updatableWorker.getPersonLegal();
		// Given name updated
		if (newPersonLegal.getGivenName() != null
				&& !(newPersonLegal.getGivenName().equals(updatablePersonLegal.getGivenName()))) {
			updatablePersonLegal.setGivenName(newPersonLegal.getGivenName());
			updatable = true;
		}
		// Middle name updated
		if (newPersonLegal.getMiddleName() != null
				&& !(newPersonLegal.getMiddleName().equals(updatablePersonLegal.getMiddleName()))) {
			updatablePersonLegal.setMiddleName(newPersonLegal.getMiddleName());
			updatable = true;
		}
		// Family Name updated
		if (newPersonLegal.getFamilyName() != null
				&& !(newPersonLegal.getFamilyName().equals(updatablePersonLegal.getFamilyName()))) {
			updatablePersonLegal.setFamilyName(newPersonLegal.getFamilyName());
			updatable = true;
		}
		// Birth Date updated
		if (newPersonLegal.getBirthDate() != null
				&& !(newPersonLegal.getBirthDate().equals(updatablePersonLegal.getBirthDate()))) {
			updatablePersonLegal.setBirthDate(newPersonLegal.getBirthDate());
			updatable = true;
		}
		// Gender updated
		if (newPersonLegal.getGender() != null
				&& !(newPersonLegal.getGender().equals(updatablePersonLegal.getGender()))) {
			updatablePersonLegal.setGender(newPersonLegal.getGender());
			updatable = true;
		}
		// maritalStatus updated
		if (newPersonLegal.getMaritalStatus() != null
				&& !(newPersonLegal.getMaritalStatus().equals(updatablePersonLegal.getMartialStatus()))) {
			updatablePersonLegal.setMartialStatus(newPersonLegal.getMaritalStatus());
			updatable = true;
		}
		// prefered name update
		if (newPersonLegal.getPreferredName() != null
				&& !(newPersonLegal.getPreferredName().equals(updatablePersonLegal.getPreferredName()))) {
			updatablePersonLegal.setPreferredName(newPersonLegal.getPreferredName());
			updatable = true;
		}
		return updatable;
	}

	@Override
	@Transactional
	public WorkerDocumentDTO uploadWorkerDocument(String workerID, WorkerDocumentDTO workerDocumentDTO) {
		Worker worker = workerValidator.getWorker(workerID);
		WorkerDocument workerDoc = workerValidator.validateWorkerDocument(workerDocumentDTO);
		if (workerDoc == null) {
			workerDoc = modelMapper.map(workerDocumentDTO, WorkerDocument.class);
			workerDoc.setWorker(worker);
			workerDoc.getWorkerDocAttributes().clear();
			workerDoc.setDocSource(EnumDocSource.UPLOADED);
		}
		workerDoc.setStatus(EnumDocStatus.PendingDocument);
		WorkerDocument savedWorkerDoc = workerDocumentRepo.save(workerDoc);
		List<WorkerDocAttributes> workerDocAttributesList = new ArrayList<>();
		if(workerDocumentDTO.getWorkerDocAttributes() != null) {
			workerDocumentDTO.getWorkerDocAttributes().forEach(workerDocAttrDTO -> {
				WorkerDocAttributes workDocAttr = modelMapper.map(workerDocAttrDTO, WorkerDocAttributes.class);
				workDocAttr.setWorkerDocument(savedWorkerDoc);
				workDocAttr.setDocAttributeDef(
						docAttrDefRepo.findById(workDocAttr.getDocAttributeDef().getDocumentAttrDefID()).get());
				workerDocAttributesList.add(workDocAttr);
			});
		}
		if(!workerDocAttributesList.isEmpty()){
			workerDocAttrRepo.saveAll(workerDocAttributesList);
		}
		List<WorkerDocAttributesDTO> workerDocAttributesDTOList = new ArrayList<>();
		for(WorkerDocAttributes workerDocAttributes:workerDocAttributesList){
			workerDocAttributesDTOList.add(modelMapper.map(workerDocAttributes,WorkerDocAttributesDTO.class));
		}
		WorkerDocumentDTO responceDto = modelMapper.map(savedWorkerDoc, WorkerDocumentDTO.class);
		responceDto.setWorkerDocAttributes(workerDocAttributesDTOList);
		return responceDto;
	}

	@Override
	public byte[] retrieveWorkerDocFile(WorkerDocument workerDocument) {
		byte[] filedata;
		try {
			filedata = azureBlobService.getFile(workerDocument.getUrl(),EnumAzureContainers.worker);
		} catch (URISyntaxException e) {
			log.error("Error while reading file from storage ");
			throw new RuntimeException(e);
		}
		return filedata;
	}

	@Override
	public WorkerDocument retrieveWorkerDocument(String workerdocID) {
		Optional<WorkerDocument> workerDocument = workerDocumentRepo.findById(workerdocID);
		if (workerDocument.isEmpty()) {
			throw new EntityNotFoundException("Worker document by Id " + workerdocID + " not found");
		}
		return workerDocument.get();
	}

	@Override
	public WorkerCountsDTO getWorkerCounts(Boolean status, Boolean workerType, Boolean billable,
										   LocalDate effectiveDate) {
		WorkerCountsDTO responseDTO = new WorkerCountsDTO();
		if (Objects.isNull(effectiveDate)) {
			effectiveDate = LocalDate.now();
		}
		responseDTO.setTotalWorkers(workerRepo.count());
		if (Objects.nonNull(workerType) && workerType) {
			responseDTO.setWorkerTypes(workerRepo.countByWorkerType());
		}
		if (Objects.nonNull(status) && status) {
			responseDTO.setWorkerStatus(workerStatusRepo.countByWorkerStatus(effectiveDate));
		}
		if (Objects.nonNull(billable) && billable) {
			responseDTO.setWorkerBillable(workerRepo.countByBillable(effectiveDate));
		}
		return responseDTO;
	}

	@Override
	public WorkerDTO updateWorkerStatus(String workerId, WorkerStatusDTO newStatusDTO) {
		log.debug("Finding worker by id:" + workerId);
		Worker worker = workerValidator.getWorker(workerId);
		log.debug("Worker found with id:" + workerId);
		WorkerStatusCode workerStatusCode = newStatusDTO.getStatus();
		if(workerStatusCode.equals(WorkerStatusCode.PENDING) || workerStatusCode.equals(WorkerStatusCode.ACTIVE)){
			throw new InvalidDataException(ErrorCodes.W_BV_0005,"worker","workerStatus",workerStatusCode.toString(),workerStatusCode+" status not allowed");
		}
		if(workerStatusCode.equals(WorkerStatusCode.INACTIVE)){
			List<WorkerStatus> workerStatusList = workerStatusRepo.getWorkerStatusAfter(workerId,LocalDate.now());
			if(!workerStatusList.isEmpty()){
				workerStatusRepo.deleteAll(workerStatusList);
			}
		}
		WorkerInActiveStatusCodes workerInActiveStatusCode = null;
		if (workerStatusCode.equals(WorkerStatusCode.INACTIVE)) {
			workerInActiveStatusCode = workerValidator.getWorkerInactiveStatusCode(newStatusDTO);
		}
		List<WorkerStatus> workerStatusEntityList = worker.getWorkerStatus();
		workerStatusEntityList.sort(WorkerStatus.createWorkerStatusLambdaComparator());

		WorkerStatus latestStatus = workerStatusEntityList.get(workerStatusEntityList.size() - 1);
		workerValidator.validateStatus(latestStatus, newStatusDTO);
		if (latestStatus.getEffectiveDate().equals(LocalDate.now())) {
			latestStatus.setStatus(workerStatusCode);
			latestStatus.setWorkerInActiveStatusCode(workerInActiveStatusCode);
		} else {
			WorkerStatus workerStatus = new WorkerStatus();
			workerStatus.setWorker(worker);
			workerStatus.setEffectiveDate(LocalDate.now());
			workerStatus.setStatus(workerStatusCode);
			workerStatus.setWorkerInActiveStatusCode(workerInActiveStatusCode);
			workerStatusEntityList.add(workerStatus);
		}
		workerStatusRepo.saveAll(workerStatusEntityList);
		return workerHelper.prepareResponseData(workerValidator.getWorker(workerId), false);
	}

	@Override
	public String uploadWorkerDocFile(String workerdocID, MultipartFile file) {
		WorkerDocument workerDoc = workerValidator.getWorkerDocForUpload(workerdocID);
		String personName = workerDoc.getWorker().getPersonLegal().getGivenName()+" "+workerDoc.getWorker().getPersonLegal().getFamilyName();
		String fileName = getFileName(workerDoc.getWorker().getWorkerID(),personName,workerDoc.getDocumentDef().getDocumentName(),workerdocID);
		try {
			azureBlobService.upload(file, fileName, EnumAzureContainers.worker);
		} catch (IOException e) {
			log.error("Error while storing MultipartFile ");
			// throw new InvalidDataException(ErrorCodes.W_BV_0010, "WorkerDocuent", "File
			// data", null, e.getMessage());
			throw new RuntimeException(e);
		}
		workerDoc.setFileName(file.getOriginalFilename());
		workerDoc.setFileType(file.getContentType());
		workerDoc.setStatus(EnumDocStatus.Active);
		workerDoc.setUrl(fileName);
		workerDocumentRepo.save(workerDoc);
		return file.getOriginalFilename() + "Uploaded sucessfully ";
	}

	private String getFileName(String workerId,String personName,String documentName,String docId){
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("yyyyMMdd");
		return workerId+"/"+documentName+"|"+personName+"|"+docId+"|"+dateFormatter.format(localDate);
	}

	@Override
	@Transactional
	public String deleteDocument(String workerdocID) {
		WorkerDocument workerDocument = workerValidator.getWorkerDoc(workerdocID);
		// delete document from storage
		if (!StringUtils.isEmpty(workerDocument.getUrl())) {
			azureBlobService.deleteBlob(workerDocument.getUrl(),EnumAzureContainers.worker);
		}
		// delete all attributes
		workerDocAttrRepo.deleteAll(workerDocument.getWorkerDocAttributes());
		// If Document is Auto assigned, then delete only document from storage, and
		// make the document name , other fields to null which are set while create
		// document and delete attributes and move the status Pending
		if (workerDocument.getDocSource().compareTo(EnumDocSource.AUTOASSIGNED) == 0) {
			workerHelper.resetValues(workerDocument);
			workerDocumentRepo.save(workerDocument);
		}
		// If Document is not Auto assigned delete the document form storage and delete
		// the worker doc and attributes
		else {
			workerDocumentRepo.delete(workerDocument);
		}
		return "Document by id:" + workerdocID + " deleted successfully";
	}

	@Override
	public WorkerDocumentDTO getWorkerDocument(String workerdocID) {
		return modelMapper.map(retrieveWorkerDocument(workerdocID), WorkerDocumentDTO.class);
	}

	private WorkerSummaryDTO convertToSummary(Worker worker) {
		WorkerSummaryDTO workerSummary = new WorkerSummaryDTO();
		workerSummary.setWorkerID(worker.getWorkerID());
		workerSummary.setWorkerCode(worker.getWorkerCode());
		RelatedWorkerTypeDTO workerTypeDTO = new RelatedWorkerTypeDTO();
		workerTypeDTO.setWorkerTypeCode(worker.getWorkerType().getWorkerTypeCode());
		workerTypeDTO.setName(worker.getWorkerType().getWorkerTypeName());
		workerSummary.setWorkerType(workerTypeDTO);
		workerSummary.setJoiningDate(worker.getJoiningDate());
		PersonLegalSummaryDTO prsnDto = new PersonLegalSummaryDTO(worker.getPersonLegal().getPersonLegalID(),
				worker.getPersonLegal().getGivenName(), worker.getPersonLegal().getMiddleName(),
				worker.getPersonLegal().getFamilyName(), worker.getPersonLegal().getPreferredName());
		workerSummary.setPersonLegal(prsnDto);
		List<WorkerStatus> workerStatusList = workerStatusRepo.getWorkerStatusByDate(LocalDate.now(),worker.getWorkerID());
		WorkerCurrentStatusDTO workerStatus = new WorkerCurrentStatusDTO(
				workerStatusList.get(0).getWorkerStatusId(), workerStatusList.get(0).getStatus(),
				workerStatusList.get(0).getEffectiveDate());
		workerSummary.setWorkerStatus(workerStatus);
		return workerSummary;
	}

}
