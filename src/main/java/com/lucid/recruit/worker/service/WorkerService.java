
package com.lucid.recruit.worker.service;

import java.time.LocalDate;
import java.util.List;

import com.lucid.recruit.worker.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.lucid.recruit.worker.entity.WorkerDocument;
import com.lucid.recruit.worker.entity.WorkerStatusCode;

public interface WorkerService {

	WorkerDTO createWorker(WorkerDTO worker);

	WorkerDTO retriveWorkerById(String workerId,boolean history);

	WorkerDTO updateWorker(String workerID, WorkerDTO worker);

	Page<WorkerSummaryDTO> retrieveAllWorkers(Integer offSet, Integer limit, String organizationID, String workerType,
			WorkerStatusCode workerStatus, String emailId, String givenName, String familyName, LocalDate effectiveDate,
			Boolean internalInd, Boolean benchInd);

	WorkerDocumentDTO uploadWorkerDocument(String workerID, WorkerDocumentDTO workerDocumentDTO);

	String uploadWorkerDocFile(String workerdocID, MultipartFile file);

	String deleteDocument(String workerdocID);

	WorkerDocumentDTO getWorkerDocument(String workerdocID);

	byte[] retrieveWorkerDocFile(WorkerDocument workerDocument);

	WorkerDocument retrieveWorkerDocument(String workerdocID);

	public WorkerCountsDTO getWorkerCounts(Boolean status, Boolean workerType, Boolean internal,
			LocalDate effectiveDate);

	WorkerDTO updateWorkerStatus(String workerId, WorkerStatusDTO workerStatusDTO);
}
