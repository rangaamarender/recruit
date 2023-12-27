package com.lucid.recruit.org.repo;

import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.org.entity.*;
import com.lucid.recruit.worker.entity.Worker;
import com.lucid.recruit.worker.entity.WorkerStatus;
import com.lucid.recruit.worker.entity.WorkerStatusCode;
import com.lucid.util.Strings;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class OrganizationSpecification {

    private Specification<Organization> byOrganizationId(String organizationID) {
        return (root, query, cb) -> cb.equal(root.get("organizationID"), organizationID);
    }

    private Specification<Organization> byEmail(String email) {
        return (root, query, cb) -> cb.equal(root.join("orgCommunications").get("authSignataryEmail"), email);
    }

    private Specification<Organization> byName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), name);
    }

    private Specification<Organization> byStatus(OrgStatus orgStatus) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Organization, OrganizationStatus> rootJoin = root.join("status");

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<OrganizationStatus> subRoot = subquery.from(OrganizationStatus.class);
            subquery.select(cb.max(subRoot.get("effectiveDate")))
                    .where(cb.and(cb.equal(root.get("organizationID"), subRoot.get("organization")),
                            cb.lessThanOrEqualTo(subRoot.get("effectiveDate"), LocalDate.now())));

            predicates.add(cb.equal(rootJoin.get("statusCode"), orgStatus));
            predicates.add(cb.equal(rootJoin.get("effectiveDate"), subquery));
            // rootJoin
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Organization> conditionalSearchForOrganization(String organizationID, String emailId, String name,OrgStatus orgStatus) {
        Specification<Organization> specification = null;
        if (!Strings.isNullOrEmpty(organizationID)) {
            specification = where(byOrganizationId(organizationID));
        }
        if (!Strings.isNullOrEmpty(emailId)) {
            if (specification != null) {
                specification = specification.and(byEmail(emailId));
            } else {
                specification = where(byEmail(emailId));
            }
        }
        if (!Strings.isNullOrEmpty(name)) {
            if (specification != null) {
                specification = specification.and(byName(name));
            } else {
                specification = where(byName(name));
            }
        }
        if (orgStatus != null) {
            if (specification != null) {
                specification = specification.and(byStatus(orgStatus));
            } else {
                specification = where(byStatus(orgStatus));
            }
        }
        return specification;
    }

}
