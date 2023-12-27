package com.lucid.recruit.org.validation;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.net.InternetDomainName;
import com.lucid.core.validators.AddressValidator;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.entity.OrgAttributeDef;
import com.lucid.recruit.attr.repo.OrgAttrDefListValuesRepo;
import com.lucid.recruit.attr.repo.OrgAttributeDefRepo;
import com.lucid.recruit.attr.validations.BaseAttributeValidator;
import com.lucid.recruit.org.dto.*;
import com.lucid.recruit.org.entity.*;
import com.lucid.recruit.referencedata.entity.OrgInActiveStatusCodes;
import com.lucid.recruit.referencedata.repo.OrgInActiveStatusCodesRepo;
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
import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.org.repo.OrgDomainRepo;
import com.lucid.recruit.org.repo.OrganizationDocumentRepo;
import com.lucid.recruit.org.repo.OrganizationRepo;
import com.lucid.recruit.referencedata.entity.Country;
import com.lucid.recruit.referencedata.entity.TaxClassificationDef;
import com.lucid.recruit.referencedata.repo.CountryRepo;
import com.lucid.recruit.referencedata.repo.TaxClassificationDefRepo;
import com.lucid.recruit.worker.validations.WorkerValidator;

@Component
public class OrgValidation {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerValidator.class);

	@Autowired
	private OrganizationRepo organizationRepo;

	@Autowired
	private OrgDomainRepo orgDomainRepo;

	@Autowired
	private CountryRepo countryRepo;

	@Autowired
	private TaxClassificationDefRepo taxClassificationDefRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DocumentDefValidatior documentDefValidatior;

	@Autowired
	private OrganizationDocumentRepo organizationDocumentRepo;

	@Autowired
	private OrgInActiveStatusCodesRepo orgInActiveStatusCodeRepo;

	@Autowired
	private AddressValidator addressValidator;

	@Autowired
	private Validator validator;
	@Autowired
	private OrgAttributeDefRepo orgAttributeDefRepo;

	@Autowired
	private OrgAttrDefListValuesRepo orgAttrDefListValuesRepo;

	private static final String DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
	private static Pattern pDomainNameOnly;

	static{
		pDomainNameOnly = Pattern.compile(DOMAIN_NAME_PATTERN);
	}

	public static String validateAndGetTopLevelDomain(String webAddress){
		URL url = null;
		try {
			url = new URL(webAddress);
		} catch (MalformedURLException e) {
			throw new InvalidDataException(ErrorCodes.ORG_BV_0010, "domain", "domain",
					webAddress, "Invalid Domian format:" + webAddress);
		}
		String webDomain = url.getHost();
		if(!pDomainNameOnly.matcher(webDomain).find()){
			throw new InvalidDataException(ErrorCodes.ORG_BV_0012, "domain", "domain",
					webDomain, "Invalid Domian " + webDomain);
		}
		return getTopLevelDomain(webDomain);
	}

	public static String getTopLevelDomain(String webDomain){
		return InternetDomainName.from(webDomain).topPrivateDomain().toString();
	}

	public void validateOrganization(OrganizationDTO organizationDTO, Organization org) {
		LOGGER.info("Validating organization");
		//validate name is unique or not
		if(!StringUtils.isAllBlank(organizationDTO.getName())){
			Organization organizationByName = organizationRepo.findByNameEqualsIgnoreCase(organizationDTO.getName());
			if(organizationByName != null){
				if(organizationDTO.getOrganizationID() == null || (!organizationDTO.getOrganizationID().equals(organizationByName.getOrganizationID()))){
					LOGGER.error("Organization  with name:"+organizationDTO.getName()+" already exists");
					throw new InvalidDataException(ErrorCodes.ORG_BV_0016,"organization","name",organizationDTO.getName(),"Organization  with name:"+organizationDTO.getName()+" already exists");
				}
			}
		}
		Optional<Country> country = countryRepo.findById(organizationDTO.getCountry().getCountryCode());
		if (country.isEmpty()) {
			LOGGER.error("Country not found with Code:" + organizationDTO.getCountry().getCountryCode());
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Country not found with Code:" + organizationDTO.getCountry().getCountryCode());
		}
		org.setCountry(country.get());
		boolean taxClassRequired = false;
		if (!StringUtils.isAllBlank(organizationDTO.getTaxId())) {
			Organization organization = findOrganizationByTaxID(organizationDTO.getTaxId());
			if (organization != null) {
				if (organizationDTO.getOrganizationID() == null ||
						 !organization.getOrganizationID().equals(organizationDTO.getOrganizationID())) {
					LOGGER.error("Organization exits with taxId:" + organizationDTO.getTaxId());
					throw new InvalidDataException(ErrorCodes.ORG_BV_0001, "organization", "taxId",
							organizationDTO.getTaxId(), "Organization exits with taxId:" + organizationDTO.getTaxId());
				}
			}
			org.setTaxId(organizationDTO.getTaxId());
			taxClassRequired = true;
		}
		if (taxClassRequired && organizationDTO.getTaxClassification() == null) {
			LOGGER.error("TaxClassification Mandatory");
			throw new InvalidDataException(ErrorCodes.ORG_BV_0002, "organization", "taxClassification", null,
					"TaxClassification Mandatory");
		}
		boolean stateOfIncRequired = false;
		if (organizationDTO.getTaxClassification() != null) {
			TaxClassificationDef taxClassificationDef = taxClassificationDefRepo.getByTaxClassCodeAndCountry(
					organizationDTO.getTaxClassification().getTaxClassCode(),
					organizationDTO.getCountry().getCountryCode());
			if (taxClassificationDef == null) {
				LOGGER.error("TaxCalassification:" + organizationDTO.getTaxClassification().getTaxClassCode()
						+ " not found with country:" + organizationDTO.getCountry().getCountryCode());
				throw new InvalidDataException(ErrorCodes.ORG_BV_0003, "organization", "taxClassification",
						organizationDTO.getTaxClassification().getTaxClassCode(),
						"TaxCalassification:" + organizationDTO.getTaxClassification().getTaxClassCode()
								+ " not found with country:" + organizationDTO.getCountry().getCountryCode());
			}
			if (taxClassificationDef.getStateInc()) {
				stateOfIncRequired = true;
			}
			org.setTaxClassification(taxClassificationDef);
		}
		if (stateOfIncRequired && StringUtils.isEmpty(organizationDTO.getStateOfInc())) {
			LOGGER.error("StateOfIncorporation required for taxClassification:"
					+ organizationDTO.getTaxClassification().getTaxClassCode());
			throw new InvalidDataException(ErrorCodes.ORG_BV_0004, "organization", "stateInc",
					organizationDTO.getStateOfInc(), "StateOfIncorporation required for taxClassification:"
					+ organizationDTO.getTaxClassification().getTaxClassCode());
		}
	}

	public void validateDomains(Organization organization, List<OrgDomainDTO> orgDomainDTOS) {
		LOGGER.info("Validating domains starts");
		if ((orgDomainDTOS != null && !orgDomainDTOS.isEmpty())) {
			List<OrgDomain> orgDomainsEntityList = organization.getOrgDomains();
			if (orgDomainsEntityList == null) {
				orgDomainsEntityList = new ArrayList<>();
				organization.setOrgDomains(orgDomainsEntityList);
			}

			// first remove deleted domains
			// in create new organization array will be empty loop will be skipped
			for (OrgDomain orgDomain : orgDomainsEntityList) {
				boolean exists = false;
				for (OrgDomainDTO orgDomainDTO : orgDomainDTOS) {
					if (orgDomain.getDomainID().equals(orgDomainDTO.getDomainID())) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					orgDomain.setDeleted(true);
				}
			}
			// Check any domains added or updated
			// will be called for create and update
			for (OrgDomainDTO orgDomainDTO : orgDomainDTOS) {
				// fetch toplevel domain
				String topLevelDomain =OrgValidation.validateAndGetTopLevelDomain(orgDomainDTO.getDomain());
				// New Domain Adding
				if (orgDomainDTO.getDomainID() == null) {
					isDomainUnique(topLevelDomain,organization.getOrganizationID());
					OrgDomain orgDomain = null;
					for (OrgDomain existing : orgDomainsEntityList) {
						if (existing.getWebDomain().equals(topLevelDomain)) {
							orgDomain = existing;
							break;
						}
					}
					if (orgDomain != null) {
						orgDomain.setDeleted(false);
					} else {
						orgDomain = modelMapper.map(orgDomainDTO, OrgDomain.class);
						orgDomain.setWebDomain(topLevelDomain);
						orgDomain.setOrganization(organization);
						orgDomain.setDeleted(false);
						orgDomainsEntityList.add(orgDomain);
					}

				}
				// Check if the existing values are getting updated
				else {
					// Existing Domain
					// loop the domains form DB and validate
					OrgDomain orgDomainFromDB = null;
					for (OrgDomain orgEnity : orgDomainsEntityList) {
						if (orgEnity.getDomainID().equals(orgDomainDTO.getDomainID())) {
							orgDomainFromDB = orgEnity;
							break;
						}
					}
					if (orgDomainFromDB == null) {
						LOGGER.error("OrgDomain by id:" + orgDomainDTO.getDomainID() + " not found");
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
								"OrgDomain by id:" + orgDomainDTO.getDomainID() + " not found");
					}
					if (!orgDomainFromDB.getDomain().equals(orgDomainDTO.getDomain())) {
						isDomainUnique(topLevelDomain,organization.getOrganizationID());
						orgDomainFromDB.setDomain(orgDomainDTO.getDomain());
						orgDomainFromDB.setWebDomain(topLevelDomain);
					}
				}
			}
		} else {
			LOGGER.error("AtLeast one domain mandatory");
			throw new InvalidDataException(ErrorCodes.ORG_BV_0008, "organization", "orgDomains", null,
					"AtLeast one domain mandatory");
		}
		LOGGER.info("Domain validation done");
	}

	private void isDomainUnique(String webDomain,String organizationID) {
		OrgDomain orgDomainByDomain = orgDomainRepo.findByWebDomain(organizationID,webDomain);
		if (orgDomainByDomain != null) {
			LOGGER.error("Domain :" + webDomain + " already mapped to organization:"
					+ orgDomainByDomain.getOrganization().getName());
			throw new InvalidDataException(ErrorCodes.ORG_BV_0006, "organization", "orgDoamins", webDomain, "domain :"
					+ webDomain + " already mapped to organization:" + orgDomainByDomain.getOrganization().getName());
		}
	}

	private Organization findOrganizationByTaxID(String taxId) {
		return organizationRepo.findByTaxId(taxId);
	}

	public void validateCommunicationMail(List<String> domains, String email) {
		if (!domains.isEmpty()) {
			String[] splitedMail = email.split("@");
			if (splitedMail.length < 2) {
				LOGGER.error("InValid email:" + email);
				throw new InvalidDataException(ErrorCodes.ORG_BV_0009, "organization", "orgCommunications", email,
						"InValid email:" + email);
			} else {
				String toplevelMailExt = OrgValidation.getTopLevelDomain(splitedMail[1]);
				boolean isWhiteListedMail = false;
				for (String domain : domains) {
					if (toplevelMailExt.equalsIgnoreCase(domain)) {
						isWhiteListedMail = true;
						break;
					}
				}

				if (!isWhiteListedMail) {
					LOGGER.error("Email extension:'" + splitedMail[1] + "' not matched with domains");
					throw new InvalidDataException(ErrorCodes.ORG_BV_0007, "organization", "orgCommunications", email,
							"Email extension:'" + splitedMail[1] + "' not matched with domains");
				}
			}
		} else {
			LOGGER.error("Domains mandatory to create communication");
			throw new InvalidDataException(ErrorCodes.ORG_BV_0006, "organization", "orgDomains", null,
					"Domains mandatory to create communication");
		}
	}

	public List<String> getDomainList(Organization organization) {
		return organization.getOrgDomains().stream().filter(orgDomain -> !orgDomain.getDeleted())
				.map(OrgDomain::getWebDomain).collect(Collectors.toList());
	}

	public void validateCommunication(Organization organization, List<OrgCommunicationDTO> orgCommunicationsDTO) {
		LOGGER.info("Communication validation starts");
		List<OrgCommunication> orgCommEntiyList = organization.getOrgCommunications();
		if (orgCommEntiyList == null) {
			orgCommEntiyList = new ArrayList<>();
			organization.setOrgCommunications(orgCommEntiyList);
		}
		orgCommEntiyList.sort(OrgCommunication.createCommunicationLambdaComparator());

		if (orgCommunicationsDTO != null && !orgCommunicationsDTO.isEmpty()) {
			// first one will be taken remaining will be discarded
			OrgCommunication orgCommunication = modelMapper.map(orgCommunicationsDTO.get(0), OrgCommunication.class);
			if (!orgCommEntiyList.isEmpty()) {
				OrgCommunication existingCommunication = orgCommEntiyList.get(orgCommEntiyList.size() - 1);

				if (!existingCommunication.equals(orgCommunication)) {
					if (existingCommunication.getStartDate().equals(LocalDate.now())) {
						existingCommunication.setAuthSignataryFn(orgCommunication.getAuthSignataryFn());
						existingCommunication.setAuthSignataryLn(orgCommunication.getAuthSignataryLn());
						existingCommunication.setAuthSignataryEmail(orgCommunication.getAuthSignataryEmail());
						existingCommunication.setAuthSignataryPhone(orgCommunication.getAuthSignataryPhone());
					} else {
						orgCommunication.setOrganization(organization);
						orgCommunication.setStartDate(LocalDate.now());
						// set existing record end date
						existingCommunication.setEndDate(LocalDate.now().minusDays(1));
						orgCommEntiyList.add(orgCommunication);
					}
				}
			} else {
				orgCommunication.setOrganization(organization);
				orgCommunication.setStartDate(LocalDate.now());
				orgCommEntiyList.add(orgCommunication);
			}
		}
		if (!orgCommEntiyList.isEmpty()) {
			OrgCommunication latestOrgComm = orgCommEntiyList.get(orgCommEntiyList.size() - 1);
			validateCommunicationMail(getDomainList(organization), latestOrgComm.getAuthSignataryEmail());
		}
		LOGGER.info("Communication validation done");
	}

	public void validateAddress(Organization organization,List<OrgAddressDTO> orgAddressDTOList){
		List<OrgAddress> orgAddressEntityList = organization.getOrgAddresses();
		if (orgAddressEntityList == null) {
			orgAddressEntityList = new ArrayList<>();
		}
		Map<String,List<OrgAddress>> orgAddressByType = new HashMap<>();
		//separate addresses by Type
		for(OrgAddress orgAddress:orgAddressEntityList){
			if(!orgAddressByType.containsKey(orgAddress.getOrgAddressType().getAddressType())){
				orgAddressByType.put(orgAddress.getOrgAddressType().getAddressType(),new ArrayList<>());
			}
			orgAddressByType.get(orgAddress.getOrgAddressType().getAddressType()).add(orgAddress);
			//sort by addressList
			orgAddressByType.get(orgAddress.getOrgAddressType().getAddressType()).sort(OrgAddress.createOrgAddLambdaComparator());
		}
		Set<String> addressType = new HashSet<>();
		if(orgAddressDTOList != null && !orgAddressDTOList.isEmpty()){
			//deleteAddresses
			List<OrgAddress> existingAddresses = orgAddressEntityList.stream()
					.filter(address -> address.getEndDate() == null)
					.collect(Collectors.toList());
			for(OrgAddress existingAddress:existingAddresses){
				boolean addressDeleted = true;
				for(OrgAddressDTO orgAddressDTO:orgAddressDTOList){
					if(existingAddress.getOrgAddressType().getAddressType().equals(orgAddressDTO.getOrgAddressType().getAddressType())){
						addressDeleted = false;
						break;
					}
				}
				if(addressDeleted){
					existingAddress.setEndDate(LocalDate.now());
				}
			}//delete address
			for(OrgAddressDTO orgAddressDTO:orgAddressDTOList) {
				Set<ConstraintViolation<OrgAddressDTO>> violations = validator.validate(orgAddressDTO);
				if (!violations.isEmpty()) {
					throw new ConstraintViolationException(violations);
				}
				OrgAddress orgAddress = modelMapper.map(orgAddressDTO,OrgAddress.class);
				if(orgAddress.getCountry() != null){
					Optional<Country> optionalCountry = countryRepo.findById(orgAddress.getCountry().getCountryCode());
					if(optionalCountry.isEmpty()){
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"Country by code:"+orgAddress.getCountry().getCountryCode()+" not found");
					}
					orgAddress.setCountry(optionalCountry.get());
				}
				if(addressType.contains(orgAddress.getOrgAddressType().getAddressType())){
					throw new InvalidDataException(ErrorCodes.ORG_BV_0017,"orgAddress","orgAddressType",orgAddress.getOrgAddressType().getAddressType(),"Duplicate address by type:"+orgAddress.getOrgAddressType().getAddressType()+" not allowed");
				}
				addressType.add(orgAddress.getOrgAddressType().getAddressType());
				if(orgAddressByType.containsKey(orgAddress.getOrgAddressType().getAddressType())){
					List<OrgAddress> orgAddresses = orgAddressByType.get(orgAddress.getOrgAddressType().getAddressType());
					//get latest orgAddress
					OrgAddress latestAddress = orgAddresses.get(orgAddresses.size()-1);
					if(!latestAddress.equals(orgAddress)){
						if(!latestAddress.getStartDate().equals(LocalDate.now())){
							latestAddress.setEndDate(LocalDate.now().minusDays(1));
							orgAddress.setStartDate(LocalDate.now());
							orgAddress.setOrganization(organization);
							orgAddresses.add(orgAddress);
						} else{
							latestAddress.setAddressName(orgAddress.getAddressName());
							latestAddress.setAddress1(orgAddress.getAddress1());
							latestAddress.setAddress2(orgAddress.getAddress2());
							latestAddress.setAddress3(orgAddress.getAddress3());
							latestAddress.setAddress4(orgAddress.getAddress4());
							latestAddress.setAddress5(orgAddress.getAddress5());
							latestAddress.setCountry(orgAddress.getCountry());
							latestAddress.setState(orgAddress.getState());
							latestAddress.setCity(orgAddress.getCity());
							latestAddress.setPostalCode(orgAddress.getPostalCode());
							latestAddress.setPostOfficeBox(orgAddress.getPostOfficeBox());
							latestAddress.setEndDate(null);
						}
					} else{
						latestAddress.setEndDate(null);
					}
				} else{
					List<OrgAddress> orgAddressList = new ArrayList<>();
					orgAddress.setStartDate(LocalDate.now());
					orgAddress.setOrganization(organization);
					orgAddressList.add(orgAddress);
					orgAddressByType.put(orgAddress.getOrgAddressType().getAddressType(),orgAddressList);
				}
			}
		}
		//combine all addressTypes in orgAddressByType(map)
		List<OrgAddress> orgAddresses = new ArrayList<>();
		orgAddressByType.forEach((s, orgAddressesList) -> {
			orgAddresses.addAll(orgAddressesList);
		});
		organization.setOrgAddresses(orgAddresses);
	}

	public Organization fetchOrganization(String organizationID) {
		return organizationRepo.findById(organizationID).orElseThrow(() -> {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"Organization by id:" + organizationID + " not found");
		});
	}

	public OrganizationDocument validateOrganizationDocument(OrganizationDocumentDTO organizationDocumentDTO) {
		// Check if organization doc ID i.e. if not null means uploading the
		// auto-assigned doc
		// And validate if the status of auto-assigned is pending
		OrganizationDocument organizationDocEntity = null;
		if (organizationDocumentDTO.getOrganizationDocID() != null
				&& !organizationDocumentDTO.getOrganizationDocID().isEmpty()) {
			Optional<OrganizationDocument> organizationDoc = organizationDocumentRepo
					.findById(organizationDocumentDTO.getOrganizationDocID());
			if (organizationDoc.isEmpty()) {
				LOGGER.error(
						"Organization Document by id " + organizationDocumentDTO.getOrganizationDocID() + " not found");
				throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
						"Organization Document by id " + organizationDocumentDTO.getOrganizationDocID() + " not found");
			}
			if (organizationDoc.get().getStatus().compareTo(EnumDocStatus.Pending) != 0) {
				LOGGER.error("Organization Document by id " + organizationDocumentDTO.getOrganizationDocID()
						+ " not in pending status");
				throw new InvalidDataException(ErrorCodes.ORG_BV_0011, "organizationDocument", "organizationDocID",
						organizationDocumentDTO.getOrganizationDocID(),
						"Organization Document by id " + organizationDocumentDTO.getOrganizationDocID()
								+ " not in pending status, current status " + organizationDoc.get().getStatus());
			}
			organizationDocEntity = organizationDoc.get();
		}

		// Get the document definition for organization documents
		DocumentDef documentDef = documentDefValidatior
				.getDocumentDef(organizationDocumentDTO.getDocumentDef().getDocumentDefID());

		// Convert organization-specific attributes to a list of base attributes
		List<BaseDocAttributesDTO> baseAttrDTO = new ArrayList<>();
		organizationDocumentDTO.getOrgDocAttributes().forEach(organizationDocAttr -> {
			baseAttrDTO.add(organizationDocAttr);
		});
		documentDefValidatior.validateDocIssueAndExpiryDates(documentDef, organizationDocumentDTO.getIssueDate(),
				organizationDocumentDTO.getExpiryDate());
		// Now check all the mandatory attributes are passed
		List<DocAttributeDef> docAttrDef = documentDefValidatior.validateDocRequiredAttributes(documentDef,
				baseAttrDTO);

		// Now check the attributes are as per definition
		documentDefValidatior.validateDocAttributes(documentDef, docAttrDef, baseAttrDTO);

		return organizationDocEntity;
	}

	public OrganizationDocument getOrgDocForUpload(String organizationDocID) {
		return organizationDocumentRepo.findById(organizationDocID).orElseThrow(() -> {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "Organization document not found");
		});
	}

	public OrgInActiveStatusCodes getOrgInactiveStatusCode(OrganizationStatusDTO orgInactiveStatusCodeDto) {
		return orgInActiveStatusCodeRepo.findById(orgInactiveStatusCodeDto.getInactiveStatusCode().getCode()).orElseThrow(() -> {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
					"OrgInActiveStatusCodes " + orgInactiveStatusCodeDto.getInactiveStatusCode().getCode() + " not configured");
		});
	}

	public void validateStatusTransition(Organization organization, OrganizationStatusDTO newOrgStatusDTO) {
		List<OrganizationStatus> orgStatusEnityList = organization.getStatus();
		orgStatusEnityList.sort(OrganizationStatus.createOrgStatusLambdaComparator());
		OrganizationStatus latestOrgStatusEnity = orgStatusEnityList.get(orgStatusEnityList.size() - 1);

		OrgStatus latestStatus = latestOrgStatusEnity.getStatusCode();
		OrgStatus newOrgStatus = newOrgStatusDTO.getStatusCode();

		if(newOrgStatus.equals(OrgStatus.DRAFT)) {
			if (latestStatus.equals(OrgStatus.INACTIVE) && !latestOrgStatusEnity.getInactiveStatusCode().isReinstate()) {
				LOGGER.error("Cannot move to status " + newOrgStatus + " as status cannot be reinstated from "
						+ latestStatus);
				throw new InvalidDataException(ErrorCodes.ORG_BV_0014, "status", "statusCode", newOrgStatus.toString(),
						"Cannot move to status " + newOrgStatus + " as status cannot be reinstated from "
								+ latestStatus);
			}
		}
		if(newOrgStatus.equals(OrgStatus.PENDING)){
			if(latestStatus.equals(OrgStatus.INACTIVE) && !latestOrgStatusEnity.getInactiveStatusCode().isReinstate()){
				LOGGER.error("Cannot move to status " + newOrgStatus + " as status cannot be reinstated from "
						+ latestStatus);
				throw new InvalidDataException(ErrorCodes.ORG_BV_0014, "status", "statusCode", newOrgStatus.toString(),
						"Cannot move to status " + newOrgStatus + " as status cannot be reinstated from "
								+ latestStatus);
			} else if (organization.getOrgCommunications() == null || organization.getOrgCommunications().isEmpty()) {
				LOGGER.error("Cannot move to status " + newOrgStatus + " as communication details are empty");
				throw new InvalidDataException(ErrorCodes.ORG_BV_0013, "status", "statusCode",
						newOrgStatus.toString(),
						"Cannot move to status " + newOrgStatus + " as communication details are empty");
			}
		}
		if(newOrgStatus.equals(OrgStatus.ACTIVE)){
			if (latestStatus.equals(OrgStatus.INACTIVE) && !latestOrgStatusEnity.getInactiveStatusCode().isReinstate()) {
				LOGGER.error("Cannot move to status " + newOrgStatus + " as status cannot be reinstated from "
						+ latestStatus);
				throw new InvalidDataException(ErrorCodes.ORG_BV_0014, "status", "statusCode", newOrgStatus.toString(),
						"Cannot move to status " + newOrgStatus + " as status cannot be reinstated from "
								+ latestStatus);

			} else if (organization.getOrgCommunications() == null || organization.getOrgCommunications().isEmpty()) {
				LOGGER.error("Cannot move to status " + newOrgStatus + " as communication details are empty");
				throw new InvalidDataException(ErrorCodes.ORG_BV_0013, "status", "statusCode",
						newOrgStatus.toString(),
						"Cannot move to status " + newOrgStatus + " as communication details are empty");
			}
		}
		if(newOrgStatus.equals(OrgStatus.INACTIVE)){
			if(newOrgStatusDTO.getInactiveStatusCode() == null){
				LOGGER.error("Required InActiveStatusCode to update status as "+newOrgStatus);
				throw new InvalidDataException(ErrorCodes.ORG_BV_0013, "status", "statusCode",
						newOrgStatus.toString(),"Required InActiveStatusCode to update status as "+newOrgStatus);
			}
		}

	}


	public void validateOrgAttributes(Organization organization, List<OrganizationAttributesDTO> orgAttributesDTOS) {
		List<OrganizationAttributes> orgAttributesList = organization.getOrganizationAttributes();
		if(orgAttributesList == null){
			orgAttributesList = new ArrayList<>();
			organization.setOrganizationAttributes(orgAttributesList);
		}
		List<OrgAttributeDef> orgAttributeDefList = orgAttributeDefRepo.findMandatoryAttributes();
		validateRequiredAttributes(orgAttributeDefList,orgAttributesDTOS);
		if(orgAttributesDTOS != null && !orgAttributesDTOS.isEmpty()) {
			for (OrganizationAttributesDTO orgAttributesDTO : orgAttributesDTOS) {
				Set<ConstraintViolation<OrganizationAttributesDTO>> constraintViolations = validator.validate(orgAttributesDTO);
				if (!constraintViolations.isEmpty()) {
					throw new ConstraintViolationException(constraintViolations);
				}
				OrgAttributeDef orgAttributeDef = getOrgAttributeDef(orgAttributesDTO.getOrgAttributeDef().getAttrDefId());
				if (orgAttributesDTO.getOrgAttrID() != null) {
					OrganizationAttributes updatableOrgAttributes = null;
					for (OrganizationAttributes orgAttributes : orgAttributesList) {
						if (orgAttributes.getOrgAttrID() != null && orgAttributesDTO.getOrgAttrID().equals(orgAttributes.getOrgAttrID())) {
							updatableOrgAttributes = orgAttributes;
							break;
						}
					}
					if (updatableOrgAttributes == null) {
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "OrganizatiomAttribute by id:" + orgAttributesDTO.getOrgAttrID() + " not found");
					} else {
						if (!updatableOrgAttributes.getAttrValue().equals(orgAttributesDTO.getAttrValue())) {
							BaseAttributeValidator.validateAttrTypeValue(orgAttributeDef.getAttrType(), orgAttributesDTO.getAttrValue());
							if (orgAttributeDef.getDefinedList()) {
								if (!valueExistsInList(orgAttrDefListValuesRepo.getAttributeListValues(orgAttributeDef.getAttrDefId()), orgAttributesDTO.getAttrValue())) {
									throw new InvalidDataException(ErrorCodes.W_ATTR_V_0004, "organizationAttributes", "attrValue", orgAttributesDTO.getAttrValue(), " AttrValue " + orgAttributesDTO.getAttrValue() + " not in predefined list");
								}
							}
							if (updatableOrgAttributes.getStartDate().equals(LocalDate.now())) {
								updatableOrgAttributes.setAttrValue(orgAttributesDTO.getAttrValue());
								updatableOrgAttributes.setOrgAttributeDef(orgAttributeDef);
							} else {
								updatableOrgAttributes.setEndDate(LocalDate.now().minusDays(1));
								OrganizationAttributes organizationAttributes = new OrganizationAttributes();
								organizationAttributes.setOrgAttributeDef(orgAttributeDef);
								organizationAttributes.setAttrValue(orgAttributesDTO.getAttrValue());
								organizationAttributes.setStartDate(LocalDate.now());
								organizationAttributes.setOrganization(organization);
								orgAttributesList.add(organizationAttributes);
							}
						}

					}
				} else {
					BaseAttributeValidator.validateAttrTypeValue(orgAttributeDef.getAttrType(), orgAttributesDTO.getAttrValue());
					OrganizationAttributes organizationAttributes = new OrganizationAttributes();
					organizationAttributes.setOrgAttributeDef(orgAttributeDef);
					organizationAttributes.setOrganization(organization);
					if (orgAttributeDef.getDefinedList()) {
						if (orgAttributeDef.getStatus().equals(EnumAttributeStatus.ACTIVE)) {
							if (orgAttributeDef.getRequired() && StringUtils.isBlank(orgAttributesDTO.getAttrValue())) {
								throw new InvalidDataException(ErrorCodes.W_ATTR_V_0005, "workerAttributes", "attrValue", orgAttributesDTO.getAttrValue(), "AttrValue mandatory");
							}
						}
						if (!valueExistsInList(orgAttrDefListValuesRepo.getAttributeListValues(orgAttributeDef.getAttrDefId()), orgAttributesDTO.getAttrValue())) {
							throw new InvalidDataException(ErrorCodes.W_ATTR_V_0004, "organizationAttributes", "attrValue", orgAttributesDTO.getAttrValue(), " AttrValue " + orgAttributesDTO.getAttrValue() + " not in predefined list");
						}
						organizationAttributes.setAttrValue(orgAttributesDTO.getAttrValue());
					} else {
						if (orgAttributeDef.getStatus().equals(EnumAttributeStatus.ACTIVE)) {
							if (StringUtils.isBlank(orgAttributesDTO.getAttrValue())) {
								if (orgAttributeDef.getRequired()) {
									if (StringUtils.isBlank(orgAttributeDef.getDefaultValue())) {
										throw new InvalidDataException(ErrorCodes.W_ATTR_V_0005, "organizationAttributes", "attrValue", orgAttributesDTO.getAttrValue(), "AttrValue mandatory");
									}
									organizationAttributes.setAttrValue(orgAttributeDef.getDefaultValue());
								}
							} else {
								organizationAttributes.setAttrValue(orgAttributesDTO.getAttrValue());
							}
						}
					}
					organizationAttributes.setStartDate(LocalDate.now());
					orgAttributesList.add(organizationAttributes);
				}
			}
		}
	}

	private void validateRequiredAttributes(List<OrgAttributeDef> orgAttributeDefList, List<OrganizationAttributesDTO> orgAttributesDTOS) {
		if(orgAttributeDefList != null && !orgAttributeDefList.isEmpty()){
			if(orgAttributesDTOS == null || orgAttributesDTOS.isEmpty()){
				throw new InvalidDataException(ErrorCodes.ORG_BV_0018,"organization","organizationAttributes",null,"Mandatory attributes not passed");
			}
			for(OrgAttributeDef orgAttributeDef:orgAttributeDefList){
				boolean matched = false;
				for(OrganizationAttributesDTO orgAttributesDTO:orgAttributesDTOS){
					if(orgAttributesDTO.getOrgAttributeDef() == null || orgAttributesDTO.getOrgAttributeDef().getAttrDefId() == null){
						throw new InvalidDataException(ErrorCodes.ORG_BV_0019,"attributes","attributeDef",null,"attributeDef mandatory");
					}
					if(orgAttributesDTO.getOrgAttributeDef().getAttrDefId().equals(orgAttributeDef.getAttrDefId())){
						matched = true;
					}
				}
				if(!matched){
					throw new InvalidDataException(ErrorCodes.ORG_BV_0020,"attributes","attributeDef",null,"Attribute "+orgAttributeDef.getAttrName()+" missed");
				}
			}
		}
	}

	private  boolean valueExistsInList(List<String> attrListValues, String attrValue) {
		return attrListValues.contains(attrValue);
	}

	public OrgAttributeDef getOrgAttributeDef(String attrDefId) {
		return orgAttributeDefRepo.findById(attrDefId).orElseThrow(() -> new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"OrgAttrDef by id:"+attrDefId+" not found"));
	}

}
