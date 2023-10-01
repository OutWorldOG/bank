package ru.yaroslav.test.exceptions_handling.user_card_exception;

public class UserCardNotFoundException extends RuntimeException {
    public UserCardNotFoundException(String message) {
        super(message);
    }
}
