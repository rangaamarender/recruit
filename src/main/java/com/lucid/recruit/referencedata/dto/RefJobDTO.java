package com.lucid.recruit.referencedata.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RefJobDTO {
    @NotNull
    private String jobID;
    @Nullable
    private String jobName;
    @Nullable
    private boolean billable;

    public RefJobDTO() {
        super();
    }

    public RefJobDTO(@NotNull String jobID, String jobName, boolean billable) {
        super();
        this.jobID = jobID;
        this.jobName = jobName;
        this.billable = billable;
    }

    public RefJobDTO(@NotNull String jobID) {
        super();
        this.jobID = jobID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }


}
