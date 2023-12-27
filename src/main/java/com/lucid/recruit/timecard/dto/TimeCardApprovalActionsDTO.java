package com.lucid.recruit.timecard.dto;

import com.lucid.recruit.contract.constants.EnumTimeCardApproval;
import com.lucid.recruit.timecard.constants.EnumTimeCardStepStatus;
import jakarta.validation.constraints.NotNull;

public class TimeCardApprovalActionsDTO {
    private String tcApprovalActionId;
    @NotNull
    private int totalSteps;
    @NotNull
    private int stepNumber;
    @NotNull
    private boolean finalStep;
    @NotNull
    private EnumTimeCardApproval tsApprovalFlow;
    @NotNull
    private EnumTimeCardStepStatus stepStatus;
    @NotNull
    private String approver;
    @NotNull
    private int reminderCount;

    public String getTcApprovalActionId() {return tcApprovalActionId;}

    public void setTcApprovalActionId(String tcApprovalActionId) {this.tcApprovalActionId = tcApprovalActionId;}

    public int getTotalSteps() {return totalSteps;}

    public void setTotalSteps(int totalSteps) {this.totalSteps = totalSteps;}

    public int getStepNumber() {return stepNumber;}

    public void setStepNumber(int stepNumber) {this.stepNumber = stepNumber;}

    public boolean isFinalStep() {return finalStep;}

    public void setFinalStep(boolean finalStep) {this.finalStep = finalStep;}

    public EnumTimeCardApproval getTsApprovalFlow() {return tsApprovalFlow;}

    public void setTsApprovalFlow(EnumTimeCardApproval tsApprovalFlow) {this.tsApprovalFlow = tsApprovalFlow;}

    public EnumTimeCardStepStatus getStepStatus() {return stepStatus;}

    public void setStepStatus(EnumTimeCardStepStatus stepStatus) {this.stepStatus = stepStatus;}

    public String getApprover() {return approver;}

    public void setApprover(String approver) {this.approver = approver;}

    public int getReminderCount() {return reminderCount;}

    public void setReminderCount(int reminderCount) {this.reminderCount = reminderCount;}
}
