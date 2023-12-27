package com.lucid.recruit.contract.entity;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.contract.constants.EnumContractAccountStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = ContractAccountStatus.TABLE_NAME)
public class ContractAccountStatus extends AuditableEntity {

    public static final String TABLE_NAME="c_contract_accstatus";

    @Id
    @Column(name = "acc_status_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accStatusId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status",nullable = false)
    private EnumContractAccountStatus status;

    @Column(name = "effective_date",nullable = false)
    private LocalDate effectiveDate;

    @ManyToOne
    @JoinColumn(name = "contract_accid",nullable = false,unique = false)
    private ContractAccount contractAccount;

    public String getAccStatusId() {
        return accStatusId;
    }

    public void setAccStatusId(String accStatusId) {
        this.accStatusId = accStatusId;
    }

    public EnumContractAccountStatus getStatus() {
        return status;
    }

    public void setStatus(EnumContractAccountStatus status) {
        this.status = status;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public ContractAccount getContractAccount() {
        return contractAccount;
    }

    public void setContractAccount(ContractAccount contractAccount) {
        this.contractAccount = contractAccount;
    }
}
