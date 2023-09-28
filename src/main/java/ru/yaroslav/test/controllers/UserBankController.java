package ru.yaroslav.test.controllers;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.dto.BankAccount;
import ru.yaroslav.test.servicies.UserBankService;

@RestController
@RequestMapping("/api/bank")
public class UserBankController {

    private final UserBankService userBankService;

    public UserBankController(UserBankService userBankService) {
        this.userBankService = userBankService;
    }

    @PostMapping(value = "/account", consumes = "application/json")
    public Card createBankAccount(@Valid @RequestBody BankAccount bankAccount) {
        return userBankService.saveNewBankAccount(bankAccount);
    }

    @DeleteMapping(value = "/account", consumes = "application/json")
    public String deleteBankAccount(@Valid @RequestBody BankAccount bankAccount, BindingResult bindingResult) {
       userBankService.deleteBankAccount(bankAccount);
       return "ok";
    }
}
