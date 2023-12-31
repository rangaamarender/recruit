package com.lucid.recruit.timecard.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class TimeCardException {

    private final Date timeStamp;
    private final String errorMessage;
    private final String errorCode;
    private final HttpStatus status;

    public TimeCardException(Date timeStamp, String errorMessage, String errorCode, HttpStatus status) {
        this.timeStamp = timeStamp;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.status = status;
    }


    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
