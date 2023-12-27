package com.lucid.recruit.attr.service;

import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.WorkerAttributeDefDTO;

import java.util.List;

public interface WorkerAttrDefService {
    WorkerAttributeDefDTO createWorkerAttributeDef(WorkerAttributeDefDTO workerAttributeDef);
    WorkerAttributeDefDTO updateWorkerAttributeDef(String attrDefId,WorkerAttributeDefDTO workerAttributeDef);
    String updateStatus(String attrDefId, EnumAttributeStatus status);
    WorkerAttributeDefDTO getWorkerAttributeDef(String attrDefId);
    List<WorkerAttributeDefDTO> getAllWorkerAttributeDef(EnumAttributeStatus status);
}
