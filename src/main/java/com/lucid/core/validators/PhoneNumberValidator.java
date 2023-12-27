package com.lucid.core.validators;

import com.lucid.core.annotations.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<Phone,String> {
    @Override
    public void initialize(Phone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        // Define a regular expression pattern for common phone number formats
        String regex = "^(\\+\\d{1,3})?[-.\\s]?\\(?(\\d{3})\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$";
        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);
        // Create a matcher object
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
