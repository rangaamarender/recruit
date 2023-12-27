package com.lucid.core.email.repo;

import com.lucid.core.email.entity.EmailContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EmailContentRepository extends JpaRepository<EmailContent, String> {


    @Query(value = "SELECT emailContentId FROM email_content WHERE status = 'OPEN'", nativeQuery = true)
    List<Long> findMail();

}
