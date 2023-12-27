/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.org.service;

import java.util.List;

import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.org.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.lucid.core.base.BaseService;
import com.lucid.recruit.org.entity.OrganizationDocument;

public interface OrganizationService extends BaseService {
	OrganizationDTO createOrganization(OrganizationDTO organizationDTO);

	OrganizationDTO updateOrganization(String organizationID, OrganizationDTO organizationDTO);

	OrganizationDTO getOrganization(String organizationID,boolean history);

	Page<OrganizationDTO> getAllOrganizations(Integer offset, Integer limit, String organizationID, String emailId,
											  String name, OrgStatus orgStatus,String sort);

	OrganizationDocumentDTO uploadOrgDocument(String organizationID, OrganizationDocumentDTO organizationDocumentDTO);

	String uploadOrgDocFile(String organizationDocID, MultipartFile file);

	String uploadOrgLogo(String organizationID, MultipartFile file);

	String deleteDocument(String organizationDocID);

	OrganizationDocumentDTO getOrgDocument(String organizationDocID);

	OrganizationDocument retrieveOrgDocument(String organizationDocID);

	byte[] retrieveOrgDocFile(OrganizationDocument organizationDocument);

	byte[] retrieveOrgLogo(String organizationId);

	OrganizationDTO updateOrgStatus(String organizationID,OrganizationStatusDTO orgStatusDTO);
	OrgCountsDTO getOrgCounts();

}
