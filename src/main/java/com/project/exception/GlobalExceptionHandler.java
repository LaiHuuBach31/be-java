package com.project.exception;

import com.project.exception.base.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
@ControllerAdvice
public class ExceptionHandlerAdvice {

//    @ExceptionHandler(value = NoCategoryFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<ErrorResponse> handleNoCategoryException(){
//        return new ResponseEntity<ErrorResponse>(new ErrorResponse(404, "Category not found", new Date()), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(CustomException.BadRequestException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ErrorResponse> handleBadRequestException() {
//        ErrorResponse response = new ErrorResponse(400, "Internal Server Error", new Date());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(CustomException.NotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<ErrorResponse> handleNotFoundException() {
//        ErrorResponse response = new ErrorResponse(404, "Internal Server Error", new Date());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<ErrorResponse> handleGenericException() {
//        ErrorResponse response = new ErrorResponse(500, "Internal Server Error", new Date());
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
