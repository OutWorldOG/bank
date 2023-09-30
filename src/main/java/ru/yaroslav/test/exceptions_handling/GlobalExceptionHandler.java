package ru.yaroslav.test.exceptions_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yaroslav.test.exceptions_handling.user_card_exception.IncorrectPinOrCardNumberException;
import ru.yaroslav.test.exceptions_handling.user_card_exception.IncorrectTransferCardsException;
import ru.yaroslav.test.exceptions_handling.user_card_exception.UserAccountCardIncorrectData;
import ru.yaroslav.test.exceptions_handling.user_card_exception.UserAccountNotFoundException;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.IncorrectNameOrPinException;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.UserBankIncorrectData;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.UserBankNotFound;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ResponseEntity<UserBankIncorrectData> handleException(IncorrectNameOrPinException exception) {
        UserBankIncorrectData incorrectData = new UserBankIncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    ResponseEntity<UserBankIncorrectData> handleException(UserBankNotFound exception) {
        UserBankIncorrectData incorrectData = new UserBankIncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    ResponseEntity<UserAccountCardIncorrectData> handleException(IncorrectPinOrCardNumberException exception) {
        UserAccountCardIncorrectData incorrectData = new UserAccountCardIncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    ResponseEntity<UserAccountCardIncorrectData> handleException(UserAccountNotFoundException exception) {
        UserAccountCardIncorrectData incorrectData = new UserAccountCardIncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    ResponseEntity<UserAccountCardIncorrectData> handleException(IncorrectTransferCardsException exception) {
        UserAccountCardIncorrectData incorrectData = new UserAccountCardIncorrectData();
        incorrectData.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
    }
}
