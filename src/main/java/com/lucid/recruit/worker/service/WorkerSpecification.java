package com.lucid.recruit.worker.service;

import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.lucid.recruit.contract.entity.ChargeCodeResource;
import com.lucid.recruit.referencedata.entity.Job;
import com.lucid.recruit.worker.entity.WorkAssignment;
import com.lucid.recruit.worker.entity.Worker;
import com.lucid.recruit.worker.entity.WorkerStatus;
import com.lucid.recruit.worker.entity.WorkerStatusCode;
import com.lucid.util.Strings;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Component
public class WorkerSpecification {

	private Specification<Worker> byEmail(String email) {
		return (root, query, cb) -> cb.equal(root.join("personLegal").join("personContactDetails").get("emailId"),
				email);
	}

	private Specification<Worker> byName(String fieldName, String name) {
		return (root, query, cb) -> cb.like(root.join("personLegal").get(fieldName), "%" + name + "%");
	}

	private Specification<Worker> byWorkerType(String workerType) {
		return (root, query, cb) -> cb.equal(root.get("workerType").get("workerTypeCode"), workerType);
	}

	private Specification<Worker> byOrganizationId(String organizationId) {
		return (root, query, cb) -> cb.equal(root.get("organization").get("organizationID"), organizationId);
	}

	private Specification<Worker> byStatus(WorkerStatusCode workerStatus, LocalDate effectiveDate) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			Join<Worker, WorkerStatus> rootJoin = root.join("workerStatus");

			Subquery<Long> subquery = query.subquery(Long.class);
			Root<WorkerStatus> subRoot = subquery.from(WorkerStatus.class);
			subquery.select(cb.max(subRoot.get("effectiveDate")))
					.where(cb.and(cb.equal(root.get("workerID"), subRoot.get("worker")),
							cb.lessThanOrEqualTo(subRoot.get("effectiveDate"), effectiveDate)));

			predicates.add(cb.equal(rootJoin.get("status"), workerStatus));
			predicates.add(cb.equal(rootJoin.get("effectiveDate"), subquery));
			// rootJoin
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	private Specification<Worker> byInternalInd(Boolean internalInd, LocalDate effectiveDate) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			Join<Worker, WorkAssignment> rootJoin = root.join("workAssignment");
			Join<WorkAssignment, Job> jobJoin = rootJoin.join("job");

			predicates.add(cb.and(cb.lessThanOrEqualTo(rootJoin.get("startDate"), effectiveDate),
					(cb.or(cb.isNull(rootJoin.get("endDate")),
							cb.greaterThanOrEqualTo(rootJoin.get("endDate"), effectiveDate)))));

			predicates.add(cb.equal(jobJoin.get("internalJobInd"), internalInd));

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	private Specification<Worker> byBenchInd(Boolean benchInd, LocalDate effectiveDate) {
		if (!benchInd.booleanValue()) {
			return (root, query, cb) -> {
				List<Predicate> predicates = new ArrayList<>();
				Join<Worker, ChargeCodeResource> rootJoin = root.join("contractResource");

				predicates.add(cb.and(cb.lessThanOrEqualTo(rootJoin.get("startDate"), effectiveDate),
						(cb.or(cb.isNull(rootJoin.get("endDate")),
								cb.greaterThanOrEqualTo(rootJoin.get("endDate"), effectiveDate)))));

				return cb.and(predicates.toArray(new Predicate[0]));
			};
		} else {
			return (root, query, cb) -> {
				List<Predicate> predicates = new ArrayList<>();

				Subquery<ChargeCodeResource> subquery = query.subquery(ChargeCodeResource.class);
				Root<ChargeCodeResource> subRoot = subquery.from(ChargeCodeResource.class);

				subquery.select(subRoot.get("worker"))
						.where(cb.and(cb.lessThanOrEqualTo(subRoot.get("startDate"), effectiveDate),
								(cb.or(cb.isNull(subRoot.get("endDate")),
										cb.greaterThanOrEqualTo(subRoot.get("endDate"), effectiveDate)))));

				predicates.add(cb.in(root.get("workerID")).value(subquery).not());

				return cb.and(predicates.toArray(new Predicate[0]));

			};
		}

	}

	public Specification<Worker> conditionalSearchForWorker(String organizationID, String workerType,
			WorkerStatusCode workerStatus, String emailId, String givenName, String familyName, LocalDate effectiveDate,
			Boolean internalInd, Boolean benchInd) {
		Specification<Worker> specification = null;
		if (!Strings.isNullOrEmpty(organizationID)) {
			specification = where(byOrganizationId(organizationID));
		}
		if (!Strings.isNullOrEmpty(workerType)) {
			if (specification != null) {
				specification = specification.and(byWorkerType(workerType));
			} else {
				specification = where(byWorkerType(workerType));
			}
		}
		if (!Strings.isNullOrEmpty(givenName)) {

			if (specification != null) {
				specification = specification.and(byName("givenName", givenName));
			} else {
				specification = where(byName("givenName", givenName));
			}
		}

		if (!Strings.isNullOrEmpty(familyName)) {
			if (specification != null) {
				specification = specification.and(byName("familyName", familyName));

			} else {
				specification = where(byName("familyName", familyName));
			}
		}

		if (!Strings.isNullOrEmpty(emailId)) {
			if (specification != null) {
				specification = specification.and(byEmail(emailId));
			} else {
				specification = where(byEmail(emailId));
			}
		}
		if (effectiveDate == null) {
			effectiveDate = LocalDate.now();
		}
		if (workerStatus != null) {
			if (specification != null) {
				specification = specification.and(byStatus(workerStatus, effectiveDate));
			} else {
				specification = where(byStatus(workerStatus, effectiveDate));
			}
		}
		if (Objects.nonNull(internalInd)) {
			if (specification != null) {
				specification = specification.and(byInternalInd(internalInd, effectiveDate));
			} else {
				specification = where(byInternalInd(internalInd, effectiveDate));
			}

		}

		if (Objects.nonNull(benchInd)) {
			if (specification != null) {
				specification = specification.and(byBenchInd(benchInd, effectiveDate));
			} else {
				specification = where(byBenchInd(benchInd, effectiveDate));
			}

		}

		// return specification object
		return specification;
	}

}
