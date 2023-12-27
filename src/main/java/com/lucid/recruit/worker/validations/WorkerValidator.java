package com.lucid.recruit.worker.validations;

import java.time.LocalDate;
import java.util.*;

import com.lucid.core.entity.BasePhoneNbr;
import com.lucid.core.validators.AddressValidator;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.entity.WorkerAttrDefListValues;
import com.lucid.recruit.attr.entity.WorkerAttributeDef;
import com.lucid.recruit.attr.repo.WorkerAttrDefListValuesRepo;
import com.lucid.recruit.attr.repo.WorkerAttributeDefRepo;
import com.lucid.recruit.attr.validations.BaseAttributeValidator;
import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.org.entity.OrganizationStatus;
import com.lucid.recruit.person.dto.*;
import com.lucid.recruit.person.entity.*;
import com.lucid.recruit.referencedata.entity.Country;
import com.lucid.recruit.worker.dto.*;
import com.lucid.recruit.worker.entity.*;
import com.lucid.recruit.worker.service.WorkerSequenceService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.dto.BaseDocAttributesDTO;
import com.lucid.recruit.docs.entity.DocAttributeDef;
import com.lucid.recruit.docs.entity.DocumentDef;
import com.lucid.recruit.docs.validations.DocumentDefValidatior;
import com.lucid.recruit.org.entity.Organization;
import com.lucid.recruit.org.repo.OrganizationRepo;
import com.lucid.recruit.referencedata.entity.TenantParameters;
import com.lucid.recruit.referencedata.entity.WorkerInActiveStatusCodes;
import com.lucid.recruit.referencedata.entity.WorkerType;
import com.lucid.recruit.referencedata.repo.TenantParametersRepo;
import com.lucid.recruit.referencedata.repo.WorkerInActiveStatusCodesRepo;
import com.lucid.recruit.referencedata.repo.WorkerTypeRepo;
import com.lucid.recruit.worker.repo.WorkerDocumentRepository;
import com.lucid.recruit.worker.repo.WorkerRepository;
import com.lucid.recruit.worker.service.WorkAssignmentComparator;

@Component
public class WorkerValidator {
	private static final Logger log = LoggerFactory.getLogger(WorkerValidator.class);
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private WorkerTypeRepo workerTypeRepo;
	@Autowired
	private WorkerDocumentRepository workerDocRepo;
	@Autowired
	private DocumentDefValidatior docDefValidator;
	@Autowired
	private OrganizationRepo organizationRepo;
	@Autowired
	private TenantParametersRepo tenantParametersRepo;
	@Autowired
	private WorkerInActiveStatusCodesRepo workerInActiveStatusCodeRepo;

	@Autowired
	private Validator validator;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AddressValidator addressValidator;

	@Autowired
	private WorkerSequenceService workerSequenceService;

	@Autowired
	private WorkerAttributeDefRepo workerAttributeDefRepo;

	@Autowired
	private WorkerAttrDefListValuesRepo workerAttrDefListValuesRepo;

	/**
	 * @param workerDTO
	 */
	public Worker validateWorker(WorkerDTO workerDTO, Worker worker) {
		if(worker == null){
			worker = new Worker();
			worker.setCreatedDt(LocalDate.now());
			worker.setJoiningDate(workerDTO.getJoiningDate());
		}
		WorkerType workerType = null;
		if(workerDTO.getWorkerType() != null && workerDTO.getWorkerType().getWorkerTypeCode() != null){
			workerType = fetchWorkerType(workerDTO.getWorkerType().getWorkerTypeCode());
			worker.setWorkerType(workerType);
		}
		//validate organization
		if(!worker.getWorkerType().getWorkerTypeCode().equals(WorkerTypeCode.WORKER_W2)) {
			if (workerDTO.getOrganization() != null && workerDTO.getOrganization().getOrganizationID() != null) {
				Organization organization = fetchOrganization(workerDTO.getOrganization().getOrganizationID());
				List<OrganizationStatus>  organizationStatuses = organization.getStatus();
				organizationStatuses.sort(OrganizationStatus.createOrgStatusLambdaComparator());
				OrganizationStatus latestStatus =  organizationStatuses.get(organizationStatuses.size()-1);
				if(!latestStatus.getStatusCode().equals(OrgStatus.ACTIVE)){
					throw new InvalidDataException(ErrorCodes.W_BV_0002,"worker","organization",workerDTO.getOrganization().getOrganizationID(),"Organization not in active state to create "+worker.getWorkerType().getWorkerTypeCode()+" worker");
				}
				worker.setOrganization(organization);
			}
		} else{
			Optional<TenantParameters> optionalTenantParameters = tenantParametersRepo.findById("TenantOrganizationId");
			if(optionalTenantParameters.isPresent()){
				try {
					Organization tenantOrganization = fetchOrganization(optionalTenantParameters.get().getValue());
					worker.setOrganization(tenantOrganization);
				} catch (Exception e){
					throw new InvalidDataException(ErrorCodes.W_BV_0001,"worker","organization","organization","Organization with configured tenantOrganizationId:"+optionalTenantParameters.get().getValue()+" not found");
				}
			} else{
				throw new InvalidDataException(ErrorCodes.W_BV_0001,"worker","organization","organization","TenantId not configured to create:"+WorkerTypeCode.WORKER_W2.toString()+" worker");
			}
		}
		if(workerDTO.getJoiningDate() != null) {
			if (!worker.getJoiningDate().equals(workerDTO.getJoiningDate())) {
				worker.setJoiningDate(workerDTO.getJoiningDate());
			}
		}
		worker.setUpdatedDt(LocalDate.now());
		return worker;
	}

	public void validateEmail(Worker worker,String emailID) {
		Worker workerByEmail = workerRepo.findByEmailID(emailID);
		if (workerByEmail != null) {
			if(worker.getWorkerID() == null || !worker.getWorkerID().equals(workerByEmail.getWorkerID())) {
				List<WorkerStatus> workerStatusList = worker.getWorkerStatus();
				workerStatusList.sort(WorkerStatus.createWorkerStatusLambdaComparator());
				if(!workerStatusList.get(workerStatusList.size()-1).getStatus().equals(WorkerStatusCode.INACTIVE)){
					log.debug("Given mailId: " + emailID + " already associated with another worker");
					throw new InvalidDataException(ErrorCodes.W_BV_0001, "personContactDetails", "emailId", emailID,
							"Given mailId: " + emailID + " already associated with another worker");
				}
			}
		}
	}

	/**
	 * Get the worker entity for give worker, not found raise exception
	 *
	 * @param workerID
	 * @return
	 */
	public Worker getWorker(String workerID) {
		Optional<Worker> optionalWorker = workerRepo.findById(workerID);
		if (optionalWorker.isEmpty()) {
			log.error("Worker by id " + workerID + " not found");
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Worker by id " + workerID + " not found");
		}
		return optionalWorker.get();

	}

	/**
	 * Check the work assignment start dates have start date if not end the dates
	 *
	 * @param wrkAssignmentList
	 */
	public void validateWorkAssignment(List<WorkAssignment> wrkAssignmentList) {
		// sort the list
		wrkAssignmentList.sort(WorkAssignmentComparator.createWSComparator());
		WorkAssignment prvAssignment = null;
		for (Iterator<WorkAssignment> iterator = wrkAssignmentList.iterator(); iterator.hasNext();) {
			WorkAssignment workAssignment = (WorkAssignment) iterator.next();
			if ((prvAssignment != null)) {

				if ((workAssignment.getStartDate().compareTo(prvAssignment.getStartDate()) == 0)) {
					log.error("Work Assignemt have same start dates Start Date: " + workAssignment.getStartDate()
							+ " for work assigment ID " + workAssignment.getWorkerAssgmtId()
							+ " and for work assigment ID " + prvAssignment.getWorkerAssgmtId());
					throw new InvalidDataException(ErrorCodes.W_BV_0006, "workAssignment", "startDate",
							workAssignment.getStartDate().toString(),
							"Work Assignemt have same start dates Start Date: " + workAssignment.getStartDate()
									+ " for work assigment ID " + workAssignment.getWorkerAssgmtId()
									+ " and for work assigment ID " + prvAssignment.getWorkerAssgmtId());

				}
				prvAssignment.setEndDate(workAssignment.getStartDate().minusDays(1));
			}
			prvAssignment = workAssignment;
		}
	}

	public void validatePersonContactDetails(Worker worker, PersonLegalDTO personLegalDTO) {
		List<PersonContactDetails> personContactEntityList = worker.getPersonLegal().getPersonContactDetails();
		if(personContactEntityList == null){
			personContactEntityList = new ArrayList<>();
		}
		// separate primary and secondary contacts
		List<PersonContactDetails> primaryContactDetails = new ArrayList<>();
		primaryContactDetails.sort(PersonContactDetails.createPersonContactLambdaComparator());
		List<PersonContactDetails> secondaryContactDetails = new ArrayList<>();
		secondaryContactDetails.sort(PersonContactDetails.createPersonContactLambdaComparator());
		for (PersonContactDetails personContactDetails : personContactEntityList) {
			if (personContactDetails.getContactType().equals(ContactType.PRIMARY)) {
				primaryContactDetails.add(personContactDetails);
			} else {
				secondaryContactDetails.add(personContactDetails);
			}
		}
		//handle primaryContact
		if(personLegalDTO.getPrimaryContactDetails() != null && !personLegalDTO.getPrimaryContactDetails().isEmpty()){
			PersonContactDetailsDTO personContactDetailsDTO = personLegalDTO.getPrimaryContactDetails().get(0);
			Set<ConstraintViolation<PersonContactDetailsDTO>> violations = validator.validate(personContactDetailsDTO);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}
			PersonContactDetails newContactDetails = modelMapper.map(personContactDetailsDTO,PersonContactDetails.class);
			newContactDetails.setContactType(ContactType.PRIMARY);
			newContactDetails.setStartDate(LocalDate.now());
			newContactDetails.setPersonLegal(worker.getPersonLegal());

			if(!primaryContactDetails.isEmpty()) {
				PersonContactDetails latestContact = primaryContactDetails.get(primaryContactDetails.size() - 1);
				if (!latestContact.equals(newContactDetails)) {
					if (latestContact.getStartDate().equals(LocalDate.now())) {
						if (!newContactDetails.getEmailId().equals(latestContact.getEmailId())) {
							validateEmail(worker, newContactDetails.getEmailId());
						}
						latestContact.setEmailId(newContactDetails.getEmailId());
						latestContact.setPhoneNumber(newContactDetails.getPhoneNumber());
					} else {
						primaryContactDetails.add(newContactDetails);
						latestContact.setEndDate(LocalDate.now().minusDays(1));
					}
				}
			}
			else{
				validateEmail(worker, newContactDetails.getEmailId());
				primaryContactDetails.add(newContactDetails);
			}
		}
		//handle secondaryContacts
		if(personLegalDTO.getSecondaryContactDetails() != null && !personLegalDTO.getSecondaryContactDetails().isEmpty()){
            //first delete exising contacts
			for (PersonContactDetails personContactDetails : secondaryContactDetails) {
				boolean exists = false;
				for (PersonContactDetailsDTO personContactDetailsDTO : personLegalDTO.getSecondaryContactDetails()) {
					if (personContactDetailsDTO.getPersonLegalContactID() != null
							&& personContactDetails.getPersonLegalContactID().equals(personContactDetailsDTO.getPersonLegalContactID())) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					personContactDetails.setEndDate(LocalDate.now());
				}
			}
			for(PersonContactDetailsDTO personContactDetailsDTO:personLegalDTO.getSecondaryContactDetails()){
				Set<ConstraintViolation<PersonContactDetailsDTO>> violations = validator.validate(personContactDetailsDTO);
				if (!violations.isEmpty()) {
					throw new ConstraintViolationException(violations);
				}
				if(personContactDetailsDTO.getPersonLegalContactID() != null){
					PersonContactDetails updatableContact = null;
					for(PersonContactDetails personContactDetails:secondaryContactDetails){
						if(personContactDetails.getPersonLegalContactID().equals(personContactDetailsDTO.getPersonLegalContactID())){
							updatableContact = personContactDetails;
							break;
						}
					}
					if(updatableContact == null){
                      throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContactDetails by id:"+personContactDetailsDTO.getPersonLegalContactID()+" not found");
					}
					updatableContact.setEmailId(personContactDetailsDTO.getEmailId());
					updatableContact.setPhoneNumber(modelMapper.map(personContactDetailsDTO.getPhoneNumber(), BasePhoneNbr.class));
				}
				else{
                   PersonContactDetails newContactDetails = modelMapper.map(personContactDetailsDTO,PersonContactDetails.class);
				   newContactDetails.setContactType(ContactType.SECONDARY);
				   boolean createNew = true;
				   for(PersonContactDetails contactDetails:secondaryContactDetails){
					   if(contactDetails.equals(newContactDetails)){
						   if(contactDetails.getEndDate() == null) {
							   throw new InvalidDataException(ErrorCodes.W_BV_0012, "worker", "perosnLegal", "contactDetails", "Duplicate contactDetails not allowed");
						   }
						   else if(contactDetails.getEndDate().isEqual(LocalDate.now())){
							   contactDetails.setEndDate(null);
							   createNew = false;
						   }
						   break;
					   }
				   }
				   if(createNew){
					   newContactDetails.setPersonLegal(worker.getPersonLegal());
					   newContactDetails.setStartDate(LocalDate.now());
					   secondaryContactDetails.add(newContactDetails);
				   }
				}
			}
		}
		//combine both primary and secondary contacts
		List<PersonContactDetails> updatableContactDetails = new ArrayList<>();
		updatableContactDetails.addAll(primaryContactDetails);
		updatableContactDetails.addAll(secondaryContactDetails);
		worker.getPersonLegal().setPersonContactDetails(updatableContactDetails);
	}

	public void validatePersonAddress(Worker updatableWorker, PersonLegalDTO personLegal) {
		List<PersonAddress> personAddressesEntityList = updatableWorker.getPersonLegal().getPersonAddress();
		if (personAddressesEntityList == null) {
			personAddressesEntityList = new ArrayList<>();
			updatableWorker.getPersonLegal().setPersonAddress(personAddressesEntityList);
		}
		List<PersonAddressDTO> personAddressDTOList = personLegal.getPersonAddress();
		// first remove deleted addresses
		for (PersonAddress personAddress : personAddressesEntityList) {
			boolean exists = false;
			for (PersonAddressDTO personAddressDTO : personAddressDTOList) {
				if (personAddressDTO.getPersonAddressID() != null
						&& personAddress.getPersonAddressID().equals(personAddressDTO.getPersonAddressID())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				personAddress.setEndDate(LocalDate.now());
			}
		}
		// if address id is not passed then create otherwise update
		for (PersonAddressDTO personAddressDTO : personAddressDTOList) {
			// validate personAddressDTO
			Set<ConstraintViolation<PersonAddressDTO>> violations = validator.validate(personAddressDTO);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}

			PersonAddress newPersonAddress = modelMapper.map(personAddressDTO,PersonAddress.class);
			if (personAddressDTO.getPersonAddressID() != null) {
				PersonAddress updatablePersonAddress = null;
				for (PersonAddress personAddress : personAddressesEntityList) {
					if (personAddressDTO.getPersonAddressID().equals(personAddress.getPersonAddressID())) {
						updatablePersonAddress = personAddress;
						break;
					}
				}
				if (updatablePersonAddress == null) {
					log.error("PersonAddress by id:" + personAddressDTO.getPersonAddressID() + " not found");
					throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
							"PersonAddress by id:" + personAddressDTO.getPersonAddressID() + " not found");
				}
				if (!updatablePersonAddress.equals(newPersonAddress)) {
					updatablePersonAddress.setAddressName(personAddressDTO.getAddressName());
					updatablePersonAddress.setAddress1(personAddressDTO.getAddress1());
					updatablePersonAddress.setAddress2(personAddressDTO.getAddress2());
					updatablePersonAddress.setAddress3(personAddressDTO.getAddress3());
					updatablePersonAddress.setAddress4(personAddressDTO.getAddress4());
					updatablePersonAddress.setAddress5(personAddressDTO.getAddress5());
					updatablePersonAddress.setPostalCode(personAddressDTO.getPostalCode());
					updatablePersonAddress.setPostOfficeBox(personAddressDTO.getPostOfficeBox());
					if(personAddressDTO.getCountry() != null) {
						updatablePersonAddress.setCountry(modelMapper.map(personAddressDTO.getCountry(), Country.class));
					}
					updatablePersonAddress.setAddressType(personAddressDTO.getAddressType());
					addressValidator.validateAddress(updatablePersonAddress);
				}
			} else {
				newPersonAddress.setStartDate(LocalDate.now());
				newPersonAddress.setPersonLegal(updatableWorker.getPersonLegal());
				addressValidator.validateAddress(newPersonAddress);
				PersonAddress existingAddress = null;
				for (PersonAddress personAddress : personAddressesEntityList) {
					if (personAddress.equals(newPersonAddress)) {
						existingAddress = personAddress;
						break;
					}
				}
				if (existingAddress != null) {
					if (existingAddress.getEndDate() != null && existingAddress.getEndDate().equals(LocalDate.now())) {
						existingAddress.setEndDate(null);
					}
				} else {
					personAddressesEntityList.add(newPersonAddress);
				}
			}
		}
	}

	/**
	 * @param updatableWorker
	 * @param personLegal
	 */
	public void validatePersonBankDetails(Worker updatableWorker, PersonLegalDTO personLegal) {
		List<PersonBankDetails> personBankDetailsEntityList = updatableWorker.getPersonLegal().getPersonBankDetails();
		if (personBankDetailsEntityList == null) {
			personBankDetailsEntityList = new ArrayList<>();
			updatableWorker.getPersonLegal().setPersonBankDetails(personBankDetailsEntityList);
		}
		List<PersonBankDetailsDTO> personBankDetailsDTOList = personLegal.getPersonBankDetails();
		// first remove deleted addresses
		for (PersonBankDetails personBankDetails : personBankDetailsEntityList) {
			boolean exists = false;
			for (PersonBankDetailsDTO personBankDetailsDTO : personBankDetailsDTOList) {
				if (personBankDetailsDTO.getPersonBankDetailsID() != null && personBankDetails.getPersonBankDetailsID()
						.equals(personBankDetailsDTO.getPersonBankDetailsID())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				personBankDetails.setEndDate(LocalDate.now());
			}
		}
		// if address id is not passed then create otherwise update
		for (PersonBankDetailsDTO personBankDetailsDTO : personBankDetailsDTOList) {
			// validate personAddressDTO
			Set<ConstraintViolation<PersonBankDetailsDTO>> violations = validator.validate(personBankDetailsDTO);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}

			PersonBankDetails newBankDetails = modelMapper.map(personBankDetailsDTO,PersonBankDetails.class);
			if (personBankDetailsDTO.getPersonBankDetailsID() != null) {
				PersonBankDetails updatablePersonBankDetails = null;
				for (PersonBankDetails personBankDetails : personBankDetailsEntityList) {
					if (personBankDetailsDTO.getPersonBankDetailsID()
							.equals(personBankDetails.getPersonBankDetailsID())) {
						updatablePersonBankDetails = personBankDetails;
						break;
					}
				}
				if (updatablePersonBankDetails == null) {
					log.error(
							"PersonBankDetails by id:" + personBankDetailsDTO.getPersonBankDetailsID() + " not found");
					throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
							"PersonBankDetails by id:" + personBankDetailsDTO.getPersonBankDetailsID() + " not found");
				}
				if (!updatablePersonBankDetails.equals(newBankDetails)) {
					updatablePersonBankDetails.setEndDate(LocalDate.now());
					newBankDetails.setStartDate(LocalDate.now());
					newBankDetails.setPersonLegal(updatableWorker.getPersonLegal());
					personBankDetailsEntityList.add(newBankDetails);
				}
			} else {
				newBankDetails.setStartDate(LocalDate.now());
				newBankDetails.setPersonLegal(updatableWorker.getPersonLegal());
				personBankDetailsEntityList.add(newBankDetails);
			}
		}
	}

	public void validatePersonEmergencyContact(Worker updatableWorker, PersonLegalDTO personLegal) {
		List<PersonEmergencyContact> personEmergencyContactEntityList = updatableWorker.getPersonLegal()
				.getPersonEmergencyContact();
		if (personEmergencyContactEntityList == null) {
			personEmergencyContactEntityList = new ArrayList<>();
			updatableWorker.getPersonLegal().setPersonEmergencyContact(personEmergencyContactEntityList);
		}
		List<PersonEmergencyContactDTO> persoEmergencyContactDTOList = personLegal.getPersonEmergencyContact();
		// first remove deleted emergencyContact
		for (PersonEmergencyContact personEmergencyContact : personEmergencyContactEntityList) {
			boolean exists = false;
			for (PersonEmergencyContactDTO persoEmergencyContactDTO : persoEmergencyContactDTOList) {
				if (persoEmergencyContactDTO.getPersonEmergContactId() != null && personEmergencyContact
						.getPersonEmergContactId().equals(persoEmergencyContactDTO.getPersonEmergContactId())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				personEmergencyContact.setEndDate(LocalDate.now());
			}
		}
		// if address id is not passed then create otherwise update
		for (PersonEmergencyContactDTO personEmergenctContactDTO : persoEmergencyContactDTOList) {
			// validate personEmergenctContactDTO
			Set<ConstraintViolation<PersonEmergencyContactDTO>> violations = validator
					.validate(personEmergenctContactDTO);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}

			PersonEmergencyContact newPersonEmergencyContact = modelMapper.map(personEmergenctContactDTO,PersonEmergencyContact.class);
			if (personEmergenctContactDTO.getPersonEmergContactId() != null) {
				PersonEmergencyContact updatablePersonEmergencyContactc = null;
				for (PersonEmergencyContact personEmergencyContact : personEmergencyContactEntityList) {
					if (personEmergenctContactDTO.getPersonEmergContactId()
							.equals(personEmergencyContact.getPersonEmergContactId())) {
						updatablePersonEmergencyContactc = personEmergencyContact;
						break;
					}
				}
				if (updatablePersonEmergencyContactc == null) {
					log.error("PersonEmergencyContact by id:" + personEmergenctContactDTO.getPersonEmergContactId()
							+ " not found");
					throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "PersonEmergencyContact by id:"
							+ personEmergenctContactDTO.getPersonEmergContactId() + " not found");
				}
				if (!updatablePersonEmergencyContactc.equals(newPersonEmergencyContact)) {
					updatablePersonEmergencyContactc.setFirstName(personEmergenctContactDTO.getFirstName());
					updatablePersonEmergencyContactc.setLastName(personEmergenctContactDTO.getFirstName());
					updatablePersonEmergencyContactc.setPhoneNumber(modelMapper.map(personEmergenctContactDTO.getPhoneNumber(), BasePhoneNbr.class));
					updatablePersonEmergencyContactc.setPhoneType(personEmergenctContactDTO.getPhoneType());
					updatablePersonEmergencyContactc.setEmailId(personEmergenctContactDTO.getEmailId());
					updatablePersonEmergencyContactc.setEmailType(personEmergenctContactDTO.getEmailType());
					updatablePersonEmergencyContactc.setRelation(personEmergenctContactDTO.getRelation());
				}
			} else {
				newPersonEmergencyContact.setStartDate(LocalDate.now());
				newPersonEmergencyContact.setPersonLegal(updatableWorker.getPersonLegal());
				personEmergencyContactEntityList.add(newPersonEmergencyContact);
			}
		}
	}

	public void validatePersonDependents(Worker updatableWorker, PersonLegalDTO personLegal) {
		List<PersonDependents> personDependantEntityList = updatableWorker.getPersonLegal().getPersonDependents();
		if (personDependantEntityList == null) {
			personDependantEntityList = new ArrayList<>();
			updatableWorker.getPersonLegal().setPersonDependents(personDependantEntityList);
		}
		List<PersonDependentsDTO> personDependantDTOList = personLegal.getPersonDependents();
		// first remove deleted emergencyContact
		for (PersonDependents personDependant : personDependantEntityList) {
			boolean exists = false;
			for (PersonDependentsDTO personDependantDTO : personDependantDTOList) {
				if (personDependant.getPersonDepId().equals(personDependantDTO.getPersonDepId())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				personDependant.setValidTo(LocalDate.now());
			}
		}
		// if dependant id is not passed then create otherwise update
		for (PersonDependentsDTO personDependantDTO : personDependantDTOList) {
			// validate personDependantDTO
			Set<ConstraintViolation<PersonDependentsDTO>> violations = validator.validate(personDependantDTO);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}

			PersonDependents newPersonDependant = modelMapper.map(personDependantDTO,PersonDependents.class);
			if (personDependantDTO.getPersonDepId() != null) {
				PersonDependents updatablePersonDependant = null;
				for (PersonDependents personDependant : personDependantEntityList) {
					if (personDependantDTO.getPersonDepId().equals(personDependant.getPersonDepId())) {
						updatablePersonDependant = personDependant;
						break;
					}
					if (updatablePersonDependant == null) {
						log.error("PersonDependant by id:" + personDependantDTO.getPersonDepId() + " not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
								"PersonDependant by id:" + personDependantDTO.getPersonDepId() + " not found");
					}
					if (!updatablePersonDependant.equals(newPersonDependant)) {
						updatablePersonDependant.setFirstName(personDependantDTO.getFirstName());
						updatablePersonDependant.setLastName(personDependantDTO.getFirstName());
						updatablePersonDependant.setBrithDate(personDependantDTO.getBrithDate());
						updatablePersonDependant.setRelation(personDependantDTO.getRelation());
					}
				}
			} else {
				newPersonDependant.setValidFrom(LocalDate.now());
				newPersonDependant.setPersonLegal(updatableWorker.getPersonLegal());
				personDependantEntityList.add(newPersonDependant);
			}
		}
	}

	public WorkerDocument validateWorkerDocument(WorkerDocumentDTO workerDocumentDTO) {
		// Check if worker doc ID i.e. if not null means uploading the auto assigned doc
		// And validate if the status of auto assigned is pending
		WorkerDocument workerDocEntity = null;
		if (workerDocumentDTO.getWorkerdocID() != null && !workerDocumentDTO.getWorkerdocID().isEmpty()) {
			Optional<WorkerDocument> workerDoc = workerDocRepo.findById(workerDocumentDTO.getWorkerdocID());
			if (workerDoc.isEmpty()) {
				log.error("Worker Document by id " + workerDocumentDTO.getWorkerdocID() + " not found");
				throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
						"Worker Document by id " + workerDocumentDTO.getWorkerdocID() + " not found");
			}
			if (workerDoc.get().getStatus().compareTo(EnumDocStatus.Pending) != 0) {
				log.error("Worker Document by id " + workerDocumentDTO.getWorkerdocID() + " not in pending status");
				throw new InvalidDataException(ErrorCodes.W_BV_0011, "workerDocument", "workerdocID",
						workerDocumentDTO.getWorkerdocID(),
						"Worker Document by id " + workerDocumentDTO.getWorkerdocID()
								+ " not in pending status, current status " + workerDoc.get().getStatus());
			}
			workerDocEntity = workerDoc.get();
		}
		// get the document definition
		DocumentDef documentDef = docDefValidator.getDocumentDef(workerDocumentDTO.getDocumentDef().getDocumentDefID());
		List<BaseDocAttributesDTO> baseAttrDTO = new ArrayList<>();
		workerDocumentDTO.getWorkerDocAttributes().forEach(workerDocAtrr -> {
			baseAttrDTO.add(workerDocAtrr);
		});
		docDefValidator.validateDocIssueAndExpiryDates(documentDef, workerDocumentDTO.getIssueDate(),
				workerDocumentDTO.getExpiryDate());
		// Now check the doc type and its related values
		docDefValidator.validateDocIssueAndExpiryDates(documentDef, workerDocumentDTO.getIssueDate(),
				workerDocumentDTO.getExpiryDate());
		// Now check all the mandatory attributes are passed
		List<DocAttributeDef> docAttrDef = docDefValidator.validateDocRequiredAttributes(documentDef, baseAttrDTO);
		// Now check the attributes are as per definition
		docDefValidator.validateDocAttributes(documentDef, docAttrDef, baseAttrDTO);
		return workerDocEntity;
	}

	public WorkerDocument getWorkerDocForUpload(String workerdocID) {
		Optional<WorkerDocument> workerDoc = workerDocRepo.findById(workerdocID);
		if (workerDoc.isEmpty()) {
			log.error("Worker Document by id " + workerdocID + " not found");
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Worker Document by id " + workerdocID + " not found");
		}
		if (workerDoc.get().getStatus().compareTo(EnumDocStatus.PendingDocument) != 0) {
			log.error("Worker Document by id " + workerdocID + " not in pending status");
			throw new InvalidDataException(ErrorCodes.W_BV_0011, "workerDocument", "workerdocID", workerdocID,
					"Worker Document by id " + workerdocID + " not in pending document status, current status "
							+ workerDoc.get().getStatus());
		}
		return workerDoc.get();
	}

	public WorkerDocument getWorkerDoc(String workerdocID) {
		Optional<WorkerDocument> workerDoc = workerDocRepo.findById(workerdocID);
		if (workerDoc.isEmpty()) {
			log.error("Worker Document by id " + workerdocID + " not found");
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Worker Document by id " + workerdocID + " not found");
		}
		return workerDoc.get();
	}

	public WorkerType fetchWorkerType(WorkerTypeCode workerTypeCode) {
		Optional<WorkerType> optionalWorkerType = workerTypeRepo.findById(workerTypeCode);
		if(optionalWorkerType.isEmpty()){
			log.error("WorkerType by workerTypeCode:"+workerTypeCode+" not found");
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkerType by workerTypeCode:"+workerTypeCode+" not found");
		}
		return optionalWorkerType.get();
	}

	public Organization fetchOrganization(String organizationID){
		Optional<Organization> optionalOrganization = organizationRepo.findById(organizationID);
		if(optionalOrganization.isEmpty()){
			log.error("Organization by id:"+organizationID+" not found");
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"Organization by id:"+organizationID+" not found");
		}
		return optionalOrganization.get();
	}

	public WorkerInActiveStatusCodes getWorkerInactiveStatusCode(WorkerStatusDTO workerStatusDTO) {
		return workerInActiveStatusCodeRepo.findById(workerStatusDTO.getWorkerInActiveStatusCode().getCode()).orElseThrow(() -> {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"WorkerInActiveStatusCodes " + workerStatusDTO.getWorkerInActiveStatusCode().getCode() + " not configured");
		});
	}

    public void validateStatus(WorkerStatus latestStatus, WorkerStatusDTO newStatusDTO) {
		if(newStatusDTO.getStatus().equals(WorkerStatusCode.ACTIVE)){
			if(latestStatus.getStatus().equals(WorkerStatusCode.INACTIVE) && !latestStatus.getWorkerInActiveStatusCode().isReinstate()){
				log.error("Cannot move to status " + newStatusDTO.getStatus() + " as status cannot be reinstated from "
						+ latestStatus.getStatus());
				throw new InvalidDataException(ErrorCodes.W_BV_0013, "workerStatus", "status", newStatusDTO.getStatus().toString(),
						"Cannot move to status " + newStatusDTO.getStatus() + " as status cannot be reinstated from "
								+ latestStatus.getStatus());
			}
		} else if(newStatusDTO.getStatus().equals(WorkerStatusCode.INACTIVE)){
			if(newStatusDTO.getWorkerInActiveStatusCode() == null){
				log.error("Required InActiveStatusCode to update status as "+newStatusDTO.getStatus());
				throw new InvalidDataException(ErrorCodes.W_BV_0013, "workerStatus", "status",
						newStatusDTO.getStatus().toString(),"Required InActiveStatusCode to update status as "+newStatusDTO.getStatus());
			}
		}
    }

	public void setWorkerCode(Worker worker) {
		String prefix=null;
		String separator = null;
		//find prefix
		Optional<TenantParameters> empCodePrefix = tenantParametersRepo.findById("Empcodeprefix");
		if(empCodePrefix.isPresent() && StringUtils.isNotBlank(empCodePrefix.get().getValue())){
			prefix = empCodePrefix.get().getValue().toUpperCase();
			Optional<TenantParameters> empCodeSeparator = tenantParametersRepo.findById("Empcodeseparator");
			if(empCodeSeparator.isPresent() && StringUtils.isNotBlank(empCodeSeparator.get().getValue())){
				separator = empCodeSeparator.get().getValue();
			}
		}
		StringBuilder empCode = new StringBuilder();
        if(StringUtils.isNotBlank(prefix)){
			empCode.append(prefix);
			if(StringUtils.isNotBlank(separator)){
				empCode.append(separator);
			}
		}
		empCode.append(workerSequenceService.getSequence());
		worker.setWorkerCode(empCode.toString());
	}

	public void validateWorkerAttributes(Worker worker, List<WorkerAttributesDTO> workerAttributesDTOS) {
		List<WorkerAttributes> workerAttributesList = worker.getWorkerAttributes();
		if(workerAttributesList == null){
			workerAttributesList = new ArrayList<>();
			worker.setWorkerAttributes(workerAttributesList);
		}
		List<WorkerAttributeDef> workerAttributeDefList = workerAttributeDefRepo.findMandatoryAttributes(worker.getWorkerType().getWorkerTypeCode(),EnumAttributeStatus.ACTIVE);
        validateRequiredAttributes(workerAttributeDefList,workerAttributesDTOS);
		if(workerAttributesDTOS != null && !workerAttributesDTOS.isEmpty()) {
			for (WorkerAttributesDTO workerAttributesDTO : workerAttributesDTOS) {
				Set<ConstraintViolation<WorkerAttributesDTO>> constraintViolations = validator.validate(workerAttributesDTO);
				if (!constraintViolations.isEmpty()) {
					throw new ConstraintViolationException(constraintViolations);
				}
				WorkerAttributeDef workerAttributeDef = getWorkerAttributeDef(workerAttributesDTO.getWorkerAttributeDef().getAttrDefId());
				if (workerAttributesDTO.getWorkerAttrID() != null) {
					WorkerAttributes updatableWorkerAttributes = null;
					for (WorkerAttributes workerAttributes : workerAttributesList) {
						if (workerAttributes.getWorkerAttrID() != null && workerAttributesDTO.getWorkerAttrID().equals(workerAttributes.getWorkerAttrID())) {
							updatableWorkerAttributes = workerAttributes;
							break;
						}
					}
					if (updatableWorkerAttributes == null) {
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "WorkerAttribute by id:" + workerAttributesDTO.getWorkerAttrID() + " not found");
					} else {
						if (!updatableWorkerAttributes.getAttrValue().equals(workerAttributesDTO.getAttrValue())) {
							if (workerAttributeDef.getDefinedList()) {
								Optional<WorkerAttrDefListValues> attributeListValue= workerAttrDefListValuesRepo.getAttributeListValues(workerAttributeDef.getAttrDefId(),workerAttributesDTO.getAttrValue());
								if (attributeListValue.isEmpty()) {
									throw new InvalidDataException(ErrorCodes.W_ATTR_V_0004, "workerAttributes", "attrValue", workerAttributesDTO.getAttrValue(), " AttrValue " + workerAttributesDTO.getAttrValue() + " not in predefined list");
								}
							}
							BaseAttributeValidator.validateAttrTypeValue(workerAttributeDef.getAttrType(), workerAttributesDTO.getAttrValue());
							if (updatableWorkerAttributes.getStartDate().equals(LocalDate.now())) {
								updatableWorkerAttributes.setAttrValue(workerAttributesDTO.getAttrValue());
								updatableWorkerAttributes.setWorkerAttributeDef(workerAttributeDef);
							} else {
								updatableWorkerAttributes.setEndDate(LocalDate.now().minusDays(1));
								WorkerAttributes workerAttributes = new WorkerAttributes();
								workerAttributes.setWorkerAttributeDef(workerAttributeDef);
								workerAttributes.setAttrValue(workerAttributesDTO.getAttrValue());
								workerAttributes.setStartDate(LocalDate.now());
								workerAttributes.setWorker(worker);
								workerAttributesList.add(workerAttributes);
							}
						}

					}
				} else {
					BaseAttributeValidator.validateAttrTypeValue(workerAttributeDef.getAttrType(), workerAttributesDTO.getAttrValue());
					WorkerAttributes workerAttributes = new WorkerAttributes();
					workerAttributes.setWorkerAttributeDef(workerAttributeDef);
					workerAttributes.setWorker(worker);
					if (workerAttributeDef.getDefinedList()) {
						if (workerAttributeDef.getStatus().equals(EnumAttributeStatus.ACTIVE)) {
							if (workerAttributeDef.getRequired() && StringUtils.isBlank(workerAttributesDTO.getAttrValue())) {
								throw new InvalidDataException(ErrorCodes.W_ATTR_V_0005, "workerAttributes", "attrValue", workerAttributesDTO.getAttrValue(), "AttrValue mandatory");
							}
						}
						Optional<WorkerAttrDefListValues> attributeListValue= workerAttrDefListValuesRepo.getAttributeListValues(workerAttributeDef.getAttrDefId(),workerAttributesDTO.getAttrValue());
						if (attributeListValue.isEmpty()) {
							throw new InvalidDataException(ErrorCodes.W_ATTR_V_0004, "workerAttributes", "attrValue", workerAttributesDTO.getAttrValue(), " AttrValue " + workerAttributesDTO.getAttrValue() + " not in predefined list");
						}
						workerAttributes.setAttrValue(workerAttributesDTO.getAttrValue());
					} else {
						if (workerAttributeDef.getStatus().equals(EnumAttributeStatus.ACTIVE)) {
							if (StringUtils.isBlank(workerAttributesDTO.getAttrValue())) {
								if (workerAttributeDef.getRequired()) {
									if (StringUtils.isBlank(workerAttributeDef.getDefaultValue())) {
										throw new InvalidDataException(ErrorCodes.W_ATTR_V_0005, "workerAttributes", "attrValue", workerAttributesDTO.getAttrValue(), "AttrValue mandatory");
									}
									workerAttributes.setAttrValue(workerAttributeDef.getDefaultValue());
								}
							} else {
								workerAttributes.setAttrValue(workerAttributesDTO.getAttrValue());
							}
						}
					}
					workerAttributes.setStartDate(LocalDate.now());
					workerAttributesList.add(workerAttributes);
				}
			}
		}
	}

	private void validateRequiredAttributes(List<WorkerAttributeDef> workerAttributeDefList, List<WorkerAttributesDTO> workerAttributesDTOS) {
		if(workerAttributeDefList != null && !workerAttributeDefList.isEmpty()){
			if(workerAttributesDTOS == null || workerAttributesDTOS.isEmpty()){
				throw new InvalidDataException(ErrorCodes.W_BV_0014,"worker","workerAttributes",null,"Mandatory attributes not passed");
			}
            for(WorkerAttributeDef workerAttributeDef:workerAttributeDefList){
				boolean matched = false;
				for(WorkerAttributesDTO workerAttributesDTO:workerAttributesDTOS){
					if(workerAttributesDTO.getWorkerAttributeDef() == null || workerAttributesDTO.getWorkerAttributeDef().getAttrDefId() == null){
						throw new InvalidDataException(ErrorCodes.W_BV_0015,"attributes","attributeDef",null,"attributeDef mandatory");
					}
					if(workerAttributesDTO.getWorkerAttributeDef().getAttrDefId().equals(workerAttributeDef.getAttrDefId())){
						matched = true;
					}
				}
				if(!matched){
					throw new InvalidDataException(ErrorCodes.W_BV_0016,"attributes","attributeDef",null,"Attribute "+workerAttributeDef.getAttrName()+" missed");
				}
			}
		}
	}

	private  boolean valueExistsInList(List<String> attrListValues, String attrValue) {
		return attrListValues.contains(attrValue);
	}

	public WorkerAttributeDef getWorkerAttributeDef(String attrDefId) {
		return workerAttributeDefRepo.findById(attrDefId).orElseThrow(() -> new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkerAttrDef by id:"+attrDefId+" not found"));
	}
}
