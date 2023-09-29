package ru.yaroslav.test.exceptions_handling.user_account_exception;

public class IncorrectPinOrCardNumberException extends RuntimeException {
    public IncorrectPinOrCardNumberException(String message) {
        super(message);
    }
}
