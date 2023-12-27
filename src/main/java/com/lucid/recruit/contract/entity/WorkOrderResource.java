package com.lucid.recruit.contract.entity;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.worker.entity.Worker;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = WorkOrderResource.TABLE_NAME )
public class WorkOrderResource extends AuditableEntity {
    private static final long serialVersionUID = 2322703611361912348L;

	public static final String TABLE_NAME="c_workorder_resource";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wo_resource_id")
    private String woResourceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wrkorder_id",nullable = false,updatable = false)
    private ContractWorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id",nullable = false)
    private Worker worker;

    @OneToMany(fetch = FetchType.LAZY,mappedBy ="workOrderResource")
    private List<ContractResourceWorkerType> workerType;


    // T - Work location remote
    // F - Work location associated with w/o
    @Column(name = "remote_wrk_loc", nullable = false)
    private Boolean remoteWorkLoc;

    // Work email id
    @Column(name = "work_email", nullable = false, length = 75)
    private String workEmail;

    @Column(name = "job_title", nullable = true, length = 75)
    private String jobTitle;

    // Work phone number
    @Column(name = "work_phone", nullable = false, length = 25)
    private String workPhone;

    // List of supervisor details
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workOrderResource")
    @OrderBy("startDate desc")
    private List<ContractSupervisor> supervisors;

    @Column(name = "start_dt",nullable = false)
    private LocalDate startDate;

    @Column(name = "end_dt",nullable = true)
    private LocalDate endDate;


    public String getWoResourceID() {
        return woResourceID;
    }

    public void setWoResourceID(String woResourceID) {
        this.woResourceID = woResourceID;
    }

    public ContractWorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(ContractWorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public List<ContractResourceWorkerType> getWorkerType() {
        return workerType;
    }

    public void setWorkerType(List<ContractResourceWorkerType> workerType) {
        this.workerType = workerType;
    }

    public Boolean getRemoteWorkLoc() {
        return remoteWorkLoc;
    }

    public void setRemoteWorkLoc(Boolean remoteWorkLoc) {
        this.remoteWorkLoc = remoteWorkLoc;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public List<ContractSupervisor> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(List<ContractSupervisor> supervisors) {
        this.supervisors = supervisors;
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
}
