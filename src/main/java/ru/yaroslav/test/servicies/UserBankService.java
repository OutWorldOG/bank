package ru.yaroslav.test.servicies;

import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.BankAccountRequest;
import ru.yaroslav.test.dto.UserBankAccount;

public interface UserBankService {
    String saveNewUserBankAccount(BankAccountRequest bankAccountRequest, BindingResult bindingResult);
    void deleteUserBankAccount(UserBankAccount bankAccount, BindingResult bindingResult);
    UserBankAccount getUserBankAccount(UserBankAccount userBankAccountRequest, BindingResult bindingResult);
}
