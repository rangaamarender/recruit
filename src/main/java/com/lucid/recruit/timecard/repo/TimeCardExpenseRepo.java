package com.lucid.recruit.timecard.repo;

import com.lucid.recruit.timecard.entity.TimeCardExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TimeCardExpenseRepo extends JpaRepository<TimeCardExpense, String> {
    @Modifying
    @Transactional
    @Query("DELETE from TimeCardExpense te where te.timeCard.timecardID =:timecardID")
    void deleteExpenseByTimeCardId(@Param("timecardID") String timecardID);
}
