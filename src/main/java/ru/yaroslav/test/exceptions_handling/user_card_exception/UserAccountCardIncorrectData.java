package ru.yaroslav.test.exceptions_handling.user_card_exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserAccountCardIncorrectData {
    String errorMessage;
}
