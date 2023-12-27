package com.lucid.recruit.docs.service;

import com.lucid.recruit.docs.constants.EnumDefaultDocStatus;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;
import com.lucid.recruit.org.entity.Organization;
import com.lucid.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class DocumentDefSpecification {

    public Specification<DefaultDocAssignments> conditionalSearchForDefaultDocAssignments(EnumDocRelatedEntity relatedEntity, EnumDefaultDocStatus status, Boolean autoAssigned) {
        Specification<DefaultDocAssignments> specification = null;
        if (relatedEntity != null) {
            specification = where(byRelatedEntity(relatedEntity));
        }
        if (status != null) {
            if (specification != null) {
                specification = specification.and(byStatus(status));
            } else {
                specification = where(byStatus(status));
            }
        }
        if (autoAssigned != null) {
            if (specification != null) {
                specification = specification.and(byAutoAssigned(autoAssigned));
            } else {
                specification = where(byAutoAssigned(autoAssigned));
            }
        }
        return specification;
    }

    private Specification<DefaultDocAssignments> byAutoAssigned(Boolean autoAssigned) {
        return (root, query, cb) -> cb.equal(root.get("autoAssigned"), autoAssigned);
    }

    private Specification<DefaultDocAssignments> byStatus(EnumDefaultDocStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private Specification<DefaultDocAssignments> byRelatedEntity(EnumDocRelatedEntity relatedEntity) {
        return (root, query, cb) -> cb.equal(root.get("relatedEnity"), relatedEntity);
    }

}
