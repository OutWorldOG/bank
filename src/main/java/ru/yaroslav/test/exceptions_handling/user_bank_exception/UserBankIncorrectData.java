package ru.yaroslav.test.exceptions_handling.user_bank_exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserBankIncorrectData {

    String errorMessage;
}
