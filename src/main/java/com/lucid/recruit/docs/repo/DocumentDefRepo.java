
package com.lucid.recruit.docs.repo;

import java.util.List;
import java.util.Optional;

import com.lucid.recruit.docs.constants.EnumDocDefStatus;
import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lucid.recruit.docs.entity.DocumentDef;
import org.springframework.data.jpa.repository.Query;

public interface DocumentDefRepo extends JpaRepository<DocumentDef, String> {
	Optional<DocumentDef> findByDocumentDefIDAndStatus(String Id, EnumDocDefStatus status);

	Page<DocumentDef> findAll(Specification<DocumentDef> specification, Pageable pageable);

    DocumentDef findByDocumentName(String documentName);

    @Query("SELECT dd FROM DocumentDef dd where dd.documentDefID NOT IN (SELECT distinct dasd.documentDef.documentDefID FROM DefaultDocAssignments dasd WHERE dasd.relatedEnity=:relatedEntity AND dasd.status='ACTIVE')")
    List<DocumentDef> findUnAssignedDocDef(@Parameter(name = "relatedEntity")EnumDocRelatedEntity relatedEntity);
}
