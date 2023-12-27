package com.lucid.recruit.attr.service;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.OrgAttributeDefDTO;
import com.lucid.recruit.attr.entity.OrgAttributeDef;
import com.lucid.recruit.attr.repo.OrgAttrDefListValuesRepo;
import com.lucid.recruit.attr.repo.OrgAttributeDefRepo;
import com.lucid.recruit.attr.validations.OrgAttrDefValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrgAttrDefServiceImpl implements OrgAttrDefService{

    @Autowired
    private OrgAttributeDefRepo orgAttributeDefRepo;
    @Autowired
    private OrgAttrDefListValuesRepo orgAttrDefListValuesRepo;
    @Autowired
    private OrgAttrDefValidator orgAttrDefValidator;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrgAttributeDefDTO createOrgAttributeDef(OrgAttributeDefDTO orgAttributeDefDTO) {
        //validate orgAttrDef
        OrgAttributeDef orgAttributeDef= orgAttrDefValidator.validateOrgAttrDef(orgAttributeDefDTO,null);
        orgAttributeDefRepo.save(orgAttributeDef);
        if(orgAttributeDef.getAttrListValues() != null){
            orgAttrDefListValuesRepo.saveAll(orgAttributeDef.getAttrListValues());
        }
        return modelMapper.map(orgAttributeDef,OrgAttributeDefDTO.class);
    }

    @Override
    public OrgAttributeDefDTO updateOrgAttributeDef(String attrDefId, OrgAttributeDefDTO orgAttributeDefDTO) {
        OrgAttributeDef updatableAttrDef = getOrgAttDef(attrDefId);
        updatableAttrDef = orgAttrDefValidator.validateOrgAttrDef(orgAttributeDefDTO,updatableAttrDef);
        orgAttributeDefRepo.save(updatableAttrDef);
        if(updatableAttrDef.getAttrListValues() != null){
            orgAttrDefListValuesRepo.saveAll(updatableAttrDef.getAttrListValues());
        }
        return modelMapper.map(updatableAttrDef,OrgAttributeDefDTO.class);
    }

    @Override
    public String updateStatus(String attrDefId, EnumAttributeStatus status) {
        OrgAttributeDef workerAttributeDef = getOrgAttDef(attrDefId);
        workerAttributeDef.setStatus(status);
        orgAttributeDefRepo.save(workerAttributeDef);
        return "AttributeDef with status:"+status.toString()+" updated successfully";
    }

    @Override
    public OrgAttributeDefDTO getOrgAttributeDef(String attrDefId) {
        return modelMapper.map(getOrgAttDef(attrDefId),OrgAttributeDefDTO.class);
    }

    @Override
    public List<OrgAttributeDefDTO> getAllOrgAttributeDef() {
        List<OrgAttributeDefDTO> orgAttributeDefDTOList = new ArrayList<>();
        List<OrgAttributeDef> orgAttributeDefList = orgAttributeDefRepo.findAll();
        if(orgAttributeDefList != null && !orgAttributeDefList.isEmpty()){
            for(OrgAttributeDef orgAttributeDef:orgAttributeDefList){
                orgAttributeDefDTOList.add(modelMapper.map(orgAttributeDef,OrgAttributeDefDTO.class)) ;
            }
        }
        return orgAttributeDefDTOList;
    }

    private OrgAttributeDef getOrgAttDef(String attrDefId){
        Optional<OrgAttributeDef> optionalOrgAttributeDef = orgAttributeDefRepo.findById(attrDefId);
        if(optionalOrgAttributeDef.isEmpty()){
            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"OrgAttributeDef by id:"+attrDefId+" not found");
        }
        return optionalOrgAttributeDef.get();
    }
}
