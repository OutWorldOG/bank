package ru.yaroslav.test.servicies;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.dto.BankAccount;
import ru.yaroslav.test.entities.UserBankEntity;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.IncorrectNameOrPinException;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.UserBankNotFound;
import ru.yaroslav.test.repositories.UserBankRep;
import utilities.CardMapper;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserBankService {

    private final UserBankRep userBankRep;

    private final UserAccountService userAccountService;

    public UserBankService(UserBankRep accountRep, UserAccountService userAccountService) {
        this.userBankRep = accountRep;
        this.userAccountService = userAccountService;
    }

    @Transactional
    public Card saveNewBankAccount(BankAccount bankAccount, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectNameOrPinException(bindingResult.getFieldErrors().toString());
        }
        Optional<UserBankEntity> optionalBankUser = userBankRep.findBankUserEntitiesByName(bankAccount.getName());
        if (optionalBankUser.isPresent()) {
            UserBankEntity userBankEntity = optionalBankUser.get();
            return CardMapper.toCard(userAccountService.saveNewUserAccount(bankAccount.getPin(), userBankEntity), userBankEntity.getName());

        }
        UserBankEntity userBankEntity = new UserBankEntity();
        userBankEntity.setUser_id(UUID.randomUUID().toString());
        userBankEntity.setName(bankAccount.getName());
        userBankRep.save(userBankEntity);
        return CardMapper.toCard(userAccountService.saveNewUserAccount(bankAccount.getPin(), userBankEntity), userBankEntity.getName());
    }

    @Transactional
    public void deleteBankAccount(BankAccount bankAccount, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectNameOrPinException(bindingResult.getFieldErrors().toString());
        }
        Optional<UserBankEntity> optionalBankUser = userBankRep.findBankUserEntitiesByName(bankAccount.getName());
        if (optionalBankUser.isEmpty()) {
            throw new UserBankNotFound("Аккаунт не найден");
        }
    }
}
