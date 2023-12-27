/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.org.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

import com.lucid.core.azure.EnumAzureContainers;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.dto.DocAttributeDefDTO;
import com.lucid.recruit.docs.dto.DocumentDefDTO;
import com.lucid.recruit.docs.dto.RelatedDocAttributeDefDTO;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;
import com.lucid.recruit.docs.entity.DocumentDef;
import com.lucid.recruit.docs.repo.DefaultDocAssignmentsRepo;
import com.lucid.recruit.org.dto.*;
import com.lucid.recruit.org.entity.*;
import com.lucid.recruit.org.repo.*;
import com.lucid.recruit.referencedata.entity.OrgInActiveStatusCodes;
import com.lucid.recruit.referencedata.repo.TaxClassificationDefRepo;
import com.lucid.recruit.worker.dto.WorkerAttributesDTO;
import com.lucid.recruit.worker.entity.Worker;
import com.lucid.recruit.worker.entity.WorkerAttributes;
import com.lucid.recruit.worker.entity.WorkerDocument;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lucid.core.azure.AzureBlobService;
import com.lucid.core.base.BaseServiceImpl;
import com.lucid.recruit.docs.constants.EnumDocSource;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.repo.DocAttributeDefRepo;
import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.org.validation.OrgValidation;
import com.lucid.recruit.referencedata.dto.RefCountryDTO;
import com.lucid.recruit.referencedata.dto.TaxClassificationDefDTO;
import com.lucid.recruit.referencedata.entity.Country;
import com.lucid.recruit.referencedata.entity.TaxClassificationDef;
import com.lucid.util.ServiceUtils;

@Service(OrganizationServiceImpl.SERVICE_NAME)
public class OrganizationServiceImpl extends BaseServiceImpl implements OrganizationService {

	public static final String SERVICE_NAME = "organizationService";
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);

	private static Set<String> validImageExt;

	@Autowired
	private OrganizationRepo organizationRepo;

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private OrgValidation orgValidation;

	@Autowired
	private OrganizationStatusRepo orgStatusRepo;

	@Autowired
	private OrganizationSpecification organizationSpecification;

	@Autowired
	private OrganizationDocumentRepo organizationDocumentRepo;

	@Autowired
	private OrgDocAttributesRepo orgDocAttributesRepo;

	@Autowired
	private DocAttributeDefRepo docAttrDefRepo;

	@Autowired
	private AzureBlobService azureBlobService;

	@Autowired
	private Validator validator;
	@Autowired
	private OrganizationAttributesRepo organizationAttributesRepo;

	@Autowired
	private DefaultDocAssignmentsRepo defaultDocAssignmentsRepo;

	@Autowired
	private TaxClassificationDefRepo taxClassificationDefRepo;

	@Override
	@Transactional
	public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
		LOGGER.debug("Control enters into create organization {}", organizationDTO);

		LOGGER.debug("Preparing organization data");
		Organization organization = createOrganizationData(organizationDTO);
		LOGGER.debug("Preparing organization data done");

		LOGGER.debug("Request data send for  validation");
		orgValidation.validateOrganization(organizationDTO, organization);
		LOGGER.debug("Validation done sucessfully");

		// validate and domains and create domain entities
		LOGGER.debug("Started validating domains");
		orgValidation.validateDomains(organization, organizationDTO.getOrgDomains());
		LOGGER.debug(" Domain validation done and saved sucessfully");

		// validate and create communicationDetails
		LOGGER.debug("Started validating communications");
		orgValidation.validateCommunication(organization, organizationDTO.getOrgCommunications());
		LOGGER.debug("Communication validation done and saved sucessfully");
		// create orgAddress
		LOGGER.debug("Started validating address");
		orgValidation.validateAddress(organization, organizationDTO.getOrgAddresses());
		// setStatus to organization
		setDefaultStatus(organization);

		LOGGER.debug("Saving Organization");
		organization = organizationRepo.save(organization);
		LOGGER.debug(" Organization saved sucessfully");

		//create organizationAttributes
		LOGGER.debug("validating Organization attributes");
		orgValidation.validateOrgAttributes(organization,organizationDTO.getOrganizationAttributes());
		//saving org attributes
		LOGGER.debug("saving organization attributes");
		organizationAttributesRepo.saveAll(organization.getOrganizationAttributes());

		//Create org Documents assigned by default
		List<OrganizationDocument> orgDocs = new ArrayList<>();
		List<DefaultDocAssignments> defaultAssignedDocs = defaultDocAssignmentsRepo.findAutoAssignedDoc(
				EnumDocRelatedEntity.ORGANIZATION,null);
		for(DefaultDocAssignments defaultDocAssignments:defaultAssignedDocs){
			OrganizationDocument orgDoc = new OrganizationDocument();
			orgDoc.setOrganization(organization);
			orgDoc.setDocumentDef(defaultDocAssignments.getDocumentDef());
			orgDoc.setDocSource(EnumDocSource.AUTOASSIGNED);
			orgDoc.setStatus(EnumDocStatus.Pending);
			orgDocs.add(orgDoc);
		}
		List<OrganizationDocument> savedOrgDocs = new ArrayList<>();
		if (!orgDocs.isEmpty()) {
			savedOrgDocs = organizationDocumentRepo.saveAll(orgDocs);
		}
		organization.setOrganizationDocuments(savedOrgDocs);
		LOGGER.debug("Address validation done and saved sucessfully");
		LOGGER.debug("preparing responce data");
		return prepareResponceData(organization,false);
	}

	private void setDefaultStatus(Organization organization) {
		OrganizationStatus organizationStatus = new OrganizationStatus();
		organizationStatus.setOrganization(organization);
		organizationStatus.setEffectiveDate(LocalDate.now());
		if (organization.getOrgCommunications() == null || organization.getOrgCommunications().isEmpty()) {
			organizationStatus.setStatusCode(OrgStatus.DRAFT);
		} else {
			organizationStatus.setStatusCode(OrgStatus.PENDING);
		}
		List<OrganizationStatus> statusList = new ArrayList<>();
		statusList.add(organizationStatus);
		organization.setStatus(statusList);
	}

	private void setPendingSatus(Organization organization, OrganizationDTO organizationDTO) {
		List<OrganizationStatus> statusEntityList = organization.getStatus();
		statusEntityList.sort(OrganizationStatus.createOrgStatusLambdaComparator());
		OrganizationStatus latestStatus = statusEntityList.get(statusEntityList.size() - 1);
		if (latestStatus.getStatusCode().equals(OrgStatus.DRAFT)
				&& (organization.getOrgCommunications() != null && !organization.getOrgCommunications().isEmpty())) {
			if (latestStatus.getEffectiveDate().compareTo(LocalDate.now()) == 0) {
				latestStatus.setStatusCode(OrgStatus.PENDING);

			} else {
				OrganizationStatus organizationStatus = new OrganizationStatus();
				organizationStatus.setOrganization(organization);
				organizationStatus.setEffectiveDate(LocalDate.now());
				organizationStatus.setStatusCode(OrgStatus.PENDING);
				statusEntityList.add(organizationStatus);
			}
		}
	}

	private Organization createOrganizationData(OrganizationDTO organizationDTO) {
		LOGGER.info("Creating organization enity object ");
		Organization organization = new Organization();
		organization.setName(organizationDTO.getName());
		organization.setFax(organizationDTO.getFax());
		organization.setStateOfInc(organizationDTO.getStateOfInc());
		organization.setPhoneNumber(organizationDTO.getPhoneNumber());
		organization.setTaxId(organizationDTO.getTaxId());
		organization.setTradeName(organizationDTO.getTradeName());
		organization.setCreatedDt(LocalDate.now());
		if (organizationDTO.getCountry() != null) {
			organization.setCountry(modelMapper.map(organizationDTO.getCountry(), Country.class));
		}
		if (organizationDTO.getTaxClassification() != null) {
			organization.setTaxClassification(
					modelMapper.map(organizationDTO.getTaxClassification(), TaxClassificationDef.class));
		}
		return organization;
	}

	@Override
	@Transactional
	public OrganizationDTO updateOrganization(String organizationID, OrganizationDTO organizationDTO) {
		LOGGER.debug("Finding organization by id:" + organizationID);
		Organization organization = orgValidation.fetchOrganization(organizationID);
		LOGGER.debug("Organization Found with id:" + organizationID);
		// validate organization data
		organizationDTO.setOrganizationID(organizationID);

		LOGGER.debug("Updating organization data");
		updateOrganizationData(organization, organizationDTO);
		LOGGER.debug("Updating organization data done");

		LOGGER.debug("Organization sent for validation");
		orgValidation.validateOrganization(organizationDTO, organization);
		LOGGER.debug("Organization sent for validation done");

		// validate and createDomains
		LOGGER.debug("Started validating updatable organization domains");
		orgValidation.validateDomains(organization, organizationDTO.getOrgDomains());
		LOGGER.debug("Started validating updatable organization domains done and saved sucessfully");

		// validate and create communicationDetails
		LOGGER.debug("Started validating updatable organization Communications");
		if (organizationDTO.getOrgCommunications() != null && !organizationDTO.getOrgCommunications().isEmpty()) {
			Set<ConstraintViolation<OrgCommunicationDTO>> violations = validator.validate(organizationDTO.getOrgCommunications().get(0));
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}
		}
		orgValidation.validateCommunication(organization, organizationDTO.getOrgCommunications());
		setPendingSatus(organization, organizationDTO);
		LOGGER.debug("Started validating updatable organization Communications done and saved sucessfully");

		// create orgAddress
		LOGGER.debug("Started validating updatable organization address");
		orgValidation.validateAddress(organization, organizationDTO.getOrgAddresses());
		LOGGER.debug("Started validating updatable organization address done and saved sucessfully");

		LOGGER.debug("Saving Updated Organization");
		organizationRepo.save(organization);
		LOGGER.debug("Updated Organization saved sucessfully");

		if(organizationDTO.getOrganizationAttributes() != null && !organizationDTO.getOrganizationAttributes().isEmpty()){
			LOGGER.debug("validating Organization attributes");
			orgValidation.validateOrgAttributes(organization,organizationDTO.getOrganizationAttributes());
			//saving org attributes
			LOGGER.debug("saving organization attributes");
			organizationAttributesRepo.saveAll(organization.getOrganizationAttributes());
		}
		return prepareResponceData(organization,false);
	}

	@Override
	public OrganizationDTO getOrganization(String organizationID,boolean history) {
		Organization organization = orgValidation.fetchOrganization(organizationID);
		return prepareResponceData(organization,history);
	}

	@Override
	public Page<OrganizationDTO> getAllOrganizations(Integer offset, Integer limit, String organizationID, String emailId,
													 String name,OrgStatus orgStatus,String sort) {
		LOGGER.debug("Find all specification creation start");
		Specification<Organization> specification = organizationSpecification
				.conditionalSearchForOrganization(organizationID, emailId, name,orgStatus);
		LOGGER.debug("Find all specification creation done");

		// Define default sorting by organization name in ascending order (A to Z)
		List<Sort.Order> defaultSort = new ArrayList<>();
		if(sort == null || sort.isEmpty()) {
			defaultSort.add(Sort.Order.asc("name"));
			defaultSort.add(Sort.Order.desc("createdDt"));
		} else{
			StringTokenizer tokenizer = new StringTokenizer(sort,",");
			while (tokenizer.hasMoreTokens()){
				String param = tokenizer.nextToken();
				if(param.contains("~")){
					String value = param.substring(1);
					defaultSort.add(Sort.Order.desc(value));
				} else{
					defaultSort.add(Sort.Order.asc(param));
				}
			}
		}

		LOGGER.debug("Creating pageable object");
		Pageable pageable = ServiceUtils.getPageableObject(offset, limit,Sort.by(defaultSort));
		LOGGER.debug("Creating pageable object done");

		Page<Organization> pageOrganizations;
		LOGGER.info("Finding organizations");
		if (specification != null) {
			LOGGER.debug("Finding organizations with specifications and pageable");
			pageOrganizations = organizationRepo.findAll(specification, pageable);
			LOGGER.debug("Finding organizations with specifications and pageable done");
		} else {
			LOGGER.debug("Finding organizations with pageable");
			pageOrganizations = organizationRepo.findAll(pageable);
			LOGGER.debug("Finding organizations with pageable done");
		}
		List<OrganizationDTO> orgSummaryDTOS = new ArrayList<>();
		if (pageOrganizations != null) {
			if (!pageOrganizations.getContent().isEmpty()) {
				LOGGER.info("preparing summary");
				LOGGER.debug("Preparing summary");
				for (Organization organization : pageOrganizations.getContent()) {
					orgSummaryDTOS.add(prepareResponceData(organization,false));
				}
				LOGGER.debug("Preparing summary done");
			}
			LOGGER.info("returns organizations summary");
			return new PageImpl<>(orgSummaryDTOS, pageable, pageOrganizations.getTotalElements());
		}
		// if pageWorkers is null then we send empty list
		else {
			LOGGER.info("returns organizations empty summary");
			return new PageImpl<>(orgSummaryDTOS, pageable, 0);
		}
	}

	@Override
	public OrganizationDocumentDTO uploadOrgDocument(String organizationID,
													 OrganizationDocumentDTO organizationDocumentDTO) {
		Organization organization = orgValidation.fetchOrganization(organizationID);
		OrganizationDocument orgDoc = orgValidation.validateOrganizationDocument(organizationDocumentDTO);
		if (orgDoc == null) {
			orgDoc = new OrganizationDocument();
			orgDoc.setDocumentDef(modelMapper.map(organizationDocumentDTO.getDocumentDef(), DocumentDef.class));
			orgDoc.setOrganization(organization);
			orgDoc.setDocSource(EnumDocSource.UPLOADED);
		}
		orgDoc.setStatus(EnumDocStatus.PendingDocument);
		organizationDocumentRepo.save(orgDoc);
		if (organizationDocumentDTO.getOrgDocAttributes() != null) {
			List<OrgDocAttributes> orgDocAttributesList = orgDoc.getOrgDocAttributes();
			if(orgDocAttributesList == null){
				orgDocAttributesList = new ArrayList<>();
			}
			for(OrgDocAttributesDTO orgDocAttributesDTO:organizationDocumentDTO.getOrgDocAttributes()){
				OrgDocAttributes orgDocAttr = modelMapper.map(orgDocAttributesDTO, OrgDocAttributes.class);
				orgDocAttr.setOrganizationDocument(orgDoc);
				orgDocAttr.setDocAttributeDef(
						docAttrDefRepo.findById(orgDocAttr.getDocAttributeDef().getDocumentAttrDefID()).get());
				orgDocAttributesList.add(orgDocAttr);
			}
			if(!orgDocAttributesList.isEmpty()) {
				orgDocAttributesRepo.saveAll(orgDocAttributesList);
			}
			orgDoc.setOrgDocAttributes(orgDocAttributesList);
		}
		return modelMapper.map(orgDoc, OrganizationDocumentDTO.class);
	}

	@Override
	public String uploadOrgDocFile(String organizationDocID, MultipartFile file) {
		OrganizationDocument orgDoc = orgValidation.getOrgDocForUpload(organizationDocID);
		String fileName = organizationDocID + file.getOriginalFilename();
		try {
			azureBlobService.upload(file, fileName, EnumAzureContainers.organization);
		} catch (IOException e) {
			LOGGER.error("Error while storing MultipartFile ");
			// throw new InvalidDataException(ErrorCodes.W_BV_0010, "WorkerDocuent", "File
			// data", null, e.getMessage());
			throw new RuntimeException(e);
		}
		orgDoc.setFileName(file.getOriginalFilename());
		orgDoc.setFileType(file.getContentType());
		orgDoc.setStatus(EnumDocStatus.Active);
		orgDoc.setUrl(fileName);
		organizationDocumentRepo.save(orgDoc);
		return file.getOriginalFilename() + "Uploaded sucessfully ";
	}

	@Override
	public String uploadOrgLogo(String organizationID, MultipartFile file) {
		Organization organization = orgValidation.fetchOrganization(organizationID);
		if(!file.getContentType().startsWith("image/")){
           throw new InvalidDataException(ErrorCodes.ORG_BV_0021,"organization","logo",file.getContentType(),"Not a image");
		}
		if(file.getSize()>(240*1024)){
			throw new InvalidDataException(ErrorCodes.ORG_BV_0022,"organization","logo",file.getContentType(),"File size exceed 240 kilo byte");
		}
		String fileName = "logos/"+organization.getName()+"_"+organizationID + file.getOriginalFilename();
		try {
			azureBlobService.upload(file, fileName, EnumAzureContainers.organization);
		} catch (IOException e) {
			LOGGER.error("Error while storing MultipartFile ");
			// throw new InvalidDataException(ErrorCodes.W_BV_0010, "WorkerDocuent", "File
			// data", null, e.getMessage());
			throw new RuntimeException(e);
		}
		organization.setLogoUrl(fileName);
		organizationRepo.save(organization);
		return file.getOriginalFilename() + "Uploaded sucessfully ";
	}

	@Override
	public String deleteDocument(String organizationDocID) {
		OrganizationDocument organizationDocument = orgValidation.getOrgDocForUpload(organizationDocID);
		// delete document from storage
		if (!StringUtils.isEmpty(organizationDocument.getUrl())) {
			azureBlobService.deleteBlob(organizationDocument.getUrl(),EnumAzureContainers.organization);
		}
		// delete all attributes
		orgDocAttributesRepo.deleteAll(organizationDocument.getOrgDocAttributes());
		// If Document is Auto assigned, then delete only document from storage, and
		// make the document name , other fields to null which are set while create
		// document and delete attributes and move the status Pending
		if (organizationDocument.getDocSource().compareTo(EnumDocSource.AUTOASSIGNED) == 0) {
			resetValues(organizationDocument);
			organizationDocumentRepo.save(organizationDocument);
		}
		// If Document is not Auto assigned delete the document form storage and delete
		// the worker doc and attributes
		else {
			organizationDocumentRepo.delete(organizationDocument);
		}
		return "Document by id:" + organizationDocID + " deleted successfully";
	}

	@Override
	public OrganizationDocumentDTO getOrgDocument(String organizationDocID) {
		return modelMapper.map(orgValidation.getOrgDocForUpload(organizationDocID), OrganizationDocumentDTO.class);
	}

	@Override
	public OrganizationDocument retrieveOrgDocument(String organizationDocID) {
		return orgValidation.getOrgDocForUpload(organizationDocID);
	}

	@Override
	public byte[] retrieveOrgDocFile(OrganizationDocument organizationDocument) {
		byte[] filedata;
		try {
			filedata = azureBlobService.getFile(organizationDocument.getUrl(),EnumAzureContainers.organization);
		} catch (URISyntaxException e) {
			LOGGER.error("Error while reading file from storage ");
			throw new RuntimeException(e);
		}
		return filedata;
	}

	@Override
	public byte[] retrieveOrgLogo(String organizationID) {
		Organization organization = orgValidation.fetchOrganization(organizationID);
		byte[] filedata;
		try {
			filedata = azureBlobService.getFile(organization.getLogoUrl(),EnumAzureContainers.organization);
		} catch (URISyntaxException e) {
			LOGGER.error("Error while reading file from storage ");
			throw new RuntimeException(e);
		}
		return filedata;
	}

	private OrgSummaryDTO convertToSummaryDTO(Organization organization) {
		LOGGER.debug("Converting Organization to OrgSummaryDTO");
		OrgSummaryDTO summaryDTO = new OrgSummaryDTO();
		if (organization != null) {
			summaryDTO.setOrganizationID(organization.getOrganizationID());
			summaryDTO.setName(organization.getName());
			if (organization.getOrgAddresses() != null && !organization.getOrgAddresses().isEmpty()) {
				LOGGER.debug("Mapping OrgAddressDTO  to  OrgSummaryDTO ");
				organization.getOrgAddresses().sort(OrgAddress.createOrgAddLambdaComparator());
				LOGGER.debug("checking the  isPrimary = true");
				summaryDTO.setOrgAddress(
						modelMapper.map(organization.getOrgAddresses().get(organization.getOrgAddresses().size() - 1),
								OrgAddressDTO.class));
			}

			// Map the first active communication, if it exists.
			if (organization.getOrgCommunications() != null && !organization.getOrgCommunications().isEmpty()) {
				LOGGER.debug("Mapping OrgCommunicationDTO to  OrgSummaryDTO ");
				organization.getOrgCommunications().sort(OrgCommunication.createCommunicationLambdaComparator());
				OrgCommunication communication = organization.getOrgCommunications()
						.get(organization.getOrgCommunications().size() - 1);
				if (communication != null) {
					LOGGER.debug(" Mapping   OrgCommunicationDTO  ");
					summaryDTO.setAuthSignataryFn(communication.getAuthSignataryFn());
					summaryDTO.setAuthSignataryLn(communication.getAuthSignataryLn());
					summaryDTO.setAuthSignataryPhone(communication.getAuthSignataryPhone());
					summaryDTO.setAuthSignataryEmail(communication.getAuthSignataryEmail());
				}
			}

			// Map latest status
			if ((organization.getStatus() != null && !organization.getStatus().isEmpty())) {
				organization.getStatus().sort(OrganizationStatus.createOrgStatusLambdaComparator());
				OrganizationStatus organizationStatus = organization.getStatus()
						.get(organization.getStatus().size() - 1);
				summaryDTO.setStatus(modelMapper.map(organizationStatus, OrganizationStatusDTO.class));
			}

		}
		LOGGER.debug("before  summaryDTO  ");
		return summaryDTO;
	}

	private OrganizationDTO prepareResponceData(Organization organization,boolean requiredHistory) {
		OrganizationDTO organizationDTO = new OrganizationDTO();
		organizationDTO.setName(organization.getName());
		organizationDTO.setDescription(organization.getDescription());
		organizationDTO.setCode(organization.getCode());
		organizationDTO.setFax(organization.getFax());
		organizationDTO.setTaxId(organization.getTaxId());
		organizationDTO.setStateOfInc(organization.getStateOfInc());
		organizationDTO.setOrganizationID(organization.getOrganizationID());
		organizationDTO.setPhoneNumber(organization.getPhoneNumber());
		organizationDTO.setTradeName(organization.getTradeName());
		organizationDTO.setCreatedDt(organization.getCreatedDt());
		if (organization.getCountry() != null) {
			organizationDTO.setCountry(modelMapper.map(organization.getCountry(), RefCountryDTO.class));
		}
		if (organization.getTaxClassification() != null) {
			organizationDTO.setTaxClassification(
					modelMapper.map(organization.getTaxClassification(), TaxClassificationDefDTO.class));
		}
		// set org communication
		if (!organization.getOrgCommunications().isEmpty()) {
			List<OrgCommunicationDTO> orgCommunicationList = new ArrayList<>();
			organization.getOrgCommunications().sort(OrgCommunication.createCommunicationLambdaComparator());
			if(requiredHistory){
				for(OrgCommunication orgCommunication:organization.getOrgCommunications()){
					OrgCommunicationDTO orgCommunicationDTO = modelMapper.map(
							orgCommunication,
							OrgCommunicationDTO.class);
					orgCommunicationList.add(orgCommunicationDTO);
				}
			} else{
				OrgCommunicationDTO orgCommunicationDTO = modelMapper.map(
						organization.getOrgCommunications().get(organization.getOrgCommunications().size() - 1),
						OrgCommunicationDTO.class);
				orgCommunicationList.add(orgCommunicationDTO);
			}
			organizationDTO.setOrgCommunications(orgCommunicationList);
		}
		// set org domains
		if (!organization.getOrgDomains().isEmpty()) {
			List<OrgDomainDTO> orgDomainDTOS = new ArrayList<>();
			if(requiredHistory){
				for (OrgDomain orgDomain : organization.getOrgDomains()) {
					orgDomainDTOS.add(modelMapper.map(orgDomain, OrgDomainDTO.class));
				}
			} else {
				for (OrgDomain orgDomain : organization.getOrgDomains()) {
					if (!orgDomain.getDeleted()) {
						orgDomainDTOS.add(modelMapper.map(orgDomain, OrgDomainDTO.class));
					}
				}
			}
			organizationDTO.setOrgDomains(orgDomainDTOS);
		}
		// set orgAddress
		if (!organization.getOrgAddresses().isEmpty()) {
			organization.getOrgAddresses().sort(OrgAddress.createOrgAddLambdaComparator());
			List<OrgAddressDTO> orgAddressDTOS = new ArrayList<>();
			for (OrgAddress orgAddress : organization.getOrgAddresses()) {
				if (requiredHistory) {
					orgAddressDTOS.add(modelMapper.map(orgAddress, OrgAddressDTO.class));
				} else if(orgAddress.getEndDate() == null) {
					orgAddressDTOS.add(modelMapper.map(orgAddress, OrgAddressDTO.class));
				}
			}
			organizationDTO.setOrgAddresses(orgAddressDTOS);
		}
		// set orgStatus
		if (!organization.getStatus().isEmpty()) {
			organization.getStatus().sort(OrganizationStatus.createOrgStatusLambdaComparator());
			List<OrganizationStatusDTO> orgStatusDTO = new ArrayList<>();
			if(requiredHistory){
				for(OrganizationStatus organizationStatus:organization.getStatus()){
					orgStatusDTO.add(modelMapper.map(organizationStatus,
							OrganizationStatusDTO.class));
				}
			} else {
				orgStatusDTO.add(modelMapper.map(organization.getStatus().get(organization.getStatus().size() - 1),
						OrganizationStatusDTO.class));
			}
			organizationDTO.setStatus(orgStatusDTO);
		}
		organizationDTO.setOrganizationAttributes(prepareOrganizationAttributes(organization.getOrganizationAttributes()));
		//setOrganization docs
		if(organization.getOrganizationDocuments() != null && !organization.getOrganizationDocuments().isEmpty()){
			List<OrganizationDocumentDTO> organizationDocumentDTOList = new ArrayList<>();
			for(OrganizationDocument organizationDocument:organization.getOrganizationDocuments()){
				OrganizationDocumentDTO organizationDocumentDTO = new OrganizationDocumentDTO();
				BeanUtils.copyProperties(organizationDocument,organizationDocumentDTO);
				DocumentDefDTO documentDefDTO = new DocumentDefDTO();
				BeanUtils.copyProperties(organizationDocument.getDocumentDef(),documentDefDTO);
				organizationDocumentDTO.setDocumentDef(documentDefDTO);
				if(organizationDocument.getOrgDocAttributes() != null && !organizationDocument.getOrgDocAttributes().isEmpty()){
					List<OrgDocAttributesDTO> orgDocAttributesDTOList = new ArrayList<>();
					for(OrgDocAttributes orgDocAttributes:organizationDocument.getOrgDocAttributes()){
						OrgDocAttributesDTO orgDocAttributesDTO = new OrgDocAttributesDTO();
						BeanUtils.copyProperties(orgDocAttributes,orgDocAttributesDTO);
						RelatedDocAttributeDefDTO docAttributeDefDTO = new RelatedDocAttributeDefDTO();
						BeanUtils.copyProperties(orgDocAttributes.getDocAttributeDef(),docAttributeDefDTO);
						orgDocAttributesDTO.setDocAttributeDef(docAttributeDefDTO);
						orgDocAttributesDTOList.add(orgDocAttributesDTO);
					}
					organizationDocumentDTO.setOrgDocAttributes(orgDocAttributesDTOList);
				}
				organizationDocumentDTOList.add(organizationDocumentDTO);
			}
			organizationDTO.setOrganizationDocuments(organizationDocumentDTOList);
		}
		return organizationDTO;
	}

	private List<OrganizationAttributesDTO> prepareOrganizationAttributes(List<OrganizationAttributes> orgAttributes) {
		List<OrganizationAttributesDTO> orgAttributesDTOList = new ArrayList<>();
		if(orgAttributes != null && !orgAttributes.isEmpty()){
			for(OrganizationAttributes orgAttribute:orgAttributes){
				if(orgAttribute.getEndDate() == null){
					orgAttributesDTOList.add(modelMapper.map(orgAttribute,OrganizationAttributesDTO.class));
				}
			}
		}
		return orgAttributesDTOList;
	}
	private void updateOrganizationData(Organization organization, OrganizationDTO organizationDTO) {
		if (!StringUtils.isEmpty(organizationDTO.getName())
				&& !organization.getName().equals(organizationDTO.getName())) {
			organization.setName(organizationDTO.getName());
		} else {
			organizationDTO.setName(organization.getName());
		}
		if (!StringUtils.isEmpty(organizationDTO.getDescription())
				&& (StringUtils.isEmpty(organization.getDescription())
				|| !organization.getDescription().equals(organizationDTO.getDescription()))) {
			organization.setDescription(organizationDTO.getDescription());
		} else {
			organizationDTO.setDescription(organization.getDescription());
		}
		if (!StringUtils.isEmpty(organizationDTO.getTradeName()) && (StringUtils.isEmpty(organization.getTradeName())
				|| !organization.getTradeName().equals(organizationDTO.getTradeName()))) {
			organization.setTradeName(organizationDTO.getTradeName());
		} else {
			organizationDTO.setTradeName(organization.getTradeName());
		}
		if (!StringUtils.isEmpty(organizationDTO.getPhoneNumber())
				&& (StringUtils.isEmpty(organization.getPhoneNumber())
				|| !organization.getPhoneNumber().equals(organizationDTO.getPhoneNumber()))) {
			organization.setPhoneNumber(organizationDTO.getPhoneNumber());
		} else {
			organizationDTO.setPhoneNumber(organization.getPhoneNumber());
		}
		if (!StringUtils.isEmpty(organizationDTO.getFax()) && (StringUtils.isEmpty(organization.getFax())
				|| !organization.getFax().equals(organizationDTO.getFax()))) {
			organization.setFax(organizationDTO.getFax());
		} else {
			organizationDTO.setFax(organization.getFax());
		}
		if (organizationDTO.getCountry() == null) {
			RefCountryDTO refCountryDTO = new RefCountryDTO();
			refCountryDTO.setCountryCode(organization.getCountry().getCountryCode());
			organizationDTO.setCountry(refCountryDTO);
		}
		if(StringUtils.isAllBlank(organizationDTO.getTaxId())){
			organizationDTO.setTaxId(organization.getTaxId());
		}
		if (organizationDTO.getTaxClassification() != null) {
			organization.setTaxClassification(
					modelMapper.map(organizationDTO.getTaxClassification(), TaxClassificationDef.class));
		} else if (organization.getTaxClassification() != null) {
			TaxClassificationDefDTO taxClassDefDto = new TaxClassificationDefDTO();
			taxClassDefDto.setTaxClassCode(organization.getTaxClassification().getTaxClassCode());
			organizationDTO.setTaxClassification(taxClassDefDto);
		}
		if (!StringUtils.isEmpty(organizationDTO.getStateOfInc()) && (StringUtils.isEmpty(organization.getStateOfInc())
				|| !organization.getStateOfInc().equals(organizationDTO.getStateOfInc()))) {
			organization.setStateOfInc(organizationDTO.getStateOfInc());
		} else {
			organizationDTO.setStateOfInc(organization.getStateOfInc());
		}

		// Populate ORG domains with existing values if input is not passed
		if (organizationDTO.getOrgDomains() == null || organizationDTO.getOrgDomains().isEmpty()) {
			List<OrgDomainDTO> domainDTOList = new ArrayList<>();
			organization.getOrgDomains().stream().forEach(orgDomain -> {
				domainDTOList.add(modelMapper.map(orgDomain, OrgDomainDTO.class));
			});
			organizationDTO.setOrgDomains(domainDTOList);
		}

	}

	@Override
	public OrganizationDTO updateOrgStatus(String organizationID, OrganizationStatusDTO orgStatusDTO) {
		LOGGER.debug("Finding organization by id:" + organizationID);
		Organization organization = orgValidation.fetchOrganization(organizationID);
		LOGGER.debug("Organization Found with id:" + organizationID);
		// validate and get the organization status definition
		OrgStatus orgStatus = orgStatusDTO.getStatusCode();
		orgValidation.validateStatusTransition(organization, orgStatusDTO);

		List<OrganizationStatus> statusEntityList = organization.getStatus();
		statusEntityList.sort(OrganizationStatus.createOrgStatusLambdaComparator());
		OrganizationStatus latestStatus = statusEntityList.get(statusEntityList.size() - 1);

		OrgInActiveStatusCodes orgInActiveStatusCode = null;
		if(orgStatusDTO.getStatusCode().equals(OrgStatus.INACTIVE)){
			orgInActiveStatusCode = orgValidation.getOrgInactiveStatusCode(orgStatusDTO);
		}

		if (latestStatus.getEffectiveDate().compareTo(LocalDate.now()) == 0) {
			latestStatus.setStatusCode(orgStatus);
			latestStatus.setInactiveStatusCode(orgInActiveStatusCode);

		} else {
			OrganizationStatus organizationStatus = new OrganizationStatus();
			organizationStatus.setOrganization(organization);
			organizationStatus.setEffectiveDate(LocalDate.now());
			organizationStatus.setStatusCode(orgStatus);
			latestStatus.setInactiveStatusCode(orgInActiveStatusCode);
			statusEntityList.add(organizationStatus);
		}

		orgStatusRepo.saveAll(statusEntityList);
		organization = orgValidation.fetchOrganization(organizationID);

		return prepareResponceData(organization,false);
	}

	@Override
	public OrgCountsDTO getOrgCounts() {
		OrgCountsDTO orgCountsDTO = new OrgCountsDTO();
		orgCountsDTO.setTotalOrgs(organizationRepo.count());
		orgCountsDTO.setOrganizationStatus(orgStatusRepo.countByOrganizationStatus());
		return orgCountsDTO;
	}

	private void resetValues(OrganizationDocument organizationDocument) {
		organizationDocument.setExpiryDate(null);
		organizationDocument.setIssueDate(null);
		organizationDocument.setFileExt(null);
		organizationDocument.setFileName(null);
		organizationDocument.setFileType(null);
		organizationDocument.setStatus(EnumDocStatus.Pending);
		organizationDocument.setUrl(null);
	}

}
