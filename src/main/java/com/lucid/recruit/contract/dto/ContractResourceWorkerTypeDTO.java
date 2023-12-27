package com.lucid.recruit.contract.dto;


import com.lucid.recruit.referencedata.dto.WorkerTypeDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public class ContractResourceWorkerTypeDTO {
    @Nullable
    private String resWorkerTypeID;


    @NotNull
    private WorkerTypeDTO workerType;

    @Nullable
    private LocalDate startDate;

    @Nullable
    private LocalDate endDate;


    public String getResWorkerTypeID() {
        return resWorkerTypeID;
    }

    public void setResWorkerTypeID(String resWorkerTypeID) {
        this.resWorkerTypeID = resWorkerTypeID;
    }

    public WorkerTypeDTO getWorkerType() {
        return workerType;
    }

    public void setWorkerType(WorkerTypeDTO workerType) {
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


}
