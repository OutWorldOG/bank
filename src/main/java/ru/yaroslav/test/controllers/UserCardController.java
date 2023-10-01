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
import ru.yaroslav.test.dto.CardTransaction;
import ru.yaroslav.test.entities.TransactionHistoryEntity;
import ru.yaroslav.test.servicies.UserCardServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card")
public class UserCardController {

    private final UserCardServiceImpl userCardServiceImpl;

    public UserCardController(UserCardServiceImpl userCardServiceImpl) {
        this.userCardServiceImpl = userCardServiceImpl;
    }
    @Operation(summary = "delete user card", description = "deletes a certain card. Accepts card number and pin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "card deleted", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "карта удалена")) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @DeleteMapping(consumes = "application/json")
    public String deleteUserCard(@Valid @RequestBody Card card, BindingResult bindingResult) {
        userCardServiceImpl.deleteUserCard(card, bindingResult);
        return "карта удалена";
    }
    @Operation(summary = "deposit money", description = "deposits money to a card. Returns amount of money deposited and total amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "money deposited", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Вы занесли 300, текущий баланс: 300")) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @PostMapping(value = "/deposit", consumes = "application/json")
    public String deposit(@Valid @RequestBody CardTransaction cardTransaction, BindingResult bindingResult) {
        return userCardServiceImpl.deposit(cardTransaction, bindingResult);
    }
    @Operation(summary = "withdraw money", description = "withdraws money from a card. Returns amount of money withdrawed and total amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "money withdrawed", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Вы сняли 300, текущий баланс: 600")) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @PostMapping(value = "/withdraw", consumes = "application/json")
    public String withdraw(@Valid @RequestBody CardTransaction cardTransaction, BindingResult bindingResult) {
        return userCardServiceImpl.withdraw(cardTransaction, bindingResult);
    }
    @Operation(summary = "transfer money", description = "method accepts array of 2 cards, amount of money of both cards should be equal. From 1st card money withdrawed, " +
            "to second deposited")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "money transfered", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Средства переведены")) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @PostMapping(value = "/transfer", consumes = "application/json")
    public String transfer(@Valid @RequestBody CardTransaction[] cardTransactions, BindingResult bindingResult) {
        userCardServiceImpl.transfer(cardTransactions, bindingResult);
        return "Средства переведены";
    }
    @Operation(summary = "get all transactions of a card", description = "accepts a card (card number and pin). Returns all transactions associated with card provided")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "transactions provided",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransactionHistoryEntity.class)) }),
            @ApiResponse(responseCode = "404", description = "card not found", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class,  example = "карта не найдена")) }),
            @ApiResponse(responseCode = "400", description = "bad request data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class, example = "Неверный pin")) })})
    @PostMapping(value = "/transactions", consumes = "application/json")
    public List<TransactionHistoryEntity> getAllTransactions(@Valid @RequestBody CardTransaction cardTransaction, BindingResult bindingResult) {
        return userCardServiceImpl.getAllTransactions(cardTransaction, bindingResult);
    }
}
