package com.taskmanager.domain.mapper;


import com.taskmanager.domain.model.DetailResponse;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.domain.model.ResultResponse;
import com.taskmanager.util.Constant;
import jakarta.validation.ConstraintViolation;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class GenericResponseMapper {

    public DetailResponse mapDetailResponse(ConstraintViolation<?> constraintViolation){
        return mapDetailResponse(
                HttpStatus.BAD_REQUEST,
                Constant.INVALID_PARAMETER,
                "Check field: " + constraintViolation.getPropertyPath());
    }

    public DetailResponse mapDetailResponse(FieldError fieldError){
        return mapDetailResponse(
                HttpStatus.BAD_REQUEST,
                Constant.INVALID_PARAMETER,
                "Check field: " + fieldError.getField());
    }

    public DetailResponse mapDetailResponse(HttpStatus status, String message, String detail){
        return DetailResponse.builder()
                .internalCode(String.valueOf(status.value()))
                .message(message)
                .detail(detail)
                .build();
    }

    public GenericResponse<?> mapGenericResponse(List<DetailResponse> list){
        return GenericResponse.builder()
                .result(new ResultResponse(list, Constant.INTERNAL_COMPONENT))
                .build();
    }

    public GenericResponse<?> mapGenericResponse(DetailResponse detailResponse){
        return mapGenericResponse(List.of(detailResponse));
    }

    public <T> GenericResponse<T> buildGenericResponse(T data, HttpStatusCode status, String detail){
        List<DetailResponse> detailResponseList = Collections.singletonList(
                DetailResponse.builder()
                        .internalCode(String.valueOf(status.value()))
                        .message(status.toString())
                        .detail(detail)
                        .build()
        );
        return GenericResponse
                .<T>builder()
                .data(data)
                .result(
                        ResultResponse.builder()
                                .details(detailResponseList)
                                .source(Constant.INTERNAL_COMPONENT)
                                .build()
                )
                .build();
    }
}
