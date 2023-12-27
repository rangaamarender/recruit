package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;


public class ContractDiscountDTO {

    //UUID for each discount
    @Nullable
    private String discountId;

    // Name of the discount, will be displayed on invoice if required
    @NotBlank
    private String discountName;

    // Name of the discount description
    @Nullable
    private String discountDesc;

    // Date from which discount is active
    @NotNull
    private LocalDate startDate;

    // Last day on of the discount
    @Nullable
    private LocalDate endDate;

    // Specifies whether percentage discount applicable on the net charge or gross
    @Nullable
    private boolean netChargeBoo;

    @NotNull
    private int priority;

    @NotNull
    @Valid
    @Size(min = 1,message = "Required at least one discountStep")
    private List<ContractDiscountStepDTO> discountStep;

    @Nullable
    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(@Nullable String discountId) {
        this.discountId = discountId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    @Nullable
    public String getDiscountDesc() {
        return discountDesc;
    }

    public void setDiscountDesc(@Nullable String discountDesc) {
        this.discountDesc = discountDesc;
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

    @Nullable
    public boolean isNetChargeBoo() {
        return netChargeBoo;
    }

    public void setNetChargeBoo(@Nullable boolean netChargeBoo) {
        this.netChargeBoo = netChargeBoo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Nullable
    public List<ContractDiscountStepDTO> getDiscountStep() {
        return discountStep;
    }

    public void setDiscountStep(@Nullable List<ContractDiscountStepDTO> discountStep) {
        this.discountStep = discountStep;
    }

}
