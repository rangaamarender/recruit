package com.lucid.recruit.org.controller;

import com.lucid.recruit.org.constants.OrgStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.recruit.org.dto.OrganizationDTO;
import com.lucid.recruit.org.dto.OrganizationDocumentDTO;
import com.lucid.recruit.org.dto.OrganizationStatusDTO;
import com.lucid.recruit.org.entity.OrganizationDocument;
import com.lucid.recruit.org.service.OrganizationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/raves")
public class OrganizationController {
	private final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

	@Autowired
	private OrganizationService organizationService;

	@PostMapping("v1/organization")
	public ResponseEntity<Object> createOrganization(@RequestBody @Valid OrganizationDTO organizationDTO) {
		LOGGER.debug("Create Organization: {} ", organizationDTO);
		LOGGER.info("Create organization ");
		OrganizationDTO response = organizationService.createOrganization(organizationDTO);
		LOGGER.debug("Organization created successfully:{}", response);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("v1/organization/{organizationID}")
	public ResponseEntity<Object> updateOrganization(
			@PathVariable(name = "organizationID") @NotNull String organizationID,
			@RequestBody OrganizationDTO organizationDTO) {
		OrganizationDTO response = organizationService.updateOrganization(organizationID, organizationDTO);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("v1/organization/{organizationID}")
	public ResponseEntity<Object> getOrganization(
			@PathVariable(name = "organizationID") @NotNull String organizationID,@RequestParam(required = false) boolean history) {
		return ResponseEntity.ok(organizationService.getOrganization(organizationID,history));
	}

	@GetMapping("v1/organization")
	public ResponseEntity<Object> retrieveAllOrganizations(
			@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "organizationID", required = false) String organizationID,
			@RequestParam(value = "emailId", required = false) String emailId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) OrgStatus status,
			@RequestParam(value = "sort",required = false) String sort) {
		return ResponseEntity.ok(organizationService.getAllOrganizations(offset, limit, organizationID, emailId, name,status,sort));
	}

	@PostMapping("v1/organization/status/{organizationID}")
	public ResponseEntity<Object> updateOrganizationStatus(
			@PathVariable(name = "organizationID") @NotNull String organizationID,
			@RequestBody @Valid OrganizationStatusDTO orgStatusDTO) {
		return ResponseEntity.ok(organizationService.updateOrgStatus(organizationID, orgStatusDTO));
	}

	@PostMapping(path = "v1/organization/{organizationID}/document", consumes = {
			"application/json" }, produces = "application/json")
	public ResponseEntity<Object> createOrgDocument(
			@PathVariable(name = "organizationID") @NotNull String organizationID,
			@RequestBody @Valid OrganizationDocumentDTO organizationDocumentDTO) {
		return ResponseEntity.ok(organizationService.uploadOrgDocument(organizationID, organizationDocumentDTO));
	}

	@PostMapping(path = "v1/organization/{organizationDocID}/documentfile", consumes = {
			"multipart/form-data" }, produces = "application/json")
	public ResponseEntity<Object> uploadOrgDocumentFile(
			@PathVariable(name = "organizationDocID") @NotNull String organizationDocID,
			@RequestParam("orgfile") MultipartFile file) {
		return ResponseEntity.ok(organizationService.uploadOrgDocFile(organizationDocID, file));
	}

	@GetMapping(path = "v1/organization/documentfile/{organizationDocID}", produces = { "multipart/form-data" })
	public ResponseEntity<byte[]> getOrgDocumentFile(
			@PathVariable(name = "organizationDocID") @NotNull String organizationDocID) {
		OrganizationDocument organizationDocument = organizationService.retrieveOrgDocument(organizationDocID);
		byte[] fileData = organizationService.retrieveOrgDocFile(organizationDocument);
		if (fileData != null) {
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + organizationDocument.getFileName() + "\"").body(fileData);

		} else {
			throw new EntityNotFoundException(
					"Organization document file by Id " + organizationDocID + " file not found");
		}
	}

	@DeleteMapping(path = "v1/organization/orgdocument/{organizationDocID}")
	public ResponseEntity<Object> deleteDocument(
			@PathVariable(name = "organizationDocID") @NotNull String organizationDocID) {
		return ResponseEntity.ok(organizationService.deleteDocument(organizationDocID));
	}

	@GetMapping(path = "v1/organization/orgdocument/{organizationDocID}")
	public ResponseEntity<Object> getOrgDoc(
			@PathVariable(name = "organizationDocID") @NotNull String organizationDocID) {
		return ResponseEntity.ok(organizationService.getOrgDocument(organizationDocID));
	}

	@GetMapping("v1/organization/count")
	public ResponseEntity<Object> getOrgCount(){
		return ResponseEntity.ok(organizationService.getOrgCounts());
	}

	@PostMapping(path = "v1/organization/{organizationID}/logo", consumes = {
			"multipart/form-data" })
	public ResponseEntity<String> uploadOrgLogo(
			@PathVariable(name = "organizationID") @NotNull String organizationID,
			@RequestParam("logoimage") MultipartFile file) {
		return ResponseEntity.ok(organizationService.uploadOrgLogo(organizationID, file));
	}

	@GetMapping(value = "v1/organization/{orgId}/logo",produces = { "multipart/form-data" })
	public byte[] getOrgLogo(@PathVariable(name = "orgId",required = true) String orgId){
		return organizationService.retrieveOrgLogo(orgId);
	}


}
