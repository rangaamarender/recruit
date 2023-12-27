package com.lucid.recruit.referencedata.repo;

import com.lucid.recruit.referencedata.entity.OrgInActiveStatusCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgInActiveStatusCodesRepo extends JpaRepository<OrgInActiveStatusCodes,String>{

}
