package ru.yaroslav.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount implements Serializable {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 36)
    private String name;

    @NotBlank
    @NotNull
    @Size(min = 4, max = 4)
    private String pin;

}
