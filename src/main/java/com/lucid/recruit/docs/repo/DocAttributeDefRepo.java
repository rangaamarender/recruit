
package com.lucid.recruit.docs.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucid.recruit.docs.entity.DocAttributeDef;
import com.lucid.recruit.docs.entity.DocumentDef;

public interface DocAttributeDefRepo extends JpaRepository<DocAttributeDef, String> {
	List<DocAttributeDef> findByDocumentDef(DocumentDef documentDef);
}
