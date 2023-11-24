package com.taskmanager.controller.constraint;

import com.taskmanager.util.Constant;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateConstraint {

    /**
     * Get the error message from property file.
     *
     * @return error message
     */
    String message() default "{error.date} {format}";

    /**
     * Set the format pattern for a date.
     *
     * @return format pattern
     */
    String format() default Constant.DEFAULT_FORMAT_DATE;

    /**
     * Groups.
     *
     * @return generic class
     */
    Class<?>[] groups() default {};

    /**
     * Payload.
     *
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};

}
