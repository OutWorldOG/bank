package ru.yaroslav.test.exceptions_handling.user_card_exception;

public class UserAccountNotFoundException extends RuntimeException {
    public UserAccountNotFoundException(String message) {
        super(message);
    }
}
