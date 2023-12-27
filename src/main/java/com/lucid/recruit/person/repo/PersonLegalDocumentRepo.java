package com.lucid.recruit.person.repo;

import com.lucid.recruit.person.entity.PersonLegalDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonLegalDocumentRepo extends JpaRepository<PersonLegalDocument,String> {
}
