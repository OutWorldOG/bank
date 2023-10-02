package ru.yaroslav.test.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yaroslav.test.dto.BankAccountRequest;
import ru.yaroslav.test.dto.UserBankAccount;
import ru.yaroslav.test.servicies.UserBankServiceImpl;

@RestController
@RequestMapping("/api/v1/bank")
public class UserBankController {

    private final UserBankServiceImpl userBankServiceImpl;

    public UserBankController(UserBankServiceImpl userBankServiceImpl) {
        this.userBankServiceImpl = userBankServiceImpl;
    }

    @Operation(summary = "create user bank account and card",
            description = "method creates bank account and card. In case bank account already exists," +
                    " creates just card number. Returns card number. Accepts name and pin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "create user bank account or/and card", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Номер вашей карты: 4032261128477751"))}),
            @ApiResponse(responseCode = "400", description = "bad request data", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Поле name не может быть пустым"))})})
    @PostMapping(value = "/account", consumes = "application/json")
    public String createUserBankAccount(@Valid @RequestBody BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        return userBankServiceImpl.saveNewUserBankAccount(bankAccountRequest, bindingResult);
    }

    @Operation(summary = "delete bank account",
            description = "method deletes user bank account. Requires just username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete user bank account", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Банковский аккаунт удален"))}),
            @ApiResponse(responseCode = "400", description = "bad request data", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Поле name не может быть пустым"))})})
    @DeleteMapping(value = "/account", consumes = "application/json")
    public String deleteUserBankAccount(@Valid @RequestBody UserBankAccount bankAccount, BindingResult bindingResult) {
        userBankServiceImpl.deleteUserBankAccount(bankAccount, bindingResult);
        return "Банковский аккаунт удален";
    }

    @Operation(summary = "get all user cards",
            description = "returns all cards associated with bank account. Accepts just username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "create user bank account or/and card", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserBankAccount.class))}),
            @ApiResponse(responseCode = "400", description = "bad request data", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Поле name не может быть пустым"))})})
    @PostMapping(value = "/accounts", consumes = "application/json")
    public UserBankAccount getUserBankAccount(@Valid @RequestBody UserBankAccount userBankAccount, BindingResult bindingResult) {
        return userBankServiceImpl.getUserBankAccount(userBankAccount, bindingResult);
    }
}
