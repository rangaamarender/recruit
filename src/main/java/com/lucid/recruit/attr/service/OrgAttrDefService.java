package com.lucid.recruit.attr.service;

import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.OrgAttributeDefDTO;

import java.util.List;

public interface OrgAttrDefService {
    OrgAttributeDefDTO createOrgAttributeDef(OrgAttributeDefDTO orgAttributeDef);
    OrgAttributeDefDTO updateOrgAttributeDef(String attrDefId,OrgAttributeDefDTO orgAttributeDef);
    String updateStatus(String attrDefId, EnumAttributeStatus status);
    OrgAttributeDefDTO getOrgAttributeDef(String attrDefId);
    List<OrgAttributeDefDTO> getAllOrgAttributeDef();
}
