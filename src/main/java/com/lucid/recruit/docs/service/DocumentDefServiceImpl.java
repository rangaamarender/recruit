package com.lucid.recruit.docs.service;

import java.util.*;
import java.util.stream.Collectors;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.docs.constants.*;
import com.lucid.recruit.docs.repo.*;
import com.lucid.recruit.org.entity.Organization;
import com.lucid.recruit.worker.entity.WorkerTypeCode;
import com.lucid.util.ServiceUtils;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lucid.recruit.docs.dto.DefaultDocAssignmentsDTO;
import com.lucid.recruit.docs.dto.DocAttributeDefDTO;
import com.lucid.recruit.docs.dto.DocumentDefDTO;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;
import com.lucid.recruit.docs.entity.DocAttributeDef;
import com.lucid.recruit.docs.entity.DocumentDef;
import com.lucid.recruit.docs.validations.DocumentDefValidatior;

@Service
public class DocumentDefServiceImpl implements DocumentDefService {

	@Autowired
	private DocumentDefRepo documentDefRepo;
	@Autowired
	private DocAttributeDefRepo docAttrDefRepo;
	@Autowired
	private DocumentDefValidatior docDefValidator;
	@Autowired
	private DefaultDocAssignmentsRepo defaultDocAssRepo;

	@Autowired
	private DocDefSpecification docDefSpecification;

	@Autowired
	private DefaultAssignDocSpecification defaultAssignDocSpecification;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DocumentDefSpecification documentDefSpecification;

	@Override
	public DocumentDefDTO createDocumentDef(DocumentDefDTO docDefDTO) {
		List<DocAttributeDefDTO> docAttrDef = docDefDTO.getDocAttrDef();
		if (Objects.nonNull(docAttrDef) && !docAttrDef.isEmpty()) {
			docDefValidator.validateOrder(docAttrDef);
			Set<String> attributeNames = docAttrDef.stream().map(DocAttributeDefDTO::getDocumentAttrName).collect(Collectors.toSet());
			if(attributeNames.size() < docAttrDef.size()){
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0016,"docAttrDef","documentAttrName",null,"Duplicate attributes not allowed");
			}
		}
		//check the given documentName already exists or not
//		docDefValidator.validateDocumentName(null,docDefDTO.getDocumentName());
		DocumentDef docDefEntity = modelMapper.map(docDefDTO, DocumentDef.class);
		docDefEntity.setStatus(EnumDocDefStatus.ACTIVE);
		DocumentDef savedEntity = documentDefRepo.save(docDefEntity);

		if (Objects.nonNull(docAttrDef) && !docAttrDef.isEmpty()) {
			List<DocAttributeDef> attrDef = new ArrayList<>();
			docAttrDef.forEach(docAttrDefDTO -> {
				DocAttributeDef docAttrDefEntity = modelMapper.map(docAttrDefDTO, DocAttributeDef.class);
				docAttrDefEntity.setDocumentDef(savedEntity);
				attrDef.add(docAttrDefEntity);
			});
			if(!attrDef.isEmpty()){
				docAttrDefRepo.saveAll(attrDef);
			}
			savedEntity.setDocAttrDef(attrDef);
		}

		return modelMapper.map(savedEntity, DocumentDefDTO.class);

	}

	@Override
	@Transactional
	public DefaultDocAssignmentsDTO assignDefaultDocuments(DefaultDocAssignmentsDTO docAssignmentDTO) {

		if (Objects.nonNull(docAssignmentDTO.getRelatedSubEnity())) {
			docDefValidator.validateRelatedType(docAssignmentDTO);
		}
		DocumentDef documentDef = docDefValidator.getDocumentDef(docAssignmentDTO.getDocumentDef().getDocumentDefID());
		DefaultDocAssignments docAssignment = null;
		Optional<DefaultDocAssignments> optionalDefaultDocAssignments = defaultDocAssRepo.findAllByDocDefAndRelatedEntity(documentDef.getDocumentDefID(),docAssignmentDTO.getRelatedEnity());
		if(optionalDefaultDocAssignments.isPresent()){
			docAssignment = optionalDefaultDocAssignments.get();
			if(docAssignment.getStatus().equals(EnumDefaultDocStatus.ACTIVE)){
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0017,"DefaultDocAssignment","documentDef",docAssignmentDTO.getDocumentDef().getDocumentDefID(),"DocDef by id:"+documentDef.getDocumentDefID()+" already associated with entity:"+docAssignmentDTO.getRelatedEnity());
			}
		}
		else{
			docAssignment = modelMapper.map(docAssignmentDTO, DefaultDocAssignments.class);
			docAssignment.setDocumentDef(documentDef);
		}
		docAssignment.setStatus(EnumDefaultDocStatus.ACTIVE);
		DefaultDocAssignments savedEntity = defaultDocAssRepo.save(docAssignment);
		return modelMapper.map(savedEntity, DefaultDocAssignmentsDTO.class);
	}

	@Override
	public void disAssociateDocDef(String docDefId, EnumDocRelatedEntity relatedEntity) {
		Optional<DefaultDocAssignments> docAssignments = defaultDocAssRepo.findByDocDefAndRelatedEntity(docDefId,relatedEntity);
		if(docAssignments.isPresent()){
			DefaultDocAssignments defaultDocAssignments=docAssignments.get();
			defaultDocAssignments.setStatus(EnumDefaultDocStatus.DISCARDED);
			defaultDocAssRepo.save(defaultDocAssignments);
		}
		throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"DocDef by id:"+docDefId+" not assigned to entity:"+relatedEntity);
	}

	@Override
	public DocumentDefDTO getDocumentDef(String documentDefID) {
		return modelMapper.map(docDefValidator.getDocumentDefById(documentDefID),DocumentDefDTO.class);
	}

	@Override
	public DocumentDefDTO updateDocumentDef(String documentDefID, DocumentDefDTO docDefDTO) {
		DocumentDef documentDef = docDefValidator.getDocumentDefById(documentDefID);
		//update doc def
		documentDef.setDocumentName(docDefDTO.getDocumentName());
		documentDef.setDocDisplayName(docDefDTO.getDocDisplayName());
		documentDef.setExpiryInd(docDefDTO.isExpiryInd());
		documentDef.setDownloadable(docDefDTO.getDownloadable());
		documentDef.setMonitorable(docDefDTO.getMonitorable());
		documentDef.setSecure(docDefDTO.getSecure());
		if(docDefDTO.getStatus() != null && !documentDef.getStatus().equals(docDefDTO.getStatus())){
			documentDef.setStatus(docDefDTO.getStatus());
		}
		documentDefRepo.save(documentDef);
		List<DocAttributeDefDTO> docAttrDef = docDefDTO.getDocAttrDef();
		if (Objects.nonNull(docAttrDef) && !docAttrDef.isEmpty()){
			docDefValidator.validateOrder(docAttrDef);
			Set<String> attributeNames = docAttrDef.stream().map(DocAttributeDefDTO::getDocumentAttrName).collect(Collectors.toSet());
			if(attributeNames.size() < docAttrDef.size()){
				throw new InvalidDataException(ErrorCodes.DOC_DEF_V_0016,"docAttrDef","documentAttrName",null,"Duplicate attributes not allowed");
			}
			List<DocAttributeDef> existingAttributes = documentDef.getDocAttrDef();
			docAttrDef.forEach(docAttrDefDTO -> {
				if(!StringUtils.isEmpty(docAttrDefDTO.getDocumentAttrDefID())){
					DocAttributeDef updatableAttributeDef = null;
					for(DocAttributeDef docAttributeDef :existingAttributes) {
						if(docAttrDefDTO.getDocumentAttrDefID().equals(docAttributeDef.getDocumentAttrDefID())){
							updatableAttributeDef = docAttributeDef;
							break;
						}
					}
					if(updatableAttributeDef == null){
						throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"docAttrDef by id:"+docAttrDefDTO.getDocumentAttrDefID()+" not found");
					}
					updateData(updatableAttributeDef,docAttrDefDTO);
				} else {
					DocAttributeDef docAttrDefEntity = modelMapper.map(docAttrDefDTO, DocAttributeDef.class);
					docAttrDefEntity.setDocumentDef(documentDef);
					existingAttributes.add(docAttrDefEntity);
				}
			});
			docAttrDefRepo.saveAll(existingAttributes);
			documentDef.setDocAttrDef(existingAttributes);
		}
		return modelMapper.map(documentDef, DocumentDefDTO.class);
	}

	@Override
	public Page<DocumentDefDTO> getAllDocDef(Integer offset, Integer limit, String name, EnumDocStatus status, EnumDocRelatedEntity relatedEntity) {
		//get specification object
		Specification specification = docDefSpecification.conditionalSearchForDocDef(name,status,relatedEntity);
		//get pageable object
		Pageable pageable = ServiceUtils.getPageableObject(offset,limit,null);
		//get records
		Page<DocumentDef> documentDefs = null;
		if(specification != null){
			documentDefs = documentDefRepo.findAll(specification,pageable);
		} else{
			documentDefs = documentDefRepo.findAll(pageable);
		}
		List<DocumentDefDTO> documentDefDTOS = new ArrayList<>();
		if(documentDefs != null && !documentDefs.getContent().isEmpty()){
			documentDefs.getContent().forEach(documentDef -> {
				documentDefDTOS.add(modelMapper.map(documentDef,DocumentDefDTO.class));
			});
			return new PageImpl<>(documentDefDTOS,pageable,documentDefs.getTotalElements());
		} else {
			return new PageImpl<>(documentDefDTOS,pageable,0);
		}
	}

	@Override
	public DefaultDocAssignmentsDTO getAssignedDefaultDocuments(String documentAssignDefID) {
		return modelMapper.map(docDefValidator.getAssignedDefaultDocument(documentAssignDefID),DefaultDocAssignmentsDTO.class);
	}

	@Override
	public Page<DefaultDocAssignmentsDTO> getAllDefaultAssignedDocs(Integer offset, Integer limit, String name, EnumDocStatus status,EnumDocRelatedEntity relatedEnity) {
		//get specification
		Specification specification = defaultAssignDocSpecification.conditionalSearchForDefAssDoc(name,status,relatedEnity);
		//get pageable object
		Pageable pageable = ServiceUtils.getPageableObject(offset,limit,null);
		Page<DefaultDocAssignments> docAssignmentsPage = null;
		if(specification != null){
			docAssignmentsPage = defaultDocAssRepo.findAll(specification,pageable);
		} else{
			docAssignmentsPage = defaultDocAssRepo.findAll(pageable);
		}
		List<DefaultDocAssignmentsDTO> defaultDocAssignmentsDTOS = new ArrayList<>();
		if(docAssignmentsPage != null && !docAssignmentsPage.getContent().isEmpty()){
			docAssignmentsPage.getContent().forEach(defaultDocAssignments -> {
				defaultDocAssignmentsDTOS.add(modelMapper.map(defaultDocAssignments,DefaultDocAssignmentsDTO.class));
			});
			return new PageImpl<>(defaultDocAssignmentsDTOS,pageable,docAssignmentsPage.getTotalElements());
		} else {
			return new PageImpl<>(defaultDocAssignmentsDTOS,pageable,0);
		}
	}

	@Override
	public DefaultDocAssignmentsDTO updateDefaultAssignDoc(String documentAssignDefID, DefaultDocAssignmentsDTO assignmentsDTO) {
		DefaultDocAssignments docAssignments = docDefValidator.getAssignedDefaultDocument(documentAssignDefID);
		if(assignmentsDTO.getStatus() != null && assignmentsDTO.getStatus().compareTo(docAssignments.getStatus()) != 0){
			docAssignments.setStatus(assignmentsDTO.getStatus());
		}
		if(assignmentsDTO.getRelatedEnity() != null && assignmentsDTO.getRelatedEnity().compareTo(docAssignments.getRelatedEnity()) != 0 ){
			docAssignments.setRelatedEnity(assignmentsDTO.getRelatedEnity());
		}
		return modelMapper.map(defaultDocAssRepo.save(docAssignments),DefaultDocAssignmentsDTO.class);
	}

	@Override
	public List<DefaultDocAssignments> getDefaultDocAssignments(EnumDocRelatedEntity docRelatatedEntity, EnumDefaultDocStatus status, Boolean autoAssigned) {
		Specification<DefaultDocAssignments> specification = documentDefSpecification
				.conditionalSearchForDefaultDocAssignments(docRelatatedEntity, status, autoAssigned);
		return defaultDocAssRepo.findAll(specification);
	}

	@Override
	public List<DocumentDefDTO> getUnAssignedDocDef(EnumDocRelatedEntity relatedEntity) {
		List<DocumentDef> unAssignedDocDefs = documentDefRepo.findUnAssignedDocDef(relatedEntity);
		List<DocumentDefDTO> documentDefDTOS = new ArrayList<>();
		if(!unAssignedDocDefs.isEmpty()){
			for(DocumentDef documentDef:unAssignedDocDefs){
				documentDefDTOS.add(modelMapper.map(documentDef,DocumentDefDTO.class));
			}
		}
		return documentDefDTOS;
	}

	@Override
	@Transactional
	public void deleteDocDef(String docDefId) {
		DocumentDef documentDef = docDefValidator.getDocumentDef(docDefId);
		documentDef.setStatus(EnumDocDefStatus.DISCARDED);
		documentDefRepo.save(documentDef);
		List<DefaultDocAssignments> defaultDocAssignments = defaultDocAssRepo.findActiveDefaultDocDef(docDefId);
		if(defaultDocAssignments != null && !defaultDocAssignments.isEmpty()){
			for(DefaultDocAssignments docAssignment:defaultDocAssignments){
				docAssignment.setStatus(EnumDefaultDocStatus.DISCARDED);
			}
			defaultDocAssRepo.saveAll(defaultDocAssignments);
		}

	}

	private void updateData(DocAttributeDef docAttributeDef,DocAttributeDefDTO docAttributeDefDTO){
		if(!StringUtils.isEmpty(docAttributeDefDTO.getDescription()) && (StringUtils.isEmpty(docAttributeDef.getDescription()) || !docAttributeDefDTO.getDescription().equals(docAttributeDef.getDescription()))){
			docAttributeDef.setDescription(docAttributeDefDTO.getDescription());
		}
		if(!StringUtils.isEmpty(docAttributeDefDTO.getAttrDisplayName()) && (StringUtils.isEmpty(docAttributeDef.getAttrDisplayName()) || !docAttributeDefDTO.getAttrDisplayName().equals(docAttributeDef.getAttrDisplayName()))){
			docAttributeDef.setAttrDisplayName(docAttributeDefDTO.getAttrDisplayName());
		}
		if(!StringUtils.isEmpty(docAttributeDefDTO.getDocumentAttrName()) && (StringUtils.isEmpty(docAttributeDef.getDocumentAttrName()) || !docAttributeDefDTO.getDocumentAttrName().equals(docAttributeDef.getDocumentAttrName()))){
			docAttributeDef.setDocumentAttrName(docAttributeDefDTO.getDocumentAttrName());
		}
		if(docAttributeDefDTO.getDisplayOrder() != docAttributeDef.getDisplayOrder()){
			docAttributeDef.setDisplayOrder(docAttributeDefDTO.getDisplayOrder());
		}
	}


}
