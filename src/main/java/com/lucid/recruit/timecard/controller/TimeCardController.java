package com.lucid.recruit.timecard.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucid.recruit.timecard.constants.EnumTimeCardStatus;
import com.lucid.recruit.timecard.dto.TimeCardDTO;
import com.lucid.recruit.timecard.service.TimeCardService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Tag(name = "TimeCard", description = "TimeCard Management API")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/raves/")
public class TimeCardController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeCardController.class);

	@Autowired
	private TimeCardService timeCardService;


	@PostMapping("v1/timecard")
	public ResponseEntity<Object> createTimeCard(@RequestBody @Valid TimeCardDTO timeCardDTO) {
		System.out.println(timeCardDTO.getContract().getWorkOrder().getTimeCardItems().get(0).getHours());
		LOGGER.info("Create new time card");
		return ResponseEntity.ok(timeCardService.createTimeCard(timeCardDTO));
	}

	@GetMapping("v1/timecard/{timecardID}")
	public ResponseEntity<Object> retrieveTimeCard(@PathVariable(name = "timecardID") @NotNull String timecardID){
		return ResponseEntity.ok(timeCardService.retrieveTimeCardById(timecardID));
	}

	@GetMapping("v1/timecard")
	public ResponseEntity<Object> retrieveAllTimeCards(
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "contractID", required = false) String contractID,
			@RequestParam(value = "workerID", required = false) String workerID,
			@RequestParam(value = "timecardID", required = false) String timecardID,
			@RequestParam(value = "workOrderID", required = false) String workOrderID,
			@RequestParam(value = "startDate", required = false) LocalDate startDate,
			@RequestParam(value = "endDate", required = false) LocalDate endDate,
			@RequestParam(value = "status", required = false) EnumTimeCardStatus status){
		return ResponseEntity.ok(timeCardService.retrieveAllTimeCards(offset, limit, contractID, workerID, timecardID, workOrderID,  startDate, endDate, status));
	}






}
