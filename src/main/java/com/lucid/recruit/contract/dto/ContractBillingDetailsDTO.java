package com.lucid.recruit.contract.dto;

import com.lucid.recruit.contract.constants.EnumBillPeriodUnit;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ContractBillingDetailsDTO {

    @Nullable
    private String cntrctBillingID;


    // Date from when this record is active
    @Nullable
    private LocalDate startDate;

    // Date till when this record is active
    @Nullable
    private LocalDate endDate;

    // bill period in the units of invoice period units
    @NotNull
    private Integer billPeriod;

    // Bill period units, Supported values
    // Weekly
    // Monthly
    // Yearly
    @NotNull
    private EnumBillPeriodUnit billPeriodUnits;

    // Invoice cycle
    // e.g. if invoice period is monthly,
    // Cycle "01" means generate invoice 1st of every month
    // Cycle "10" means generate invoice 10th of every month
    // Monthly can have 28 cycles
    // Weekly can have 01 to 07 cycles
    // Yearly can have 01 to 12 cycles
    @NotNull
    private Integer billCycle;

    // By when the payment need to be made after bill is generated
    @Nullable
    private Integer paymentDueDays;

    // Grace period to start dunning actions, if invoice is not paid
    @Nullable
    private Integer gracePeriodDays;


    private boolean expensesBillBoo;

    @Nullable
    public String getCntrctBillingID() {
        return cntrctBillingID;
    }

    public void setCntrctBillingID(@Nullable String cntrctBillingID) {
        this.cntrctBillingID = cntrctBillingID;
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

    public Integer getBillPeriod() {
        return billPeriod;
    }

    public void setBillPeriod(Integer billPeriod) {
        this.billPeriod = billPeriod;
    }

    public EnumBillPeriodUnit getBillPeriodUnits() {
        return billPeriodUnits;
    }

    public void setBillPeriodUnits(EnumBillPeriodUnit billPeriodUnits) {
        this.billPeriodUnits = billPeriodUnits;
    }

    public Integer getBillCycle() {
        return billCycle;
    }

    public void setBillCycle(Integer billCycle) {
        this.billCycle = billCycle;
    }

    @Nullable
    public Integer getPaymentDueDays() {
        return paymentDueDays;
    }

    public void setPaymentDueDays(@Nullable Integer paymentDueDays) {
        this.paymentDueDays = paymentDueDays;
    }

    @Nullable
    public Integer getGracePeriodDays() {
        return gracePeriodDays;
    }

    public void setGracePeriodDays(@Nullable Integer gracePeriodDays) {
        this.gracePeriodDays = gracePeriodDays;
    }

    public boolean isExpensesBillBoo() {
        return expensesBillBoo;
    }

    public void setExpensesBillBoo(boolean expensesBillBoo) {
        this.expensesBillBoo = expensesBillBoo;
    }
}
