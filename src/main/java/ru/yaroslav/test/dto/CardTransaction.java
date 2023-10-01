package ru.yaroslav.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Objects;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardTransaction extends Card implements Serializable {

    @Min(value = 10)
    private Long amountOfMoney;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CardTransaction that = (CardTransaction) o;
        return Objects.equals(amountOfMoney, that.amountOfMoney);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), amountOfMoney);
    }
}
