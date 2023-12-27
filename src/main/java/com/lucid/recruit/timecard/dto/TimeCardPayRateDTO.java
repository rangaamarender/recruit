package com.lucid.recruit.timecard.dto;

import com.lucid.core.dto.BaseDTO;
import com.lucid.recruit.timecard.entity.TimeCard;
import jakarta.validation.Valid;

public class TimeCardPayRateDTO extends BaseDTO {
    private String timeCardPayRateID;
    private String timecardID;
    @Valid
    private TimeCard timeCard;

    public String getTimeCardPayRateID() {
        return timeCardPayRateID;
    }

    public void setTimeCardPayRateID(String timeCardPayRateID) {
        this.timeCardPayRateID = timeCardPayRateID;
    }

    public String getTimecardID() {
        return timecardID;
    }

    public void setTimecardID(String timecardID) {
        this.timecardID = timecardID;
    }

    public TimeCard getTimeCard() {
        return timeCard;
    }

    public void setTimeCard(TimeCard timeCard) {
        this.timeCard = timeCard;
    }
}

