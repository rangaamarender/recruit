package com.lucid.recruit.attr.repo;

import com.lucid.recruit.attr.entity.ContractAttrDefListValues;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractAttrDefListValuesRepo extends JpaRepository<ContractAttrDefListValues,String> {
    @Query("SELECT calv.value FROM ContractAttrDefListValues calv where calv.contractAttributeDef.attrDefId=:attrDefId")
    List<String> getAttributeListValues(@Parameter(name = "attrDefId") String attrDefId);

}
