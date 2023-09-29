package ru.yaroslav.test.exceptions_handling.user_bank_exception;

public class IncorrectNameOrPinException extends RuntimeException {

    public IncorrectNameOrPinException(String message) {
        super(message);
    }
}
