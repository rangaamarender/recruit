package com.lucid.recruit.attr.validations;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.constants.EnumAttributeType;
import com.lucid.recruit.attr.dto.WorkerAttrDefListValuesDTO;
import com.lucid.recruit.attr.dto.WorkerAttributeDefDTO;
import com.lucid.recruit.attr.entity.WorkerAttrDefListValues;
import com.lucid.recruit.attr.entity.WorkerAttributeDef;
import com.lucid.recruit.attr.repo.WorkerAttributeDefRepo;
import com.lucid.recruit.referencedata.entity.WorkerType;
import com.lucid.recruit.referencedata.repo.WorkerTypeRepo;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerAttrDefValidator {

    @Autowired
    private WorkerAttributeDefRepo workerAttributeDefRepo;

    @Autowired
    private WorkerTypeRepo workerTypeRepo;

    @Autowired
    private ModelMapper modelMapper;

    public WorkerAttributeDef validateWorkerAttrDef(WorkerAttributeDefDTO workerAttributeDefDTO, WorkerAttributeDef workerAttributeDef){
        if(workerAttributeDef == null){
            workerAttributeDef = new WorkerAttributeDef();
        }
        //set name
        if(workerAttributeDef.getAttrDefId() == null) {
            //validate given name already exists or not
            WorkerAttributeDef existsByName = workerAttributeDefRepo.findByAttrName(workerAttributeDefDTO.getAttrName());
            if(existsByName != null){
                throw new InvalidDataException(ErrorCodes.W_ATTR_V_0001,"workerAttributeDef","attrName",workerAttributeDefDTO.getAttrName(),"WorkerAttrDef by name:"+workerAttributeDefDTO.getAttrName()+" alreadyExists");
            }
            workerAttributeDef.setAttrType(workerAttributeDefDTO.getAttrType());
            workerAttributeDef.setDefinedList(workerAttributeDefDTO.getDefinedList());
            workerAttributeDef.setStatus(EnumAttributeStatus.ACTIVE);
            workerAttributeDef.setUniqueType(workerAttributeDefDTO.getUniqueType());
        }
        workerAttributeDef.setRequired(workerAttributeDefDTO.getRequired());
        //set name
        workerAttributeDef.setAttrName(workerAttributeDefDTO.getAttrName());
        //set displayName
        workerAttributeDef.setAttrDspName(workerAttributeDefDTO.getAttrDspName());
        //set displayOrder
        workerAttributeDef.setDisplayOrder(workerAttributeDefDTO.getDisplayOrder());
        //set attribute type
        //if worker type is not null then check the workerType is configured or not
        //set workerType
        if(workerAttributeDefDTO.getWorkerType() != null){
            Optional<WorkerType> optionalWorkerType = workerTypeRepo.findById(workerAttributeDefDTO.getWorkerType().getWorkerTypeCode());
            if(optionalWorkerType.isEmpty()){
                throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkerType by code:"+workerAttributeDefDTO.getWorkerType().getWorkerTypeCode()+" not found");
            }
            workerAttributeDef.setWorkerType(optionalWorkerType.get());
        }
        //validate attribute type
        if(workerAttributeDefDTO.getDefaultValue() != null){
            BaseAttributeValidator.validateAttrTypeValue(workerAttributeDef.getAttrType(),workerAttributeDefDTO.getDefaultValue());
            workerAttributeDef.setDefaultValue(workerAttributeDefDTO.getDefaultValue());
        }
        if(workerAttributeDef.getDefinedList()){
            if(workerAttributeDefDTO.getAttrListValues() == null || workerAttributeDefDTO.getAttrListValues().isEmpty()){
                throw new InvalidDataException(ErrorCodes.W_ATTR_V_0003,"workerAttributeDef","attrListValues",null,"AttributeListValues mandatroy if definedList is true");
            } else {
                List<WorkerAttrDefListValues> workerAttrDefListValuesList = workerAttributeDef.getAttrListValues();
                if(workerAttrDefListValuesList == null){
                    workerAttrDefListValuesList = new ArrayList<>();
                }
                for(WorkerAttrDefListValuesDTO workerAttrDefListValuesDTO:workerAttributeDefDTO.getAttrListValues()){
                    BaseAttributeValidator.validateAttrTypeValue(workerAttributeDef.getAttrType(), workerAttrDefListValuesDTO.getValue());
                    if(workerAttrDefListValuesDTO.getAttrListValueID() != null){
                        WorkerAttrDefListValues updatableAttDefListValue = null;
                        for(WorkerAttrDefListValues workerAttrDefListValues:workerAttrDefListValuesList){
                            if(workerAttrDefListValues.getAttrListValueID() != null && workerAttrDefListValues.getAttrListValueID().equals(workerAttrDefListValuesDTO.getAttrListValueID())){
                                updatableAttDefListValue = workerAttrDefListValues;
                                break;
                            }
                        }
                        if(updatableAttDefListValue == null){
                            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkerAttrDefListValue by id:"+workerAttrDefListValuesDTO.getAttrListValueID()+" not found");
                        }
                        updatableAttDefListValue.setValue(workerAttrDefListValuesDTO.getValue());
                        updatableAttDefListValue.setDisplayOrder(workerAttrDefListValuesDTO.getDisplayOrder());
                    }
                    else {
                        WorkerAttrDefListValues workerAttrDefListValue = new WorkerAttrDefListValues(workerAttrDefListValuesDTO.getValue(),
                                workerAttrDefListValuesDTO.getDisplayOrder(),
                                workerAttributeDef);
                        workerAttrDefListValuesList.add(workerAttrDefListValue);
                    }
                }
                workerAttributeDef.setAttrListValues(workerAttrDefListValuesList);
            }
        }
        return workerAttributeDef;
    }




}
