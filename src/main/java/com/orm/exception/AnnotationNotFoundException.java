package com.orm.exception;

public class AnnotationNotFoundException extends RuntimeException {
    public AnnotationNotFoundException(String message) {
        super(message);
    }
    public AnnotationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
