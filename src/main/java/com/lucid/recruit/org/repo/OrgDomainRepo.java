package com.lucid.recruit.org.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lucid.recruit.org.entity.OrgDomain;

public interface OrgDomainRepo extends JpaRepository<OrgDomain,String> {

    @Query(value = "select * from o_domains where domain_name=:domain and domain_ext=:domainExt and org_id=:orgId", nativeQuery = true)
    OrgDomain getOrgDomain(@Param("domain") String domain, @Param("domainExt") String domainExt, @Param("orgId") String orgId);

    @Query("SELECT od FROM OrgDomain od WHERE od.domain =:domain")
    OrgDomain findByWebDomain(@Param("domain")String domain);
    
	@Query("SELECT orgdomain FROM OrgDomain orgdomain WHERE orgdomain.webDomain=:webDomain and orgdomain.deleted=false")
	OrgDomain getActiveOrgDomainByDomain(@Param("webDomain") String webDomain);

    @Query("SELECT orgdomain FROM OrgDomain orgdomain WHERE orgdomain.webDomain = :webDomain AND orgdomain.deleted=false AND"+
    "(:organizationID IS NULL OR orgdomain.organization.organizationID <>:organizationID)")
    OrgDomain findByWebDomain(@Param("organizationID") String organizationID, @Param("webDomain") String webDomain);

}
