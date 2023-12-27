package com.lucid.recruit.referencedata.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucid.core.azure.AzureBlobService;
import com.lucid.core.azure.EnumAzureContainers;
import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.docs.constants.EnumDefaultDocStatus;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.dto.DocumentDefDTO;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;
import com.lucid.recruit.docs.repo.DefaultDocAssignmentsRepo;
import com.lucid.recruit.docs.service.DocumentDefService;
import com.lucid.recruit.org.dto.OrgAddressTypeDTO;
import com.lucid.recruit.org.entity.OrgAddressType;
import com.lucid.recruit.org.entity.OrgDomain;
import com.lucid.recruit.org.repo.OrgAddressTypeRepo;
import com.lucid.recruit.org.repo.OrgDomainRepo;
import com.lucid.recruit.org.validation.OrgValidation;
import com.lucid.recruit.referencedata.constants.EnumParametersType;
import com.lucid.recruit.referencedata.dto.*;
import com.lucid.recruit.referencedata.entity.*;
import com.lucid.recruit.referencedata.repo.*;
import com.lucid.recruit.worker.entity.WorkerTypeCode;
import com.lucid.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lucid.recruit.org.dto.OrganizationDTO;
import com.lucid.recruit.org.repo.OrganizationRepo;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReferenceDataServiceImpl implements RefernceDataService {

	@Autowired
	private JobRepo jobRepo;
	@Autowired
	private OrganizationRepo organizationRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DepartmentRepo departmentRepo;
	@Autowired
	private WorkerTypeRepo workerTypeRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RefCountryRepo refCountryRepo;

	@Autowired
	private TaxClassificationDefRepo taxClassificationDefRepo;

	@Autowired
	private OrgInActiveStatusCodesRepo orgInActiveStatusCodesRepo;

	@Autowired
	private WorkerInActiveStatusCodesRepo workerInActiveStatusCodesRepo;

	@Autowired
	private OrgAddressTypeRepo orgAddressTypeRepo;

	@Autowired
	private DefaultDocAssignmentsRepo defaultDocAssignmentsRepo;

	@Autowired
	private DocumentDefService documentDefService;

	@Autowired
	private OrgDomainRepo orgDomainRepo;

	@Autowired
	private AzureBlobService azureBlobService;

	@Autowired
	private TenantParametersService tenantParametersService;

	private static final String DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
	private static Pattern pDomainNameOnly;

	static{
		pDomainNameOnly = Pattern.compile(DOMAIN_NAME_PATTERN);
	}

	@Override
	public List<OrganizationDTO>  retriveAllOrganizations() {
		return organizationRepo.findAll().stream()
				.map(organization -> modelMapper.map(organization, OrganizationDTO.class)).collect(Collectors.toList());
	}

	@Override
	public WorkerTypeDTO updateWorkerType(WorkerTypeCode workerTypeCode,WorkerTypeDTO workerTypeDto) {
		WorkerType workerType = workerTypeRepo.findById(workerTypeCode).orElseThrow(() -> new EntityNotFoundException("WorkerType by code:"+workerTypeCode+" not found"));
		workerType.setWorkerTypeName(workerTypeDto.getWorkerTypeName());
		workerType.setWorkerTypeDesc(workerTypeDto.getWorkerTypeDesc());
		workerTypeRepo.save(workerType);
		return modelMapper.map(workerType,WorkerTypeDTO.class);
	}

	@Override
	public List<WorkerTypeDTO> retrieveAllWorkerTypes() {
		List<WorkerType> workerTypes = workerTypeRepo.findAll();
		return workerTypes.stream().map(workerType -> {
			return modelMapper.map(workerType,WorkerTypeDTO.class);
		}).toList();
	}

	@Override
	public RefDepartmentDTO updateDepartment(String deptID, RefDepartmentDTO departmentDTO) {
		Department department = departmentRepo.findById(deptID).orElseThrow(() -> {throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"Department by id:"+deptID+" not found to upadte");});
		if(departmentDTO != null){
			if(!Strings.isNullOrEmpty(departmentDTO.getDeptName())){
				if(!department.getDeptName().equals(departmentDTO.getDeptName())){
					validateDeptName(department.getDeptID(),departmentDTO.getDeptName());
				}
				department.setDeptName(departmentDTO.getDeptName());
			}
			if(!Strings.isNullOrEmpty(departmentDTO.getDeptDesc()) && (Strings.isNullOrEmpty(department.getDeptDesc()) || !department.getDeptDesc().equals(departmentDTO.getDeptDesc()))){
				department.setDeptDesc(departmentDTO.getDeptDesc());
			}
		}
		departmentRepo.save(department);
		RefDepartmentDTO responseDto = modelMapper.map(department, RefDepartmentDTO.class);
		if(department.getJobs().size()>1){
			responseDto.setBillable(null);
		}
		else {
			responseDto.setBillable(department.getJobs().get(0).isBillable());
		}
		return responseDto;
	}

	@Override
	public RefDepartmentDTO createDepartment(RefDepartmentDTO departmentDTO) {
		//ModelMapper modelMapper = new ModelMapper();
		validateDeptName(null,departmentDTO.getDeptName());
		Department department = modelMapper.map(departmentDTO, Department.class);
		departmentRepo.save(department);
		// Convert the saved Department back to DepartmentDTO
		List<Job> jobs = new ArrayList<>();
		if(departmentDTO.getBillable() == null || departmentDTO.getBillable()){
			Job job = new Job();
			job.setJobName("BILLABLE");
			job.setBillable(true);
			job.setDepartment(department);
			jobRepo.save(job);
			jobs.add(job);
		}
		if(departmentDTO.getBillable() == null || !departmentDTO.getBillable()){
			Job job = new Job();
			job.setJobName("NONBILLABLE");
			job.setBillable(false);
			job.setDepartment(department);
			jobRepo.save(job);
			jobs.add(job);
		}
		department.setJobs(jobs);
		RefDepartmentDTO responseDto = modelMapper.map(department, RefDepartmentDTO.class);
		if(department.getJobs().size()>1){
			responseDto.setBillable(null);
		}
		else {
			responseDto.setBillable(department.getJobs().get(0).isBillable());
		}
		return responseDto;
	}

	private void validateDeptName(String deptID, String deptName) {
		Department department = departmentRepo.findByDeptName(deptName);
		if(department != null){
			if(deptID == null || !department.getDeptID().equals(deptID)){
				throw new InvalidDataException(ErrorCodes.REF_V_0003,"department","deptName",deptName,"Department:"+deptName+" already exists");
			}
		}
	}

	@Override
	public RefDepartmentDTO retrieveDepartment(String deptID) {
		Optional<Department> optionalDepartment = departmentRepo.findById(deptID);
		if (optionalDepartment.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "Department by id:"+deptID+" not found");
		}
		Department department = optionalDepartment.get();
		RefDepartmentDTO responseDto = modelMapper.map(department, RefDepartmentDTO.class);
		if(department.getJobs().size()>1){
			responseDto.setBillable(null);
		}
		else {
			responseDto.setBillable(department.getJobs().get(0).isBillable());
		}
		return responseDto;
	}

	@Override
	public Set<Currency> getAllCurrencyCodes() {
		return Currency.getAvailableCurrencies();
	}

	@Override
	public Country createRefCountry(Country refCountry) {
        if(refCountryRepo.existsById(refCountry.getCountryCode())){
			throw new InvalidDataException(ErrorCodes.REF_V_0001,"refCountry","countryCode",refCountry.getCountryCode(),"Country with code:"+refCountry.getCountryCode()+" already exists");
		}
		return refCountryRepo.save(refCountry);
	}

	@Override
	public List<Country> getAllCountrys() {
		return refCountryRepo.findAll();
	}


	@Override
	public boolean loadCountrys() throws JsonProcessingException {
		String apiUrl="https://restcountries.com/v3.1/all";
		ResponseEntity<String> response = restTemplate.getForEntity(apiUrl,String.class);
		JsonNode jsonNodes = new ObjectMapper().readTree(response.getBody());
		List<Country> refCountries = new ArrayList<>();
		for(JsonNode jsonNode:jsonNodes){
            Country refCountry = new Country();
			refCountry.setCountryName(String.valueOf(jsonNode.get("name").get("common")).replace("\"","").replace(" ",""));
			refCountry.setCountryCode(String.valueOf(jsonNode.get("cca3")).replace("\"",""));
			refCountries.add(refCountry);
		}
		refCountryRepo.saveAll(refCountries);
		return true;
	}

	@Override
	public List<TaxClassificationDefDTO> getTaxClassifications(String countryCode) {
		List<TaxClassificationDefDTO> taxList = new ArrayList<>();
		List<TaxClassificationDef> taxListRes;
		if(!StringUtils.isEmpty(countryCode)){
			taxListRes=taxClassificationDefRepo.fetchTaxClassByCountry(countryCode);
		} else{
			taxListRes = taxClassificationDefRepo.findAll();
		}
		if(taxListRes.size()> 0){
			for(TaxClassificationDef taxData:taxListRes){
				taxList.add(modelMapper.map(taxData,TaxClassificationDefDTO.class));
			}

		}
		return  taxList;
	}

	@Override
	public List<OrgInActiveStatusCodesDTO> getOrgInActiveStatusCodes() {
		List<OrgInActiveStatusCodesDTO> orgInActiveStatusCodesDTOList = new ArrayList<>();
		List<OrgInActiveStatusCodes> orgInActiveStatusCodes = orgInActiveStatusCodesRepo.findAll();
		if(!orgInActiveStatusCodes.isEmpty()){
			for(OrgInActiveStatusCodes orgInActiveStatusCode:orgInActiveStatusCodes){
				orgInActiveStatusCodesDTOList.add(modelMapper.map(orgInActiveStatusCode,OrgInActiveStatusCodesDTO.class));
			}
		}
		return orgInActiveStatusCodesDTOList;
	}

	@Override
	public List<WorkerInActiveStatusCodesDTO> getWorkerInActiveStatusCodes() {
		List<WorkerInActiveStatusCodesDTO> workerInActiveStatusCodesDTOList = new ArrayList<>();
		List<WorkerInActiveStatusCodes> workerInActiveStatusCodes = workerInActiveStatusCodesRepo.findAll();
		if(!workerInActiveStatusCodes.isEmpty()){
			for(WorkerInActiveStatusCodes workerInActiveStatusCode:workerInActiveStatusCodes){
				workerInActiveStatusCodesDTOList.add(modelMapper.map(workerInActiveStatusCode,WorkerInActiveStatusCodesDTO.class));
			}
		}
		return workerInActiveStatusCodesDTOList;
	}

	@Override
	public List<OrgAddressTypeDTO> getAllOrgAddressTypes() {
		List<OrgAddressTypeDTO> orgAddressTypeDTOS = new ArrayList<>();
		List<OrgAddressType> orgAddressTypes = orgAddressTypeRepo.findAll();
		if(orgAddressTypes != null && !orgAddressTypes.isEmpty()){
			orgAddressTypes.forEach(orgAddressType -> {
				orgAddressTypeDTOS.add(modelMapper.map(orgAddressType,OrgAddressTypeDTO.class));
			});
		}
		return orgAddressTypeDTOS;
	}

	@Override
	public List<DocumentDefDTO> getDocDef(EnumDocRelatedEntity docRelatatedEntity, EnumDefaultDocStatus status, Boolean autoAssigned) {
		List<DocumentDefDTO> documentDefDTOS = new ArrayList<>();
		List<DefaultDocAssignments> defaultDocAssignments = documentDefService.getDefaultDocAssignments(docRelatatedEntity,status,autoAssigned);
		if(defaultDocAssignments != null && !defaultDocAssignments.isEmpty()){
			defaultDocAssignments.forEach(defaultDocAssignment -> {
				documentDefDTOS.add(modelMapper.map(defaultDocAssignment.getDocumentDef(),DocumentDefDTO.class));
			});
		}
		return documentDefDTOS;
	}

	@Override
	public boolean isDomainMapped(String organizationID, String domain) {
		if(!(domain.startsWith("http://") || domain.startsWith("https://"))){
			domain = "http://"+domain;
		}
		String topLevelDomain = OrgValidation.validateAndGetTopLevelDomain(domain);
		OrgDomain existingDomain = orgDomainRepo.findByWebDomain(organizationID, topLevelDomain);
		if (existingDomain != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<RefDepartmentDTO> retrieveAllDepartments(Boolean billable) {
		List<Job> jobs = jobRepo.findAll(null,billable);
		Map<String,RefDepartmentDTO> deptDTO= new HashMap<>();
		if(jobs != null && !jobs.isEmpty()){
			for(Job job:jobs){
				Department department = job.getDepartment();
				RefDepartmentDTO departmentDTO=deptDTO.get(department.getDeptID());
				if(departmentDTO== null){
					departmentDTO= new RefDepartmentDTO();
					departmentDTO.setDeptID(department.getDeptID());
					departmentDTO.setDeptName(department.getDeptName());
					departmentDTO.setDeptDesc(department.getDeptDesc());
					if(department.getJobs().size()>1){
						departmentDTO.setBillable(null);
					}
					else {
						departmentDTO.setBillable(department.getJobs().get(0).isBillable());
					}
					deptDTO.put(department.getDeptID(),departmentDTO);
				}
				departmentDTO.getJobsList().add(modelMapper.map(job,RefJobDTO.class));
			}
		}
		return deptDTO.values().stream().toList();
	}

	@Override
	public String uploadTenantLogo(MultipartFile multipartFile) {
		if(!multipartFile.getContentType().startsWith("image/")){
			throw new InvalidDataException(ErrorCodes.REF_V_0002,"tenant","logo",multipartFile.getContentType(),"Not a image");
		}
		if(multipartFile.getSize()>(240*1024)){
			throw new InvalidDataException(ErrorCodes.REF_V_0002,"tenant","logo",multipartFile.getContentType(),"File size exceed 240 kilo byte");
		}
		String fileName = multipartFile.getOriginalFilename();
		try {
			azureBlobService.upload(multipartFile, fileName, EnumAzureContainers.tenant);
		} catch (IOException e) {
			// throw new InvalidDataException(ErrorCodes.W_BV_0010, "WorkerDocuent", "File
			// data", null, e.getMessage());
			throw new RuntimeException(e);
		}
		TenantParametersDTO tenantParametersDTO = new TenantParametersDTO();
		tenantParametersDTO.setType(EnumParametersType.STRING);
		tenantParametersDTO.setName("TENANTLOGO");
		tenantParametersDTO.setValue(fileName);
		tenantParametersService.createTenantParameter(tenantParametersDTO);
		return multipartFile.getOriginalFilename() + "Uploaded sucessfully ";
	}

	@Override
	public byte[] getTenantLogo() {
		byte[] filedata;
		try {
			TenantParametersDTO tenantParameters = tenantParametersService.findTenantParameterByName("TENANTLOGO");
			filedata = azureBlobService.getFile(tenantParameters.getValue(),EnumAzureContainers.tenant);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return filedata;
	}

}
