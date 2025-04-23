package com.tiendaweb.exception;

public class TransbankException extends RuntimeException {
    public TransbankException(String message){
        super(message);
    }

    public TransbankException(String message, Throwable cause) {
        super(message, cause);
    }
}
