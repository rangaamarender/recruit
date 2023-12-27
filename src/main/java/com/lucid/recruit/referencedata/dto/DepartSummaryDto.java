package com.lucid.recruit.referencedata.dto;



import com.lucid.core.dto.BaseDTO;

import java.util.List;

public class DepartSummaryDto extends BaseDTO {


    private String deptID;

    private String deptName;

    private String deptDesc;

    private List<JobDTO> job;

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public List<JobDTO> getJob() {
        return job;
    }

    public void setJob(List<JobDTO> job) {
        this.job = job;
    }
}
