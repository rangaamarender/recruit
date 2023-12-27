package com.lucid.recruit.timecard.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.lucid.core.base.BaseService;
import com.lucid.recruit.timecard.constants.EnumTimeCardStatus;
import com.lucid.recruit.timecard.dto.TimeCardDTO;
import com.lucid.recruit.timecard.dto.TimeCardSummaryDTO;

public interface TimeCardService extends BaseService {

    TimeCardDTO createTimeCard(TimeCardDTO timeCardDTO);

    TimeCardDTO retrieveTimeCardById(String timeCardID);

    Page<TimeCardSummaryDTO> retrieveAllTimeCards(int offset, int limit, String contractID, String workerID,
                                                  String timecardID, String workOrderID, LocalDate startDate, LocalDate endDate, EnumTimeCardStatus status);

//    TimeCardDTO updateTimeCard(String timeCardID, TimeCardDTO timeCardDTO);

}
