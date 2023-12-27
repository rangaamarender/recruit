package com.lucid.recruit.referencedata.controller;

import java.util.List;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.recruit.docs.constants.EnumDefaultDocStatus;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.org.entity.OrganizationDocument;
import com.lucid.recruit.person.entity.RelationshipCode;
import com.lucid.recruit.referencedata.dto.RefDepartmentDTO;
import com.lucid.recruit.worker.entity.WorkerTypeCode;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lucid.recruit.org.dto.OrganizationDTO;
import com.lucid.recruit.referencedata.dto.JobDTO;
import com.lucid.recruit.referencedata.dto.WorkerTypeDTO;
import com.lucid.recruit.referencedata.entity.Country;
import com.lucid.recruit.referencedata.entity.Job;
import com.lucid.recruit.referencedata.service.RefernceDataService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "*")
@RequestMapping("/api/raves/reference/")
@Tag(name = "ReferenceData", description = "ReferenceData API")
@RestController
public class ReferenceDataController {

	@Autowired
	private RefernceDataService refDataService;

	@GetMapping("v1/workerType")
	public ResponseEntity<Object> retrieveAllWorkerTypes() {
		return ResponseEntity.ok(refDataService.retrieveAllWorkerTypes());
	}

	@PatchMapping("v1/workerType/{workerTypeCode}")
	public ResponseEntity<Object> updateWorkerType(@PathVariable(name = "workerTypeCode") @NotNull WorkerTypeCode workerTypeCode, @RequestBody WorkerTypeDTO workerTypeDTO) {
		return ResponseEntity.ok(refDataService.updateWorkerType(workerTypeCode, workerTypeDTO));
	}

	@PostMapping("v1/department")
	public ResponseEntity<Object> createDepartment(@RequestBody @Valid RefDepartmentDTO departmentDTO) {
		return ResponseEntity.ok(refDataService.createDepartment(departmentDTO));
	}

	@GetMapping("v1/department/{id}")
	public ResponseEntity<Object> retrieveDepartment(@PathVariable(name = "id") String deptID) {
		return ResponseEntity.ok(refDataService.retrieveDepartment(deptID));
	}

	@GetMapping("v1/department")
	public ResponseEntity<Object> retrieveAllDepartments(@RequestParam(name = "billable",required = false) Boolean billable) {
		return ResponseEntity.ok(refDataService.retrieveAllDepartments(billable));
	}

	@PatchMapping("v1/department/{id}")
	public ResponseEntity<Object> updateDepartment(@PathVariable(name = "id") @NotBlank String deptID, @RequestBody RefDepartmentDTO departmentDTO) {
		return ResponseEntity.ok(refDataService.updateDepartment(deptID, departmentDTO));
	}

	@GetMapping("v1/currencycodes")
	public ResponseEntity<Object> getAllCurrencyCodes() {
		return ResponseEntity.ok(refDataService.getAllCurrencyCodes());
	}

	@GetMapping("v1/country/load")
	public ResponseEntity<Object> loadCountrys() throws JsonProcessingException {
		return ResponseEntity.ok(refDataService.loadCountrys());
	}

	@PostMapping("v1/country")
	public ResponseEntity<Object> createCountry(@RequestBody @Valid Country refCountry) {
		return ResponseEntity.ok(refDataService.createRefCountry(refCountry));
	}

	@GetMapping("v1/country")
	public ResponseEntity<Object> getAllCountrys() {
		return ResponseEntity.ok(refDataService.getAllCountrys());
	}


	@GetMapping("v1/taxclassification")
	public ResponseEntity<Object> getTaxIdByCountry(@RequestParam(name = "countryCode", required = false) @Nullable String countryCode) {
		return ResponseEntity.ok(refDataService.getTaxClassifications(countryCode));
	}

	@GetMapping("v1/organization/inactivestatuscodes")
	public ResponseEntity<Object> getOrgStatusDef() {
		return ResponseEntity.ok(refDataService.getOrgInActiveStatusCodes());
	}

	@GetMapping("v1/worker/inactivestatuscodes")
	public ResponseEntity<Object> getWorkerStatusDef() {
		return ResponseEntity.ok(refDataService.getWorkerInActiveStatusCodes());
	}

	@GetMapping("v1/relationships")
	public ResponseEntity<Object> getAllRelationShips() {
		return ResponseEntity.ok(RelationshipCode.fetchAllRelationShipCodes());
	}

	@GetMapping("v2/organization/addresstype")
	public ResponseEntity<Object> getAllOrgAddressTypes() {
		return ResponseEntity.ok(refDataService.getAllOrgAddressTypes());
	}

	@GetMapping("v1/docdef")
	public ResponseEntity<?> getDocDef(@RequestParam(required = true, name = "relatedEntity") EnumDocRelatedEntity relatedEntity, @RequestParam(required = false) EnumDefaultDocStatus status, @RequestParam(required = false) Boolean autoAssigned) {
		return ResponseEntity.ok(refDataService.getDocDef(relatedEntity, status, autoAssigned));
	}

	@GetMapping("v1/organization/domainmapped")
	public ResponseEntity<?> isDomainMapped(@RequestParam(name = "organizationID",required = false) String organizationID,@RequestParam(name = "domain",required = true)@NotNull String domain){
		return ResponseEntity.ok(refDataService.isDomainMapped(organizationID,domain));
	}

	@PostMapping(path = "v1/tenant/logo", consumes = {
			"multipart/form-data" }, produces = "application/json")
	public ResponseEntity<Object> uploadTenantLogo(@RequestParam("tenantLogo") MultipartFile tenantLogo) {
		return ResponseEntity.ok(refDataService.uploadTenantLogo(tenantLogo));
	}
	@GetMapping(path = "v1/tenant/logo", produces = { "multipart/form-data" })
	public ResponseEntity<byte[]> getOrgDocumentFile() {
		return ResponseEntity.ok(refDataService.getTenantLogo());
	}

}