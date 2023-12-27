package com.lucid.recruit.docs.controller;

import com.lucid.recruit.docs.dto.RelatedDocumentDefDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.dto.DefaultDocAssignmentsDTO;
import com.lucid.recruit.docs.dto.DocumentDefDTO;
import com.lucid.recruit.docs.service.DocumentDefService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Tag(name = "DocumentDef", description = "Document Definition Management API")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/raves/")
public class DocDefController {
	private static final Logger log = LoggerFactory.getLogger(DocDefController.class);

	@Autowired
	private DocumentDefService docDefService;

	@PostMapping("v1/worker/docdef")
	public ResponseEntity<Object> createDocDefForWorker(@RequestBody @Valid DocumentDefDTO docDefDTO) {
		log.info("Create Document Definition");
		DocumentDefDTO documentDefDTO = docDefService.createDocumentDef(docDefDTO);
		DefaultDocAssignmentsDTO defaultDocAssignmentsDTO = new DefaultDocAssignmentsDTO();
		RelatedDocumentDefDTO relatedDocumentDefDTO = new RelatedDocumentDefDTO();
		relatedDocumentDefDTO.setDocumentDefID(documentDefDTO.getDocumentDefID());
		defaultDocAssignmentsDTO.setDocumentDef(relatedDocumentDefDTO);
		defaultDocAssignmentsDTO.setRelatedEnity(EnumDocRelatedEntity.WORKER);
		if(docDefDTO.getAutoAssigned() != null) {
			defaultDocAssignmentsDTO.setAutoAssigned(docDefDTO.getAutoAssigned());
		}
		else{
			defaultDocAssignmentsDTO.setAutoAssigned(false);
		}
		docDefService.assignDefaultDocuments(defaultDocAssignmentsDTO);
		return ResponseEntity.ok(documentDefDTO);
	}

	@PostMapping("v1/organization/docdef")
	public ResponseEntity<Object> createDocDefForOrganization(@RequestBody @Valid DocumentDefDTO docDefDTO) {
		log.info("Create Document Definition");
		DocumentDefDTO documentDefDTO = docDefService.createDocumentDef(docDefDTO);
		DefaultDocAssignmentsDTO defaultDocAssignmentsDTO = new DefaultDocAssignmentsDTO();
		RelatedDocumentDefDTO relatedDocumentDefDTO = new RelatedDocumentDefDTO();
		relatedDocumentDefDTO.setDocumentDefID(documentDefDTO.getDocumentDefID());
		defaultDocAssignmentsDTO.setDocumentDef(relatedDocumentDefDTO);
		defaultDocAssignmentsDTO.setRelatedEnity(EnumDocRelatedEntity.ORGANIZATION);
		if(docDefDTO.getAutoAssigned() != null) {
			defaultDocAssignmentsDTO.setAutoAssigned(docDefDTO.getAutoAssigned());
		}
		else{
			defaultDocAssignmentsDTO.setAutoAssigned(false);
		}
		docDefService.assignDefaultDocuments(defaultDocAssignmentsDTO);
		return ResponseEntity.ok(documentDefDTO);
	}

	@PostMapping("v1/contract/docdef")
	public ResponseEntity<Object> createDocDefForContract(@RequestBody @Valid DocumentDefDTO docDefDTO) {
		log.info("Create Document Definition");
		DocumentDefDTO documentDefDTO = docDefService.createDocumentDef(docDefDTO);
		DefaultDocAssignmentsDTO defaultDocAssignmentsDTO = new DefaultDocAssignmentsDTO();
		RelatedDocumentDefDTO relatedDocumentDefDTO = new RelatedDocumentDefDTO();
		relatedDocumentDefDTO.setDocumentDefID(documentDefDTO.getDocumentDefID());
		defaultDocAssignmentsDTO.setDocumentDef(relatedDocumentDefDTO);
		defaultDocAssignmentsDTO.setRelatedEnity(EnumDocRelatedEntity.CONTRACT);
		if(docDefDTO.getAutoAssigned() != null) {
			defaultDocAssignmentsDTO.setAutoAssigned(docDefDTO.getAutoAssigned());
		}
		else{
			defaultDocAssignmentsDTO.setAutoAssigned(false);
		}
		docDefService.assignDefaultDocuments(defaultDocAssignmentsDTO);
		return ResponseEntity.ok(documentDefDTO);
	}

	@PostMapping("v1/timesheet/docdef")
	public ResponseEntity<Object> createDocDefForTimesheet(@RequestBody @Valid DocumentDefDTO docDefDTO) {
		log.info("Create Document Definition");
		DocumentDefDTO documentDefDTO = docDefService.createDocumentDef(docDefDTO);
		DefaultDocAssignmentsDTO defaultDocAssignmentsDTO = new DefaultDocAssignmentsDTO();
		RelatedDocumentDefDTO relatedDocumentDefDTO = new RelatedDocumentDefDTO();
		relatedDocumentDefDTO.setDocumentDefID(documentDefDTO.getDocumentDefID());
		defaultDocAssignmentsDTO.setDocumentDef(relatedDocumentDefDTO);
		defaultDocAssignmentsDTO.setRelatedEnity(EnumDocRelatedEntity.TIMESHEET);
		if(docDefDTO.getAutoAssigned() != null) {
			defaultDocAssignmentsDTO.setAutoAssigned(docDefDTO.getAutoAssigned());
		}
		else{
			defaultDocAssignmentsDTO.setAutoAssigned(false);
		}
		docDefService.assignDefaultDocuments(defaultDocAssignmentsDTO);
		return ResponseEntity.ok(documentDefDTO);
	}

	@GetMapping("v1/docdef/{documentDefID}")
	public ResponseEntity<Object> getDocumentDef(@PathVariable(name = "documentDefID") String documentDefID) {
		return ResponseEntity.ok(docDefService.getDocumentDef(documentDefID));
	}

	@GetMapping("v1/docdef")
	public ResponseEntity<Object> getAllDocDef(@RequestParam(required = false) Integer offset,
											   @RequestParam(required = false) Integer limit,
											   @RequestParam(required = false) String name,
											   @RequestParam(required = false) EnumDocStatus status,
											   @RequestParam(required = false) EnumDocRelatedEntity relatedEntity){
		return ResponseEntity.ok(docDefService.getAllDocDef(offset,limit,name,status,relatedEntity));
	}

	@PatchMapping("v1/docdef/{documentDefID}")
	public ResponseEntity<Object> updateDocDef(@PathVariable(name = "documentDefID") @NotBlank String documentDefID, @RequestBody DocumentDefDTO docDefDTO){
		log.info("Update Document Definition");
		return ResponseEntity.ok(docDefService.updateDocumentDef(documentDefID,docDefDTO));
	}

	@PostMapping("v1/assigndocdef")
	public ResponseEntity<Object> assignDefaultDocs(@RequestBody @Valid DefaultDocAssignmentsDTO defDocAssignmentDTO) {
		log.info("Assign Default Document Defintion");
		return ResponseEntity.ok(docDefService.assignDefaultDocuments(defDocAssignmentDTO));
	}

	@GetMapping("v1/assigndocdef/{documentAssignDefID}")
	public ResponseEntity<Object> assignDefaultDocs(@PathVariable(name = "documentAssignDefID") String documentAssignDefID) {
		log.info("Get Default Document Defintion");
		return ResponseEntity.ok(docDefService.getAssignedDefaultDocuments(documentAssignDefID));
	}

	@GetMapping("v1/assigndocdef")
	public ResponseEntity<Object> getAllDefaultAssignDocs(@RequestParam(required = false) Integer offset,
											   @RequestParam(required = false) Integer limit,
											   @RequestParam(required = false) String name,
											   @RequestParam(required = false) EnumDocStatus status,
											   @RequestParam(required = false) EnumDocRelatedEntity relatedEntity){
		return ResponseEntity.ok(docDefService.getAllDefaultAssignedDocs(offset,limit,name,status,relatedEntity));
	}

	@PatchMapping("v1/assigndocdef/{documentAssignDefID}")
	public ResponseEntity<Object> updateDefaultAssignDoc(@PathVariable(name = "documentAssignDefID")@NotBlank String documentAssignDefID,@RequestBody DefaultDocAssignmentsDTO defaultDocAssignmentsDTO){
		return ResponseEntity.ok(docDefService.updateDefaultAssignDoc(documentAssignDefID,defaultDocAssignmentsDTO));
	}

	@GetMapping("v1/getunassigneddocdef")
	public ResponseEntity<?> getUnAssignedDocDef(@RequestParam(name = "relatedEntity") EnumDocRelatedEntity relatedEntity){
		return ResponseEntity.ok(docDefService.getUnAssignedDocDef(relatedEntity));
	}

	@DeleteMapping("v1/disAssociateDocDef")
	public ResponseEntity<?> disAssociateDocDef(@RequestParam(name = "docDefId") String docDefId,@RequestParam(name = "relatedEntity")EnumDocRelatedEntity relatedEntity){
		docDefService.disAssociateDocDef(docDefId,relatedEntity);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@DeleteMapping("v1/docdef/{docDefId}")
	public ResponseEntity<?> deleteDocDef(@PathVariable(name = "docDefId") String docDefId){
		docDefService.deleteDocDef(docDefId);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

}
