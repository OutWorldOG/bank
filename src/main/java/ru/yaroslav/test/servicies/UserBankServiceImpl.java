package ru.yaroslav.test.servicies;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.CardTransaction;
import ru.yaroslav.test.dto.BankAccountRequest;
import ru.yaroslav.test.dto.UserBankAccount;
import ru.yaroslav.test.entities.UserCardEntity;
import ru.yaroslav.test.entities.UserBankAccountEntity;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.IncorrectNameOrPinException;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.UserBankNotFound;
import ru.yaroslav.test.repositories.UserBankAccountRep;
import ru.yaroslav.test.utilities.CardMapper;

import java.util.*;

@Service
@Transactional
public class UserBankServiceImpl implements UserBankService {

    private final UserBankAccountRep userBankAccountRep;

    private final UserCardService userCardService;

    public UserBankServiceImpl(UserBankAccountRep accountRep, UserCardService userCardService) {
        this.userBankAccountRep = accountRep;
        this.userCardService = userCardService;
    }

    public String saveNewUserBankAccount(BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        Optional<UserBankAccountEntity> optionalBankUser = checkForBindingResultNReturnEntity(bindingResult, bankAccountRequest.getName());

        if (optionalBankUser.isPresent()) {
            UserBankAccountEntity userBankAccountEntity = optionalBankUser.get();
            UserCardEntity userCardEntity = userCardService.saveNewUserCard(bankAccountRequest.getPin(), userBankAccountEntity);
            return String.format("Номер вашей карты: %s", userCardEntity.getCardNumber());

        }
        UserBankAccountEntity userBankAccountEntity = new UserBankAccountEntity();
        userBankAccountEntity.setUser_id(UUID.randomUUID().toString());
        userBankAccountEntity.setName(bankAccountRequest.getName());
        userBankAccountRep.save(userBankAccountEntity);
        UserCardEntity userCardEntity = userCardService.saveNewUserCard(bankAccountRequest.getPin(), userBankAccountEntity);
        return String.format("Номер вашей карты: %s", userCardEntity.getCardNumber());
    }

    public void deleteUserBankAccount(UserBankAccount bankAccount, BindingResult bindingResult) {
        Optional<UserBankAccountEntity> optionalBankUser = checkForBindingResultNReturnEntity(bindingResult, bankAccount.getName());

        if (optionalBankUser.isEmpty()) {
            throw new UserBankNotFound("Аккаунт не найден");
        }
        userBankAccountRep.delete(optionalBankUser.get());
    }

    public UserBankAccount getUserBankAccount(UserBankAccount userBankAccountRequest, BindingResult bindingResult) {
        Optional<UserBankAccountEntity> optionalBankUser = checkForBindingResultNReturnEntity(bindingResult, userBankAccountRequest.getName());

        if (optionalBankUser.isEmpty()) {
            throw new UserBankNotFound("Аккаунт не найден");
        }

        UserBankAccountEntity userBankAccountEntity = optionalBankUser.get();
        List<UserCardEntity> userCardEntityList = userBankAccountEntity.getUserAccounts();
        UserBankAccount userBankAccount = new UserBankAccount();
        List<CardTransaction> cardTransactions = new ArrayList<>();

        if (userCardEntityList != null && !userCardEntityList.isEmpty()) {
            for (UserCardEntity userCardEntity : userCardEntityList) {
                cardTransactions.add(CardMapper.toCard(userCardEntity));
            }
        }
        userBankAccount.setName(userBankAccountRequest.getName());
        userBankAccount.setUserCardTransactions(cardTransactions);
        return userBankAccount;
    }

    private Optional<UserBankAccountEntity> checkForBindingResultNReturnEntity(BindingResult bindingResult, String name) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectNameOrPinException(bindingResult.getFieldErrors().toString());
        }
        return userBankAccountRep.findBankUserEntitiesByName(name);
    }
}
