package ru.yaroslav.test.servicies;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.dto.BankAccountRequest;
import ru.yaroslav.test.dto.UserBankAccount;
import ru.yaroslav.test.entities.UserCardEntity;
import ru.yaroslav.test.entities.UserBankEntity;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.IncorrectNameOrPinException;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.UserBankNotFound;
import ru.yaroslav.test.repositories.UserBankRep;
import ru.yaroslav.test.utilities.CardMapper;

import java.util.*;

@Service
@Transactional
public class UserBankService {

    private final UserBankRep userBankRep;

    private final UserAccountService userAccountService;

    public UserBankService(UserBankRep accountRep, UserAccountService userAccountService) {
        this.userBankRep = accountRep;
        this.userAccountService = userAccountService;
    }

    public String saveNewBankAccount(BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        Optional<UserBankEntity> optionalBankUser = checkForBindingResultNReturnEntity(bindingResult, bankAccountRequest.getName());

        if (optionalBankUser.isPresent()) {
            UserBankEntity userBankEntity = optionalBankUser.get();
            UserCardEntity userCardEntity = userAccountService.saveNewUserAccount(bankAccountRequest.getPin(), userBankEntity);
            return String.format("Номер вашей карты: %s", userCardEntity.getCardNumber());

        }
        UserBankEntity userBankEntity = new UserBankEntity();
        userBankEntity.setUser_id(UUID.randomUUID().toString());
        userBankEntity.setName(bankAccountRequest.getName());
        userBankRep.save(userBankEntity);
        UserCardEntity userCardEntity = userAccountService.saveNewUserAccount(bankAccountRequest.getPin(), userBankEntity);
        return String.format("Номер вашей карты: %s", userCardEntity.getCardNumber());
    }

    public void deleteBankAccount(BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        Optional<UserBankEntity> optionalBankUser = checkForBindingResultNReturnEntity(bindingResult, bankAccountRequest.getName());

        if (optionalBankUser.isEmpty()) {
            throw new UserBankNotFound("Аккаунт не найден");
        }
        userBankRep.delete(optionalBankUser.get());
    }

    public UserBankAccount getUserBankAccount(UserBankAccount userBankAccountRequest, BindingResult bindingResult) {
        Optional<UserBankEntity> optionalBankUser = checkForBindingResultNReturnEntity(bindingResult, userBankAccountRequest.getName());

        if (optionalBankUser.isEmpty()) {
            throw new UserBankNotFound("Аккаунт не найден");
        }

        UserBankEntity userBankEntity = optionalBankUser.get();
        List<UserCardEntity> userCardEntityList = userBankEntity.getUserAccounts();
        UserBankAccount userBankAccount = new UserBankAccount();
        List<Card> cards = new ArrayList<>();

        if (!userCardEntityList.isEmpty()) {
            for (UserCardEntity userCardEntity : userCardEntityList) {
                cards.add(CardMapper.toCard(userCardEntity));
            }
        }
        userBankAccount.setName(userBankAccountRequest.getName());
        userBankAccount.setUserCards(cards);
        return userBankAccount;
    }

    private Optional<UserBankEntity> checkForBindingResultNReturnEntity(BindingResult bindingResult, String name) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectNameOrPinException(bindingResult.getFieldErrors().toString());
        }
        return userBankRep.findBankUserEntitiesByName(name);
    }
}
