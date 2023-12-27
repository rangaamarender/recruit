package com.lucid.recruit.org.repo;

import com.lucid.recruit.org.dto.OrgListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lucid.recruit.org.entity.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrganizationRepo extends JpaRepository<Organization, String>{

	Organization findByTaxId(String organizationTaxId);

	Page<Organization> findAll(Specification specification, Pageable pageable);

	@Query("SELECT org FROM Organization org JOIN org.orgDomains domains WHERE domains.webDomain=:webDomain AND domains.deleted=false")
	Organization getOrganizationByDomain(@Param("webDomain") String webDomain);



	@Query("SELECT NEW com.lucid.recruit.org.dto.OrgListDTO(o.organizationID, o.name) FROM Organization o ORDER BY o.name ASC")
	List<OrgListDTO> getByName();

	@Query(value = "SELECT status_name, \n" +
			"       COUNT(*) AS record_count\n" +
			"FROM public.ref_orgstatus\n" +
			"GROUP BY status_name;", nativeQuery = true)
	List<Object[]> getOrgByStatus();


	@Query(value="select count(*) from Organization",nativeQuery = true)
	Integer countTotalOrganizations();

	Organization findByNameEqualsIgnoreCase(String name);
}
