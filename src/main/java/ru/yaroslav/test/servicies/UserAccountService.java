package ru.yaroslav.test.servicies;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.entities.TransactionHistoryEntity;
import ru.yaroslav.test.entities.UserAccountEntity;
import ru.yaroslav.test.entities.UserBankEntity;
import ru.yaroslav.test.exceptions_handling.user_account_exception.IncorrectPinOrCardNumberException;
import ru.yaroslav.test.exceptions_handling.user_account_exception.IncorrectTransferCardsException;
import ru.yaroslav.test.exceptions_handling.user_account_exception.UserAccountNotFoundException;
import ru.yaroslav.test.repositories.UserAccountRep;
import utilities.BankCardGenerator;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserAccountService {

    private final UserAccountRep userAccountRep;

    private final TransactionalHistoryService transactionalHistoryService;

    private final PasswordEncoder allMightyEncoder;

    private final UserAccountService userAccountService;

    public UserAccountService(UserAccountRep userAccountRep, TransactionalHistoryService transactionalHistoryService, PasswordEncoder allMightyEncoder, @Lazy UserAccountService userAccountService) {
        this.transactionalHistoryService = transactionalHistoryService;
        this.userAccountRep = userAccountRep;
        this.allMightyEncoder = allMightyEncoder;
        this.userAccountService = userAccountService;
    }

    public UserAccountEntity saveNewUserAccount(String pin, UserBankEntity userBankEntity) {
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.setUserId(userBankEntity);
        userAccountEntity.setAccountId(UUID.randomUUID().toString());
        userAccountEntity.setPin(allMightyEncoder.encode(pin));
        userAccountEntity.setAccountNumber(BankCardGenerator.generateUniqueBankCardNumber());
        userAccountRep.save(userAccountEntity);
        return userAccountEntity;
    }

    public void deleteUserAccountCard(Card card, BindingResult bindingResult) {
        UserAccountEntity userAccountEntity = checkForErrorsNReturnEntity(bindingResult, card);

        userAccountRep.delete(userAccountEntity);
    }

    public String deposit(Card card, BindingResult bindingResult) {
        UserAccountEntity userAccountEntity = checkForErrorsNReturnEntity(bindingResult, card);

        Long OperationResult = card.getAmountOfMoney() + userAccountEntity.getMoney();
        userAccountRep.depositOrWithdraw(card.getCardNumber(), OperationResult);
        transactionalHistoryService.save(createTransaction("deposit", userAccountEntity, card));
        return String.format("Вы занесли %s, текущий баланс: %s", card.getAmountOfMoney(), OperationResult);
    }

    public String withdraw(Card card, BindingResult bindingResult) {
        UserAccountEntity userAccountEntity = checkForErrorsNReturnEntity(bindingResult, card);

        Long operationResult = userAccountEntity.getMoney() - card.getAmountOfMoney();
        if (Long.signum(operationResult) == -1) {
            throw new UserAccountNotFoundException("Недостаточно средств");
        }
        userAccountRep.depositOrWithdraw(userAccountEntity.getAccountNumber(), operationResult);
        transactionalHistoryService.save(createTransaction("withdraw", userAccountEntity, card));
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


    private boolean compareCards(Card[] cards) {
        if (cards.length != 2) {
            return false;
        }
        if (!cards[0].getAmountOfMoney().equals(cards[1].getAmountOfMoney())) {
            return false;
        }
        return !cards[0].getCardNumber().equals(cards[1].getCardNumber());
    }

    private TransactionHistoryEntity createTransaction(String operation, UserAccountEntity userAccountEntity, Card card) {
        TransactionHistoryEntity transactionHistoryEntity = new TransactionHistoryEntity();
        transactionHistoryEntity.setHistoryId(UUID.randomUUID().toString());
        transactionHistoryEntity.setDate(LocalDateTime.now());
        transactionHistoryEntity.setOperationType(operation);
        transactionHistoryEntity.setAccountId(userAccountEntity);
        transactionHistoryEntity.setAmount(card.getAmountOfMoney());
        return transactionHistoryEntity;
    }

    private UserAccountEntity checkForErrorsNReturnEntity(BindingResult bindingResult, Card card) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectPinOrCardNumberException(bindingResult.getFieldErrors().toString());
        }
        Optional<UserAccountEntity> optionalUserAccountEntity = userAccountRep.findByAccountNumber(card.getCardNumber());

        if (optionalUserAccountEntity.isEmpty()) {
            throw new UserAccountNotFoundException("карта не найдена");
        }

        UserAccountEntity userAccountEntity = optionalUserAccountEntity.get();
        if (!allMightyEncoder.matches(card.getPin(), userAccountEntity.getPin())) {
            throw new UserAccountNotFoundException("введён неверный pin");
        }
        return userAccountEntity;
    }
}
