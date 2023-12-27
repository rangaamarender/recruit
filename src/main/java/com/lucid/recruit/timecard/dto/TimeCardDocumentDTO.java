package com.lucid.recruit.timecard.dto;

import com.lucid.recruit.docs.dto.BaseDocumentDTO;
import com.lucid.recruit.docs.dto.DocumentDefDTO;
import com.lucid.recruit.worker.dto.WorkerDocAttributesDTO;
import jakarta.annotation.Nullable;

import java.util.List;

public class TimeCardDocumentDTO  extends BaseDocumentDTO{
    private static final long serialVersionUID = 1L;
    @Nullable
    private String timeCardDocID;
    private  String timecardID;


    public String getTimeCardDocID() {
        return timeCardDocID;
    }

    public void setTimeCardDocID(String timeCardDocID) {
        this.timeCardDocID = timeCardDocID;
    }

    public String getTimecardID() {return timecardID;}

    public void setTimecardID(String timecardID) {this.timecardID = timecardID;}
}
