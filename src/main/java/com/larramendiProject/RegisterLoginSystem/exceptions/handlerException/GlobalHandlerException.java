package com.larramendiProject.RegisterLoginSystem.exceptions.handlerException;

import com.larramendiProject.RegisterLoginSystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<String> handlerIdNotFoundException(IdNotFoundException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.badRequest().body(errorDetails.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handlerEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.badRequest().body(errorDetails.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handlerInvalidPwdException(InvalidPasswordException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.badRequest().body(errorDetails.getMessage());
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<String> handlerEmptyFieldsException(EmptyFieldException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.badRequest().body(errorDetails.getMessage());
    }
}