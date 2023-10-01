package ru.yaroslav.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBankAccount {

    @NotBlank(message = "Поле name не может быть пустым")
    @NotNull(message = "необходимо передать значение name")
    @Size(min = 3, max = 36)
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CardTransaction> userCardTransactions;
}
