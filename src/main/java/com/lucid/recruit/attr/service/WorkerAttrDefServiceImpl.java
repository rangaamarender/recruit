package com.lucid.recruit.attr.service;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.WorkerAttributeDefDTO;
import com.lucid.recruit.attr.entity.WorkerAttributeDef;
import com.lucid.recruit.attr.repo.WorkerAttrDefListValuesRepo;
import com.lucid.recruit.attr.repo.WorkerAttributeDefRepo;
import com.lucid.recruit.attr.validations.WorkerAttrDefValidator;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerAttrDefServiceImpl implements WorkerAttrDefService{

    @Autowired
    private WorkerAttributeDefRepo workerAttributeDefRepo;
    @Autowired
    private WorkerAttrDefListValuesRepo workerAttrDefListValuesRepo;
    @Autowired
    private WorkerAttrDefValidator workerAttrDefValidator;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public WorkerAttributeDefDTO createWorkerAttributeDef(WorkerAttributeDefDTO workerAttributeDefDTO) {
        //validate workerAttrDef
        WorkerAttributeDef workerAttributeDef= workerAttrDefValidator.validateWorkerAttrDef(workerAttributeDefDTO,null);
        workerAttributeDefRepo.save(workerAttributeDef);
        if(workerAttributeDef.getAttrListValues() != null){
            workerAttrDefListValuesRepo.saveAll(workerAttributeDef.getAttrListValues());
        }
        return modelMapper.map(workerAttributeDef,WorkerAttributeDefDTO.class);
    }

    @Override
    public WorkerAttributeDefDTO updateWorkerAttributeDef(String attrDefId, WorkerAttributeDefDTO workerAttributeDefDTO) {
        WorkerAttributeDef updatableAttrDef = getWorkerAttDef(attrDefId);
        updatableAttrDef = workerAttrDefValidator.validateWorkerAttrDef(workerAttributeDefDTO,updatableAttrDef);
        workerAttributeDefRepo.save(updatableAttrDef);
        if(updatableAttrDef.getAttrListValues() != null){
            workerAttrDefListValuesRepo.saveAll(updatableAttrDef.getAttrListValues());
        }
        return modelMapper.map(updatableAttrDef,WorkerAttributeDefDTO.class);
    }

    @Override
    public String updateStatus(String attrDefId, EnumAttributeStatus status) {
        WorkerAttributeDef workerAttributeDef = getWorkerAttDef(attrDefId);
        workerAttributeDef.setStatus(status);
        workerAttributeDefRepo.save(workerAttributeDef);
        return "AttributeDef with status:"+status.toString()+" updated successfully";
    }

    @Override
    public WorkerAttributeDefDTO getWorkerAttributeDef(String attrDefId) {
        return modelMapper.map(getWorkerAttDef(attrDefId),WorkerAttributeDefDTO.class);
    }

    @Override
    public List<WorkerAttributeDefDTO> getAllWorkerAttributeDef(EnumAttributeStatus status) {
        List<WorkerAttributeDefDTO> workerAttributeDefDTOList = new ArrayList<>();
        List<WorkerAttributeDef> workerAttributeDefList = workerAttributeDefRepo.findByStatus(status);
        if(workerAttributeDefList != null && !workerAttributeDefList.isEmpty()){
            for(WorkerAttributeDef workerAttributeDef:workerAttributeDefList){
                workerAttributeDefDTOList.add(modelMapper.map(workerAttributeDef,WorkerAttributeDefDTO.class)) ;
            }
        }
        return workerAttributeDefDTOList;
    }

    private WorkerAttributeDef getWorkerAttDef(String attrDefId){
        Optional<WorkerAttributeDef> optionalWorkerAttributeDef = workerAttributeDefRepo.findById(attrDefId);
        if(optionalWorkerAttributeDef.isEmpty()){
            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkerAttributeDef by id:"+attrDefId+" not found");
        }
        return optionalWorkerAttributeDef.get();
    }
}
