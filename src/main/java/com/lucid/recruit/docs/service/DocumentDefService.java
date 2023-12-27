package com.lucid.recruit.docs.service;

import com.lucid.recruit.docs.constants.EnumDefaultDocStatus;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.dto.DefaultDocAssignmentsDTO;
import com.lucid.recruit.docs.dto.DocumentDefDTO;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DocumentDefService {
	DocumentDefDTO createDocumentDef(DocumentDefDTO docDefDTO);

	DefaultDocAssignmentsDTO assignDefaultDocuments(DefaultDocAssignmentsDTO docAssignmentDTO);

	void disAssociateDocDef(String docDefId,EnumDocRelatedEntity relatedEntity);

	DocumentDefDTO getDocumentDef(String documentDefID);

	DocumentDefDTO updateDocumentDef(String documentDefID,DocumentDefDTO docDefDTO);

	Page<DocumentDefDTO> getAllDocDef(Integer offset, Integer limit, String name, EnumDocStatus status, EnumDocRelatedEntity relatedEnity);

    DefaultDocAssignmentsDTO getAssignedDefaultDocuments(String documentAssignDefID);

	Page<DefaultDocAssignmentsDTO> getAllDefaultAssignedDocs(Integer offset, Integer limit, String name, EnumDocStatus status, EnumDocRelatedEntity relatedEnity);

	DefaultDocAssignmentsDTO updateDefaultAssignDoc(String documentAssignDefID, DefaultDocAssignmentsDTO assignmentsDTO);

	List<DefaultDocAssignments> getDefaultDocAssignments(EnumDocRelatedEntity docRelatatedEntity, EnumDefaultDocStatus status, Boolean autoAssigned);

	List<DocumentDefDTO> getUnAssignedDocDef(EnumDocRelatedEntity relatedEntity);

	void deleteDocDef(String docDefId);
}
