package com.lucid.recruit.attr.repo;

import com.lucid.recruit.attr.entity.ContractAttributeDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractAttributeDefRepo extends JpaRepository<ContractAttributeDef,String> {

    ContractAttributeDef findByAttrName(String attrName);

    @Query("SELECT cad FROM ContractAttributeDef cad WHERE cad.required=true")
    List<ContractAttributeDef> findMandatoryAttributes();
}
