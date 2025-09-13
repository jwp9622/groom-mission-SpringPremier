package com.example.SpringPremier.domain.user.exception;


public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}