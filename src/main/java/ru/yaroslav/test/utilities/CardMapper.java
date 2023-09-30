package ru.yaroslav.test.utilities;

import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.entities.UserCardEntity;

public class CardMapper {

    public static Card toCard(UserCardEntity userCardEntity) {

        return Card.builder()
                .cardNumber(userCardEntity.getCardNumber())
                .amountOfMoney(userCardEntity.getMoney())
                .build();

    }
}
