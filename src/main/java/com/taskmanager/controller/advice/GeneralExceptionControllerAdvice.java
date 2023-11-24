package com.taskmanager.controller.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.taskmanager.domain.exeption.NotFoundException;
import com.taskmanager.domain.exeption.RepeatAssignException;
import com.taskmanager.domain.exeption.TaskOverdueException;
import com.taskmanager.domain.exeption.UserExistsException;
import com.taskmanager.domain.mapper.GenericResponseMapper;
import com.taskmanager.domain.model.DetailResponse;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.util.Constant;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.BindException;
import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;

@Slf4j
@Order
@ControllerAdvice
public class GeneralExceptionControllerAdvice {

    private final HttpStatus badRequest = HttpStatus.BAD_REQUEST;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> handle(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        if (!violations.isEmpty()) {
            List<DetailResponse> fieldErrors = violations.stream()
                    .map(GenericResponseMapper::mapDetailResponse)
                    .toList();
            return ResponseEntity
                    .status(badRequest)
                    .body(GenericResponseMapper.mapGenericResponse(fieldErrors));
        }
        return defaultResponse(badRequest, Constant.CONSTRAINT_VIOLATION, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> illegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage()
                .replaceAll("\\s{2,}", " ")
                .replaceAll("[^\\w\\s]","")
                .replace(" ", ".");
        return defaultResponse(badRequest, message.toLowerCase(), e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { InvalidParameterException.class })
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> invalidParameterException(InvalidParameterException e) {
        return defaultResponse(badRequest, e.getMessage(), e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { MissingRequestHeaderException.class })
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> missingRequestHeaderException(MissingRequestHeaderException e) {
        return defaultResponse(badRequest, Constant.INVALID_HEADER, e.getLocalizedMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { BindException.class })
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> bindException(BindException e) {
        return defaultResponse(badRequest, e.getMessage(), e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { UnexpectedTypeException.class })
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> unexpectedTypeException(UnexpectedTypeException e) {
        return defaultResponse(badRequest, e.getMessage(), e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { AccessDeniedException.class })
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> accessDeniedException(AccessDeniedException e) {
        return defaultResponse(badRequest, e.getMessage(), e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> buildMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<DetailResponse> fieldErrors = result.getFieldErrors().stream()
                .map(GenericResponseMapper::mapDetailResponse)
                .toList();
        return ResponseEntity
                .status(badRequest)
                .body(GenericResponseMapper.mapGenericResponse(fieldErrors));    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { InvalidFormatException.class })
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> invalidFormatException(InvalidFormatException e) {
        String detail = e.getLocalizedMessage();
        if(e.getValue() != null){
            detail = e.getValue() + " is not a "+ e.getTargetType().getSimpleName() +".";
        }
        return defaultResponse(badRequest, Constant.INVALID_FORMAT, detail);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> processMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return defaultResponse(HttpStatus.BAD_REQUEST, ex.getParameterName(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return defaultResponse(HttpStatus.METHOD_NOT_ALLOWED, Constant.METHOD_NO_SUPPORTED, ex.getLocalizedMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GenericResponse<?>> processException(Exception ex) {
        log.info("processException: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return defaultResponse(status, status.getReasonPhrase(), Constant.DEFAULT_ERROR);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericResponse<?>> buildHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return defaultResponse(badRequest, badRequest.getReasonPhrase(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericResponse<?>> notFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return defaultResponse(status, status.name(), ex.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<GenericResponse<?>> userExistsException(UserExistsException e){
        return defaultResponse(badRequest, badRequest.getReasonPhrase(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(RepeatAssignException.class)
    public ResponseEntity<GenericResponse<?>> repeatAssignException(RepeatAssignException e){
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        return defaultResponse(status, status.name(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(TaskOverdueException.class)
    public ResponseEntity<GenericResponse<?>> taskOverdueException(TaskOverdueException e){
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        return defaultResponse(status, status.name(), e.getMessage());
    }

    private ResponseEntity<GenericResponse<?>> defaultResponse(HttpStatus httpStatus, String message, String detail){
        return ResponseEntity
                .status(httpStatus)
                .body(GenericResponseMapper.mapGenericResponse(
                        new DetailResponse(String.valueOf(httpStatus.value()),
                                message, detail)));
    }
}
