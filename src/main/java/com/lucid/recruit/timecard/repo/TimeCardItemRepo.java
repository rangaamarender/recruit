package com.lucid.recruit.timecard.repo;

import com.lucid.recruit.timecard.entity.TimeCard;
import com.lucid.recruit.timecard.entity.TimeCardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TimeCardItemRepo extends JpaRepository<TimeCardItem,String> {
    @Modifying
    @Transactional
    @Query("DELETE from TimeCardItem ti where ti.timeCard.timecardID =:timeCardID")
    void deleteItemsByTimeCardID(@Param("timeCardID") String timeCardID);
}
