package com.lucid.recruit.org.repo;

import com.lucid.recruit.org.entity.OrganizationDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgDocRepo extends JpaRepository<OrganizationDocument,String> {
}
