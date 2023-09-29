package ru.yaroslav.test.exceptions_handling.user_account_exception;

public class UserAccountNotFoundException extends RuntimeException {
    public UserAccountNotFoundException(String message) {
        super(message);
    }
}
