package com.lucid.recruit.docs.validations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.recruit.docs.constants.EnumDocDefStatus;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;
import com.lucid.recruit.docs.repo.DefaultDocAssignmentsRepo;
import com.lucid.recruit.worker.entity.WorkerTypeCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.dto.BaseDocAttributesDTO;
import com.lucid.recruit.docs.dto.DefaultDocAssignmentsDTO;
import com.lucid.recruit.docs.dto.DocAttributeDefDTO;
import com.lucid.recruit.docs.entity.DocAttributeDef;
import com.lucid.recruit.docs.entity.DocumentDef;
import com.lucid.recruit.docs.repo.DocAttributeDefRepo;
import com.lucid.recruit.docs.repo.DocumentDefRepo;
import com.lucid.recruit.referencedata.entity.WorkerType;
import com.lucid.recruit.referencedata.repo.WorkerTypeRepo;

@Component
public class DocumentDefValidatior {

	private static final Logger log = LoggerFactory.getLogger(DocumentDefValidatior.class);

	@Autowired
	private WorkerTypeRepo workerTypeRepo;
	@Autowired
	private DocumentDefRepo docDefRepo;
	@Autowired
	private DocAttributeDefRepo docAttrDefRepo;

	@Autowired
	private DefaultDocAssignmentsRepo defaultDocAssignmentsRepo;

	public void validateOrder(List<DocAttributeDefDTO> docAttrDef) {
		int temp[] = new int[docAttrDef.size()];
		for (int i = 0; i < docAttrDef.size(); i++) {
			temp[i] = docAttrDef.get(i).getDisplayOrder();
		}
		Arrays.sort(temp);
		if (temp[docAttrDef.size() - 1] > docAttrDef.size()) {
			log.error(
					"Invalid Attribute Def Display Order : Display Order maximum number cannot exceed size of attributes");
			throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0001, "docAttrDef", "docAttrDef",
					String.valueOf(temp[docAttrDef.size() - 1]),
					"Invalid Attribute Def Display Order : Display Order maximum number cannot exceed size of attributes");
		}

		if (temp[0] != 1) {
			log.error("Invalid Attribute Def Display Order : Display Order starting number should be 1");
			throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0002, "docAttrDef", "docAttrDef",
					String.valueOf(temp[0]),
					"Invalid Attribute Def Display Order : Display Order starting number should be 1");
		}

		for (int i = 0; i < temp.length; i++) {
			if (i == 0)
				continue;
			if (temp[i] - temp[i - 1] != 1) {
				log.error("Invalid Attribute Def Display Order : Display Order numbers have missing or duplicate");
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0003, "docAttrDef", "docAttrDef",
						String.valueOf(temp[i]),
						"Invalid Attribute Def Display Order : Display Order numbers have missing or duplicate");

			}
		}

	}

	public void validateRelatedType(DefaultDocAssignmentsDTO docAssignmentDTO) {
		if (docAssignmentDTO.getRelatedEnity().equals(EnumDocRelatedEntity.WORKER)) {
			WorkerTypeCode workerTypeCode = null;
			try{
				workerTypeCode= WorkerTypeCode.valueOf(docAssignmentDTO.getRelatedSubEnity());
			} catch (Exception e){
				log.error("Worker Type ID " + docAssignmentDTO.getRelatedSubEnity() + " not found");
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0014, "relatedSubEnity", "relatedSubEnity",
						docAssignmentDTO.getRelatedSubEnity(),
						"Worker Type ID " + docAssignmentDTO.getRelatedSubEnity() + " not found");
			}
			Optional<WorkerType> findById = workerTypeRepo.findById(workerTypeCode);
			if (findById.isEmpty()) {
				log.error("Related Sub Entity Worker Type ID " + docAssignmentDTO.getRelatedSubEnity() + " not found");
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0004, "relatedSubEnity", "relatedSubEnity",
						docAssignmentDTO.getRelatedSubEnity(),
						"Worker Type ID " + docAssignmentDTO.getRelatedSubEnity() + " not found");
			}
		} else if (docAssignmentDTO.getRelatedEnity().equals(EnumDocRelatedEntity.CONTRACT)) {
			log.error("Related Sub Entity shall be null for related entity Contract");
			throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0005, "relatedSubEnity", "relatedSubEnity",
					docAssignmentDTO.getRelatedSubEnity(),
					"elated Sub Entity shall be null for related entity Contract");

		}
	}

	public DocumentDef getDocumentDef(String documentDefId) {
		Optional<DocumentDef> documentDef = docDefRepo.findByDocumentDefIDAndStatus(documentDefId,
				EnumDocDefStatus.ACTIVE);
		if (documentDef.isEmpty()) {
			log.error("Document Definition with ID " + documentDefId + " with active status not found");
			throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0006, "documentDefID", "documentDefID", documentDefId,
					"Document Definition with ID " + documentDefId + " with active status not found");
		}
		return documentDef.get();
	}

	public DocumentDef getDocumentDefById(String documentDefId) {
		Optional<DocumentDef> documentDef = docDefRepo.findById(documentDefId);
		if (documentDef.isEmpty()) {
			log.error("Document Definition with ID " + documentDefId + " not found");
			throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"Document Definition with ID " + documentDefId + " not found");
		}
		return documentDef.get();
	}

	public void validateDocIssueAndExpiryDates(DocumentDef documentDef, LocalDate issueDate, LocalDate expiryDate) {
		if (documentDef.isExpiryInd()) {
			if (Objects.isNull(issueDate)) {
				log.error("Issue date is mandatory for " + documentDef.getDocumentName() + " with ID "
						+ documentDef.getDocumentDefID());
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0012, "docAttributes", "docAttributes",
						documentDef.getDocumentDefID(), "Issue date is mandatory for " + documentDef.getDocumentName()
						+ " with ID " + documentDef.getDocumentDefID());
			}
			if (Objects.isNull(expiryDate)) {
				log.error("Expiry date is mandatory for " + documentDef.getDocumentName() + " with ID "
						+ documentDef.getDocumentDefID());
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0013, "docAttributes", "docAttributes",
						documentDef.getDocumentDefID(), "Expiry date is mandatory for "
						+ documentDef.getDocumentName() + " with ID " + documentDef.getDocumentDefID());
			}
		}
	}

	public List<DocAttributeDef> validateDocRequiredAttributes(DocumentDef documentDef,
															   List<BaseDocAttributesDTO> docAttributes) {
		List<DocAttributeDef> docDefAttributes = docAttrDefRepo.findByDocumentDef(documentDef);
		if (docDefAttributes == null || docDefAttributes.isEmpty()) {
			// No Attributes configured return
			return null;
		}
		if (docAttributes == null || docAttributes.isEmpty()) {
			log.error("Mandatory attributes not passed for document " + documentDef.getDocumentName() + " with ID "
					+ documentDef.getDocumentDefID());
			throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0007, "docAttributes", "docAttributes",
					documentDef.getDocumentDefID(), "Mandatory attributes not passed for document "
					+ documentDef.getDocumentName() + " with ID " + documentDef.getDocumentDefID());
		}
		// Check all mandatory attributes passed
		for (Iterator<DocAttributeDef> iterator = docDefAttributes.iterator(); iterator.hasNext();) {
			DocAttributeDef docAttribute = iterator.next();
			if (docAttribute.isRequired()) {
				boolean matched = false;
				for (Iterator<BaseDocAttributesDTO> iterator2 = docAttributes.iterator(); iterator2.hasNext();) {
					BaseDocAttributesDTO inputAttribute = iterator2.next();
					if (docAttribute.getDocumentAttrDefID()
							.equals(inputAttribute.getDocAttributeDef().getDocumentAttrDefID())) {
						matched = true;
						if (inputAttribute.getDocAttrValue() == null || inputAttribute.getDocAttrValue().isEmpty()) {
							log.error("Document mandatory attribute " + docAttribute.getDocumentAttrName()
									+ " attribute id " + docAttribute.getDocumentAttrDefID() + " of document "
									+ documentDef.getDocumentName() + " document id " + documentDef.getDocumentDefID()
									+ " is null");
							throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0008, "docAttributes",
									"workerDocAttributes", documentDef.getDocumentDefID(),
									"Document mandatory attribute " + docAttribute.getDocumentAttrName()
											+ " attribute id " + docAttribute.getDocumentAttrDefID() + " of document "
											+ documentDef.getDocumentName() + " document id "
											+ documentDef.getDocumentDefID() + " is null");
						}

					}
				}
				if (!matched) {
					// Attribute itself not passed
					log.error("Document mandatory attribute " + docAttribute.getDocumentAttrName() + " attribute id "
							+ docAttribute.getDocumentAttrDefID() + " of document " + documentDef.getDocumentName()
							+ " document id " + documentDef.getDocumentDefID() + " is null");
					throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0009, "docAttributes", "docAttributes",
							documentDef.getDocumentDefID(),
							"Document mandatory attribute " + docAttribute.getDocumentAttrName() + " attribute id "
									+ docAttribute.getDocumentAttrDefID() + " of document "
									+ documentDef.getDocumentName() + " document id " + documentDef.getDocumentDefID()
									+ " is null");
				}
			}

		}
		return docDefAttributes;

	}

	public void validateDocAttributes(DocumentDef documentDef, List<DocAttributeDef> docDefAttributes,
									  List<BaseDocAttributesDTO> docAttributes) {
		if (docDefAttributes == null || docDefAttributes.isEmpty()) {
			return;
		}
		for (Iterator<BaseDocAttributesDTO> iterator = docAttributes.iterator(); iterator.hasNext();) {
			BaseDocAttributesDTO docAttributeDTO = (BaseDocAttributesDTO) iterator.next();
			boolean matched = false;
			DocAttributeDef docAttrDef = null;
			for (Iterator<DocAttributeDef> iterator2 = docDefAttributes.iterator(); iterator2.hasNext();) {
				docAttrDef = iterator2.next();
				if (docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID()
						.equals(docAttrDef.getDocumentAttrDefID())) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				// Attribute definition not exist
				log.error("Attribute defintion id " + docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID()
						+ " does not exist for document " + documentDef.getDocumentDefID());
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0010, "docAttributes", "docAttributes",
						documentDef.getDocumentDefID(),
						"Attribute defintion id " + docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID()
								+ " does not exist for document " + documentDef.getDocumentDefID());
			}

			switch (docAttrDef.getAttrType()) {
				case DATE: {
					if (!validateDate(docAttributeDTO.getDocAttrValue())) {
						log.error("Invalid attribute date value " + docAttributeDTO.getDocAttrValue() + " for document def "
								+ documentDef.getDocumentDefID() + " document attribute id "
								+ docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID());
						throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0011, "docAttributes", "docAttributes",
								documentDef.getDocumentDefID(),
								"Invalid attribute date value " + docAttributeDTO.getDocAttrValue() + " for document def "
										+ documentDef.getDocumentDefID() + " document attribute id "
										+ docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID());
					}
					break;
				}
				case NUMBER: {
					if (!StringUtils.isNumeric(docAttributeDTO.getDocAttrValue())) {
						log.error("Invalid attribute number value " + docAttributeDTO.getDocAttrValue()
								+ " for document def " + documentDef.getDocumentDefID() + " document attribute id "
								+ docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID());
						throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0011, "docAttributes", "docAttributes",
								documentDef.getDocumentDefID(),
								"Invalid attribute number value " + docAttributeDTO.getDocAttrValue() + " for document def "
										+ documentDef.getDocumentDefID() + " document attribute id "
										+ docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID());
					}
					break;
				}
				case STRING: {
					if (StringUtils.isAllBlank(docAttributeDTO.getDocAttrValue())) {
						log.error("Invalid attribute string value " + docAttributeDTO.getDocAttrValue()
								+ " for document def " + documentDef.getDocumentDefID() + " document attribute id "
								+ docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID());
						throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0011, "docAttributes", "docAttributes",
								documentDef.getDocumentDefID(),
								"Invalid attribute string value " + docAttributeDTO.getDocAttrValue() + " for document def "
										+ documentDef.getDocumentDefID() + " document attribute id "
										+ docAttributeDTO.getDocAttributeDef().getDocumentAttrDefID());
					}
					break;
				}

			}
		}
	}

	public boolean validateDate(String dateStr) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		try {
			LocalDate.parse(dateStr, dateFormatter);
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;

	}

    public DefaultDocAssignments getAssignedDefaultDocument(String documentAssignDefID) {
       return defaultDocAssignmentsRepo.findById(documentAssignDefID).orElseThrow(() -> new EntityNotFoundException("defaultDocAssigment by id:"+documentAssignDefID+" not found"));
    }

	public void validateDocumentName(String documentDefID, String documentName) {
		DocumentDef documentDef = docDefRepo.findByDocumentName(documentName);
		if(documentDef != null){
			if(documentDefID == null || !documentDefID.equals(documentDef.getDocumentDefID())){
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0015,"docDef","documentName",documentName,"DocumentDef by name:"+documentName+" already exists");
			}
		}
	}

}
