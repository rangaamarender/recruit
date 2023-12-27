/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.timecard.entity;

import java.time.LocalDate;

import com.lucid.core.entity.BaseEntity;
import com.lucid.recruit.contract.entity.ChargeCodeTasks;
import com.lucid.recruit.contract.entity.WorkOrderChargeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = TimeCardItem.TABLE_NAME)
public class TimeCardItem extends BaseEntity {

	private static final long serialVersionUID = 1688975540910475920L;
	public static final String TABLE_NAME = "t_timecard_item";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "timecard_item_id", nullable = false, length = 75)
	private String timecardItemID;

	// time card for items
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "timecard_id", nullable = false, updatable = false)
	private TimeCard timeCard;

	@Column(name = "item_date", nullable = false)
	private LocalDate itemDate;

	// Category under the project link to pay rate
	@Column(name = "task_id", nullable = false)
	private ChargeCodeTasks chargeCodeTask;

	// Work order change code
	@Column(name = "charge_code_id", nullable = true)
	private WorkOrderChargeCode chargeCode;

	// Number of hours
	@Column(name = "hours", nullable = false)
	private double hours;

	// Comments
	@Column(name = "comment", nullable = true, length = 400)
	private String comment;

	// status comments
	@Column(name = "status_comments", nullable = true, length = 400)
	private String statusComments;

	public TimeCardItem() {
		super();
	}

	public String getTimecardItemID() {
		return timecardItemID;
	}

	public void setTimecardItemID(String timecardItemID) {
		this.timecardItemID = timecardItemID;
	}

	public TimeCard getTimeCard() {
		return timeCard;
	}

	public void setTimeCard(TimeCard timeCard) {
		this.timeCard = timeCard;
	}

	public LocalDate getItemDate() {
		return itemDate;
	}

	public void setItemDate(LocalDate itemDate) {
		this.itemDate = itemDate;
	}

	public ChargeCodeTasks getChargeCodeTask() {
		return chargeCodeTask;
	}

	public void setChargeCodeTask(ChargeCodeTasks chargeCodeTask) {
		this.chargeCodeTask = chargeCodeTask;
	}

	public WorkOrderChargeCode getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(WorkOrderChargeCode chargeCode) {
		this.chargeCode = chargeCode;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatusComments() {
		return statusComments;
	}

	public void setStatusComments(String statusComments) {
		this.statusComments = statusComments;
	}
}
