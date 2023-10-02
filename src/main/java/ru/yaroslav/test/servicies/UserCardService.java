package ru.yaroslav.test.servicies;

import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.dto.CardTransaction;
import ru.yaroslav.test.entities.TransactionHistoryEntity;
import ru.yaroslav.test.entities.UserBankAccountEntity;
import ru.yaroslav.test.entities.UserCardEntity;

import java.util.List;

public interface UserCardService {
    UserCardEntity saveNewUserCard(String pin, UserBankAccountEntity userBankAccountEntity);

    void deleteUserCard(Card cardTransaction, BindingResult bindingResult);

    String deposit(CardTransaction cardTransaction, BindingResult bindingResult);

    String withdraw(CardTransaction cardTransaction, BindingResult bindingResult);

    void transfer(CardTransaction[] cardTransactions, BindingResult bindingResult);

    List<TransactionHistoryEntity> getAllTransactions(Card cardTransaction, BindingResult bindingResult);
}
