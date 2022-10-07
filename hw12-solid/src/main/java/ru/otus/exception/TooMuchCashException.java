package ru.otus.exception;

public class TooMuchCashException extends RuntimeException {
    public TooMuchCashException(String errorMessage) {
        super(errorMessage);
    }
}