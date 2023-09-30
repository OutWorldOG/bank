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
import ru.yaroslav.test.servicies.UserBankService;

@RestController
@RequestMapping("/api/bank")
public class UserBankController {

    private final UserBankService userBankService;

    public UserBankController(UserBankService userBankService) {
        this.userBankService = userBankService;
    }
    @Operation(summary = "create user bank account and card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "create user bank account or/and card", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Номер вашей карты: 4032261128477751")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Поле name не может быть пустым")) })})
    @PostMapping(value = "/account", consumes = "application/json")
    public String createBankAccount(@Valid @RequestBody BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        return userBankService.saveNewBankAccount(bankAccountRequest, bindingResult);
    }
    @Operation(summary = "delete bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "create user bank account or/and card", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Банковский аккаунт удален")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Поле name не может быть пустым")) })})
    @DeleteMapping(value = "/account", consumes = "application/json")
    public String deleteBankAccount(@Valid @RequestBody BankAccountRequest bankAccountRequest, BindingResult bindingResult) {
        userBankService.deleteBankAccount(bankAccountRequest, bindingResult);
        return "Банковский аккаунт удален";
    }
    @Operation(summary = "get all user accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "create user bank account or/and card", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserBankAccount.class)) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Поле name не может быть пустым")) })})
    @PostMapping(value = "/accounts", consumes = "application/json")
    public UserBankAccount getUserBankAccount(@Valid @RequestBody UserBankAccount userBankAccount, BindingResult bindingResult) {
        return userBankService.getUserBankAccount(userBankAccount, bindingResult);
    }
}
