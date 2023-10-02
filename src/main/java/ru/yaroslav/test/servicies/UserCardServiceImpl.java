package ru.yaroslav.test.servicies;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.dto.CardTransaction;
import ru.yaroslav.test.entities.TransactionHistoryEntity;
import ru.yaroslav.test.entities.UserCardEntity;
import ru.yaroslav.test.entities.UserBankAccountEntity;
import ru.yaroslav.test.exceptions_handling.user_card_exception.IncorrectPinOrCardNumberException;
import ru.yaroslav.test.exceptions_handling.user_card_exception.IncorrectTransferCardsException;
import ru.yaroslav.test.exceptions_handling.user_card_exception.UserCardNotFoundException;
import ru.yaroslav.test.repositories.UserCardRep;
import ru.yaroslav.test.utilities.BankCardGenerator;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserCardServiceImpl implements UserCardService {

    private final UserCardRep userCardRep;

    private final TransactionHistoryService transactionHistoryService;

    private final PasswordEncoder allMightyEncoder;

    private final UserCardServiceImpl userCardServiceImpl;

    public UserCardServiceImpl(UserCardRep userCardRep, TransactionHistoryService transactionHistoryServiceImpl, PasswordEncoder allMightyEncoder, @Lazy UserCardServiceImpl userCardServiceImpl) {
        this.transactionHistoryService = transactionHistoryServiceImpl;
        this.userCardRep = userCardRep;
        this.allMightyEncoder = allMightyEncoder;
        this.userCardServiceImpl = userCardServiceImpl;
    }

    public UserCardEntity saveNewUserCard(String pin, UserBankAccountEntity userBankAccountEntity) {
        UserCardEntity userCardEntity = new UserCardEntity();
        userCardEntity.setUserId(userBankAccountEntity);
        userCardEntity.setAccountId(UUID.randomUUID().toString());
        userCardEntity.setPin(allMightyEncoder.encode(pin));
        userCardEntity.setCardNumber(BankCardGenerator.generateUniqueBankCardNumber());
        userCardRep.save(userCardEntity);
        return userCardEntity;
    }

    public void deleteUserCard(Card card, BindingResult bindingResult) {
        UserCardEntity userCardEntity = checkForErrorsNReturnEntity(bindingResult, card);

        userCardRep.delete(userCardEntity);
    }

    public String deposit(CardTransaction cardTransaction, BindingResult bindingResult) {
        UserCardEntity userCardEntity = checkForErrorsNReturnEntity(bindingResult, cardTransaction);

        Long OperationResult = cardTransaction.getAmountOfMoney() + userCardEntity.getMoney();
        userCardRep.depositOrWithdraw(cardTransaction.getCardNumber(), OperationResult);
        transactionHistoryService.save(createTransaction("deposit", userCardEntity, cardTransaction));
        return String.format("Вы занесли %s, текущий баланс: %s", cardTransaction.getAmountOfMoney(), OperationResult);
    }

    public String withdraw(CardTransaction cardTransaction, BindingResult bindingResult) {
        UserCardEntity userCardEntity = checkForErrorsNReturnEntity(bindingResult, cardTransaction);

        Long operationResult = userCardEntity.getMoney() - cardTransaction.getAmountOfMoney();
        if (Long.signum(operationResult) == -1) {
            throw new UserCardNotFoundException("Недостаточно средств");
        }
        userCardRep.depositOrWithdraw(userCardEntity.getCardNumber(), operationResult);
        transactionHistoryService.save(createTransaction("withdraw", userCardEntity, cardTransaction));
        return String.format("Вы сняли %s, текущий баланс: %s", cardTransaction.getAmountOfMoney(), operationResult);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void transfer(CardTransaction[] cardTransactions, BindingResult bindingResult) {
        if (!compareCards(cardTransactions)) {
            throw new IncorrectTransferCardsException("incorrect cards data");
        }

        userCardServiceImpl.withdraw(cardTransactions[0], bindingResult);
        userCardServiceImpl.deposit(cardTransactions[1], bindingResult);

    }

    public List<TransactionHistoryEntity> getAllTransactions(Card card, BindingResult bindingResult) {
        UserCardEntity userCardEntity = checkForErrorsNReturnEntity(bindingResult, card);
        return userCardEntity.getTransactionHistoryEntities();
    }


    private boolean compareCards(CardTransaction[] cardTransactions) {
        if (cardTransactions.length != 2) {
            return false;
        }
        if (!cardTransactions[0].getAmountOfMoney().equals(cardTransactions[1].getAmountOfMoney())) {
            return false;
        }
        return !cardTransactions[0].getCardNumber().equals(cardTransactions[1].getCardNumber());
    }

    private TransactionHistoryEntity createTransaction(String operation, UserCardEntity userCardEntity, CardTransaction cardTransaction) {
        TransactionHistoryEntity transactionHistoryEntity = new TransactionHistoryEntity();
        transactionHistoryEntity.setHistoryId(UUID.randomUUID().toString());
        transactionHistoryEntity.setDate(LocalDateTime.now());
        transactionHistoryEntity.setOperationType(operation);
        transactionHistoryEntity.setCardId(userCardEntity);
        transactionHistoryEntity.setAmount(cardTransaction.getAmountOfMoney());
        return transactionHistoryEntity;
    }

    private UserCardEntity checkForErrorsNReturnEntity(BindingResult bindingResult, Card cardTransaction) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectPinOrCardNumberException(bindingResult.getFieldErrors().toString());
        }
        Optional<UserCardEntity> optionalUserCardEntity = userCardRep.findByCardNumber(cardTransaction.getCardNumber());

        if (optionalUserCardEntity.isEmpty()) {
            throw new UserCardNotFoundException("карта не найдена");
        }

        UserCardEntity userCardEntity = optionalUserCardEntity.get();
        if (!allMightyEncoder.matches(cardTransaction.getPin(), userCardEntity.getPin())) {
            throw new UserCardNotFoundException("введён неверный pin");
        }
        return userCardEntity;
    }
}
