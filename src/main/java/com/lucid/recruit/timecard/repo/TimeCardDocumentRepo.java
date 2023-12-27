package com.lucid.recruit.timecard.repo;

import com.lucid.recruit.timecard.entity.TimeCard;
import com.lucid.recruit.timecard.entity.TimeCardDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TimeCardDocumentRepo extends JpaRepository<TimeCardDocument,String> {

    @Modifying
    @Transactional
    @Query("DELETE from TimeCardDocument td where td.timeCard.timecardID =:timecardID")
    void deleteDocumentByTimeCardId(@Param("timecardID") String timecardID);
}
