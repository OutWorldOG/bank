package ru.yaroslav.test.servicies;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.entities.TransactionHistoryEntity;
import ru.yaroslav.test.entities.UserCardEntity;
import ru.yaroslav.test.entities.UserBankEntity;
import ru.yaroslav.test.exceptions_handling.user_card_exception.IncorrectPinOrCardNumberException;
import ru.yaroslav.test.exceptions_handling.user_card_exception.IncorrectTransferCardsException;
import ru.yaroslav.test.exceptions_handling.user_card_exception.UserAccountNotFoundException;
import ru.yaroslav.test.repositories.UserCardRep;
import ru.yaroslav.test.utilities.BankCardGenerator;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserAccountService {

    private final UserCardRep userCardRep;

    private final TransactionHistoryService transactionHistoryService;

    private final PasswordEncoder allMightyEncoder;

    private final UserAccountService userAccountService;

    public UserAccountService(UserCardRep userCardRep, TransactionHistoryService transactionHistoryService, PasswordEncoder allMightyEncoder, @Lazy UserAccountService userAccountService) {
        this.transactionHistoryService = transactionHistoryService;
        this.userCardRep = userCardRep;
        this.allMightyEncoder = allMightyEncoder;
        this.userAccountService = userAccountService;
    }

    public UserCardEntity saveNewUserAccount(String pin, UserBankEntity userBankEntity) {
        UserCardEntity userCardEntity = new UserCardEntity();
        userCardEntity.setUserId(userBankEntity);
        userCardEntity.setAccountId(UUID.randomUUID().toString());
        userCardEntity.setPin(allMightyEncoder.encode(pin));
        userCardEntity.setCardNumber(BankCardGenerator.generateUniqueBankCardNumber());
        userCardRep.save(userCardEntity);
        return userCardEntity;
    }

    public void deleteUserAccountCard(Card card, BindingResult bindingResult) {
        UserCardEntity userCardEntity = checkForErrorsNReturnEntity(bindingResult, card);

        userCardRep.delete(userCardEntity);
    }

    public String deposit(Card card, BindingResult bindingResult) {
        UserCardEntity userCardEntity = checkForErrorsNReturnEntity(bindingResult, card);

        Long OperationResult = card.getAmountOfMoney() + userCardEntity.getMoney();
        userCardRep.depositOrWithdraw(card.getCardNumber(), OperationResult);
        transactionHistoryService.save(createTransaction("deposit", userCardEntity, card));
        return String.format("Вы занесли %s, текущий баланс: %s", card.getAmountOfMoney(), OperationResult);
    }

    public String withdraw(Card card, BindingResult bindingResult) {
        UserCardEntity userCardEntity = checkForErrorsNReturnEntity(bindingResult, card);

        Long operationResult = userCardEntity.getMoney() - card.getAmountOfMoney();
        if (Long.signum(operationResult) == -1) {
            throw new UserAccountNotFoundException("Недостаточно средств");
        }
        userCardRep.depositOrWithdraw(userCardEntity.getCardNumber(), operationResult);
        transactionHistoryService.save(createTransaction("withdraw", userCardEntity, card));
        return String.format("Вы сняли %s, текущий баланс: %s", card.getAmountOfMoney(), operationResult);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void transfer(Card[] cards, BindingResult bindingResult) {
        if (!compareCards(cards)) {
            throw new IncorrectTransferCardsException("incorrect cards data");
        }

        userAccountService.withdraw(cards[0], bindingResult);
        userAccountService.deposit(cards[1], bindingResult);

    }

    public List<TransactionHistoryEntity> getAllTransactions(Card card, BindingResult bindingResult) {
        UserCardEntity userCardEntity = checkForErrorsNReturnEntity(bindingResult, card);
        return userCardEntity.getTransactionHistoryEntities();
    }


    private boolean compareCards(Card[] cards) {
        if (cards.length != 2) {
            return false;
        }
        if (!cards[0].getAmountOfMoney().equals(cards[1].getAmountOfMoney())) {
            return false;
        }
        return !cards[0].getCardNumber().equals(cards[1].getCardNumber());
    }

    private TransactionHistoryEntity createTransaction(String operation, UserCardEntity userCardEntity, Card card) {
        TransactionHistoryEntity transactionHistoryEntity = new TransactionHistoryEntity();
        transactionHistoryEntity.setHistoryId(UUID.randomUUID().toString());
        transactionHistoryEntity.setDate(LocalDateTime.now());
        transactionHistoryEntity.setOperationType(operation);
        transactionHistoryEntity.setAccountId(userCardEntity);
        transactionHistoryEntity.setAmount(card.getAmountOfMoney());
        return transactionHistoryEntity;
    }

    private UserCardEntity checkForErrorsNReturnEntity(BindingResult bindingResult, Card card) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectPinOrCardNumberException(bindingResult.getFieldErrors().toString());
        }
        Optional<UserCardEntity> optionalUserAccountEntity = userCardRep.findByAccountNumber(card.getCardNumber());

        if (optionalUserAccountEntity.isEmpty()) {
            throw new UserAccountNotFoundException("карта не найдена");
        }

        UserCardEntity userCardEntity = optionalUserAccountEntity.get();
        if (!allMightyEncoder.matches(card.getPin(), userCardEntity.getPin())) {
            throw new UserAccountNotFoundException("введён неверный pin");
        }
        return userCardEntity;
    }
}
