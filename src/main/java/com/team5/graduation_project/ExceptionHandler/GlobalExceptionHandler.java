package com.team5.graduation_project.ExceptionHandler;

import com.team5.graduation_project.Exceptions.AlreadyExists;
import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<BaseResponse> handleResourceNotFound(ResourceNotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse(e.getMessage(), null));
    }

    @ExceptionHandler(AlreadyExists.class)
    public ResponseEntity<BaseResponse> handleAlreadyExists(AlreadyExists e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse(e.getMessage() , null));
    }
}
