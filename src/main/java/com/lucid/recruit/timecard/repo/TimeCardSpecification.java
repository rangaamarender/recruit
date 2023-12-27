package com.lucid.recruit.timecard.repo;


import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.lucid.recruit.timecard.constants.EnumTimeCardStatus;
import com.lucid.recruit.timecard.entity.TimeCard;
import com.lucid.util.Strings;

@Component
public class TimeCardSpecification {

    // By contractID
    private Specification<TimeCard> byContractID(String contractID) {
        return (root, query, cb) -> cb.equal(root.get("contract").get("contractID"), contractID);
    }

    // By resourceID
    private Specification<TimeCard> byResourceID(String workerID) {
        return (root, query, cb) -> cb.equal(root.get("worker").get("workerID"), workerID);
    }

    // By timecardID
    private Specification<TimeCard> byTimecardID(String timecardID) {
        return (root, query, cb) -> cb.equal(root.get("timecardID"), timecardID);
    }

    // By workOrderID
    private Specification<TimeCard> byWorkOrderID(String workOrderID) {
        return (root, query, cb) -> cb.equal(root.get("workOrder").get("workOrderID"), workOrderID);
    }

    // By startDate
    private Specification<TimeCard> byStartDate(LocalDate startDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startDate"), startDate);
    }

    // By endDate
    private Specification<TimeCard> byEndDate(LocalDate endDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("endDate"), endDate);
    }

    // By status
    private Specification<TimeCard> byStatus(EnumTimeCardStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public Specification<TimeCard> conditionalSearchForTimeCard(String contractID, String workerID, String timecardID,
                                                                String workOrderID, LocalDate startDate, LocalDate endDate, EnumTimeCardStatus status) {

        Specification<TimeCard> specification = null;

        if (!Strings.isNullOrEmpty(contractID)) {
            specification = (byContractID(contractID));
        }

        if (!Strings.isNullOrEmpty(workerID)) {
            if (specification != null) {
                specification = specification.and(byResourceID(workerID));
            } else {
                specification = where(byResourceID(workerID));
            }
        }

        if (!Strings.isNullOrEmpty(timecardID)) {
            if (specification != null) {
                specification = specification.and(byTimecardID(timecardID));
            } else {
                specification = where(byTimecardID(timecardID));
            }
        }

        if (!Strings.isNullOrEmpty(workOrderID)) {
            if (specification != null) {
                specification = specification.and(byWorkOrderID(workOrderID));
            } else {
                specification = where(byWorkOrderID(workOrderID));
            }
        }

        if (startDate != null) {
            if (specification != null) {
                specification = specification.and(byStartDate(startDate));
            } else {
                specification = where(byStartDate(startDate));
            }
        }

        if (endDate != null) {
            if (specification != null) {
                specification = specification.and(byEndDate(endDate));
            } else {
                specification = where(byEndDate(endDate));
            }
        }

        if (status != null) {
            if (specification != null) {
                specification = specification.and(byStatus(status));
            } else {
                specification = where(byStatus(status));
            }
        }

        return specification;
    }

}
