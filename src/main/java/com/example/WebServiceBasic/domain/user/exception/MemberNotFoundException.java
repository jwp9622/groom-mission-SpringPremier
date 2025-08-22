package com.example.WebServiceBasic.domain.user.exception;


public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}