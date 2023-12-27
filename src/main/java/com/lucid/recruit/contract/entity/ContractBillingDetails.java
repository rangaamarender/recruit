package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.entity.AuditableEntity;

import com.lucid.recruit.contract.constants.EnumBillPeriodUnit;
import jakarta.persistence.*;

@Entity
@Table(name = ContractBillingDetails.TABLE_NAME)
public class ContractBillingDetails extends AuditableEntity {

	private static final long serialVersionUID = -7200894734570180260L;

	public static final String TABLE_NAME = "c_contract_billing_details";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "cntrct_billing_id", nullable = false, length = 75)
	private String cntrctBillingID;

	// Contract work order group ID
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cntrct_account_id", nullable = false, updatable = false)
	private ContractAccount contractAccount;

	// Date from when this record is active
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// Date till when this record is active
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	// bill period in the units of invoice period units
	@Column(name = "bill_period", nullable = false)
	private Integer billPeriod;

	// Bill period units, Supported values
	// Weekly
	// Monthly
	// Yearly
	@Enumerated(EnumType.STRING)
	@Column(name = "bill_period_units", nullable = false, length = 10)
	private EnumBillPeriodUnit billPeriodUnits;

	// Invoice cycle
	// e.g. if invoice period is monthly,
	// Cycle "01" means generate invoice 1st of every month
	// Cycle "10" means generate invoice 10th of every month
	// Monthly can have 28 cycles
	// Weekly can have 01 to 07 cycles
	// Yearly can have 01 to 12 cycles
	@Column(name = "bill_cycle", nullable = false, length = 2)
	private Integer billCycle;

	// By when the payment need to be made after bill is generated
	@Column(name = "payment_due_days", nullable = true)
	private Integer paymentDueDays;

	// Grace period to start dunning actions, if invoice is not paid
	@Column(name = "grace_period_days", nullable = true)
	private Integer gracePeriodDays;

	@Column(name = "expenses_bill_boo", nullable = false)
	private boolean expensesBillBoo;

	public ContractBillingDetails() {
		super();
	}

	public String getCntrctBillingID() {
		return cntrctBillingID;
	}

	public void setCntrctBillingID(String cntrctBillingID) {
		this.cntrctBillingID = cntrctBillingID;
	}

	public ContractAccount getContractAccount() {
		return contractAccount;
	}

	public void setContractAccount(ContractAccount contractAccount) {
		this.contractAccount = contractAccount;
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

	public Integer getPaymentDueDays() {
		return paymentDueDays;
	}

	public void setPaymentDueDays(Integer paymentDueDays) {
		this.paymentDueDays = paymentDueDays;
	}

	public Integer getGracePeriodDays() {
		return gracePeriodDays;
	}

	public void setGracePeriodDays(Integer gracePeriodDays) {
		this.gracePeriodDays = gracePeriodDays;
	}

	public boolean isExpensesBillBoo() {
		return expensesBillBoo;
	}

	public void setExpensesBillBoo(boolean expensesBillBoo) {
		this.expensesBillBoo = expensesBillBoo;
	}

	public static Comparator<ContractBillingDetails> createContractBillingLambdaComparator() {
		return Comparator.comparing(ContractBillingDetails::getStartDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}

		ContractBillingDetails contractBillingDetails = (ContractBillingDetails) obj; // Cast obj to your class type
		// Compare relevant fields for equality

		if(billPeriod == null){
			if(contractBillingDetails.billPeriod != null){
				return false;
			}
		} else if(!billPeriod.equals(contractBillingDetails.billPeriod)){
			return false;
		}
		if(billCycle == null){
			if(contractBillingDetails.billCycle != null) {
				return false;
			}
		} else if(!billCycle.equals(contractBillingDetails.billCycle)){
			return false;
		}
		if(billPeriodUnits == null){
			if(contractBillingDetails.getBillPeriodUnits() != null){
				return false;
			}
		} else if(!billPeriodUnits.equals(contractBillingDetails.getBillPeriodUnits())){
			return false;
		}
		if(paymentDueDays == null){
			if(contractBillingDetails.paymentDueDays != null){
				return false;
			}
		}
		else if(!paymentDueDays.equals(contractBillingDetails.paymentDueDays)){
			return false;
		}
		if(gracePeriodDays == null){
			if(contractBillingDetails.gracePeriodDays != null){
				return false;
			}
		}
		else if(!gracePeriodDays.equals(contractBillingDetails.gracePeriodDays)){
			return false;
		}
		if(isExpensesBillBoo() != contractBillingDetails.isExpensesBillBoo()){
			return false;
		}
		return true;
	}

}
