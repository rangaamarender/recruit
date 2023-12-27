package com.lucid.recruit.attr.service;

import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.ContractAttributeDefDTO;
import com.lucid.recruit.attr.dto.WorkerAttributeDefDTO;

import java.util.List;

public interface ContractAttrDefService {
    ContractAttributeDefDTO createContractAttributeDef(ContractAttributeDefDTO contractAttributeDef);
    ContractAttributeDefDTO updateContractAttributeDef(String attrDefId, ContractAttributeDefDTO contractAttributeDef);
    String updateStatus(String attrDefId, EnumAttributeStatus status);
    ContractAttributeDefDTO getContractAttributeDef(String attrDefId);
    List<ContractAttributeDefDTO> getAllContractAttributeDef();
}
