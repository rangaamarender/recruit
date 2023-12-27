package com.lucid.recruit.timecard.repo;

import com.lucid.recruit.timecard.entity.TimeCardDocAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeCardDocAttributesRepo extends JpaRepository<TimeCardDocAttributes, String> {
}
