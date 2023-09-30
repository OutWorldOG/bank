package ru.yaroslav.test.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.entities.TransactionHistoryEntity;
import ru.yaroslav.test.servicies.UserAccountService;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class UserCardController {

    private final UserAccountService userAccountService;

    public UserCardController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
    @Operation(summary = "delete user card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "card deleted", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "карта удалена")) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @DeleteMapping(consumes = "application/json")
    public String deleteUserAccountCard(@Valid @RequestBody Card card, BindingResult bindingResult) {
        userAccountService.deleteUserAccountCard(card, bindingResult);
        return "карта удалена";
    }
    @Operation(summary = "deposit money")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "money deposited", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Вы занесли 300, текущий баланс: 300")) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @PostMapping(value = "/deposit", consumes = "application/json")
    public String deposit(@Valid @RequestBody Card card, BindingResult bindingResult) {
        return userAccountService.deposit(card, bindingResult);
    }
    @Operation(summary = "withdraw money")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "money withdrawed", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Вы сняли 300, текущий баланс: 600")) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @PostMapping(value = "/withdraw", consumes = "application/json")
    public String withdraw(@Valid @RequestBody Card card, BindingResult bindingResult) {
        return userAccountService.withdraw(card, bindingResult);
    }
    @Operation(summary = "transfer money")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "money transfered", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Средства переведены")) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @PostMapping(value = "/transfer", consumes = "application/json")
    public String transfer(@Valid @RequestBody Card[] cards, BindingResult bindingResult) {
        userAccountService.transfer(cards, bindingResult);
        return "Средства переведены";
    }
    @Operation(summary = "get all transactions of a card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "transactions provided",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransactionHistoryEntity.class)) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @PostMapping(value = "/transactions", consumes = "application/json")
    public List<TransactionHistoryEntity> getAllTransactions(@Valid @RequestBody Card card, BindingResult bindingResult) {
        return userAccountService.getAllTransactions(card, bindingResult);
    }
}
