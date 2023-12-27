package com.lucid.recruit.docs.repo;

import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;
import com.lucid.recruit.docs.entity.DocumentDef;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;
@Service
public class DocDefSpecification {

    private Specification<DocumentDef> byName(String name){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("documentName"),"%"+name+"%");
    }
    private Specification<DocumentDef> byStatus(EnumDocStatus status){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),status);
    }

    private Specification<DocumentDef> byRelatedEntity(EnumDocRelatedEntity relatedEnity){
        return (root, query, criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();
            Join<DocumentDef,DefaultDocAssignments> rootJoin = root.join("docAssignments");

            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(rootJoin.get("relatedEnity"), relatedEnity)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<DocumentDef> conditionalSearchForDocDef(String name, EnumDocStatus status, EnumDocRelatedEntity relatedEnity){
        Specification<DocumentDef> specification = null;
        if(!StringUtils.isEmpty(name)){
            specification = where(byName(name));
        }
        if(status != null){
            if(specification != null){
                specification = specification.and(byStatus(status));
            }
            else {
                specification=where(byStatus(status));
            }
        }
        if(relatedEnity != null){
            if(specification != null){
                specification = specification.and(byRelatedEntity(relatedEnity));
            }
            else {
                specification=where(byRelatedEntity(relatedEnity));
            }
        }
        return specification;
    }
}
