package com.lucid.recruit.contract.entity;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.lucid.core.entity.AuditableEntity;

import jakarta.persistence.*;

@Entity
@Table(name = ContractDiscount.TABLE_NAME)
public class ContractDiscount extends AuditableEntity {

	private static final long serialVersionUID = -850703202329193906L;

	public static final String TABLE_NAME = "c_contract_discounts";

	// UUID for each discount
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "discount_id", length = 75, nullable = false)
	private String discountId;

	// Name of the discount, will be displayed on invoice if required
	@Column(name = "discount_name", length = 75, nullable = false)
	private String discountName;

	// Name of the discount description
	@Column(name = "discount_desc", length = 255, nullable = true)
	private String discountDesc;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cntrct_account_id", nullable = false, updatable = false)
	private ContractAccount contractAccount;

	// Date from which discount is active
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	// Last day on of the discount
	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;

	// Specifies whether percentage discount applicable on the net charge or gross
	@Column(name = "net_charge_boo", nullable = true)
	private boolean netChargeBoo;

	@Column(name = "disc_priority", nullable = false)
	private int priority;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractDiscount",cascade = CascadeType.ALL)
	private List<ContractDiscountStep> discountStep;

	public ContractDiscount() {
		super();
	}

	public String getDiscountId() {
		return discountId;
	}

	public void setDiscountId(String discountId) {
		this.discountId = discountId;
	}

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
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

	public String getDiscountDesc() {
		return discountDesc;
	}

	public void setDiscountDesc(String discountDesc) {
		this.discountDesc = discountDesc;
	}

	public boolean isNetChargeBoo() {
		return netChargeBoo;
	}

	public void setNetChargeBoo(boolean netChargeBoo) {
		this.netChargeBoo = netChargeBoo;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<ContractDiscountStep> getDiscountStep() {
		return discountStep;
	}

	public void setDiscountStep(List<ContractDiscountStep> discountStep) {
		this.discountStep = discountStep;
	}

	public static Comparator<ContractDiscount> createContractDiscountLambdaComparator() {
		return Comparator.comparing(ContractDiscount::getPriority);
	}
}
