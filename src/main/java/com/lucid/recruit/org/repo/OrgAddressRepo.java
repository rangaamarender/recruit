package com.lucid.recruit.org.repo;

import com.lucid.core.embeddable.GeoCode;
import com.lucid.recruit.org.dto.OrgAddressDTO;
import com.lucid.recruit.org.entity.OrgAddress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrgAddressRepo extends JpaRepository<OrgAddress,String> {
	
	@Transactional
	@Modifying
	@Query(value="UPDATE o_org_addr SET  organizationid=:orgId  WHERE org_address_id=:orgAddrId",nativeQuery = true)
	void updateOrgAddr(@Param("orgId") String orgId,@Param("orgAddrId")String orgAddrId);

    //Optional<OrgAddress> findById(List<OrgAddressDTO> orgAddresses);


	@Query(value = "select * from o_org_addr where address1=:address1 and address2=:address2 and address3=:address3 and "
			+ "address4=:address4 and address5=:address5 and city=:city and state=:state and organizationID=:orgId", nativeQuery = true)
	OrgAddress getOrgAddress(@Param("address1") String address1, @Param("address2") String address2, @Param("address3") String address3,
							 @Param("address4") String address4, @Param("address5") String address5, @Param("city") String city,
							 @Param("state") String state,@Param("orgId") String orgId);


}
