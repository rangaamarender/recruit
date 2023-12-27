package com.lucid.recruit.contract.dto;

import com.lucid.core.annotations.Phone;
import com.lucid.recruit.worker.dto.RelatedWorkerDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class WorkOrderResourceDTO {

   @Nullable
   private String woResourceID;


    @NotNull
    private RelatedWorkerDTO worker;

    @NotNull
    private LocalDate startDate;

    @Nullable
    private LocalDate endDate;

    @NotNull
    @Size(min = 1,max = 1,message = "Worker type mandatory")
    private List<ContractResourceWorkerTypeDTO> workerType;

    // T - Work location remote
    // F - Work location associated with w/o
    @NotNull
    private Boolean remoteWorkLoc;

    // Work email id
    @Email
    @NotBlank
    private String workEmail;

    @Nullable
    private String jobTitle;

    // Work phone number
    @Phone
    @NotBlank
    private String workPhone;

    // List of supervisor details
    @Nullable
    private List<ContractSupervisorDTO> supervisors;

    @Nullable
    public String getWoResourceID() {
        return woResourceID;
    }

    public void setWoResourceID(@Nullable String woResourceID) {
        this.woResourceID = woResourceID;
    }

    public RelatedWorkerDTO getWorker() {
        return worker;
    }

    public void setWorker(RelatedWorkerDTO worker) {
        this.worker = worker;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Nullable
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@Nullable LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<ContractResourceWorkerTypeDTO> getWorkerType() {
        return workerType;
    }

    public void setWorkerType(List<ContractResourceWorkerTypeDTO> workerType) {
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

    @Nullable
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(@Nullable String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    @Nullable
    public List<ContractSupervisorDTO> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(@Nullable List<ContractSupervisorDTO> supervisors) {
        this.supervisors = supervisors;
    }
}
