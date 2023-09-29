package ru.yaroslav.test.controllers;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.servicies.UserAccountService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @DeleteMapping(consumes = "application/json")
    public String deleteUserAccountCard(@Valid @RequestBody Card card, BindingResult bindingResult) {
        userAccountService.deleteUserAccountCard(card, bindingResult);
        return "карта удалена";
    }

    @PostMapping(value = "/deposit", consumes = "application/json")
    public String deposit(@Valid @RequestBody Card card, BindingResult bindingResult) {
        return userAccountService.deposit(card, bindingResult);
    }

    @PostMapping(value = "/withdraw", consumes = "application/json")
    public String withdraw(@Valid @RequestBody Card card, BindingResult bindingResult) {
        return userAccountService.withdraw(card, bindingResult);
    }

    @PostMapping(value = "/transfer", consumes = "application/json")
    public String transfer(@Valid @RequestBody Card[] cards, BindingResult bindingResult) {
        userAccountService.transfer(cards, bindingResult);
        return "Средства переведены";
    }
}
