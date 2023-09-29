package ru.yaroslav.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Card implements Serializable {

    @NotBlank(message = "cant be blank")
    @NotNull(message = "cant be null")
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotBlank(message = "Поле name не может быть пустым")
    @NotNull(message = "необходимо передать значение name")
    @Size(min = 4, max = 4)
    @Pattern(regexp = "^\\d+$", message = "pin должен состоять только из цифр")
    private String pin;

    @Min(value = 10)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // @Pattern(regexp = "^\\d+$", message = "только положительные и цифры")
    private Long amountOfMoney;
}
