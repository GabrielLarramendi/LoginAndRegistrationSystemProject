package com.larramendiProject.RegisterLoginSystem.exceptions;

public class EmptyFieldException extends RuntimeException{
    public EmptyFieldException(String msg) {
        super(msg);
    }
}
