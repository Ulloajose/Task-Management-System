package com.taskmanager.controller.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateValidator implements ConstraintValidator<DateConstraint, String> {

    private String format;

    @Override
    public void initialize(DateConstraint dateConstraint) {
        this.format = dateConstraint.format();
    }

    @Override
    public boolean isValid(String dateField,
                           ConstraintValidatorContext cxt) {
        try {
            if (dateField != null) {
                LocalDate.parse(dateField, DateTimeFormatter.ofPattern(format));
            }
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}
