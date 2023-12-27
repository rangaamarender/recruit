package com.lucid.recruit.worker.dto;

import com.lucid.recruit.worker.entity.WorkerTypeCode;

public class WorkerTypeCountDTO {
    private WorkerTypeCode workerTypeCode;
    private Long count;

    public WorkerTypeCountDTO(WorkerTypeCode workerTypeCode, Long count) {
        this.workerTypeCode = workerTypeCode;
        this.count = count;
    }

    public WorkerTypeCode getWorkerTypeCode() {
        return workerTypeCode;
    }

    public void setWorkerTypeCode(WorkerTypeCode workerTypeCode) {
        this.workerTypeCode = workerTypeCode;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
