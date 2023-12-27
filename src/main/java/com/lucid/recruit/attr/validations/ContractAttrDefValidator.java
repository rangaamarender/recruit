package com.lucid.recruit.attr.validations;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.ContractAttrDefListValuesDTO;
import com.lucid.recruit.attr.dto.ContractAttributeDefDTO;
import com.lucid.recruit.attr.dto.OrgAttrDefListValuesDTO;
import com.lucid.recruit.attr.dto.OrgAttributeDefDTO;
import com.lucid.recruit.attr.entity.ContractAttrDefListValues;
import com.lucid.recruit.attr.entity.ContractAttributeDef;
import com.lucid.recruit.attr.entity.OrgAttrDefListValues;
import com.lucid.recruit.attr.entity.OrgAttributeDef;
import com.lucid.recruit.attr.repo.ContractAttributeDefRepo;
import com.lucid.recruit.contract.entity.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractAttrDefValidator {

    @Autowired
    private ContractAttributeDefRepo contractAttributeDefRepo;


    public ContractAttributeDef validateContractAttrDef(ContractAttributeDefDTO contractAttributeDefDTO, ContractAttributeDef contractAttributeDef){
        if(contractAttributeDef == null){
            contractAttributeDef = new ContractAttributeDef();
        }
        //set name
        if(contractAttributeDef.getAttrDefId() == null) {
            //validate given name already exists or not
            ContractAttributeDef existsByName = contractAttributeDefRepo.findByAttrName(contractAttributeDefDTO.getAttrName());
            if(existsByName != null){
                throw new InvalidDataException(ErrorCodes.C_ATTR_V_0001,"contractAttributeDef","attrName",contractAttributeDefDTO.getAttrName(),"ContractAttrDef by name:"+contractAttributeDefDTO.getAttrName()+" alreadyExists");
            }
            contractAttributeDef.setAttrName(contractAttributeDefDTO.getAttrName());
            contractAttributeDef.setRequired(contractAttributeDefDTO.getRequired());
            contractAttributeDef.setAttrType(contractAttributeDefDTO.getAttrType());
            contractAttributeDef.setDefinedList(contractAttributeDefDTO.getDefinedList());
            contractAttributeDef.setStatus(EnumAttributeStatus.ACTIVE);
        }
        //set displayName
        contractAttributeDef.setAttrDspName(contractAttributeDefDTO.getAttrDspName());
        //set displayOrder
        contractAttributeDef.setDisplayOrder(contractAttributeDefDTO.getDisplayOrder());
        //validate attribute type
        if(contractAttributeDefDTO.getDefaultValue() != null){
            BaseAttributeValidator.validateAttrTypeValue(contractAttributeDef.getAttrType(),contractAttributeDefDTO.getDefaultValue());
            contractAttributeDef.setDefaultValue(contractAttributeDefDTO.getDefaultValue());
        }
        if(contractAttributeDef.getDefinedList()){
            if(contractAttributeDefDTO.getAttrListValues() == null || contractAttributeDefDTO.getAttrListValues().isEmpty()){
                throw new InvalidDataException(ErrorCodes.O_ATTR_V_0003,"orgAttributeDef","attrListValues",null,"AttributeListValues mandatroy if definedList is true");
            } else {
                List<ContractAttrDefListValues> contractAttrDefListValuesList = contractAttributeDef.getAttrListValues();
                if(contractAttrDefListValuesList == null){
                    contractAttrDefListValuesList = new ArrayList<>();
                }
                for(ContractAttrDefListValuesDTO contractAttrDefListValuesDTO:contractAttributeDefDTO.getAttrListValues()){
                    BaseAttributeValidator.validateAttrTypeValue(contractAttributeDef.getAttrType(), contractAttrDefListValuesDTO.getValue());
                    if(contractAttrDefListValuesDTO.getAttrListValueID() != null){
                        ContractAttrDefListValues updatableAttDefListValue = null;
                        for(ContractAttrDefListValues contractAttrDefListValues:contractAttrDefListValuesList){
                            if(contractAttrDefListValues.getAttrListValueID() != null && contractAttrDefListValues.getAttrListValueID().equals(contractAttrDefListValuesDTO.getAttrListValueID())){
                                updatableAttDefListValue = contractAttrDefListValues;
                                break;
                            }
                        }
                        if(updatableAttDefListValue == null){
                            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractAttrDefListValue by id:"+contractAttrDefListValuesDTO.getAttrListValueID()+" not found");
                        }
                        updatableAttDefListValue.setValue(contractAttrDefListValuesDTO.getValue());
                        updatableAttDefListValue.setDisplayOrder(contractAttrDefListValuesDTO.getDisplayOrder());
                    }
                    else {
                        ContractAttrDefListValues contractAttrDefListValue = new ContractAttrDefListValues(contractAttrDefListValuesDTO.getValue(),
                                contractAttrDefListValuesDTO.getDisplayOrder(),
                                contractAttributeDef);
                        contractAttrDefListValuesList.add(contractAttrDefListValue);
                    }
                }
                contractAttributeDef.setAttrListValues(contractAttrDefListValuesList);
            }
        }
        return contractAttributeDef;
    }


}
