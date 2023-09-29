package ru.yaroslav.test.exceptions_handling.user_bank_exception;

public class UserBankNotFound extends RuntimeException {
    public UserBankNotFound(String message) {
        super(message);
    }
}
