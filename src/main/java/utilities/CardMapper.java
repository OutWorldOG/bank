package utilities;

import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.entities.UserAccountEntity;

public class CardMapper {

    public static Card toCard(UserAccountEntity userAccountEntity, String cardHolderName) {

        return Card.builder()
                .cardNumber(userAccountEntity.getAccountNumber())
                .pin(userAccountEntity.getPin())
                .name(cardHolderName)
                .build();

    }
}
