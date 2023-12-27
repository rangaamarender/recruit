package com.lucid.recruit.attr.service;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.ContractAttributeDefDTO;
import com.lucid.recruit.attr.entity.ContractAttributeDef;
import com.lucid.recruit.attr.repo.ContractAttrDefListValuesRepo;
import com.lucid.recruit.attr.repo.ContractAttributeDefRepo;
import com.lucid.recruit.attr.validations.ContractAttrDefValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContractAttrDefServiceImpl implements ContractAttrDefService{

    @Autowired
    private ContractAttributeDefRepo contractAttributeDefRepo;
    @Autowired
    private ContractAttrDefListValuesRepo contractAttrDefListValuesRepo;
    @Autowired
    private ContractAttrDefValidator contractAttrDefValidator;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ContractAttributeDefDTO createContractAttributeDef(ContractAttributeDefDTO contractAttributeDefDTO) {
        //validate workerAttrDef
        ContractAttributeDef contractAttributeDef= contractAttrDefValidator.validateContractAttrDef(contractAttributeDefDTO,null);
        contractAttributeDefRepo.save(contractAttributeDef);
        if(contractAttributeDef.getAttrListValues() != null){
            contractAttrDefListValuesRepo.saveAll(contractAttributeDef.getAttrListValues());
        }
        return modelMapper.map(contractAttributeDef,ContractAttributeDefDTO.class);
    }

    @Override
    public ContractAttributeDefDTO updateContractAttributeDef(String attrDefId, ContractAttributeDefDTO contractAttributeDefDTO) {
        ContractAttributeDef updatableAttrDef = getContractAttDef(attrDefId);
        updatableAttrDef = contractAttrDefValidator.validateContractAttrDef(contractAttributeDefDTO,updatableAttrDef);
        contractAttributeDefRepo.save(updatableAttrDef);
        if(updatableAttrDef.getAttrListValues() != null){
            contractAttrDefListValuesRepo.saveAll(updatableAttrDef.getAttrListValues());
        }
        return modelMapper.map(updatableAttrDef,ContractAttributeDefDTO.class);
    }

    @Override
    public String updateStatus(String attrDefId, EnumAttributeStatus status) {
        ContractAttributeDef contractAttributeDef = getContractAttDef(attrDefId);
        contractAttributeDef.setStatus(status);
        contractAttributeDefRepo.save(contractAttributeDef);
        return "AttributeDef with status:"+status.toString()+" updated successfully";
    }

    @Override
    public ContractAttributeDefDTO getContractAttributeDef(String attrDefId) {
        return modelMapper.map(getContractAttDef(attrDefId),ContractAttributeDefDTO.class);
    }

    @Override
    public List<ContractAttributeDefDTO> getAllContractAttributeDef() {
        List<ContractAttributeDefDTO> contractAttributeDefDTOList = new ArrayList<>();
        List<ContractAttributeDef> contractAttributeDefList = contractAttributeDefRepo.findAll();
        if(contractAttributeDefList != null && !contractAttributeDefList.isEmpty()){
            for(ContractAttributeDef contractAttributeDef:contractAttributeDefList){
                contractAttributeDefDTOList.add(modelMapper.map(contractAttributeDef,ContractAttributeDefDTO.class)) ;
            }
        }
        return contractAttributeDefDTOList;
    }

    private ContractAttributeDef getContractAttDef(String attrDefId){
        Optional<ContractAttributeDef> optionalContractAttributeDef = contractAttributeDefRepo.findById(attrDefId);
        if(optionalContractAttributeDef.isEmpty()){
            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractAttributeDef by id:"+attrDefId+" not found");
        }
        return optionalContractAttributeDef.get();
    }
}
