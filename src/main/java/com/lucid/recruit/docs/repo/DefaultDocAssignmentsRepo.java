package com.lucid.recruit.docs.repo;

import java.util.List;
import java.util.Optional;

import com.lucid.recruit.docs.constants.EnumDefaultDocStatus;
import com.lucid.recruit.docs.dto.DocumentDefDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lucid.recruit.docs.constants.EnumDocRelatedEntity;
import com.lucid.recruit.docs.entity.DefaultDocAssignments;

public interface DefaultDocAssignmentsRepo extends JpaRepository<DefaultDocAssignments, String> {

	@Query("select distinct da from DefaultDocAssignments da where "
			+ "((da.relatedEnity=:relatedEnity and da.relatedSubEnity is null) or "
			+ " (da.relatedEnity=:relatedEnity and da.relatedSubEnity is not null and da.relatedSubEnity=:subEntity))")
	List<DefaultDocAssignments> findByRelatedEnityAndSubEntity(@Param("relatedEnity") EnumDocRelatedEntity relatedEnity,
															   @Param("subEntity") String subEntity);

	@Query("select distinct da from DefaultDocAssignments da where "
			+ "((da.relatedEnity=:relatedEnity and da.relatedSubEnity is null) or "
			+ " (da.relatedEnity=:relatedEnity and da.relatedSubEnity is not null and da.relatedSubEnity=:subEntity)) and da.autoAssigned=true and da.status='ACTIVE'")
	List<DefaultDocAssignments> findAutoAssignedDoc(@Param("relatedEnity") EnumDocRelatedEntity relatedEnity,
													@Param("subEntity") String subEntity);

	Page<DefaultDocAssignments> findAll(Specification<?> specification, Pageable pageable);

	@Query("select da from DefaultDocAssignments da where da.relatedEnity=:relatedEntity"
			+ " and da.documentDef.documentDefID =:documentDefID AND da.status='ACTIVE'")
	List<DefaultDocAssignments> findDefaultDocAssignments(@Param("relatedEntity") EnumDocRelatedEntity relatedEntity,
														  @Param("documentDefID") String documentDefID);

	List<DefaultDocAssignments> findAll(Specification specification);

	@Query("SELECT dda FROM DefaultDocAssignments dda where dda.documentDef.documentDefID=:docDefId AND dda.relatedEnity=:relatedEntity AND dda.status='ACTIVE'")
	Optional<DefaultDocAssignments> findByDocDefAndRelatedEntity(@Parameter(name="docDefId") String docDefId,@Parameter(name = "relatedEntity") EnumDocRelatedEntity relatedEntity);

	@Query("SELECT dda FROM DefaultDocAssignments dda where dda.documentDef.documentDefID=:docDefId AND dda.relatedEnity=:relatedEntity")
	Optional<DefaultDocAssignments> findAllByDocDefAndRelatedEntity(@Parameter(name="docDefId") String docDefId,@Parameter(name = "relatedEntity") EnumDocRelatedEntity relatedEntity);

	@Query("SELECT dda FROM DefaultDocAssignments dda where dda.documentDef.documentDefID=:docDefId AND dda.status='ACTIVE'")
	List<DefaultDocAssignments> findActiveDefaultDocDef(@Parameter(name="docDefId")String docDefId);

}
