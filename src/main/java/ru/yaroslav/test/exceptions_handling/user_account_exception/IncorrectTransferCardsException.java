package ru.yaroslav.test.exceptions_handling.user_account_exception;

public class IncorrectTransferCardsException extends RuntimeException {
    public IncorrectTransferCardsException(String message) {
        super(message);
    }
}
