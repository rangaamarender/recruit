package com.lucid.recruit.contract.entity;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.referencedata.entity.WorkerType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Comparator;

@Entity
@Table(name = ContractResourceWorkerType.TABLE_NAME)
public class ContractResourceWorkerType extends AuditableEntity {

    public static final String TABLE_NAME="c_cntrct_res_workertype";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "res_wt_id",nullable = false, length = 75, updatable = false)
    private String resWorkerTypeID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_type_code", nullable = false,updatable = false)
    private WorkerType workerType;

    @Column(name = "start_date",nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date",nullable = true)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wo_resource_id",updatable = false)
    private WorkOrderResource workOrderResource;

    public String getResWorkerTypeID() {
        return resWorkerTypeID;
    }

    public void setResWorkerTypeID(String resWorkerTypeID) {
        this.resWorkerTypeID = resWorkerTypeID;
    }

    public WorkerType getWorkerType() {
        return workerType;
    }

    public void setWorkerType(WorkerType workerType) {
        this.workerType = workerType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public WorkOrderResource getWorkOrderResource() {
        return workOrderResource;
    }

    public void setWorkOrderResource(WorkOrderResource workOrderResource) {
        this.workOrderResource = workOrderResource;
    }

    public static Comparator<ContractResourceWorkerType> createContractResourceWokerTypeComparator() {
        return Comparator.comparing(ContractResourceWorkerType::getStartDate);
    }
}
