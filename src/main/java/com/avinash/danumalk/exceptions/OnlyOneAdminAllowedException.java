package com.avinash.danumalk.exceptions;

public class OnlyOneAdminAllowedException extends RuntimeException {

    public OnlyOneAdminAllowedException(String message) {
        super(message);
    }
}
