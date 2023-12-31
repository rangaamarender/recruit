package com.lucid.recruit.timecard.entity;

import com.lucid.recruit.docs.entity.BaseDocument;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = TimeCardDocument.TABLE_NAME)
public class TimeCardDocument extends BaseDocument {

	// --------------------------------------------------------------- Constants
	private static final long serialVersionUID = 8419402802337278054L;
	public static final String TABLE_NAME = "t_timecard_doc";

	// --------------------------------------------------------- Class Variables
	// ----------------------------------------------------- Static Initializers
	// ------------------------------------------------------ Instance Variables
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "timecard_doc_id", nullable = false, length = 75)
	private String timeCardDocID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "timecard_id", nullable = true,updatable = false)
	private TimeCard timeCard;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TimeCardDocument")
	@OrderBy(value = "timeCard_doc_attr_id")
	private List<TimeCardDocAttributes> timeCardDocAttributes;


	// ------------------------------------------------------------ Constructors
	public TimeCardDocument() {
		super();
	}

	public String getTimeCardDocID() {
		return timeCardDocID;
	}

	public void setTimeCardDocID(String timeCardDocID) {
		this.timeCardDocID = timeCardDocID;
	}

	public TimeCard getTimecard() {
		return timeCard;
	}

	public void setTimecard(TimeCard timeCard) {
		this.timeCard = timeCard;
	}

	public TimeCard getTimeCard() {
		return timeCard;
	}

	public void setTimeCard(TimeCard timeCard) {
		this.timeCard = timeCard;
	}

	public List<TimeCardDocAttributes> getTimeCardDocAttributes() {
		return timeCardDocAttributes;
	}

	public void setTimeCardDocAttributes(List<TimeCardDocAttributes> timeCardDocAttributes) {
		this.timeCardDocAttributes = timeCardDocAttributes;
	}
}
