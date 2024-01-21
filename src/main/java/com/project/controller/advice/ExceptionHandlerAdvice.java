package com.project.controller.advice;

import com.project.exception.ResponeError;
import com.project.exception.NoCategoryFoundException;
import com.project.exception.base.BadRequestException;
import com.project.exception.base.BaseException;
import com.project.exception.base.NotFoundException;
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

    @ExceptionHandler(value = NoCategoryFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponeError> handleNoCategoryException(){
        return new ResponseEntity<ResponeError>(new ResponeError(404, "Category not found", new Date()), HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(BaseException.class)
//    public ResponseEntity<ResponeError> handleBaseException() {
//        String message = getMessage(ex.getCode(), locale, ex.getParams());
//        ResponseGeneral<Object> response = ResponseGeneral.of(ex.getStatus(), message, null);
//        return new ResponseEntity<ResponeError>(response, HttpStatus.valueOf(ex.getStatus()));
//    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponeError> handleBadRequestException() {
        ResponeError response = new ResponeError(400, "Internal Server Error", new Date());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponeError> handleNotFoundException() {
        ResponeError response = new ResponeError(404, "Internal Server Error", new Date());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponeError> handleGenericException() {
        ResponeError response = new ResponeError(500, "Internal Server Error", new Date());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
