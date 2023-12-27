package com.lucid.recruit.timecard.repo;

import com.lucid.recruit.timecard.entity.TimeCard;
import com.lucid.recruit.timecard.entity.TimeCardExpenseDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TimeCardExpenseDocumentRepo extends JpaRepository<TimeCardExpenseDocument,String> {
    @Modifying
    @Transactional
    @Query("DELETE from TimeCardExpenseDocument ted where ted.timeCard.timecardID =:timecardID")
    void deleteExpenseDocumentByTimeCardId(@Param("timecardID") String timecardID);
}
