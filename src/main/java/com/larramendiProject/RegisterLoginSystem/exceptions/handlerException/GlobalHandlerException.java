package com.larramendiProject.RegisterLoginSystem.exceptions.handlerException;

import com.larramendiProject.RegisterLoginSystem.exceptions.EmailAlreadyExistsException;
import com.larramendiProject.RegisterLoginSystem.exceptions.ErrorDetails;
import com.larramendiProject.RegisterLoginSystem.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {

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
}
