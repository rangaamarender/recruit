package com.lucid.recruit.person.repo;

import com.lucid.recruit.person.entity.PersonDependents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDependentRepo extends JpaRepository<PersonDependents,String> {

    PersonDependents findByPersonLegal_PersonLegalIDAndValidToIsNull(String personLegalID);
}
