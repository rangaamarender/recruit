package com.lucid.recruit.contract.entity;

import com.lucid.core.entity.AuditableEntity;

import com.lucid.recruit.contract.constants.EnumDiscountStepType;
import jakarta.persistence.*;

import java.util.Comparator;

@Entity
@Table(name = ContractDiscountStep.TABLE_NAME, uniqueConstraints = {
		@UniqueConstraint(name = "DISCSTEP1", columnNames = { "discount_id", "step_number" }) })
public class ContractDiscountStep extends AuditableEntity {

	private static final long serialVersionUID = -4330927819884789378L;

	public static final String TABLE_NAME = "c_contract_disc_step";

	// UUID for each discount step
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "disc_step_id", length = 75, nullable = false)
	private String discSetpId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "discount_id", nullable = false, updatable = false)
	private ContractDiscount contractDiscount;

	@Column(name = "step_number", nullable = false)
	private int stepNumber;

	@Column(name = "step_threshold", nullable = false)
	private double stepThreshold;

	// percentage discount, null if and only if discount type is monetary
	@Column(name = "discount_pct", nullable = true)
	private Integer discountPct;

	// total amount of the discounted amount, null if and only if discount type is
	// percentage
	@Column(name = "discount_mny", nullable = true)
	private Double discountAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "discount_type", nullable = false)
	private EnumDiscountStepType discountType;

	public ContractDiscountStep() {
		super();
	}

	public String getDiscSetpId() {
		return discSetpId;
	}

	public void setDiscSetpId(String discSetpId) {
		this.discSetpId = discSetpId;
	}

	public ContractDiscount getContractDiscount() {
		return contractDiscount;
	}

	public void setContractDiscount(ContractDiscount contractDiscount) {
		this.contractDiscount = contractDiscount;
	}

	public int getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	public double getStepThreshold() {
		return stepThreshold;
	}

	public void setStepThreshold(double stepThreshold) {
		this.stepThreshold = stepThreshold;
	}

	public Integer getDiscountPct() {
		return discountPct;
	}

	public void setDiscountPct(Integer discountPct) {
		this.discountPct = discountPct;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public EnumDiscountStepType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(EnumDiscountStepType discountType) {
		this.discountType = discountType;
	}

	public static Comparator<ContractDiscountStep> createContractDiscountStepLambdaComparator() {
		return Comparator.comparing(ContractDiscountStep::getStepThreshold);
	}
}
