package com.lucid.recruit.org.repo;

import com.lucid.recruit.org.entity.OrgCommunication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrgCommunicationRepo extends JpaRepository<OrgCommunication,String> {
    @Query(value = "select * from o_org_communication where auth_signatory_email=:authSignataryEmail and org_id=:orgId", nativeQuery = true)
    OrgCommunication getOrgCommunication(@Param("authSignataryEmail") String authSignataryEmail,@Param("orgId") String orgId);
}
