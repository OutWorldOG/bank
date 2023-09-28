package ru.yaroslav.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
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
public class Card implements Serializable {

    @NotBlank(message = "cant be blank")
    @NotNull(message = "cant be null")
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotBlank
    @NotNull
    @Size(min = 4, max = 4)
    private String pin;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 36)
    private String name;

    @Min(value = 10)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long money;
}
