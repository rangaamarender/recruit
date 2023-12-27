package com.lucid.recruit.referencedata.service;

import java.util.Currency;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lucid.recruit.docs.constants.EnumDefaultDocStatus;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.dto.DocumentDefDTO;
import com.lucid.recruit.org.dto.OrgAddressTypeDTO;
import com.lucid.recruit.org.dto.OrganizationDTO;
import com.lucid.recruit.referencedata.dto.*;
import com.lucid.recruit.referencedata.entity.Job;
import com.lucid.recruit.referencedata.entity.Country;
import com.lucid.recruit.worker.entity.WorkerTypeCode;
import org.springframework.web.multipart.MultipartFile;

public interface RefernceDataService {

	List<OrganizationDTO> retriveAllOrganizations();

	WorkerTypeDTO updateWorkerType(WorkerTypeCode workerTypeCode,WorkerTypeDTO workerTypeDTO);

	List<WorkerTypeDTO> retrieveAllWorkerTypes();

	RefDepartmentDTO createDepartment(RefDepartmentDTO departmentDTO);

	RefDepartmentDTO retrieveDepartment(String deptID);

	RefDepartmentDTO updateDepartment(String deptID, RefDepartmentDTO departmentDTO);


	Set<Currency> getAllCurrencyCodes();

	Country createRefCountry(Country refCountry);

	List<Country> getAllCountrys();

	boolean loadCountrys() throws JsonProcessingException;

	List<TaxClassificationDefDTO> getTaxClassifications(String countryCode);

	List<OrgInActiveStatusCodesDTO> getOrgInActiveStatusCodes();

	List<WorkerInActiveStatusCodesDTO> getWorkerInActiveStatusCodes();

    List<OrgAddressTypeDTO> getAllOrgAddressTypes();

    List<DocumentDefDTO> getDocDef(EnumDocRelatedEntity docRelatatedEntity, EnumDefaultDocStatus status, Boolean autoAssigned);

    boolean isDomainMapped(String organizationID, String domain);

	List<RefDepartmentDTO> retrieveAllDepartments(Boolean billable);

	String uploadTenantLogo(MultipartFile multipartFile);
	byte[] getTenantLogo();

}
