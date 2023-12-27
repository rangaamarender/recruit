package com.lucid.recruit.attr.validations;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.OrgAttrDefListValuesDTO;
import com.lucid.recruit.attr.dto.OrgAttributeDefDTO;
import com.lucid.recruit.attr.dto.WorkerAttrDefListValuesDTO;
import com.lucid.recruit.attr.dto.WorkerAttributeDefDTO;
import com.lucid.recruit.attr.entity.OrgAttrDefListValues;
import com.lucid.recruit.attr.entity.OrgAttributeDef;
import com.lucid.recruit.attr.entity.WorkerAttrDefListValues;
import com.lucid.recruit.attr.entity.WorkerAttributeDef;
import com.lucid.recruit.attr.repo.OrgAttributeDefRepo;
import com.lucid.recruit.attr.repo.WorkerAttributeDefRepo;
import com.lucid.recruit.referencedata.entity.WorkerType;
import com.lucid.recruit.referencedata.repo.WorkerTypeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrgAttrDefValidator {

    @Autowired
    private OrgAttributeDefRepo orgAttributeDefRepo;


    public OrgAttributeDef validateOrgAttrDef(OrgAttributeDefDTO orgAttributeDefDTO, OrgAttributeDef orgAttributeDef){
        if(orgAttributeDef == null){
            orgAttributeDef = new OrgAttributeDef();
        }
        //set name
        if(orgAttributeDef.getAttrDefId() == null) {
            //validate given name already exists or not
            OrgAttributeDef existsByName = orgAttributeDefRepo.findByAttrName(orgAttributeDefDTO.getAttrName());
            if(existsByName != null){
                throw new InvalidDataException(ErrorCodes.O_ATTR_V_0001,"orgAttributeDef","attrName",orgAttributeDefDTO.getAttrName(),"OrgAttrDef by name:"+orgAttributeDefDTO.getAttrName()+" alreadyExists");
            }
            orgAttributeDef.setAttrName(orgAttributeDefDTO.getAttrName());
            orgAttributeDef.setRequired(orgAttributeDefDTO.getRequired());
            orgAttributeDef.setAttrType(orgAttributeDefDTO.getAttrType());
            orgAttributeDef.setDefinedList(orgAttributeDefDTO.getDefinedList());
            orgAttributeDef.setStatus(EnumAttributeStatus.ACTIVE);
        }
        //set displayName
        orgAttributeDef.setAttrDspName(orgAttributeDefDTO.getAttrDspName());
        //set displayOrder
        orgAttributeDef.setDisplayOrder(orgAttributeDefDTO.getDisplayOrder());
        //validate attribute type
        if(orgAttributeDefDTO.getDefaultValue() != null){
            BaseAttributeValidator.validateAttrTypeValue(orgAttributeDef.getAttrType(),orgAttributeDefDTO.getDefaultValue());
            orgAttributeDef.setDefaultValue(orgAttributeDefDTO.getDefaultValue());
        }
        if(orgAttributeDef.getDefinedList()){
            if(orgAttributeDefDTO.getAttrListValues() == null || orgAttributeDefDTO.getAttrListValues().isEmpty()){
                throw new InvalidDataException(ErrorCodes.O_ATTR_V_0003,"orgAttributeDef","attrListValues",null,"AttributeListValues mandatroy if definedList is true");
            } else {
                List<OrgAttrDefListValues> orgAttrDefListValuesList = orgAttributeDef.getAttrListValues();
                if(orgAttrDefListValuesList == null){
                    orgAttrDefListValuesList = new ArrayList<>();
                }
                for(OrgAttrDefListValuesDTO orgAttrDefListValuesDTO:orgAttributeDefDTO.getAttrListValues()){
                    BaseAttributeValidator.validateAttrTypeValue(orgAttributeDef.getAttrType(), orgAttrDefListValuesDTO.getValue());
                    if(orgAttrDefListValuesDTO.getAttrListValueID() != null){
                        OrgAttrDefListValues updatableAttDefListValue = null;
                        for(OrgAttrDefListValues orgAttrDefListValues:orgAttrDefListValuesList){
                            if(orgAttrDefListValues.getAttrListValueID() != null && orgAttrDefListValues.getAttrListValueID().equals(orgAttrDefListValuesDTO.getAttrListValueID())){
                                updatableAttDefListValue = orgAttrDefListValues;
                                break;
                            }
                        }
                        if(updatableAttDefListValue == null){
                            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkerAttrDefListValue by id:"+orgAttrDefListValuesDTO.getAttrListValueID()+" not found");
                        }
                        updatableAttDefListValue.setValue(orgAttrDefListValuesDTO.getValue());
                        updatableAttDefListValue.setDisplayOrder(orgAttrDefListValuesDTO.getDisplayOrder());
                    }
                    else {
                        OrgAttrDefListValues orgAttrDefListValue = new OrgAttrDefListValues(orgAttrDefListValuesDTO.getValue(),
                                orgAttrDefListValuesDTO.getDisplayOrder(),
                                orgAttributeDef);
                        orgAttrDefListValuesList.add(orgAttrDefListValue);
                    }
                }
                orgAttributeDef.setAttrListValues(orgAttrDefListValuesList);
            }
        }
        return orgAttributeDef;
    }


}
