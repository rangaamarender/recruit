package com.lucid.recruit.attr.repo;

import com.lucid.recruit.attr.entity.OrgAttrDefListValues;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgAttrDefListValuesRepo extends JpaRepository<OrgAttrDefListValues,String> {
    @Query("SELECT oalv.value FROM OrgAttrDefListValues oalv where oalv.orgAttributeDef.attrDefId=:attrDefId")
    List<String> getAttributeListValues(@Parameter(name = "attrDefId") String attrDefId);

}
