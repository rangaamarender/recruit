package com.lucid.recruit.attr.validations;

import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.attr.constants.EnumAttributeType;
import com.lucid.recruit.attr.dto.BaseAttributeDefDTO;
import com.lucid.recruit.attr.entity.BaseAttrDefListValues;
import com.lucid.recruit.attr.entity.BaseAttributeDef;
import com.lucid.recruit.attr.entity.WorkerAttrDefListValues;
import com.lucid.recruit.attr.entity.WorkerAttributeDef;
import com.lucid.recruit.worker.dto.WorkerAttributesDTO;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class BaseAttributeValidator {
    public static void validateAttrTypeValue(EnumAttributeType attributeType, String value) {
        switch (attributeType){
            case STRING -> {
                return ;
            }
            case DATE -> {
                if(!validateDate(value,"yyyyMMdd")){
                    throw new InvalidDataException(ErrorCodes.W_ATTR_V_0002,"workerAttributeDef","attributeType",attributeType.toString(),"Attribute value must be date") ;
                }
            }
            case NUMBER -> {
                if(!StringUtils.isNumeric(value)){
                    throw new InvalidDataException(ErrorCodes.W_ATTR_V_0002,"workerAttributeDef","attributeType",attributeType.toString(),"Attribute value must be number") ;
                }
            }
            case DATETIME -> {
                if(!validateDateAndTime(value,"yyyyMMddThhmmss")){
                    throw new InvalidDataException(ErrorCodes.W_ATTR_V_0002,"workerAttributeDef","attributeType",attributeType.toString(),"Attribute value must be dateTime") ;
                }
            }
            case TIME -> {
                if(!validateTime(value,"hhmmss")){
                    throw new InvalidDataException(ErrorCodes.W_ATTR_V_0002,"workerAttributeDef","attributeType",attributeType.toString(),"Attribute value must be time") ;
                }
            }
        }
    }

    private static boolean validateDate(String dateStr, String format) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private static boolean validateDateAndTime(String dateAndTimeStr, String format) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalDateTime.parse(dateAndTimeStr, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private static boolean validateTime(String timeStr, String format) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalTime.parse(timeStr, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }


}
