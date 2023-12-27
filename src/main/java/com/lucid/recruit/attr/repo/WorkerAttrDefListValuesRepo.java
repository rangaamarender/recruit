package com.lucid.recruit.attr.repo;

import com.lucid.recruit.attr.entity.WorkerAttrDefListValues;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkerAttrDefListValuesRepo extends JpaRepository<WorkerAttrDefListValues,String> {
    @Query("SELECT walv FROM WorkerAttrDefListValues walv where walv.workerAttributeDef.attrDefId=:attrDefId AND walv.attrListValueID=:attrListValueID")
    Optional<WorkerAttrDefListValues> getAttributeListValues(@Parameter(name = "attrDefId") String attrDefId, @Parameter(name = "attrListValueID") String attrListValueID);

}
