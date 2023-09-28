package ru.yaroslav.test.controllers;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.servicies.UserAccountService;

@RestController
@RequestMapping("/api/account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @DeleteMapping(consumes = "application/json")
    public String deleteBankAccount(@Valid @RequestBody Card card, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldErrors().toString();
        }
        userAccountService.deleteUserBankAccount(card);
        return "ok";
    }

    @PostMapping(value = "/deposit", consumes = "application/json")
    public String deposit(@Valid @RequestBody Card card) {
        return userAccountService.deposit(card);
    }

    @PostMapping(value = "/withdraw", consumes = "application/json")
    public String withdraw(@Valid @RequestBody Card card) {
        return userAccountService.withdraw(card);
    }
}
