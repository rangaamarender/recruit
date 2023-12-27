package com.lucid.recruit.worker.controller;



import java.time.LocalDate;

import com.lucid.recruit.worker.dto.WorkerStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.lucid.recruit.worker.dto.WorkerDTO;
import com.lucid.recruit.worker.dto.WorkerDocumentDTO;
import com.lucid.recruit.worker.entity.WorkerDocument;
import com.lucid.recruit.worker.entity.WorkerStatusCode;
import com.lucid.recruit.worker.service.WorkerService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Tag(name = "Worker", description = "Worker Management API")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/raves/")
public class WorkerController {
	private static final Logger log = LoggerFactory.getLogger(WorkerController.class);

	@Autowired
	private WorkerService workerService;

	@PostMapping(path = "v1/worker")
	public ResponseEntity<Object> createWorker(@RequestBody @Valid WorkerDTO workerDTO) {
		log.info("Create Worker Request Received");
		return ResponseEntity.ok(workerService.createWorker(workerDTO));
	}

	@GetMapping(path = "v1/worker/{workerId}")
	public WorkerDTO retriveWorker(@PathVariable String workerId,@RequestParam(required = false,name = "history") boolean history) {
		return workerService.retriveWorkerById(workerId,history);
	}

	@PatchMapping(path = "v1/worker/{workerID}")
	public ResponseEntity<Object> updateWorker(@PathVariable @NotNull String workerID,
			@RequestBody WorkerDTO workerDTO) {
		log.info("Update worker Request Received");
		return ResponseEntity.ok(workerService.updateWorker(workerID, workerDTO));
	}

	@PostMapping(path = "v1/worker/{workerID}/document", consumes = {
			"application/json" }, produces = "application/json")
	public ResponseEntity<Object> createWokerDocument(@PathVariable @NotNull String workerID,
			@RequestBody @Valid WorkerDocumentDTO workerDocumentDTO) {
		return ResponseEntity.ok(workerService.uploadWorkerDocument(workerID, workerDocumentDTO));
	}

	@PostMapping(path = "v1/worker/{workerdocID}/documentfile", consumes = {
			"multipart/form-data" }, produces = "application/json")
	public ResponseEntity<Object> uploadWokerDocumentFile(@PathVariable @NotNull String workerdocID,
			@RequestParam("workerfile") MultipartFile file) {
		return ResponseEntity.ok(workerService.uploadWorkerDocFile(workerdocID, file));
	}

	@GetMapping(path = "v1/worker/documentfile/{workerdocID}", produces = { "multipart/form-data" })
	public ResponseEntity<byte[]> getWokerDocumentFile(@PathVariable @NotNull String workerdocID) {
		WorkerDocument workerDocument = workerService.retrieveWorkerDocument(workerdocID);
		byte[] fileData = workerService.retrieveWorkerDocFile(workerDocument);
		if (fileData != null) {
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + workerDocument.getFileName() + "\"").body(fileData);

		} else {
			throw new EntityNotFoundException("Worker document file by Id " + workerdocID + " file not found");
		}
	}

	@DeleteMapping(path = "v1/worker/workerdocument/{workerdocID}")
	public ResponseEntity<Object> deleteDocument(@PathVariable(name = "workerdocID") @NotBlank String workerdocID) {
		return ResponseEntity.ok(workerService.deleteDocument(workerdocID));
	}

	@GetMapping(path = "v1/worker/workerdocument/{workerdocID}")
	public ResponseEntity<Object> getWorkerDoc(@PathVariable(name = "workerdocID") @NotBlank String workerdocID) {
		return ResponseEntity.ok(workerService.getWorkerDocument(workerdocID));
	}

	@GetMapping(path = "v1/worker")
	public ResponseEntity<Object> retrieveAllWorkers(@RequestParam(required = false, defaultValue = "0") Integer offset,
			@RequestParam(required = false, defaultValue = "10") Integer limit,
			@RequestParam(required = false) String organizationID, @RequestParam(required = false) String workerType,
			@RequestParam(required = false) WorkerStatusCode workerStatusCode,
			@RequestParam(required = false) String emailId, @RequestParam(required = false) String givenName,
			@RequestParam(required = false) String familyName, @RequestParam(required = false) LocalDate effectiveDate,
			@RequestParam(required = false) Boolean internalInd, @RequestParam(required = false) Boolean benchInd) {

/*		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String principalName = authentication.getName();

		System.out.println("***** " + authentication.getPrincipal());
		org.springframework.security.oauth2.jwt.Jwt jwt = (Jwt) authentication.getPrincipal();
		Map<String, Jwt> singletonMap = Collections.singletonMap("principal", jwt);
		Map<String, Object> claims = jwt.getClaims();
		for (Map.Entry<String, Object> entry : claims.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
*/
		return ResponseEntity.ok(workerService.retrieveAllWorkers(offset, limit, organizationID, workerType,
				workerStatusCode, emailId, givenName, familyName, effectiveDate, internalInd, benchInd));
	}

	@GetMapping(path = "v1/worker/counts")
	public ResponseEntity<Object> getWorkerCounts(@RequestParam(required = false) Boolean status,
			@RequestParam(required = false) Boolean workerType, @RequestParam(required = false) Boolean billable,
			@RequestParam(required = false) LocalDate effectiveDate) {
		return ResponseEntity.ok(workerService.getWorkerCounts(status, workerType, billable, effectiveDate));
	}

	@PostMapping("v1/worker/status/{workerId}")
	public ResponseEntity<Object> updateStatus(@PathVariable(name = "workerId")String workerID, @RequestBody @Valid WorkerStatusDTO newWorkerStatus){
		return ResponseEntity.ok(workerService.updateWorkerStatus(workerID,newWorkerStatus));
	}

}
