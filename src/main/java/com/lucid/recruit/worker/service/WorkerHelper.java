package com.lucid.recruit.worker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.lucid.recruit.org.dto.RelatedOrgDTO;
import com.lucid.recruit.person.entity.*;
import com.lucid.recruit.referencedata.dto.AddressFormatDTO;
import com.lucid.recruit.referencedata.dto.RelatedDepartmentDTO;
import com.lucid.recruit.referencedata.entity.Department;
import com.lucid.recruit.referencedata.entity.Job;
import com.lucid.recruit.referencedata.service.AddressFormatService;
import com.lucid.recruit.worker.dto.*;
import com.lucid.recruit.worker.entity.*;
import com.lucid.recruit.worker.repo.WorkerStatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.org.dto.OrganizationDTO;
import com.lucid.recruit.person.dto.PersonAddressDTO;
import com.lucid.recruit.person.dto.PersonBankDetailsDTO;
import com.lucid.recruit.person.dto.PersonContactDetailsDTO;
import com.lucid.recruit.person.dto.PersonDependentsDTO;
import com.lucid.recruit.person.dto.PersonEmergencyContactDTO;
import com.lucid.recruit.person.dto.PersonLegalDTO;
import com.lucid.recruit.referencedata.dto.RelatedWorkerTypeDTO;
import org.springframework.stereotype.Service;

@Service
public class WorkerHelper {

	@Autowired
	private  ModelMapper modelMapper;

	@Autowired
	private AddressFormatService addressFormatService;

	@Autowired
	private WorkerStatusRepository workerStatusRepository;

	public  WorkerDTO convertToDto(Worker worker) {
		return modelMapper.map(worker, WorkerDTO.class);
	}

	public  Worker convertToEntity(WorkerDTO workerDto) {
		return modelMapper.map(workerDto, Worker.class);
	}

	public  PersonLegal convertToEntity(PersonLegalDTO personDto) {
		return modelMapper.map(personDto, PersonLegal.class);
	}

	public  PersonContactDetails convertToEntity(PersonContactDetailsDTO personContactDto) {
		return modelMapper.map(personContactDto, PersonContactDetails.class);
	}

	public  PersonAddress convertToEntity(PersonAddressDTO personAddressDto) {
		return modelMapper.map(personAddressDto, PersonAddress.class);
	}

	public  WorkerStatus convertToEntity(WorkerStatusDTO workerStatusDto) {
		return modelMapper.map(workerStatusDto, WorkerStatus.class);
	}

	public  WorkAssignment convertToEntity(WorkAssignmentDTO workAssigmentDto) {
		return modelMapper.map(workAssigmentDto, WorkAssignment.class);
	}

	public PersonBankDetails convertToEntity(PersonBankDetailsDTO newBankDetails) {
		return modelMapper.map(newBankDetails, PersonBankDetails.class);
	}

	public  PersonEmergencyContact convertToEntity(PersonEmergencyContactDTO personEmergencyContactDTO) {
		return modelMapper.map(personEmergencyContactDTO, PersonEmergencyContact.class);
	}

	public PersonDependents convertToEntity(PersonDependentsDTO personDependentsDTO) {
		return modelMapper.map(personDependentsDTO, PersonDependents.class);
	}

	public WorkerDTO  prepareResponseData(Worker worker, boolean history) {
		WorkerDTO workerDTO = new WorkerDTO();
		// set workerStatusData

		List<WorkerStatusDTO> workerStatuses = new ArrayList<>();
		if(!history){
			List<WorkerStatus> workerStatusList = workerStatusRepository.getWorkerStatusByDate(LocalDate.now(),worker.getWorkerID());
			workerStatuses.add(modelMapper.map(workerStatusList.get(0), WorkerStatusDTO.class));
		}
		else{
			List<WorkerStatus> workerStatusEntityList = worker.getWorkerStatus();
			workerStatusEntityList.sort(WorkerStatus.createWorkerStatusLambdaComparator());
			for (WorkerStatus workerStatus : workerStatusEntityList) {
				workerStatuses.add(modelMapper.map(workerStatus, WorkerStatusDTO.class));
			}
		}
		workerDTO.setWorkerStatus(workerStatuses);
		// set organization
		if (worker.getOrganization() != null) {
			workerDTO.setOrganization(modelMapper.map(worker.getOrganization(), RelatedOrgDTO.class));
		}
		// set workerCode
		workerDTO.setWorkerCode(worker.getWorkerCode());
		workerDTO.setWorkerID(worker.getWorkerID());
		workerDTO.setJoiningDate(worker.getJoiningDate());
		RelatedWorkerTypeDTO relatedWorkerTypeDTO = new RelatedWorkerTypeDTO();
		relatedWorkerTypeDTO.setWorkerTypeCode(worker.getWorkerType().getWorkerTypeCode());
		relatedWorkerTypeDTO.setName(worker.getWorkerType().getWorkerTypeName());
		workerDTO.setWorkerType(relatedWorkerTypeDTO);
		// set personLegalData
		workerDTO.setPersonLegal(preparePersonLegalData(worker.getPersonLegal(), history));
		// set workAssignment
		if (worker.getWorkAssignment() != null && !worker.getWorkAssignment().isEmpty()) {
			List<WorkAssignmentDTO> workAssignmentDTOList = new ArrayList<>();
			for (WorkAssignment workAssignment : worker.getWorkAssignment()) {
				if (history) {
					workAssignmentDTOList.add(modelMapper.map(workAssignment, WorkAssignmentDTO.class));
				} else if (workAssignment.getEndDate() == null) {
					workAssignmentDTOList.add(modelMapper.map(workAssignment, WorkAssignmentDTO.class));
				}
				if(workAssignment.getEndDate() == null){
					Job job = workAssignment.getJob();
					workerDTO.setDepartment(modelMapper.map(job.getDepartment(), RelatedDepartmentDTO.class));
					workerDTO.setBillable(job.isBillable());
				}
			}
			workerDTO.setWorkAssignment(workAssignmentDTOList);
		}

		workerDTO.setWorkerAttributes(prepareWorkerAttributes(worker.getWorkerAttributes()));
		//set workerDocuments
		if(worker.getWorkerDocuments() != null && !worker.getWorkerDocuments().isEmpty()){
			List<WorkerDocumentDTO> workerDocumentDTOList = new ArrayList<>();
			worker.getWorkerDocuments().forEach(workerDocument -> {
				workerDocumentDTOList.add(modelMapper.map(workerDocument,WorkerDocumentDTO.class));
			});
			workerDTO.setWorkerDocuments(workerDocumentDTOList);
		}
		return workerDTO;
	}

	private List<WorkerAttributesDTO> prepareWorkerAttributes(List<WorkerAttributes> workerAttributes) {
		List<WorkerAttributesDTO> workerAttributesDTOList = new ArrayList<>();
		if(workerAttributes != null && !workerAttributes.isEmpty()){
			for(WorkerAttributes workerAttribute:workerAttributes){
				if(workerAttribute.getEndDate() == null){
					workerAttributesDTOList.add(modelMapper.map(workerAttribute,WorkerAttributesDTO.class));
				}
			}
		}
		return workerAttributesDTOList;
	}

	public PersonLegalDTO preparePersonLegalData(PersonLegal personLegal, boolean history) {
		PersonLegalDTO personLegalDTO = new PersonLegalDTO();
		personLegalDTO.setPersonLegalID(personLegal.getPersonLegalID());
		personLegalDTO.setBirthDate(personLegal.getBirthDate());
		personLegalDTO.setGivenName(personLegal.getGivenName());
		personLegalDTO.setMiddleName(personLegal.getMiddleName());
		personLegalDTO.setFamilyName(personLegal.getFamilyName());
		personLegalDTO.setGender(personLegal.getGender());
		personLegalDTO.setMaritalStatus(personLegal.getMartialStatus());
		personLegalDTO.setPreferredName(personLegal.getPreferredName());
		// set personContactDetails
		if (personLegal.getPersonContactDetails() != null && !personLegal.getPersonContactDetails().isEmpty()) {
			List<PersonContactDetailsDTO> primaryContactDetailsDTOList = new ArrayList<>();
			List<PersonContactDetailsDTO> secondaryContactDetailsDTOList = new ArrayList<>();
			for (PersonContactDetails personContactDetails : personLegal.getPersonContactDetails()) {
				if (history) {
					if(personContactDetails.getContactType().equals(ContactType.PRIMARY)){
						primaryContactDetailsDTOList
								.add(modelMapper.map(personContactDetails, PersonContactDetailsDTO.class));
					}
					else{
						secondaryContactDetailsDTOList
								.add(modelMapper.map(personContactDetails, PersonContactDetailsDTO.class));
					}

				} else if (personContactDetails.getEndDate() == null) {
					if(personContactDetails.getContactType().equals(ContactType.PRIMARY)){
						primaryContactDetailsDTOList
								.add(modelMapper.map(personContactDetails, PersonContactDetailsDTO.class));
					}
					else{
						secondaryContactDetailsDTOList
								.add(modelMapper.map(personContactDetails, PersonContactDetailsDTO.class));
					}
				}
			}
			personLegalDTO.setPrimaryContactDetails(primaryContactDetailsDTOList);
			personLegalDTO.setSecondaryContactDetails(secondaryContactDetailsDTOList);
		}
		// set addresses
		if (personLegal.getPersonAddress() != null && !personLegal.getPersonAddress().isEmpty()) {
			List<PersonAddressDTO> personAddressDTOList = new ArrayList<>();
			for (PersonAddress personAddress : personLegal.getPersonAddress()) {
				AddressFormatDTO addressFormatDTO = null;
				if(personAddress.getCountry() != null){
					try {
						addressFormatDTO = addressFormatService.getAddressFormat(personAddress.getCountry().getCountryCode());
					}
					catch (Exception e){
					}
				}
				if (history) {
					PersonAddressDTO personAddressDTO = modelMapper.map(personAddress, PersonAddressDTO.class);
					personAddressDTO.setAddressFormat(addressFormatDTO);
					personAddressDTOList.add(personAddressDTO);
				} else if (personAddress.getEndDate() == null) {
					PersonAddressDTO personAddressDTO = modelMapper.map(personAddress, PersonAddressDTO.class);
					personAddressDTO.setAddressFormat(addressFormatDTO);
					personAddressDTOList.add(personAddressDTO);
				}
			}
			personLegalDTO.setPersonAddress(personAddressDTOList);
		}
		// setPersonEmergencyContacts
		if (personLegal.getPersonEmergencyContact() != null && !personLegal.getPersonEmergencyContact().isEmpty()) {
			List<PersonEmergencyContactDTO> personEmergencyContactDTOList = new ArrayList<>();
			for (PersonEmergencyContact personEmergencyContact : personLegal.getPersonEmergencyContact()) {
				if (history) {
					personEmergencyContactDTOList
							.add(modelMapper.map(personEmergencyContact, PersonEmergencyContactDTO.class));
				} else if (personEmergencyContact.getEndDate() == null) {
					personEmergencyContactDTOList
							.add(modelMapper.map(personEmergencyContact, PersonEmergencyContactDTO.class));
				}
			}
			personLegalDTO.setPersonEmergencyContact(personEmergencyContactDTOList);
		}
		// set personDependants
		// setPersonEmergencyContacts
		if (personLegal.getPersonDependents() != null && !personLegal.getPersonDependents().isEmpty()) {
			List<PersonDependentsDTO> personDependentsDTOList = new ArrayList<>();
			for (PersonDependents personDependents : personLegal.getPersonDependents()) {
				if (history) {
					personDependentsDTOList.add(modelMapper.map(personDependents, PersonDependentsDTO.class));
				} else if (personDependents.getValidTo() == null) {
					personDependentsDTOList.add(modelMapper.map(personDependents, PersonDependentsDTO.class));
				}
			}
			personLegalDTO.setPersonDependents(personDependentsDTOList);
		}
		// set person bank details
		if (personLegal.getPersonBankDetails() != null && !personLegal.getPersonBankDetails().isEmpty()) {
			List<PersonBankDetailsDTO> personBankDetailsDTOList = new ArrayList<>();
			for (PersonBankDetails personBankDetails : personLegal.getPersonBankDetails()) {
				if (history) {
					personBankDetailsDTOList.add(modelMapper.map(personBankDetails, PersonBankDetailsDTO.class));
				} else if (personBankDetails.getEndDate() == null) {
					personBankDetailsDTOList.add(modelMapper.map(personBankDetails, PersonBankDetailsDTO.class));
				}
			}
			personLegalDTO.setPersonBankDetails(personBankDetailsDTOList);
		}
		return personLegalDTO;
	}

	public void resetValues(WorkerDocument workerDocument) {
		workerDocument.setExpiryDate(null);
		workerDocument.setIssueDate(null);
		workerDocument.setFileExt(null);
		workerDocument.setFileName(null);
		workerDocument.setFileType(null);
		workerDocument.setStatus(EnumDocStatus.Pending);
		workerDocument.setUrl(null);
	}
}
