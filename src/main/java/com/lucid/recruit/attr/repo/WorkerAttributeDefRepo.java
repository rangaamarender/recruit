package com.lucid.recruit.attr.repo;

import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.entity.WorkerAttributeDef;
import com.lucid.recruit.contract.constants.EnumWorkOrderType;
import com.lucid.recruit.referencedata.entity.WorkerType;
import com.lucid.recruit.worker.entity.WorkerTypeCode;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkerAttributeDefRepo extends JpaRepository<WorkerAttributeDef,String> {

    WorkerAttributeDef findByAttrName(String attrName);

    @Query("SELECT wad FROM WorkerAttributeDef wad WHERE wad.required=true AND wad.status=:status AND (wad.workerType IS null OR wad.workerType.workerTypeCode =:workerType)")
    List<WorkerAttributeDef> findMandatoryAttributes(@Parameter(name = "workerType") WorkerTypeCode workerType, @Parameter(name = "status")EnumAttributeStatus status);

    @Query("SELECT wad FROM WorkerAttributeDef wad WHERE (:status IS NULL OR wad.status=:status)")
    List<WorkerAttributeDef> findByStatus(@Parameter(name="status")EnumAttributeStatus status);
}
