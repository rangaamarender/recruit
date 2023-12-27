package com.lucid.recruit.org.repo;

import com.lucid.recruit.org.dto.OrgStatusCountsDTO;
import com.lucid.recruit.org.entity.OrganizationStatus;
import com.lucid.recruit.worker.dto.WorkerStatusCountsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrganizationStatusRepo extends JpaRepository<OrganizationStatus,String> {

    @Query("select new com.lucid.recruit.org.dto.OrgStatusCountsDTO(os.statusCode, count(os)) from OrganizationStatus os where os.effectiveDate= "
            + "(select max(inos.effectiveDate) from OrganizationStatus inos where os.organization=inos.organization) group by os.statusCode")
    List<OrgStatusCountsDTO> countByOrganizationStatus();

    @Query("SELECT orgstatus FROM OrganizationStatus orgstatus WHERE orgstatus.organization.organizationID=:organizationID AND orgstatus.effectiveDate= "
    +"(select max(inos.effectiveDate) from OrganizationStatus inos where orgstatus.organization=inos.organization AND inos.effectiveDate<=:effectiveDate)")
    OrganizationStatus getLatestOrgStatus(@Param("organizationID") String organizationID,@Param("effectiveDate") LocalDate effectiveDate);

}
