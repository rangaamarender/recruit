/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.worker.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.lucid.base.test.BaseTest1;
import com.lucid.core.dto.BasePhoneNbrDTO;
import com.lucid.recruit.org.repo.OrganizationRepo;
import com.lucid.recruit.person.dto.*;
import com.lucid.recruit.person.entity.PersonAddressType;
import com.lucid.recruit.person.entity.RelationshipCode;
import com.lucid.recruit.worker.dto.WorkerSummaryDTO;
import com.lucid.recruit.worker.entity.WorkerTypeCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lucid.core.entity.BasePhoneNbr;
import com.lucid.recruit.org.dto.OrganizationDTO;
import com.lucid.recruit.person.entity.ContactType;
import com.lucid.recruit.referencedata.dto.JobDTO;
import com.lucid.recruit.referencedata.dto.RelatedWorkerTypeDTO;
import com.lucid.recruit.referencedata.dto.WorkerTypeDTO;
import com.lucid.recruit.worker.dto.WorkAssignmentDTO;
import com.lucid.recruit.worker.dto.WorkerDTO;
import com.lucid.recruit.worker.dto.WorkerStatusDTO;
import com.lucid.recruit.worker.entity.WorkerStatusCode;
import org.springframework.data.domain.Page;


/**
 * 
 * @author chandu
 *
 */

public class TestWorkerService extends BaseTest1 {


	@Autowired
	private WorkerService workerService;

	@Autowired
	private OrganizationRepo organizationRepo;


	/**
	 * Create a new <code>TestWorkerService</code>
	 */
	public TestWorkerService() {
		super();
	}


	/*@Test
	public void testCreateWorkerHR() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbrDTO("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		woker = workerService.createWorker(woker);

		assertNotNull(woker.getWorkerID());
	}

	@Test
	public void testCreateWorkerWithDuplicateMail(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		workerService.createWorker(woker);


		//test duplicate email check
		try {
			woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
					workerStatusList, workerAssigmentList);
			woker = workerService.createWorker(woker);
		} catch (Exception e){
			assertEquals("Given mailId: joew.test@mail.com already associated with another worker",e.getLocalizedMessage());
		}

	}

	@Test
	public void testCreateWorkerWithStatusProbation() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				LocalDate.now().minusDays(4), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		try {
			worker = workerService.createWorker(worker);
		} catch (Exception e){
			assertEquals("Worker cannot start with status "+WorkerStatusCode.ACTIVE,e.getLocalizedMessage());
		}
	}

	@Test
	public void testCreateWorkerWithInvalidWorkerType(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);


		//test duplicate email check
		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		try {
			woker = workerService.createWorker(woker);
		} catch (Exception e){
			assertEquals("Worker Type by Id ABC not found",e.getLocalizedMessage());
		}

	}

	@Test
	public void testCreateWorkerWithInvalidOrganization(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("ABC");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);


		//test duplicate email check
		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		try {
			woker = workerService.createWorker(woker);
		} catch (Exception e){
			assertEquals("Organization by Id ABC not found",e.getLocalizedMessage());
		}

	}

	@Test
	public void testCreateWorkerWithInvalidJob(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("ABC"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);


		//test duplicate email check
		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		try {
			woker = workerService.createWorker(woker);
		} catch (Exception e){
			assertEquals("Job by Id ABC not found",e.getLocalizedMessage());
		}

	}

	@Test
	public  void testGetWorkerById(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		woker = workerService.createWorker(woker);

		WorkerDTO existedWorker = workerService.retriveWorkerById(woker.getWorkerID(),false);

		assertNotNull(existedWorker);
	}

	@Test
	public void testGetWorkerByInvalidId(){
		try {
			WorkerDTO existedWorker = workerService.retriveWorkerById("123",false);
		} catch (Exception e){
			assertEquals("Worker by Id 123 not found",e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerContactDetails(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.testmail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		String contactId = worker.getPersonLegal().getPersonContactDetails().get(0).getPersonLegalContactID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		prsContact = new PersonContactDetailsDTO(contactId, ContactType.PRIMARY,
				new BasePhoneNbr("0000000001"), "joewx.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);
		contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		personLegal.setPersonContactDetails(contactList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		workerService.updateWorker(workerId,worker);

		worker = workerService.retriveWorkerById(workerId,false);

		List<PersonContactDetailsDTO> personContactDetailsDTOList = worker.getPersonLegal().getPersonContactDetails();

		PersonContactDetailsDTO personContactDetailsDTO = personContactDetailsDTOList.get(0);

		assertEquals("0000000001",personContactDetailsDTO.getPhoneNumber().getDialNumber());
		assertEquals("joewx.test@mail.com",personContactDetailsDTO.getEmailId());

	}

	@Test
	public void testUpdateWorkerNewContactDetails(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000001"), "joewx.test@mail.com",
				LocalDate.now(), null);
		contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		personLegal.setPersonContactDetails(contactList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		workerService.updateWorker(workerId,worker);

		worker = workerService.retriveWorkerById(workerId,false);

		List<PersonContactDetailsDTO> personContactDetailsDTOList = worker.getPersonLegal().getPersonContactDetails();

		assertEquals(2,personContactDetailsDTOList.size());

		PersonContactDetailsDTO personContactDetailsDTO = personContactDetailsDTOList.get(1);
		assertEquals("0000000001",personContactDetailsDTO.getPhoneNumber().getDialNumber());
		assertEquals("joewx.test@mail.com",personContactDetailsDTO.getEmailId());
		assertNull(personContactDetailsDTO.getEndDate());

		personContactDetailsDTO = personContactDetailsDTOList.get(0);
		assertEquals("0000000000",personContactDetailsDTO.getPhoneNumber().getDialNumber());
		assertEquals("joew.test@mail.com",personContactDetailsDTO.getEmailId());
		assertEquals(LocalDate.now().minusDays(1),personContactDetailsDTO.getEndDate());

	}

	@Test
	public void testUpdateWorkerNewContactDetailsHaveSameStartDateOfOld(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(1), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());
		prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000001"), "joewx.test@mail.com",
				LocalDate.now().minusDays(1), null);
		contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		personLegal.setPersonContactDetails(contactList);

		WorkerDTO worker1 = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		try {
			workerService.updateWorker(worker.getWorkerID(), worker1);
		} catch (Exception e){
			String personContactID = worker.getPersonLegal().getPersonContactDetails().get(0).getPersonLegalContactID();
			assertEquals("Person Contact Details for contact type Personal have same start dates, Start Date: "+LocalDate.now().minusDays(1)+" for person contact details id null and for person contact details id "+personContactID,e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerContactDetailsWithInvalidID() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		prsContact = new PersonContactDetailsDTO("123", ContactType.PRIMARY,
				new BasePhoneNbr("0000000001"), "joewx.test@mail.com",
				LocalDate.now(), null);
		contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		personLegal.setPersonContactDetails(contactList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		try {
			workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("Person Contact Details by Id 123 not found",e.getLocalizedMessage());
		}
	}


	@Test
	public void testUpdateWorkerType(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();

		worker = new WorkerDTO(null, null, new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_C2C), null, null,
				null, null);

		worker = workerService.updateWorker(workerId, worker);

		assertEquals("C2C",worker.getWorkerType().getWorkerTypeCode());

	}

	@Test
	public void testUpdateWorkerTypeWithInValidWorkerType(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();

		worker = new WorkerDTO(null, null, new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), null, null,
				null, null);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("Worker Type by Id ABC not found",e.getLocalizedMessage());
		}

	}

	@Test
	public void testUpdateWorkerOrganization(){

		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();

		org = new OrganizationDTO("SSCONST");

		worker = new WorkerDTO(null, null, null, null, org,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		assertEquals("SSCONST",worker.getOrganization().getOrganizationID());

	}

	@Test
	public void testUpdateWorkerOrganizationWithInvalidOrgID(){

		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();

		org = new OrganizationDTO("ABC");

		worker = new WorkerDTO(null, null, null, null, org,
				null, null);
		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e) {
           assertEquals("Organization by Id ABC not found",e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerStatusEffectiveDate() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				LocalDate.now().minusDays(4), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);


		String workerId = worker.getWorkerID();
		//update workerEffectiveDate
		workerStatus = new WorkerStatusDTO(worker.getWorkerStatus().get(0).getWorkerStatusId(),WorkerStatusCode.ACTIVE,
				LocalDate.now().minusDays(2), "Joining","Joined now");

		workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);

		worker = new WorkerDTO(null, null, null, null, null,
				workerStatusList, null);

		worker = workerService.updateWorker(workerId,worker);

		assertEquals(LocalDate.now().minusDays(2),worker.getWorkerStatus().get(0).getEffectiveDate());

	}

	@Test
	public void testUpdateWorkerStatusWithInvalidStatusId() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				LocalDate.now().minusDays(4), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);


		String workerId = worker.getWorkerID();
		//update workerEffectiveDate
		workerStatus = new WorkerStatusDTO("ABC",WorkerStatusCode.ACTIVE,
				LocalDate.now().minusDays(2), "Joining","Joined now");

		workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);

		worker = new WorkerDTO(null, null, null, null, null,
				workerStatusList, null);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e) {
            assertEquals("WorkerStatus by Id ABC not found",e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerStatus() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.INACTIVE,
				LocalDate.now().minusDays(4), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);


		String workerId = worker.getWorkerID();
		//update workerStatus
		workerStatus = new WorkerStatusDTO(null,WorkerStatusCode.INACTIVE,
				LocalDate.now().minusDays(2), "Joining","Joined now");

		workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);

		worker = new WorkerDTO(null, null, null, null, null,
				workerStatusList, null);

		worker = workerService.updateWorker(workerId,worker);


		assertEquals(LocalDate.now().minusDays(2),worker.getWorkerStatus().get(1).getEffectiveDate());
		assertEquals(WorkerStatusCode.INACTIVE,worker.getWorkerStatus().get(1).getStatus());

	}

	@Test
	public void testUpdateWorkerStatusWithSameDates() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.INACTIVE,
				LocalDate.now().minusDays(2), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);


		String workerId = worker.getWorkerID();
		//update workerStatus
		workerStatus = new WorkerStatusDTO(null,WorkerStatusCode.INACTIVE,
				LocalDate.now().minusDays(2), "Joining","Joined now");

		workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);

		worker = new WorkerDTO(null, null, null, null, null,
				workerStatusList, null);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("Worker status have matching date Status: WORKER_PENDING Date: "+LocalDate.now().minusDays(2)+" Status: WORKER_PROBATION Date: "+LocalDate.now().minusDays(2),e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerStatusWithSameStatus() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.INACTIVE,
				LocalDate.now().minusDays(2), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);


		String workerId = worker.getWorkerID();
		//update workerStatus
		workerStatus = new WorkerStatusDTO(null,WorkerStatusCode.INACTIVE,
				LocalDate.now(), "Joining","Joined now");

		workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);

		worker = new WorkerDTO(null, null, null, null, null,
				workerStatusList, null);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("Invalid status transition from "+WorkerStatusCode.INACTIVE+" To "+WorkerStatusCode.INACTIVE,e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerStatusWithOutPassingReason(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.INACTIVE,
				LocalDate.now().minusDays(2), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);


		String workerId = worker.getWorkerID();
		//update workerStatus
		workerStatus = new WorkerStatusDTO(null,WorkerStatusCode.INACTIVE,
				LocalDate.now(), null,"Joine");

		workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);

		worker = new WorkerDTO(null, null, null, null, null,
				workerStatusList, null);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("reasonCode: must not be null",e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerWorkAssignmentJob() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				LocalDate.now().minusDays(2), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();

		wrkAssignmentDto = new WorkAssignmentDTO(worker.getWorkAssignment().get(0).getWorkerAssgmtId(), new JobDTO("SRDEV"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		worker = new WorkerDTO(null, null, null, null, null,
				null, workerAssigmentList);

		worker = workerService.updateWorker(workerId,worker);

		assertEquals("SRDEV",worker.getWorkAssignment().get(0).getJob().getJobID());

	}

	@Test
	public void testUpdateWorkerWorkAssignmentJobWithInvalidAssignmentId() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				LocalDate.now().minusDays(2), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();

		wrkAssignmentDto = new WorkAssignmentDTO("ABC", new JobDTO("SRDEV"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		worker = new WorkerDTO(null, null, null, null, null,
				null, workerAssigmentList);
		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("WorkAssignment by Id ABC not found",e.getLocalizedMessage());
		}

	}

	@Test
	public void testUpdateWorkerWorkAssignmentWithInvalidJob() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.INACTIVE,
				LocalDate.now().minusDays(2), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();

		wrkAssignmentDto = new WorkAssignmentDTO(worker.getWorkAssignment().get(0).getWorkerAssgmtId(), new JobDTO("ABC"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		worker = new WorkerDTO(null, null, null, null, null,
				null, workerAssigmentList);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e) {
			assertEquals("Job by Id ABC not found",e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerNewWorkAssignmentWithInvalidJob() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.INACTIVE,
				LocalDate.now().minusDays(2), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();

		wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("ABC"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		worker = new WorkerDTO(null, null, null, null, null,
				null, workerAssigmentList);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e) {
			assertEquals("Job by Id ABC not found",e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerNewWorkAssignmentWithSameDates() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				LocalDate.now().minusDays(2), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		String workAssignmentId = worker.getWorkAssignment().get(0).getWorkerAssgmtId();

		wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("SRDEV"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		worker = new WorkerDTO(null, null, null, null, null,
				null, workerAssigmentList);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e) {
			assertEquals("Work Assignemt have same start dates Start Date: "+LocalDate.now()+" for work assigment ID null and for work assigment ID "+workAssignmentId,e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerNewAddressDetails() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonAddressDTO personAddress = new PersonAddressDTO();
		personAddress.setAddressType(PersonAddressType.Personal);
		personAddress.setStartDate(LocalDate.now());
		personAddress.setAddressName("Home");
		personAddress.setAddress1("TestAddress1");
		personAddress.setCity("Hydrabad");
		personAddress.setState("Telangana");
		personAddress.setPostalCode("500089");
		personAddress.setCountryCode("IND");


		List<PersonAddressDTO> personAddressDTOS = new ArrayList<>();
		personAddressDTOS.add(personAddress);
		//set person contact details as null because here we are just updating new address
		personLegal.setPersonContactDetails(null);
		personLegal.setPersonAddress(personAddressDTOS);


		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		workerService.updateWorker(workerId, worker);
		worker = workerService.retriveWorkerById(workerId,false);

		assertEquals(1,worker.getPersonLegal().getPersonAddress().size());
	}

	@Test
	public void testUpdateWorkerNewAddressWithSameStartDate(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonAddressDTO personAddress = new PersonAddressDTO();
		personAddress.setAddressType(PersonAddressType.Personal);
		personAddress.setStartDate(LocalDate.now());
		personAddress.setAddressName("Home");
		personAddress.setAddress1("TestAddress1");
		personAddress.setCity("Hydrabad");
		personAddress.setState("Telangana");
		personAddress.setPostalCode("500089");
		personAddress.setCountryCode("IND");


		List<PersonAddressDTO> personAddressDTOS = new ArrayList<>();
		personAddressDTOS.add(personAddress);
		//set person contact details as null because here we are just updating new address
		personLegal.setPersonContactDetails(null);
		personLegal.setPersonAddress(personAddressDTOS);


		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker=workerService.updateWorker(workerId, worker);

		List<PersonAddressDTO> personAddressDTOS1 = worker.getPersonLegal().getPersonAddress();

		personAddress = new PersonAddressDTO();
		personAddress.setAddressType(PersonAddressType.Personal);
		personAddress.setStartDate(LocalDate.now());
		personAddress.setAddressName("Home");
		personAddress.setAddress1("TestAddress1");
		personAddress.setCity("Hydrabad");
		personAddress.setState("Telangana");
		personAddress.setPostalCode("500089");
		personAddress.setCountryCode("IND");


		personAddressDTOS = new ArrayList<>();
		personAddressDTOS.add(personAddress);
		personLegal.setPersonAddress(personAddressDTOS);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		try {
			workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("Person Address have same start dates Start Date: "+LocalDate.now()+" for Address ID "+personAddressDTOS1.get(0).getPersonAddressID()+" and address ID null",e.getLocalizedMessage());
		}

	}

	@Test
	public void testUpdateWorkerExistedAddress(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonAddressDTO personAddress = new PersonAddressDTO();
		personAddress.setAddressType(PersonAddressType.Personal);
		personAddress.setStartDate(LocalDate.now());
		personAddress.setAddressName("Home");
		personAddress.setAddress1("TestAddress1");
		personAddress.setCity("Hydrabad");
		personAddress.setState("Telangana");
		personAddress.setPostalCode("500089");
		personAddress.setCountryCode("IND");


		List<PersonAddressDTO> personAddressDTOS = new ArrayList<>();
		personAddressDTOS.add(personAddress);
		//set person contact details as null because here we are just updating new address
		personLegal.setPersonContactDetails(null);
		personLegal.setPersonAddress(personAddressDTOS);


		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker=workerService.updateWorker(workerId, worker);

		List<PersonAddressDTO> personAddressDTOS1 = worker.getPersonLegal().getPersonAddress();

		personAddress = new PersonAddressDTO();
		personAddress.setPersonAddressID(personAddressDTOS1.get(0).getPersonAddressID());
		personAddress.setAddressType(PersonAddressType.Business);
		personAddress.setStartDate(LocalDate.now());
		personAddress.setAddressName("Home");
		personAddress.setAddress1("TestAddress1");
		personAddress.setCity("Hydrabad");
		personAddress.setState("Telangana");
		personAddress.setPostalCode("500089");
		personAddress.setCountryCode("IND");


		personAddressDTOS = new ArrayList<>();
		personAddressDTOS.add(personAddress);
		personLegal.setPersonAddress(personAddressDTOS);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		worker = workerService.updateWorker(workerId, worker);

		personAddressDTOS1 = worker.getPersonLegal().getPersonAddress();

		assertEquals(PersonAddressType.Business,personAddressDTOS1.get(0).getAddressType());

	}

	@Test
	public void testUpdateWorkerExistedAddressWithInvaliId(){
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonAddressDTO personAddress = new PersonAddressDTO();
		personAddress.setAddressType(PersonAddressType.Personal);
		personAddress.setStartDate(LocalDate.now());
		personAddress.setAddressName("Home");
		personAddress.setAddress1("TestAddress1");
		personAddress.setCity("Hydrabad");
		personAddress.setState("Telangana");
		personAddress.setPostalCode("500089");
		personAddress.setCountryCode("IND");


		List<PersonAddressDTO> personAddressDTOS = new ArrayList<>();
		personAddressDTOS.add(personAddress);
		//set person contact details as null because here we are just updating new address
		personLegal.setPersonContactDetails(null);
		personLegal.setPersonAddress(personAddressDTOS);


		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker=workerService.updateWorker(workerId, worker);

		List<PersonAddressDTO> personAddressDTOS1 = worker.getPersonLegal().getPersonAddress();

		personAddress = new PersonAddressDTO();
		personAddress.setPersonAddressID("ABC");
		personAddress.setAddressType(PersonAddressType.Business);
		personAddress.setStartDate(LocalDate.now());
		personAddress.setAddressName("Home");
		personAddress.setAddress1("TestAddress1");
		personAddress.setCity("Hydrabad");
		personAddress.setState("Telangana");
		personAddress.setPostalCode("500089");
		personAddress.setCountryCode("IND");


		personAddressDTOS = new ArrayList<>();
		personAddressDTOS.add(personAddress);
		personLegal.setPersonAddress(personAddressDTOS);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e){
           assertEquals("Person Address by Id ABC not found",e.getLocalizedMessage());
		}
	}


	@Test
	public void testUpdateWorkerNewBankDetails() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonBankDetailsDTO personBankDetailsDTO = new PersonBankDetailsDTO(null,"TestName","0000000000","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now());
		List<PersonBankDetailsDTO> bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);
		personLegal.setPersonContactDetails(null);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		assertEquals(1,worker.getPersonLegal().getPersonContactDetails().size());

	}

	@Test
	public void testUpdateWorkerBankDetailsAccNo() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonBankDetailsDTO personBankDetailsDTO = new PersonBankDetailsDTO(null,"TestName","0000000000","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now());
		List<PersonBankDetailsDTO> bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);
		personLegal.setPersonContactDetails(null);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		personBankDetailsDTO = new PersonBankDetailsDTO(worker.getPersonLegal().getPersonBankDetails().get(0).getPersonBankDetailsID(),"TestName","0000000001","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now());
		bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		assertEquals("0000000001",worker.getPersonLegal().getPersonBankDetails().get(0).getAccountNumber());
	}

	@Test
	public void testUpdateWorkerNewBankDetailsWithExpirngExisting() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonBankDetailsDTO personBankDetailsDTO = new PersonBankDetailsDTO(null,"TestName","0000000000","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now());
		List<PersonBankDetailsDTO> bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);
		personLegal.setPersonContactDetails(null);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		personBankDetailsDTO = new PersonBankDetailsDTO(null,"TestName","0000000001","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now().plusDays(10));
		bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		worker.getPersonLegal().getPersonBankDetails().get(0).getPersonBankDetailsID();

		assertEquals(2,worker.getPersonLegal().getPersonBankDetails().size());

		assertEquals(LocalDate.now().plusDays(9),worker.getPersonLegal().getPersonBankDetails().get(0).getEndDate());
	}

	@Test
	public void testUpdateWorkerNewBankDetailsWithSameValidFromDates() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonBankDetailsDTO personBankDetailsDTO = new PersonBankDetailsDTO(null,"TestName","0000000000","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now());
		List<PersonBankDetailsDTO> bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);
		personLegal.setPersonContactDetails(null);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		String personBankDetailsId = worker.getPersonLegal().getPersonBankDetails().get(0).getPersonBankDetailsID();

		personBankDetailsDTO = new PersonBankDetailsDTO(null,"TestName","0000000001","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now());
		bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("Person BankDetails have same validFrom date : "+LocalDate.now()+" for bankDetailsId: null and for bankDetailsId: "+personBankDetailsId,e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateWorkerExistedBankDetailsWithInvalidId() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());

		PersonBankDetailsDTO personBankDetailsDTO = new PersonBankDetailsDTO(null,"TestName","0000000000","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now());
		List<PersonBankDetailsDTO> bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);
		personLegal.setPersonContactDetails(null);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		String personBankDetailsId = worker.getPersonLegal().getPersonBankDetails().get(0).getPersonBankDetailsID();

		personBankDetailsDTO = new PersonBankDetailsDTO("ABC","TestName","0000000001","TestBank","TestBranch","ABC","123456","Savings",LocalDate.now());
		bankDetailsDTOS = new ArrayList<>();
		bankDetailsDTOS.add(personBankDetailsDTO);
		personLegal.setPersonBankDetails(bankDetailsDTOS);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		try {
			worker = workerService.updateWorker(workerId, worker);
		} catch (Exception e){
			assertEquals("PersonBankDetails by id :ABC not found to update",e.getLocalizedMessage());
		}
	}


	//emergency contact test
	@Test
	public void testUpdateWorkerNewEmergencyContact() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());
		personLegal.setPersonContactDetails(null);

		BasePhoneNbr basePhoneNbr = new BasePhoneNbr();
		basePhoneNbr.setPhoneExtension("+91");
		basePhoneNbr.setDialNumber("0000000000");
		basePhoneNbr.setCountryDialingCode("+91");

		PersonEmergencyContactDTO personEmergencyContactDTO = new PersonEmergencyContactDTO(null,LocalDate.now(),null,"TestName","TestLastName", RelationshipCode.Mother,"Test","Test@gamil.com","Test",basePhoneNbr);

		BasePhoneNbr basePhoneNbr1 = new BasePhoneNbr();
		basePhoneNbr.setPhoneExtension("+91");
		basePhoneNbr.setDialNumber("0000000001");
		basePhoneNbr.setCountryDialingCode("+91");

		PersonEmergencyContactDTO personEmergencyContactDTO1 = new PersonEmergencyContactDTO(null,LocalDate.now(),null,"TestName1","TestLastName", RelationshipCode.Father,"Test","Test@gamil.com","Test",basePhoneNbr1);

		List<PersonEmergencyContactDTO> personEmergencyContactDTOList = new ArrayList<>();
		personEmergencyContactDTOList.add(personEmergencyContactDTO);
		personEmergencyContactDTOList.add(personEmergencyContactDTO1);
		personLegal.setPersonEmergencyContact(personEmergencyContactDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		assertEquals(2,worker.getPersonLegal().getPersonEmergencyContact().size());


	}

	@Test
	public void testUpdateWorkerEmergencyContactData() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());
		personLegal.setPersonContactDetails(null);

		BasePhoneNbr basePhoneNbr = new BasePhoneNbr();
		basePhoneNbr.setPhoneExtension("+91");
		basePhoneNbr.setDialNumber("0000000000");
		basePhoneNbr.setCountryDialingCode("+91");

		PersonEmergencyContactDTO personEmergencyContactDTO = new PersonEmergencyContactDTO(null,LocalDate.now(),null,"TestName","TestLastName", RelationshipCode.Mother,"Test","Test@gamil.com","Test",basePhoneNbr);


		List<PersonEmergencyContactDTO> personEmergencyContactDTOList = new ArrayList<>();
		personEmergencyContactDTOList.add(personEmergencyContactDTO);
		personLegal.setPersonEmergencyContact(personEmergencyContactDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		basePhoneNbr = new BasePhoneNbr();
		basePhoneNbr.setPhoneExtension("+91");
		basePhoneNbr.setDialNumber("0000000001");
		basePhoneNbr.setCountryDialingCode("+91");

		List<PersonEmergencyContactDTO> personEmergencyContactDTOS = worker.getPersonLegal().getPersonEmergencyContact();
		personEmergencyContactDTO = new PersonEmergencyContactDTO(personEmergencyContactDTOS.get(0).getPersonEmergContactId(),LocalDate.now(),null,"TestName1","TestLastName", RelationshipCode.Father,"Test","Test@gamil.com","Test",basePhoneNbr);

		personEmergencyContactDTOList = new ArrayList<>();
		personEmergencyContactDTOList.add(personEmergencyContactDTO);
		personLegal.setPersonEmergencyContact(personEmergencyContactDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		worker = workerService.updateWorker(workerId,worker);

		personEmergencyContactDTOS = worker.getPersonLegal().getPersonEmergencyContact();

		assertEquals("TestName1",personEmergencyContactDTOS.get(0).getFirstName());
		assertEquals("0000000001",personEmergencyContactDTOS.get(0).getPhoneNumber().getDialNumber());
	}

	@Test
	public void testUpdateWorkerEmergencyContactDataWithInvalidID() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());
		personLegal.setPersonContactDetails(null);

		BasePhoneNbr basePhoneNbr = new BasePhoneNbr();
		basePhoneNbr.setPhoneExtension("+91");
		basePhoneNbr.setDialNumber("0000000000");
		basePhoneNbr.setCountryDialingCode("+91");

		PersonEmergencyContactDTO personEmergencyContactDTO = new PersonEmergencyContactDTO(null,LocalDate.now(),null,"TestName","TestLastName", RelationshipCode.Mother,"Test","Test@gamil.com","Test",basePhoneNbr);


		List<PersonEmergencyContactDTO> personEmergencyContactDTOList = new ArrayList<>();
		personEmergencyContactDTOList.add(personEmergencyContactDTO);
		personLegal.setPersonEmergencyContact(personEmergencyContactDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		basePhoneNbr = new BasePhoneNbr();
		basePhoneNbr.setPhoneExtension("+91");
		basePhoneNbr.setDialNumber("0000000001");
		basePhoneNbr.setCountryDialingCode("+91");

		List<PersonEmergencyContactDTO> personEmergencyContactDTOS = worker.getPersonLegal().getPersonEmergencyContact();
		personEmergencyContactDTO = new PersonEmergencyContactDTO("ABC",LocalDate.now(),null,"TestName1","TestLastName", RelationshipCode.Father,"Test","Test@gamil.com","Test",basePhoneNbr);

		personEmergencyContactDTOList = new ArrayList<>();
		personEmergencyContactDTOList.add(personEmergencyContactDTO);
		personLegal.setPersonEmergencyContact(personEmergencyContactDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);

		try {
			worker = workerService.updateWorker(workerId, worker);
		}
		catch (Exception e){
            assertEquals("PersonEmergencyContact by id:ABC not found",e.getLocalizedMessage());
		}
	}

	//dependent contact test
	@Test
	public void testUpdateWorkerNewDependantContact() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());
		personLegal.setPersonContactDetails(null);


		PersonDependentsDTO personDependentsDTO = new PersonDependentsDTO(null,"FirstName","LasteName",RelationshipCode.Father,LocalDate.ofYearDay(1978,10),LocalDate.now(),null);


		PersonDependentsDTO personDependentsDTO1 = new PersonDependentsDTO(null,"FirstName1","LasteName",RelationshipCode.Mother,LocalDate.ofYearDay(1984,10),LocalDate.now(),null);

		List<PersonDependentsDTO> personDependentsDTOList = new ArrayList<>();
		personDependentsDTOList.add(personDependentsDTO);
		personDependentsDTOList.add(personDependentsDTO1);
		personLegal.setPersonDependents(personDependentsDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);

		assertEquals(2,worker.getPersonLegal().getPersonDependents().size());


	}

	@Test
	public void testUpdateWorkerDependantContactData() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());
		personLegal.setPersonContactDetails(null);

		PersonDependentsDTO personDependentsDTO = new PersonDependentsDTO(null,"FirstName1","LasteName",RelationshipCode.Mother,LocalDate.ofYearDay(1984,10),LocalDate.now(),null);

		List<PersonDependentsDTO> personDependentsDTOList = new ArrayList<>();
		personDependentsDTOList.add(personDependentsDTO);
		personLegal.setPersonDependents(personDependentsDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);


		List<PersonDependentsDTO> personDependentsDTOS = worker.getPersonLegal().getPersonDependents();

		personDependentsDTO = new PersonDependentsDTO(personDependentsDTOS.get(0).getPersonDepId(),"FirstName2","LasteName",RelationshipCode.Mother,LocalDate.ofYearDay(1984,10),LocalDate.now(),null);

		personDependentsDTOList = new ArrayList<>();
		personDependentsDTOList.add(personDependentsDTO);
		personLegal.setPersonDependents(personDependentsDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);


		personDependentsDTOS = worker.getPersonLegal().getPersonDependents();

		assertEquals("FirstName2",personDependentsDTOS.get(0).getFirstName());
	}

	@Test
	public void testUpdateWorkerDependentsDataWithInvalidID() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				LocalDate.now().minusDays(5), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO worker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		worker = workerService.createWorker(worker);

		String workerId = worker.getWorkerID();
		//set personLegalId
		personLegal.setPersonLegalID(worker.getPersonLegal().getPersonLegalID());
		personLegal.setPersonContactDetails(null);

		PersonDependentsDTO personDependentsDTO = new PersonDependentsDTO(null,"FirstName1","LasteName",RelationshipCode.Mother,LocalDate.ofYearDay(1984,10),LocalDate.now(),null);

		List<PersonDependentsDTO> personDependentsDTOList = new ArrayList<>();
		personDependentsDTOList.add(personDependentsDTO);
		personLegal.setPersonDependents(personDependentsDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		worker = workerService.updateWorker(workerId,worker);


		List<PersonDependentsDTO> personDependentsDTOS = worker.getPersonLegal().getPersonDependents();

		personDependentsDTO = new PersonDependentsDTO("ABC","FirstName2","LasteName",RelationshipCode.Mother,LocalDate.ofYearDay(1984,10),LocalDate.now(),null);

		personDependentsDTOList = new ArrayList<>();
		personDependentsDTOList.add(personDependentsDTO);
		personLegal.setPersonDependents(personDependentsDTOList);

		worker = new WorkerDTO(null, null, null, personLegal, null,
				null, null);
		try {
			worker = workerService.updateWorker(workerId, worker);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testFetchWokersSummary() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		woker = workerService.createWorker(woker);

		Page<WorkerSummaryDTO>  workerSummaryDTOPage = workerService.retrieveAllWorkers(0,0,null,null,null,null,null,null,null,null,null);

		assertEquals(1,workerSummaryDTOPage.getContent().size());
	}

	@Test
	public void testFetchWokersSummaryWithName() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		woker = workerService.createWorker(woker);

		Page<WorkerSummaryDTO>  workerSummaryDTOPage = workerService.retrieveAllWorkers(0,0,null,null,null,null,"Abc",null,null,null,null);

		assertEquals(0,workerSummaryDTOPage.getContent().size());
	}

	@Test
	public void testFetchWokersSummaryWithMail() {
		PersonContactDetailsDTO prsContact = new PersonContactDetailsDTO(null, ContactType.PRIMARY,
				new BasePhoneNbr("0000000000"), "joew.test@mail.com",
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);

		List<PersonContactDetailsDTO> contactList = new ArrayList<PersonContactDetailsDTO>();
		contactList.add(prsContact);
		PersonLegalDTO personLegal = new PersonLegalDTO("Joe", "W", "Test", contactList);

		WorkerStatusDTO workerStatus = new WorkerStatusDTO(WorkerStatusCode.ACTIVE,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "Joining",
				"Joined now");
		List<WorkerStatusDTO> workerStatusList = new ArrayList<WorkerStatusDTO>();
		workerStatusList.add(workerStatus);
		OrganizationDTO org = new OrganizationDTO("70MM");

		WorkAssignmentDTO wrkAssignmentDto = new WorkAssignmentDTO(null, new JobDTO("DEVARCH"),
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				FAKER.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null,
				null);
		List<WorkAssignmentDTO> workerAssigmentList = new ArrayList<WorkAssignmentDTO>();
		workerAssigmentList.add(wrkAssignmentDto);

		WorkerDTO woker = new WorkerDTO(null, "EMP001", new RelatedWorkerTypeDTO(WorkerTypeCode.WORKER_W2), personLegal, org,
				workerStatusList, workerAssigmentList);
		woker = workerService.createWorker(woker);

		Page<WorkerSummaryDTO>  workerSummaryDTOPage = workerService.retrieveAllWorkers(0,0,null,null,null,"test@gmail.com",null,null,null,null,null);

		assertEquals(0,workerSummaryDTOPage.getContent().size());
	}
*/
}

