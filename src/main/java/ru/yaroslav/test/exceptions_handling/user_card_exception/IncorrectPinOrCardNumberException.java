package ru.yaroslav.test.exceptions_handling.user_card_exception;

public class IncorrectPinOrCardNumberException extends RuntimeException {
    public IncorrectPinOrCardNumberException(String message) {
        super(message);
    }
}
