package ru.yaroslav.test.controllers;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.dto.BankAccountRequest;
import ru.yaroslav.test.dto.UserBankAccount;
import ru.yaroslav.test.servicies.UserBankService;

@RestController
@RequestMapping("/api/bank")
public class UserBankController {

    private final UserBankService userBankService;

    public UserBankController(UserBankService userBankService) {
        this.userBankService = userBankService;
    }

    @PostMapping(value = "/account", consumes = "application/json")
    public String createBankAccount(@Valid @RequestBody BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        return userBankService.saveNewBankAccount(bankAccountRequest, bindingResult);
    }

    @DeleteMapping(value = "/account", consumes = "application/json")
    public String deleteBankAccount(@Valid @RequestBody BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        userBankService.deleteBankAccount(bankAccountRequest, bindingResult);
        return "Банковский аккаунт удален";
    }

    @PostMapping(value = "/accounts", consumes = "application/json")
    public UserBankAccount getUserBankAccount(@Valid @RequestBody String name, BindingResult bindingResult) {
        return userBankService.getUserBankAccount(name, bindingResult);
    }
}
