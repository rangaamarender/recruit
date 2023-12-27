package com.lucid.recruit.referencedata.service;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.referencedata.constants.EnumParametersType;
import com.lucid.recruit.referencedata.controller.TenantParametersController;
import com.lucid.recruit.referencedata.dto.TenantParametersDTO;
import com.lucid.recruit.referencedata.entity.TenantParameters;
import com.lucid.recruit.referencedata.repo.TenantParametersRepo;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@Service
public class TenantParametersServiceImpl implements TenantParametersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantParametersController.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TenantParametersRepo tntpameterrepo;

    private  Map<EnumParametersType, Consumer<String>> validators = new HashMap<>();

    {
        validators.put(EnumParametersType.STRING, this::validateString);
        validators.put(EnumParametersType.NUMBER, this::validateNumber);
        validators.put(EnumParametersType.DATE, this::validateDate);
        validators.put(EnumParametersType.DATETIME, this::validateDateTime);
        validators.put(EnumParametersType.TIME, this::validateTime);
    }

    @Override
    @Transactional
    public TenantParametersDTO createTenantParameter(TenantParametersDTO tntParameterDTO) {

        LOGGER.debug("Entering to create a new TenantParameter with name: {}", tntParameterDTO.getName());
        LOGGER.debug("Validation passed for the new TenantParameter value: {}", tntParameterDTO.getValue());
        validateValueForType(tntParameterDTO.getType(), tntParameterDTO.getValue());
        Optional<TenantParameters> optionalTenantParameters = tntpameterrepo.findById(tntParameterDTO.getName());
        TenantParameters tenantParameters = null;
        if(optionalTenantParameters.isPresent()){
            tenantParameters = optionalTenantParameters.get();
        }
        else{
            tenantParameters = new TenantParameters();
            tenantParameters.setName(tntParameterDTO.getName());
        }
        tenantParameters.setValue(tntParameterDTO.getValue());
        tenantParameters.setType(tntParameterDTO.getType());
        tenantParameters.setDate(LocalDate.now());
        tntpameterrepo.save(tenantParameters);
        // Map the saved Entity back to DTO
        return modelMapper.map(tenantParameters, TenantParametersDTO.class);
    }


    private void validateString(String value) {
        LOGGER.debug("Validating string value: {}", value);
        if (value == null || StringUtils.isAllBlank(value)) {
            LOGGER.debug("Invalid string value: {}", value);
            throw new InvalidDataException("ERROR_INVALID_DATA", "TenantParameter", "value", value, "String value cannot be empty or null");
        }
    }

    private void validateNumber(String value) {
        LOGGER.debug("Validating numeric value: {}", value);
        if (!StringUtils.isNumeric(value)) {
            LOGGER.debug("Invalid numeric value: {}", value);
            throw new InvalidDataException("ERROR_INVALID_DATA", "TenantParameter", "value", value, "Invalid numeric value");
        }
    }

    private void validateDate(String value) {
        LOGGER.debug("Validating date value: {}", value);
        if (!isValidDate(value)) {
            LOGGER.debug("Invalid date value: {}", value);
            throw new InvalidDataException("ERROR_INVALID_DATA", "TenantParameter", "value", value, "Invalid date value");
        }
    }

    private void validateDateTime(String value) {
        LOGGER.debug("Validating datetime value: {}", value);
        if (!isValidDateTime(value)) {
            LOGGER.debug("Invalid datetime value: {}", value);
            throw new InvalidDataException("ERROR_INVALID_DATA", "TenantParameter", "value", value, "Invalid datetime value");
        }
    }

    private void validateTime(String value) {
        LOGGER.debug("Validating time value: {}", value);
        if (!isValidTime(value)) {
            LOGGER.debug("Invalid time value: {}", value);
            throw new InvalidDataException("ERROR_INVALID_DATA", "TenantParameter", "value", value, "Invalid time value");
        }
    }

    private void validateValueForType(EnumParametersType type, String value) {
        LOGGER.debug("Validating value for type: {}", type);
        validators.getOrDefault(type, v -> {
        }).accept(value);
    }

    private boolean isValidDate(String value) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            LocalDate.parse(value, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private boolean isValidDateTime(String value) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddThhmmss");
        try {
            LocalDateTime.parse(value, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private boolean isValidTime(String value) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("hhmmss");
        try {
            LocalTime.parse(value, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public TenantParametersDTO findTenantParameterByName(String name) {
        LOGGER.debug("Retrieve tenant by name and date: {} - {}", name);
        TenantParameters tntParameter = tntpameterrepo.findById(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCodes.RESOURCE_NOT_FOUND_404,
                        "TenantParameter by name:" + name +" not found"
                ));

        LOGGER.debug("Returning tenant Result: {}", tntParameter);
        return modelMapper.map(tntParameter, TenantParametersDTO.class);
    }

    @Override
    public List<TenantParametersDTO> getAllTenantParameters() {
        List<TenantParameters> tenantParameters = tntpameterrepo.findAll();
        return tenantParameters.stream().map(tenantParameters1 -> {return modelMapper.map(tenantParameters1,TenantParametersDTO.class);}).collect(Collectors.toList());
    }



}
