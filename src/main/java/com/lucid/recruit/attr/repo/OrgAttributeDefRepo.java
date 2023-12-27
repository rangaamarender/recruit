package com.lucid.recruit.attr.repo;

import com.lucid.recruit.attr.entity.OrgAttributeDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgAttributeDefRepo extends JpaRepository<OrgAttributeDef,String> {

    OrgAttributeDef findByAttrName(String attrName);

    @Query("SELECT oad FROM OrgAttributeDef oad WHERE oad.required=true")
    List<OrgAttributeDef> findMandatoryAttributes();
}
