package ru.yaroslav.test.utilities;

import ru.yaroslav.test.dto.CardTransaction;
import ru.yaroslav.test.entities.UserCardEntity;

public class CardMapper {

    public static CardTransaction toCard(UserCardEntity userCardEntity) {

        return CardTransaction.builder()
                .cardNumber(userCardEntity.getCardNumber())
                .amountOfMoney(userCardEntity.getMoney())
                .build();

    }
}
