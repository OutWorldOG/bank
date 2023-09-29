package ru.yaroslav.test.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountRequest implements Serializable {

    @NotBlank(message = "Поле name не может быть пустым")
    @NotNull(message = "необходимо передать значение name")
    @Size(min = 3, max = 36)
    private String name;

    @NotBlank(message = "Поле name не может быть пустым")
    @NotNull(message = "необходимо передать значение name")
    @Size(min = 4, max = 4)
    @Pattern(regexp = "^\\d+$", message = "pin должен состоять только из цифр")
    private String pin;

}
