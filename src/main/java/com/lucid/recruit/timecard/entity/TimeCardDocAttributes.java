package com.lucid.recruit.timecard.entity;

import com.lucid.recruit.docs.entity.BaseDocAttributes;
import jakarta.persistence.*;

@Entity
@Table(name = TimeCardDocAttributes.TABLE_NAME)
public class TimeCardDocAttributes extends BaseDocAttributes {
    private static final long serialVersionUID = 3127766575009544916L;
    public static final String TABLE_NAME = "t_timeCard_doc_attributes";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "timeCard_doc_attr_id", nullable = false, length = 75)
    private String timeCardDocAttrID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeCard_doc_id", nullable = false, updatable = false)
    private com.lucid.recruit.timecard.entity.TimeCardDocument TimeCardDocument;

    public String getTimeCardDocAttrID() {
        return timeCardDocAttrID;
    }

    public void setTimeCardDocAttrID(String timeCardDocAttrID) {
        this.timeCardDocAttrID = timeCardDocAttrID;
    }

    public com.lucid.recruit.timecard.entity.TimeCardDocument getTimeCardDocument() {
        return TimeCardDocument;
    }

    public void setTimeCardDocument(com.lucid.recruit.timecard.entity.TimeCardDocument timeCardDocument) {
        TimeCardDocument = timeCardDocument;
    }
}
