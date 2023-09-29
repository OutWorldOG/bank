package ru.yaroslav.test.servicies;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.dto.BankAccountRequest;
import ru.yaroslav.test.dto.UserBankAccount;
import ru.yaroslav.test.entities.UserAccountEntity;
import ru.yaroslav.test.entities.UserBankEntity;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.IncorrectNameOrPinException;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.UserBankNotFound;
import ru.yaroslav.test.repositories.UserBankRep;
import utilities.CardMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            UserAccountEntity userAccountEntity = userAccountService.saveNewUserAccount(bankAccountRequest.getPin(), userBankEntity);
            return String.format("Номер вашей карты: %s", userAccountEntity.getAccountNumber());

        }
        UserBankEntity userBankEntity = new UserBankEntity();
        userBankEntity.setUser_id(UUID.randomUUID().toString());
        userBankEntity.setName(bankAccountRequest.getName());
        userBankRep.save(userBankEntity);
        UserAccountEntity userAccountEntity = userAccountService.saveNewUserAccount(bankAccountRequest.getPin(), userBankEntity);
        return String.format("Номер вашей карты: %s", userAccountEntity.getAccountNumber());
    }

    public void deleteBankAccount(BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        Optional<UserBankEntity> optionalBankUser = checkForBindingResultNReturnEntity(bindingResult, bankAccountRequest.getName());

        if (optionalBankUser.isEmpty()) {
            throw new UserBankNotFound("Аккаунт не найден");
        }
        userBankRep.delete(optionalBankUser.get());
    }

    public UserBankAccount getUserBankAccount(String name, BindingResult bindingResult) {
        Optional<UserBankEntity> optionalBankUser = checkForBindingResultNReturnEntity(bindingResult, "Yaroszlav");
        if (optionalBankUser.isEmpty()) {
            throw new UserBankNotFound("Аккаунт не найден");
        }
        UserBankEntity userBankEntity = optionalBankUser.get();
        List<UserAccountEntity> userAccountEntityList = userBankEntity.getUserAccounts();
        UserBankAccount userBankAccount = new UserBankAccount();
        List<Card> cards = new ArrayList<>();
        if (!userAccountEntityList.isEmpty()) {
            for (UserAccountEntity userAccountEntity : userAccountEntityList) {
                cards.add(CardMapper.toCard(userAccountEntity));
            }
        }
        userBankAccount.setName(name);
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
