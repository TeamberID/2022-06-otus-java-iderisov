package ru.otus.exception;

public class NotEnoughCashException extends RuntimeException {
    public NotEnoughCashException(String errorMessage) {
        super(errorMessage);
    }
}