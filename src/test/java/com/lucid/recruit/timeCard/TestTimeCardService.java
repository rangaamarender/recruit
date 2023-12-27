package com.lucid.recruit.timeCard;

import org.springframework.beans.factory.annotation.Autowired;

import com.lucid.base.test.BaseTest1;
import com.lucid.recruit.timecard.service.TimeCardService;

public class TestTimeCardService extends BaseTest1 {
    @Autowired
    private TimeCardService timeCardService;

   /* @Test
    public void testCreateTimeCard() {

        TimeCardDTO timeCardDTO = new TimeCardDTO();

        RelatedWorkerDTO relatedWorkerDTO = new RelatedWorkerDTO();
        relatedWorkerDTO.setWorkerID("W001");
        timeCardDTO.setWorker(relatedWorkerDTO);

        RelatedWorkOrderDTO relatedWorkOrderDTO = new RelatedWorkOrderDTO();
        relatedWorkOrderDTO.setWorkOrderID("W0001");
        timeCardDTO.set.setWorkOrder(relatedWorkOrderDTO);

        timeCardDTO.setStartDate(LocalDate.of(2023, 1, 21));
        timeCardDTO.setEndDate(LocalDate.of(2023, 1, 25));

        timeCardDTO.setTotalRegularTime(40.00);
        timeCardDTO.setTotalOverTime(10.00);
        timeCardDTO.setTotaldoubleTime(05.00);
        timeCardDTO.setTimeCardStatus(EnumTimeCardStatus.OPEN);

        // Setup TimeCardItemDTO
        List<TimeCardItemDTO> timeCardItemDTOS = List.of(
                createTimeCardItemDTO(LocalDate.of(2023, 1, 21), "Timacard Test Day 1"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 22), "Timacard Test Day 2"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 23), "Timacard Test Day 3"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 24), "Timacard Test Day 4"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 25), "Timacard Test Day 5")
        );
        timeCardDTO.setTimeCardItems(timeCardItemDTOS);


        // Setup TimeCardExpenseDTO
        TimeCardExpenseDTO timeCardExpenseDTO = new TimeCardExpenseDTO();
        Amount amount = new Amount();
        amount.setAmountValue(100.0);
        amount.setAmountCurrency("$");
        timeCardExpenseDTO.setAmount(amount);
        timeCardExpenseDTO.setName("Expense 1");
        timeCardExpenseDTO.setAmount(amount);
        timeCardExpenseDTO.setBillable(true);
        timeCardExpenseDTO.setPayable(true);
        timeCardExpenseDTO.setCode("123");
        timeCardExpenseDTO.setComment("TimeCard Expense Test");
        timeCardDTO.setTimeCardExpenses(Arrays.asList(timeCardExpenseDTO));

        // Setup TimeCardDocumentDTO
        TimeCardDocumentDTO timeCardDocumentDTO = new TimeCardDocumentDTO();
        DocumentDefDTO documentDefDTO = new DocumentDefDTO();
        timeCardDocumentDTO.setDocumentType(EnumDocumentType.UPLOAD);
        timeCardDocumentDTO.setDocumentName("TimeCardDoc1.pdf");
        timeCardDocumentDTO.setExternalDocCode("123");
        timeCardDocumentDTO.setDocumentDesc("abc");
        timeCardDocumentDTO.setDocumentTag("000");
        timeCardDocumentDTO.setFileType("pdf");
        timeCardDocumentDTO.setFileExt("pdf");        
        timeCardDocumentDTO.setDocumentSource("1");
        timeCardDocumentDTO.setStatus(EnumDocStatus.Active);
        timeCardDTO.setTimeCardDocuments(Arrays.asList(timeCardDocumentDTO));

        // Setup TimeCardExpenseDocumentDTO
        TimeCardExpenseDocumentDTO timeCardExpenseDocumentDTO = new TimeCardExpenseDocumentDTO();
        documentDefDTO = new DocumentDefDTO();
        timeCardExpenseDocumentDTO.setDocumentType(EnumDocumentType.UPLOAD);
        timeCardExpenseDocumentDTO.setDocumentName("ExpenseDoc1.pdf");
        timeCardExpenseDocumentDTO.setExternalDocCode("123");
        timeCardExpenseDocumentDTO.setDocumentDesc("abc");
        timeCardExpenseDocumentDTO.setDocumentTag("000");
        timeCardExpenseDocumentDTO.setFileType("pdf");
        timeCardExpenseDocumentDTO.setFileExt("pdf");
        timeCardExpenseDocumentDTO.setDocumentSource("1");
        timeCardExpenseDocumentDTO.setStatus(EnumDocStatus.Active);
        timeCardDTO.setTimeCardExpenseDocuments(Arrays.asList(timeCardExpenseDocumentDTO));

        timeCardDTO = timeCardService.createTimeCard(timeCardDTO);

        // Validate that the TimeCardDTO is properly setup
        assertNotNull(timeCardDTO.getTimecardID());
    }

    private TimeCardItemDTO createTimeCardItemDTO(LocalDate itemDate, String comment) {
        TimeCardItemDTO timeCardItemDTO = new TimeCardItemDTO();
        timeCardItemDTO.setItemDate(itemDate);
        timeCardItemDTO.setRegularTime(LocalTime.of(8, 0));
        timeCardItemDTO.setOverTime(LocalTime.of(2, 0));
        timeCardItemDTO.setDoubleTime(LocalTime.of(1, 0));
        timeCardItemDTO.setComment(comment);
        return timeCardItemDTO;
    }

    @Test
    public void testCreateTimeCardWithInvalidWorker() {
        assertThrows(NullPointerException.class, () -> {
            TimeCardDTO timeCard = new TimeCardDTO();
            RelatedWorkOrderDTO relatedWorkOrderDTO = new RelatedWorkOrderDTO();
            relatedWorkOrderDTO.setWorkOrderID("123");
            timeCard.setWorkOrder(relatedWorkOrderDTO);
            timeCard.setStartDate(LocalDate.of(2023, 7, 1));
            timeCard.setEndDate(LocalDate.of(2023, 7, 15));
            timeCardService.createTimeCard(timeCard);
        });
    }

    @Test
    public void testCreateTimeCardWithInvalidWorkOrder() {
        assertThrows(NullPointerException.class, () -> {
            TimeCardDTO timeCard = new TimeCardDTO();
            RelatedWorkOrderDTO relatedWorkOrderDTO = new RelatedWorkOrderDTO();
            relatedWorkOrderDTO.setWorkOrderID("1234");
            timeCard.setWorkOrder(relatedWorkOrderDTO);
            timeCard.setStartDate(LocalDate.of(2023, 7, 1));
            timeCard.setEndDate(LocalDate.of(2023, 7, 15));
            timeCardService.createTimeCard(timeCard);
        });
    }

    @Test
    public void testRetrieveAllTimeCards() {
        // Setup data
        TimeCard timeCard = new TimeCard();

        // Assuming you have default constructors and setter methods for your entities
        Worker worker = new Worker();
        worker.setWorkerID("123");

        PersonLegal personLegal = new PersonLegal();
        personLegal.setGivenName("John");
        worker.setPersonLegal(personLegal);

        ContractWorkOrder workOrder = new ContractWorkOrder();
        workOrder.setWorkOrderID("123");
        // workOrder.setWorkorderName("Contract Name");


        timeCard.setWorker(worker);
        timeCard.setWorkOrder(workOrder);
        timeCard.setTimecardID("timecard1");
        timeCard.setStartDate(LocalDate.now());
        timeCard.setEndDate(LocalDate.now().plusDays(5));
        timeCard.setTimeCardStatus(EnumTimeCardStatus.OPEN);

        // Setup parameters
        int offset = 0;
        int limit = 5;
        String contractID = "contract1";
        String resourceID = "worker1";
        String timecardID = "timecard1";
        String workLocation = "location1";
        String taskId = "task1";
        String workOrderID = "order1";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        EnumTimeCardStatus status = EnumTimeCardStatus.OPEN;

        // Test the method
        Page<TimeCardSummaryDTO> retrievedTimeCards = timeCardService.retrieveAllTimeCards(offset, limit, contractID, resourceID, timecardID, workLocation, taskId, workOrderID,startDate, endDate, status);

        // Validate the result
        assertNotNull(retrievedTimeCards);
    }


    @Test
    public void testRetrieveTimeCardById() {
        TimeCardDTO timeCardDTO = new TimeCardDTO();

        RelatedWorkerDTO relatedWorkerDTO = new RelatedWorkerDTO();
        relatedWorkerDTO.setWorkerID("123");
        timeCardDTO.setWorker(relatedWorkerDTO);

        RelatedWorkOrderDTO relatedWorkOrderDTO = new RelatedWorkOrderDTO();
        relatedWorkOrderDTO.setWorkOrderID("123");
        timeCardDTO.setWorkOrder(relatedWorkOrderDTO);

        timeCardDTO.setStartDate(LocalDate.of(2023, 1, 21));
        timeCardDTO.setEndDate(LocalDate.of(2023, 1, 25));

        List<TimeCardItemDTO> timeCardItemDTOS = List.of(
                createTimeCardItemDTO(LocalDate.of(2023, 1, 21), "Timacard Test Day 1"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 22), "Timacard Test Day 2"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 23), "Timacard Test Day 3"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 24), "Timacard Test Day 4"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 25), "Timacard Test Day 5")
        );
        timeCardDTO.setTimeCardItems(timeCardItemDTOS);

        timeCardDTO.setTotalRegularTime(40.00);
        timeCardDTO.setTotalOverTime(10.00);
        timeCardDTO.setTotaldoubleTime(05.00);

        // Act: Create and save a TimeCard using the service
        TimeCardDTO savedTimeCard = timeCardService.createTimeCard(timeCardDTO);

        // Act: Retrieve the saved TimeCard using the service
        TimeCardDTO retrievedTimeCard = timeCardService.retrieveTimeCardById(savedTimeCard.getTimecardID());

        // Assert: Verify that the retrieved TimeCard is not null and has the expected ID
        assertNotNull(retrievedTimeCard, "TimeCard ID should not be null");
        assertEquals(savedTimeCard.getTimecardID(), retrievedTimeCard.getTimecardID(), "TimeCard's ID does not match the saved TimeCard's ID");
    }


    @Test
    public void testRetrieveTimeCardByInvalidId() {
        // invalid TimeCard ID
        String invalidId = "12345";

        // TimeCardNotFoundException thrown when retrieving  TimeCard using an invalid ID
        Exception exception = assertThrows(TimeCardNotFoundException.class, () -> {
            //  retrieve a TimeCard using invalid ID
            timeCardService.retrieveTimeCardById(invalidId);
        });

        // Assert: Verify that the exception message is as expected
        String expectedMessage = "TimeCard by ID " + invalidId + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage, "Exception message does not match expected");
    }


    @Test
    public void testUpdateTimeCard() {

        // Create a new TimeCardDTO
        TimeCardDTO timeCardDTO = new TimeCardDTO();

        RelatedWorkerDTO relatedWorkerDTO = new RelatedWorkerDTO();
        relatedWorkerDTO.setWorkerID("123");
        timeCardDTO.setWorker(relatedWorkerDTO);

        RelatedWorkOrderDTO relatedWorkOrderDTO = new RelatedWorkOrderDTO();
        relatedWorkOrderDTO.setWorkOrderID("123");
        timeCardDTO.setWorkOrder(relatedWorkOrderDTO);

        timeCardDTO.setStartDate(LocalDate.of(2023, 1, 21));
        timeCardDTO.setEndDate(LocalDate.of(2023, 1, 25));

        timeCardDTO.setTotalRegularTime(40.00);
        timeCardDTO.setTotalOverTime(10.00);
        timeCardDTO.setTotaldoubleTime(05.00);
        timeCardDTO.setTimeCardStatus(EnumTimeCardStatus.OPEN);

        List<TimeCardItemDTO> timeCardItemDTOS = List.of(
                createTimeCardItemDTO(LocalDate.of(2023, 1, 21), "Timacard Test Day 1"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 22), "Timacard Test Day 2"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 23), "Timacard Test Day 3"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 24), "Timacard Test Day 4"),
                createTimeCardItemDTO(LocalDate.of(2023, 1, 25), "Timacard Test Day 5")
        );
        timeCardDTO.setTimeCardItems(timeCardItemDTOS);

        // Create a new TimeCard
        TimeCardDTO createdTimeCardDTO = timeCardService.createTimeCard(timeCardDTO);

        //  time card  created and the returned TimeCardDTO has the correct ID
        assertEquals(createdTimeCardDTO.getTimecardID(), createdTimeCardDTO.getTimecardID());

        // Getting  time card with the given ID
        TimeCardDTO originalTimeCardDTO = timeCardService.retrieveTimeCardById(createdTimeCardDTO.getTimecardID());

        if (originalTimeCardDTO == null) {
            throw new TimeCardNotFoundException("No time card found with ID: " + createdTimeCardDTO.getTimecardID());
        } else {
            // Check if the original time card status is OPEN
            if (originalTimeCardDTO.getTimeCardStatus() == EnumTimeCardStatus.OPEN) {
                originalTimeCardDTO.setTimeCardStatus(EnumTimeCardStatus.SUBMITTED); // Set status to SUBMITTED for the update

                // Update the time card
                TimeCardDTO updatedTimeCardDTO = timeCardService.updateTimeCard(createdTimeCardDTO.getTimecardID(), originalTimeCardDTO);

                // Assert that the time card was updated
                assertEquals(EnumTimeCardStatus.SUBMITTED, updatedTimeCardDTO.getTimeCardStatus()); // Check for the new status here
            } else {
                throw new EntityNotFoundException("No open time card found with ID: " + createdTimeCardDTO.getTimecardID());
            }
        }
    }*/
}

