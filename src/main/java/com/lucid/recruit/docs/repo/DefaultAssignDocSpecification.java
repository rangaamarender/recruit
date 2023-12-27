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

@Service
public class DefaultAssignDocSpecification {

    private Specification<DefaultDocAssignments> byDocDefName(String name){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<DefaultDocAssignments,DocumentDef> rootJoin = root.join("documentDef");

            predicates.add(criteriaBuilder.and(criteriaBuilder.like(rootJoin.get("documentName"), "%"+name+"%")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<DefaultDocAssignments> byStatus(EnumDocStatus status){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),status);
    }

    private Specification<DefaultDocAssignments> byRelatedEnity(EnumDocRelatedEntity relatedEntity){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("relatedEnity"),relatedEntity);
    }

    public Specification<DefaultDocAssignments> conditionalSearchForDefAssDoc(String name,EnumDocStatus status,EnumDocRelatedEntity relatedEntity){
        Specification<DefaultDocAssignments> specification = null;
        if(!StringUtils.isEmpty(name)){
            specification = Specification.where(byDocDefName(name));
        }
        if(status != null){
            if(specification != null){
                specification = specification.and(byStatus(status));
            }
            else{
                specification = Specification.where(byStatus(status));
            }
        }
        if(relatedEntity != null){
            if(specification != null){
                specification = specification.and(byRelatedEnity(relatedEntity));
            }
            else{
                specification = Specification.where(byRelatedEnity(relatedEntity));
            }
        }
        return specification;
    }

}
