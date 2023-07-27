package com.larramendiProject.RegisterLoginSystem.exceptions.handlerException;

import com.larramendiProject.RegisterLoginSystem.exceptions.EmailAlreadyExistsException;
import com.larramendiProject.RegisterLoginSystem.exceptions.ErrorDetails;
import com.larramendiProject.RegisterLoginSystem.exceptions.IdNotFoundException;
import com.larramendiProject.RegisterLoginSystem.exceptions.InvalidPasswordException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
